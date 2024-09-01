package com.calculator.manager.resource.persistence.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "record")
@Getter
@Setter
public class RecordEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "operation_id", referencedColumnName = "id")
    OperationEntity operation;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    UserEntity user;
    Double amount;
    @Column(name = "user_balance")
    Double userBalance;
    @Column(name = "operation_response")
    String operationResponse;
    LocalDateTime date;
    Boolean inactive;
}
