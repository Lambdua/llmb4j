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

package com.llmb4j.common;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * @author LiangTao
 * @date 2023年05月25 16:03
 **/
@Data
@Builder
@NoArgsConstructor
public class Generation {

    public Generation(String text, Map<String, Object> generationInfo) {
        this.text = text;
        this.generationInfo = generationInfo;
    }

    /**
     * llm模型的输出文本内容
     */
    private String text;

    /**
     * 提供商的原始生成信息响应。可能包括诸如finishReason之类的内容（例如在OpenAI中）
     * totalTokens: 生成的令牌总数
     * completionTokens: 完成的令牌总数(响应)
     * promptTokens: 提示令牌总数(请求）
     * finishReason: 完成原因
     * functionCall: 函数调用
     */
    private Map<String, Object> generationInfo;

}
