package com.llmb4j.prompt.base;

import java.util.List;
import java.util.Map;

/**
 * @author LiangTao
 * @date 2023年05月25 17:25
 **/
public record RoleMessage(String content,ChatRole role) {


    @Override
    public String toString() {
        return role.getValue() + ": " + content;
    }

    public String type() {
        return role.getValue();
    }

    public static Map<String, String> msgToMap(RoleMessage msg) {
        return Map.of("type", msg.type(), "content", msg.content);
    }

    public static List<Map<String, String>> msgsToMaps(List<RoleMessage> msgs) {
        return msgs.stream().map(RoleMessage::msgToMap).toList();
    }

    public static RoleMessage mapToMsg(Map<String, String> map) {
        return new RoleMessage(map.get("content"), ChatRole.valueOf(map.get("type")));
    }

    public static List<RoleMessage> mapsToMsgs(List<Map<String, String>> maps) {
        return maps.stream().map(RoleMessage::mapToMsg).toList();
    }

    public static String getBufferString(List<RoleMessage> messages) {
        return getBufferString(messages, "Human", "AI");
    }

    public static String getBufferString(List<RoleMessage> messages, String humanPrefix, String aiPrefix) {
        StringBuilder sb = new StringBuilder();
        for (RoleMessage m : messages) {
            if (m.role.equals(ChatRole.HUMAN)) {
                sb.append(humanPrefix).append(": ").append(m.content).append(System.lineSeparator());
            } else if (m.role.equals(ChatRole.AI)) {
                sb.append(aiPrefix).append(": ").append(m.content).append(System.lineSeparator());
            } else if (m.role.equals(ChatRole.SYSTEM)) {
                sb.append("System").append(": ").append(m.content).append(System.lineSeparator());
            } else if (m.role.equals(ChatRole.CHAT)) {
                sb.append(m.role.getValue()).append(": ").append(m.content).append(System.lineSeparator());
            } else {
                throw new IllegalArgumentException("Got unsupported message type: " + m);
            }
        }
        return sb.toString();
    }
}
