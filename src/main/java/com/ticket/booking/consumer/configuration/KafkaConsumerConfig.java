package com.ticket.booking.consumer.configuration;


import com.ticket.booking.consumer.entity.TicketBookedEvent;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.*;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.util.backoff.FixedBackOff;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConsumerConfig {


    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, TicketBookedEvent> kafkaListenerContainerFactory(
            ConsumerFactory<String, TicketBookedEvent> consumerFactory, KafkaTemplate<String, TicketBookedEvent> kafkaTemplat) {
        ConcurrentKafkaListenerContainerFactory<String, TicketBookedEvent> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory);
        factory.setCommonErrorHandler(new DefaultErrorHandler(
                new DeadLetterPublishingRecoverer(kafkaTemplat,
                        (r, e) -> new TopicPartition("ticket-booked-dlq", r.partition())),
                new FixedBackOff(1000L, 2L) // retry twice with 1s delay
        ));
        return factory;
    }

    @Bean
    public ConsumerFactory<String, TicketBookedEvent> consumerFactory() {
        Map<String, Object> config = new HashMap<>();
        config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        config.put(ConsumerConfig.GROUP_ID_CONFIG, "ticket-processor");
        config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        config.put(JsonDeserializer.TRUSTED_PACKAGES, "com.ticket.booking.consumer.entity");
        config.put(JsonDeserializer.VALUE_DEFAULT_TYPE, "com.ticket.booking.consumer.entity.TicketBookedEvent");
        return new DefaultKafkaConsumerFactory<>(config);
    }


    @Bean
    public ProducerFactory<String, TicketBookedEvent> producerFactory() {
        Map<String, Object> config = new HashMap<>();
        config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, org.apache.kafka.common.serialization.StringSerializer.class);
        config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, org.springframework.kafka.support.serializer.JsonSerializer.class);

        // Optional: to trust all packages for JSON serialization
        config.put(JsonSerializer.ADD_TYPE_INFO_HEADERS, false);

        return new DefaultKafkaProducerFactory<>(config);
    }

    @Bean
    public KafkaTemplate<String, TicketBookedEvent> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }


    @Bean
    public ConsumerFactory<String, TicketBookedEvent> dlqConsumerFactory() {
        Map<String, Object> config = new HashMap<>();
        config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        config.put(ConsumerConfig.GROUP_ID_CONFIG, "dlq-group");
        config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        config.put(JsonDeserializer.TRUSTED_PACKAGES, "com.ticket.booking.consumer.entity");
        config.put(JsonDeserializer.VALUE_DEFAULT_TYPE,
                "com.ticket.booking.consumer.entity.TicketBookedEvent");
        return new DefaultKafkaConsumerFactory<>(config);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, TicketBookedEvent> dlqKafkaListenerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, TicketBookedEvent> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(dlqConsumerFactory());
        return factory;
    }



}
