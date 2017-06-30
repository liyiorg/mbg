package com.github.liyiorg.mbg.support.service;

import java.lang.reflect.Proxy;
import java.util.List;

import org.apache.ibatis.executor.BatchResult;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import com.github.liyiorg.mbg.bean.ModelExample;
import com.github.liyiorg.mbg.bean.Page;
import com.github.liyiorg.mbg.support.example.MbgExample;
import com.github.liyiorg.mbg.support.mapper.MbgMapper;
import com.github.liyiorg.mbg.support.mapper.MbgReadonlyBLOBsMapper;
import com.github.liyiorg.mbg.support.mapper.MbgReadonlyMapper;
import com.github.liyiorg.mbg.support.mapper.MbgUpdateBLOBsMapper;
import com.github.liyiorg.mbg.support.mapper.MbgUpdateMapper;
import com.github.liyiorg.mbg.util.GenericsUtils;

/**
 * 
 * @author LiYi
 *
 * @param <Model> Model
 * @param <Example> Example
 * @param <PrimaryKey> PrimaryKey
 */
public abstract class MbgServiceSupport<Model, Example, PrimaryKey> implements
		MbgReadonlyBLOBsService<Model, Example, PrimaryKey>, MbgUpdateBLOBsService<Model, Example, PrimaryKey> {

	protected MbgMapper<Model, Example, PrimaryKey> mapper;

	protected SqlSessionFactory sqlSessionFactory;

	protected String mapperName;

	private MbgReadonlyBLOBsMapper<Model, Example, PrimaryKey> readonlyBLOBsMapper;

	private MbgReadonlyMapper<Model, Example, PrimaryKey> readonlyMapper;

	private MbgUpdateMapper<Model, Example, PrimaryKey> updateMapper;

	private MbgUpdateBLOBsMapper<Model, Example, PrimaryKey> updateBLOBsMapper;

	private Class<?> exampleClass;

	{
		exampleClass = GenericsUtils.getSuperClassGenricType(this.getClass(), 1);
	}

	private String mapperName() {
		if (mapperName == null) {
			if (Proxy.isProxyClass(mapper.getClass())) {
				mapperName = mapper.getClass().getGenericInterfaces()[0].getTypeName();
			} else {
				mapperName = mapper.getClass().getName();
			}
		}
		return mapperName;
	}

	private MbgReadonlyBLOBsMapper<Model, Example, PrimaryKey> readonlyBLOBsMapper() {
		if (readonlyBLOBsMapper == null) {
			if (mapper instanceof MbgReadonlyBLOBsMapper) {
				readonlyBLOBsMapper = (MbgReadonlyBLOBsMapper<Model, Example, PrimaryKey>) mapper;
			}
		}
		return readonlyBLOBsMapper;
	}

	private MbgReadonlyMapper<Model, Example, PrimaryKey> readonlyMapper() {
		if (readonlyMapper == null) {
			if (mapper instanceof MbgReadonlyMapper) {
				readonlyMapper = (MbgReadonlyMapper<Model, Example, PrimaryKey>) mapper;
			}
		}
		return readonlyMapper;
	}

	private MbgUpdateMapper<Model, Example, PrimaryKey> updateMapper() {
		if (updateMapper == null) {
			if (mapper instanceof MbgUpdateMapper) {
				updateMapper = (MbgUpdateMapper<Model, Example, PrimaryKey>) mapper;
			}
		}
		return updateMapper;
	}

	private MbgUpdateBLOBsMapper<Model, Example, PrimaryKey> updateBLOBsMapper() {
		if (updateBLOBsMapper == null) {
			if (mapper instanceof MbgUpdateBLOBsMapper) {
				updateBLOBsMapper = (MbgUpdateBLOBsMapper<Model, Example, PrimaryKey>) mapper;
			}
		}
		return updateBLOBsMapper;
	}

	@Override
	public long countByExample(Example example) {
		return readonlyMapper().countByExample(example);
	}

	@Override
	public int deleteByExample(Example example) {
		return updateMapper().deleteByExample(example);
	}

	@Override
	public int deleteByPrimaryKey(PrimaryKey id) {
		return updateMapper().deleteByPrimaryKey(id);
	}

	@Override
	public int insert(Model record) {
		return updateMapper().insert(record);
	}

	@Override
	public int insertSelective(Model record) {
		return updateMapper().insertSelective(record);
	}

	@Override
	public List<Model> selectByExampleWithBLOBs(Example example) {
		return readonlyBLOBsMapper().selectByExampleWithBLOBs(example);
	}

	@Override
	public List<Model> selectByExample(Example example) {
		return readonlyMapper().selectByExample(example);
	}

	@Override
	public Model selectByPrimaryKey(PrimaryKey id) {
		return readonlyMapper().selectByPrimaryKey(id);
	}

	@Override
	public int updateByExampleSelective(Model record, Example example) {
		return updateMapper().updateByExampleSelective(record, example);
	}

	@Override
	public int updateByExampleWithBLOBs(Model record, Example example) {
		return updateBLOBsMapper().updateByExampleWithBLOBs(record, example);
	}

	@Override
	public int updateByExample(Model record, Example example) {
		return updateMapper().updateByExample(record, example);
	}

	@Override
	public int updateByPrimaryKeySelective(Model record) {
		return updateMapper().updateByPrimaryKeySelective(record);
	}

	@Override
	public int updateByPrimaryKeyWithBLOBs(Model record) {
		return updateBLOBsMapper().updateByPrimaryKeyWithBLOBs(record);
	}

	@Override
	public int updateByPrimaryKey(Model record) {
		return updateMapper().updateByPrimaryKey(record);
	}

	/**
	 * 批量操作
	 * 
	 * @param statements
	 *            MAPPER 方法,必须包含 insert,delete,update 字样
	 * @param params
	 *            POJO,MAP
	 * @return
	 */
	protected int[] batchExec(String statements, Object[] params) {
		return batchExec(new String[] { statements }, params);
	}

	/**
	 * 批量操作<br>
	 * The statements and params length must 1:N or N:N
	 * 
	 * @param statements
	 *            MAPPER 方法,必须包含 insert,delete,update 字样
	 * @param params
	 *            POJO,MAP
	 * @return
	 */
	protected int[] batchExec(String[] statements, Object[] params) {
		SqlSession sqlSession = null;
		try {
			sqlSession = sqlSessionFactory.openSession(ExecutorType.BATCH);
			for (int i = 0; i < statements.length; i++) {
				String mapperStatement = statements[i];
				int type = 0;

				String method;
				if (mapperStatement.indexOf(".") == -1) {
					// 补充完整mapper statement名称
					method = mapperStatement;
					mapperStatement = mapperName() + "." + mapperStatement;
				} else {
					method = mapperStatement.substring(mapperStatement.lastIndexOf(".") + 1);
				}
				if (method.matches("(\\w*(i?)insert)\\w*")) {
					type = 1;
				} else if (method.matches("(\\w*(i?)delete)\\w*")) {
					type = 2;
				} else if (method.matches("(\\w*(i?)update)\\w*")) {
					type = 3;
				} else {
					throw new RuntimeException(
							"BatchExec error,The statement must insert,delete,update with '" + mapperStatement + "'");
				}

				if (statements.length == 1 && params.length > 1) {
					// 参数格式 1:N ，使用单一 statement
					for (int j = 0; j < params.length; j++) {
						switch (type) {
						case 1:
							sqlSession.insert(mapperStatement, params[j]);
							break;
						case 2:
							sqlSession.delete(mapperStatement, params[j]);
							break;
						case 3:
							sqlSession.update(mapperStatement, params[j]);
							break;
						}
					}
				} else if (statements.length == params.length) {
					// 参数格式 N:N ， statement 与 params 1:1
					switch (type) {
					case 1:
						sqlSession.insert(mapperStatement, params[i]);
						break;
					case 2:
						sqlSession.delete(mapperStatement, params[i]);
						break;
					case 3:
						sqlSession.update(mapperStatement, params[i]);
						break;
					}
				} else {
					throw new RuntimeException("BatchExec error,The statements and params length must 1:N or N:N");
				}
			}

			List<BatchResult> list = sqlSession.flushStatements();
			int[] updateCount = new int[list.size()];
			int i = 0;
			for (BatchResult batchResult : list) {
				updateCount[i++] = batchResult.getUpdateCounts()[0];
			}
			return updateCount;
		} finally {
			if (sqlSession != null) {
				sqlSession.close();
			}
		}
	}

	@Override
	public int[] batchDeleteByExample(Example[] example) {
		return batchExec("deleteByExample", example);
	}

	@Override
	public int[] batchDeleteByPrimaryKey(PrimaryKey[] id) {
		return batchExec("deleteByPrimaryKey", id);
	}

	@Override
	public int[] batchInsert(Model[] record) {
		return batchExec("insert", record);
	}

	@Override
	public int[] batchInsertSelective(Model[] record) {
		return batchExec("insertSelective", record);
	}

	@Override
	public int[] batchUpdateByExampleSelective(ModelExample<Model, Example>[] modelExample) {
		return batchExec("updateByExampleSelective", modelExample);
	}

	@Override
	public int[] batchUpdateByExample(ModelExample<Model, Example>[] modelExample) {
		return batchExec("updateByExample", modelExample);
	}

	@Override
	public int[] batchUpdateByPrimaryKeySelective(Model[] record) {
		return batchExec("updateByPrimaryKeySelective", record);
	}

	@Override
	public int[] batchUpdateByPrimaryKey(Model[] record) {
		return batchExec("updateByPrimaryKey", record);
	}

	@Override
	public int[] batchUpdateByExampleWithBLOBs(ModelExample<Model, Example>[] modelExample) {
		return batchExec("updateByExampleWithBLOBs", modelExample);
	}

	@Override
	public int[] batchUpdateByPrimaryKeyWithBLOBs(Model[] record) {
		return batchExec("updateByPrimaryKeyWithBLOBs", record);
	}

	@Override
	public Page<Model> selectByExample(Example example, Integer page, Integer size) {
		return selectByExampleChooes(example, page, size, false);
	}

	@Override
	public Page<Model> selectByExampleWithBLOBs(Example example, Integer page, Integer size) {
		return selectByExampleChooes(example, page, size, true);
	}

	/**
	 * 分页查询
	 * 
	 * @param example
	 *            example可以为空
	 * @param page
	 *            页码
	 * @param size
	 *            每页条数
	 * @param blobs
	 *            是否为blobs 查找
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private Page<Model> selectByExampleChooes(Example example, Integer page, Integer size, boolean blobs) {
		if (example == null) {
			try {
				example = (Example) exampleClass.newInstance();
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		if (example instanceof MbgExample) {
			MbgExample temp = (MbgExample) example;

			if ("Oracle".equals(temp.getDatabaseType())) {
				temp.setLimitStart((long) (page - 1) * size);
				temp.setLimitEnd((long) page * size);
			} else {
				temp.setLimitStart((long) (page - 1) * size);
				temp.setLimitEnd((long) size);
			}

			List<Model> list;
			if (blobs) {
				list = selectByExampleWithBLOBs(example);
			} else {
				list = selectByExample(example);
			}
			temp.setLimitStart(null);
			temp.setOrderByClause(null);
			long count = countByExample(example);
			return new Page<Model>(list, count, page, size);
		}
		return null;
	}

	public MbgMapper<Model, Example, PrimaryKey> getMapper() {
		return mapper;
	}

	public void setMapper(MbgMapper<Model, Example, PrimaryKey> mapper) {
		this.mapper = mapper;
	}

	public SqlSessionFactory getSqlSessionFactory() {
		return sqlSessionFactory;
	}

	public void setSqlSessionFactory(SqlSessionFactory sqlSessionFactory) {
		this.sqlSessionFactory = sqlSessionFactory;
	}

	public String getMapperName() {
		return mapperName;
	}

	public void setMapperName(String mapperName) {
		this.mapperName = mapperName;
	}

}
