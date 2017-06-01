package com.github.liyiorg.mbg.plugin;

import java.util.List;

import org.mybatis.generator.api.CommentGenerator;
import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.JavaVisibility;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.api.dom.java.Parameter;
import org.mybatis.generator.api.dom.java.TopLevelClass;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.TextElement;
import org.mybatis.generator.api.dom.xml.XmlElement;

import com.github.liyiorg.mbg.util.TopLevelClassUtil;
/**
 * <pre>
 * add pagination using mysql limit.
 * This class is only used in ibator code generator.
 * </pre>
 */
public class PostgreSQLPaginationPlugin extends PluginAdapter {
	@Override
	public void initialized(IntrospectedTable introspectedTable) {
		//添加接口
		properties.setProperty(ExampleSuperPlugin.EXAMPLE_SUPER_INTERFACES, "com.github.liyiorg.mbg.suport.LimitInterface");
		super.initialized(introspectedTable);
	}

	@Override
	public boolean modelExampleClassGenerated(TopLevelClass topLevelClass,
			IntrospectedTable introspectedTable) {
		// add field, getter, setter for limit clause
		TopLevelClassUtil.addField(context.getCommentGenerator(),topLevelClass, introspectedTable,new FullyQualifiedJavaType(Long.class.getName()),"limitStart",null);
		TopLevelClassUtil.addField(context.getCommentGenerator(),topLevelClass, introspectedTable,new FullyQualifiedJavaType(Long.class.getName()), "limitEnd",null);
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
	
	
	@Override
	public boolean sqlMapSelectByExampleWithoutBLOBsElementGenerated(
			XmlElement element, IntrospectedTable introspectedTable) {
		
		XmlElement isNotNullElement = new XmlElement("if"); //$NON-NLS-1$ 
        isNotNullElement.addAttribute(new Attribute("test", "limitStart != null")); //$NON-NLS-1$ //$NON-NLS-2$ 
        isNotNullElement.addElement(new TextElement("limit ${limitStart} offset ${limitEnd}")); 
        element.addElement(isNotNullElement);
        return super.sqlMapUpdateByExampleWithoutBLOBsElementGenerated(element, introspectedTable); 
	}
	
	@Override
	public boolean sqlMapSelectByExampleWithBLOBsElementGenerated(XmlElement element,
			IntrospectedTable introspectedTable) {
		List<IntrospectedColumn> list = introspectedTable.getBLOBColumns();
		if(list != null && list.size()>0){
			XmlElement isNotNullElement = new XmlElement("if"); //$NON-NLS-1$ 
	        isNotNullElement.addAttribute(new Attribute("test", "limitStart != null")); //$NON-NLS-1$ //$NON-NLS-2$ 
	        isNotNullElement.addElement(new TextElement("limit ${limitStart} offset ${limitEnd}")); 
	        element.addElement(isNotNullElement);
		}
		return super.sqlMapSelectByExampleWithBLOBsElementGenerated(element, introspectedTable);
	}
	
	/**
	 * This plugin is always valid - no properties are required
	 */
	public boolean validate(List<String> warnings) {
		return true;
	}
	
}
