package com.huiyi.plugin.doms;

import com.huiyi.plugin.tools.BaseTools;
import org.dom4j.Attribute;
import org.dom4j.Element;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MethodDom implements Cloneable {
    private String name;
    private String url;
    private String method;
    private String methodEx = "";
    private String req = "";
    private String rep = "";
    private String des;
    private ArrayList<MethodParameterDom> methodParameterDoms;

    public MethodDom() {
        methodParameterDoms = new ArrayList<>();
    }

    public void setElement(Element element) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        List<Attribute> attributes = element.attributes();
        for (Attribute attribute : attributes) {
            Class<MethodDom> clz = MethodDom.class;
            Method mt = clz.getMethod("set" + Help.toUpperCaseFirstOne(attribute.getName()), String.class);
            mt.invoke(this, attribute.getValue());
        }
        Iterator param = element.elementIterator();
        while (param.hasNext()) {
            MethodParameterDom methodParameterDom = new MethodParameterDom();
            Element param_element = (Element) param.next();
            methodParameterDom.setElement(param_element);
            methodParameterDoms.add(methodParameterDom);
        }
        this.name = BaseTools.getInstance().toLowerCaseFirstOne(this.name);
        if (this.url == null || this.url.isEmpty()) {
            this.url = this.name;
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

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getRep() {
        return rep;
    }

    public void setRep(String rep) {
        this.rep = rep;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public ArrayList<MethodParameterDom> getMethodParameterDoms() {
        return methodParameterDoms;
    }

    public void setMethodParameterDoms(ArrayList<MethodParameterDom> methodParameterDoms) {
        this.methodParameterDoms = methodParameterDoms;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        MethodDom clone = null;
        clone = (MethodDom) super.clone();
        clone.methodParameterDoms = new ArrayList<>();
        for (MethodParameterDom methodParameterDom : methodParameterDoms) {
            clone.methodParameterDoms.add((MethodParameterDom) methodParameterDom.clone());
        }
        return clone;
    }

    public String getReq() {
        return req;
    }

    public void setReq(String req) {
        this.req = req;
    }

    public String getMethodEx() {
        return methodEx;
    }

    public void setMethodEx(String methodEx) {
        this.methodEx = methodEx;
    }
}
