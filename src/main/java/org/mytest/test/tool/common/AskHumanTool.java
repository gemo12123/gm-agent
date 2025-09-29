package org.mytest.test.tool.common;

import dev.langchain4j.agent.tool.P;
import dev.langchain4j.agent.tool.Tool;

public class AskHumanTool {

    @Tool(name = "askHuman", value = "如果有不明确的工具参数或内容，你可以向用户提问，但提问的问题必须是简洁、明确、可回答的")
    public String askHuman(@P("需要向用户询问的问题") String question){
        throw new RuntimeException("Ask human is executing!");
    }
}
