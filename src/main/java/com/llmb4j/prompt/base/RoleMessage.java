package com.llmb4j.prompt.base;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author LiangTao
 * @date 2023年05月25 17:25
 **/
@Data
@NoArgsConstructor
public class RoleMessage{

    private String content;

    private ChatRole role;

    public RoleMessage(String content, ChatRole role) {
        this.content = content;
        this.role = role;
    }

    @Override
    public String toString() {
        return role.getValue() + ": " + content;
    }

    public String type() {
        return role.getValue();
    }


}
