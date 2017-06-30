package com.github.liyiorg.mbg.support.service;

import com.github.liyiorg.mbg.bean.ModelExample;

/**
 * 
 * @author LiYi
 *
 * @param <Model> Model
 * @param <Example> Example
 * @param <PrimaryKey> PrimaryKey
 */
public interface MbgUpdateService<Model, Example, PrimaryKey> extends MbgService<Model, Example, PrimaryKey> {

	int deleteByExample(Example example);

	int[] batchDeleteByExample(Example[] example);

	int deleteByPrimaryKey(PrimaryKey id);

	int[] batchDeleteByPrimaryKey(PrimaryKey[] id);

	int insert(Model record);

	int[] batchInsert(Model[] record);

	int insertSelective(Model record);

	int[] batchInsertSelective(Model[] record);

	int updateByExampleSelective(Model record, Example example);

	int[] batchUpdateByExampleSelective(ModelExample<Model, Example>[] modelExample);

	int updateByExample(Model record, Example example);

	int[] batchUpdateByExample(ModelExample<Model, Example>[] modelExample);

	int updateByPrimaryKeySelective(Model record);

	int[] batchUpdateByPrimaryKeySelective(Model[] record);

	int updateByPrimaryKey(Model record);

	int[] batchUpdateByPrimaryKey(Model[] record);

}
