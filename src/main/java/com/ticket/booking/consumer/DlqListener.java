package com.ticket.booking.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ticket.booking.consumer.entity.FailedTicket;
import com.ticket.booking.consumer.entity.TicketBookedEvent;
import com.ticket.booking.consumer.repo.FailedTicketRepository;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
public class DlqListener {

    private final ObjectMapper mapper;

    private final FailedTicketRepository failedTicketRepository;

    public DlqListener(ObjectMapper mapper, FailedTicketRepository failedTicketRepository) {
        this.mapper = mapper;
        this.failedTicketRepository = failedTicketRepository;
    }


    @KafkaListener(
            topics = "ticket-booked-dlq",
            groupId = "dlq-group",
            containerFactory = "dlqKafkaListenerFactory"
    )
    public void consumeDlq(ConsumerRecord<String, TicketBookedEvent> record) throws JsonProcessingException {
        // Access topic, partition, offset
        String topic = record.topic();
        int partition = record.partition();
        long offset = record.offset();

        // Access key and value
        String key = record.key();
        TicketBookedEvent value = record.value();

        System.err.println("DLQ Received:");
        System.err.println("Topic: " + topic + ", Partition: " + partition + ", Offset: " + offset);
        System.err.println("Key: " + key + ", Value: " + value);

        FailedTicket ft=new FailedTicket();
        ft.setPayload(mapper.writeValueAsString(value));
        ft.setOffset(offset);
        ft.setPartitionId(partition);
        ft.setErrorMessage(null);
        ft.setTopic(topic);
        ft.setKeyValue(key);

        failedTicketRepository.save(ft);

    }
}
