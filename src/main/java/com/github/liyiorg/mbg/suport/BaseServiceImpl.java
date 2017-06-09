package com.github.liyiorg.mbg.suport;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import com.github.liyiorg.mbg.bean.Page;
import com.github.liyiorg.mbg.util.GenericsUtils;
import com.github.liyiorg.mbg.util.MethodUtils;

public abstract class BaseServiceImpl<Model, Example, PrimaryKey>
		implements BaseBLOBsService<Model, Example, PrimaryKey> {

	protected Object mapper;

	private Class<?> modelClass, exampleClass, primaryKeyClass;

	{
		modelClass = GenericsUtils.getSuperClassGenricType(this.getClass(), 0);
		exampleClass = GenericsUtils.getSuperClassGenricType(this.getClass(), 1);
		primaryKeyClass = GenericsUtils.getSuperClassGenricType(this.getClass(), 2);
	}

	@SuppressWarnings("unchecked")
	private <T> T invokeExactMethod(String methodName, Object[] args, Class<?>[] parameterTypes) {
		try {
			return (T) MethodUtils.invokeExactMethod(mapper, methodName, args, parameterTypes);
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		return null;
	}

	private <T> T invokeExactMethod(String methodName, Object arg, Class<?> parameterType) {
		return invokeExactMethod(methodName, new Object[] { arg }, new Class<?>[] { parameterType });
	}

	@Override
	public long countByExample(Example example) {
		return invokeExactMethod("countByExample", example, exampleClass);

	}

	@Override
	public int deleteByExample(Example example) {
		return invokeExactMethod("deleteByExample", example, exampleClass);
	}

	@Override
	public int deleteByPrimaryKey(PrimaryKey id) {
		return invokeExactMethod("deleteByPrimaryKey", id, primaryKeyClass);
	}

	@Override
	public int insert(Model record) {
		return invokeExactMethod("insert", record, modelClass);
	}

	@Override
	public int insertSelective(Model record) {
		return invokeExactMethod("insertSelective", record, modelClass);
	}

	@Override
	public List<Model> selectByExampleWithBLOBs(Example example) {
		return invokeExactMethod("selectByExampleWithBLOBs", example, exampleClass);
	}

	@Override
	public List<Model> selectByExample(Example example) {
		return invokeExactMethod("selectByExample", example, exampleClass);
	}

	@Override
	public Model selectByPrimaryKey(PrimaryKey id) {
		return invokeExactMethod("selectByPrimaryKey", id, primaryKeyClass);
	}

	@Override
	public int updateByExampleSelective(Model record, Example example) {
		return invokeExactMethod("updateByExampleSelective", new Object[] { record, example },
				new Class<?>[] { modelClass, exampleClass });
	}

	@Override
	public int updateByExampleWithBLOBs(Model record, Example example) {
		return invokeExactMethod("updateByExampleWithBLOBs", new Object[] { record, example },
				new Class<?>[] { modelClass, exampleClass });
	}

	@Override
	public int updateByExample(Model record, Example example) {
		return invokeExactMethod("updateByExample", new Object[] { record, example },
				new Class<?>[] { modelClass, exampleClass });
	}

	@Override
	public int updateByPrimaryKeySelective(Model record) {
		return invokeExactMethod("updateByPrimaryKeySelective", record, modelClass);
	}

	@Override
	public int updateByPrimaryKeyWithBLOBs(Model record) {
		return invokeExactMethod("updateByPrimaryKeyWithBLOBs", record, modelClass);
	}

	@Override
	public int updateByPrimaryKey(Model record) {
		return invokeExactMethod("updateByPrimaryKey", record, modelClass);
	}
	
	@Override
	public Page<Model> selectByExample(Example example, Integer page, Integer size) {
		return selectByExampleChooes(example, page, size, false);
	}

	@Override
	public Page<Model> selectByExampleWithBLOBs(Example example, Integer page, Integer size) {
		return selectByExampleChooes(example, page, size, true);
	}
	
	/**
	 * 分页查询
	 * @param example	example可以为空
	 * @param page	页码
	 * @param size	每页条数
	 * @param blobs	是否为blobs 查找
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private Page<Model> selectByExampleChooes(Example example, Integer page, Integer size,boolean blobs) {
		if(example == null){
			try {
				example = (Example)exampleClass.newInstance();
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		if (example instanceof LimitInterface) {
			LimitInterface temp = (LimitInterface) example;
			temp.setLimitStart((long) (page - 1) * size);
			temp.setLimitEnd((long) page * size);
			List<Model> list;
			if(blobs){
				list = selectByExampleWithBLOBs(example);
			}else{
				list = selectByExample(example);
			}
			temp.setLimitStart(null);
			temp.setOrderByClause(null);
			long count = countByExample(example);
			return new Page<Model>(list, count, page, size);
		}
		return null;
	}

}
