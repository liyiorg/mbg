package com.github.liyiorg.mbg.plugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.Document;
import org.mybatis.generator.api.dom.xml.Element;
import org.mybatis.generator.api.dom.xml.TextElement;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.codegen.mybatis3.MyBatis3FormattingUtilities;
import org.mybatis.generator.internal.util.StringUtility;

/**
 * 乐观锁 更新
 *
 * @author LiYi
 */
public class OptimisticLockPlugin extends PluginAdapter {

	private static String DEFAULT_LOCK_COLUMN = "lock_version";

	private static String LOCK_NAEM = "lock";

	private boolean readonly;

	@Override
	public void initialized(IntrospectedTable introspectedTable) {
		if ("VIEW".equalsIgnoreCase(introspectedTable.getTableType())) {
			readonly = true;
		} else {
			String readonly_pro = introspectedTable.getTableConfiguration().getProperty("readonly");
			readonly = StringUtility.isTrue(readonly_pro);
		}

	}

	@Override
	public boolean sqlMapDocumentGenerated(Document document, IntrospectedTable introspectedTable) {

		if (!readonly) {
			String lockVersionColumnName = null;
			String lock = introspectedTable.getTableConfiguration().getProperty(LOCK_NAEM);
			if (StringUtility.stringHasValue(lock)) {
				if (StringUtility.isTrue(lock)) {
					lockVersionColumnName = DEFAULT_LOCK_COLUMN;
				} else if (!"false".equals(lock)) {
					lockVersionColumnName = lock;
				}
			}
			if (lockVersionColumnName != null && introspectedTable.hasPrimaryKeyColumns()) {
				if (introspectedTable.getColumn(lockVersionColumnName) != null) {
					Map<String, XmlElement> updateXmlElementMap = new HashMap<>();
					for (Element e : document.getRootElement().getElements()) {
						if (e instanceof XmlElement && ((XmlElement) e).getName().equals("update")) {
							for (Attribute a : ((XmlElement) e).getAttributes()) {
								if (a.getName().equals("id")) {
									updateXmlElementMap.put(a.getValue(), (XmlElement) e);
									break;
								}
							}
						}

					}
					if (introspectedTable.hasBLOBColumns()) {
						improvementUpdateByPrimaryKeyWithBLOBs(lockVersionColumnName,
								updateXmlElementMap.get("updateByPrimaryKeyWithBLOBs"), introspectedTable);
					}
					if (introspectedTable.hasBaseColumns()) {
						improvementUpdateByPrimaryKey(lockVersionColumnName,
								updateXmlElementMap.get("updateByPrimaryKey"), introspectedTable);
					}
					if (introspectedTable.hasBaseColumns() || introspectedTable.hasBLOBColumns()) {
						XmlElement xmlElement = buildUpdateByPrimaryKeySelectiveWithOptimisticLock(
								lockVersionColumnName, introspectedTable);
						document.getRootElement().addElement(xmlElement);
					}
				} else {
					throw new RuntimeException("No column name " + lockVersionColumnName + " with table "
							+ introspectedTable.getAliasedFullyQualifiedTableNameAtRuntime());
				}
			}

		}

		return super.sqlMapDocumentGenerated(document, introspectedTable);
	}

	/**
	 * 改进 updateByPrimaryKey 或 updateByPrimaryKeyWithBLOBs 方法
	 *
	 * @param type
	 *            1 updateByPrimaryKey, 2 updateByPrimaryKeyWithBLOBs
	 * @param lock_version_column
	 * @param xmlElement
	 * @param introspectedTable
	 */
	private void improvementUpdateByPrimaryKeyMethods(int type, String lock_version_column, XmlElement xmlElement,
			IntrospectedTable introspectedTable) {
		// 构建 batchInsert
		xmlElement.getElements().clear();
		context.getCommentGenerator().addComment(xmlElement);
		xmlElement.addElement(new TextElement("update " + introspectedTable.getFullyQualifiedTableNameAtRuntime()));
		List<IntrospectedColumn> noKeyColumns = type == 1 ? introspectedTable.getBaseColumns()
				: introspectedTable.getNonPrimaryKeyColumns();
		// 构建 set 部分
		for (int i = 0; i < noKeyColumns.size(); i++) {
			IntrospectedColumn column = noKeyColumns.get(i);
			String prefix = i == 0 ? "set " : "  ";
			String escapedColumnName = MyBatis3FormattingUtilities.getEscapedColumnName(column);
			String lastFlag = i < noKeyColumns.size() - 1 ? "," : "";
			String body;
			if (column.getActualColumnName().equals(lock_version_column)) {
				body = String.format("%s%s = %s + 1%s", prefix, escapedColumnName, escapedColumnName, lastFlag);
			} else {
				body = String.format("%s%s = %s%s", prefix, escapedColumnName,
						MyBatis3FormattingUtilities.getParameterClause(column), lastFlag);
			}
			xmlElement.addElement(new TextElement(body));
		}

		// 构建 where 部分
		List<IntrospectedColumn> keyColumns = new ArrayList<>(introspectedTable.getPrimaryKeyColumns());
		keyColumns.add(introspectedTable.getColumn(lock_version_column));
		for (int i = 0; i < keyColumns.size(); i++) {
			IntrospectedColumn column = keyColumns.get(i);
			String body = String.format("%s %s = %s", i == 0 ? "where" : "  and",
					MyBatis3FormattingUtilities.getEscapedColumnName(column),
					MyBatis3FormattingUtilities.getParameterClause(column));
			xmlElement.addElement(new TextElement(body));
		}
	}

	/**
	 * 改进 updateByPrimaryKey 乐观锁模式
	 *
	 * @param lock_version_column
	 * @param xmlElement
	 * @param introspectedTable
	 */
	private void improvementUpdateByPrimaryKey(String lock_version_column, XmlElement xmlElement,
			IntrospectedTable introspectedTable) {
		improvementUpdateByPrimaryKeyMethods(1, lock_version_column, xmlElement, introspectedTable);
	}

	/**
	 * 改进 updateByPrimaryKeyWithBLOBs 乐观锁模式
	 *
	 * @param lock_version_column
	 * @param xmlElement
	 * @param introspectedTable
	 */
	private void improvementUpdateByPrimaryKeyWithBLOBs(String lock_version_column, XmlElement xmlElement,
			IntrospectedTable introspectedTable) {
		improvementUpdateByPrimaryKeyMethods(2, lock_version_column, xmlElement, introspectedTable);
	}

	/**
	 * 生成 updateByPrimaryKeySelective 乐观锁模式
	 *
	 * @param lock_version_column
	 * @param introspectedTable
	 * @return
	 */
	private XmlElement buildUpdateByPrimaryKeySelectiveWithOptimisticLock(String lock_version_column,
			IntrospectedTable introspectedTable) {

		XmlElement xmlElement = new XmlElement("update");
		xmlElement.addAttribute(new Attribute("id", "updateByPrimaryKeySelectiveWithOptimisticLock"));
		String parameterType;
		if (introspectedTable.hasBLOBColumns() && introspectedTable.getBLOBColumns().size() > 1) {
			parameterType = introspectedTable.getRecordWithBLOBsType();
		} else {
			parameterType = introspectedTable.getBaseRecordType();
		}
		xmlElement.addAttribute(new Attribute("parameterType", parameterType));
		context.getCommentGenerator().addComment(xmlElement);

		xmlElement.addElement(new TextElement("update " + introspectedTable.getFullyQualifiedTableNameAtRuntime()));
		XmlElement setXmlElement = new XmlElement("set");
		for (IntrospectedColumn column : introspectedTable.getNonPrimaryKeyColumns()) {
			if (column.getActualColumnName().equals(lock_version_column)) {
				String body = String.format("  %s = %s + 1,", MyBatis3FormattingUtilities.getEscapedColumnName(column),
						MyBatis3FormattingUtilities.getEscapedColumnName(column));
				setXmlElement.addElement(new TextElement(body));
			} else {
				XmlElement innerIfXmlElement = new XmlElement("if");
				String test = String.format("%s != null", column.getJavaProperty());
				innerIfXmlElement.addAttribute(new Attribute("test", test));
				String body = String.format("%s = %s,", MyBatis3FormattingUtilities.getEscapedColumnName(column),
						MyBatis3FormattingUtilities.getParameterClause(column));
				innerIfXmlElement.addElement(new TextElement(body));
				setXmlElement.addElement(innerIfXmlElement);
			}
		}
		xmlElement.addElement(setXmlElement);

		// 构建 where 部分
		List<IntrospectedColumn> keyColumns = introspectedTable.getPrimaryKeyColumns();
		keyColumns.add(introspectedTable.getColumn(lock_version_column));
		for (int i = 0; i < keyColumns.size(); i++) {
			IntrospectedColumn column = keyColumns.get(i);
			String body = String.format("%s %s = %s", i == 0 ? "where" : "  and",
					MyBatis3FormattingUtilities.getEscapedColumnName(column),
					MyBatis3FormattingUtilities.getParameterClause(column));
			xmlElement.addElement(new TextElement(body));
		}

		return xmlElement;
	}

	@Override
	public boolean validate(List<String> warnings) {
		return true;
	}
}
