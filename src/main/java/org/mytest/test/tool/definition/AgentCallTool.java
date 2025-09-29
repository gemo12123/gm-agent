package org.mytest.test.tool.definition;

import dev.langchain4j.agent.tool.ToolSpecification;
import dev.langchain4j.model.chat.request.json.JsonObjectSchema;
import lombok.Data;
import org.mytest.test.agent.Agent;

@Data
public class AgentCallTool extends BaseTool {
    protected Agent<?> agent;

    public AgentCallTool(Agent<?> agent) {
        this.agent = agent;
        super.toolSpecification = ToolSpecification.builder()
                .name(agent.getName())
                .description(agent.getDescription())
                .parameters(JsonObjectSchema.builder()
                        .addStringProperty("arg0", "需要完成的功能")
                        .build())
                .build();
    }
}
