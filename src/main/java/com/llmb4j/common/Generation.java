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

import java.util.Map;

/**
 * @author LiangTao
 * @date 2023年05月25 16:03
 **/
@Data
@Builder
public class Generation {

    /**
     * llm模型的输出文本内容
     */
    private String text;

    /**
     * 提供商的原始生成信息响应。可能包括诸如finishReason之类的内容（例如在OpenAI中）
     */
    private Map<String, Object> generationInfo;

}
