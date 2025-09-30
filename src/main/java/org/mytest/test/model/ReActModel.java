package org.mytest.test.model;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import dev.langchain4j.agent.tool.ToolExecutionRequest;
import dev.langchain4j.data.message.*;
import dev.langchain4j.model.chat.request.ChatRequest;
import dev.langchain4j.model.chat.response.ChatResponse;
import lombok.extern.slf4j.Slf4j;
import org.mytest.test.agent.Agent;
import org.mytest.test.constant.AgentState;
import org.mytest.test.constant.ResponseType;
import org.mytest.test.context.ReActModelContext;
import org.mytest.test.entity.Response;
import org.mytest.test.env.Environment;
import org.mytest.test.tool.exec.ToolExecuteCoordinator;
import org.mytest.test.util.AgentUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Slf4j
public class ReActModel extends BaseModel<ReActModelContext> {
    @Override
    public Response step() {
        if (context.getActions().isEmpty()) {
            think();
        }
        if (context.getActions().isEmpty()) {
            context.setAgentState(AgentState.FINISHED);
            return switch (context.getMemory().getLast()) {
                case UserMessage userMessage -> Response.normal(((TextContent) userMessage.contents().get(0)).text());
                case AiMessage aiMessage -> Response.normal(aiMessage.text());
                default -> Response.normal("异常回复");
            };
        }

        for (ToolExecutionRequest action : context.getActions()) {
            if ("askHuman".equals(action.name())
                    && AgentUtils.findExistToolResult(action, context) == null) {
                context.setAgentState(AgentState.WAITING);
                return Response.askQuestion(action.arguments(), context.getExecId());
            }
        }

        List<Response> actionResult = action();
        List<Response> questions = actionResult.stream()
                .filter(item -> item.getType() == ResponseType.ASK_QUESTION)
                .toList();
        if (!questions.isEmpty()) {
            // 如果允许顶级智能体向用户提问，需要处理该逻辑
            if (!context.isHasParentAgent() && !questions.stream()
                    .filter(item -> context.getExecId().equals(item.getExtension().get("execId")))
                    .toList()
                    .isEmpty()) {
                context.setAgentState(AgentState.FINISHED);
                return Response.normal("我有回答不了的问题！");
            }
            answerQuestion(questions);
        }

        return Response.withResponses(actionResult);
    }

    private void answerQuestion(List<Response> questions) {
        Table<String, String, String> table = HashBasedTable.create();
        for (Response question : questions) {
            UserMessage userMessage = UserMessage.userMessage(question.getContent().getFirst());
            context.getMemory().add(userMessage);
            AgentUtils.printLog(context, userMessage);
            ChatResponse response = Environment.CHAT_MODEL.chat(ChatRequest.builder()
                    .messages(context.getMemory())
                    .toolSpecifications(new ArrayList<>(context.getTools().keySet()))
                    .build());
            AiMessage aiMessage = response.aiMessage();
            context.getMemory().add(aiMessage);

            String execId = question.getExtension().get("execId").toString();
            String id = question.getExtension().get("id").toString();
            String answer = aiMessage.text();
            table.put(execId, id, answer);
        }

        Set<String> rowKeySet = table.rowKeySet();
        for (String rowKey : rowKeySet) {
            Agent<?> agent = context.getSubTask().get(rowKey);
            Map<String, String> rows = table.row(rowKey);
            agent.captureAskResult(rows);
            agent.getModel().run();
        }

    }


    @Override
    public void think() {
        LinkedList<ChatMessage> memory = context.getMemory();
        if (context.getCurrentStep() == 1) {
            SystemMessage systemMessage = SystemMessage.systemMessage(context.getSystemPrompt()
                    .replace("{{date}}",
                            LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))));
            memory.add(systemMessage);
            UserMessage userMessage = UserMessage.userMessage(context.getQuery());
            memory.add(userMessage);
            AgentUtils.printLog(context, userMessage);
        } else {
            UserMessage userMessage = UserMessage.from(context.getNextPrompt().replace("{{query}}", context.getQuery()));
            memory.add(userMessage);
            AgentUtils.printLog(context, userMessage);
        }

        ChatResponse response = Environment.CHAT_MODEL.chat(ChatRequest.builder()
                .messages(memory)
                .toolSpecifications(new ArrayList<>(context.getTools().keySet()))
                .build());
        AiMessage aiMessage = response.aiMessage();
        memory.add(aiMessage);
        AgentUtils.printLog(context, aiMessage);
        if (aiMessage.hasToolExecutionRequests()) {
            context.getActions().addAll(aiMessage.toolExecutionRequests());
        }
    }

    @Override
    public List<Response> action() {
        List<Response> responses = ToolExecuteCoordinator.executeTools(context);
        context.getActions().clear();
        return responses;
    }

}
