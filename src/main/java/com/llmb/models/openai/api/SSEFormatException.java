package com.llmb.models.openai.api;

/**
 * Exception indicating a SSE format error
 */
public class SSEFormatException extends Throwable {
    public SSEFormatException(String msg) {
        super(msg);
    }
}
