package com.llmb.prompt.base;

/**
 * 和llm模型通信的基础接口
 * @author LiangTao
 * @date 2023年05月25 16:03
 **/
public interface  LLMMessage{
    /**
     * 通讯的消息内容
     * @author liangtao
     * @date 2023/5/31
     **/
    String getMsg();
}
