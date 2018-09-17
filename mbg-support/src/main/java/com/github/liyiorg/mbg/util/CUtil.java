package com.github.liyiorg.mbg.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.github.liyiorg.mbg.support.example.CInterface;
import com.github.liyiorg.mbg.support.example.ColumnListAble;

public class CUtil {

	private static final Map<Class<? extends Enum<?>>, Item> C_DATA_MAP = new ConcurrentHashMap<Class<? extends Enum<?>>, Item>(1024);

	private static final Comparator<CInterface> C_COMPARATOR = new Comparator<CInterface>() {

		@Override
		public int compare(CInterface o1, CInterface o2) {
			return o1.name().compareTo(o2.name());
		}
	};

	/**
	 * 获取分组枚举项
	 * 
	 * @param clazz
	 *            C
	 * @param types
	 *            [1,2,3]
	 * @return Set<CInterface>
	 */
	public static <E extends Enum<E>> Set<CInterface> group(Class<E> clazz, int... types) {
		E[] es = clazz.getEnumConstants();
		Set<CInterface> set = null;
		for (E e : es) {
			if (e instanceof CInterface) {
				CInterface c = (CInterface) e;
				for (int type : types) {
					if (c.getType() == type) {
						if (set == null) {
							set = new LinkedHashSet<CInterface>();
						}
						set.add(c);
						break;
					}
				}
			}
		}
		return set;
	}

	/**
	 * 拼接columns
	 * 
	 * @param coll
	 *            Collection<C>
	 * @param alias
	 *            alias
	 * @param sort
	 *            sort
	 * @return String
	 */
	public static <C extends CInterface> String joinDelimitedNames(Collection<C> coll, boolean alias, boolean sort) {
		if (coll == null || coll.isEmpty()) {
			return "";
		}
		Iterator<C> it;
		// sort
		if (sort) {
			List<C> list = new ArrayList<C>(coll);
			Collections.sort(list, C_COMPARATOR);
			it = list.iterator();
		} else {
			it = coll.iterator();
		}
		// join delimited names
		StringBuilder sb = new StringBuilder();
		while (it.hasNext()) {
			if (alias) {
				sb.append(it.next().delimitedAliasName());
			} else {
				sb.append(it.next().delimitedName());
			}
			if (it.hasNext()) {
				sb.append(",");
			}
		}
		return sb.toString();
	}

	/**
	 * 包含的列
	 * 
	 * @param clazz
	 *            C
	 * @param columnListAble
	 *            columnListAble
	 * @param cs
	 *            cs
	 */
	public static <E extends Enum<E>, C extends CInterface> void includeColumns(Class<E> clazz,
			ColumnListAble columnListAble, C[] cs) {

		Set<C> baseSet = null;
		columnListAble.setBase_Column_List(null);
		Set<C> blobSet = null;
		columnListAble.setBlob_Column_List(null);
		if (cs != null && cs.length > 0) {
			for (C c : cs) {
				switch (c.getType()) {
				case 1:
				case 2:
					if (baseSet == null) {
						baseSet = new LinkedHashSet<C>();
					}
					baseSet.add(c);
					break;
				case 3:
					if (blobSet == null) {
						blobSet = new LinkedHashSet<C>();
					}
					blobSet.add(c);
					break;
				}
			}

			if (!C_DATA_MAP.containsKey(clazz)) {
				C_DATA_MAP.put(clazz, new Item(group(clazz, 1, 2), group(clazz, 3)));
			}
			Item item = C_DATA_MAP.get(clazz);

			if (item.default_base_columns != null) {
				columnListAble.setBase_Column_List(baseSet == null ? "1" : joinDelimitedNames(baseSet, true, true));
			}
			if (item.default_blob_columns != null) {
				columnListAble.setBlob_Column_List(blobSet == null ? "2" : joinDelimitedNames(blobSet, true, true));
			}
		}
	}

	/**
	 * 排除的列
	 * 
	 * @param clazz
	 *            C
	 * @param columnListAble
	 *            columnListAble
	 * @param cs
	 *            cs
	 */
	public static <E extends Enum<E>, C extends CInterface> void excludeColumns(Class<E> clazz,
			ColumnListAble columnListAble, C[] cs) {

		Set<CInterface> baseSet = null;
		columnListAble.setBase_Column_List(null);
		Set<CInterface> blobSet = null;
		columnListAble.setBlob_Column_List(null);
		if (cs != null && cs.length > 0) {
			if (!C_DATA_MAP.containsKey(clazz)) {
				C_DATA_MAP.put(clazz, new Item(group(clazz, 1, 2), group(clazz, 3)));
			}
			Item item = C_DATA_MAP.get(clazz);
			if (item.default_base_columns != null) {
				baseSet = new LinkedHashSet<CInterface>(item.default_base_columns);
			}
			if (item.default_blob_columns != null) {
				blobSet = new LinkedHashSet<CInterface>(item.default_blob_columns);
			}

			for (C c : cs) {
				switch (c.getType()) {
				case 1:
				case 2:
					baseSet.remove(c);
					break;
				case 3:
					blobSet.remove(c);
					break;
				}
			}

			if (item.default_base_columns != null) {
				columnListAble.setBase_Column_List(baseSet.isEmpty() ? "1" : joinDelimitedNames(baseSet, true, true));
			}
			if (item.default_blob_columns != null) {
				columnListAble.setBlob_Column_List(blobSet.isEmpty() ? "2" : joinDelimitedNames(blobSet, true, true));
			}
		}

	}

	private static class Item {

		Set<CInterface> default_base_columns;

		Set<CInterface> default_blob_columns;

		public Item(Set<CInterface> default_base_columns, Set<CInterface> default_blob_columns) {
			this.default_base_columns = default_base_columns;
			this.default_blob_columns = default_blob_columns;
		}
	}
}
