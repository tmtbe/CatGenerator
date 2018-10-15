package com.huiyi.plugin.doms;

import com.huiyi.plugin.tools.BaseTools;
import com.huiyi.plugin.tools.JavaTool;
import org.dom4j.Element;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class ProtoDom {
    private HashMap<String, EnvironmentDom> environmentDoms;
    private ArrayList<MethodParameterDom> baseParamDoms;
    private ArrayList<ControllerDom> controllerDoms;
    private ArrayList<ModelDom> modelDoms;
    private HashMap<String, ModelDom> allModelDomMap;
    private ArrayList<MarcoFileDom> marcoFileDoms;
    private ArrayList<ModelDom> compatibleGenericsModelDoms;
    private ArrayList<ControllerDom> compatibleGenericsControllerDoms;
    private BaseTools baseTools;
    private JavaTool javaTool;
    private String file_name;
    public ProtoDom() {
        javaTool = new JavaTool();
        baseParamDoms = new ArrayList<>();
        environmentDoms = new HashMap<>();
        controllerDoms = new ArrayList<>();
        modelDoms = new ArrayList<>();
        marcoFileDoms = new ArrayList<>();
        compatibleGenericsModelDoms = new ArrayList<>();
        compatibleGenericsControllerDoms = new ArrayList<>();
        baseTools = new BaseTools();
        allModelDomMap = new HashMap<>();
    }

    public ArrayList<ControllerDom> getControllerDoms() {
        return controllerDoms;
    }

    public void setControllerDoms(Element element) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        ControllerDom controllerDom = new ControllerDom();
        controllerDom.setElement(element);
        controllerDoms.add(controllerDom);
    }

    public ArrayList<ModelDom> getModelDoms() {
        return modelDoms;
    }

    public void setModelDoms(Element element) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        ModelDom modelDom = new ModelDom();
        modelDom.setElement(element);
        modelDoms.add(modelDom);
    }

    public ArrayList<MarcoFileDom> getMarcoFileDoms() {
        return marcoFileDoms;
    }

    public void setMarcoFileDoms(Element element) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        MarcoFileDom marcoFileDom = new MarcoFileDom();
        marcoFileDom.setElement(element);
        marcoFileDoms.add(marcoFileDom);
    }

    public ArrayList<MethodParameterDom> getBaseParamDoms() {
        return baseParamDoms;
    }

    public void setBaseParamDoms(Element element) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        Iterator param = element.elementIterator();
        while (param.hasNext()) {
            MethodParameterDom methodParameterDom = new MethodParameterDom();
            Element param_element = (Element) param.next();
            methodParameterDom.setElement(param_element);
            baseParamDoms.add(methodParameterDom);
        }
    }

    public HashMap<String, EnvironmentDom> getEnvironmentDoms() {
        return environmentDoms;
    }

    public void setEnvironmentDoms(Element element) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        Iterator envionment = element.elementIterator();
        while (envionment.hasNext()) {
            EnvironmentDom environmentDom = new EnvironmentDom();
            Element envionment_element = (Element) envionment.next();
            environmentDom.setElement(envionment_element);
            environmentDoms.put(environmentDom.getLanguage(), environmentDom);
        }
    }

    public ArrayList<ModelDom> getCompatibleGenericsModelDoms() {
        return compatibleGenericsModelDoms;
    }

    public ArrayList<ControllerDom> getCompatibleGenericsControllerDoms() {
        return compatibleGenericsControllerDoms;
    }

    /**
     * 修正Model，修正泛型
     *
     * @param modelDomMap
     * @throws CloneNotSupportedException
     */
    public void compatibleGenerics(Map<String, ModelDom> modelDomMap) throws Exception {
        //清除默认的modelDoms，重新检测生成
        modelDoms.clear();
        for (ControllerDom controllerDom : controllerDoms) {
            ControllerDom clone = (ControllerDom) controllerDom.clone();
            compatibleGenericsControllerDoms.add(clone);
            for (MethodDom methodDom : clone.getMethodDoms()) {
                if(methodDom.getRep().equals("")) continue;
                int index = methodDom.getRep().indexOf("{");
                if (index > 0) {
                    String baseClass = methodDom.getRep().substring(0, index);
                    String tclass = methodDom.getRep().substring(index + 1, methodDom.getRep().length() - 1);
                    newModel(baseClass, tclass, modelDomMap);
                    methodDom.setRep(baseClass + "_" + baseTools.typeToClassString(tclass));
                    //寻找需要的model并添加进来
                    ModelDom needModel = modelDomMap.get(tclass);
                    if (needModel == null) {
                        throw new Exception(tclass + "->没有找到对应的Model");
                    }
                    modelDoms.add(needModel);
                    needModel = modelDomMap.get(baseClass + "{T}");
                    if (needModel == null) {
                        throw new Exception(baseClass + "{T}" + "->没有找到对应的Model");
                    }
                    modelDoms.add(needModel);
                } else {
                    //寻找需要的model并添加进来
                    String bastype = javaTool.baseType(methodDom.getRep());
                    if(javaTool.isBaseType(bastype)) continue;
                    ModelDom needModel = modelDomMap.get(bastype);
                    if (needModel == null) {
                        throw new Exception(javaTool.baseType(methodDom.getRep()) + "->没有找到对应的Model");
                    }
                    modelDoms.add(needModel);
                }
            }
        }
        //循环找寻子model，这里忽略检测泛型嵌套
        for (ModelDom modelDom : modelDoms) {
            allModelDomMap.put(modelDom.getName(), modelDom);
            getAllModel(modelDom, modelDomMap);
        }
        modelDoms.clear();
        modelDoms.addAll(allModelDomMap.values());
        //生成泛型兼容Model
        for (ModelDom modelDom : modelDoms) {
            //泛型嵌套的跳过
            if (modelDom.getName().indexOf("{") > 0) {
                continue;
            }
            boolean needClone = true;
            for (ModelParameterDom modelParameterDom : modelDom.getModelParameterDoms()) {
                int index = modelParameterDom.getType().indexOf("{");
                if (index > 0) {
                    needClone = false;
                    String baseClass = modelParameterDom.getType().substring(0, index);
                    String tclass = modelParameterDom.getType().substring(index + 1, modelParameterDom.getType().length() - 1);
                    newModel(baseClass, tclass, modelDomMap);
                    modelParameterDom.setType(baseClass + "_" + baseTools.typeToClassString(tclass));
                }
            }
            if (needClone) {
                compatibleGenericsModelDoms.add((ModelDom) modelDom.clone());
            }
        }

    }

    /**
     * 递归寻找Model
     *
     * @param modelDom
     */
    protected void getAllModel(ModelDom modelDom, Map<String, ModelDom> modelDomMap) throws Exception {
        for (ModelParameterDom modelParameterDom : modelDom.getModelParameterDoms()) {
            String type = modelParameterDom.getType();
            //不允许泛型嵌套
            if (type.indexOf("{") > 0) {
                throw new Exception(modelDom.getName() + "->Model中不允许使用泛型");
            }
            if (!javaTool.isBaseType(type)) {
                type = javaTool.baseType(type);
                ModelDom modelDom1 = modelDomMap.get(type);
                if (modelDom1 == null) {
                    throw new Exception(type + "->没有找到对应的Model");
                }
                allModelDomMap.put(type, modelDom1);
                getAllModel(modelDom1, modelDomMap);
            }
        }
    }

    protected void newModel(String baseClass, String tclass, Map<String, ModelDom> modelDomMap) throws Exception {
        ModelDom modelDom = modelDomMap.get(baseClass + "{T}");
        if (modelDom == null) {
            throw new Exception(baseClass + "{T}" + "->没有找到model");
        }
        ModelDom clone = (ModelDom) modelDom.clone();
        clone.setName(baseClass + "_" + baseTools.typeToClassString(tclass));
        for (ModelParameterDom modelParameterDom : clone.getModelParameterDoms()) {
            if (modelParameterDom.getType().equals("T")) {
                modelParameterDom.setType(tclass);
            }
        }
        compatibleGenericsModelDoms.add(clone);

    }

    public String getFile_name() {
        return file_name;
    }

    public void setFile_name(String file_name) {
        this.file_name = file_name;
    }
}
