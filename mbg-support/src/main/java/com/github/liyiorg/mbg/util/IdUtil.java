package com.github.liyiorg.mbg.util;

import org.apache.commons.codec.digest.DigestUtils;

public abstract class IdUtil {

	/**
	 * 根据参数 , 号拼接生成 key
	 * @param keys 多个key
	 * @return id
	 */
	public static String sha1Id(String... keys){
		StringBuilder stringBuilder = new StringBuilder();
		for(int i = 0; i < keys.length; i++){
			stringBuilder.append(keys[i]);
			if(i < keys.length - 1){
				stringBuilder.append(",");
			}
		}
		return DigestUtils.shaHex(stringBuilder.toString());
	}
}
