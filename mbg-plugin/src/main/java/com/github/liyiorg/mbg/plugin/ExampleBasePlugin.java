package com.github.liyiorg.mbg.plugin;

import java.util.List;

import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.Field;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.InnerClass;
import org.mybatis.generator.api.dom.java.JavaVisibility;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.api.dom.java.TopLevelClass;

/**
 * ExampleBasePlugin Set SuperClass
 * <code>com.github.liyiorg.mbg.support.example.MbgExample</code>
 * 
 * @author LiYi
 *
 */
public class ExampleBasePlugin extends PluginAdapter {

	private static final String MbgExampleClass = "com.github.liyiorg.mbg.support.example.MbgExample";

	private static final String[] REMOVE_METHODS = "getOredCriteria,or,or,createCriteria,setOrderByClause,getOrderByClause,setDistinct,isDistinct,clear"
			.split(",");

	private static final String[] REMOVE_FIELDS = "oredCriteria,orderByClause,distinct".split(",");

	@Override
	public boolean modelExampleClassGenerated(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
		// 添加import
		// 添加import
		topLevelClass.addImportedType("java.io.Serializable");
		topLevelClass.addImportedType(MbgExampleClass);
		topLevelClass.addImportedType(introspectedTable.getExampleType() + ".Criteria");
		topLevelClass.setSuperClass(MbgExampleClass + String.format("<%s>","Criteria"));
		
		// 添加 Serializable
		topLevelClass.addSuperInterface(new FullyQualifiedJavaType("Serializable"));
		Field field_serial = new Field("serialVersionUID",new FullyQualifiedJavaType("long"));
		field_serial.setStatic(true);
		field_serial.setFinal(true);
		field_serial.setVisibility(JavaVisibility.PRIVATE);
		field_serial.setInitializationString("1L");
		field_serial.addJavaDocLine("");
		topLevelClass.getFields().add(0, field_serial);

		// 删除 父类中已存在的 METHOD
		for (int i = 0; i < topLevelClass.getMethods().size(); i++) {
			Method method = topLevelClass.getMethods().get(i);
			for (String name : REMOVE_METHODS) {
				if (name.equals(method.getName())) {
					topLevelClass.getMethods().remove(i--);
					break;
				}
			}
		}

		// 删除 父类中已存在的 FIELD
		for (int i = 0; i < topLevelClass.getFields().size(); i++) {
			Field field = topLevelClass.getFields().get(i);
			for (String name : REMOVE_FIELDS) {
				if (name.equals(field.getName())) {
					topLevelClass.getFields().remove(i--);
					break;
				}
			}
		}

		for (Method method : topLevelClass.getMethods()) {
			if (method.isConstructor() && (method.getParameters() == null || method.getParameters().size() == 0)) {
				if (!method.getBodyLines().isEmpty()) {
					for (int i = 0; i < method.getBodyLines().size(); i++) {
						String body = method.getBodyLines().get(i);
						if (body.startsWith("oredCriteria")) {
							method.getBodyLines().remove(i);
							break;
						}
					}
				}
				break;
			}
		}
		
		//Criteria 添加 Serializable
		List<InnerClass> innerClassList = topLevelClass.getInnerClasses();
		for (InnerClass innerClass : innerClassList) {
			if ("Criteria".equals(innerClass.getType().getShortName())) {
				// 添加 Serializable
				innerClass.addSuperInterface(new FullyQualifiedJavaType("Serializable"));
				innerClass.addField(field_serial);
			}
		}
		
		return super.modelExampleClassGenerated(topLevelClass, introspectedTable);
	}

	public boolean validate(List<String> warnings) {
		return true;
	}

}
