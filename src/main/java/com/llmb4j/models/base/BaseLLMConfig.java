package com.llmb4j.models.base;

import com.llmb4j.callbacks.BaseCallbackHandler;
import lombok.Data;

import java.util.List;

/**
 * llm配置基类
 * @author LiangTao
 * @date 2023年06月15 09:55
 **/
@Data
public class BaseLLMConfig {

    private String defaultCompletionModelName;

    private String defaultChatModelName;

    private String defaultEmbeddingModelName;

    /**
     * llm的回调处理器集合
     */
    private List<BaseCallbackHandler> callbackHandlers;

}
