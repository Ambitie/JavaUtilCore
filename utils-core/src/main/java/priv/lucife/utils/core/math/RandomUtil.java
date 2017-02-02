/**
 * Copyright (C) 2017 Lucifer Wong
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package priv.lucife.utils.core.math;

import java.util.Random;

import priv.lucife.utils.core.annotation.UBTCompatible;

/**
 * 随机数工具类
 * 
 * @author Lucifer Wong
 */
@UBTCompatible
public abstract class RandomUtil {
	private static final String ALL_CHAR = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
	private static final String LETTER_CHAR = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
	private static final String NUMBER_CHAR = "0123456789";

	/**
	 * 获取定长的随机数，只包含小写字母
	 * 
	 * @author Lucifer Wong
	 * @param length
	 *            随机数长度
	 * @return 获取定长的随机数，只包含小写字母
	 */
	public static String generateLowerString(int length) {
		return generateMixString(length).toLowerCase();
	}

	/**
	 * 获取定长的随机数，包含大小写字母
	 * 
	 * @author Lucifer Wong
	 * @param length
	 *            随机数长度
	 * @return 获取定长的随机数，包含大小写字母
	 */
	public static String generateMixString(int length) {
		StringBuffer sb = new StringBuffer();
		Random random = new Random();
		for (int i = 0; i < length; i++) {
			sb.append(LETTER_CHAR.charAt(random.nextInt(LETTER_CHAR.length())));
		}
		return sb.toString();
	}

	/**
	 * 获取定长的随机数，只包含数字
	 * 
	 * @author Lucifer Wong
	 * @param length
	 *            随机数长度
	 * @return 获取定长的随机数，只包含数字
	 */
	public static String generateNumberString(int length) {
		StringBuffer sb = new StringBuffer();
		Random random = new Random();
		for (int i = 0; i < length; i++) {
			sb.append(NUMBER_CHAR.charAt(random.nextInt(NUMBER_CHAR.length())));
		}
		return sb.toString();
	}

	/**
	 * 获取定长的随机数，包含大小写、数字
	 * 
	 * @author Lucifer Wong
	 * @param length
	 *            随机数长度
	 * @return 获取定长的随机数，包含大小写、数字
	 */
	public static String generateString(int length) {
		StringBuffer sb = new StringBuffer();
		Random random = new Random();
		for (int i = 0; i < length; i++) {
			sb.append(ALL_CHAR.charAt(random.nextInt(ALL_CHAR.length())));
		}
		return sb.toString();
	}

	/**
	 * 获取定长的随机数，只包含大写字母
	 * 
	 * @author Lucifer Wong
	 * @param length
	 *            随机数长度
	 * @return 获取定长的随机数，只包含大写字母
	 */
	public static String generateUpperString(int length) {
		return generateMixString(length).toUpperCase();
	}

}
