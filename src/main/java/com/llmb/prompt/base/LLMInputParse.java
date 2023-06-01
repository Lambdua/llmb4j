package com.llmb.prompt.base;

import java.util.Map;

/**
 * llm交互的输入内容基础接口
 *
 * @author LiangTao
 * @date 2023年05月25 16:04
 **/
public interface LLMInputParse<M extends LLMMessage<?>,T>{

    M toMsg(T target,Record argsPayload);

    M toMsg(T target, Map<String,Object> argsPayload);

}
