package com.github.liyiorg.mbg.plugin;

import java.util.List;

import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.TopLevelClass;

/**
 * Example 添加父类
 * @author Administrator
 *
 */
public class ExampleSuperPlugin extends PluginAdapter {
	
	public static final String EXAMPLE_SUPER_CLASS = "superClass";
	public static final String EXAMPLE_SUPER_INTERFACES = "superInterfaces";
	
	@Override
	public boolean modelExampleClassGenerated(TopLevelClass topLevelClass,
			IntrospectedTable introspectedTable) {
		//添加父类
		Object superClass = properties.get(EXAMPLE_SUPER_CLASS);
		if(superClass != null){
			topLevelClass.addImportedType(superClass.toString());
			topLevelClass.setSuperClass(superClass.toString());
		}
		//添加实现接口
		Object superInterfaces = properties.get(EXAMPLE_SUPER_INTERFACES);
		if(superInterfaces != null){
			for(String interfaceStr : superInterfaces.toString().split(";")){
				topLevelClass.addImportedType(interfaceStr);
				topLevelClass.addSuperInterface(new FullyQualifiedJavaType(interfaceStr));
			}
		}
		return super.modelExampleClassGenerated(topLevelClass,introspectedTable);
	}

	public boolean validate(List<String> warnings) {
		return true;
	}
	
}
