package com.llmb4j.prompt;

import com.llmb4j.prompt.base.ChatRole;
import com.llmb4j.prompt.base.LLMInputParse;
import com.llmb4j.prompt.base.RoleMessage;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.RecordComponent;
import java.util.Map;

/**
 * @author LiangTao
 * @date 2023年06月16 14:05
 **/
@Slf4j
public class ChatPromptTemplate implements LLMInputParse<RoleMessage> {
    @Override
    public RoleMessage format(String template, Record argsPayload) {
        Class<? extends Record> aClass = argsPayload.getClass();
        ChatRole chatRole = ChatRole.SYSTEM;

        for (RecordComponent recordComponent : aClass.getRecordComponents()) {
            Method accessor = recordComponent.getAccessor();
            accessor.setAccessible(true);
            String paramName = recordComponent.getName();
            try {
                if (template.contains("{$" + paramName + "}")) {
                    template = template.replace("{$" + paramName + "}", accessor.invoke(argsPayload).toString());
                }
                if (recordComponent.getType().equals(ChatRole.class)) {
                    chatRole = (ChatRole) accessor.invoke(argsPayload);
                }
            } catch (IllegalAccessException | InvocationTargetException e) {
                log.error("获取参数值失败", e);
            }

        }
        return new RoleMessage(template, chatRole);
    }

    @Override
    public RoleMessage format(String template, Map<String, Object> argsPayload) {
        ChatRole chatRole = ChatRole.SYSTEM;
        for (Map.Entry<String, Object> entry : argsPayload.entrySet()) {
            String paramName = entry.getKey();
            Object paramValue = entry.getValue();
            if (template.contains("{$" + paramName + "}")) {
                template = template.replace("{$" + paramName + "}", paramValue.toString());
            }
            if (paramValue instanceof ChatRole role) {
                chatRole = role;
            }
        }
        return new RoleMessage(template, chatRole);
    }
}
