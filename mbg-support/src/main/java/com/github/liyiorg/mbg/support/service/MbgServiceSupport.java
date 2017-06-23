package com.github.liyiorg.mbg.support.service;

import java.util.List;

import org.apache.ibatis.executor.BatchResult;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import com.github.liyiorg.mbg.bean.ModelExample;
import com.github.liyiorg.mbg.bean.Page;
import com.github.liyiorg.mbg.support.example.MbgExample;
import com.github.liyiorg.mbg.support.mapper.MbgBLOBsMapper;
import com.github.liyiorg.mbg.support.mapper.MbgMapper;
import com.github.liyiorg.mbg.util.GenericsUtils;

public abstract class MbgServiceSupport<Model, Example, PrimaryKey>
		implements MbgBLOBsService<Model, Example, PrimaryKey> {

	protected MbgMapper<Model, Example, PrimaryKey> mapper;

	private MbgBLOBsMapper<Model, Example, PrimaryKey> blobsMapper;

	protected SqlSessionFactory sqlSessionFactory;

	private Class<?> exampleClass;

	protected String mapperName;

	{
		exampleClass = GenericsUtils.getSuperClassGenricType(this.getClass(), 1);
	}

	private MbgBLOBsMapper<Model, Example, PrimaryKey> blobsMapper() {
		if (blobsMapper == null) {
			if (mapper instanceof MbgBLOBsMapper) {
				blobsMapper = (MbgBLOBsMapper<Model, Example, PrimaryKey>) mapper;
			}
		}
		return blobsMapper;
	}

	@Override
	public long countByExample(Example example) {
		return mapper.countByExample(example);
	}

	@Override
	public int deleteByExample(Example example) {
		return mapper.deleteByExample(example);
	}

	@Override
	public int deleteByPrimaryKey(PrimaryKey id) {
		return mapper.deleteByPrimaryKey(id);
	}

	@Override
	public int insert(Model record) {
		return mapper.insert(record);
	}

	@Override
	public int insertSelective(Model record) {
		return mapper.insertSelective(record);
	}

	@Override
	public List<Model> selectByExampleWithBLOBs(Example example) {
		if (blobsMapper() != null) {
			return blobsMapper.selectByExampleWithBLOBs(example);
		}
		return selectByExample(example);
	}

	@Override
	public List<Model> selectByExample(Example example) {
		return mapper.selectByExample(example);
	}

	@Override
	public Model selectByPrimaryKey(PrimaryKey id) {
		return mapper.selectByPrimaryKey(id);
	}

	@Override
	public int updateByExampleSelective(Model record, Example example) {
		return mapper.updateByExampleSelective(record, example);
	}

	@Override
	public int updateByExampleWithBLOBs(Model record, Example example) {
		if (blobsMapper() != null) {
			return blobsMapper.updateByExampleWithBLOBs(record, example);
		}
		return updateByExample(record, example);
	}

	@Override
	public int updateByExample(Model record, Example example) {
		return mapper.updateByExample(record, example);
	}

	@Override
	public int updateByPrimaryKeySelective(Model record) {
		return mapper.updateByPrimaryKeySelective(record);
	}

	@Override
	public int updateByPrimaryKeyWithBLOBs(Model record) {
		if (blobsMapper() != null) {
			return blobsMapper.updateByPrimaryKeyWithBLOBs(record);
		}
		return updateByPrimaryKey(record);
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
					mapperStatement = mapperName + "." + mapperStatement;
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
	public int updateByPrimaryKey(Model record) {
		return mapper.updateByPrimaryKey(record);
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
		if (blobsMapper() != null) {
			return batchExec("updateByExampleWithBLOBs", modelExample);
		}
		return batchUpdateByExample(modelExample);
	}

	@Override
	public int[] batchUpdateByPrimaryKeyWithBLOBs(Model[] record) {
		if (blobsMapper() != null) {
			return batchExec("updateByPrimaryKeyWithBLOBs", record);
		}
		return batchUpdateByPrimaryKey(record);
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

}
