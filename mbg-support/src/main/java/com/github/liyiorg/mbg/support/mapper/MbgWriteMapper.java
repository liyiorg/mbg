package com.github.liyiorg.mbg.support.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

/**
 * 
 * @author LiYi
 *
 * @param <PrimaryKey> PrimaryKey
 * @param <Model> Model
 * @param <ModelWithBLOBs> ModelWithBLOBs
 * @param <Example> Example
 */
public interface MbgWriteMapper<PrimaryKey, Model, ModelWithBLOBs, Example> extends MbgMapper<PrimaryKey, Model, ModelWithBLOBs, Example> {

	int deleteByExample(Example example);

	int deleteByPrimaryKey(PrimaryKey id);

	int insert(ModelWithBLOBs record);

	int insertSelective(ModelWithBLOBs record);

	int updateByExampleSelective(@Param("record") Model record, @Param("example") Example example);

	int updateByExample(@Param("record") Model record, @Param("example") Example example);

	int updateByPrimaryKeySelective(Model record);

	int updateByPrimaryKey(Model record);
	
	int batchInsert(List<ModelWithBLOBs> record);
	
	int batchInsertSelective(List<ModelWithBLOBs> record);

	int updateByPrimaryKeySelectiveWithOptimisticLock(ModelWithBLOBs record);
}
