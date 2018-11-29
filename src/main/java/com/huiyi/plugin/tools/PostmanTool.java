package com.huiyi.plugin.tools;

import com.alibaba.fastjson.JSON;
import com.huiyi.plugin.doms.ControllerDom;
import com.huiyi.plugin.doms.MethodDom;
import com.huiyi.plugin.doms.MethodParameterDom;
import com.huiyi.plugin.postman.*;

import java.util.ArrayList;
import java.util.List;

public class PostmanTool extends BaseTools {
    public String createPostmanJson(List<ControllerDom> controllerDoms, String baseUrl) {
        PostmanData postmanData = new PostmanData();
        postmanData.setInfo(new PostmanInfo());
        postmanData.getInfo().setDescription("荟医前端请求");
        postmanData.getInfo().setName("荟医信息 RESTful APIs");
        postmanData.getInfo().setSchema("https://schema.getpostman.com/json/collection/v2.1.0/collection.json");
        List<PostmanItem> postmanItems = new ArrayList<>();
        postmanData.setItem(postmanItems);
        for (ControllerDom controllerDom : controllerDoms) {
            PostmanItem postmanItem = new PostmanItem();
            postmanItems.add(postmanItem);
            postmanItem.setName(controllerDom.getDes());
            List<PostmanItemItem> postmanItemItems = new ArrayList<>();
            postmanItem.setItem(postmanItemItems);
            for (MethodDom methodDom : controllerDom.getMethodDoms()) {
                PostmanItemItem postmanItemItem = new PostmanItemItem();
                postmanItemItems.add(postmanItemItem);

                postmanItemItem.setName(methodDom.getDes());

                PostmanItemItemRequest postmanItemItemRequest = new PostmanItemItemRequest();
                postmanItemItem.setRequest(postmanItemItemRequest);
                postmanItemItemRequest.setMethod(methodDom.getMethod());

                List<PostmanKeyValue> header = new ArrayList<>();
                postmanItemItemRequest.setHeader(header);
                header.add(new PostmanKeyValue("Accept", "*/*", ""));
                header.add(new PostmanKeyValue("Content-Type", "application/json; charset=utf-8;", ""));
                header.add(new PostmanKeyValue("jwt", "{{jwt}}", "jwt头"));
                PostmanItemItemRequestBody body = new PostmanItemItemRequestBody();
                postmanItemItemRequest.setBody(body);
                body.setMode("raw");
                body.setRaw("");

                PostmanItemItemRequestUrl url = new PostmanItemItemRequestUrl();
                postmanItemItemRequest.setUrl(url);
                url.setProtocol("http");

                String[] hostOnes = baseUrl.split("\\/\\/")[1].split("\\.");
                List<String> host = new ArrayList<>();
                for (String hostOne : hostOnes) {
                    host.add(hostOne);
                }
                url.setHost(host);

                List<String> path = new ArrayList<>();
                path.add(controllerDom.getService());
                path.add(controllerDom.getUrl());
                path.add(methodDom.getUrl());
                url.setPath(path);

                //if (methodDom.getMethod().equals("GET")) {
                List<PostmanKeyValue> query = new ArrayList<>();
                for (MethodParameterDom methodParameterDom : methodDom.getMethodParameterDoms()) {
                    PostmanKeyValue postmanKeyValue = new PostmanKeyValue(methodParameterDom.getName(), "{{" + methodParameterDom.getName() + "}}", methodParameterDom.getDes());
                    if (methodParameterDom.getRequired() != null && methodParameterDom.getRequired().equals("true")) {
                        postmanKeyValue.setDescription("[" + methodParameterDom.getType() + "]" + postmanKeyValue.getDescription() + "(必填)");
                    }
                    query.add(postmanKeyValue);
                }
                url.setQuery(query);
                //} else {
                //设置body

                //}
            }
        }
        return JSON.toJSONString(postmanData);
    }
}
