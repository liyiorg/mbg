package com.github.liyiorg.mbg.plugin.pagination;

import java.util.List;

import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.TextElement;
import org.mybatis.generator.api.dom.xml.XmlElement;

/**
 * 
 * @author LiYi
 *
 */
public class PostgreSQLPaginationPlugin extends AbstractPaginationPlugin {

	@Override
	public String getDataBaseType() {
		return "PostgreSQL";
	}

	@Override
	public boolean sqlMapSelectByExampleWithoutBLOBsElementGenerated(XmlElement element,
			IntrospectedTable introspectedTable) {

		XmlElement isNotNullElement = new XmlElement("if");
		isNotNullElement.addAttribute(new Attribute("test", "limitStart != null"));
		isNotNullElement.addElement(new TextElement("limit ${limitStart} offset ${limitEnd}"));
		element.addElement(isNotNullElement);
		return super.sqlMapUpdateByExampleWithoutBLOBsElementGenerated(element, introspectedTable);
	}

	@Override
	public boolean sqlMapSelectByExampleWithBLOBsElementGenerated(XmlElement element,
			IntrospectedTable introspectedTable) {
		if (introspectedTable.hasBLOBColumns()) {
			XmlElement isNotNullElement = new XmlElement("if");
			isNotNullElement.addAttribute(new Attribute("test", "limitStart != null"));
			isNotNullElement.addElement(new TextElement("limit ${limitStart} offset ${limitEnd}"));
			element.addElement(isNotNullElement);
		}
		return super.sqlMapSelectByExampleWithBLOBsElementGenerated(element, introspectedTable);
	}

	public boolean validate(List<String> warnings) {
		return true;
	}

}
