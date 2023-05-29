package com.llmb.prompt.chat;

import com.llmb.prompt.base.LLMPromptTemplate;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.RecordComponent;
import java.util.Map;

/**
 * @author LiangTao
 * @date 2023年05月26 10:53
 **/
@Slf4j
public class ChatPromptTemplate implements LLMPromptTemplate<ChatMessage> {

    @Override
    public ChatMessage toMsg(Object target, Record argsPayload) {
        if (target instanceof String prompt) {
            Class<? extends Record> aClass = argsPayload.getClass();
            ChatRole chatRole = ChatRole.SYSTEM;

            for (RecordComponent recordComponent : aClass.getRecordComponents()) {
                Method accessor = recordComponent.getAccessor();
                accessor.setAccessible(true);
                String paramName = recordComponent.getName();
                try {
                    if (prompt.contains("{$" + paramName + "}")) {
                        prompt = prompt.replace("{$" + paramName + "}", accessor.invoke(argsPayload).toString());
                    }
                    if (recordComponent.getType().equals(ChatRole.class)) {
                        chatRole = (ChatRole) accessor.invoke(argsPayload);
                    }
                } catch (IllegalAccessException | InvocationTargetException e) {
                    log.error("获取参数值失败", e);
                }

            }
            return new ChatMessage(prompt, chatRole);
        }
        throw new UnsupportedOperationException("不支持的类型: " + target.getClass());
    }

    @Override
    public ChatMessage toMsg(Object target, Map<String, Object> argsPayload) {
        if (target instanceof String prompt) {
            ChatRole chatRole = ChatRole.SYSTEM;
            for (Map.Entry<String, Object> entry : argsPayload.entrySet()) {
                String paramName = entry.getKey();
                Object paramValue = entry.getValue();
                if (prompt.contains("{$" + paramName + "}")) {
                    prompt = prompt.replace("{$" + paramName + "}", paramValue.toString());
                }
                if (paramValue instanceof ChatRole role) {
                    chatRole = role;
                }
            }
            return new ChatMessage(prompt, chatRole);
        }
        throw new UnsupportedOperationException("不支持的类型: " + target.getClass());
    }
}
