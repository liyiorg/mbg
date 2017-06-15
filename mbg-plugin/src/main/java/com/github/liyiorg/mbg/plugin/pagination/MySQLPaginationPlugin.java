package com.github.liyiorg.mbg.plugin.pagination;

import java.util.List;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.TextElement;
import org.mybatis.generator.api.dom.xml.XmlElement;

import com.github.liyiorg.mbg.plugin.DatabaseType;

/**
 * <pre>
 * add pagination using mysql limit.
 * This class is only used in ibator code generator.
 * </pre>
 */
public class MySQLPaginationPlugin extends AbstractPaginationPlugin {

	@Override
	public DatabaseType getDataBaseType() {
		return DatabaseType.MySQL;
	}

	@Override
	public boolean sqlMapSelectByExampleWithoutBLOBsElementGenerated(XmlElement element,
			IntrospectedTable introspectedTable) {

		XmlElement isNotNullElement = new XmlElement("if");
		isNotNullElement.addAttribute(new Attribute("test", "limitStart != null"));
		isNotNullElement.addElement(new TextElement("limit ${limitStart} , ${limitEnd}"));
		element.addElement(isNotNullElement);
		return super.sqlMapUpdateByExampleWithoutBLOBsElementGenerated(element, introspectedTable);
	}

	@Override
	public boolean sqlMapSelectByExampleWithBLOBsElementGenerated(XmlElement element,
			IntrospectedTable introspectedTable) {
		List<IntrospectedColumn> list = introspectedTable.getBLOBColumns();
		if (list != null && list.size() > 0) {
			XmlElement isNotNullElement = new XmlElement("if");
			isNotNullElement.addAttribute(new Attribute("test", "limitStart != null"));
			isNotNullElement.addElement(new TextElement("limit ${limitStart} , ${limitEnd}"));
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
