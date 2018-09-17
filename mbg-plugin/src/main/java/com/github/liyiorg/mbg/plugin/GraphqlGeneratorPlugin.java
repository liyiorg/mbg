package com.github.liyiorg.mbg.plugin;

import java.io.File;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.Interface;
import org.mybatis.generator.api.dom.java.TopLevelClass;
import org.mybatis.generator.internal.util.StringUtility;
import org.mybatis.generator.logging.Log;
import org.mybatis.generator.logging.LogFactory;

import com.github.liyiorg.mbg.util.MBGFileUtil;

/**
 * 生成Graphql type
 * 
 * table property <br>
 * targetPackage 存放目录
 * graphql true|false default true 生成graphql_type file <br>
 * graphqlIgnores 忽略的columns 多个用,号分割
 * 
 * @author LiYi
 *
 */
public class GraphqlGeneratorPlugin extends PluginAdapter {
	
	private static Log log = LogFactory.getLog(GraphqlGeneratorPlugin.class);
	
	private String targetPackage;
	
	private static final String TARGETPACKAGE_PROPERTY_NAME = "targetPackage";
	
	private static final String GRAPHQL_PROPERTY_NAME = "graphql";
	
	private static final String GRAPHQL_IGNORES_PROPERTY_NAME = "graphqlIgnores";
	
	@Override
	public void initialized(IntrospectedTable introspectedTable) {
		//set targetPackage
		targetPackage = properties.getOrDefault(TARGETPACKAGE_PROPERTY_NAME, "graphqls").toString();
		super.initialized(introspectedTable);
	}

	@Override
	public boolean clientGenerated(Interface interfaze, TopLevelClass topLevelClass,
			IntrospectedTable introspectedTable) {
		String graphql_pro = introspectedTable.getTableConfiguration().getProperty(GRAPHQL_PROPERTY_NAME);
		if(graphql_pro == null || StringUtility.isTrue(graphql_pro)){
			String baseRecordType = introspectedTable.getBaseRecordType();
			String graphqlIgnores = introspectedTable.getTableConfiguration().getProperty(GRAPHQL_IGNORES_PROPERTY_NAME);
			Set<String> ignores = new HashSet<>();
			if(StringUtility.stringHasValue(graphqlIgnores)){
				for(String ignore : graphqlIgnores.split(",")){
					ignores.add(ignore);
				}
			}
			String graphqlTypeCode = builderGraphqlTypeCode(introspectedTable, ignores);
			String graphqlFilePath = targetPackage.replace(".", "/") + "/TYPE_" + shortClassName(baseRecordType) + ".graphqls";
			File graphqlFile = MBGFileUtil.getResourcesFile(graphqlFilePath);
			MBGFileUtil.createFile(graphqlFile, graphqlTypeCode);
			log.debug("Generated file is saved as " + graphqlFile.getAbsolutePath());
		}
		return super.clientGenerated(interfaze, topLevelClass, introspectedTable);
	}

	public boolean validate(List<String> warnings) {
		return true;
	}
	
	/**
	 * 生成 graphql type code
	 * @param introspectedTable
	 * @param ignores
	 * @return
	 */
	private String builderGraphqlTypeCode(IntrospectedTable introspectedTable,Set<String> ignores){
		String baseRecordType = introspectedTable.getBaseRecordType();
		String baseRecordTypeWithBLOBs = introspectedTable.getRecordWithBLOBsType();
		boolean hasWithBLOBs = true;
		if(baseRecordTypeWithBLOBs == null || introspectedTable.getBLOBColumns().size() <= 1){
			hasWithBLOBs = false;
		}
		Map<String, List<IntrospectedColumn>> map = new LinkedHashMap<>();
		if(hasWithBLOBs){
			// WithBLOBs 方式
			map.put(shortClassName(baseRecordType), introspectedTable.getBaseColumns());
			map.put(shortClassName(baseRecordTypeWithBLOBs), introspectedTable.getAllColumns());
		}else{
			// 常规方式
			map.put(shortClassName(baseRecordType), introspectedTable.getAllColumns());
		}
		StringBuilder stringBuilder = new StringBuilder();
		for(Map.Entry<String, List<IntrospectedColumn>> entry :  map.entrySet()){
			if (StringUtility.stringHasValue(introspectedTable.getRemarks())) {
				stringBuilder.append("#").append(introspectedTable.getRemarks()).append(System.lineSeparator());
			}
			stringBuilder.append("type ").append(entry.getKey()).append("{");
			for(IntrospectedColumn column : entry.getValue()){
				// 排除忽略的字段
				if(ignores.contains(column.getActualColumnName())){
					continue;
				}
				stringBuilder.append(System.lineSeparator());
				if (StringUtility.stringHasValue(column.getRemarks())) {
					stringBuilder.append(System.lineSeparator())
								 .append("    #").append(column.getRemarks());
				}
				stringBuilder.append(System.lineSeparator())
								 .append("    ").append(column.getJavaProperty()).append(" : ");
				String scalarType = column.getFullyQualifiedJavaType().getShortName();
				switch(scalarType){
					case "Integer":
						stringBuilder.append("Int");
						break;
					case "Double":
						stringBuilder.append("Float");
						break;
					case "Byte":
						stringBuilder.append("Int");
						break;
					default:
						stringBuilder.append(scalarType);
						break;
				}
				
			}
			stringBuilder.append(System.lineSeparator()).append("}")
						 .append(System.lineSeparator()).append(System.lineSeparator());
		}
		return stringBuilder.toString();
	}
	
	/**
	 * 获取类simple name
	 * 
	 * @param fullClassName
	 * @return String
	 */
	protected String shortClassName(String fullClassName) {
		if (fullClassName != null) {
			return fullClassName.replaceAll("(.*\\.)+(.*)", "$2");
		}
		return fullClassName;
	}
	
}
