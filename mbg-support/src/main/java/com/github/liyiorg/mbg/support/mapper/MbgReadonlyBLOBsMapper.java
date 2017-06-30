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
public interface MbgReadonlyBLOBsMapper<Model, Example, PrimaryKey> extends MbgReadonlyMapper<Model, Example, PrimaryKey> {

	List<Model> selectByExampleWithBLOBs(Example example);
}
