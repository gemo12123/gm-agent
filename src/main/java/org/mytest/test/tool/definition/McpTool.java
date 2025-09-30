package org.mytest.test.tool.definition;

import dev.langchain4j.agent.tool.ToolSpecification;
import dev.langchain4j.mcp.client.McpClient;
import lombok.Data;

@Data
public class McpTool extends BaseTool {
    protected McpClient mcpClient;

    public McpTool(ToolSpecification toolSpecification, McpClient mcpClient) {
        this.toolSpecification = toolSpecification;
        this.mcpClient = mcpClient;
    }
}
