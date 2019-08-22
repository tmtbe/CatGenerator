package com.thoughtworks.plugin.doms;

import org.dom4j.Attribute;
import org.dom4j.Element;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

public class MarcoDom {
    private String name;
    private String type;
    private String value;
    private String des;
    public void setElement(Element element) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        List<Attribute> attributes = element.attributes();
        for (Attribute attribute : attributes) {
            Class<MarcoDom> clz = MarcoDom.class;
            Method mt = clz.getMethod("set" + Help.toUpperCaseFirstOne(attribute.getName()), String.class);
            mt.invoke(this, attribute.getValue());
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }
}
