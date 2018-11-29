package com.huiyi.plugin.postman;


import java.util.List;

/**
 * catgen-maven-plugin
 * 2018/11/29 12:02
 *
 * @author zhangjincheng
 * @since
 **/
public class PostmanItemItemRequest {
    private String method;
    private List<PostmanKeyValue> header;
    private PostmanItemItemRequestBody body;
    private PostmanItemItemRequestUrl url;

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public List<PostmanKeyValue> getHeader() {
        return header;
    }

    public void setHeader(List<PostmanKeyValue> header) {
        this.header = header;
    }

    public PostmanItemItemRequestBody getBody() {
        return body;
    }

    public void setBody(PostmanItemItemRequestBody body) {
        this.body = body;
    }

    public PostmanItemItemRequestUrl getUrl() {
        return url;
    }

    public void setUrl(PostmanItemItemRequestUrl url) {
        this.url = url;
    }
}
