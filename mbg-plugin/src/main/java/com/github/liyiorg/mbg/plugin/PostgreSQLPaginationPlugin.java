package com.github.liyiorg.mbg.plugin;

import java.util.List;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.TextElement;
import org.mybatis.generator.api.dom.xml.XmlElement;
/**
 * <pre>
 * add pagination using mysql limit.
 * This class is only used in ibator code generator.
 * </pre>
 */
public class PostgreSQLPaginationPlugin extends PluginAdapter {
	
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
