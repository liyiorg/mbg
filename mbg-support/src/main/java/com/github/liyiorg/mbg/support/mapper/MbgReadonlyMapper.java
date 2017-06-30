package com.github.liyiorg.mbg.support.mapper;

import java.util.List;

/**
 * 
 * @author LiYi
 *
 * @param <Model> Model
 * @param <Example> Example
 * @param <PrimaryKey> PrimaryKey
 */
public interface MbgReadonlyMapper<Model, Example, PrimaryKey> extends MbgMapper<Model, Example, PrimaryKey> {

	long countByExample(Example example);

	List<Model> selectByExample(Example example);

	Model selectByPrimaryKey(PrimaryKey id);
}
