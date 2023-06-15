package com.llmb4j.models.base;

import lombok.Data;

import java.util.List;

/**
 * llm补全文本的荷载参数基类
 * @author LiangTao
 * @date 2023年06月15 09:58
 **/
@Data
public class BaseLLMCompletionPayload extends BaseLLMPayload{

    /**
     * 要补全的文本集合
     */
    private List<String> prompts;
}
