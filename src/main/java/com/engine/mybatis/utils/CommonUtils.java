package com.engine.mybatis.utils;

import com.intellij.execution.ui.ConsoleView;
import com.intellij.execution.ui.ConsoleViewContentType;

import java.util.ArrayList;
import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.util.JdbcConstants;
import java.util.List;

public class CommonUtils {
    public static void buildSql(ConsoleView console, String str){
        List<String> mybatisSQLTexts = new ArrayList<>();
        while (str.lastIndexOf("Preparing: ") > -1) {

            // 因为是从尾部截取，所以需要从数组的头部添加
            mybatisSQLTexts.add(str.substring(str.lastIndexOf("Preparing: ")));
            str = str.substring(0, str.lastIndexOf("Preparing: "));
        }

        if(mybatisSQLTexts.size()==0){
            console.print("未能正确转化Sql"+ "\n", ConsoleViewContentType.LOG_INFO_OUTPUT);
            return;
        }
        // 将数组中的字符串挨个处理，以数组形式返回
        for (int i = 0; i < mybatisSQLTexts.size(); i++) {
            fun(console,mybatisSQLTexts.get(i));
        }
    }

    //单句的问号生成SQL
    public static void fun(ConsoleView console ,String textVa){
// 获取带问号的SQL语句
        int statementStartIndex = textVa.indexOf("Preparing: ");
        int statementEndIndex = textVa.length()-1;
        for(int i = statementStartIndex; i < textVa.length(); i++) {
            if(textVa.charAt(i) == '\n') {
                statementEndIndex = i;
                break;
            }
        }
        String statementStr = textVa.substring(statementStartIndex+"Preparing: ".length(), statementEndIndex);
        //获取参数
        int parametersStartIndex = textVa.indexOf("Parameters: ");

        if(parametersStartIndex==-1){
            console.print("不能转换SQL,缺少Parameters参数，请检查!"+ "\n", ConsoleViewContentType.LOG_INFO_OUTPUT);
            return;
        }
        int parametersEndIndex = textVa.length();

        for(int i = parametersStartIndex; i < textVa.length(); i++) {
            if(textVa.charAt(i) == '\n') {
                parametersEndIndex = i;
                break;
            }
        }
        String parametersStr = textVa.substring(parametersStartIndex+"Parameters: ".length(), parametersEndIndex);
        String[] parametersStr1 = parametersStr.split(",");
        if(calcStrCount(statementStr) != parametersStr1.length){
            console.print("参数不匹配,请检查!"+ "\n", ConsoleViewContentType.LOG_INFO_OUTPUT);

            return;
        }
        String tempStr = "",typeStr="";

        for(int i = 0; i < parametersStr1.length; i++) {

            // 如果数据中带括号将使用其他逻辑
            tempStr = parametersStr1[i].substring(0, parametersStr1[i].indexOf("("));

            // 获取括号中内容
            typeStr = parametersStr1[i].substring(parametersStr1[i].indexOf("(")+1,parametersStr1[i].indexOf(")"));

            // 如果为字符类型
            if (  "String".equals(typeStr) ||   "Timestamp".equals(typeStr)) {

                statementStr = statementStr.replaceFirst("\\?", "'"+tempStr.trim()+"'");
            }else{

                // 数值类型
                statementStr = statementStr.replaceFirst("\\?", tempStr.trim());
            }
        }
        console.print("-- ---------------------------------------------------------------------------------------------------------------------"+ "\n", ConsoleViewContentType.USER_INPUT);

        console.print("-- ---------SQL转换成功开始输出-----------"+ "\n", ConsoleViewContentType.USER_INPUT);
        console.print(SQLUtils.format(statementStr, JdbcConstants.MYSQL)+ "\n", ConsoleViewContentType.LOG_INFO_OUTPUT);

        console.print("-- ---------------------------------------------------------------------------------------------------------------------"+ "\n\n\n", ConsoleViewContentType.USER_INPUT);





    }

    /**
     * 计算?出现次数
     */
    public static int calcStrCount(String str){
        int count =0;
        int origialLength = str.length();
        str = str.replace("?", "");
        int newLength = str.length();

        count = origialLength - newLength;
        return count;
    }
}
