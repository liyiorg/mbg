package com.github.liyiorg.mbg.suport;

import java.util.List;

import com.github.liyiorg.mbg.bean.Page;

public interface BaseBLOBsService<Model, Example, PrimaryKey> extends BaseService<Model, Example, PrimaryKey>{
	
	List<Model> selectByExampleWithBLOBs(Example example);

	int updateByExampleWithBLOBs(Model record, Example example);

	int updateByPrimaryKeyWithBLOBs(Model record);
	
	Page<Model> selectByExampleWithBLOBs(Example example,Integer page,Integer size);
}
