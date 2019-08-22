package com.thoughtworks.plugin.doms;

import com.thoughtworks.plugin.Global;
import org.dom4j.Attribute;
import org.dom4j.Element;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

public class EnvironmentDom {
    private String language;
    private String root_path;
    private String notes;
    private String tool_class;
    private String baseUrl;
    private String base_package;
    private String controller_package;
    private String model_package;
    private String marcos_package;
    private String params_package;
    private String service_name;
    public void setElement(Element element) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        List<Attribute> attributes = element.attributes();
        for (Attribute attribute : attributes) {
            Class<EnvironmentDom> clz = EnvironmentDom.class;
            Method mt = clz.getMethod("set" + Help.toUpperCaseFirstOne(attribute.getName()), String.class);
            mt.invoke(this, attribute.getValue());
        }
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getRoot_path() {
        return root_path;
    }

    public void setRoot_path(String root_path) {
        if(root_path.equals("local")){
            root_path = Global.LocalPath;
        }
        this.root_path = root_path;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getTool_class() {
        return tool_class;
    }

    public void setTool_class(String tool_class) {
        this.tool_class = tool_class;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public String getController_package() {
        return controller_package;
    }

    public void setController_package(String controller_package) {
        this.controller_package = controller_package;
    }

    public String getModel_package() {
        return model_package;
    }

    public void setModel_package(String model_package) {
        this.model_package = model_package;
    }

    public String getMarcos_package() {
        return marcos_package;
    }

    public void setMarcos_package(String marcos_package) {
        this.marcos_package = marcos_package;
    }

    public String getParams_package() {
        return params_package;
    }

    public void setParams_package(String params_package) {
        this.params_package = params_package;
    }

    public String getService_name() {
        return service_name;
    }

    public void setService_name(String service_name) {
        this.service_name = service_name;
    }
    public String getBase_package() {
        return base_package;
    }

    public void setBase_package(String base_package) {
        this.base_package = base_package;
    }
}
