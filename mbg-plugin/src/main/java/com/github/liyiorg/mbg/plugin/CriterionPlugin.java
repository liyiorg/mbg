package com.github.liyiorg.mbg.plugin;

import java.util.List;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.Field;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.InnerClass;
import org.mybatis.generator.api.dom.java.JavaVisibility;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.api.dom.java.TopLevelClass;
import org.mybatis.generator.codegen.mybatis3.MyBatis3FormattingUtilities;
import org.mybatis.generator.logging.Log;
import org.mybatis.generator.logging.LogFactory;

/**
 * 
 * @author LiYi
 *
 */
public class CriterionPlugin extends PluginAdapter {

	private static Log log = LogFactory.getLog(CriterionPlugin.class);

	private static final String MbgGeneratedCriteriaClass = "com.github.liyiorg.mbg.support.example.MbgGeneratedCriteria";

	private static final String CriterionClass = "com.github.liyiorg.mbg.support.example.Criterion";

	private static final String[] REMOVE_METHODS = "GeneratedCriteria,isValid,getAllCriteria,getCriteria,addCriterion,addCriterionForJDBCDate,addCriterionForJDBCTime"
			.split(",");

	@Override
	public boolean modelExampleClassGenerated(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
		topLevelClass.addAnnotation("@SuppressWarnings(\"unused\")");
	
		List<InnerClass> innerClassList = topLevelClass.getInnerClasses();
		boolean change = false;
		boolean removed = false;
		for (InnerClass innerClass : innerClassList) {
			if ("GeneratedCriteria".equals(innerClass.getType().getShortName())) {
				for (Field field : innerClass.getFields()) {
					// 删除criteria 成员变量
					if ("criteria".equals(field.getName())) {
						innerClass.getFields().remove(field);
						break;
					}
				}
	
				// 删除super 中已有的方法
				for (int i = 0; i < innerClass.getMethods().size(); i++) {
					Method m = innerClass.getMethods().get(i);
					for (int r = 0; r < REMOVE_METHODS.length; r++) {
						String reMethod = REMOVE_METHODS[r];
						if (reMethod.equals(m.getName())) {
							innerClass.getMethods().remove(i--);
							break;
						}
					}
				}
				topLevelClass.addImportedType(new FullyQualifiedJavaType(MbgGeneratedCriteriaClass));
				innerClass.setSuperClass(MbgGeneratedCriteriaClass);
				change = true;
				
				//补充 blobs isNull isNotNull method
				addBLOBColumnsMethods(innerClass, topLevelClass, introspectedTable);
				break;
			}
		}
		if (!change) {
			log.debug("Not find InnerClass GeneratedCriteria");
		}
	
		for (InnerClass innerClass : innerClassList) {
			if ("Criterion".equals(innerClass.getType().getShortName())) {
				if (!change) {
					topLevelClass.addImportedType(CriterionClass);
				}
				innerClassList.remove(innerClass);
				removed = true;
				break;
			}
		}
		if (!removed) {
			log.debug("Not find InnerClass Criterion");
		}
		return super.modelExampleClassGenerated(topLevelClass, introspectedTable);
	}

	private static void addBLOBColumnsMethods(InnerClass generatedCriteria, TopLevelClass topLevelClass,
			IntrospectedTable introspectedTable) {
		for (IntrospectedColumn introspectedColumn : introspectedTable.getBLOBColumns()) {
			topLevelClass.addImportedType(introspectedColumn.getFullyQualifiedJavaType());
			generatedCriteria.addMethod(getSetNullMethod(introspectedColumn));
			generatedCriteria.addMethod(getSetNotNullMethod(introspectedColumn));
		}
	}

	private static Method getSetNullMethod(IntrospectedColumn introspectedColumn) {
		return getNoValueMethod(introspectedColumn, "IsNull", "is null");
	}

	private static Method getSetNotNullMethod(IntrospectedColumn introspectedColumn) {
		return getNoValueMethod(introspectedColumn, "IsNotNull", "is not null");
	}

	private static Method getNoValueMethod(IntrospectedColumn introspectedColumn, String nameFragment,
			String operator) {
		Method method = new Method();
		method.setVisibility(JavaVisibility.PUBLIC);
		StringBuilder sb = new StringBuilder();
		sb.append(introspectedColumn.getJavaProperty());
		sb.setCharAt(0, Character.toUpperCase(sb.charAt(0)));
		sb.insert(0, "and"); //$NON-NLS-1$
		sb.append(nameFragment);
		method.setName(sb.toString());
		method.setReturnType(FullyQualifiedJavaType.getCriteriaInstance());
		sb.setLength(0);
		sb.append("addCriterion(\""); //$NON-NLS-1$
		sb.append(MyBatis3FormattingUtilities.getAliasedActualColumnName(introspectedColumn));
		sb.append(' ');
		sb.append(operator);
		sb.append("\");"); //$NON-NLS-1$
		method.addBodyLine(sb.toString());
		method.addBodyLine("return (Criteria) this;"); //$NON-NLS-1$
		return method;
	}

	@Override
	public boolean validate(List<String> arg0) {
		return true;
	}

}
