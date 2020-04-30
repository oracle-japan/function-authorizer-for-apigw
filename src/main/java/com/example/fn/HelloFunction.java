package com.example.fn;

import com.fnproject.fn.api.FnConfiguration;
import com.fnproject.fn.api.RuntimeContext;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class HelloFunction {

    private String secretToken;

    @FnConfiguration
    public void config(RuntimeContext ctx) {
        secretToken = ctx.getConfigurationByKey("SECRET_TOKEN").orElse("");
    }

    public static class Input {
        public String type;
        public String token;
    }

    public static class Result {
        // required
        public boolean active;
        public String principal;
        public String[] scope;
        public String expiresAt;

        // optional
        public String wwwAuthenticate;

        // optional
        public String clientId;

        // optional
        public Map<String, Object> context;
    }

    public Result handleRequest(Input input) {

        System.out.println("Inside Java Hello function");

        Result result = falseResult();

        if (input == null || input.type == null || !"TOKEN".equals(input.type.trim())) {
            result.wwwAuthenticate = "Bearer realm=\"example.com\", error=\"invalid type\", error_description=\"type must be provided and the value should be \"TOKEN\"\"";
            return result;
        }

        if (input.token == null || "".equals(input.token.trim())) {
            result.wwwAuthenticate = "Bearer realm=\"example.com\", error=\"invalid request\", error_description=\"missing token\"";
            return result;
        }

        // write down your own check logic here
        if (secretToken.equals(input.token)) {
            result = trueResult();
        } else {
            // for demo purpose only, output correct token in error description
            result.wwwAuthenticate = "Bearer realm=\"example.com\", error=\"invalid token\", error_description=\"token should be \"Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1nHyDtTwR3SEJ3z489...\"\"";
            return result;
        }

        return result;
    }

    private Result trueResult() {
        Result trueResult = new Result();
        trueResult.active = true;
        trueResult.principal = "https://example.com/users/jdoe";
        trueResult.scope = new String[]{"list:hello", "read:hello", "create:hello", "update:hello", "delete:hello", "someScope"};
        trueResult.clientId = "host123";
        trueResult.expiresAt = new Date().toInstant().plusMillis(60000).toString();
        Map<String, Object> contextMap = new HashMap<>();
        contextMap.put("email", "john.doe@example.com");
        trueResult.context = contextMap;
        return trueResult;
    }

    private Result falseResult() {
        Result falseResult = new Result();
        falseResult.active = false;
        falseResult.expiresAt = "2020-04-30T10:15:30+01:00";
        Map<String, Object> contextMap = new HashMap<>();
        contextMap.put("email", "john.doe@example.com");
        falseResult.context = contextMap;
        falseResult.wwwAuthenticate = "Bearer realm=\"example.com\"";
        return falseResult;
    }

}