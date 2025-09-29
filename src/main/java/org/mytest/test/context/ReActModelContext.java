package org.mytest.test.context;

import dev.langchain4j.agent.tool.ToolSpecification;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import org.mytest.test.model.BaseModel;
import org.mytest.test.model.ReActModel;
import org.mytest.test.tool.definition.BaseTool;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class ReActModelContext extends BaseContext {

    private Map<String, BaseModel> subTask = new HashMap<>();

    private List<String> files = new LinkedList<>();

    private Map<ToolSpecification, BaseTool> tools;

    private String systemPrompt;
    private String nextPrompt;
    private String summaryPrompt;

    @Override
    public ReActModelContext cloneContext() {
        return ReActModelContext.builder()
                .name(this.name)
                .query(this.query)
                .maxSteps(this.maxSteps)
                .agentState(this.agentState)
                .nextPrompt(this.nextPrompt)
                .summaryPrompt(this.summaryPrompt)
                .build();
    }
}
