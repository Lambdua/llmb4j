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


import com.llmb4j.common.Generation;
import com.llmb4j.common.LLMResult;
import com.llmb4j.prompt.base.RoleMessage;
import com.llmb4j.util.ChatMsgUtil;
import com.llmb4j.util.PyScript;

import java.util.Collections;
import java.util.List;


/**
 * @author LiangTao
 * @date 2023年06月07 13:33
 **/

public interface BaseLanguageModel<C extends BaseLLMConfig> {


    C getConfig();


    /**
     * 使用llm进行文本补全
     *
     * @return com.llmb4j.common.LLMResult
     * @author liangtao
     * @date 2023/6/15
     **/
    default <P extends BaseLLMCompletionPayload> LLMResult generateCompletion(P payload) {
        throw new UnsupportedOperationException("generateCompletion in " + llmType() + " is not supported");
    }


    /**
     * 使用llm进行对话文本生成，默认调用generateCompletion来实现。
     *
     * @param payload 对话荷载
     * @param <P>     荷载类型
     * @return com.llmb4j.common.LLMResult
     * @author liangtao
     * @date 2023/6/15
     */
    default <P extends BaseLLMChatPayload> List<? extends RoleMessage> generateChat(P payload) {
        BaseLLMCompletionPayload completionPayload = new BaseLLMCompletionPayload();
        completionPayload.setModelName(getConfig().getDefaultCompletionModelName());
        completionPayload.setPrompts(List.of(ChatMsgUtil.getBufferString(payload.chatHistory)));
        completionPayload.setStop(payload.stop);
        completionPayload.setCallbackHandler(payload.callbackHandler);
        return ChatMsgUtil.llmResultToRoleMessage(generateCompletion(completionPayload));
    }


    /**
     * 补全文本。
     *
     * @param text 文本
     * @param stop stop
     * @return java.lang.String
     * @author liangtao
     * @date 2023/6/15
     **/
    default String predictCompletion(String text, List<String> stop) {
        BaseLLMCompletionPayload payload = new BaseLLMCompletionPayload();
        payload.setPrompts(Collections.singletonList(text));
        payload.setStream(false);
        payload.setStop(stop);
        payload.setVerbose(false);
        payload.setModelName(getConfig().getDefaultCompletionModelName());
        return generateCompletion(payload).getGenerations().get(0).stream().map(Generation::getText).reduce("", String::concat);
    }

    /**
     * llm的返回类型。
     */
    String llmType();


    default Integer getNumTokens(String text) {
        return getNumTokens(text, getConfig().getDefaultCompletionModelName());
    }

    /**
     * 获取模型的token长度
     *
     * @param text token
     */
    default Integer getNumTokens(String text, String modelName) {
        return getTokenIds(text, modelName).size();
    }


    default Integer getNumTokensFromMessages(List<RoleMessage> messages) {
        return getNumTokensFromMessages(messages, getConfig().getDefaultChatModelName());
    }

    /**
     * 获取messages的token长度
     *
     * @param messages messages
     * @return token长度
     */
    default Integer getNumTokensFromMessages(List<? extends RoleMessage> messages, String modelName) {
        return getNumTokens(ChatMsgUtil.getBufferString(messages), modelName);
    }

    /**
     * 获取token的分组
     *
     * @param text token
     * @return token的id
     */
    default List<Integer> getTokenIds(String text, String modelName) {
        return PyScript.tokenIds(text, modelName);
    }


}
