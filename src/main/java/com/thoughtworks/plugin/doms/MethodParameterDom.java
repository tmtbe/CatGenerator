package com.thoughtworks.plugin.doms;

import com.thoughtworks.plugin.tools.BaseTools;
import org.dom4j.Attribute;
import org.dom4j.Element;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

public class MethodParameterDom implements Cloneable {
    private String name;
    private String type;
    private String des;
    private String required;
    private String sen;

    public void setElement(Element element) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        List<Attribute> attributes = element.attributes();
        for (Attribute attribute : attributes) {
            Class<MethodParameterDom> clz = MethodParameterDom.class;
            Method mt = clz.getMethod("set" + Help.toUpperCaseFirstOne(attribute.getName()), String.class);
            mt.invoke(this, attribute.getValue());
        }
        if(sen!=null) {
            sen = sen.toUpperCase();
            if (sen.equals("AUTO")) {
                sen = BaseTools.getInstance().getAutoSen(name);
            }
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getRequired() {
        return required;
    }

    public void setRequired(String required) {
        this.required = required;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public String getSen() {
        return sen;
    }

    public void setSen(String sen) {
        this.sen = sen;
    }
}
