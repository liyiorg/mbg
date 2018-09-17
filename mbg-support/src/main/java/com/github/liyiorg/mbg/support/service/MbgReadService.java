package com.github.liyiorg.mbg.support.service;

import java.util.List;

import com.github.liyiorg.mbg.bean.Page;

/**
 * 
 * @author LiYi
 *
 * @param <PrimaryKey> PrimaryKey
 * @param <Model> Model
 * @param <ModelWithBLOBs> ModelWithBLOBs
 * @param <Example> Example
 */
public interface MbgReadService<PrimaryKey, Model, ModelWithBLOBs, Example> extends MbgService<PrimaryKey, Model, ModelWithBLOBs, Example>{

	long countByExample(Example example);

	List<Model> selectByExample(Example example);

	ModelWithBLOBs selectByPrimaryKey(PrimaryKey id);

	Page<Model> selectPageByExample(Example example, Integer page, Integer size);
	
	Model selectByExampleSingleResult(Example example);
}
