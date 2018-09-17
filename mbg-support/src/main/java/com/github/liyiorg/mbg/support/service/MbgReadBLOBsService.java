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
public interface MbgReadBLOBsService<PrimaryKey, Model, ModelWithBLOBs, Example> extends MbgReadService<PrimaryKey, Model, ModelWithBLOBs, Example>{

	List<ModelWithBLOBs> selectByExampleWithBLOBs(Example example);
	
	Page<ModelWithBLOBs> selectPageByExampleWithBLOBs(Example example, Integer page, Integer size);
	
	ModelWithBLOBs selectByExampleWithBLOBsSingleResult(Example example);
}
