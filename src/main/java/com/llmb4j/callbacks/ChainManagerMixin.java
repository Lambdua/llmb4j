package com.llmb4j.callbacks;

import com.llmb4j.common.AgentAction;
import com.llmb4j.common.AgentFinish;

import java.util.Map;
import java.util.UUID;

/**
 * @author LiangTao
 * @date 2023年06月07 09:42
 **/
public interface ChainManagerMixin {
    default void onChainEnd(Map<String, Object> outputs, UUID runId, UUID parentRunId, Map<String, Object> kwargs){

    }
    default void onChainError(Exception error, UUID runId, UUID parentRunId, Map<String, Object> kwargs){

    }
    default void onAgentAction(AgentAction action, UUID runId, UUID parentRunId, Map<String, Object> kwargs){

    }
    default void onAgentFinish(AgentFinish finish, UUID runId, UUID parentRunId, Map<String, Object> kwargs){

    }
}
