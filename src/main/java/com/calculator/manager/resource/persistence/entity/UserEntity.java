package com.calculator.manager.resource.persistence.entity;

import com.calculator.manager.domain.model.user.StatusType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "users")
@Getter
@Setter
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String username;
    String password;
    @Enumerated(EnumType.STRING)
    StatusType status;
}
