package org.mytest.test.context;

import dev.langchain4j.agent.tool.ToolExecutionRequest;
import dev.langchain4j.agent.tool.ToolSpecification;
import dev.langchain4j.data.message.ChatMessage;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.SuperBuilder;
import org.mytest.test.constant.AgentState;
import org.mytest.test.tool.definition.BaseTool;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Data
@SuperBuilder
public abstract class BaseContext {
    protected String execId;

    protected String name;

    protected String query;

    private Map<ToolSpecification, BaseTool> tools;

    @Builder.Default
    protected LinkedList<ChatMessage> memory = new LinkedList<>();

    @Builder.Default
    protected LinkedList<ChatMessage> summaryMemory = new LinkedList<>();

    @Builder.Default
    protected List<ToolExecutionRequest> actions = new ArrayList<>();

    @Builder.Default
    protected int currentStep = 0;
    @Builder.Default
    protected int maxSteps = 10;
    protected AgentState agentState;

    protected boolean hasParentAgent;

    public abstract BaseContext cloneContext();
}
