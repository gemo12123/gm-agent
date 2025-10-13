package org.mytest.test.entity;

import lombok.Data;
import org.mytest.test.constant.ResponseType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Data
public class Response {

    private ResponseType type;

    private List<String> content;

    private Map<String, Object> extension;

    private Response() {
    }

    public static Response withResponses(List<Response> respones) {
        Response response = new Response();
        response.type = ResponseType.NORMAL;
        response.content = respones.stream().flatMap(r -> r.content.stream()).toList();
        return response;
    }
    public static Response normal(List<String> contents) {
        Response response = new Response();
        response.type = ResponseType.NORMAL;
        response.content = contents;
        return response;
    }

    public static Response normal(String content) {
        Response response = new Response();
        response.type = ResponseType.NORMAL;
        response.content = List.of(content);
        return response;
    }

    public static Response askQuestion(String content, String execId, String requestId) {
        Response response = new Response();
        response.type = ResponseType.ASK_QUESTION;
        response.content = List.of(content);
        HashMap<String, Object> map = new HashMap<>();
        map.put("execId", execId);
        map.put("requestId", requestId);
        map.put("id", UUID.randomUUID().toString());
        response.extension = map;
        return response;
    }

}
