package com.github.liyiorg.mbg.support.service;

import java.util.List;

import com.github.liyiorg.mbg.bean.Page;
import com.github.liyiorg.mbg.support.mapper.MbgMapper;
import com.github.liyiorg.mbg.support.mapper.MbgReadBLOBsMapper;
import com.github.liyiorg.mbg.support.mapper.MbgReadMapper;
import com.github.liyiorg.mbg.support.mapper.MbgUpsertMapper;
import com.github.liyiorg.mbg.support.mapper.MbgWriteBLOBsMapper;
import com.github.liyiorg.mbg.support.mapper.MbgWriteMapper;

/**
 * 
 * @author LiYi
 *
 * @param <PrimaryKey>
 *            PrimaryKey
 * @param <Model>
 *            Model
 * @param <ModelWithBLOBs>
 *            ModelWithBLOBs
 * @param <Example>
 *            Example
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public abstract class MbgServiceSupport<Mapper extends MbgMapper<PrimaryKey, Model, ModelWithBLOBs, Example>, PrimaryKey, Model, ModelWithBLOBs, Example>
		implements MbgReadBLOBsService<PrimaryKey, Model, ModelWithBLOBs, Example>,
		MbgWriteBLOBsService<PrimaryKey, Model, ModelWithBLOBs, Example>,
		MbgUpsertService<PrimaryKey, Model, ModelWithBLOBs, Example>{

	protected MbgMapper mapper;

	@Override
	public long countByExample(Example example) {
		return ((MbgReadMapper<PrimaryKey, Model, ModelWithBLOBs, Example>) mapper).countByExample(example);
	}

	@Override
	public int deleteByExample(Example example) {
		return ((MbgWriteMapper<PrimaryKey, Model, ModelWithBLOBs, Example>) mapper).deleteByExample(example);
	}

	@Override
	public int deleteByPrimaryKey(PrimaryKey id) {
		return ((MbgWriteMapper<PrimaryKey, Model, ModelWithBLOBs, Example>) mapper).deleteByPrimaryKey(id);
	}

	@Override
	public int insert(ModelWithBLOBs record) {
		return ((MbgWriteMapper<PrimaryKey, Model, ModelWithBLOBs, Example>) mapper).insert(record);
	}

	@Override
	public int insertSelective(ModelWithBLOBs record) {
		return ((MbgWriteMapper<PrimaryKey, Model, ModelWithBLOBs, Example>) mapper).insertSelective(record);
	}

	@Override
	public List<ModelWithBLOBs> selectByExampleWithBLOBs(Example example) {
		return ((MbgReadBLOBsMapper<PrimaryKey, Model, ModelWithBLOBs, Example>) mapper)
				.selectByExampleWithBLOBs(example);
	}

	@Override
	public List<Model> selectByExample(Example example) {
		return ((MbgReadMapper<PrimaryKey, Model, ModelWithBLOBs, Example>) mapper).selectByExample(example);
	}

	@Override
	public ModelWithBLOBs selectByPrimaryKey(PrimaryKey id) {
		return ((MbgReadMapper<PrimaryKey, Model, ModelWithBLOBs, Example>) mapper).selectByPrimaryKey(id);
	}

	@Override
	public int updateByExampleSelective(Model record, Example example) {
		return ((MbgWriteMapper<PrimaryKey, Model, ModelWithBLOBs, Example>) mapper).updateByExampleSelective(record,
				example);
	}

	@Override
	public int updateByExampleWithBLOBs(ModelWithBLOBs record, Example example) {
		return ((MbgWriteBLOBsMapper<PrimaryKey, Model, ModelWithBLOBs, Example>) mapper)
				.updateByExampleWithBLOBs(record, example);
	}

	@Override
	public int updateByExample(Model record, Example example) {
		return ((MbgWriteMapper<PrimaryKey, Model, ModelWithBLOBs, Example>) mapper).updateByExample(record, example);
	}

	@Override
	public int updateByPrimaryKeySelective(Model record) {
		return ((MbgWriteMapper<PrimaryKey, Model, ModelWithBLOBs, Example>) mapper)
				.updateByPrimaryKeySelective(record);
	}

	@Override
	public int updateByPrimaryKeyWithBLOBs(ModelWithBLOBs record) {
		return ((MbgWriteBLOBsMapper<PrimaryKey, Model, ModelWithBLOBs, Example>) mapper)
				.updateByPrimaryKeyWithBLOBs(record);
	}

	@Override
	public int updateByPrimaryKey(Model record) {
		return ((MbgWriteMapper<PrimaryKey, Model, ModelWithBLOBs, Example>) mapper).updateByPrimaryKey(record);
	}

	@Override
	public int batchInsert(List<ModelWithBLOBs> record) {
		return ((MbgWriteMapper<PrimaryKey, Model, ModelWithBLOBs, Example>) mapper).batchInsert(record);
	}

	@Override
	public int batchInsertSelective(List<ModelWithBLOBs> record) {
		return ((MbgWriteMapper<PrimaryKey, Model, ModelWithBLOBs, Example>) mapper).batchInsertSelective(record);
	}

	@Override
	public int updateByPrimaryKeySelectiveWithOptimisticLock(ModelWithBLOBs record) {
		return ((MbgWriteMapper<PrimaryKey, Model, ModelWithBLOBs, Example>) mapper).updateByPrimaryKeySelectiveWithOptimisticLock(record);
	}

	@Override
	public Page<Model> selectPageByExample(Example example, Integer page, Integer size) {
		return ((MbgReadMapper<PrimaryKey, Model, ModelWithBLOBs, Example>) mapper)
				.selectPageByExample(example, page, size);
	}

	@Override
	public Page<ModelWithBLOBs> selectPageByExampleWithBLOBs(Example example, Integer page, Integer size) {
		return ((MbgReadBLOBsMapper<PrimaryKey, Model, ModelWithBLOBs, Example>) mapper)
				.selectPageByExampleWithBLOBs(example, page, size);
	}

	@Override
	public Model selectByExampleSingleResult(Example example) {
		return ((MbgReadMapper<PrimaryKey, Model, ModelWithBLOBs, Example>) mapper)
				.selectByExampleSingleResult(example);
	}

	@Override
	public ModelWithBLOBs selectByExampleWithBLOBsSingleResult(Example example) {
		return ((MbgReadBLOBsMapper<PrimaryKey, Model, ModelWithBLOBs, Example>) mapper)
				.selectByExampleWithBLOBsSingleResult(example);
	}
	
	@Override
	public int upsert(ModelWithBLOBs record) {
		return ((MbgUpsertMapper<PrimaryKey, Model, ModelWithBLOBs, Example>) mapper).
				upsert(record);
				
    }

	@Override
	public int upsertSelective(ModelWithBLOBs record) {
    	return ((MbgUpsertMapper<PrimaryKey, Model, ModelWithBLOBs, Example>) mapper).
    			upsertSelective(record);
    }

}
