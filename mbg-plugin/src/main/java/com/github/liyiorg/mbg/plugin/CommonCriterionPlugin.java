package com.github.liyiorg.mbg.plugin;

import java.util.List;

import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.InnerClass;
import org.mybatis.generator.api.dom.java.TopLevelClass;
import org.mybatis.generator.logging.Log;
import org.mybatis.generator.logging.LogFactory;

/**
 * 
 * 设置Example 中使用公用的Criterion类
 * 
 * @author LiYi
 *
 */
public class CommonCriterionPlugin extends PluginAdapter {

	private static Log log = LogFactory.getLog(CommonCriterionPlugin.class);
	
	private static final String CriterionClass = "com.github.liyiorg.mbg.support.example.Criterion";

	@Override
	public boolean modelExampleClassGenerated(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {

		List<InnerClass> innerClassList = topLevelClass.getInnerClasses();
		boolean removed = false;
		for (InnerClass innerClass : innerClassList) {
			if ("Criterion".equals(innerClass.getType().getShortName())) {
				topLevelClass.addImportedType(CriterionClass);
				innerClassList.remove(innerClass);
				removed = true;
				break;
			}
		}
		if(!removed){
			log.debug("Not find InnerClass Criterion");
		}
		return super.modelExampleClassGenerated(topLevelClass, introspectedTable);
	}

	@Override
	public boolean validate(List<String> arg0) {
		return true;
	}

}
