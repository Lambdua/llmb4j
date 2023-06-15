package com.llmb4j.models.openai.completion.chat;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChatFunction {

    @NonNull
    String name;
    String description;
    Object parameters;

}
