package com.github.liyiorg.mbg.interceptor.pagination;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.ibatis.executor.parameter.ParameterHandler;
import org.apache.ibatis.executor.statement.BaseStatementHandler;
import org.apache.ibatis.executor.statement.RoutingStatementHandler;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.logging.Log;
import org.apache.ibatis.logging.LogFactory;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.scripting.defaults.DefaultParameterHandler;
import org.apache.ibatis.session.RowBounds;

import com.github.liyiorg.mbg.interceptor.pagination.Pagination.Limit;

/**
 * 
 * @author LiYi
 *
 */
public abstract class AbstractPaginationInterceptor implements Interceptor {

	private static final Pattern PATTERN_SQL_BLANK = Pattern.compile("\\s+");

	private static final String BLANK = " ";

	private static final String FIELD_DELEGATE = "delegate";

	private static final String FIELD_ROWBOUNDS = "rowBounds";

	private static final String FIELD_MAPPEDSTATEMENT = "mappedStatement";

	private static final String FIELD_SQL = "sql";

	private static Log log = LogFactory.getLog(AbstractPaginationInterceptor.class);

	public Object intercept(Invocation invocation) throws Throwable {

		Limit limit = Pagination.get();
		if (limit != null) {
			Connection connection = (Connection) invocation.getArgs()[0];
			RoutingStatementHandler statementHandler = (RoutingStatementHandler) invocation.getTarget();

			StatementHandler handler = (StatementHandler) FieldUtil.readField(statementHandler, FIELD_DELEGATE);
			MappedStatement mappedStatement = (MappedStatement) FieldUtil.readField(handler, FIELD_MAPPEDSTATEMENT);
			BoundSql boundSql = handler.getBoundSql();
			String baseSql = boundSql.getSql();
			// replace all blank
			String targetSql = replaceSqlBlank(baseSql);
			if (isSelectSql(targetSql)) {
				//获取分页 SQL
				String pagingSql = buildSelectPagingSql(targetSql, limit.getPageNo(),limit.getPageSize());
				//覆盖boundSql
				FieldUtil.writeDeclaredField(boundSql, FIELD_SQL, pagingSql);
				//获取 count(1) SQL
				String totalSql = buildSelectTotalSql(targetSql, true);
				log.debug("Pagination \nBASE_SQl:"+baseSql +"\nPAGE_SQl:"+pagingSql+"\nTOTAL_SQl:"+totalSql);
				//获取 count(1) 数量
				Long total = queryTotal(totalSql, boundSql, mappedStatement, connection);
				//设置分页数
				limit.setTotal(total);
				Pagination.set(limit);
			} else {
				log.debug("No select sql with:" + baseSql);
			}
		}
		return invocation.proceed();
	}

	@Override
	public Object plugin(Object target) {
		if (target instanceof RoutingStatementHandler) {
			try {
				if(Pagination.get() != null){
					Field delegateField =  FieldUtil.getField(RoutingStatementHandler.class,FIELD_DELEGATE);
					StatementHandler statementHandler = (StatementHandler) delegateField.get(target);
					if (statementHandler instanceof BaseStatementHandler) {
						Field rowboundsField = FieldUtil.getField(BaseStatementHandler.class,FIELD_ROWBOUNDS);
						RowBounds rowBounds = (RowBounds) rowboundsField.get(statementHandler);
						if (rowBounds == null || rowBounds == RowBounds.DEFAULT) {
							return Plugin.wrap(target, this);
						}
					}
				}
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		return target;
	}

	@Override
	public void setProperties(Properties properties) {
		// TODO Auto-generated method stub
	}

	/**
	 * 查询总记录数
	 * 
	 * @param totalSql totalSql
	 * @param boundSql boundSql
	 * @param mappedStatement mappedStatement
	 * @param connection connection
	 * @return Long
	 * @throws SQLException
	 */
	private Long queryTotal(String totalSql, BoundSql boundSql, MappedStatement mappedStatement, Connection connection)
			throws SQLException {
		List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();
		Object parameterObject = boundSql.getParameterObject();
		BoundSql totalBoundSql = new BoundSql(mappedStatement.getConfiguration(), totalSql, parameterMappings,
				parameterObject);
		ParameterHandler parameterHandler = new DefaultParameterHandler(mappedStatement, parameterObject,
				totalBoundSql);

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = connection.prepareStatement(totalSql);
			parameterHandler.setParameters(pstmt);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				return rs.getLong(1);
			}
		} finally {
			if (rs != null) {
				rs.close();
			}
			if (pstmt != null) {
				pstmt.close();
			}
		}
		return null;
	}

	/**
	 * 获取select count SQL
	 * 
	 * @param orgSql orgSql
	 * @param ignoreOrderBy ignoreOrderBy
	 * @return String
	 */
	protected String buildSelectTotalSql(String orgSql, boolean ignoreOrderBy) {
		String newSql = orgSql;
		if (ignoreOrderBy) {
			// 嵌套查询内部order by 清除
			String sql1 = orgSql.replaceAll(
					"(((?i)select)\\s+.+\\s+((?i)from)\\s*\\(.*\\s+)(((?i)order\\s+by)(\\s+[^\\)]*))\\)", "$1)");
			//log.debug("inner order by remove \nbase:" + orgSql + " \ntosql:" + sql1);
			// 非嵌套查询 order by 清除
			String sql2 = sql1.replaceAll("(.*)(\\s+(?i)order\\s+by)\\s+.*", "$1");
			//log.debug("out order by remove \nbase:" + sql1 + " \ntosql:" + sql2);
			newSql = sql2;
		}
		// 带having 语句
		if (newSql.matches(".*\\s+((?i)having)\\s+.*")) {
			newSql = String.format("select count(1) from(%s)", newSql);
		} else {
			newSql = newSql.replaceFirst("\\s+((?i)from)\\s+", " from ");
			newSql = String.format("select count(1)%s", newSql.substring(newSql.indexOf(" from ")));
		}
		return newSql;
	}

	/**
	 * 生成分页查询语句
	 * 
	 * @param targetSql
	 * @param pageNo pageNo
	 * @param pageSize pageSize
	 * @return String
	 */
	protected abstract String buildSelectPagingSql(String targetSql, int pageNo, int pageSize);

	private String replaceSqlBlank(String originalSql) {
		Matcher matcher = PATTERN_SQL_BLANK.matcher(originalSql);
		return matcher.replaceAll(BLANK);
	}

	/**
	 * select 语句判断
	 * 
	 * @param sql sql
	 * @return boolean
	 */
	private static boolean isSelectSql(String sql) {
		if (sql != null) {
			return sql.matches("\\s*((?i)select)\\s+.+\\s+((?i)from)\\s+.*");
		}
		return false;
	}

	protected static class FieldUtil {

		/**
		 * 设置field 值
		 * 
		 * @param target target
		 * @param fieldName fieldName
		 * @param value value
		 * @throws IllegalAccessException
		 */
		protected static void writeDeclaredField(Object target, String fieldName, Object value)
				throws IllegalAccessException {
			if (target == null) {
				throw new IllegalArgumentException("target object must not be null");
			}
			Class<?> cls = target.getClass();
			Field field = getField(cls, fieldName);
			if (field == null) {
				throw new IllegalArgumentException("Cannot locate declared field " + cls.getName() + "." + fieldName);
			}
			field.set(target, value);
		}

		/**
		 * 读取Field 值
		 * 
		 * @param target target
		 * @param fieldName fieldName
		 * @return Object
		 * @throws IllegalAccessException
		 */
		protected static Object readField(Object target, String fieldName) throws IllegalAccessException {
			if (target == null) {
				throw new IllegalArgumentException("target object must not be null");
			}
			Class<?> cls = target.getClass();
			Field field = getField(cls, fieldName);
			if (field == null) {
				throw new IllegalArgumentException("Cannot locate field " + fieldName + " on " + cls);
			}
			if (!field.isAccessible()) {
				field.setAccessible(true);
			}
			return field.get(target);
		}

		/**
		 * 获取Field
		 * 
		 * @param cls cls
		 * @param fieldName fieldName
		 * @return Field
		 */
		protected static Field getField(final Class<?> cls, String fieldName) {
			for (Class<?> acls = cls; acls != null; acls = acls.getSuperclass()) {
				try {
					Field field = acls.getDeclaredField(fieldName);
					if (!Modifier.isPublic(field.getModifiers())) {
						field.setAccessible(true);
						return field;
					}
				} catch (NoSuchFieldException ex) {
					// ignore
				}
			}
			return null;
		}
	}
	
}
