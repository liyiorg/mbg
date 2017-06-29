package com.github.liyiorg.mbg.support.service;

import java.util.List;

import com.github.liyiorg.mbg.bean.Page;

/**
 * 
 * @author LiYi
 *
 * @param <Model>
 * @param <Example>
 * @param <PrimaryKey>
 */
public interface MbgReadonlyBLOBsService<Model, Example, PrimaryKey> extends MbgReadonlyService<Model, Example, PrimaryKey>{

	List<Model> selectByExampleWithBLOBs(Example example);
	
	Page<Model> selectByExampleWithBLOBs(Example example, Integer page, Integer size);
}
