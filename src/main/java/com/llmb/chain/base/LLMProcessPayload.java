package com.llmb.chain.base;

import com.llmb.memory.LLMMemory;
import com.llmb.models.base.LLmConfig;
import lombok.Getter;
import lombok.Setter;

/**
 * @author LiangTao
 * @date 2023年06月05 15:43
 **/
@Getter
@Setter
public class LLMProcessPayload {

    private LLMMemory memory;

    private String templateTarget;

    private Record paramsRecord;

    private LLmConfig chatConfig;
}
