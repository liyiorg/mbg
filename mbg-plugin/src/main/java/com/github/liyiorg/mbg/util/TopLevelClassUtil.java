package com.github.liyiorg.mbg.util;

import org.mybatis.generator.api.CommentGenerator;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.dom.java.Field;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.JavaVisibility;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.api.dom.java.Parameter;
import org.mybatis.generator.api.dom.java.TopLevelClass;

/**
 * 
 * @author LiYi
 *
 */
public class TopLevelClassUtil {
	
	/**
	 * 添加成员变量 与对应的 get set 方法
	 * @param commentGenerator
	 * @param topLevelClass
	 * @param introspectedTable
	 * @param isStatic
	 * @param isFinal
	 * @param name 成员变量名称 
	 * @param initializationString 初始值，可以为空
	 * @param addGetSet 添加get set 方法
	 */
	public static void addField(CommentGenerator commentGenerator,TopLevelClass topLevelClass,IntrospectedTable introspectedTable,boolean isStatic,boolean isFinal,FullyQualifiedJavaType fullyQualifiedJavaType, String name,String initializationString,boolean addGetSet) {
		Field field = new Field();
		field.setVisibility(JavaVisibility.PROTECTED);
		field.setType(fullyQualifiedJavaType);
		field.setName(name);
		field.setStatic(isStatic);
		field.setFinal(isFinal);
		if (initializationString != null) {
			field.setInitializationString(initializationString);
		}
		commentGenerator.addFieldComment(field, introspectedTable);
		topLevelClass.addField(field);
		//添加 GET SET 方法
		if(addGetSet){
			char c = name.charAt(0);
			String camel = Character.toUpperCase(c) + name.substring(1);
			Method method = new Method();
			method.setVisibility(JavaVisibility.PUBLIC);
			method.setName("set" + camel);
			method.addParameter(new Parameter(field.getType(), name));
			method.addBodyLine("this." + name + " = " + name + ";");
			commentGenerator.addGeneralMethodComment(method, introspectedTable);
			topLevelClass.addMethod(method);
			method = new Method();
			method.setVisibility(JavaVisibility.PUBLIC);
			method.setReturnType(field.getType());
			method.setName("get" + camel);
			method.addBodyLine("return " + name + ";");
			commentGenerator.addGeneralMethodComment(method, introspectedTable);
			topLevelClass.addMethod(method);
		}
	}
	
	

}
