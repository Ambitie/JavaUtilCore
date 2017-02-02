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
package priv.lucife.utils.core.chinese;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import priv.lucife.utils.core.annotation.UBTCompatible;
import priv.lucife.utils.core.datastructure.DoubleArrayTrie;

/**
 * 汉字转拼音类
 *
 * @author Lucifer Wong
 */
@UBTCompatible
public abstract class PinyinUtil {

	/**
	 * 汉字简繁体转换类
	 * 
	 * @author Lucifer Wong
	 *
	 */
	public static class ChineseHelper {

		private static final Map<String, String> CHINESE_MAP = PinyinResource.getChineseResource();
		private static final String CHINESE_REGEX = "[\\u4e00-\\u9fa5]";

		public static void addChineseDict(String path) throws FileNotFoundException {
			CHINESE_MAP.putAll(PinyinResource.getResource(PinyinResource.newFileReader(path)));
		}

		/**
		 * 判断字符串中是否包含中文
		 * 
		 * @param str
		 *            字符串
		 * @return 包含汉字返回true，否则返回false
		 */
		public static boolean containsChinese(String str) {
			for (int i = 0, len = str.length(); i < len; i++) {
				if (isChinese(str.charAt(i))) {
					return true;
				}
			}
			return false;
		}

		/**
		 * 将单个繁体字转换为简体字
		 * 
		 * @param c
		 *            需要转换的繁体字
		 * @return 转换后的简体字
		 */
		public static char convertToSimplifiedChinese(char c) {
			String simplifiedChinese = CHINESE_MAP.get(String.valueOf(c));
			if (simplifiedChinese != null) {
				return simplifiedChinese.charAt(0);
			}
			return c;
		}

		/**
		 * 将繁体字转换为简体字
		 * 
		 * @param str
		 *            需要转换的繁体字
		 * @return 转换后的简体体
		 */
		public static String convertToSimplifiedChinese(String str) {
			StringBuilder sb = new StringBuilder();
			for (int i = 0, len = str.length(); i < len; i++) {
				char c = str.charAt(i);
				sb.append(convertToSimplifiedChinese(c));
			}
			return sb.toString();
		}

		/**
		 * 将单个简体字转换为繁体字
		 * 
		 * @param c
		 *            需要转换的简体字
		 * @return 转换后的繁字体
		 */
		public static char convertToTraditionalChinese(char c) {
			String simplifiedChinese = String.valueOf(c);
			for (Entry<String, String> entry : CHINESE_MAP.entrySet()) {
				if (entry.getValue().equals(simplifiedChinese)) {
					return entry.getKey().charAt(0);
				}
			}

			return c;
		}

		/**
		 * 将简体字转换为繁体字
		 * 
		 * @param str
		 *            需要转换的简体字
		 * @return 转换后的繁字体
		 */
		public static String convertToTraditionalChinese(String str) {
			StringBuilder sb = new StringBuilder();
			for (int i = 0, len = str.length(); i < len; i++) {
				char c = str.charAt(i);
				sb.append(convertToTraditionalChinese(c));
			}
			return sb.toString();
		}

		/**
		 * 判断某个字符是否为汉字
		 * 
		 * @param c
		 *            需要判断的字符
		 * @return 是汉字返回true，否则返回false
		 */
		public static boolean isChinese(char c) {
			return '〇' == c || String.valueOf(c).matches(CHINESE_REGEX);
		}

		/**
		 * 判断某个字符是否为繁体字
		 * 
		 * @param c
		 *            需要判断的字符
		 * @return 是繁体字返回true，否则返回false
		 */
		public static boolean isTraditionalChinese(char c) {
			return CHINESE_MAP.containsKey(String.valueOf(c));
		}

	}

	public enum PinyinFormat {
		WITH_TONE_MARK, WITH_TONE_NUMBER, WITHOUT_TONE;
	}

	private static class PinyinResource {

		protected static Map<String, String> getChineseResource() {
			return getResource(newClassPathReader("/dict/chinese.txt"));
		}

		protected static Map<String, String> getMutilPinyinResource() {
			return getResource(newClassPathReader("/dict/mutil_pinyin.txt"));
		}

		protected static Map<String, String> getPinyinResource() {
			return getResource(newClassPathReader("/dict/pinyin.txt"));
		}

		protected static Map<String, String> getResource(Reader reader) {
			Map<String, String> map = new ConcurrentHashMap<String, String>();
			try {
				BufferedReader br = new BufferedReader(reader);
				String line = null;
				while ((line = br.readLine()) != null) {
					String[] tokens = line.trim().split("=");
					map.put(tokens[0], tokens[1]);
				}
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}

			return map;
		}

		protected static Reader newClassPathReader(String classpath) {
			InputStream is = PinyinResource.class.getResourceAsStream(classpath);
			try {
				return new InputStreamReader(is, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				return null;
			}
		}

		protected static Reader newFileReader(String path) throws FileNotFoundException {
			try {
				return new InputStreamReader(new FileInputStream(path), "UTF-8");
			} catch (UnsupportedEncodingException e) {
				return null;
			}
		}

	}

	private static final String ALL_MARKED_VOWEL = "āáǎàēéěèīíǐìōóǒòūúǔùǖǘǚǜ"; // 所有带声调的拼音字母
	private static final String ALL_UNMARKED_VOWEL = "aeiouv";
	private static final char CHINESE_LING = '〇';
	private static List<String> dict = new ArrayList<String>();
	private static final DoubleArrayTrie DOUBLE_ARRAY_TRIE = new DoubleArrayTrie();
	private static final Map<String, String> MUTIL_PINYIN_TABLE = PinyinResource.getMutilPinyinResource();

	private static final String PINYIN_SEPARATOR = ","; // 拼音分隔符

	private static final Map<String, String> PINYIN_TABLE = PinyinResource.getPinyinResource();

	static {
		for (String word : MUTIL_PINYIN_TABLE.keySet()) {
			dict.add(word);
		}
		Collections.sort(dict);
		DOUBLE_ARRAY_TRIE.build(dict);
	}

	public static void addMutilPinyinDict(String path) throws FileNotFoundException {
		MUTIL_PINYIN_TABLE.putAll(PinyinResource.getResource(PinyinResource.newFileReader(path)));
		dict.clear();
		DOUBLE_ARRAY_TRIE.clear();
		for (String word : MUTIL_PINYIN_TABLE.keySet()) {
			dict.add(word);
		}
		Collections.sort(dict);
		DOUBLE_ARRAY_TRIE.build(dict);
	}

	public static void addPinyinDict(String path) throws FileNotFoundException {
		PINYIN_TABLE.putAll(PinyinResource.getResource(PinyinResource.newFileReader(path)));
	}

	/**
	 * 将单个汉字转换成带声调格式的拼音
	 * 
	 * @param c
	 *            需要转换成拼音的汉字
	 * @return 字符串的拼音
	 * @author Lucifer Wong
	 */
	public static String[] convertToPinyinArray(char c) {
		return convertToPinyinArray(c, PinyinFormat.WITH_TONE_MARK);
	}

	/**
	 * 将单个汉字转换为相应格式的拼音
	 * 
	 * @param c
	 *            需要转换成拼音的汉字
	 * @param pinyinFormat
	 *            拼音格式：WITH_TONE_NUMBER--数字代表声调，WITHOUT_TONE--不带声调，
	 *            WITH_TONE_MARK--带声调
	 * @return 汉字的拼音
	 * @author Lucifer Wong
	 */
	public static String[] convertToPinyinArray(char c, PinyinFormat pinyinFormat) {
		String pinyin = PINYIN_TABLE.get(String.valueOf(c));
		if ((pinyin != null) && (!"null".equals(pinyin))) {
			Set<String> set = new LinkedHashSet<String>();
			for (String str : formatPinyin(pinyin, pinyinFormat)) {
				set.add(str);
			}
			return set.toArray(new String[set.size()]);
		}
		return new String[0];
	}

	/**
	 * 将字符串转换成带声调格式的拼音
	 * 
	 * @param str
	 *            需要转换的字符串
	 * @param separator
	 *            拼音分隔符
	 * @return 转换后带声调的拼音
	 * @author Lucifer Wong
	 * @throws Exception
	 *             转化过程中出现空指针
	 */
	public static String convertToPinyinString(String str, String separator) throws Exception {
		return convertToPinyinString(str, separator, PinyinFormat.WITH_TONE_MARK);
	}

	/**
	 * 将字符串转换成相应格式的拼音
	 * 
	 * @param str
	 *            需要转换的字符串
	 * @param separator
	 *            拼音分隔符
	 * @param pinyinFormat
	 *            拼音格式：WITH_TONE_NUMBER--数字代表声调，WITHOUT_TONE--不带声调，
	 *            WITH_TONE_MARK--带声调
	 * @return 字符串的拼音
	 * @author Lucifer Wong
	 * @throws Exception
	 *             转化过程中出现空指针
	 */
	public static String convertToPinyinString(String str, String separator, PinyinFormat pinyinFormat)
			throws Exception {
		str = ChineseHelper.convertToSimplifiedChinese(str);
		StringBuilder sb = new StringBuilder();
		int i = 0;
		int strLen = str.length();
		while (i < strLen) {
			String substr = str.substring(i);
			List<Integer> commonPrefixList = DOUBLE_ARRAY_TRIE.commonPrefixSearch(substr);
			if (commonPrefixList.size() == 0) {
				char c = str.charAt(i);
				// 判断是否为汉字或者〇
				if (ChineseHelper.isChinese(c) || c == CHINESE_LING) {
					String[] pinyinArray = convertToPinyinArray(c, pinyinFormat);
					if (pinyinArray != null) {
						if (pinyinArray.length > 0) {
							sb.append(pinyinArray[0]);
						} else {
							throw new Exception("Can't convert to pinyin: " + c);
						}
					} else {
						sb.append(str.charAt(i));
					}
				} else {
					sb.append(c);
				}
				i++;
			} else {
				String words = dict.get(commonPrefixList.get(commonPrefixList.size() - 1));
				String[] pinyinArray = formatPinyin(MUTIL_PINYIN_TABLE.get(words), pinyinFormat);
				for (int j = 0, l = pinyinArray.length; j < l; j++) {
					sb.append(pinyinArray[j]);
					if (j < l - 1) {
						sb.append(separator);
					}
				}
				i += words.length();
			}

			if (i < strLen) {
				sb.append(separator);
			}
		}
		return sb.toString();
	}

	/**
	 * 将带声调格式的拼音转换为不带声调格式的拼音
	 * 
	 * @param pinyinArrayString
	 *            带声调格式的拼音
	 * @return 不带声调的拼音
	 * @author Lucifer Wong
	 */
	private static String[] convertWithoutTone(String pinyinArrayString) {
		String[] pinyinArray;
		for (int i = ALL_MARKED_VOWEL.length() - 1; i >= 0; i--) {
			char originalChar = ALL_MARKED_VOWEL.charAt(i);
			char replaceChar = ALL_UNMARKED_VOWEL.charAt((i - i % 4) / 4);
			pinyinArrayString = pinyinArrayString.replace(String.valueOf(originalChar), String.valueOf(replaceChar));
		}
		// 将拼音中的ü替换为v
		pinyinArray = pinyinArrayString.replace("ü", "v").split(PINYIN_SEPARATOR);
		return pinyinArray;
	}

	/**
	 * 将带声调格式的拼音转换为数字代表声调格式的拼音
	 * 
	 * @param pinyinArrayString
	 *            带声调格式的拼音
	 * @return 数字代表声调格式的拼音
	 * @author Lucifer Wong
	 */
	private static String[] convertWithToneNumber(String pinyinArrayString) {
		String[] pinyinArray = pinyinArrayString.split(PINYIN_SEPARATOR);
		for (int i = pinyinArray.length - 1; i >= 0; i--) {
			boolean hasMarkedChar = false;
			String originalPinyin = pinyinArray[i].replace("ü", "v"); // 将拼音中的ü替换为v

			for (int j = originalPinyin.length() - 1; j >= 0; j--) {
				char originalChar = originalPinyin.charAt(j);

				// 搜索带声调的拼音字母，如果存在则替换为对应不带声调的英文字母
				if (originalChar < 'a' || originalChar > 'z') {
					int indexInAllMarked = ALL_MARKED_VOWEL.indexOf(originalChar);
					int toneNumber = indexInAllMarked % 4 + 1; // 声调数
					char replaceChar = ALL_UNMARKED_VOWEL.charAt((indexInAllMarked - indexInAllMarked % 4) / 4);
					pinyinArray[i] = originalPinyin.replace(String.valueOf(originalChar), String.valueOf(replaceChar))
							+ toneNumber;
					hasMarkedChar = true;
					break;
				}
			}
			if (!hasMarkedChar) {
				// 找不到带声调的拼音字母说明是轻声，用数字5表示
				pinyinArray[i] = originalPinyin + "5";
			}
		}

		return pinyinArray;
	}

	/**
	 * 将带声调的拼音格式化为相应格式的拼音
	 * 
	 * @param pinyinString
	 *            带声调的拼音
	 * @param pinyinFormat
	 *            拼音格式：WITH_TONE_NUMBER--数字代表声调，WITHOUT_TONE--不带声调，
	 *            WITH_TONE_MARK--带声调
	 * @return 格式转换后的拼音
	 * @author Lucifer Wong
	 */
	private static String[] formatPinyin(String pinyinString, PinyinFormat pinyinFormat) {
		if (pinyinFormat == PinyinFormat.WITH_TONE_MARK) {
			return pinyinString.split(PINYIN_SEPARATOR);
		} else if (pinyinFormat == PinyinFormat.WITH_TONE_NUMBER) {
			return convertWithToneNumber(pinyinString);
		} else if (pinyinFormat == PinyinFormat.WITHOUT_TONE) {
			return convertWithoutTone(pinyinString);
		}
		return new String[0];
	}

	/**
	 * 获取字符串对应拼音的首字母
	 * 
	 * @param str
	 *            需要转换的字符串
	 * @return 对应拼音的首字母
	 * @author Lucifer Wong
	 * @throws Exception
	 *             转化过程中出现空指针
	 */
	public static String getShortPinyin(String str) throws Exception {
		String separator = "#"; // 使用#作为拼音分隔符
		StringBuilder sb = new StringBuilder();

		char[] charArray = new char[str.length()];
		for (int i = 0, len = str.length(); i < len; i++) {
			char c = str.charAt(i);

			// 首先判断是否为汉字或者〇，不是的话直接将该字符返回
			if (!ChineseHelper.isChinese(c) && c != CHINESE_LING) {
				charArray[i] = c;
			} else {
				int j = i + 1;
				sb.append(c);

				// 搜索连续的汉字字符串
				while (j < len && (ChineseHelper.isChinese(str.charAt(j)) || str.charAt(j) == CHINESE_LING)) {
					sb.append(str.charAt(j));
					j++;
				}
				String hanziPinyin = convertToPinyinString(sb.toString(), separator, PinyinFormat.WITHOUT_TONE);
				String[] pinyinArray = hanziPinyin.split(separator);
				for (String string : pinyinArray) {
					charArray[i] = string.charAt(0);
					i++;
				}
				i--;
				sb.setLength(0);
			}
		}
		return String.valueOf(charArray);
	}

	/**
	 * 判断一个汉字是否为多音字
	 * 
	 * @param c
	 *            汉字
	 * @return 判断结果，是汉字返回true，否则返回false
	 * @author Lucifer Wong
	 */
	public static boolean hasMultiPinyin(char c) {
		String[] pinyinArray = convertToPinyinArray(c);
		if (pinyinArray != null && pinyinArray.length > 1) {
			return true;
		}
		return false;
	}
}
