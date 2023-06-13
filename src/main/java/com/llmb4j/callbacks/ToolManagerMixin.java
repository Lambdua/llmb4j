package com.llmb4j.callbacks;

import java.util.Map;
import java.util.UUID;

/**
 * @author LiangTao
 * @date 2023年06月07 09:46
 **/
public interface ToolManagerMixin {
    default void onToolEnd(String output, UUID runId, UUID parentRunId, Map<String, Object> kwargs){

    }
    default void onToolError(Exception error, UUID runId, UUID parentRunId, Map<String, Object> kwargs){

    }
}
