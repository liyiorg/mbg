package com.github.liyiorg.mbg.support;

import com.github.liyiorg.mbg.bean.Page;

public interface MbgBLOBsService<Model, Example, PrimaryKey> extends MbgBLOBsMapper<Model, Example, PrimaryKey>{
	
	Page<Model> selectByExample(Example example,Integer page,Integer size);
	
	Page<Model> selectByExampleWithBLOBs(Example example,Integer page,Integer size);
}
