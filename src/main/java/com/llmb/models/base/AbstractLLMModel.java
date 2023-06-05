package com.llmb.models.base;

import com.llmb.prompt.base.LLMMessage;
import lombok.extern.slf4j.Slf4j;

/**
 * @author LiangTao
 * @date 2023年06月01 11:46
 **/
@Slf4j
public abstract class AbstractLLMModel<C extends LLmConfig, M extends LLMMessage<?>> implements LLMModel<C, M> {



}
