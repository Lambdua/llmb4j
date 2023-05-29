package com.llmb.prompt.base;

/**
 * 输出解析器
 *
 * @author LiangTao
 * @date 2023年05月25 16:06
 **/
public interface LLMOutputParse<IN extends LLMMessage> {
    default Object parse(IN msg){
        return msg.getMsg();
    }

}
