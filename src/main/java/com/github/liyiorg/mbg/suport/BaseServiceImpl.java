package com.github.liyiorg.mbg.suport;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import org.apache.commons.beanutils.MethodUtils;

public class BaseServiceImpl<Model, Example, PrimaryKey> implements BaseBLOBsService<Model, Example, PrimaryKey>{

	protected Object mapper;
	
	@SuppressWarnings("unchecked")
	private <T> T invokeExactMethod(String methodName,Object... args){
		try {
			return (T) MethodUtils.invokeExactMethod(mapper, methodName, args);
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@Override
	public int countByExample(Example example) {
		return invokeExactMethod("countByExample",example);
		
	}

	@Override
	public int deleteByExample(Example example) {
		return invokeExactMethod("deleteByExample",example);
	}

	@Override
	public int deleteByPrimaryKey(PrimaryKey id) {
		return invokeExactMethod("deleteByPrimaryKey",id);
	}

	@Override
	public int insert(Example record) {
		return invokeExactMethod("insert",record);
	}

	@Override
	public int insertSelective(Example record) {
		return invokeExactMethod("insertSelective",record);
	}

	@Override
	public List<Model> selectByExampleWithBLOBs(Example example) {
		return invokeExactMethod("selectByExampleWithBLOBs",example);
	}

	@Override
	public List<Model> selectByExample(Example example) {
		return invokeExactMethod("selectByExample",example);
	}

	@Override
	public Model selectByPrimaryKey(PrimaryKey id) {
		return invokeExactMethod("selectByPrimaryKey", id);
	}

	@Override
	public int updateByExampleSelective(Model record, Example example) {
		return invokeExactMethod("updateByExampleSelective", record,example);
	}

	@Override
	public int updateByExampleWithBLOBs(Model record, Example example) {
		return invokeExactMethod("updateByExampleWithBLOBs", record,example);
	}

	@Override
	public int updateByExample(Model record, Example example) {
		return invokeExactMethod("updateByExample", record,example);
	}

	@Override
	public int updateByPrimaryKeySelective(Model record) {
		return invokeExactMethod("updateByPrimaryKeySelective", record);
	}

	@Override
	public int updateByPrimaryKeyWithBLOBs(Model record) {
		return invokeExactMethod("updateByPrimaryKeyWithBLOBs", record);
	}

	@Override
	public int updateByPrimaryKey(Model record) {
		return invokeExactMethod("updateByPrimaryKey", record);
	}

	
}
