package com.huiyi.plugin.tools;

import com.huiyi.plugin.doms.MethodParameterDom;
import org.apache.commons.lang.StringUtils;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JavaTool extends BaseTools {
    private Map<String, String> baseType;
    private Map<String, String> casebaseType;
    public JavaTool() {
        baseType = new HashMap<>();
        baseType.put("int", "Integer");
        baseType.put("string", "String");
        baseType.put("bool", "Boolean");
        baseType.put("float", "Float");
        baseType.put("double", "Double");
        baseType.put("date", "Date");
        baseType.put("datetime", "Date");

        casebaseType = new HashMap<>();
        casebaseType.put("int", "int");
        casebaseType.put("string", "String");
        casebaseType.put("bool", "boolean");
        casebaseType.put("float", "float");
        casebaseType.put("double", "double");
        casebaseType.put("date", "Date");
        casebaseType.put("datetime", "Date");
    }

    public String buildGetParam(List<MethodParameterDom> ... methodParameterDoms) {
        ArrayList<String> result = new ArrayList<>();
        for (List<MethodParameterDom> doms : methodParameterDoms) {
            for (MethodParameterDom dom : doms) {
                String one = MessageFormat.format("@RequestParam(name = \"{0}\",required = {1}) {2} {3}",
                        dom.getName(),
                        dom.getRequired(),
                        typeChange(dom.getType()),
                        dom.getName());
                result.add(one);
            }
        }
        return StringUtils.join(result.toArray(),", ");
    }

    public boolean isBaseType(String s) {
        if (s.equals("T")) return true;
        String type = baseType(s);
        if (baseType.containsValue(type)) {
            return true;
        } else {
            return false;
        }
    }

    //是否是泛型
    public boolean isT(String s) {
        int startA = s.indexOf("{");
        if (startA > 0) {
            return true;
        }
        return false;
    }

    //类型转换
    public String typeChange(String s) {
        int startA = s.indexOf("{");
        int startB = s.lastIndexOf("}");
        if (startA > 0) {
            String change = typeChange(s.substring(startA + 1, startB));
            s = s.substring(0, startA + 1) + change + s.substring(startB);
        }
        s = s.replaceAll("\\{", "<");
        s = s.replaceAll("\\}", ">");
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
    public String baseType(String s) {
        s = s.replaceAll("\\{", "<");
        s = s.replaceAll("\\}", ">");
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

    //基础类型
    public String baseCaseType(String s) throws Exception {
        if (casebaseType.containsKey(s)) {
            return casebaseType.get(s);
        } else{
            throw new Exception("case的类型不对");
        }
    }

    public String getTypeClass(String s) {
        int arrIndex = s.indexOf("[]");
        if (arrIndex > 0) {
            return "List.class";
        } else {
            arrIndex = s.indexOf("{");
            if (arrIndex > 0) {
                s = s.substring(0, arrIndex);
            }
            return toUpperCaseFirstOne(s) + ".class";
        }
    }

    public String getPath(String packageName) {
        return packageName.replaceAll("\\.", "/") + "/";
    }

}
