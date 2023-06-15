package com.llmb4j.models.openai.service;

/**
 * Exception indicating a SSE format error
 */
public class SSEFormatException extends Throwable {
    public SSEFormatException(String msg) {
        super(msg);
    }
}
