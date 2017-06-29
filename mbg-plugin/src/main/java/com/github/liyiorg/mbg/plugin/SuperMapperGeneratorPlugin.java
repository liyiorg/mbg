package com.github.liyiorg.mbg.plugin;

import java.util.ArrayList;
import java.util.List;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.Interface;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.api.dom.java.TopLevelClass;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.logging.Log;
import org.mybatis.generator.logging.LogFactory;

/**
 * Mapper 父类
 * 
 * 设置属性 readonlyTables 生成只读接口Mapper <br>
 * 示例 <br>
 * readonlyTables="tableName1,tableName2"
 * @author LiYi
 *
 */
public class SuperMapperGeneratorPlugin extends PluginAdapter {
	
	private static Log log = LogFactory.getLog(SuperMapperGeneratorPlugin.class);
	
	private static final String MbgReadonlyBLOBsMapperClass = "com.github.liyiorg.mbg.support.mapper.MbgReadonlyBLOBsMapper";
	
	private static final String MbgReadonlyMapperClass = "com.github.liyiorg.mbg.support.mapper.MbgReadonlyMapper";
	
	private static final String MbgUpdateBLOBsMapperClass = "com.github.liyiorg.mbg.support.mapper.MbgUpdateBLOBsMapper";
	
	private static final String MbgUpdateMapperClass = "com.github.liyiorg.mbg.support.mapper.MbgUpdateMapper";
	
	private static final String P_readonlyTables  = "readonlyTables";
	
	protected boolean readonly;
	
	@Override
	public void initialized(IntrospectedTable introspectedTable) {
		String readonlyTables = properties.getProperty(P_readonlyTables);
		String tableName = introspectedTable.getTableConfiguration().getTableName();
		readonly = false;
		if(readonlyTables != null){
			for(String rtableName : readonlyTables.split(",")){
				if(rtableName.trim().equals(tableName)){
					readonly = true;
					break;
				}
			}
		}
		super.initialized(introspectedTable);
	}

	@Override
	public boolean clientGenerated(Interface interfaze, TopLevelClass topLevelClass,
			IntrospectedTable introspectedTable) {
		try {
			String baseRecordType = introspectedTable.getBaseRecordType();
			String exampleType = introspectedTable.getExampleType();
			List<IntrospectedColumn> columns = introspectedTable.getPrimaryKeyColumns();

			String primaryKeyType;
			if (columns != null && columns.size() == 1) {
				primaryKeyType = columns.get(0).getFullyQualifiedJavaType().getFullyQualifiedName();
			} else {
				primaryKeyType = introspectedTable.getPrimaryKeyType();
			}
			
			interfaze.addImportedType(new FullyQualifiedJavaType(baseRecordType));
			interfaze.addImportedType(new FullyQualifiedJavaType(exampleType));
			if(!primaryKeyType.startsWith("java.lang.")){
				interfaze.addImportedType(new FullyQualifiedJavaType(primaryKeyType));
			}
			
			List<String> superInterfaces = new ArrayList<String>();
			boolean blobs = introspectedTable.hasBLOBColumns();
			superInterfaces.add(blobs ? MbgReadonlyBLOBsMapperClass : MbgReadonlyMapperClass);
			if(!readonly){
				superInterfaces.add(blobs ? MbgUpdateBLOBsMapperClass : MbgUpdateMapperClass);
			}
			for(String superClass : superInterfaces){
				StringBuilder stringBuilder = new StringBuilder();
				stringBuilder.append(shortClassName(superClass))
							.append("<")
						 	.append(shortClassName(baseRecordType))
						 	.append(", ")
						 	.append(shortClassName(exampleType))
						 	.append(", ")
						 	.append(shortClassName(primaryKeyType))
						 	.append(">");
				interfaze.addImportedType(new FullyQualifiedJavaType(superClass));
				interfaze.addSuperInterface(new FullyQualifiedJavaType(stringBuilder.toString()));
				log.debug("Extend Mapper " + interfaze.getType().getFullyQualifiedName() + " with " + stringBuilder.toString());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return super.clientGenerated(interfaze, topLevelClass, introspectedTable);
	}
	
	@Override
	public boolean clientCountByExampleMethodGenerated(Method method, Interface interfaze,
			IntrospectedTable introspectedTable) {
		return false;
	}

	@Override
	public boolean clientCountByExampleMethodGenerated(Method method, TopLevelClass topLevelClass,
			IntrospectedTable introspectedTable) {
		return false;
	}

	@Override
	public boolean clientDeleteByExampleMethodGenerated(Method method, Interface interfaze,
			IntrospectedTable introspectedTable) {
		return false;
	}

	@Override
	public boolean clientDeleteByExampleMethodGenerated(Method method, TopLevelClass topLevelClass,
			IntrospectedTable introspectedTable) {
		return false;
	}

	@Override
	public boolean clientDeleteByPrimaryKeyMethodGenerated(Method method, Interface interfaze,
			IntrospectedTable introspectedTable) {
		return false;
	}

	@Override
	public boolean clientDeleteByPrimaryKeyMethodGenerated(Method method, TopLevelClass topLevelClass,
			IntrospectedTable introspectedTable) {
		return false;
	}

	@Override
	public boolean clientInsertMethodGenerated(Method method, Interface interfaze,
			IntrospectedTable introspectedTable) {
		return false;
	}

	@Override
	public boolean clientInsertMethodGenerated(Method method, TopLevelClass topLevelClass,
			IntrospectedTable introspectedTable) {
		return false;
	}

	@Override
	public boolean clientSelectByExampleWithBLOBsMethodGenerated(Method method, Interface interfaze,
			IntrospectedTable introspectedTable) {
		return false;
	}

	@Override
	public boolean clientSelectByExampleWithBLOBsMethodGenerated(Method method, TopLevelClass topLevelClass,
			IntrospectedTable introspectedTable) {
		return false;
	}

	@Override
	public boolean clientSelectByExampleWithoutBLOBsMethodGenerated(Method method, Interface interfaze,
			IntrospectedTable introspectedTable) {
		return false;
	}

	@Override
	public boolean clientSelectByExampleWithoutBLOBsMethodGenerated(Method method, TopLevelClass topLevelClass,
			IntrospectedTable introspectedTable) {
		return false;
	}

	@Override
	public boolean clientSelectByPrimaryKeyMethodGenerated(Method method, Interface interfaze,
			IntrospectedTable introspectedTable) {
		return false;
	}

	@Override
	public boolean clientSelectByPrimaryKeyMethodGenerated(Method method, TopLevelClass topLevelClass,
			IntrospectedTable introspectedTable) {
		return false;
	}

	@Override
	public boolean clientUpdateByExampleSelectiveMethodGenerated(Method method, Interface interfaze,
			IntrospectedTable introspectedTable) {
		return false;
	}

	@Override
	public boolean clientUpdateByExampleSelectiveMethodGenerated(Method method, TopLevelClass topLevelClass,
			IntrospectedTable introspectedTable) {
		return false;
	}

	@Override
	public boolean clientUpdateByExampleWithBLOBsMethodGenerated(Method method, Interface interfaze,
			IntrospectedTable introspectedTable) {
		return false;
	}

	@Override
	public boolean clientUpdateByExampleWithBLOBsMethodGenerated(Method method, TopLevelClass topLevelClass,
			IntrospectedTable introspectedTable) {
		return false;
	}

	@Override
	public boolean clientUpdateByExampleWithoutBLOBsMethodGenerated(Method method, Interface interfaze,
			IntrospectedTable introspectedTable) {
		return false;
	}

	@Override
	public boolean clientUpdateByExampleWithoutBLOBsMethodGenerated(Method method, TopLevelClass topLevelClass,
			IntrospectedTable introspectedTable) {
		return false;
	}

	@Override
	public boolean clientUpdateByPrimaryKeySelectiveMethodGenerated(Method method, Interface interfaze,
			IntrospectedTable introspectedTable) {
		return false;
	}

	@Override
	public boolean clientUpdateByPrimaryKeySelectiveMethodGenerated(Method method, TopLevelClass topLevelClass,
			IntrospectedTable introspectedTable) {
		return false;
	}

	@Override
	public boolean clientUpdateByPrimaryKeyWithBLOBsMethodGenerated(Method method, Interface interfaze,
			IntrospectedTable introspectedTable) {
		return false;
	}

	@Override
	public boolean clientUpdateByPrimaryKeyWithBLOBsMethodGenerated(Method method, TopLevelClass topLevelClass,
			IntrospectedTable introspectedTable) {
		return false;
	}

	@Override
	public boolean clientUpdateByPrimaryKeyWithoutBLOBsMethodGenerated(Method method, Interface interfaze,
			IntrospectedTable introspectedTable) {
		return false;
	}

	@Override
	public boolean clientUpdateByPrimaryKeyWithoutBLOBsMethodGenerated(Method method, TopLevelClass topLevelClass,
			IntrospectedTable introspectedTable) {
		return false;
	}

	@Override
	public boolean clientInsertSelectiveMethodGenerated(Method method, Interface interfaze,
			IntrospectedTable introspectedTable) {
		return false;
	}

	@Override
	public boolean clientInsertSelectiveMethodGenerated(Method method, TopLevelClass topLevelClass,
			IntrospectedTable introspectedTable) {
		return false;
	}

	@Override
	public boolean clientSelectAllMethodGenerated(Method method, Interface interfaze,
			IntrospectedTable introspectedTable) {
		return false;
	}

	@Override
	public boolean clientSelectAllMethodGenerated(Method method, TopLevelClass topLevelClass,
			IntrospectedTable introspectedTable) {
		return false;
	}
	
	
	
	@Override
	public boolean sqlMapDeleteByExampleElementGenerated(XmlElement element, IntrospectedTable introspectedTable) {
		if (readonly) {
			return false;
		} else {
			return super.sqlMapDeleteByExampleElementGenerated(element, introspectedTable);
		}
	}

	@Override
	public boolean sqlMapDeleteByPrimaryKeyElementGenerated(XmlElement element, IntrospectedTable introspectedTable) {
		if (readonly) {
			return false;
		} else {
			return super.sqlMapDeleteByPrimaryKeyElementGenerated(element, introspectedTable);
		}
	}

	@Override
	public boolean sqlMapInsertElementGenerated(XmlElement element, IntrospectedTable introspectedTable) {
		if (readonly) {
			return false;
		} else {
			return super.sqlMapInsertElementGenerated(element, introspectedTable);
		}
	}

	@Override
	public boolean sqlMapUpdateByExampleSelectiveElementGenerated(XmlElement element,
			IntrospectedTable introspectedTable) {
		if (readonly) {
			return false;
		} else {
			return super.sqlMapUpdateByExampleSelectiveElementGenerated(element, introspectedTable);
		}
	}

	@Override
	public boolean sqlMapUpdateByExampleWithBLOBsElementGenerated(XmlElement element,
			IntrospectedTable introspectedTable) {
		if (readonly) {
			return false;
		} else {
			return super.sqlMapUpdateByExampleWithBLOBsElementGenerated(element, introspectedTable);
		}
	}

	@Override
	public boolean sqlMapUpdateByExampleWithoutBLOBsElementGenerated(XmlElement element,
			IntrospectedTable introspectedTable) {
		if (readonly) {
			return false;
		} else {
			return super.sqlMapUpdateByExampleWithoutBLOBsElementGenerated(element, introspectedTable);
		}
	}

	@Override
	public boolean sqlMapUpdateByPrimaryKeySelectiveElementGenerated(XmlElement element,
			IntrospectedTable introspectedTable) {
		if (readonly) {
			return false;
		} else {
			return super.sqlMapUpdateByPrimaryKeySelectiveElementGenerated(element, introspectedTable);
		}
	}

	@Override
	public boolean sqlMapUpdateByPrimaryKeyWithBLOBsElementGenerated(XmlElement element,
			IntrospectedTable introspectedTable) {
		if (readonly) {
			return false;
		} else {
			return super.sqlMapUpdateByPrimaryKeyWithBLOBsElementGenerated(element, introspectedTable);
		}
	}

	@Override
	public boolean sqlMapUpdateByPrimaryKeyWithoutBLOBsElementGenerated(XmlElement element,
			IntrospectedTable introspectedTable) {
		if (readonly) {
			return false;
		} else {
			return super.sqlMapUpdateByPrimaryKeyWithoutBLOBsElementGenerated(element, introspectedTable);
		}
	}

	@Override
	public boolean sqlMapInsertSelectiveElementGenerated(XmlElement element, IntrospectedTable introspectedTable) {
		if (readonly) {
			return false;
		} else {
			return super.sqlMapInsertSelectiveElementGenerated(element, introspectedTable);
		}
	}
	
	@Override
	public boolean sqlMapExampleWhereClauseElementGenerated(XmlElement element, IntrospectedTable introspectedTable) {
		for (Attribute attribute : element.getAttributes()) {
			// 确定进入 Update_By_Example_Where_Clause
			if ("id".equals(attribute.getName()) && "Update_By_Example_Where_Clause".equals(attribute.getValue())) {
				if (readonly) {
					return false;
				}
			}
		}
		return super.sqlMapExampleWhereClauseElementGenerated(element, introspectedTable);
	}

	public boolean validate(List<String> warnings) {
		return true;
	}

	/**
	 * 获取类simple name
	 * 
	 * @param fullClassName
	 * @return
	 */
	protected String shortClassName(String fullClassName) {
		if (fullClassName != null) {
			return fullClassName.replaceAll("(.*\\.)+(.*)", "$2");
		}
		return fullClassName;
	}

}
