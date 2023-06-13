package com.llmb4j.prompt.base;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ChatRole{
    SYSTEM("System") ,
    HUMAN("Human") ,
    AI("Ai"),
    CHAT("Chat")
    ;


    final String value;
}
