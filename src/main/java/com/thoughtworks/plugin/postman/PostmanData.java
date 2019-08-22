package com.thoughtworks.plugin.postman;

import java.util.List;

/**
 * catgen-maven-plugin
 * 2018/11/29 11:57
 *
 * @author zhangjincheng
 * @since
 **/

public class PostmanData {
    private PostmanInfo info;
    private List<PostmanItem> item;

    public PostmanInfo getInfo() {
        return info;
    }

    public void setInfo(PostmanInfo info) {
        this.info = info;
    }

    public List<PostmanItem> getItem() {
        return item;
    }

    public void setItem(List<PostmanItem> item) {
        this.item = item;
    }
}
