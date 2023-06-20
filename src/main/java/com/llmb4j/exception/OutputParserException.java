package com.llmb4j.exception;

/**
 * 输出解析异常
 * @author LiangTao
 * @date 2023年06月06 15:05
 **/
public class OutputParserException extends RuntimeException{
    private final String llmSendMsg;

    private final String llmOutMsg;

    public OutputParserException(String llmSendMsg, String llmOutMsg) {
        this.llmSendMsg = llmSendMsg;
        this.llmOutMsg = llmOutMsg;
    }

}
