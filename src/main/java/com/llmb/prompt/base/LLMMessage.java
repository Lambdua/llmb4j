package com.llmb.prompt.base;

/**
 * 和llm模型通信的基础接口
 * @author LiangTao
 * @date 2023年05月25 16:03
 **/
public interface  LLMMessage<S>{
    /**
     * 通讯的消息内容
     * @author liangtao
     * @date 2023/5/31
     * @return java.lang.Object 这里是object，后续可能会有多模态
     **/
    S getMsg();

    default String getMsgStr(){
        return getMsg().toString();
    }

}
