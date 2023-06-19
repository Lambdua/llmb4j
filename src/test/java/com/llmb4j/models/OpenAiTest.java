package com.llmb4j.models;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.map.MapUtil;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.llmb4j.callbacks.BaseCallbackHandler;
import com.llmb4j.common.Generation;
import com.llmb4j.common.LLMResult;
import com.llmb4j.models.openai.*;
import com.llmb4j.models.openai.base.OpenAiModels;
import com.llmb4j.models.openai.completion.chat.ChatFunction;
import com.llmb4j.models.openai.completion.chat.ChatFunctionCall;
import com.llmb4j.prompt.base.ChatRole;
import com.llmb4j.prompt.base.RoleMessage;
import com.llmb4j.util.ChatMsgUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.*;

/**
 * @author LiangTao
 * @date 2023年05月30 10:46
 **/
@Slf4j
public class OpenAiTest {
    final OpenAiLLM openAiLLM = new OpenAiLLM();

    @Test
    public void streamChatTest() {
        OpenAiPromptTemplate chatPromptTemplate = new OpenAiPromptTemplate();
        String template = "你好，我的名字叫：{$name},我应该叫你什么？";
        record payload(String name, ChatRole role) {
        }
        OpenAiRoleMessage request = chatPromptTemplate.format(template, new payload("小明", ChatRole.HUMAN));
        OpenAiLLmChatPayload chatPayload = OpenAiLLM.withDefaultChatModel();
        chatPayload.setChatHistory(Collections.singletonList(request));
        chatPayload.setStream(true);
        chatPayload.setVerbose(true);
        StringBuilder sb = new StringBuilder();
        chatPayload.setCallbackHandler(new BaseCallbackHandler() {
            @Override
            public void onLlmStart(Map<String, Object> serialized, List<String> prompts, UUID runId, UUID parentRunId, Map<String, Object> kwargs) {
                log.info("llmStart:-->" + serialized.toString());
            }

            @Override
            public void onLlmNewToken(String token, UUID runId, UUID parentRunId, Map<String, Object> kwargs) {
                sb.append(token);
            }

            @Override
            public void onLlmError(Exception error, UUID runId, UUID parentRunId, Map<String, Object> kwargs) {
                log.error("onLlmError:{}", error.getMessage());
            }

            @Override
            public void onLlmEnd(LLMResult response, UUID runId, UUID parentRunId, Map<String, Object> kwargs) {
                log.info("onLlmEnd:{}", response.toString());
            }
        });

        List<? extends RoleMessage> roleMessages = openAiLLM.generateChat(chatPayload);
        Assert.notEmpty(roleMessages);
        Assert.equals(sb.toString(), roleMessages.get(0).getContent());
    }

    @Test
    public void fullChatTest() {
        OpenAiPromptTemplate chatPromptTemplate = new OpenAiPromptTemplate();
        String template = "你好，我的名字叫：{$name},我应该叫你什么？";
        record payload(String name, ChatRole role) {
        }
        OpenAiRoleMessage request = chatPromptTemplate.format(template, new payload("小明", ChatRole.HUMAN));
        OpenAiLLmChatPayload chatPayload = OpenAiLLM.withDefaultChatModel();
        chatPayload.setChatHistory(Collections.singletonList(request));
        chatPayload.setStream(false);
        chatPayload.setVerbose(true);
        chatPayload.setN(2);
        chatPayload.setTemperature(1.0D);
        chatPayload.setCallbackHandler(new BaseCallbackHandler() {
            @Override
            public void onLlmEnd(LLMResult response, UUID runId, UUID parentRunId, Map<String, Object> kwargs) {
                log.info("onLlmEnd:{}", response.toString());
            }
        });
        List<? extends RoleMessage> roleMessages = openAiLLM.generateChat(chatPayload);
        Assert.notEmpty(roleMessages);
    }

    @Test
    public void completionStreamTest() {
        OpenAiPromptTemplate chatPromptTemplate = new OpenAiPromptTemplate();
        String template = "你好，我的名字叫：{$name},我应该叫你什么？";
        record payload(String name, ChatRole role) {
        }
        OpenAiRoleMessage request = chatPromptTemplate.format(template, new payload("小明", ChatRole.HUMAN));
        OpenAiLLmCompletionPayload chatPayload = OpenAiLLM.withDefaultCompletionModel();
        chatPayload.setPrompts(Collections.singletonList(ChatMsgUtil.getBufferString(Collections.singletonList(request))));
        chatPayload.setStream(true);
        chatPayload.setVerbose(true);
        StringBuilder sb = new StringBuilder();
        chatPayload.setCallbackHandler(new BaseCallbackHandler() {
            @Override
            public void onLlmStart(Map<String, Object> serialized, List<String> prompts, UUID runId, UUID parentRunId, Map<String, Object> kwargs) {
                log.info("llmStart:-->" + serialized.toString());
            }

            @Override
            public void onLlmNewToken(String token, UUID runId, UUID parentRunId, Map<String, Object> kwargs) {
                sb.append(token);
            }

            @Override
            public void onLlmError(Exception error, UUID runId, UUID parentRunId, Map<String, Object> kwargs) {
                log.error("onLlmError:{}", error.getMessage());
            }

            @Override
            public void onLlmEnd(LLMResult response, UUID runId, UUID parentRunId, Map<String, Object> kwargs) {
                log.info("onLlmEnd:{}", response.toString());
            }
        });
        LLMResult llmResult = openAiLLM.generateCompletion(chatPayload);
        log.info("llmResult:{}", llmResult.toString());
        Assert.equals(sb.toString(), llmResult.getGenerations().get(0).stream().map(Generation::getText).reduce("", String::concat));
    }

    @Test
    public void completionTest() {
        OpenAiPromptTemplate chatPromptTemplate = new OpenAiPromptTemplate();
        String template = "你好，我的名字叫：{$name},我应该叫你什么？";
        record payload(String name, ChatRole role) {
        }
        OpenAiRoleMessage request = chatPromptTemplate.format(template, new payload("小明", ChatRole.HUMAN));
        OpenAiLLmCompletionPayload chatPayload = OpenAiLLM.withDefaultCompletionModel();
        chatPayload.setPrompts(Collections.singletonList(ChatMsgUtil.getBufferString(Collections.singletonList(request))));
        chatPayload.setStream(false);
        chatPayload.setVerbose(true);
        chatPayload.setN(2);
        chatPayload.setCallbackHandler(new BaseCallbackHandler() {
            @Override
            public void onLlmEnd(LLMResult response, UUID runId, UUID parentRunId, Map<String, Object> kwargs) {
                log.info("onLlmEnd:{}", response.toString());
            }
        });
        LLMResult llmResult = openAiLLM.generateCompletion(chatPayload);
        log.info("llmResult:{}", llmResult.toString());
        Assert.notEmpty(llmResult.getGenerations());
    }


    @Test
    public void chatFunctionTest() {
        OpenAiPromptTemplate chatPromptTemplate = new OpenAiPromptTemplate();
        String location = "北京";
        String template = "你好，请问今天{$location}的天气怎么样?";

        record payload(String location, ChatRole role) {
        }
        OpenAiRoleMessage askRequest = chatPromptTemplate.format(template, new payload(location, ChatRole.HUMAN));
        OpenAiLLmChatPayload chatPayload = OpenAiLLM.withDefaultChatModel();
        chatPayload.setChatHistory(Collections.singletonList(askRequest));
        chatPayload.setStream(false);
        chatPayload.setVerbose(true);
        chatPayload.setN(1);
        chatPayload.setTemperature(1.0D);
        chatPayload.setModelName(OpenAiModels.gpt35turbo16k);

        //设置为none,来声明强制不适用function
        // chatPayload.setFunctionCall("none");

        List<ChatFunction> functions = new ArrayList<>();
        /**
         * "functions": [
         *     {
         *       "name": "get_current_weather",
         *       "description": "Get the current weather in a given location",
         *       "parameters": {
         *         "type": "object",
         *         "properties": {
         *           "location": {
         *             "type": "string",
         *             "description": "The city and state, e.g. San Francisco, CA"
         *           },
         *           "unit": {
         *             "type": "string",
         *             "enum": ["celsius", "fahrenheit"]
         *           }
         *         },
         *         "required": ["location"]
         *       }
         *     }
         *   ]
         */
        record Properties(String type, String description, @JsonProperty("enum") List<String> enumValue) {
        }
        record Parameters(String type, Map<String, Properties> properties, List<String> required) {
        }
        Properties locationP=new Properties("string", "城市和区，例如：上海市，杨浦区", null);
        Properties unitP=new Properties("string", "要使用的温度单位。从用户位置推断出这一点。", List.of("摄氏度", "华氏度"));
        Parameters parameters = new Parameters("object", Map.of("location", locationP, "unit", unitP), List.of("location"));

        ChatFunction chatFunction = new ChatFunction("get_current_weather", "获取给定位置的当前天气", parameters );
        functions.add(chatFunction);
        chatPayload.setFunctions(functions);
        chatPayload.setCallbackHandler(new BaseCallbackHandler() {
            @Override
            public void onLlmEnd(LLMResult response, UUID runId, UUID parentRunId, Map<String, Object> kwargs) {
                log.info("onLlmEnd:{}", response.toString());
            }
        });
        List<? extends RoleMessage> roleMessages = openAiLLM.generateChat(chatPayload);
        Assert.notEmpty(roleMessages);
        OpenAiRoleMessage functionCallMsg= (OpenAiRoleMessage) roleMessages.get(0);
        Assert.isTrue(functionCallMsg.getRole().equals(ChatRole.AI));
        ChatFunctionCall functionCall = functionCallMsg.getFunctionCall();
        String callName = functionCall.getName();
        Assert.equals(callName, "get_current_weather");
        Map<String, Object> arguments = functionCall.getArguments();
        String callLocation = MapUtil.getStr(arguments, "location");
        Assert.isTrue(callLocation.contains(location));

        OpenAiRoleMessage functionResponseMsg = new OpenAiRoleMessage("今天天气晴朗，温度20度", ChatRole.FUNCTION,"get_current_weather",null);

        chatPayload.setChatHistory(List.of(askRequest,functionCallMsg,functionResponseMsg));
        List<? extends RoleMessage> finalResponse = openAiLLM.generateChat(chatPayload);
        Assert.notEmpty(finalResponse);
        OpenAiRoleMessage finalResponseMsg = (OpenAiRoleMessage) finalResponse.get(0);
        Assert.isTrue(finalResponseMsg.getRole().equals(ChatRole.AI));
        Assert.isTrue(finalResponseMsg.getContent().contains("20"));
    }

}
