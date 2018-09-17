package com.github.liyiorg.mbg.support.service;

/**
 * @param <PrimaryKey>     PrimaryKey
 * @param <Model>          Model
 * @param <ModelWithBLOBs> ModelWithBLOBs
 * @param <Example>        Example
 * @author LiYi
 */
public interface MbgUpsertService<PrimaryKey, Model, ModelWithBLOBs, Example>
        extends MbgService<PrimaryKey, Model, ModelWithBLOBs, Example> {

	int upsert(ModelWithBLOBs record);

    int upsertSelective(ModelWithBLOBs record);
}
