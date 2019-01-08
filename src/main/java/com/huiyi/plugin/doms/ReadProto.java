package com.huiyi.plugin.doms;

import org.apache.maven.plugin.logging.Log;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.*;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class ReadProto {
    private ArrayList<File> files;
    private ArrayList<ProtoDom> protoDoms;
    private Map<String, ModelDom> modelDoms;
    private Map<String,ProtoDom> outProtoDom;
    private VelocityEngine velocityEngine;

    public ReadProto() {
        files = new ArrayList<>();
        protoDoms = new ArrayList<>();
        modelDoms = new HashMap<>();
        outProtoDom = new HashMap<>();
    }

    /**
     * 添加一个xml
     *
     * @param readfile
     */
    public void addXml(File readfile) {
        files.add(readfile);
    }

    public void ready(Log log) throws Exception {
        //获取所有的proto
        for (File file : files) {
            log.info(file.getAbsolutePath());
            getProto(file);
        }
        //合并所有model
        for (ProtoDom protoDom : protoDoms) {
            for (ModelDom modelDom : protoDom.getModelDoms()) {
                if (modelDoms.containsKey(modelDom.getName())) {
                    throw new Exception(modelDom.getName() + "->Model重复了");
                }
                modelDoms.put(modelDom.getName(), modelDom);
            }
        }
        //获取所有Out类型
        for (ProtoDom protoDom : protoDoms) {
            if(protoDom.getType()!=null&&protoDom.getType().equals("out")) {
                if(!outProtoDom.containsKey(protoDom.getOutName())){
                    ProtoDom totalProtoDom = new ProtoDom();
                    totalProtoDom.setOutName(protoDom.getOutName());
                    totalProtoDom.setFile_name(protoDom.getOutName());
                    outProtoDom.put(protoDom.getOutName(),totalProtoDom);
                }
            }
        }
        //遍历所有的out类型
        for(String key:outProtoDom.keySet()){
            Map<String, ControllerDom> controllerFileDomMap = new HashMap<>();
            Map<String, MarcoFileDom> marcoFileDomMap = new HashMap<>();
            ProtoDom totalProtoDom = outProtoDom.get(key);
            //合并OutProto的marco,controller
            for (ProtoDom protoDom : protoDoms) {
                if(protoDom.getType()!=null&&protoDom.getType().equals("out")&&protoDom.getOutName().equals(key)) {
                    for (ControllerDom controllerDom : protoDom.getControllerDoms()) {
                        if (controllerFileDomMap.containsKey(controllerDom.getName())) {
                            throw new Exception(controllerDom.getName() + "->Controller重复了");
                        }
                        controllerFileDomMap.put(controllerDom.getName(), controllerDom);
                    }
                }

                for (MarcoFileDom marcoFileDom : protoDom.getMarcoFileDoms()) {
                    if (marcoFileDomMap.containsKey(marcoFileDom.getName())) {
                        throw new Exception(marcoFileDom.getName() + "->MarcoFile重复了");
                    }
                    marcoFileDomMap.put(marcoFileDom.getName(), marcoFileDom);
                }
            }
            totalProtoDom.setControllerDoms(new ArrayList(controllerFileDomMap.values()));
            totalProtoDom.setMarcoFileDoms(new ArrayList(marcoFileDomMap.values()));
            totalProtoDom.compatibleGenerics(modelDoms);
            totalProtoDom.setBaseParamDoms(new ArrayList<>());
        }

        //其余的操作
        for (ProtoDom protoDom : protoDoms) {
            protoDom.compatibleGenerics(modelDoms);
        }
        //输出结果了
        velocityEngine = new VelocityEngine();
        velocityEngine.setProperty("file.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
        velocityEngine.init();
    }

    /**
     * 执行
     */
    public void run(Log log, String environmentName) throws Exception {
        for (ProtoDom protoDom : protoDoms) {
            if(protoDom.getType()!=null) {
                EnvironmentDom java_environmentDom = new EnvironmentDom();
                java_environmentDom.setLanguage("java");
                java_environmentDom.setRoot_path("local");
                java_environmentDom.setTool_class("com.huiyi.plugin.tools.JavaTool");
                java_environmentDom.setController_package(MessageFormat.format("com.huiyi.autogen.server.{0}", protoDom.getService()));
                java_environmentDom.setParams_package(MessageFormat.format("com.huiyi.autogen.server.{0}.params", protoDom.getService()));
                java_environmentDom.setMarcos_package(MessageFormat.format("com.huiyi.autogen.marcos.{0}", protoDom.getService()));
                java_environmentDom.setModel_package("com.huiyi.autogen.model");
                java_environmentDom.setService_name(protoDom.getService());
                java_environmentDom.setNotes("//代码生成工具生成，请勿直接修改");
                if (protoDom.getType().equals("out")) {
                    HashMap<String, EnvironmentDom> environmentDomsMap = new HashMap<>();
                    environmentDomsMap.put("java", java_environmentDom);
                    protoDom.setEnvironmentDoms(environmentDomsMap);
                } else if (protoDom.getType().equals("in")) {
                    EnvironmentDom environmentDom = new EnvironmentDom();
                    environmentDom.setLanguage("fegin");
                    environmentDom.setRoot_path("local");
                    environmentDom.setTool_class("com.huiyi.plugin.tools.JavaTool");
                    environmentDom.setController_package(MessageFormat.format("com.huiyi.autogen.fegin.{0}", protoDom.getService()));
                    environmentDom.setParams_package(MessageFormat.format("com.huiyi.autogen.server.{0}.params", protoDom.getService()));
                    environmentDom.setMarcos_package(MessageFormat.format("com.huiyi.autogen.marcos.{0}", protoDom.getService()));
                    environmentDom.setModel_package("com.huiyi.autogen.model");
                    environmentDom.setService_name(protoDom.getService());
                    environmentDom.setNotes("//代码生成工具生成，请勿直接修改");
                    HashMap<String, EnvironmentDom> environmentDomsMap = new HashMap<>();
                    environmentDomsMap.put("java", java_environmentDom);
                    environmentDomsMap.put("fegin", environmentDom);
                    protoDom.setEnvironmentDoms(environmentDomsMap);
                } else if (protoDom.getType().equals("rabbit")) {
                    EnvironmentDom environmentDom = new EnvironmentDom();
                    environmentDom.setLanguage("rabbit");
                    environmentDom.setRoot_path("local");
                    environmentDom.setTool_class("com.huiyi.plugin.tools.JavaTool");
                    environmentDom.setController_package(MessageFormat.format("com.huiyi.autogen.rabbit.{0}", protoDom.getService()));
                    environmentDom.setParams_package(MessageFormat.format("com.huiyi.autogen.rabbit.{0}.params", protoDom.getService()));
                    environmentDom.setMarcos_package(MessageFormat.format("com.huiyi.autogen.marcos.{0}", protoDom.getService()));
                    environmentDom.setModel_package("com.huiyi.autogen.model");
                    environmentDom.setService_name(protoDom.getService());
                    environmentDom.setNotes("//代码生成工具生成，请勿直接修改");
                    HashMap<String, EnvironmentDom> environmentDomsMap = new HashMap<>();
                    environmentDomsMap.put("rabbit", environmentDom);
                    protoDom.setEnvironmentDoms(environmentDomsMap);
                }
            }
            for (Map.Entry<String, EnvironmentDom> entry : protoDom.getEnvironmentDoms().entrySet()) {
                EnvironmentDom environment = protoDom.getEnvironmentDoms().get(entry.getKey());
                if (!environment.getLanguage().equals(environmentName)) {
                    continue;
                }
                log.info("开始生成 [" + protoDom.getFile_name() + "] [" + environment.getLanguage() + "] 代码");
                Template t = velocityEngine.getTemplate("catgen/" + entry.getKey() + ".vm");
                VelocityContext ctx = new VelocityContext();
                ctx.put("environment", environment);
                ctx.put("protoDom", protoDom);
                ctx.put("outProtoDomList", outProtoDom.values());
                Class clazz = Class.forName(environment.getTool_class());
                Object tool = clazz.newInstance();
                ctx.put("tool", tool);
                StringWriter sw = new StringWriter();
                t.merge(ctx, sw);
                readResult(sw, environment, log);
                if (entry.getKey().equals("fegin") || entry.getKey().equals("java") || entry.getKey().equals("rabbit")) {
                    t = velocityEngine.getTemplate("catgen/java-base.vm");
                    sw = new StringWriter();
                    t.merge(ctx, sw);
                    readResult(sw, environment, log);
                }
            }
        }
    }

    /**
     * 获取Proto
     *
     * @param file
     * @return
     * @throws CloneNotSupportedException
     */
    private void getProto(File file) throws Exception {
        SAXReader reader = new SAXReader();
        ProtoDom protoDom = new ProtoDom();
        protoDom.setFile_name(file.getName().split("\\.")[0]);
        Document document = reader.read(file);
        Element bookstore = document.getRootElement();
        protoDom.setService(bookstore.attributeValue("service"));
        protoDom.setType(bookstore.attributeValue("type"));
        protoDom.setOutName(bookstore.attributeValue("outName"));
        Iterator storeit = bookstore.elementIterator();
        while (storeit.hasNext()) {
            Element element = (Element) storeit.next();
            switch (element.getName()) {
                case "environments":
                    protoDom.setEnvironmentDoms(element);
                    break;
                case "controller":
                    protoDom.setControllerDoms(element);
                    break;
                case "model":
                    protoDom.setModelDoms(element);
                    break;
                case "marcos":
                    protoDom.setMarcoFileDoms(element);
                    break;
                case "baseParam":
                    protoDom.setBaseParamDoms(element);
                    break;
            }
        }
        protoDoms.add(protoDom);
    }

    /**
     * 读入并写文件
     *
     * @param message
     * @param environmentDom
     */
    private void readResult(StringWriter message, EnvironmentDom environmentDom, Log log) throws IOException, DocumentException {
        SAXReader reader = new SAXReader();
        Document document = reader.read(new ByteArrayInputStream(message.toString().getBytes("UTF-8")));
        Element bookstore = document.getRootElement();
        Iterator storeit = bookstore.elementIterator();
        while (storeit.hasNext()) {
            Element element = (Element) storeit.next();
            if (element.getName().equals("file")) {
                String fileName = environmentDom.getRoot_path() + element.attributeValue("name");
                fileName = fileName.replaceAll("\\\\", "/");
                int folderIndex = fileName.lastIndexOf("/");
                String path = fileName.substring(0, folderIndex);
                File file = new File(path);
                if (!file.exists()) {
                    file.mkdirs();
                }
                FileWriter writer;
                if (element.attributeValue("append") != null && element.attributeValue("append").equals("true")) {
                    writer = new FileWriter(fileName, true);
                    writer.write("\n");
                } else {
                    writer = new FileWriter(fileName);
                }
                writer.write(element.getText().trim());
                writer.close();
                log.info("输出文件：" + fileName);
            }
        }

    }

}
