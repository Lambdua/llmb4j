package com.llmb.log;

import cn.hutool.core.lang.ansi.AnsiColor;
import cn.hutool.core.lang.ansi.AnsiElement;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum ChatStyle {
    INPUT("input", AnsiColor.YELLOW),

    OUTPUT("output", AnsiColor.BLUE)

    ;

    public String type;

    public AnsiElement contentStyles;


    public static ChatStyle fromType(String type) {
        for (ChatStyle chatStyle : ChatStyle.values()) {
            if (chatStyle.type.equals(type)) {
                return chatStyle;
            }
        }
        return null;
    }
}
