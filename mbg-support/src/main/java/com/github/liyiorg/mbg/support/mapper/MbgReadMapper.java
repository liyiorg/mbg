package com.github.liyiorg.mbg.support.mapper;

import java.util.ArrayList;
import java.util.List;

import com.github.liyiorg.mbg.bean.Page;
import com.github.liyiorg.mbg.exceptions.MbgExampleException;
import com.github.liyiorg.mbg.exceptions.MbgMapperException;
import com.github.liyiorg.mbg.support.example.PaginationAble;

/**
 * 
 * @author LiYi
 *
 * @param <PrimaryKey> PrimaryKey
 * @param <Model> Model
 * @param <ModelWithBLOBs> ModelWithBLOBs
 * @param <Example> Example
 */
public interface MbgReadMapper<PrimaryKey, Model, ModelWithBLOBs, Example> extends MbgMapper<PrimaryKey, Model, ModelWithBLOBs, Example> {

	long countByExample(Example example);

	List<Model> selectByExample(Example example);

	ModelWithBLOBs selectByPrimaryKey(PrimaryKey id);
	
	default Page<Model> selectPageByExample(Example example, Integer page, Integer size) {
		if (example instanceof PaginationAble) {
			long count = countByExample(example);
			if (count > 0) {
				PaginationAble temp = (PaginationAble) example;
				if ("Oracle".equalsIgnoreCase(temp.getDatabaseType())) {
					temp.setLimitStart((long) (page - 1) * size);
					temp.setLimitEnd((long) page * size);
				} else {
					temp.setLimitStart((long) (page - 1) * size);
					temp.setLimitEnd((long) size);
				}

				List<Model> list = selectByExample(example);
				temp.setLimitStart(null);
				temp.setOrderByClause(null);
				return new Page<Model>(list, count, page, size);
			}
			return new Page<Model>(new ArrayList<Model>(), 0, page, size);
		} else {
			throw new MbgExampleException("Example must PaginationAble");
		}
	}

	default Model selectByExampleSingleResult(Example example) {
		List<Model> results = selectByExample(example);
		int size = (results != null ? results.size() : 0);
		if (size == 0) {
			return null;
		}
		if (results.size() > 1) {
			throw new MbgMapperException("data results must single result");
		}
		return results.iterator().next();
	}
}
