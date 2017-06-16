package com.github.liyiorg.mbg.support;

import java.util.List;

import com.github.liyiorg.mbg.bean.Page;
import com.github.liyiorg.mbg.util.GenericsUtils;

public abstract class MbgServiceSupport<Model, Example, PrimaryKey>
		implements MbgService<Model, Example, PrimaryKey>,MbgBLOBsService<Model, Example, PrimaryKey> {

	protected MbgMapper<Model, Example, PrimaryKey> mapper;

	private Class<?> exampleClass;

	{
		exampleClass = GenericsUtils.getSuperClassGenricType(this.getClass(), 1);
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
		if(mapper instanceof MbgBLOBsMapper){
			MbgBLOBsMapper<Model, Example, PrimaryKey> blobsMapper = (MbgBLOBsMapper<Model, Example, PrimaryKey>) mapper;
			return blobsMapper.selectByExampleWithBLOBs(example);
		}
		return null;
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
		if(mapper instanceof MbgBLOBsMapper){
			MbgBLOBsMapper<Model, Example, PrimaryKey> blobsMapper = (MbgBLOBsMapper<Model, Example, PrimaryKey>) mapper;
			return blobsMapper.updateByExampleWithBLOBs(record,example);
		}
		return 0;
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
		if(mapper instanceof MbgBLOBsMapper){
			MbgBLOBsMapper<Model, Example, PrimaryKey> blobsMapper = (MbgBLOBsMapper<Model, Example, PrimaryKey>) mapper;
			return blobsMapper.updateByPrimaryKeyWithBLOBs(record);
		}
		return 0;
	}

	@Override
	public int updateByPrimaryKey(Model record) {
		return mapper.updateByPrimaryKey(record);
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
	 * @param example	example可以为空
	 * @param page	页码
	 * @param size	每页条数
	 * @param blobs	是否为blobs 查找
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private Page<Model> selectByExampleChooes(Example example, Integer page, Integer size,boolean blobs) {
		if(example == null){
			try {
				example = (Example)exampleClass.newInstance();
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		if (example instanceof MbgLimit) {
			MbgLimit temp = (MbgLimit) example;
			
			if("Oracle".equals(temp.getDatabaseType())){
				temp.setLimitStart((long) (page - 1) * size);
				temp.setLimitEnd((long) page * size);
			}else{
				temp.setLimitStart((long) (page - 1) * size);
				temp.setLimitEnd((long) size);
			}
			
			List<Model> list;
			if(blobs){
				list = selectByExampleWithBLOBs(example);
			}else{
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
