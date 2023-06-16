package com.llmb4j.prompt.base;

import java.util.Map;

/**
 * llm交互的输入内容基础接口
 *
 * @author LiangTao
 * @date 2023年05月25 16:04
 **/
public interface LLMInputParse<M> {
    M format(String template, Record argsPayload);

    M format(String template, Map<String, Object> argsPayload);

}
