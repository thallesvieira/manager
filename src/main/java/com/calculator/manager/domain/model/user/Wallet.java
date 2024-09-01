package com.calculator.manager.domain.model.user;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Wallet {
    Long id;
    User user;
    Double balance;
}
