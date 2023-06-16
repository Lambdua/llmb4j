package com.llmb4j.util;

import cn.hutool.core.lang.Assert;
import com.llmb4j.common.Generation;
import com.llmb4j.common.LLMResult;
import com.llmb4j.prompt.base.ChatRole;
import com.llmb4j.prompt.base.RoleMessage;

import java.util.List;
import java.util.Map;

/**
 * @author LiangTao
 * @date 2023年06月15 13:23
 **/
public class ChatMsgUtil {
    public static Map<String, String> msgToMap(RoleMessage msg) {
        return Map.of("type", msg.type(), "content", msg.getContent());
    }

    public static List<RoleMessage> llmResultToRoleMessage(LLMResult llmResult) {
        Assert.notEmpty(llmResult.getGenerations(), "llmResult.getGenerations() must not be empty");
        Assert.notNull(llmResult.getLlmOutput(), "llmResult.getLlmOutput() must not be null");
        return llmResult.getGenerations().stream().map(itemGenerations -> {
            String content = itemGenerations.stream().map(Generation::getText).reduce("", String::concat);
            return new RoleMessage(content, ChatRole.CHAT);
        }).toList();
    }

    public static List<Map<String, String>> msgsToMaps(List<RoleMessage> msgs) {
        return msgs.stream().map(ChatMsgUtil::msgToMap).toList();
    }

    public static RoleMessage mapToMsg(Map<String, String> map) {
        return new RoleMessage(map.get("content"), ChatRole.valueOf(map.get("type")));
    }

    public static List<RoleMessage> mapsToMsgs(List<Map<String, String>> maps) {
        return maps.stream().map(ChatMsgUtil::mapToMsg).toList();
    }

    public static String getBufferString(List<? extends RoleMessage> messages) {
        return getBufferString(messages, "Human", "AI");
    }

    public static String getBufferString(List<? extends RoleMessage> messages, String humanPrefix, String aiPrefix) {
        StringBuilder sb = new StringBuilder();
        for (RoleMessage m : messages) {
            switch (m.getRole()) {
                case HUMAN -> sb.append(humanPrefix).append(": ").append(m.getContent()).append(System.lineSeparator());
                case AI -> sb.append(aiPrefix).append(": ").append(m.getContent()).append(System.lineSeparator());
                case SYSTEM,CHAT,FUNCTION -> sb.append(m.getRole().getValue()).append(": ").append(m.getContent()).append(System.lineSeparator());
                default -> throw new IllegalArgumentException("Got unsupported message type: " + m);
            }
        }
        return sb.toString();
    }

}
