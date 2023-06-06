package com.llmb.prompt.base;

/**
 * 输出解析器
 *
 * @author LiangTao
 * @date 2023年05月25 16:06
 **/
@FunctionalInterface
public interface LLMOutputParse<M extends LLMMessage,O>{
    O parse(M msg);

}
