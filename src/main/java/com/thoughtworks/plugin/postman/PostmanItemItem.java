package com.thoughtworks.plugin.postman;

/**
 * catgen-maven-plugin
 * 2018/11/29 12:02
 *
 * @author zhangjincheng
 * @since
 **/
public class PostmanItemItem {
    private String name;
    private PostmanItemItemRequest request;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public PostmanItemItemRequest getRequest() {
        return request;
    }

    public void setRequest(PostmanItemItemRequest request) {
        this.request = request;
    }
}
