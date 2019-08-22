package com.thoughtworks.plugin.doms;

import org.dom4j.Attribute;
import org.dom4j.Element;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ModelDom implements Cloneable {
    private ArrayList<ModelParameterDom> modelParameterDoms;
    private String name;
    private String type;
    private String des;
    private String used;
    public ModelDom() {
        modelParameterDoms = new ArrayList<>();
    }

    public void setElement(Element element) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        List<Attribute> attributes = element.attributes();
        for (Attribute attribute : attributes) {
            Class<ModelDom> clz = ModelDom.class;
            Method mt = clz.getMethod("set" + Help.toUpperCaseFirstOne(attribute.getName()), String.class);
            mt.invoke(this, attribute.getValue());
        }
        Iterator param = element.elementIterator();
        while (param.hasNext()) {
            ModelParameterDom modelParameterDom = new ModelParameterDom();
            Element param_element = (Element) param.next();
            modelParameterDom.setElement(param_element);
            modelParameterDoms.add(modelParameterDom);
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

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public ArrayList<ModelParameterDom> getModelParameterDoms() {
        return modelParameterDoms;
    }

    public void setModelParameterDoms(ArrayList<ModelParameterDom> modelParameterDoms) {
        this.modelParameterDoms = modelParameterDoms;
    }

    public Object clone() throws CloneNotSupportedException {
        ModelDom clone = null;
        clone = (ModelDom) super.clone();
        clone.modelParameterDoms = new ArrayList<>();
        for (ModelParameterDom modelParameterDom : modelParameterDoms) {
            clone.modelParameterDoms.add((ModelParameterDom) modelParameterDom.clone());
        }
        return clone;
    }

    public String getUsed() {
        return used;
    }

    public void setUsed(String used) {
        this.used = used;
    }
}
