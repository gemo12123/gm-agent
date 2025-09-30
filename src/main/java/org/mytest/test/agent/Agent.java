package org.mytest.test.agent;

import dev.langchain4j.agent.tool.ToolSpecification;
import dev.langchain4j.data.message.ToolExecutionResultMessage;
import lombok.Builder;
import lombok.Data;
import org.mytest.test.context.BaseContext;
import org.mytest.test.entity.Response;
import org.mytest.test.model.BaseModel;
import org.mytest.test.tool.definition.AgentCallTool;
import org.mytest.test.tool.definition.BaseTool;
import org.mytest.test.util.AgentUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Builder
@Data
public class Agent<T extends BaseContext> {
    private String name;

    private String description;

    private BaseModel<T> model;

    private T context;

    private List<BaseTool> tools;

    public void run(String query) {
        context.setQuery(query);
        context.setName(name);
        context.setExecId(UUID.randomUUID().toString());

        Map<ToolSpecification, BaseTool> map = new HashMap<>();
        for (BaseTool tool : tools) {
            if (tool instanceof AgentCallTool agentCallTool) {
                BaseContext subContext = agentCallTool.getAgent().getContext();
                subContext.setHasParentAgent(true);
            }
            map.put(tool.getToolSpecification(), tool);
        }
        context.setTools(map);

        model.setContext(context);
        Response response = model.run();
        System.out.println(response);
    }

    public void captureAskResult(Map<String, String> rows) {
        for (Map.Entry<String, String> entry : rows.entrySet()) {
            String id = entry.getKey();
            String resultVal = entry.getValue();
            ToolExecutionResultMessage result = ToolExecutionResultMessage.from(AgentUtils.findToolCallRequest(context, id), resultVal);
            context.getMemory().add(result);
            AgentUtils.printLog(context, result);
        }
    }

    public Agent<T> cloneAgent() {
        return Agent.<T>builder()
                .name(name)
                .description(description)
                .model(model)
                .context((T) context.cloneContext())
                .tools(tools)
                .build();
    }

}
