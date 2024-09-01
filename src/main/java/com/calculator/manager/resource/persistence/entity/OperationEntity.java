package com.calculator.manager.resource.persistence.entity;

import com.calculator.manager.domain.model.operation.OperationType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "operation")
@Getter
@Setter
public class OperationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @Enumerated(EnumType.STRING)
    OperationType type;
    Double cost;
}
