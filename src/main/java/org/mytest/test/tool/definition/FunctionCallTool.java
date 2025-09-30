package org.mytest.test.tool.definition;

import dev.langchain4j.agent.tool.ToolSpecifications;
import dev.langchain4j.service.tool.DefaultToolExecutor;
import lombok.Data;

import java.lang.reflect.Method;

@Data
public class FunctionCallTool extends BaseTool{
    protected DefaultToolExecutor toolExecutor;

    public FunctionCallTool(Object obj, String methodName) {
        Class<?> aClass = obj.getClass();
        Method[] methods = aClass.getDeclaredMethods();
        Method fcMethod = null;
        for (Method method : methods) {
            if(method.getName().equals(methodName)){
                fcMethod = method;
                break;
            }
        }
        toolSpecification = ToolSpecifications.toolSpecificationFrom(fcMethod);
        toolExecutor = new DefaultToolExecutor(obj, fcMethod);
    }
}
