package com.llmb.prompt.base;

/**
 * 数据模板
 *
 * @author LiangTao
 * @date 2023年05月25 16:37
 **/
public interface LLMPromptTemplate<M extends LLMMessage> extends LLMInputParse<M>,LLMOutputParse<M>{

}
