package com.github.liyiorg.mbg.plugin.pagination;

import org.mybatis.generator.api.CommentGenerator;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.JavaVisibility;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.api.dom.java.Parameter;
import org.mybatis.generator.api.dom.java.TopLevelClass;

import com.github.liyiorg.mbg.plugin.DatabaseType;
import com.github.liyiorg.mbg.util.TopLevelClassUtil;

/**
 * Example 添加父类
 * 
 * @author LiYi
 *
 */
public abstract class AbstractPaginationPlugin extends PluginAdapter {
	
	private static String ExampleClass = "com.github.liyiorg.mbg.support.example.MbgExample";

	public abstract DatabaseType getDataBaseType();
	
	@Override
	public boolean modelExampleClassGenerated(TopLevelClass topLevelClass,
			IntrospectedTable introspectedTable) {
		//add DataBaseType
		TopLevelClassUtil.addField(context.getCommentGenerator(),topLevelClass, introspectedTable, false,new FullyQualifiedJavaType(String.class.getName()),"databaseType","\"" + getDataBaseType().name() + "\"",true);
		
		// add field, getter, setter for limit clause
		TopLevelClassUtil.addField(context.getCommentGenerator(),topLevelClass, introspectedTable, false,new FullyQualifiedJavaType(Long.class.getName()),"limitStart",null,true);
		TopLevelClassUtil.addField(context.getCommentGenerator(),topLevelClass, introspectedTable, false,new FullyQualifiedJavaType(Long.class.getName()),"limitEnd",null,true);
		addMethod_limit(topLevelClass, introspectedTable);
		
		// add MbgExample interface
		topLevelClass.addImportedType(ExampleClass);
		topLevelClass.addSuperInterface(new FullyQualifiedJavaType(ExampleClass));
		return super.modelExampleClassGenerated(topLevelClass,introspectedTable);
	}
	
	
	private void addMethod_limit(TopLevelClass topLevelClass,IntrospectedTable introspectedTable) {
		CommentGenerator commentGenerator = context.getCommentGenerator();
		Method method = new Method();
		method.setVisibility(JavaVisibility.PUBLIC);
		method.setName("limit");
		method.addParameter(new Parameter(new FullyQualifiedJavaType(Long.class.getName()),"limitStart"));
		method.addParameter(new Parameter(new FullyQualifiedJavaType(Long.class.getName()),"limitEnd"));
		method.addBodyLine("this.limitStart = limitStart;");
		method.addBodyLine("this.limitEnd = limitEnd;");
		commentGenerator.addGeneralMethodComment(method, introspectedTable);
		topLevelClass.addMethod(method);
	}
	
}
