package com.github.liyiorg.mbg.plugin;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.dom.java.Interface;
import org.mybatis.generator.api.dom.java.TopLevelClass;
import org.mybatis.generator.internal.util.StringUtility;
import org.mybatis.generator.logging.Log;
import org.mybatis.generator.logging.LogFactory;

import com.github.liyiorg.mbg.util.MBGFileUtil;

/**
 * 生成Service 代码
 * 
 * @author LiYi
 *
 */
public class ServiceGeneratorPlugin extends SuperMapperGeneratorPlugin {
	
	private static Log log = LogFactory.getLog(ServiceGeneratorPlugin.class);
	
	private String targetPackage;
	
	private String targetImplPackage;
	
	private static final String TARGETPACKAGE_PROPERTY_NAME = "targetPackage";
	
	private static final String TARGETIMPLPACKAGE_PROPERTY_NAME = "targetImplPackage";
	
	private static final String MbgReadBLOBsServiceClass = "com.github.liyiorg.mbg.support.service.MbgReadBLOBsService";
	
	private static final String MbgReadServiceClass = "com.github.liyiorg.mbg.support.service.MbgReadService";
	
	private static final String MbgWriteBLOBsServiceClass = "com.github.liyiorg.mbg.support.service.MbgWriteBLOBsService";
	
	private static final String MbgWriteServiceClass = "com.github.liyiorg.mbg.support.service.MbgWriteService";
	
	private static final String MbgUpsertServiceSupportClass = "com.github.liyiorg.mbg.support.service.MbgUpsertService";
	
	private static final String MbgServiceSupportClass = "com.github.liyiorg.mbg.support.service.MbgServiceSupport";
	
	private static final String NoKeyClass = "com.github.liyiorg.mbg.support.model.NoKey";
	
	@Override
	public void initialized(IntrospectedTable introspectedTable) {
		//set targetPackage
		Object pro_targetPackage = properties.get(TARGETPACKAGE_PROPERTY_NAME);
		if (pro_targetPackage != null && StringUtility.stringHasValue(pro_targetPackage.toString())) {
			targetPackage = pro_targetPackage.toString();
		} else {
			//set default service package
			String baseRecordType = introspectedTable.getBaseRecordType();
			String[] sp = baseRecordType.split("\\.");
			StringBuilder stringBuilder = new StringBuilder();
			for (int i = 0; i < sp.length - 2; i++) {
				stringBuilder.append(sp[i]).append(".");
			}
			stringBuilder.append("service");
			targetPackage = stringBuilder.toString();
		}
		
		//set targetImplPackage
		Object pro_targetImplPackage = properties.get(TARGETIMPLPACKAGE_PROPERTY_NAME);
		if (pro_targetImplPackage != null && StringUtility.stringHasValue(pro_targetImplPackage.toString())) {
			targetImplPackage = pro_targetImplPackage.toString();
		} else {
			targetImplPackage = targetPackage + ".impl";
		}

		super.initialized(introspectedTable);
	}

	@Override
	public boolean clientGenerated(Interface interfaze, TopLevelClass topLevelClass,
			IntrospectedTable introspectedTable) {
		boolean blobs = introspectedTable.hasBLOBColumns();
		List<String> superInterfaces = new ArrayList<String>();
		superInterfaces.add(blobs ? MbgReadBLOBsServiceClass : MbgReadServiceClass);
		if(!readonly){
			superInterfaces.add(blobs ? MbgWriteBLOBsServiceClass : MbgWriteServiceClass);
			if (introspectedTable.getTableConfiguration().getGeneratedKey() == null) {
                superInterfaces.add(MbgUpsertServiceSupportClass);
            }
		}
		
		String baseRecordType = introspectedTable.getBaseRecordType();
		String baseRecordTypeWithBLOBs = introspectedTable.getRecordWithBLOBsType();
		if(baseRecordTypeWithBLOBs == null || introspectedTable.getBLOBColumns().size() <= 1){
			baseRecordTypeWithBLOBs = baseRecordType;
		}
		
		String primaryKeyType;
		if (introspectedTable.hasPrimaryKeyColumns()) {
			List<IntrospectedColumn> columns = introspectedTable.getPrimaryKeyColumns();
			if (columns.size() == 1) {
				primaryKeyType = columns.get(0).getFullyQualifiedJavaType().getFullyQualifiedName();
			} else {
				primaryKeyType = introspectedTable.getPrimaryKeyType();
			}
		} else {
			primaryKeyType = NoKeyClass;
		}
		
		String exampleType = introspectedTable.getExampleType();
		String serviceCode = builderService(superInterfaces, baseRecordType, baseRecordTypeWithBLOBs, exampleType, primaryKeyType);
		String serviceCodeImpl = builderServiceImpl(baseRecordType, baseRecordTypeWithBLOBs, exampleType, primaryKeyType);
		
		String serviceFilePath = targetPackage.replace(".", "/") + "/" + shortClassName(baseRecordType)	+ "Service.java";
		String serviceImplFilePath = targetImplPackage.replace(".", "/") + "/" + shortClassName(baseRecordType)	+ "ServiceImpl.java";
		File serviceFile = MBGFileUtil.getFile(serviceFilePath);
		File serviceImplFile = MBGFileUtil.getFile(serviceImplFilePath);
		
		if(!serviceFile.exists()){
			MBGFileUtil.createFile(serviceFile, serviceCode);
			log.debug("Generated file is saved as " + serviceFile.getAbsolutePath());
		}else{
			log.warn("Existing file " + serviceFile.getAbsolutePath());
		}
		
		if(!serviceImplFile.exists()){
			MBGFileUtil.createFile(serviceImplFile, serviceCodeImpl);
			log.debug("Generated file is saved as " + serviceImplFile.getAbsolutePath());
		}else{
			log.warn("Existing file " + serviceImplFile.getAbsolutePath());
		}
		
		return super.clientGenerated(interfaze, topLevelClass, introspectedTable);
	}

	public boolean validate(List<String> warnings) {
		return true;
	}
	
	/**
	 * 生成service 接口代码	
	 * @param superClass superClass
	 * @param baseRecordType baseRecordType
	 * @param exampleType exampleType
	 * @param primaryKeyType primaryKeyType
	 * @return code
	 */
	private String builderService(List<String> superInterfaces,String baseRecordType,String baseRecordTypeWithBLOBs,String exampleType,String primaryKeyType){
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("package ").append(targetPackage).append(";").append(System.lineSeparator())
					 .append(System.lineSeparator())
					 .append("import ").append(exampleType).append(";").append(System.lineSeparator())
					 .append("import ").append(baseRecordType).append(";").append(System.lineSeparator());
		if(!baseRecordType.equals(baseRecordTypeWithBLOBs)){
			stringBuilder.append("import ").append(baseRecordTypeWithBLOBs).append(";").append(System.lineSeparator());
		}
		
		if(!langPackage(primaryKeyType)){
		stringBuilder.append("import ").append(primaryKeyType).append(";").append(System.lineSeparator());
		}
		for(String superClass : superInterfaces){
		stringBuilder.append("import ").append(superClass).append(";").append(System.lineSeparator());
		}
		stringBuilder.append(System.lineSeparator())
					 .append("public interface ").append(shortClassName(baseRecordType)).append("Service extends ");
		for(int i = 0; i < superInterfaces.size(); i ++){
			
		stringBuilder.append(shortClassName(superInterfaces.get(i)))
					 	.append("<")
					 	.append(shortClassName(primaryKeyType))
					 	.append(", ")
					 	.append(shortClassName(baseRecordType))
					 	.append(", ")
					 	.append(shortClassName(baseRecordTypeWithBLOBs))
					 	.append(", ")
					 	.append(shortClassName(exampleType))
					 	.append(">");
			if( i < superInterfaces.size() - 1){
			stringBuilder.append(",");
			}
		}
		stringBuilder.append("{").append(System.lineSeparator())
					 .append(System.lineSeparator())
					 .append("}");
		return stringBuilder.toString();
	}
	
	/**
	 * 生成service impl 代码
	 * @param baseRecordType baseRecordType
	 * @param exampleType exampleType
	 * @param primaryKeyType primaryKeyType
	 * @return code
	 */
	private String builderServiceImpl(String baseRecordType,String baseRecordTypeWithBLOBs,String exampleType,String primaryKeyType){
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("package ").append(targetImplPackage).append(";").append(System.lineSeparator())
					 .append(System.lineSeparator());
		stringBuilder
					.append("import javax.annotation.Resource").append(";").append(System.lineSeparator())
					.append("import javax.annotation.PostConstruct").append(";").append(System.lineSeparator())
					.append("import org.springframework.stereotype.Service").append(";").append(System.lineSeparator())
					.append(System.lineSeparator());
			
		stringBuilder.append("import ").append(baseRecordType).append(";").append(System.lineSeparator());
		if(!baseRecordType.equals(baseRecordTypeWithBLOBs)){
		stringBuilder.append("import ").append(baseRecordTypeWithBLOBs).append(";").append(System.lineSeparator());	
		}
		stringBuilder.append("import ").append(mapperPackage()).append(".").append(shortClassName(baseRecordType)).append("Mapper").append(";").append(System.lineSeparator())
					 .append("import ").append(exampleType).append(";");
		
		if(!langPackage(primaryKeyType)){
		stringBuilder.append(System.lineSeparator())
					 .append("import ").append(primaryKeyType).append(";").append(System.lineSeparator());
		}
			 
		stringBuilder.append("import ").append(targetPackage).append(".").append(shortClassName(baseRecordType)).append("Service").append(";").append(System.lineSeparator())
					 .append(System.lineSeparator())
					 .append("import ").append(MbgServiceSupportClass).append(";").append(System.lineSeparator())
					 .append(System.lineSeparator())
					 .append("@Service").append(System.lineSeparator());
		
		String shortName = shortClassName(baseRecordType);
		stringBuilder.append("public class ").append(shortClassName(baseRecordType)).append("ServiceImpl extends ").append(shortClassName(MbgServiceSupportClass))
					 	.append("<")
					 	.append(shortName).append("Mapper")
					 	.append(", ")
					 	.append(shortClassName(primaryKeyType))
					 	.append(", ")
					 	.append(shortClassName(baseRecordType))
					 	.append(", ")
					 	.append(shortClassName(baseRecordTypeWithBLOBs))
					 	.append(", ")
					 	.append(shortClassName(exampleType))
					 	.append(">")
					 	.append(" implements ").append(shortClassName(baseRecordType)).append("Service")
					 .append("{").append(System.lineSeparator())
					 .append(System.lineSeparator());
			stringBuilder.append("\t@Resource").append(System.lineSeparator());
		String shortNameLowerCase = shortName.substring(0, 1).toLowerCase()+shortName.substring(1);
		stringBuilder.append("\tprivate ")
			.append(shortName).append("Mapper").append(" ")
			.append(shortNameLowerCase).append("Mapper;").append(System.lineSeparator())
			.append(System.lineSeparator())
			.append("\t@PostConstruct").append(System.lineSeparator())
			.append("\tprivate void initService(){").append(System.lineSeparator())
			.append("\t\tsuper.mapper = ").append(shortNameLowerCase).append("Mapper;").append(System.lineSeparator())
			.append("\t}")
			.append(System.lineSeparator())
			.append("}");
		return stringBuilder.toString();
	}
	
	/**
	 * 获取 mapper 包路径
	 * @param baseRecordType baseRecordType
	 * @return path
	 */
	private String mapperPackage(){
		String mapperPath = super.getContext().getJavaClientGeneratorConfiguration().getTargetPackage();
		return mapperPath.replace("\\", ".");
	}
	
	private boolean langPackage(String primaryKeyType){
		return primaryKeyType.startsWith("java.lang.");
	}
	
}
