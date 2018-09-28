package com.huiyi.plugin.doms;

import org.dom4j.Attribute;
import org.dom4j.Element;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ControllerDom implements Cloneable{
    private ArrayList<MethodDom> methodDoms;
    private String name;
    private String url;
    private String des;
    public ControllerDom(){
        methodDoms = new ArrayList<>();
    }
    public void setElement(Element element) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        List<Attribute> attributes = element.attributes();
        for (Attribute attribute : attributes) {
            Class<ControllerDom> clz = ControllerDom.class;
            Method mt = clz.getMethod("set" + Help.toUpperCaseFirstOne(attribute.getName()), String.class);
            mt.invoke(this, attribute.getValue());
        }
        Iterator method = element.elementIterator();
        while (method.hasNext()) {
            MethodDom methodDom = new MethodDom();
            Element method_element = (Element) method.next();
            methodDom.setElement(method_element);
            methodDoms.add(methodDom);
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public ArrayList<MethodDom> getMethodDoms() {
        return methodDoms;
    }

    public void setMethodDoms(ArrayList<MethodDom> methodDoms) {
        this.methodDoms = methodDoms;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        ControllerDom clone = null;
        clone = (ControllerDom) super.clone();
        clone.methodDoms = new ArrayList<>();
        for (MethodDom methodDom : methodDoms) {
            clone.methodDoms.add((MethodDom) methodDom.clone());
        }
        return clone;
    }
}
