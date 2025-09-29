package org.mytest.test.tool.definition;

import dev.langchain4j.agent.tool.ToolSpecification;
import lombok.Data;

@Data
public abstract class BaseTool {
    protected ToolSpecification toolSpecification;
}
