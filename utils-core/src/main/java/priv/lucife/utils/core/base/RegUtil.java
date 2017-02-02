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
package priv.lucife.utils.core.base;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import priv.lucife.utils.core.annotation.UBTCompatible;

/**
 * 正则表达式工具类
 * 
 * @author Lucifer Wong
 */
@UBTCompatible
public abstract class RegUtil {

	/**
	 * 从内容中得到匹配的子串
	 * 
	 * @author Lucifer Wong
	 * @param content
	 *            内容
	 * @param regex
	 *            正则表达
	 * @return 子串
	 */
	public static String getMatchStr(String content, String regex) {
		Pattern p = Pattern.compile(regex);
		return getMatchStr(content, p);
	}

	/**
	 * 从内容中得到匹配的子串
	 * 
	 * @author Lucifer Wong
	 * @param content
	 *            内容
	 * @param p
	 *            正则表达
	 * @return 子串
	 */
	public static String getMatchStr(String content, Pattern p) {
		return getMatchStr(content, p, 0);
	}

	/**
	 * 从内容中得到匹配的子串的某个分组
	 * 
	 * @author Lucifer Wong
	 * @param content
	 *            内容
	 * @param regex
	 *            正则表达
	 * @param i
	 *            第i个分组
	 * @return 子串
	 */
	public static String getMatchStr(String content, String regex, int i) {
		Pattern p = Pattern.compile(regex);
		return getMatchStr(content, p, i);
	}

	/**
	 * 从内容中得到匹配的子串的某个分组
	 * 
	 * @author Lucifer Wong
	 * @param content
	 *            内容
	 * @param p
	 *            正则表达
	 * @param i
	 *            1代表第一个, 0代表匹配 的
	 * @return 子串
	 */
	public static String getMatchStr(String content, Pattern p, int i) {
		Matcher m = p.matcher(content);
		if (m.find()) {
			return m.group(i);
		}
		return "";
	}

	/**
	 * 从内容中得到匹配的子串的某个分组,选中多个分组组成一串
	 * 
	 * @author Lucifer Wong
	 * @param content
	 *            内容
	 * @param p
	 *            正则表达
	 * @param groups
	 *            i 1代表第一个, 0代表匹配 的
	 * @return 子串
	 */
	public static String getMatchGroupAsStr(String content, Pattern p, int... groups) {

		Matcher m = p.matcher(content);
		if (m.find()) {
			String temp = "";
			for (int jItem : groups) {
				temp += m.group(jItem);
			}
			return temp;

		}
		return "";
	}

	/**
	 * 得到多个匹配的作为Sring
	 * 
	 * @author Lucifer Wong
	 * @param content
	 *            内容
	 * @param p
	 *            正则表达
	 * @return 子串
	 */
	public static List<String> getMatchAsArray(String content, Pattern p) {
		if (ValidatorUtil.isBlank(content))
			return null;

		List<String> results = new ArrayList<String>();

		Matcher m = p.matcher(content);
		while (m.find()) {
			results.add(m.group(0));
		}
		return results;
	}

	/**
	 * 得到多个匹配的作为Sring
	 * 
	 * @author Lucifer Wong
	 * @param content
	 *            内容
	 * @param pattern
	 *            正则表达
	 * @return 子串数组
	 */
	public static List<String> getMatchAsArray(String content, String pattern) {

		if (ValidatorUtil.isBlank(content))
			return null;
		return getMatchAsArray(content, Pattern.compile(pattern));
	}

	/**
	 * 得到多个带分组的匹配的作为Sring
	 * 
	 * @author Lucifer Wong
	 * @param content
	 *            内容
	 * @param p
	 *            正则表达
	 * @param groups
	 *            1代表第一个, 0代表匹配 的
	 * @return 子串数组
	 */
	public static String[] getMatchGroupAsArray(String content, Pattern p, int... groups) {
		if (ValidatorUtil.isBlank(content))
			return null;

		String[] result = new String[groups.length];
		Matcher m = p.matcher(content);
		if (m.find()) {
			for (int i = 0; i < groups.length; i++) {
				result[i] = m.group(groups[i]);
			}
			return result;
		}
		return null;
	}

	/**
	 * 得到多个带分组的匹配的作为Sring
	 * 
	 * @author Lucifer Wong
	 * @param content
	 *            内容
	 * @param pattern
	 *            正则表达
	 * @param groups
	 *            1代表第一个, 0代表匹配 的
	 * @return 子串数组
	 */
	public static String[] getMatchGroupAsArray(String content, String pattern, int... groups) {

		if (ValidatorUtil.isBlank(content))
			return null;
		return getMatchGroupAsArray(content, Pattern.compile(pattern), groups);
	}
}
