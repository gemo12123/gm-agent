package org.mytest.test.env;

import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.openai.OpenAiChatModel;

public class Environment {
    public static final ChatModel CHAT_MODEL = OpenAiChatModel.builder()
            .baseUrl("")
            .modelName("")
            .apiKey("")
            .build();
}
