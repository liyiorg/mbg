package com.github.liyiorg.mbg.util;

import java.lang.reflect.Field;

/**
 * 
 * @author LiYi
 *
 */
public class ModelUtils {

	/**
	 * 数据模型 空白字符串Field 设置为NULL
	 * 
	 * @param model
	 *            model
	 */
	public static void stringFieldEmptyValueToNullValue(Object model) {
		if (model != null) {
			Class<?> clazz = model.getClass();
			for (Field field : clazz.getDeclaredFields()) {
				field.setAccessible(true);
				if (field.getType() == String.class) {
					try {
						Object object = field.get(model);
						if (object != null) {
							if("".equals(object.toString()) || object.toString().matches("\\s+")){
								field.set(model, null);
							}
						}
					} catch (IllegalArgumentException e) {
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

	/**
	 * 主键key 都不为空
	 * 
	 * @param primaryKey
	 * @return
	 */
	public static boolean primaryKeyFulledValues(Object primaryKey) {
		if (primaryKey != null) {
			try {
				Class<?> clazz = primaryKey.getClass().getSuperclass();
				if (clazz.getName().startsWith("java.lang.")) {
					return true;
				}
				for (Field field : clazz.getDeclaredFields()) {
					field.setAccessible(true);
					if ("serialVersionUID".equals(field.getName())) {
						continue;
					}
					if (field.get(primaryKey) == null) {
						return false;
					}
				}
				return true;
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		return false;
	}

}
