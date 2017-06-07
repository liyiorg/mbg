package com.github.liyiorg.mbg.suport;

import java.util.List;

import com.github.liyiorg.mbg.bean.Page;

public interface BaseService<Model, Example, PrimaryKey> {

	long countByExample(Example example);

	int deleteByExample(Example example);

	int deleteByPrimaryKey(PrimaryKey id);

	int insert(Model record);

	int insertSelective(Model record);

	List<Model> selectByExample(Example example);

	Model selectByPrimaryKey(PrimaryKey id);

	int updateByExampleSelective(Model record, Example example);

	int updateByExample(Model record, Example example);

	int updateByPrimaryKeySelective(Model record);

	int updateByPrimaryKey(Model record);
	
	Page<Model> selectByExample(Example example,Integer page,Integer size);
}
