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

import java.io.UnsupportedEncodingException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import priv.lucife.utils.core.annotation.UBTCompatible;

/**
 * 关键字对，用于列表排序
 * 
 * @author Lucifer Wong
 */
@UBTCompatible
class PairKeyword {

	private final int index;
	private final String name;

	public PairKeyword(String name, int index) {
		this.name = name;
		this.index = index;
	}

	public int getIndex() {
		return index;
	}

	public String getName() {
		return name;
	}

}

/**
 * 关键字排序比较器
 * 
 * @author Lucifer Wong
 */
class PairKeywordComparator implements Comparator<PairKeyword> {

	@Override
	public int compare(PairKeyword keyword0, PairKeyword keyword1) {
		return keyword0.getIndex() > keyword1.getIndex() ? 1 : -1;
	}
}

/**
 * 
 * 字符串工具类，对字符串进行常规的处理
 * 
 * @author Lucifer Wong
 */
@UBTCompatible
public abstract class StringUtil {

	private static final String BOOLEAN_FALSE_NUMBER = "0";
	private static final String BOOLEAN_FALSE_STRING = "false";
	private static final String BOOLEAN_TRUE_NUMBER = "1";
	private static final String BOOLEAN_TRUE_STRING = "true";

	public static final String EMPTY = "";
	public static final String SEPARATOR_MULTI = ";";
	public static final String SEPARATOR_SINGLE = "#";
	public static final String SQL_REPLACE = "_";

	/**
	 * 在str右边加入足够多的addStr字符串
	 * 
	 * @author Lucifer Wong
	 * @param str
	 *            源串
	 * @param addStr
	 *            添加串
	 * @param length
	 *            最大长度
	 * @return 添加之后的长度
	 */
	public static String addStringRight(String str, String addStr, int length) {
		StringBuilder builder = new StringBuilder(str);
		while (builder.length() < length) {
			builder.append(addStr);
		}
		return builder.toString();
	}

	/**
	 * 在字符串str拼接分隔符regex和字符串sub
	 * 
	 * @author Lucifer Wong
	 * @param str
	 *            源串
	 * @param sub
	 *            子串
	 * @param regex
	 *            正则
	 * @return 在字符串str拼接分隔符regex和字符串sub
	 */
	public static String addSubString(String str, String sub, String regex) {
		return ValidatorUtil.isEmpty(str) ? sub : str + regex + sub;
	}

	/**
	 * 在字符串str右边补齐0直到长度等于length
	 * 
	 * @author Lucifer Wong
	 * @param str
	 *            源串
	 * @param length
	 *            最大长度
	 * @return 在字符串str右边补齐0直到长度等于length
	 */
	public static String addZeroRight(String str, int length) {
		return addStringRight(str, "0", length);
	}

	/**
	 * 将半角的符号转换成全角符号.
	 * 
	 * @author Lucifer Wong
	 *
	 * @param str
	 *            要转换的字符
	 * @return 将半角的符号转换成全角符号
	 */
	public static String changeToFull(String str) {
		String source = "1234567890!@#$%^&*()abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ-_=+\\|[];:'\",<.>/?";
		String[] decode = { "１", "２", "３", "４", "５", "６", "７", "８", "９", "０", "！", "＠", "＃", "＄", "％", "︿", "＆", "＊",
				"（", "）", "ａ", "ｂ", "ｃ", "ｄ", "ｅ", "ｆ", "ｇ", "ｈ", "ｉ", "ｊ", "ｋ", "ｌ", "ｍ", "ｎ", "ｏ", "ｐ", "ｑ", "ｒ", "ｓ",
				"ｔ", "ｕ", "ｖ", "ｗ", "ｘ", "ｙ", "ｚ", "Ａ", "Ｂ", "Ｃ", "Ｄ", "Ｅ", "Ｆ", "Ｇ", "Ｈ", "Ｉ", "Ｊ", "Ｋ", "Ｌ", "Ｍ", "Ｎ",
				"Ｏ", "Ｐ", "Ｑ", "Ｒ", "Ｓ", "Ｔ", "Ｕ", "Ｖ", "Ｗ", "Ｘ", "Ｙ", "Ｚ", "－", "＿", "＝", "＋", "＼", "｜", "【", "】", "；",
				"：", "'", "\"", "，", "〈", "。", "〉", "／", "？" };
		String result = "";
		for (int i = 0; i < str.length(); i++) {
			int pos = source.indexOf(str.charAt(i));
			if (pos != -1) {
				result += decode[pos];
			} else {
				result += str.charAt(i);
			}
		}
		return result;
	}

	/**
	 * 计算字符串str中字符sub的个数
	 * 
	 * @author Lucifer Wong
	 * @param str
	 *            source
	 * @param sub
	 *            子串
	 * @return 计算字符串str中字符sub的个数
	 */
	public static int charCount(String str, char sub) {
		int charCount = 0;
		int fromIndex = 0;

		while ((fromIndex = str.indexOf(sub, fromIndex)) != -1) {
			fromIndex++;
			charCount++;
		}
		return charCount;
	}

	/**
	 * 计算字符串str右边出现多少次sub
	 * 
	 * @author Lucifer Wong
	 * @param str
	 *            source
	 * @param sub
	 *            子串
	 * @return 计算字符串str右边出现多少次sub
	 */
	public static int charCountRight(String str, String sub) {
		if (str == null) {
			return 0;
		}

		int charCount = 0;
		String subStr = str;
		int currentLength = subStr.length() - sub.length();
		while (currentLength >= 0 && subStr.substring(currentLength).equals(sub)) {
			charCount++;
			subStr = subStr.substring(0, currentLength);
			currentLength = subStr.length() - sub.length();
		}
		return charCount;
	}

	/**
	 * @author Lucifer Wong
	 * 
	 *         Counts how many times the substring appears in the larger String.
	 * 
	 * 
	 * 
	 *         A <code>null</code> or empty ("") String input returns
	 *         <code>0</code>.
	 * 
	 * 
	 *         <pre>
	 *   StringUtils.countMatches(null, *)                           = 0
	 *   StringUtils.countMatches(&quot;&quot;, *)                   = 0
	 *   StringUtils.countMatches(&quot;abba&quot;, null)            = 0
	 *   StringUtils.countMatches(&quot;abba&quot;, &quot;&quot;)    = 0
	 *   StringUtils.countMatches(&quot;abba&quot;, &quot;a&quot;)   = 2
	 *   StringUtils.countMatches(&quot;abba&quot;, &quot;ab&quot;)  = 1
	 *   StringUtils.countMatches(&quot;abba&quot;, &quot;xxx&quot;) = 0
	 *         </pre>
	 * 
	 * @param str
	 *            the String to check, may be null
	 * @param sub
	 *            the substring to count, may be null
	 * @return the number of occurrences, 0 if either String is
	 *         <code>null</code>
	 */
	public static int countMatches(String str, String sub) {
		if (ValidatorUtil.isEmpty(str) || ValidatorUtil.isEmpty(sub)) {
			return 0;
		}

		int count = 0;
		int idx = 0;
		while ((idx = str.indexOf(sub, idx)) != -1) {
			count++;
			idx += sub.length();
		}
		return count;
	}

	/**
	 * 截取固定长度的字符串，剩余部分真实长度不会超过len，超长部分用suffix代替。
	 * 
	 * @author Lucifer Wong
	 * @param str
	 *            source
	 * @param len
	 *            最大真实长度
	 * @param suffix
	 *            代替子串
	 * @return 截取固定长度的字符串，剩余部分真实长度不会超过len，超长部分用suffix代替。
	 * @deprecated 已经被 {@link #cutOut(String, int, String)} 取代，该方法的显示格式会更整齐。
	 */
	@Deprecated
	public static String cutOff(String str, int len, String suffix) {
		if (ValidatorUtil.isEmpty(str)) {
			return str;
		}

		int byteIndex = 0;
		int charIndex = 0;

		while (charIndex < str.length() && byteIndex < len) {
			if (str.charAt(charIndex) >= 256) {
				byteIndex += 2;
			} else {
				byteIndex++;
			}

			charIndex++;
		}
		return byteIndex < len ? str.substring(0, charIndex) : str.substring(0, charIndex) + suffix;
	}

	/**
	 * 截取固定长度的字符串，超长部分用suffix代替，最终字符串真实长度不会超过maxLength.
	 * 
	 * @author Lucifer Wong
	 * @param str
	 *            source
	 * @param maxLength
	 *            最大真实长度
	 * @param suffix
	 *            代替子串
	 * @return 截取固定长度的字符串，剩余部分真实长度不会超过len，超长部分用suffix代替
	 */
	public static String cutOut(String str, int maxLength, String suffix) {
		if (ValidatorUtil.isEmpty(str)) {
			return str;
		}

		int byteIndex = 0;
		int charIndex = 0;

		while (charIndex < str.length() && byteIndex <= maxLength) {
			char c = str.charAt(charIndex);
			if (c >= 256) {
				byteIndex += 2;
			} else {
				byteIndex++;
			}
			charIndex++;
		}

		if (byteIndex <= maxLength) {
			return str;
		}

		StringBuilder sb = new StringBuilder();
		sb.append(str.substring(0, charIndex));
		sb.append(suffix);

		while (getRealLength(sb.toString()) > maxLength) {
			sb.deleteCharAt(--charIndex);
		}

		return sb.toString();
	}

	/**
	 * 过滤html标签，包括script、style、html、空格、回车标签
	 * 
	 * @author Lucifer Wong
	 *
	 * @param htmlStr
	 *            source
	 * @return 过滤后的文本
	 */
	public static String delHTMLTag(String htmlStr) {
		String regEx_script = "<script[^>]*?>[\\s\\S]*?<\\/script>"; // 定义script的正则表达式
		String regEx_style = "<style[^>]*?>[\\s\\S]*?<\\/style>"; // 定义style的正则表达式
		String regEx_html = "<[^>]+>"; // 定义HTML标签的正则表达式
		String regEx_space = "\\s*|\t|\r|\n";// 定义空格回车换行符

		Pattern p_script = Pattern.compile(regEx_script, Pattern.CASE_INSENSITIVE);
		Matcher m_script = p_script.matcher(htmlStr);
		htmlStr = m_script.replaceAll(""); // 过滤script标签

		Pattern p_style = Pattern.compile(regEx_style, Pattern.CASE_INSENSITIVE);
		Matcher m_style = p_style.matcher(htmlStr);
		htmlStr = m_style.replaceAll(""); // 过滤style标签

		Pattern p_html = Pattern.compile(regEx_html, Pattern.CASE_INSENSITIVE);
		Matcher m_html = p_html.matcher(htmlStr);
		htmlStr = m_html.replaceAll(""); // 过滤html标签

		Pattern p_space = Pattern.compile(regEx_space, Pattern.CASE_INSENSITIVE);
		Matcher m_space = p_space.matcher(htmlStr);
		htmlStr = m_space.replaceAll(""); // 过滤空格回车标签

		return htmlStr.trim(); // 返回文本字符串
	}

	/**
	 * 返回两个字符串的最少编辑距离(由一个字符串通过删除、替换、增加字符、字符换位 变成另一个字符串)
	 * 
	 * @author Lucifer Wong
	 * @param source
	 *            source
	 * @param target
	 *            target
	 * @return 最少编辑距离
	 */
	public static int distanceDamerauLevenshtein(CharSequence source, CharSequence target) {
		if (source == null || "".equals(source)) {
			return target == null || "".equals(target) ? 0 : target.length();
		} else if (target == null || "".equals(target)) {
			return source.length();
		}

		int srcLen = source.length();
		int targetLen = target.length();
		int[][] distanceMatrix = new int[srcLen + 1][targetLen + 1];

		// We need indexers from 0 to the length of the source string.
		// This sequential set of numbers will be the row "headers"
		// in the matrix.
		for (int srcIndex = 0; srcIndex <= srcLen; srcIndex++) {
			distanceMatrix[srcIndex][0] = srcIndex;
		}

		// We need indexers from 0 to the length of the target string.
		// This sequential set of numbers will be the
		// column "headers" in the matrix.
		for (int targetIndex = 0; targetIndex <= targetLen; targetIndex++) {
			// Set the value of the first cell in the column
			// equivalent to the current value of the iterator
			distanceMatrix[0][targetIndex] = targetIndex;
		}

		for (int srcIndex = 1; srcIndex <= srcLen; srcIndex++) {
			for (int targetIndex = 1; targetIndex <= targetLen; targetIndex++) {
				// If the current characters in both strings are equal
				int cost = source.charAt(srcIndex - 1) == target.charAt(targetIndex - 1) ? 0 : 1;

				int[] values = new int[] {
						// Character match between current character in
						// source string and next character in target
						distanceMatrix[srcIndex - 1][targetIndex] + 1,
						// Character match between next character in
						// source string and current character in target
						distanceMatrix[srcIndex][targetIndex - 1] + 1,
						// No match, at current, add cumulative penalty
						distanceMatrix[srcIndex - 1][targetIndex - 1] + cost };
				int len = values.length;
				int current = values[0];

				for (int k = 1; k < len; k++) {
					current = Math.min(values[k], current);
				}
				// Find the current distance by determining the shortest path to
				// a
				// match (hence the 'minimum' calculation on distances).
				distanceMatrix[srcIndex][targetIndex] = current;

				// We don't want to do the next series of calculations on
				// the first pass because we would get an index out of bounds
				// exception.
				if (srcIndex == 1 || targetIndex == 1) {
					continue;
				}

				// transposition check (if the current and previous
				// character are switched around (e.g.: t[se]t and t[es]t)...
				if (source.charAt(srcIndex - 1) == target.charAt(targetIndex - 2)
						&& source.charAt(srcIndex - 2) == target.charAt(targetIndex - 1)) {

					int[] values2 = new int[] {
							// Current cost
							distanceMatrix[srcIndex][targetIndex],
							// Transposition
							distanceMatrix[srcIndex - 2][targetIndex - 2] + cost };
					int len2 = values2.length;
					int current2 = values2[0];

					for (int k = 1; k < len2; k++) {
						current2 = Math.min(values2[k], current2);
					}
					// What's the minimum cost between the current distance
					// and a transposition.
					distanceMatrix[srcIndex][targetIndex] = current2;
				}
			}
		}

		return distanceMatrix[srcLen][targetLen];
	}

	/**
	 * 返回两个字符串的最少编辑距离(由一个字符串通过删除、替换、增加字符变成另一个字符串)
	 * 
	 * @author Lucifer Wong
	 * @param s
	 *            String one
	 * @param t
	 *            String two
	 * @return 最少编辑距离
	 */
	public static int distanceLevenshtein(CharSequence s, CharSequence t) {
		// degenerate cases s
		if (s == null || "".equals(s)) {
			return t == null || "".equals(t) ? 0 : t.length();
		} else if (t == null || "".equals(t)) {
			return s.length();
		}

		// create two work vectors of integer distances
		int[] v0 = new int[t.length() + 1];
		int[] v1 = new int[t.length() + 1];

		// initialize v0 (the previous row of distances)
		// this row is A[0][i]: edit distance for an empty s
		// the distance is just the number of characters to delete from t
		for (int i = 0; i < v0.length; i++) {
			v0[i] = i;
		}

		int sLen = s.length();
		int tLen = t.length();
		for (int i = 0; i < sLen; i++) {
			// calculate v1 (current row distances) from the previous row v0

			// first element of v1 is A[i+1][0]
			// edit distance is delete (i+1) chars from s to match empty t
			v1[0] = i + 1;

			// use formula to fill in the rest of the row
			for (int j = 0; j < tLen; j++) {
				int cost = (s.charAt(i) == t.charAt(j)) ? 0 : 1;

				int[] values = new int[] { v1[j] + 1, v0[j + 1] + 1, v0[j] + cost };
				int len = values.length;
				int current = values[0];

				for (int k = 1; k < len; k++) {
					current = Math.min(values[k], current);
				}
				v1[j + 1] = current;
			}

			// copy v1 (current row) to v0 (previous row) for next iteration
			System.arraycopy(v1, 0, v0, 0, v0.length);
		}

		return v1[t.length()];
	}

	/**
	 * 在字符串str左边补齐0直到长度等于length
	 * 
	 * @author Lucifer Wong
	 * @param str
	 *            source
	 * @param len
	 *            最终长度
	 * @return 在字符串str左边补齐0直到长度等于length
	 */
	public static String enoughZero(String str, int len) {
		while (str.length() < len) {
			str = "0" + str;
		}
		return str;
	}

	/**
	 * 判断两个字符串是否equals
	 * 
	 * @author Lucifer Wong
	 * @param str1
	 *            字符串1
	 * @param str2
	 *            字符串2
	 * @return Compares this string to the specified object. The result is true
	 *         if and only if the argument is not null and is a String object
	 *         that represents the same sequence of characters as this object.
	 */
	public static boolean equals(String str1, String str2) {
		if (str1 == null || str2 == null) {
			return false;
		}
		return str1.equals(str2);
	}

	/**
	 * 比较两个字符串是否相同(忽略大小写)
	 * 
	 * @author Lucifer Wong
	 * @param s1
	 *            字符串1
	 * @param s2
	 *            字符串2
	 * @return Compares this String to another String, ignoring case
	 *         considerations. Two strings are considered equal ignoring case if
	 *         they are of the same length and corresponding characters in the
	 *         two strings are equal ignoring case.
	 * 
	 * 
	 *         Two characters {@code c1} and {@code c2} are considered the same
	 *         ignoring case if at least one of the following is true:
	 *         <ul>
	 *         <li>The two characters are the same (as compared by the
	 *         {@code ==} operator)
	 *         <li>Applying the method
	 *         {@link java.lang.Character#toUpperCase(char)} to each character
	 *         produces the same result
	 *         <li>Applying the method
	 *         {@link java.lang.Character#toLowerCase(char)} to each character
	 *         produces the same result
	 *         </ul>
	 * 
	 */
	public static boolean equalsIgnoreCase(final String s1, final String s2) {
		if (s1 == null || s2 == null) {
			return s1 == s2;
		}
		return s1.equalsIgnoreCase(s2);
	}

	/**
	 * 比较两个字符串是否相同(忽视左右空白 忽略大小写)
	 * 
	 * @author Lucifer Wong
	 * @param s1
	 *            字符串1
	 * @param s2
	 *            字符串2
	 * @return this String to another String, ignoring case considerations. Two
	 *         strings are considered equal ignoring case if they are of the
	 *         same length and corresponding characters in the two strings are
	 *         equal ignoring case.
	 * 
	 * 
	 *         Two characters {@code c1} and {@code c2} are considered the same
	 *         ignoring case if at least one of the following is true:
	 *         <ul>
	 *         <li>The two characters are the same (as compared by the
	 *         {@code ==} operator)
	 *         <li>Applying the method
	 *         {@link java.lang.Character#toUpperCase(char)} to each character
	 *         produces the same result
	 *         <li>Applying the method
	 *         {@link java.lang.Character#toLowerCase(char)} to each character
	 *         produces the same result
	 *         </ul>
	 */
	public static boolean equalsIgnoreCaseWithTrim(final String s1, final String s2) {
		if (s1 == null || s2 == null) {
			return s1 == s2;
		}
		return s1.trim().equalsIgnoreCase(s2.trim());
	}

	/**
	 * 比较两个字符串是否相同(忽视左右空白)
	 * 
	 * @author Lucifer Wong
	 * @param s1
	 *            字符串1
	 * @param s2
	 *            字符串2
	 * @return true if the given object represents a String equivalent to this
	 *         string, false otherwise
	 */
	public static boolean equalsWithTrim(final String s1, final String s2) {
		if (s1 == null || s2 == null) {
			return s1 == s2;
		}
		return s1.trim().equals(s2.trim());
	}

	/**
	 * 用来显示异常信息的html过滤器
	 * 
	 * @author Lucifer Wong
	 * @param value
	 *            替换为html的换行&lt;br&gt; tab为&amp;bsp;
	 * @return 替换后的字符串
	 */
	public static String exceptionFilter(String value) {
		return ValidatorUtil.isEmpty(value) ? "" : value.replaceAll("\n", "<br>").replaceAll("\t", "&nbsp; &nbsp; ");
	}

	/**
	 * @author Lucifer Wong
	 * @param text
	 *            将要被格式化的字符串 <br>
	 *            eg:参数一:{0},参数二:{1},参数三:{2}
	 * 
	 * @param args
	 *            将替代字符串中的参数,些参数将替换{X} <br>
	 *            eg:new Object[] { "0001", "0005049", new Integer(1) }
	 * @return 格式化后的字符串 <br>
	 *         eg: 在上面的输入下输出为:参数一:0001,参数二:0005049,参数三:1
	 */
	public static String format(String text, Object[] args) {
		if (ValidatorUtil.isEmpty(text) || args == null || args.length == 0) {
			return text;
		}
		for (int i = 0, length = args.length; i < length; i++) {
			text = replace(text, "{" + i + "}", args[i].toString());
		}
		return text;
	}

	/**
	 * 格式化浮点型数字成字符串, 保留两位小数位.
	 * 
	 * @author Lucifer Wong
	 * @param number
	 *            浮点数字
	 * @return 格式化之后的字符串
	 */
	public static String formatDecimal(double number) {
		NumberFormat format = NumberFormat.getInstance();

		format.setMaximumIntegerDigits(Integer.MAX_VALUE);
		format.setMaximumFractionDigits(2);
		format.setMinimumFractionDigits(2);

		return format.format(number);
	}

	/**
	 * 格式化浮点类型数据.
	 * 
	 * @author Lucifer Wong
	 * @param number
	 *            浮点数据
	 * @param minFractionDigits
	 *            最小保留小数位
	 * @param maxFractionDigits
	 *            最大保留小数位
	 * @return 将浮点数据格式化后的字符串
	 */
	public static String formatDecimal(double number, int minFractionDigits, int maxFractionDigits) {
		NumberFormat format = NumberFormat.getInstance();
		format.setMinimumFractionDigits(minFractionDigits);
		format.setMaximumFractionDigits(minFractionDigits);

		return format.format(number);
	}

	/**
	 * 获得成对出现的第一个关键字对应的关键字的位置。
	 * 
	 * @author Lucifer Wong
	 * @param str
	 *            source
	 * @param keyword
	 *            关键字，例如：select
	 * @param oppositeKeyword
	 *            对应的关键字，例如：from
	 * @return 第一个关键字对应的关键字的位置
	 */
	public static int getFirstPairIndex(String str, String keyword, String oppositeKeyword) {
		ArrayList<PairKeyword> keywordArray = new ArrayList<PairKeyword>();
		int index = -1;
		while ((index = str.indexOf(keyword, index)) != -1) {
			keywordArray.add(new PairKeyword(keyword, index));
			index += keyword.length();
		}

		index = -1;
		while ((index = str.indexOf(oppositeKeyword, index)) != -1) {
			keywordArray.add(new PairKeyword(oppositeKeyword, index));
			index += oppositeKeyword.length();
		}

		if (keywordArray.size() < 2) {
			return -1;
		}

		Collections.sort(keywordArray, new PairKeywordComparator());

		PairKeyword firstKeyword = keywordArray.get(0);
		if (!firstKeyword.getName().equals(keyword)) {
			return -1;
		}

		while (keywordArray.size() > 2) {
			boolean hasOpposite = false;
			for (int i = 2; i < keywordArray.size(); i++) {
				PairKeyword keyword0 = keywordArray.get(i - 1);
				PairKeyword keyword1 = keywordArray.get(i);
				if (keyword0.getName().equals(keyword) && keyword1.getName().equals(oppositeKeyword)) {
					keywordArray.remove(i);
					keywordArray.remove(i - 1);
					hasOpposite = true;
					break;
				}
			}
			if (!hasOpposite) {
				return -1;
			}
		}

		if (keywordArray.size() != 2) {
			return -1;
		}

		PairKeyword lastKeyword = keywordArray.get(1);
		if (!lastKeyword.getName().equals(oppositeKeyword)) {
			return -1;
		}

		return lastKeyword.getIndex();
	}

	/**
	 * 取得字符串的真实长度，一个汉字长度为两个字节。
	 * 
	 * @author Lucifer Wong
	 * @param str
	 *            字符串
	 * @return 字符串的字节数
	 */
	public static int getRealLength(String str) {
		if (str == null) {
			return 0;
		}

		char separator = 256;
		int realLength = 0;

		for (int i = 0; i < str.length(); i++) {
			if (str.charAt(i) >= separator) {
				realLength += 2;
			} else {
				realLength++;
			}
		}
		return realLength;
	}

	/**
	 * 将以ASCII字符表示的16进制字符串以每两个字符分割转换为16进制表示的byte数组.<br>
	 * e.g. "e024c854" --&gt; byte[]{0xe0, 0x24, 0xc8, 0x54}
	 * 
	 * @author Lucifer Wong
	 * @param str
	 *            原16进制字符串
	 * @return 16进制表示的byte数组
	 */
	public static byte[] hexString2Bytes(String str) {
		if (ValidatorUtil.isEmpty(str)) {
			return null;
		}

		if (str.length() % 2 != 0) {
			str = "0" + str;
		}

		byte[] result = new byte[str.length() / 2];
		for (int i = 0; i < result.length; i++) {
			// High bit
			byte high = (byte) (Byte.decode("0x" + str.charAt(i * 2)).byteValue() << 4);
			// Low bit
			byte low = Byte.decode("0x" + str.charAt(i * 2 + 1)).byteValue();
			result[i] = (byte) (high ^ low);
		}
		return result;
	}

	/**
	 * 把16进制表达的字符串转换为整数
	 * 
	 * @author Lucifer Wong
	 * @param hexString
	 *            16进制表达的字符串
	 * @return Returns the value of this Integer as an int.
	 */
	public static int hexString2Int(String hexString) {
		return Integer.valueOf(hexString, 16).intValue();
	}

	/**
	 * HTML 文本过滤，如果 value 为 <code>null</code> 或为空串，则返回 "&amp;nbsp;"。
	 * 
	 * @author Lucifer Wong
	 * 
	 *         转换的字符串关系如下：
	 * 
	 *         <ul>
	 *         <li>&amp; --&gt; &amp;amp;</li>
	 *         <li>&lt; --&gt; &amp;lt;</li>
	 *         <li>&gt; --&gt; &amp;gt;</li>
	 *         <li>&quot; --&gt; &amp;quot;</li>
	 *         <li>\n --&gt; &lt;br/&gt;</li>
	 *         <li>\t --&gt; &amp;nbsp;&amp;nbsp;&amp;nbsp;&amp;nbsp;</li>
	 *         <li>空格 --&gt; &amp;nbsp;</li>
	 *         </ul>
	 * 
	 *         <strong>此方法适用于在 HTML 页面上的非文本框元素（div、span、table
	 *         等）中显示文本时调用。</strong>
	 * 
	 * @param value
	 *            要过滤的文本
	 * @return 过滤后的 HTML 文本
	 */
	public static String htmlFilter(String value) {
		if (value == null || value.length() == 0) {
			return "&nbsp;";
		}

		return value.replaceAll("&", "&amp;").replaceAll("\t", "    ").replaceAll(" ", "&nbsp;").replaceAll("<", "&lt;")
				.replaceAll(">", "&gt;").replaceAll("\"", "&quot;").replaceAll("\n", "<br/>");
	}

	/**
	 * HTML 文本过滤，如果 value 为 <code>null</code> 或为空串，则返回空串。
	 * 
	 * 
	 * 转换的字符串关系如下：
	 * 
	 * <ul>
	 * <li>&amp; --&gt; &amp;amp;</li>
	 * <li>&lt; --&gt; &amp;lt;</li>
	 * <li>&gt; --&gt; &amp;gt;</li>
	 * <li>&quot; --&gt; &amp;quot;</li>
	 * <li>\n --&gt; &lt;br/&gt;</li>
	 * </ul>
	 * 
	 * <strong>此方法适用于在 HTML 页面上的文本框（text、textarea）中显示文本时调用。</strong>
	 * 
	 * @author Lucifer Wong
	 * @param value
	 *            要过滤的文本
	 * @return 过滤后的 HTML 文本
	 */
	public static String htmlFilterToEmpty(String value) {
		if (value == null || value.length() == 0) {
			return "";
		}

		return value.replaceAll("&", "&amp;").replaceAll("<", "&lt;").replaceAll(">", "&gt;").replaceAll("\"",
				"&quot;");
	}

	/**
	 * 清除字符串中的回车和换行符
	 * 
	 * @author Lucifer Wong
	 * @param str
	 *            要过滤的文本
	 * @return 过滤后的文本 \r|\n
	 */
	public static String ignoreEnter(String str) {
		if (str == null || str.length() == 0) {
			return null;
		}

		return str.replaceAll("\r|\n", "");
	}

	/**
	 * 忽略值为 <code>null</code> 的字符串
	 * 
	 * @author Lucifer Wong
	 * @param str
	 *            字符串
	 * @return 如果字符串为 <code>null</code>, 则返回空字符串.
	 */
	public static String ignoreNull(String str) {
		return str == null ? "" : str;
	}

	/**
	 * 判断字符串是否全部都为小写<br>
	 * <blockquote>
	 * 
	 * <pre>
	 * a b c d e f g h i j k l m n o p q r s t u v w x y z
	 * '&#92;u00DF' '&#92;u00E0' '&#92;u00E1' '&#92;u00E2' '&#92;u00E3' '&#92;u00E4' '&#92;u00E5' '&#92;u00E6'
	 * '&#92;u00E7' '&#92;u00E8' '&#92;u00E9' '&#92;u00EA' '&#92;u00EB' '&#92;u00EC' '&#92;u00ED' '&#92;u00EE'
	 * '&#92;u00EF' '&#92;u00F0' '&#92;u00F1' '&#92;u00F2' '&#92;u00F3' '&#92;u00F4' '&#92;u00F5' '&#92;u00F6'
	 * '&#92;u00F8' '&#92;u00F9' '&#92;u00FA' '&#92;u00FB' '&#92;u00FC' '&#92;u00FD' '&#92;u00FE' '&#92;u00FF'
	 * </pre>
	 * 
	 * </blockquote>
	 * 
	 * @author Lucifer Wong
	 * @param value
	 *            待判断的字符串
	 * @return 判断字符串是否全部都为小写
	 */
	public static boolean isAllLowerCase(String value) {
		if (value == null || "".equals(value)) {
			return false;
		}
		for (int i = 0; i < value.length(); i++) {
			if (Character.isLowerCase(value.charAt(i)) == false) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 判断字符串是否全部大写
	 * 
	 * <blockquote>
	 * 
	 * <pre>
	 * A B C D E F G H I J K L M N O P Q R S T U V W X Y Z
	 * '&#92;u00C0' '&#92;u00C1' '&#92;u00C2' '&#92;u00C3' '&#92;u00C4' '&#92;u00C5' '&#92;u00C6' '&#92;u00C7'
	 * '&#92;u00C8' '&#92;u00C9' '&#92;u00CA' '&#92;u00CB' '&#92;u00CC' '&#92;u00CD' '&#92;u00CE' '&#92;u00CF'
	 * '&#92;u00D0' '&#92;u00D1' '&#92;u00D2' '&#92;u00D3' '&#92;u00D4' '&#92;u00D5' '&#92;u00D6' '&#92;u00D8'
	 * '&#92;u00D9' '&#92;u00DA' '&#92;u00DB' '&#92;u00DC' '&#92;u00DD' '&#92;u00DE'
	 * </pre>
	 * 
	 * </blockquote>
	 * 
	 * @author Lucifer Wong
	 * 
	 * @param value
	 *            待判断的字符串
	 * @return 判断字符串是否全部大写
	 */
	public static boolean isAllUpperCase(String value) {
		if (value == null || "".equals(value)) {
			return false;
		}
		for (int i = 0; i < value.length(); i++) {
			if (Character.isUpperCase(value.charAt(i)) == false) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 数字验证
	 * 
	 * @author Lucifer Wong
	 * @param str
	 *            待判断的字符串
	 * @return 数字验证
	 */
	public static boolean isDigital(String str) {
		return str == null || "".equals(str) ? false : str.matches("^[0-9]*$");
	}

	/**
	 * 只否包括"\""等不利于文本框显示的字符
	 * 
	 * @author Lucifer Wong
	 * @param arg
	 *            待判断的字符串
	 * @return 只否包括"\""等不利于文本框显示的字符
	 */
	public static boolean isNotAllowed4TextBox(String arg) {
		if (ValidatorUtil.isEmpty(arg)) {
			return false;
		}

		return arg.indexOf("\"") >= 0;
	}

	/**
	 * 判断 value 的值是否表示条件为假。例子：
	 * 
	 * <ul>
	 * <li>"0" -&gt; true</li>
	 * <li>"false" -&gt; true</li>
	 * <li>"False" -&gt; true</li>
	 * <li>"FALSE" -&gt; true</li>
	 * <li>"1" -&gt; false</li>
	 * <li>"true" -&gt; false</li>
	 * <li>"test" -&gt; false</li>
	 * </ul>
	 * 
	 * @author Lucifer Wong
	 * @param value
	 *            字符串
	 * @return 如果 value 等于 “0” 或者 “false”（大小写无关） 返回 <code>true</code>，否则返回
	 *         <code>false</code>。
	 */
	public static boolean isValueFalse(String value) {
		return BOOLEAN_FALSE_NUMBER.equals(value) || BOOLEAN_FALSE_STRING.equalsIgnoreCase(value);
	}

	/**
	 * 判断 value 的值是否表示条件为真。例子：
	 * 
	 * <ul>
	 * <li>"1" -&gt; true</li>
	 * <li>"true" -&gt; true</li>
	 * <li>"True" -&gt; true</li>
	 * <li>"TRUE" -&gt; true</li>
	 * <li>"2" -&gt; false</li>
	 * <li>"false" -&gt; false</li>
	 * <li>"test" -&gt; false</li>
	 * </ul>
	 * 
	 * @author Lucifer Wong
	 * @param value
	 *            字符串
	 * @return 如果 value 等于 “1” 或者 “true”（大小写无关） 返回 <code>true</code>，否则返回
	 *         <code>false</code>。
	 */
	public static boolean isValueTrue(String value) {
		return BOOLEAN_TRUE_NUMBER.equals(value) || BOOLEAN_TRUE_STRING.equalsIgnoreCase(value);
	}

	/**
	 * 过滤html的"'"字符(转义为"\'")以及其他特殊字符, 主要用于链接地址的特殊字符过滤.
	 * 
	 * @author Lucifer Wong
	 * @param str
	 *            要过滤的字符串
	 * @return 过滤后的字符串
	 */
	public static String linkFilter(String str) {
		if (ValidatorUtil.isEmpty(str)) {
			return str;
		}

		return htmlFilter(htmlFilter(str.replaceAll("'", "\\\\'")));
	}

	/**
	 * 清除字符串左侧的空格
	 * 
	 * @author Lucifer Wong
	 * @param str
	 *            要过滤的字符串
	 * @return 清除字符串左侧的空格
	 */
	public static String ltrim(String str) {
		return ltrim(str, " ");
	}

	/**
	 * 清除字符串左侧的指定字符串
	 * 
	 * @author Lucifer Wong
	 * @param str
	 *            要过滤的字符串
	 * @param remove
	 *            指定的字符串
	 * @return 清除字符串左侧的指定字符串
	 */
	public static String ltrim(String str, String remove) {
		if (str == null || str.length() == 0 || remove == null || remove.length() == 0) {
			return str;
		}

		while (str.startsWith(remove)) {
			str = str.substring(remove.length());
		}
		return str;
	}

	/**
	 * 将某个字符重复N次
	 * 
	 * @author Lucifer Wong
	 *
	 * @param ch
	 *            需要循环的字符
	 * @param count
	 *            循环的次数
	 * @return 将某个字符重复N次
	 */
	public static String repeatChar(char ch, int count) {
		char[] buf = new char[count];
		for (int i = count - 1; i >= 0; i--) {
			buf[i] = ch;
		}
		return new String(buf);
	}

	/**
	 * 将字符串重复N次，null、""不在循环次数里面 <br>
	 * 当value == null || value == "" return value;<br>
	 * 当count &lt;= 1 返回 value
	 * 
	 * @author Lucifer Wong
	 *
	 * @param value
	 *            需要循环的字符串
	 * @param count
	 *            循环的次数
	 * @return 将某个字符重复N次
	 */
	public static String repeatString(String value, int count) {
		if (value == null || "".equals(value) || count <= 1) {
			return value;
		}

		int length = value.length();
		if (length == 1) { // 长度为1，存在字符
			return repeatChar(value.charAt(0), count);
		}

		int outputLength = length * count;
		switch (length) {
		case 1:
			return repeatChar(value.charAt(0), count);
		case 2:
			char ch0 = value.charAt(0);
			char ch1 = value.charAt(1);
			char[] output2 = new char[outputLength];
			for (int i = count * 2 - 2; i >= 0; i--, i--) {
				output2[i] = ch0;
				output2[i + 1] = ch1;
			}
			return new String(output2);
		default:
			StringBuilder buf = new StringBuilder(outputLength);
			for (int i = 0; i < count; i++) {
				buf.append(value);
			}
			return buf.toString();
		}

	}

	/**
	 * @author Lucifer Wong
	 * @param text
	 *            要替换的文本
	 * @param repl
	 *            the substring to search for.
	 * @param with
	 *            替换的文本
	 * @return
	 * 
	 * 		Replaces all occurrences of a String within another String.
	 * 
	 */
	public static String replace(String text, String repl, String with) {
		return replace(text, repl, with, -1);
	}

	/**
	 * 
	 * Replaces a String with another String inside a larger String, for the
	 * first <code>max</code> values of the search String.
	 * 
	 * @author Lucifer Wong
	 * @param text
	 *            要替换的文本
	 * @param repl
	 *            the substring to search for.
	 * @param with
	 *            替换的文本
	 * @param max
	 *            最大长度
	 * @return
	 * 
	 * 		Replaces a String with another String inside a larger String, for
	 *         the first <code>max</code> values of the search String.
	 */
	public static String replace(String text, String repl, String with, int max) {
		if (text == null || ValidatorUtil.isEmpty(repl) || with == null || max == 0) {
			return text;
		}

		StringBuilder buf = new StringBuilder(text.length());
		int start = 0, end = 0;
		while ((end = text.indexOf(repl, start)) != -1) {
			buf.append(text.substring(start, end)).append(with);
			start = end + repl.length();

			if (--max == 0) {
				break;
			}
		}
		buf.append(text.substring(start));
		return buf.toString();
	}

	/**
	 * 反转字符串
	 * 
	 * @author Lucifer Wong
	 *
	 * @param value
	 *            待反转的字符串
	 * @return 反转的字符串
	 */
	public static String reverse(String value) {
		if (value == null) {
			return null;
		}
		return new StringBuffer(value).reverse().toString();
	}

	/**
	 * 清除字符串右侧的空格
	 * 
	 * @author Lucifer Wong
	 * @param str
	 *            待清除字符串
	 * @return 清除右侧的空格字符串
	 */
	public static String rtrim(String str) {
		return rtrim(str, " ");
	}

	/**
	 * 清除字符串右侧的指定字符串
	 * 
	 * @author Lucifer Wong
	 * @param str
	 *            待清除字符串
	 * @param remove
	 *            指定字符串
	 * @return 清除右侧的指定字符串后的符串
	 */
	public static String rtrim(String str, String remove) {
		if (str == null || str.length() == 0 || remove == null || remove.length() == 0) {
			return str;
		}

		while (str.endsWith(remove) && (str.length() - remove.length()) >= 0) {
			str = str.substring(0, str.length() - remove.length());
		}
		return str;
	}

	/**
	 * 根据分割符对字符串进行分割，每个分割后的字符串将被 <tt>trim</tt> 后放到列表中。
	 * 
	 * @author Lucifer Wong
	 * @param str
	 *            将要被分割的字符串
	 * @param delimiter
	 *            分隔符
	 * @return 分割后的结果列表
	 */
	public static final List<String> split(String str, char delimiter) {
		// return no groups if we have an empty string
		if (str == null || "".equals(str)) {
			return Collections.emptyList();
		}

		ArrayList<String> parts = new ArrayList<String>();
		int currentIndex;
		int previousIndex = 0;

		while ((currentIndex = str.indexOf(delimiter, previousIndex)) > 0) {
			String part = str.substring(previousIndex, currentIndex).trim();
			parts.add(part);
			previousIndex = currentIndex + 1;
		}

		parts.add(str.substring(previousIndex, str.length()).trim());

		return parts;
	}

	/**
	 * 把字符串按照规则分割<br>
	 * 比如str为&quot;id=123&amp;name=test&quot;，rule为&quot;id=#&amp;name=#&quot;，
	 * 分隔后为[&quot;123&quot;, &quot;test&quot;]
	 * 
	 * @author Lucifer Wong
	 * @param str
	 *            被分割的字符串
	 * @param rule
	 *            规则
	 * @return 把字符串按照规则分割
	 */
	public static String[] split(String str, String rule) {
		if (rule.indexOf(SEPARATOR_SINGLE) == -1 || rule.indexOf(SEPARATOR_SINGLE + SEPARATOR_SINGLE) != -1) {
			throw new IllegalArgumentException("Could not parse rule");
		}

		String[] rules = rule.split(SEPARATOR_SINGLE);
		// System.out.println(rules.length);

		if (str == null || str.length() < rules[0].length()) {
			return new String[0];
		}

		boolean endsWithSeparator = rule.endsWith(SEPARATOR_SINGLE);

		String[] strs = new String[endsWithSeparator ? rules.length : rules.length - 1];
		if (rules[0].length() > 0 && !str.startsWith(rules[0])) {
			return new String[0];
		}

		int startIndex = 0;
		int endIndex = 0;
		for (int i = 0; i < strs.length; i++) {
			startIndex = str.indexOf(rules[i], endIndex) + rules[i].length();
			if (i + 1 == strs.length && endsWithSeparator) {
				endIndex = str.length();
			} else {
				endIndex = str.indexOf(rules[i + 1], startIndex);
			}

			// System.out.println(startIndex + "," + endIndex);

			if (startIndex == -1 || endIndex == -1) {
				return new String[0];
			}
			strs[i] = str.substring(startIndex, endIndex);
		}

		return strs;
	}

	/**
	 * 替换sql like的字段中的通配符，包括[]%_
	 * 
	 * @author Lucifer Wong
	 * @param str
	 *            被替换的字符串
	 * @return 替换后的字符串
	 */
	public static String sqlWildcardFilter(String str) {
		if (ValidatorUtil.isEmpty(str)) {
			return str;
		}

		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < str.length(); i++) {
			char c = str.charAt(i);
			if (c == '[') {
				sb.append("[[]");
			} else if (c == ']') {
				sb.append("[]]");
			} else if (c == '%') {
				sb.append("[%]");
			} else if (c == '_') {
				sb.append("[_]");
			} else {
				sb.append(c);
			}
		}
		return sb.toString();
	}

	/**
	 * 过滤html标签，包括script、style、html、空格、回车标签
	 * 
	 * @author Lucifer Wong
	 *
	 * @param htmlString
	 *            source
	 * @param length
	 *            最大长度
	 * @return 除去html标签后的字符串
	 */
	public static String subHTMLString(String htmlString, int length) {
		return subString(delHTMLTag(htmlString), length);
	}

	/**
	 * 截取字符串，支持中英文混乱，其中中文当做两位处理
	 * 
	 * @author Lucifer Wong
	 *
	 * @param resourceString
	 *            resource
	 * @param length
	 *            最大长度
	 * @return 截取字符串，支持中英文混乱，其中中文当做两位处理
	 */
	public static String subString(String resourceString, int length) {
		String resultString = "";
		if (resourceString == null || "".equals(resourceString) || length < 1) {
			return resourceString;
		}

		if (resourceString.length() < length) {
			return resourceString;
		}

		char[] chr = resourceString.toCharArray();
		int strNum = 0;
		int strGBKNum = 0;
		boolean isHaveDot = false;

		for (int i = 0; i < resourceString.length(); i++) {
			if (chr[i] >= 0xa1) {// 0xa1汉字最小位开始
				strNum = strNum + 2;
				strGBKNum++;
			} else {
				strNum++;
			}

			if (strNum == length || strNum == length + 1) {
				if (i + 1 < resourceString.length()) {
					isHaveDot = true;
				}
				break;
			}
		}
		resultString = resourceString.substring(0, strNum - strGBKNum);
		if (isHaveDot) {
			resultString = resultString + "...";
		}

		return resultString;
	}

	/**
	 * 把字符串按照指定的字符集进行编码
	 * 
	 * <blockquote>
	 * <table width="80%" summary="Description of standard charsets">
	 * <tr>
	 * <th>
	 * <p style="text-align:left">
	 * Charset
	 * 
	 * </th>
	 * <th>
	 * <p style="text-align:left">
	 * Description
	 * 
	 * </th>
	 * </tr>
	 * <tr>
	 * <td valign=top><tt>US-ASCII</tt></td>
	 * <td>Seven-bit ASCII, a.k.a. <tt>ISO646-US</tt>, a.k.a. the Basic Latin
	 * block of the Unicode character set</td>
	 * </tr>
	 * <tr>
	 * <td valign=top><tt>ISO-8859-1&nbsp;&nbsp;</tt></td>
	 * <td>ISO Latin Alphabet No. 1, a.k.a. <tt>ISO-LATIN-1</tt></td>
	 * </tr>
	 * <tr>
	 * <td valign=top><tt>UTF-8</tt></td>
	 * <td>Eight-bit UCS Transformation Format</td>
	 * </tr>
	 * <tr>
	 * <td valign=top><tt>UTF-16BE</tt></td>
	 * <td>Sixteen-bit UCS Transformation Format, big-endian byte&nbsp;order
	 * </td>
	 * </tr>
	 * <tr>
	 * <td valign=top><tt>UTF-16LE</tt></td>
	 * <td>Sixteen-bit UCS Transformation Format, little-endian byte&nbsp;order
	 * </td>
	 * </tr>
	 * <tr>
	 * <td valign=top><tt>UTF-16</tt></td>
	 * <td>Sixteen-bit UCS Transformation Format, byte&nbsp;order identified by
	 * an optional byte-order mark</td>
	 * </tr>
	 * </table>
	 * </blockquote>
	 * 
	 * @author Lucifer Wong
	 * @param str
	 *            source
	 * @param charSetName
	 *            字符编码
	 * @return 把字符串按照指定的字符集进行编码
	 */
	public static String toCharSet(String str, String charSetName) {
		try {
			return new String(str.getBytes(), charSetName);
		} catch (UnsupportedEncodingException e) {
			return str;
		}
	}

	/**
	 * 把一个字节数组转换为16进制表达的字符串
	 * 
	 * @author Lucifer Wong
	 * @param bytes
	 *            source
	 * @return 把一个字节数组转换为16进制表达的字符串
	 */
	public static String toHexString(byte[] bytes) {
		StringBuilder hexString = new StringBuilder();

		for (int i = 0; i < bytes.length; i++) {
			hexString.append(enoughZero(Integer.toHexString(bytes[i] & 0xff), 2));
		}
		return hexString.toString();
	}

	/**
	 * 进行toString操作，若为空，返回默认值
	 * 
	 * @author Lucifer Wong
	 * @param object
	 *            要进行toString操作的对象
	 * @param nullStr
	 *            返回的默认值
	 * @return 进行toString操作，若为空，返回默认值
	 */
	public static String toString(Object object, String nullStr) {
		return object == null ? nullStr : object.toString();
	}

	/**
	 * 清除字符串两边的空格，null不处理
	 * 
	 * @author Lucifer Wong
	 * @param str
	 *            source
	 * @return 清除字符串两边的空格，null不处理
	 */
	public static String trim(String str) {
		return str == null ? str : str.trim();
	}

	/**
	 * 不计算左右空白的字符串长度
	 * 
	 * @author Lucifer Wong
	 * @param s
	 *            source
	 * @return 不计算左右空白的字符串长度
	 */
	public static int trimLength(final String s) {
		return (s == null) ? 0 : s.trim().length();
	}

	/**
	 * 
	 * Removes control characters (char &lt;= 32) from both ends of this String
	 * returning an empty String ("") if the String is empty ("") after the trim
	 * or if it is <code>null</code>.
	 * 
	 * 
	 * @author Lucifer Wong
	 * @param str
	 *            the String to be trimmed, may be null
	 * @return the trimmed String, or an empty String if <code>null</code> input
	 */
	public static String trimToEmpty(String str) {
		return (str == null ? "" : str.trim());
	}

	/**
	 * 清除下划线，把下划线后面字母转换成大写字母
	 * 
	 * @author Lucifer Wong
	 * @param str
	 *            source
	 * @return 清除下划线，把下划线后面字母转换成大写字母
	 */
	public static String underline2Uppercase(String str) {
		if (ValidatorUtil.isEmpty(str)) {
			return str;
		}

		char[] charArray = str.toCharArray();
		for (int i = 0; i < charArray.length; i++) {
			if (charArray[i] == '_' && i < charArray.length - 1) {
				charArray[i + 1] = Character.toUpperCase(charArray[i + 1]);
			}
		}

		return new String(charArray).replaceAll("_", "");
	}

	/**
	 * 将字符转换为编码为Unicode，格式 为'\u0020'<br>
	 * unicodeEscaped(' ') = "\u0020"<br>
	 * unicodeEscaped('A') = "\u0041"
	 * 
	 * @author Lucifer Wong
	 * @param ch
	 *            待转换的char 字符
	 * @return 将字符转换为编码为Unicode
	 */
	public static String unicodeEscaped(char ch) {
		if (ch < 0x10) {
			return "\\u000" + Integer.toHexString(ch);
		} else if (ch < 0x100) {
			return "\\u00" + Integer.toHexString(ch);
		} else if (ch < 0x1000) {
			return "\\u0" + Integer.toHexString(ch);
		}
		return "\\u" + Integer.toHexString(ch);
	}
}