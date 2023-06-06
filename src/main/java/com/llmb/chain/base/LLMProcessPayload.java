package com.llmb.chain.base;

import com.llmb.memory.LLMMemory;
import com.llmb.models.base.LLmConfig;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * chain执行时的载荷信息
 * @author LiangTao
 * @date 2023年06月05 15:43
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LLMProcessPayload {

    /**
     * 会话id
     */
    private String sessionId;

    /**
     * 上下文存储
     */
    private LLMMemory memory;

    /**
     * 生成模板的目标对象
     */
    private Object templateTarget;

    /**
     * 模板的相关参数，优先从这里取，取不到再从memory取
     */
    private Record paramsRecord;

    /**
     * 和llm交互的配置
     */
    private LLmConfig chatConfig;
}
