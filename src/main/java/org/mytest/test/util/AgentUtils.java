package org.mytest.test.util;

import dev.langchain4j.agent.tool.ToolExecutionRequest;
import dev.langchain4j.data.message.*;
import lombok.extern.slf4j.Slf4j;
import org.mytest.test.context.BaseContext;

import java.util.List;

@Slf4j
public class AgentUtils {
    public static String formatMessage(ChatMessage message) {
        return switch (message) {
            case UserMessage userMessage -> String.format("role:%s content:%s\n",
                    userMessage.type().name(),
                    ((TextContent) userMessage.contents().getFirst()).text());
            case SystemMessage systemMessage -> String.format("role:%s content:%s\n",
                    systemMessage.type().name(),
                    systemMessage.text());
            case AiMessage aiMessage -> {
                if (aiMessage.hasToolExecutionRequests()) {
                    yield String.format("role:%s reason:%s tools:%s\n",
                            aiMessage.type().name(),
                            aiMessage.text(),
                            aiMessage.toolExecutionRequests());
                }
                yield String.format("role:%s content:%s\n",
                        aiMessage.type().name(),
                        aiMessage.text());
            }
            case ToolExecutionResultMessage toolResultMessage -> String.format("role:%s tool:%s\n",
                    toolResultMessage.type().name(),
                    toolResultMessage.text());
            default -> null;
        };
    }

    public static ChatMessage findExistToolResult(ToolExecutionRequest request, BaseContext context) {
        String id = request.id();
        for (ChatMessage chatMessage : context.getMemory()) {
            if (chatMessage instanceof ToolExecutionResultMessage toolResultMessage
                    && toolResultMessage.id().equals(id)) {
                return chatMessage;
            }
        }
        return null;
    }

    public static ToolExecutionRequest findToolCallRequest(BaseContext context, String id) {
        for (ChatMessage chatMessage : context.getMemory()) {
            if (chatMessage instanceof AiMessage aiMessage) {
                if (aiMessage.hasToolExecutionRequests()) {
                    for (ToolExecutionRequest toolExecutionRequest : aiMessage.toolExecutionRequests()) {
                        if (toolExecutionRequest.id().equals(id)) {
                            return toolExecutionRequest;
                        }
                    }
                }
            }
        }
        return null;
    }


    public static void printLog(BaseContext context, ChatMessage message) {
        log.info("{}({}): {}", context.getName(), context.getQuery(), AgentUtils.formatMessage(message));
    }


    public static String historyFormat(List<ChatMessage> memory) {
        StringBuilder sb = new StringBuilder();
        for (ChatMessage chatMessage : memory) {
            String s = formatMessage(chatMessage);
            if (s != null) {
                sb.append(s);
            }
        }
        return sb.toString();
    }
}
