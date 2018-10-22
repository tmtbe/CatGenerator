package com.huiyi.plugin.tools;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

public class BaseTools {

    //转大写
    public String toUpperCase(String s) {
        return s.toUpperCase();
    }

    //首字母转大写其余小写
    public String toUpperCaseFirstOneOtherLow(String s) {
        return s.substring(0, 1).toUpperCase().concat(s.substring(1).toLowerCase());
    }

    //首字母转大写
    public String toUpperCaseFirstOne(String s) {
        return s.substring(0, 1).toUpperCase().concat(s.substring(1));
    }
    //类型是否为数组
    public boolean typeIsArray(String s){
        if(s.indexOf("[]")>0){
            return true;
        }else{
            return false;
        }
    }

    //大写
    public String toUpper(String s) {
        return s.toUpperCase();
    }
    //小写
    public String toLower(String s) {
        return s.toLowerCase();
    }
    /**
     * 获取文件名
     * @return String
     */
    public String getFileName(String s) {
        int arrIndex = s.indexOf("{");
        String result;
        if (arrIndex > 0) {
            s = s.substring(0, arrIndex);
        }
        result = toUpperCaseFirstOne(s);
        return result;
    }
    //构建数组
    public ArrayList<String> newStrings() {
        return new ArrayList<>();
    }

    public String explode(List<String> strings, String explode) {
        String result = "";
        for (int i = 0; i < strings.size(); i++) {
            result = result + strings.get(i);
            if (i < strings.size() - 1) {
                result += explode;
            }
        }
        return result;
    }
    public String format(String source, Object ... arguments){
        return MessageFormat.format(source,arguments);
    }

    public String typeToClassString(String s) {
        s = s.replaceAll("\\{","_");
        s = s.replaceAll("\\}","");
        s = s.replaceAll("\\[\\]","s");
        return s;

    }

    public String getMarco(String type,String value){
        if(type.toLowerCase().equals("string")){
            return MessageFormat.format("\"{0}\"",value);
        }else{
            return value;
        }
    }
}
