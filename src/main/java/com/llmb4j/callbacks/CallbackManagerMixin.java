package com.llmb4j.callbacks;

import com.llmb4j.prompt.base.RoleMessage;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @author LiangTao
 * @date 2023年06月07 09:46
 **/
public interface CallbackManagerMixin {
    default void onLlmStart(Map<String, Object> serialized, List<String> prompts, UUID runId, UUID parentRunId, Map<String, Object> kwargs){

    }

    default void onChatModelStart(Map<String, Object> serialized, List<? extends RoleMessage> messages, UUID runId, UUID parentRunId, Map<String, Object> kwargs){

    }

    default void onChainStart(Map<String, Object> serialized, Map<String, Object> inputs, UUID runId, UUID parentRunId, Map<String, Object> kwargs){

    }
    default void onToolStart(Map<String, Object> serialized, String inputStr, UUID runId, UUID parentRunId, Map<String, Object> kwargs){

    }
}
