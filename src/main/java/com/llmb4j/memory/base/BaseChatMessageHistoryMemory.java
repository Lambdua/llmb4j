package com.llmb4j.memory.base;


import com.llmb4j.prompt.base.RoleMessage;

import java.util.List;

/**
 * 聊天消息历史记录存储类
 * @author LiangTao
 * @date 2023年06月06 14:24
 **/
public interface BaseChatMessageHistoryMemory {
    List<RoleMessage> messageList();

    void addMessage(RoleMessage message);

    void clear();
}
