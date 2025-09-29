package org.mytest.test.model;

import lombok.Data;
import org.mytest.test.constant.AgentState;
import org.mytest.test.context.BaseContext;
import org.mytest.test.entity.Response;

import java.util.List;

@Data
public abstract class BaseModel<T extends BaseContext> {

    protected T context;

    public Response run() {
        Response response = null;
        context.setAgentState(AgentState.RUNNING);
        while (context.getCurrentStep() < context.getMaxSteps() && context.getAgentState() == AgentState.RUNNING){
            context.setCurrentStep(context.getCurrentStep() + 1);
            response = step();
        }
        return response;
    }

    public abstract Response step();

    public abstract void think();

    public abstract List<Response> action();
}
