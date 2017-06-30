package com.github.liyiorg.mbg.support.service;

import java.util.List;

import com.github.liyiorg.mbg.bean.Page;

/**
 * 
 * @author LiYi
 *
 * @param <Model> Model
 * @param <Example> Example
 * @param <PrimaryKey> PrimaryKey
 */
public interface MbgReadonlyService<Model, Example, PrimaryKey> extends MbgService<Model, Example, PrimaryKey>{

	long countByExample(Example example);

	List<Model> selectByExample(Example example);

	Model selectByPrimaryKey(PrimaryKey id);

	Page<Model> selectByExample(Example example, Integer page, Integer size);
}
