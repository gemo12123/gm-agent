package org.mytest.test.tool.exec;

import dev.langchain4j.agent.tool.ToolExecutionRequest;
import org.mytest.test.context.ReActModelContext;
import org.mytest.test.tool.definition.BaseTool;

public interface ToolExecutor {

    void executeTools(ReActModelContext context, ToolExecutionRequest request, BaseTool tool);

    String toolName();
}
