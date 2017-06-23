package com.github.liyiorg.mbg.util;

import java.lang.reflect.Field;
import java.util.List;

/**
 * Model Util
 * @author LiYi
 *
 */
public class ModelUtil {

	/**
	 * 忽略字段返回值
	 * @param model
	 * @param fieldNames
	 */
	public static void excludes(Object model, String... fieldNames) {
		for (String fieldName : fieldNames) {
			try {
				Field field = model.getClass().getDeclaredField(fieldName);
				if (!field.isAccessible()) {
					field.setAccessible(true);
				}
				field.set(model, null);
			} catch (NoSuchFieldException e) {
				e.printStackTrace();
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 忽略字段返回值
	 * @param models
	 * @param fieldNames
	 */
	public static void excludes(List<Object> models, String... fieldNames) {
		for (Object model : models) {
			excludes(model, fieldNames);
		}
	}

	/**
	 * 包含字段返回值
	 * @param model
	 * @param fieldNames
	 */
	public static void includes(Object model, String... fieldNames) {
		for (String fieldName : fieldNames) {
			try {
				model.getClass().getDeclaredField(fieldName);
			} catch (NoSuchFieldException e) {
				e.printStackTrace();
			} catch (SecurityException e) {
				e.printStackTrace();
			}
		}
		for (Field field : model.getClass().getDeclaredFields()) {
			if("serialVersionUID".equals(field.getName())){
				continue;
			}
			boolean hasField = false;
			for (String fieldName : fieldNames) {
				if (fieldName.equals(field.getName())) {
					hasField = true;
					break;
				}
			}
			if (!hasField) {
				try {
					if (!field.isAccessible()) {
						field.setAccessible(true);
					}
					field.set(model, null);
				} catch (SecurityException e) {
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 包含字段返回值
	 * @param models
	 * @param fieldNames
	 */
	public static void includes(List<Object> models, String... fieldNames) {
		for (Object model : models) {
			includes(model, fieldNames);
		}
	}
}
