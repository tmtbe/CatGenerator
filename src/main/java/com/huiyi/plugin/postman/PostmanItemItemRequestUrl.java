package com.huiyi.plugin.postman;

import java.util.List;

/**
 * catgen-maven-plugin
 * 2018/11/29 12:05
 *
 * @author zhangjincheng
 * @since
 **/

public class PostmanItemItemRequestUrl {
    private String protocol;
    private List<String> host;
    private List<String> path;
    private List<PostmanKeyValue> query;

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public List<String> getHost() {
        return host;
    }

    public void setHost(List<String> host) {
        this.host = host;
    }

    public List<String> getPath() {
        return path;
    }

    public void setPath(List<String> path) {
        this.path = path;
    }

    public List<PostmanKeyValue> getQuery() {
        return query;
    }

    public void setQuery(List<PostmanKeyValue> query) {
        this.query = query;
    }
}
