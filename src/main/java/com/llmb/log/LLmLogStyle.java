package com.llmb.log;

import cn.hutool.core.lang.ansi.AnsiColor;
import cn.hutool.core.lang.ansi.AnsiElement;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum LLmLogStyle {
    INPUT("input", AnsiColor.YELLOW),

    OUTPUT("output", AnsiColor.BLUE),

    CONFIG("llmConfig", AnsiColor.RED),

    UNKNOWN("unknown", AnsiColor.WHITE)

    ;

    public String type;

    public AnsiElement contentStyles;


    public static LLmLogStyle fromType(String type) {
        for (LLmLogStyle LLmLogStyle : LLmLogStyle.values()) {
            if (LLmLogStyle.type.equals(type)) {
                return LLmLogStyle;
            }
        }
        return UNKNOWN;

    }
}
