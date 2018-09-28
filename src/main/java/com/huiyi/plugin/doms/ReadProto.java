package com.huiyi.plugin.doms;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.apache.maven.plugin.logging.Log;
public class ReadProto {
    private ArrayList<File> files;
    private ArrayList<ProtoDom> protoDoms;
    private Map<String, ModelDom> modelDoms;

    public ReadProto() {
        files = new ArrayList<>();
        protoDoms = new ArrayList<>();
        modelDoms = new HashMap<>();
    }

    /**
     * 添加一个xml
     *
     * @param readfile
     */
    public void addXml(File readfile) {
        files.add(readfile);
    }

    /**
     * 执行
     */
    public void run(Log log,String environmentName) throws Exception {
        //获取所有的proto
        for (File file : files) {
            getProto(file);
        }
        //合并proto的model
        for (ProtoDom protoDom : protoDoms) {
            for (ModelDom modelDom : protoDom.getModelDoms()) {
                if (modelDoms.containsKey(modelDom.getName())) {
                    throw new Exception(modelDom.getName() + "->Model重复了");
                }
                modelDoms.put(modelDom.getName(), modelDom);
            }
        }
        //输出结果了
        VelocityEngine ve = new VelocityEngine();
        ve.setProperty("file.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
        ve.init();
        for (ProtoDom protoDom : protoDoms) {
            protoDom.compatibleGenerics(modelDoms);
            for (Map.Entry<String, EnvironmentDom> entry : protoDom.getEnvironmentDoms().entrySet()) {
                EnvironmentDom environment = protoDom.getEnvironmentDoms().get(entry.getKey());
                if(!environmentName.equals("all")){
                    if(!environment.getLanguage().equals(environmentName)){
                        continue;
                    }
                }
                log.info("开始生成"+environment.getLanguage()+"代码");
                Template t = ve.getTemplate("catgen/" + entry.getKey() + ".vm");
                VelocityContext ctx = new VelocityContext();
                ctx.put("environment", environment);
                ctx.put("protoDom", protoDom);
                Class clazz = Class.forName(environment.getTool_class());
                Object tool = clazz.newInstance();
                ctx.put("tool", tool);
                StringWriter sw = new StringWriter();
                t.merge(ctx, sw);
                log.info("生成" + entry.getKey() + "协议");
                readResult(sw, environment,log);
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
    private void readResult(StringWriter message, EnvironmentDom environmentDom,Log log) throws IOException, DocumentException {
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
                FileWriter writer = new FileWriter(fileName);
                writer.write(element.getText().trim());
                writer.close();
                log.info("输出文件：" + fileName);
            }
        }

    }

}
