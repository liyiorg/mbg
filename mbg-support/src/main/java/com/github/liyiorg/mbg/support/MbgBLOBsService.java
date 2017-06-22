package com.github.liyiorg.mbg.support;

import java.util.List;

import com.github.liyiorg.mbg.bean.Page;

public interface MbgBLOBsService<Model, Example, PrimaryKey> extends MbgService<Model, Example, PrimaryKey> {

	List<Model> selectByExampleWithBLOBs(Example example);

	int updateByExampleWithBLOBs(Model record, Example example);
	
	int[] batchUpdateByExampleWithBLOBs(ModelExample<Model,Example>[] modelExample);

	int updateByPrimaryKeyWithBLOBs(Model record);
	
	int[] batchUpdateByPrimaryKeyWithBLOBs(Model[] record);

	Page<Model> selectByExampleWithBLOBs(Example example, Integer page, Integer size);
}
