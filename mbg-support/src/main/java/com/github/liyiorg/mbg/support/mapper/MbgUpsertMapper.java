package com.github.liyiorg.mbg.support.mapper;

/**
 * @param <PrimaryKey>     PrimaryKey
 * @param <Model>          Model
 * @param <ModelWithBLOBs> ModelWithBLOBs
 * @param <Example>        Example
 * @author LiYi
 */
public interface MbgUpsertMapper<PrimaryKey, Model, ModelWithBLOBs, Example>
        extends MbgMapper<PrimaryKey, Model, ModelWithBLOBs, Example> {

    int upsert(ModelWithBLOBs record);

    int upsertSelective(ModelWithBLOBs record);

}
