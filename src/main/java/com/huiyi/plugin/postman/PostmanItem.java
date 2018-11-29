package com.huiyi.plugin.postman;

import java.util.List;

/**
 * catgen-maven-plugin
 * 2018/11/29 12:00
 *
 * @author zhangjincheng
 * @since
 **/
public class PostmanItem {
    private String name;
    private List<PostmanItemItem> item;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<PostmanItemItem> getItem() {
        return item;
    }

    public void setItem(List<PostmanItemItem> item) {
        this.item = item;
    }
}
