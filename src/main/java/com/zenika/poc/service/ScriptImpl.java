package com.zenika.poc.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zenika.poc.model.Data;
import com.zenika.poc.model.DataPoint;
import org.graalvm.polyglot.Context;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ScriptImpl implements ScriptService {

    public String execute(String content, List<DataPoint> produced, List<DataPoint> announced, double tolerance) {
        final String jsonInString = serializeDataToJson(produced, announced, tolerance);
        return executeJavaScript(content, jsonInString);
    }

    private String serializeDataToJson(List<DataPoint> produced, List<DataPoint> announced, double tolerance) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(new Data(produced, announced, tolerance));
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error serializing data to JSON: " + e.getMessage());
        }
    }

    private String executeJavaScript(String content, String jsonInString) {
        try (Context context = Context.create()) {
            context.eval("js", content);

            return context.eval("js", String.format("compute(%s)", jsonInString)).toString();
        }
    }
}
