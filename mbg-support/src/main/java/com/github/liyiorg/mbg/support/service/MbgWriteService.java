package com.github.liyiorg.mbg.support.service;

import java.util.List;

/**
 * 
 * @author LiYi
 *
 * @param <PrimaryKey>
 *            PrimaryKey
 * @param <Model>
 *            Model
 * @param <ModelWithBLOBs>
 *            ModelWithBLOBs
 * @param <Example>
 *            Example
 */
public interface MbgWriteService<PrimaryKey, Model, ModelWithBLOBs, Example>
		extends MbgService<PrimaryKey, Model, ModelWithBLOBs, Example> {

	int deleteByExample(Example example);

	int deleteByPrimaryKey(PrimaryKey id);

	int insert(ModelWithBLOBs record);

	int insertSelective(ModelWithBLOBs record);

	int updateByExampleSelective(Model record, Example example);

	int updateByExample(Model record, Example example);

	int updateByPrimaryKeySelective(Model record);

	int updateByPrimaryKey(Model record);

	int batchInsert(List<ModelWithBLOBs> record);
	
	int batchInsertSelective(List<ModelWithBLOBs> record);
	
	int updateByPrimaryKeySelectiveWithOptimisticLock(ModelWithBLOBs record);
}
