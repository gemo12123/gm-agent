package org.mytest.test.tool.exec;

import dev.langchain4j.agent.tool.ToolExecutionRequest;
import dev.langchain4j.agent.tool.ToolSpecification;
import lombok.extern.slf4j.Slf4j;
import org.mytest.test.context.ReActModelContext;
import org.mytest.test.tool.definition.BaseTool;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
public class ToolExecuteCoordinator {

    public static final List<ToolExecutor> TOOL_EXECUTORS = new ArrayList<>();
    public static final ToolExecutor DEFAULT_TOOL_EXECUTORS = new DefaultToolExecutor();

    public static void executeTools(ReActModelContext context) {
        List<ToolExecutionRequest> requests = context.getActions();
        for (ToolExecutionRequest request : requests) {
            doExecute(context, request);
        }
    }

    private static void doExecute(ReActModelContext context, ToolExecutionRequest request) {
        BaseTool baseTool = null;

        Map<ToolSpecification, BaseTool> tools = context.getTools();
        for (Map.Entry<ToolSpecification, BaseTool> entry : tools.entrySet()) {
            ToolSpecification toolSpecification = entry.getKey();
            if (toolSpecification.name().equals(request.name())) {
                baseTool = entry.getValue();
            }
        }
        if (baseTool == null){
            log.warn("未找到工具执行："+ request);
            return;
        }

        for (ToolExecutor toolExecutor : TOOL_EXECUTORS) {
            if (toolExecutor.toolName().equals(request.name())) {
                toolExecutor.executeTools(context, request, baseTool);
                return;
            }
        }
        DEFAULT_TOOL_EXECUTORS.executeTools(context, request, baseTool);
    }
}
