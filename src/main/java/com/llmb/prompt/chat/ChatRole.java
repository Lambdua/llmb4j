package com.llmb.prompt.chat;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ChatRole{
    SYSTEM("system") ,
    USER("user") ,
    AI("assistant")
    ;


    final String value;
}
