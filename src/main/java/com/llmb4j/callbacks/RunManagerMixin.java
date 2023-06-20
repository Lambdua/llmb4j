package com.llmb4j.callbacks;

import java.util.Map;
import java.util.UUID;

/**
 * @author LiangTao
 * @date 2023年06月07 09:47
 **/
public interface RunManagerMixin {
    default void onText(String text, UUID runId, UUID parentRunId, Map<String, Object> kwargs) {

    }

}
