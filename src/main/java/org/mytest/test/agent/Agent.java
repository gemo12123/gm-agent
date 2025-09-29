package org.mytest.test.agent;

import lombok.Builder;
import lombok.Data;
import org.mytest.test.context.BaseContext;
import org.mytest.test.entity.Response;
import org.mytest.test.model.BaseModel;

@Builder
@Data
public class Agent<T extends BaseContext> {
    private String name;

    private String description;

    private BaseModel<T> model;

    private T context;

    public void run(String query) {
        context.setQuery(query);
        model.setContext(context);
        Response response = model.run();
        System.out.println(response);
    }

    public Agent<T> cloneAgent(){
        return Agent.<T>builder()
                .name(name)
                .description(description)
                .model(model)
                .context((T)context.cloneContext())
                .build();
    }

}
