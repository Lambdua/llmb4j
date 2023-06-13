/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.llmb4j.models.base;


import com.llmb4j.callbacks.BaseCallbackHandler;
import com.llmb4j.common.LLMResult;
import com.llmb4j.prompt.base.ChatRole;
import com.llmb4j.prompt.base.PromptValue;
import com.llmb4j.prompt.base.RoleMessage;
import com.llmb4j.util.PyScript;

import java.util.List;


/**
 * @author LiangTao
 * @date 2023年06月07 13:33
 **/

public interface BaseLanguageModel {

    /**
     * 是否打印日志。
     */
    boolean isVerbose();


    /**
     * 生成LLMResult。
     */
    default LLMResult generate(List<PromptValue> prompts, List<String> stop, BaseCallbackHandler callbackHandler) {
        return generateCompletion(prompts.stream().map(PromptValue::toString).toList(), stop, callbackHandler);
    }


    default LLMResult generateCompletion(List<String> prompts, List<String> stop, BaseCallbackHandler callbackHandler){
        throw new UnsupportedOperationException("generateCompletion in "+llmType()+" is not supported");
    }


    default LLMResult generateChat(List<RoleMessage> prompts, List<String> stop, BaseCallbackHandler callbackHandler) {
        return generateCompletion(List.of(RoleMessage.getBufferString(prompts)), stop, callbackHandler);
    }


    default String predict(PromptValue promptValue, List<String> stop) {
        return predictCompletion(promptValue.toString(), stop);
    }

    /**
     * 根据文本预测文本。
     */
    default String predictCompletion(String text, List<String> stop){
        throw new UnsupportedOperationException("predictCompletion in "+llmType()+" is not supported");
    }

    /**
     * Predict message from messages.
     */
    default RoleMessage predictChat(List<RoleMessage> messages, List<String> stop) {
        String aiMsg = predictCompletion(RoleMessage.getBufferString(messages), stop);
        return new RoleMessage(aiMsg, ChatRole.AI);
    }

    /**
     * llm的返回类型。
     */
    String llmType();

    /**
     * 使用的模型名称
     */
    String getModelName();

    /**
     * 获取模型的token长度
     *
     * @param text token
     */
    default Integer getNumTokens(String text) {
        return getTokenIds(text).size();
    }

    /**
     * 获取messages的token长度
     *
     * @param messages messages
     * @return token长度
     */
    default Integer getNumTokensFromMessages(List<RoleMessage> messages) {
        return getNumTokens(RoleMessage.getBufferString(messages));
    }

    /**
     * 获取token的分组
     *
     * @param text token
     * @return token的id
     */
    default List<Integer> getTokenIds(String text) {
        return PyScript.tokenIds(text, getModelName());
    }


}
