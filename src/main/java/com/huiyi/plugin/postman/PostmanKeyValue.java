package com.huiyi.plugin.postman;

/**
 * catgen-maven-plugin
 * 2018/11/29 12:03
 *
 * @author zhangjincheng
 * @since
 **/
public class PostmanKeyValue {
    private String key;
    private String value;
    private String description;
    public PostmanKeyValue(String key, String value, String description) {
        this.value = value;
        this.key = key;
        this.description = description;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
