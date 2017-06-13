package com.github.liyiorg.mbg.plugin;

import org.mybatis.generator.api.CommentGenerator;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.JavaVisibility;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.api.dom.java.Parameter;
import org.mybatis.generator.api.dom.java.TopLevelClass;

import com.github.liyiorg.mbg.util.TopLevelClassUtil;

/**
 * Example 添加父类
 * 
 * @author Administrator
 *
 */
public abstract class PaginationPlugin extends PluginAdapter {

	@Override
	public boolean modelExampleClassGenerated(TopLevelClass topLevelClass,
			IntrospectedTable introspectedTable) {
		// add field, getter, setter for limit clause
		TopLevelClassUtil.addField(context.getCommentGenerator(),topLevelClass, introspectedTable,new FullyQualifiedJavaType(Long.class.getName()),"limitStart",null);
		TopLevelClassUtil.addField(context.getCommentGenerator(),topLevelClass, introspectedTable,new FullyQualifiedJavaType(Long.class.getName()),"limitEnd",null);
		addLimitMethod(topLevelClass, introspectedTable);
		return super.modelExampleClassGenerated(topLevelClass,introspectedTable);
	}
	
	
	private void addLimitMethod(TopLevelClass topLevelClass,IntrospectedTable introspectedTable) {
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
