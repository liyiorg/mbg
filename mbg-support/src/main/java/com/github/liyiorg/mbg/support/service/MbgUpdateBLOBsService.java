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
public interface MbgUpdateBLOBsService<Model, Example, PrimaryKey>
		extends MbgUpdateService<Model, Example, PrimaryKey> {

	int updateByExampleWithBLOBs(Model record, Example example);

	int[] batchUpdateByExampleWithBLOBs(ModelExample<Model, Example>[] modelExample);

	int updateByPrimaryKeyWithBLOBs(Model record);

	int[] batchUpdateByPrimaryKeyWithBLOBs(Model[] record);

}
