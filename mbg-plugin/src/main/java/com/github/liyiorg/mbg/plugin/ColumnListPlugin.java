package com.github.liyiorg.mbg.plugin;

import java.util.List;
import java.util.Set;

import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.JavaVisibility;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.api.dom.java.Parameter;
import org.mybatis.generator.api.dom.java.TopLevelClass;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.Element;
import org.mybatis.generator.api.dom.xml.TextElement;
import org.mybatis.generator.api.dom.xml.XmlElement;

/**
 * ColumnListPlugin 适用于<br>
 * 
 * dependency plugin <br>
 * <code>com.github.liyiorg.mbg.plugin.ExampleCPlugin</code>
 * 
 * Mapper.selectByExample(Example) <br>
 * Mapper.selectByExampleWithBLOBs(Example)
 * 
 * @author LiYi
 *
 */
public class ColumnListPlugin extends PluginAdapter {

	private static final String CUtilClass = "com.github.liyiorg.mbg.util.CUtil";

	private static final String ColumnListAbleClass = "com.github.liyiorg.mbg.support.example.ColumnListAble";

	@Override
	public boolean modelExampleClassGenerated(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
		// 添加import
		topLevelClass.addImportedType(CUtilClass);
		topLevelClass.addImportedType(ColumnListAbleClass);
		topLevelClass.addImportedType(Set.class.getName());

		topLevelClass.addSuperInterface(new FullyQualifiedJavaType(ColumnListAbleClass));

		addMethod_includeColumns(topLevelClass, introspectedTable);
		addMethod_excludeColumns(topLevelClass, introspectedTable);

		return super.modelExampleClassGenerated(topLevelClass, introspectedTable);
	}

	@Override
	public boolean sqlMapSelectByExampleWithoutBLOBsElementGenerated(XmlElement element,
			IntrospectedTable introspectedTable) {
		builderXML(element);
		return super.sqlMapSelectByExampleWithoutBLOBsElementGenerated(element, introspectedTable);
	}

	@Override
	public boolean sqlMapSelectByExampleWithBLOBsElementGenerated(XmlElement element,
			IntrospectedTable introspectedTable) {
		builderXML(element);
		return super.sqlMapSelectByExampleWithoutBLOBsElementGenerated(element, introspectedTable);
	}

	private void addMethod_includeColumns(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
		Method method = new Method();
		method.setVisibility(JavaVisibility.PUBLIC);
		method.setName("includeColumns");
		method.addParameter(new Parameter(new FullyQualifiedJavaType("C..."), "cs"));
		method.addBodyLine(String.format("CUtil.includeColumns(C.class, this, cs);"));
		context.getCommentGenerator().addGeneralMethodComment(method, introspectedTable);
		topLevelClass.addMethod(method);
	}

	private void addMethod_excludeColumns(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
		Method method = new Method();
		method.setVisibility(JavaVisibility.PUBLIC);
		method.setName("excludeColumns");
		method.addParameter(new Parameter(new FullyQualifiedJavaType("C..."), "cs"));
		method.addBodyLine(String.format("CUtil.excludeColumns(C.class, this, cs);"));
		context.getCommentGenerator().addGeneralMethodComment(method, introspectedTable);
		topLevelClass.addMethod(method);
	}

	/**
	 * 替换XML
	 * 
	 * @param element
	 *            element
	 */
	private void builderXML(XmlElement element) {
		int base_Column_List_index = 0;
		int blob_Column_List_index = 0;
		List<Element> elements = element.getElements();
		for (int i = 0; i < elements.size(); i++) {
			Element e = element.getElements().get(i);
			if (e instanceof XmlElement) {
				XmlElement exml = (XmlElement) e;
				if ("include".equals(exml.getName())) {
					for (Attribute attribute : exml.getAttributes()) {
						if ("refid".equals(attribute.getName()) && "Base_Column_List".equals(attribute.getValue())) {
							base_Column_List_index = i;
							break;
						}
						if ("refid".equals(attribute.getName()) && "Blob_Column_List".equals(attribute.getValue())) {
							blob_Column_List_index = i;
							break;
						}
					}
				}
			}
		}

		if (base_Column_List_index != 0) {
			XmlElement when = new XmlElement("when");
			when.addAttribute(new Attribute("test", "base_Column_List != null"));
			when.addElement(new TextElement("${base_Column_List}"));
			XmlElement otherwise = new XmlElement("otherwise");
			otherwise.addElement(elements.get(base_Column_List_index));
			XmlElement chooseXMLElement = new XmlElement("choose");
			chooseXMLElement.addElement(when);
			chooseXMLElement.addElement(otherwise);

			// 替换<include refid="Base_Column_List" /> 为 <choose>
			elements.remove(base_Column_List_index);
			elements.add(base_Column_List_index, chooseXMLElement);
		}

		if (blob_Column_List_index != 0) {
			XmlElement when = new XmlElement("when");
			when.addAttribute(new Attribute("test", "blob_Column_List != null"));
			when.addElement(new TextElement("${blob_Column_List}"));
			XmlElement otherwise = new XmlElement("otherwise");
			otherwise.addElement(elements.get(blob_Column_List_index));
			XmlElement chooseXMLElement = new XmlElement("choose");
			chooseXMLElement.addElement(when);
			chooseXMLElement.addElement(otherwise);

			// 替换<include refid="Blob_Column_List" /> 为 <choose>
			elements.remove(blob_Column_List_index);
			elements.add(blob_Column_List_index, chooseXMLElement);
		}
	}

	public boolean validate(List<String> warnings) {
		return true;
	}

}
