package com.huiyi.plugin.doms;

import org.dom4j.Attribute;
import org.dom4j.Element;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MarcoFileDom {
    private String name;
    private String des;
    private ArrayList<MarcoDom>  marcoDoms;

    public MarcoFileDom() {
        marcoDoms = new ArrayList<>();
    }
    public ArrayList<MarcoDom> getMarcoDoms(){
        return marcoDoms;
    }
    public void setMarcoDoms(ArrayList<MarcoDom> marcoDoms){
        this.marcoDoms = marcoDoms;
    }
    public void setElement(Element element) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        List<Attribute> attributes = element.attributes();
        for (Attribute attribute : attributes) {
            Class<MarcoFileDom> clz = MarcoFileDom.class;
            Method mt = clz.getMethod("set" + Help.toUpperCaseFirstOne(attribute.getName()), String.class);
            mt.invoke(this, attribute.getValue());
        }
        Iterator marco = element.elementIterator();
        while (marco.hasNext()) {
            MarcoDom marcoDom = new MarcoDom();
            Element marco_element = (Element) marco.next();
            marcoDom.setElement(marco_element);
            marcoDoms.add(marcoDom);
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
}
