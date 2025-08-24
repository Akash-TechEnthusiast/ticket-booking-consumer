package com.ticket.booking.consumer.entity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "failed_tickets")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FailedTicket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // AUTO_INCREMENT
    private Long id;

    @Column(name = "topic", length = 255)
    private String topic;

    @Column(name = "partition_id")
    private Integer partitionId;

    @Column(name = "offset")
    private Long offset;

    @Column(name = "keyvalue", length = 255)
    private String keyValue;

    @Lob  // large text column
    @Column(name = "payload")
    private String payload;

    @Lob
    @Column(name = "error_message")
    private String errorMessage;

    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;
}
