package com.github.liyiorg.mbg.plugin.pagination;

import java.util.List;

import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.TextElement;
import org.mybatis.generator.api.dom.xml.XmlElement;

import com.github.liyiorg.mbg.bean.DatabaseType;

/**
 * 
 * @author LiYi
 *
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
		if (introspectedTable.hasBLOBColumns()) {
			XmlElement isNotNullElement = new XmlElement("if");
			isNotNullElement.addAttribute(new Attribute("test", "limitStart != null"));
			isNotNullElement.addElement(new TextElement("limit ${limitStart} , ${limitEnd}"));
			element.addElement(isNotNullElement);
		}
		return super.sqlMapSelectByExampleWithBLOBsElementGenerated(element, introspectedTable);
	}

	public boolean validate(List<String> warnings) {
		return true;
	}

}
