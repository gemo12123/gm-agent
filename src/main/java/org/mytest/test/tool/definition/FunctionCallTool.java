package org.mytest.test.tool.definition;

import dev.langchain4j.service.tool.DefaultToolExecutor;
import lombok.Data;

@Data
public class FunctionCallTool extends BaseTool{
    protected DefaultToolExecutor toolExecutor;
}
