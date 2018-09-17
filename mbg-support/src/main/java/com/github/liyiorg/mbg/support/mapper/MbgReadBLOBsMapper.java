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
public interface MbgReadBLOBsMapper<PrimaryKey, Model, ModelWithBLOBs, Example> extends MbgReadMapper<PrimaryKey, Model, ModelWithBLOBs, Example> {

	List<ModelWithBLOBs> selectByExampleWithBLOBs(Example example);
	
	default Page<ModelWithBLOBs> selectPageByExampleWithBLOBs(Example example, Integer page, Integer size) {
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

				List<ModelWithBLOBs> list = selectByExampleWithBLOBs(example);
				temp.setLimitStart(null);
				temp.setOrderByClause(null);
				return new Page<ModelWithBLOBs>(list, count, page, size);
			}
			return new Page<ModelWithBLOBs>(new ArrayList<ModelWithBLOBs>(), 0, page, size);
		} else {
			throw new MbgExampleException("Example must PaginationAble");
		}
	}

	default ModelWithBLOBs selectByExampleWithBLOBsSingleResult(Example example) {
		List<ModelWithBLOBs> results = selectByExampleWithBLOBs(example);
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
