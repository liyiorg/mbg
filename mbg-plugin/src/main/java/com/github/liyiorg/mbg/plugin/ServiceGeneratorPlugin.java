package com.github.liyiorg.mbg.plugin;

import java.io.File;
import java.util.List;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.dom.java.Interface;
import org.mybatis.generator.api.dom.java.TopLevelClass;
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
	
	Log log = LogFactory.getLog(ServiceGeneratorPlugin.class);
	
	private boolean spring = true;
	
	private String servicePackage;
	
	private static final String MbgServiceClass = "com.github.liyiorg.mbg.support.service.MbgService";
	
	private static final String MbgBLOBsServiceClass = "com.github.liyiorg.mbg.support.service.MbgBLOBsService";
	
	private static final String MbgServiceSupportClass = "com.github.liyiorg.mbg.support.service.MbgServiceSupport";

	@Override
	public void initialized(IntrospectedTable introspectedTable) {
		super.initialized(introspectedTable);
		Object pro_spring = properties.get("spring");
		if(pro_spring != null){
			spring = Boolean.valueOf(pro_spring.toString());
		}
		Object pro_servicePackage = properties.get("servicePackage");
		if(pro_servicePackage != null){
			servicePackage = pro_servicePackage.toString();
		}
	}

	@Override
	public boolean clientGenerated(Interface interfaze, TopLevelClass topLevelClass,
			IntrospectedTable introspectedTable) {
		String superClass;
		List<IntrospectedColumn> list = introspectedTable.getBLOBColumns();
		if (list != null && list.size() > 0) {
			superClass = MbgBLOBsServiceClass;
		} else {
			superClass = MbgServiceClass;
		}
		String baseRecordType = introspectedTable.getBaseRecordType();
		String exampleType = introspectedTable.getExampleType();
		List<IntrospectedColumn> columns = introspectedTable.getPrimaryKeyColumns();

		String primaryKeyType;
		if (columns != null && columns.size() == 1) {
			primaryKeyType = columns.get(0).getFullyQualifiedJavaType().getFullyQualifiedName();
		} else {
			primaryKeyType = introspectedTable.getPrimaryKeyType();
		}
		
		String serviceCode = builderService(superClass, baseRecordType, exampleType, primaryKeyType);
		String serviceCodeImpl = builderServiceImpl(superClass, baseRecordType, exampleType, primaryKeyType,spring);
		
		String serviceFilePath = servicePackage(baseRecordType).replace(".", "/")+"/"+shortClassName(baseRecordType)+"Service.java";
		String serviceImplFilePath = servicePackage(baseRecordType).replace(".", "/")+"/impl/"+shortClassName(baseRecordType)+"ServiceImpl.java";
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
	private String builderService(String superClass,String baseRecordType,String exampleType,String primaryKeyType){
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("package ").append(servicePackage(baseRecordType)).append(";").append(System.lineSeparator())
					 .append(System.lineSeparator())
					 .append("import ").append(baseRecordType).append(";").append(System.lineSeparator())
					 .append("import ").append(exampleType).append(";").append(System.lineSeparator());
		
		if(!langPackage(primaryKeyType)){
			stringBuilder.append("import ").append(primaryKeyType).append(";").append(System.lineSeparator());
		}
		
		stringBuilder.append(System.lineSeparator())
					 .append("import ").append(superClass).append(";").append(System.lineSeparator())
					 .append(System.lineSeparator())
					 .append("public interface ").append(shortClassName(baseRecordType)).append("Service extends ")
					 	.append(shortClassName(superClass))
					 	.append("<")
					 	.append(shortClassName(baseRecordType))
					 	.append(", ")
					 	.append(shortClassName(exampleType))
					 	.append(", ")
					 	.append(shortClassName(primaryKeyType))
					 	.append(">")
					 .append("{").append(System.lineSeparator())
					 .append(System.lineSeparator())
			.append("}");
		return stringBuilder.toString();
	}
	
	/**
	 * 生成service impl 代码
	 * @param superClass superClass
	 * @param baseRecordType baseRecordType
	 * @param exampleType exampleType
	 * @param primaryKeyType primaryKeyType
	 * @param spring spring
	 * @return code
	 */
	private String builderServiceImpl(String superClass,String baseRecordType,String exampleType,String primaryKeyType,boolean spring){
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("package ").append(servicePackage(baseRecordType)).append(".impl").append(";").append(System.lineSeparator())
					 .append(System.lineSeparator());
		if(spring){
			stringBuilder
			.append("import javax.annotation.PostConstruct").append(";").append(System.lineSeparator())
			.append("import javax.annotation.Resource").append(";").append(System.lineSeparator())
			.append(System.lineSeparator())
			.append("import org.springframework.stereotype.Service").append(";").append(System.lineSeparator())
			.append("import org.apache.ibatis.session.SqlSessionFactory").append(";").append(System.lineSeparator())
			.append(System.lineSeparator());
			
		}
		stringBuilder.append("import ").append(baseRecordType).append(";").append(System.lineSeparator())
					 .append("import ").append(mapperPackage()).append(".").append(shortClassName(baseRecordType)).append("Mapper").append(";").append(System.lineSeparator())
					 .append("import ").append(exampleType).append(";").append(System.lineSeparator());
		
			 if(!langPackage(primaryKeyType)){
					stringBuilder.append("import ").append(primaryKeyType).append(";").append(System.lineSeparator());
			 }
			 
		stringBuilder.append(System.lineSeparator())
					 .append("import ").append(servicePackage(baseRecordType)).append(".").append(shortClassName(baseRecordType)).append("Service").append(";").append(System.lineSeparator())
					 .append("import ").append(MbgServiceSupportClass).append(";").append(System.lineSeparator())
					 .append(System.lineSeparator());
		if(spring){
			stringBuilder.append("@Service").append(System.lineSeparator());
		}
		
		stringBuilder.append("public class ").append(shortClassName(baseRecordType)).append("ServiceImpl extends ").append(shortClassName(MbgServiceSupportClass))
					 	.append("<")
					 	.append(shortClassName(baseRecordType))
					 	.append(", ")
					 	.append(shortClassName(exampleType))
					 	.append(", ")
					 	.append(shortClassName(primaryKeyType))
					 	.append(">")
					 	.append(" implements ").append(shortClassName(baseRecordType)).append("Service")
					 .append("{").append(System.lineSeparator())
					 .append(System.lineSeparator());
		if(spring){
			stringBuilder.append("\t@Resource").append(System.lineSeparator());
		}
		String shortName = shortClassName(baseRecordType);
		String shortNameLowerCase = shortName.substring(0, 1).toLowerCase()+shortName.substring(1);
		stringBuilder.append("\tprivate ").append(shortName).append("Mapper").append(" ")
										   .append(shortNameLowerCase).append("Mapper;").append(System.lineSeparator())
										   .append(System.lineSeparator());
		
		if(spring){
			stringBuilder.append("\t@Resource").append(System.lineSeparator());
		}
		stringBuilder.append("\tprivate SqlSessionFactory sqlSessionFactory;").append(System.lineSeparator()).append(System.lineSeparator());
		
		
		if(spring){
			stringBuilder.append("\t@PostConstruct").append(System.lineSeparator())
			.append("\tprivate void initService(){").append(System.lineSeparator())
			.append("\t\tsuper.mapperName = ").append(shortName).append("Mapper").append(".class.getName();").append(System.lineSeparator())
			.append("\t\tsuper.mapper = ").append(shortNameLowerCase).append("Mapper;").append(System.lineSeparator())
			.append("\t\tsuper.sqlSessionFactory = sqlSessionFactory;").append(System.lineSeparator())
			.append("\t}");
		}else{
			stringBuilder.append("{").append(System.lineSeparator())
			.append("\t\tsuper.mapperName = ").append(shortName).append("Mapper").append(".class.getName();").append(System.lineSeparator())
			.append("}");
		}
		stringBuilder.append(System.lineSeparator())
			.append("}");
		return stringBuilder.toString();
	}
	
	/**
	 * 获取 service 包路径
	 * @param baseRecordType baseRecordType
	 * @return path
	 */
	private String servicePackage(String baseRecordType){
		if(servicePackage == null){
			String[] sp = baseRecordType.split("\\.");
			StringBuilder stringBuilder = new StringBuilder();
			for(int i = 0;i < sp.length - 2;i++){
				stringBuilder.append(sp[i]).append(".");
			}
			stringBuilder.append("service");
			return stringBuilder.toString();
		}else{
			return servicePackage;
		}
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
