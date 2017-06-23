package com.github.liyiorg.mbg.support.service;

import java.util.List;

import com.github.liyiorg.mbg.bean.ModelExample;
import com.github.liyiorg.mbg.bean.Page;

public interface MbgService<Model, Example, PrimaryKey> {

	long countByExample(Example example);

	int deleteByExample(Example example);
	
	int[] batchDeleteByExample(Example[] example);

	int deleteByPrimaryKey(PrimaryKey id);
	
	int[] batchDeleteByPrimaryKey(PrimaryKey[] id);

	int insert(Model record);
	
	int[] batchInsert(Model[] record);

	int insertSelective(Model record);
	
	int[] batchInsertSelective(Model[] record);

	List<Model> selectByExample(Example example);

	Model selectByPrimaryKey(PrimaryKey id);

	int updateByExampleSelective(Model record,Example example);
	
	int[] batchUpdateByExampleSelective(ModelExample<Model,Example>[] modelExample);

	int updateByExample(Model record,Example example);
	
	int[] batchUpdateByExample(ModelExample<Model,Example>[] modelExample);

	int updateByPrimaryKeySelective(Model record);
	
	int[] batchUpdateByPrimaryKeySelective(Model[] record);

	int updateByPrimaryKey(Model record);
	
	int[] batchUpdateByPrimaryKey(Model[] record);
	
	Page<Model> selectByExample(Example example,Integer page,Integer size);
}
