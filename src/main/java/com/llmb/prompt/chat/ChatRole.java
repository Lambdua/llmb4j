package com.llmb.prompt.chat;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum ChatRole{
    SYSTEM("system") ,
    USER("user") ,
    AI("assistant")
    ;


    String name;
}
