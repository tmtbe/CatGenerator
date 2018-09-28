package com.huiyi.plugin.tools;

import java.util.HashMap;
import java.util.Map;

public class JsTool extends BaseTools {
    private Map<String, String> baseType;
    public JsTool() {
        baseType = new HashMap<>();
        baseType.put("int", "int");
        baseType.put("string", "string");
        baseType.put("bool", "boolean");
        baseType.put("float", "Number");
        baseType.put("double", "Number");
    }
    public Boolean isBaseType(String s) {
        return  baseType.containsKey(s);
    }
    public String desType(String s) {
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
            return "Array.<" + result + ">";
        } else {
            return result;
        }
    }

    //类型转换
    public String typeChange(String s) {
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
            return result + "_array";
        } else {
            return result;
        }
    }

    //基础类型
    public String baseType(String s) {
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
            return toUpperCaseFirstOne(s) + ".class";
        }
    }
}
