package com.github.liyiorg.mbg.support.service;

/**
 * 
 * @author LiYi
 *
 * @param <PrimaryKey> PrimaryKey
 * @param <Model> Model
 * @param <ModelWithBLOBs> ModelWithBLOBs
 * @param <Example> Example
 */
public interface MbgWriteBLOBsService<PrimaryKey, Model, ModelWithBLOBs, Example>
		extends MbgWriteService<PrimaryKey, Model, ModelWithBLOBs, Example> {

	int updateByExampleWithBLOBs(ModelWithBLOBs record, Example example);

	int updateByPrimaryKeyWithBLOBs(ModelWithBLOBs record);

}
