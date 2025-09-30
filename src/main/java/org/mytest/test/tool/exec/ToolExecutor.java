package org.mytest.test.tool.exec;

import dev.langchain4j.agent.tool.ToolExecutionRequest;
import dev.langchain4j.data.message.ToolExecutionResultMessage;
import org.mytest.test.context.ReActModelContext;
import org.mytest.test.tool.definition.BaseTool;

public interface ToolExecutor {

    ToolExecutionResultMessage executeTools(ReActModelContext context, ToolExecutionRequest request, BaseTool tool);

    String toolName();
}
