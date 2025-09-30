package org.mytest.test.tool.exec;

import com.alibaba.fastjson.JSONObject;
import dev.langchain4j.agent.tool.ToolExecutionRequest;
import dev.langchain4j.data.message.ToolExecutionResultMessage;
import lombok.extern.slf4j.Slf4j;
import org.mytest.test.agent.Agent;
import org.mytest.test.context.ReActModelContext;
import org.mytest.test.tool.definition.AgentCallTool;
import org.mytest.test.tool.definition.BaseTool;
import org.mytest.test.tool.definition.FunctionCallTool;
import org.mytest.test.tool.definition.McpTool;

import java.util.UUID;

@Slf4j
public class DefaultToolExecutor implements ToolExecutor {

    @Override
    public ToolExecutionResultMessage executeTools(ReActModelContext context, ToolExecutionRequest request, BaseTool tool) {
        String result = null;
        if (tool instanceof McpTool mcpTool) {
            result = mcpTool.getMcpClient()
                    .executeTool(request);
        } else if (tool instanceof FunctionCallTool functionCallTool) {
            result = functionCallTool.getToolExecutor()
                    .execute(request, null);
        } else if (tool instanceof AgentCallTool agentCallTool) {
            String subId = UUID.randomUUID().toString();
            Agent<?> subTask = agentCallTool.getAgent().cloneAgent();
            subTask.getContext().setExecId(subId);
            context.getSubTask()
                    .put(subId, subTask);
            String arg0 = JSONObject.parseObject(request.arguments())
                    .getString("arg0");
            subTask.run(arg0);
        }

        if (result == null) {
            log.warn("未知异常！request: {}, tool: {}", request, tool);
            result = "执行异常！";
        }
        ToolExecutionResultMessage resultMessage = ToolExecutionResultMessage.from(request, result);
        return resultMessage;
    }

    @Override
    public String toolName() {
        return "default";
    }
}
