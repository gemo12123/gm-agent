package org.mytest.test.context;

import dev.langchain4j.agent.tool.ToolExecutionRequest;
import dev.langchain4j.data.message.ChatMessage;
import lombok.Data;
import lombok.experimental.SuperBuilder;
import org.mytest.test.constant.AgentState;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Data
@SuperBuilder
public abstract class BaseContext {
    protected String execId;

    protected String name;

    protected String query;

    protected LinkedList<ChatMessage> memory = new LinkedList<>();

    protected List<ToolExecutionRequest> actions = new ArrayList<>();

    protected int currentStep = 0;
    protected int maxSteps = 10;
    protected AgentState agentState;

    public abstract BaseContext cloneContext();
}
