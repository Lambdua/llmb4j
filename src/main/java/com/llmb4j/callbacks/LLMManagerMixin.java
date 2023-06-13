package com.llmb4j.callbacks;

import com.llmb4j.common.LLMResult;

import java.util.Map;
import java.util.UUID;

/**
 *
 * @author LiangTao
 * @date 2023年06月07 09:17
 **/
public interface LLMManagerMixin {
    /**
     * 使用新的LLM令牌运行。仅在启用流时可用。
     * @author liangtao
     * @date 2023/6/7
     **/
    default void onLlmNewToken(String token, UUID runId, UUID parentRunId, Map<String, Object> kwargs) {

    }

    default void onLlmEnd(LLMResult response, UUID runId, UUID parentRunId, Map<String, Object> kwargs) {

    }

    default void onLlmError(Exception error, UUID runId, UUID parentRunId, Map<String, Object> kwargs) {

    }
}
