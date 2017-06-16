package com.github.liyiorg.mbg.support;

import java.util.List;

import org.apache.ibatis.annotations.Param;

public interface MbgBLOBsMapper<Model, Example, PrimaryKey> extends MbgMapper<Model, Example, PrimaryKey>{
	
	List<Model> selectByExampleWithBLOBs(Example example);

	int updateByExampleWithBLOBs(@Param("record") Model record,@Param("example") Example example);

	int updateByPrimaryKeyWithBLOBs(Model record);
	
}
