package com.huiyi.plugin.tools;

import java.util.HashMap;
import java.util.Map;

public class JavaTool extends BaseTools {
    private Map<String, String> baseType;

    public JavaTool() {
        baseType = new HashMap<>();
        baseType.put("int", "Integer");
        baseType.put("string", "String");
        baseType.put("bool", "Boolean");
        baseType.put("float", "Float");
        baseType.put("double", "Double");
    }

    public boolean isBaseType(String s) {
        if(s.equals("T")) return true;
        String type = baseType(s);
        if(baseType.containsValue(type)){
            return true;
        }else{
            return false;
        }
    }
    //类型转换
    public String typeChange(String s) {
        int startA = s.indexOf("{");
        int startB = s.lastIndexOf("}");
        if(startA>0){
            String change = typeChange(s.substring(startA+1,startB));
            s = s.substring(0,startA+1)+change+s.substring(startB);
        }
        s = s.replaceAll("\\{","<");
        s = s.replaceAll("\\}",">");
        int arrIndex = s.indexOf("[]");
        boolean isArray = false;
        String result = "";
        if (arrIndex > 0) {
            isArray = true;
            s = s.substring(0, arrIndex);
        }
        if (baseType.containsKey(s)) {
            result = baseType.get(s);
        } else {
            result = toUpperCaseFirstOne(s);
        }
        if (isArray) {
            return "List<" + result + ">";
        } else {
            return result;
        }
    }
    //基础类型
    public String baseType(String s){
        s = s.replaceAll("\\{","<");
        s = s.replaceAll("\\}",">");
        int arrIndex = s.indexOf("[]");
        String result;
        if (arrIndex > 0) {
            s = s.substring(0, arrIndex);
        }
        if (baseType.containsKey(s)) {
            result = baseType.get(s);
        } else {
            result = toUpperCaseFirstOne(s);
        }
        return result;
    }
    public String getTypeClass(String s) {
        int arrIndex = s.indexOf("[]");
        if (arrIndex > 0) {
            return "List.class";
        } else {
            arrIndex = s.indexOf("{");
            if (arrIndex > 0) {
                s = s.substring(0,arrIndex);
            }
            return toUpperCaseFirstOne(s) + ".class";
        }
    }

    public String getPath(String packageName){
        return packageName.replaceAll("\\.","/")+"/";
    }

}
