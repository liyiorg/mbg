package com.github.liyiorg.mbg.support;

import com.github.liyiorg.mbg.bean.Page;

public interface MbgService<Model, Example, PrimaryKey> extends MbgMapper<Model, Example, PrimaryKey>{

	Page<Model> selectByExample(Example example,Integer page,Integer size);
}
