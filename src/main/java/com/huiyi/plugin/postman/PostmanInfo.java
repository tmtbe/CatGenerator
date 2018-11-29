package com.huiyi.plugin.postman;

/**
 * catgen-maven-plugin
 * 2018/11/29 11:58
 *
 * @author zhangjincheng
 * @since
 **/
public class PostmanInfo {
    private String name;
    private String description;
    private String schema;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSchema() {
        return schema;
    }

    public void setSchema(String schema) {
        this.schema = schema;
    }
}
