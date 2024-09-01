package com.calculator.manager.domain.model;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Token {
    public Token(String token) {
        this.token = token;
        this.type = "Bearer";
    }

    private String token;
    private String type;
}
