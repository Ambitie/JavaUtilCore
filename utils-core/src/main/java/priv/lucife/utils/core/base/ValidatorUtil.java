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

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import priv.lucife.utils.core.annotation.UBTCompatible;
import priv.lucife.utils.core.date.DateUtil;

/**
 * 对字符串按照常用规则进行验证的工具类.
 * 
 * @author Lucifer Wong
 */
@UBTCompatible
public abstract class ValidatorUtil {

	/**
	 * 简体中文的正则表达式。
	 */
	private static final String REGEX_SIMPLE_CHINESE = "^[\u4E00-\u9FA5]+$";

	/**
	 * 字母数字的正则表达式。
	 */
	private static final String REGEX_ALPHANUMERIC = "[a-zA-Z0-9]+";

	/**
	 * 移动手机号码的正则表达式。
	 */
	private static final String REGEX_CHINA_MOBILE = "1(3[4-9]|4[7]|5[012789]|8[278])\\d{8}";

	/**
	 * 联通手机号码的正则表达式。
	 */
	private static final String REGEX_CHINA_UNICOM = "1(3[0-2]|5[56]|8[56])\\d{8}";

	/**
	 * 电信手机号码的正则表达式。
	 */
	private static final String REGEX_CHINA_TELECOM = "(?!00|015|013)(0\\d{9,11})|(1(33|53|80|89)\\d{8})";

	/**
	 * 整数或浮点数的正则表达式。
	 */
	private static final String REGEX_NUMERIC = "(\\+|-){0,1}(\\d+)([.]?)(\\d*)";

	/**
	 * 电子邮箱的正则表达式。
	 */
	private static final String REGEX_EMAIL = "^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$";

	/**
	 * 电话号码的正则表达式。
	 */
	private static final String REGEX_PHONE_NUMBER = "(([\\(（]\\d+[\\)）])?|(\\d+[-－]?)*)\\d+";

	/**
	 * 判断字符串是否符合正则表达式
	 * 
	 * @author Lucifer Wong
	 *
	 * @param str
	 *            source
	 * @param regex
	 *            真正表达
	 * @return true：符合， false：不符合
	 */
	public static boolean march(String str, String regex) {
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(str);
		boolean b = m.find();
		return b;
	}

	/**
	 * 判断一个对象是不是一个集合
	 * 
	 * @author Lucifer Wong
	 * @param o
	 *            被判断的对象
	 * @return true：是集合， false：不是集合
	 */
	public static boolean isContainerType(Object o) {
		return o instanceof Collection || o instanceof Map;
	}

	/**
	 * 判断字符串是否只包含字母和数字.
	 * 
	 * @author Lucifer Wong
	 * @param str
	 *            字符串
	 * @return 如果字符串只包含字母和数字, 则返回 <code>true</code>, 否则返回 <code>false</code>.
	 */
	public static boolean isAlphanumeric(String str) {
		return isRegexMatch(str, REGEX_ALPHANUMERIC);
	}

	/**
	 * <p>
	 * Checks if a String is whitespace, empty ("") or null.
	 * </p>
	 * 
	 * @author Lucifer Wong
	 * 
	 *         <pre>
	 *   StringUtils.isBlank(null)                = true
	 *   StringUtils.isBlank(&quot;&quot;)        = true
	 *   StringUtils.isBlank(&quot; &quot;)       = true
	 *   StringUtils.isBlank(&quot;bob&quot;)     = false
	 *   StringUtils.isBlank(&quot;  bob  &quot;) = false
	 *         </pre>
	 * 
	 * @param str
	 *            the String to check, may be null
	 * @return <code>true</code> if the String is null, empty or whitespace
	 */
	public static boolean isBlank(String str) {
		int strLen;
		if (str == null || (strLen = str.length()) == 0) {
			return true;
		}
		for (int i = 0; i < strLen; i++) {
			if ((Character.isWhitespace(str.charAt(i)) == false)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 是否为中国移动手机号码。
	 * 
	 * @author Lucifer Wong
	 * @param str
	 *            字符串
	 * @return 如果是移动号码，返回 <code>true</code>，否则返回 <code>false</code>。
	 */
	public static boolean isChinaMobile(String str) {
		return isRegexMatch(str, REGEX_CHINA_MOBILE);
	}

	/**
	 * 是否为中国联通手机号码。
	 * 
	 * @author Lucifer Wong
	 * @param str
	 *            字符串
	 * @return 如果是联通号码，返回 <code>true</code>，否则返回 <code>false</code>。
	 */
	public static boolean isChinaUnicom(String str) {
		return isRegexMatch(str, REGEX_CHINA_UNICOM);
	}

	/**
	 * 判断是否为电信手机。
	 * 
	 * @author Lucifer Wong
	 * @param str
	 *            字符串
	 * @return 如果是电信号码，返回 <code>true</code>，否则返回 <code>false</code>。
	 */
	public static boolean isChinaTelecom(String str) {
		return isRegexMatch(str, REGEX_CHINA_TELECOM);
	}

	/**
	 * 判断是否为小灵通手机(Personal Access Phone System, PAS)。
	 * 
	 * @author Lucifer Wong
	 * @param str
	 *            字符串
	 * @return 如果是小灵通号码，返回 <code>true</code>，否则返回 <code>false</code>。
	 * @deprecated 已经被 {@link #isChinaTelecom(String)} 取代
	 */
	@Deprecated
	public static boolean isChinaPAS(String str) {
		return isChinaTelecom(str);
	}

	/**
	 * 是否是合法的日期字符串
	 * 
	 * @author Lucifer Wong
	 * @param str
	 *            日期字符串
	 * @return 是true，否则false
	 */
	public static boolean isDate(String str) {
		if (isEmpty(str) || str.length() > 10) {
			return false;
		}

		String[] items = str.split("-");

		if (items.length != 3) {
			return false;
		}

		if (!isNumber(items[0], 1900, 9999) || !isNumber(items[1], 1, 12)) {
			return false;
		}

		int year = Integer.parseInt(items[0]);
		int month = Integer.parseInt(items[1]);

		return isNumber(items[2], 1, DateUtil.getMaxDayOfMonth(year, month - 1));
	}

	/**
	 * 是否是合法的日期时间字符串
	 * 
	 * @author Lucifer Wong
	 * @param str
	 *            日期时间字符串
	 * @return 是true，否则false
	 */
	public static boolean isDateTime(String str) {
		if (isEmpty(str) || str.length() > 20) {
			return false;
		}

		String[] items = str.split(" ");

		if (items.length != 2) {
			return false;
		}

		return isDate(items[0]) && isTime(items[1]);
	}

	/**
	 * 判断字符串是否是合法的电子邮箱地址.
	 * 
	 * @author Lucifer Wong
	 * @param str
	 *            字符串
	 * @return 是true，否则false
	 */
	public static boolean isEmail(String str) {
		return isRegexMatch(str, REGEX_EMAIL);
	}

	/**
	 * 当数组为<code>null</code>, 或者长度为0, 或者长度为1且元素的值为<code>null</code>时返回
	 * <code>true</code>.
	 * 
	 * @author Lucifer Wong
	 * @param args
	 *            source
	 * @return 是true，否则false
	 */
	public static boolean isEmpty(Object... args) {
		return args == null || args.length == 0 || (args.length == 1 && args[0] == null);
	}

	/**
	 * 字符串是否为Empty，null和空格都算是Empty
	 * 
	 * @author Lucifer Wong
	 * @param str
	 *            字符串
	 * @return 是true，否则false
	 */
	public static boolean isEmpty(String str) {
		return str == null || str.trim().length() == 0;
	}

	/**
	 * 判断集合是否为空。
	 * 
	 * @author Lucifer Wong
	 * @param <T>
	 *            集合泛型
	 * @param collection
	 *            集合对象
	 * @return 当集合对象为 <code>null</code> 或者长度为零时返回 <code>true</code>，否则返回
	 *         <code>false</code>。
	 */
	public static <T> boolean isEmpty(Collection<T> collection) {
		return collection == null || collection.isEmpty();
	}

	/**
	 * 是否为手机号码, 包括移动, 联通, 小灵通等手机号码.
	 * 
	 * @author Lucifer Wong
	 * @param str
	 *            字符串
	 * @return 若是合法的手机号码返回 <code>true</code>, 否则返回 <code>false</code>.
	 */
	public static boolean isMobile(String str) {
		return isChinaMobile(str) || isChinaUnicom(str) || isChinaPAS(str);
	}

	/**
	 * 是否为数字的字符串。
	 * 
	 * @author Lucifer Wong
	 * @param str
	 *            字符串
	 * @return 是true，否则false
	 */
	public static boolean isNumber(String str) {
		if (isEmpty(str)) {
			return false;
		}

		for (int i = 0; i < str.length(); i++) {
			if (str.charAt(i) > '9' || str.charAt(i) < '0') {
				return false;
			}
		}
		return true;
	}

	/**
	 * 是否是固定范围内的数字的字符串
	 * 
	 * @author Lucifer Wong
	 * @param str
	 *            目标字符串
	 * @param min
	 *            最小值
	 * @param max
	 *            最大值
	 * @return 是true，否则false
	 */
	public static boolean isNumber(String str, int min, int max) {
		if (!isNumber(str)) {
			return false;
		}

		int number = Integer.parseInt(str);
		return number >= min && number <= max;
	}

	/**
	 * 判断字符是否为整数或浮点数. <br>
	 * 
	 * @author Lucifer Wong
	 * @param str
	 *            字符串
	 * @return 若为整数或浮点数则返回 <code>true</code>, 否则返回 <code>false</code>
	 */
	public static boolean isNumeric(String str) {
		return isRegexMatch(str, REGEX_NUMERIC);
	}

	/**
	 * 判断字符是否为符合精度要求的整数或浮点数。
	 * 
	 * @author Lucifer Wong
	 * @param str
	 *            字符串
	 * @param fractionNum
	 *            小数部分的最多允许的位数
	 * @return 若为整数或浮点数则返回 <code>true</code>, 否则返回 <code>false</code>
	 */
	public static boolean isNumeric(String str, int fractionNum) {
		if (isEmpty(str)) {
			return false;
		}

		// 整数或浮点数
		String regex = "(\\+|-){0,1}(\\d+)([.]?)(\\d{0," + fractionNum + "})";
		return Pattern.matches(regex, str);
	}

	/**
	 * @author Lucifer Wong
	 * 
	 *         <p>
	 *         Validating for phone number.
	 *         </p>
	 * 
	 *         e.g.
	 *         <ul>
	 *         <li>78674585 --&gt; valid</li>
	 *         <li>6872-4585 --&gt; valid</li>
	 *         <li>(6872)4585 --&gt; valid</li>
	 *         <li>0086-10-6872-4585 --&gt; valid</li>
	 *         <li>0086-(10)-6872-4585 --&gt; invalid</li>
	 *         <li>0086(10)68724585 --&gt; invalid</li>
	 *         </ul>
	 * @param str
	 *            string to be validated
	 * @return If the str is valid phone number return <code>true</code>,
	 *         otherwise return <code>false</code>.
	 */
	public static boolean isPhoneNumber(String str) {
		// Regex for checking phone number
		return isRegexMatch(str, REGEX_PHONE_NUMBER);
	}

	/**
	 * 判断是否是合法的邮编
	 * 
	 * @author Lucifer Wong
	 * @param str
	 *            字符串
	 * @return 是true，否则false
	 */
	public static boolean isPostcode(String str) {
		if (isEmpty(str)) {
			return false;
		}

		if (str.length() != 6 || !ValidatorUtil.isNumber(str)) {
			return false;
		}

		return true;
	}

	/**
	 * 判断是否是固定长度范围内的字符串
	 * 
	 * @author Lucifer Wong
	 * @param str
	 *            source
	 * @param minLength
	 *            最小长度
	 * @param maxLength
	 *            最大长度
	 * @return 是true，否则false
	 */
	public static boolean isString(String str, int minLength, int maxLength) {
		if (str == null) {
			return false;
		}

		if (minLength < 0) {
			return str.length() <= maxLength;
		} else if (maxLength < 0) {
			return str.length() >= minLength;
		} else {
			return str.length() >= minLength && str.length() <= maxLength;
		}
	}

	/**
	 * 判断是否是合法的时间字符串。
	 * 
	 * @author Lucifer Wong
	 * @param str
	 *            字符串
	 * @return 是true，否则false
	 */
	public static boolean isTime(String str) {
		if (isEmpty(str) || str.length() > 8) {
			return false;
		}

		String[] items = str.split(":");

		if (items.length != 2 && items.length != 3) {
			return false;
		}

		for (int i = 0; i < items.length; i++) {
			if (items[i].length() != 2 && items[i].length() != 1) {
				return false;
			}
		}

		return !(!isNumber(items[0], 0, 23) || !isNumber(items[1], 0, 59)
				|| (items.length == 3 && !isNumber(items[2], 0, 59)));
	}

	/**
	 * 是否是简体中文字符串。
	 * 
	 * @author Lucifer Wong
	 * @param str
	 *            字符串
	 * @return 是true，否则false
	 */
	public static boolean isSimpleChinese(String str) {
		return isRegexMatch(str, REGEX_SIMPLE_CHINESE);
	}

	/**
	 * 判断字符串是否匹配了正则表达式。
	 * 
	 * @author Lucifer Wong
	 * @param str
	 *            字符串
	 * @param regex
	 *            正则表达式
	 * @return 是true，否则false
	 */
	public static boolean isRegexMatch(String str, String regex) {
		return str != null && str.matches(regex);
	}

	/**
	 * 判断输入的字符串是否为纯汉字
	 * 
	 * @author Lucifer Wong
	 * @param value
	 *            传入的字符串
	 * @return 是true，否则false
	 */
	public static boolean isChinese(String value) {
		Pattern pattern = Pattern.compile("[\u0391-\uFFE5]+$");
		return pattern.matcher(value).matches();
	}

	/**
	 * 判断是否为浮点数，包括double和float
	 * 
	 * @author Lucifer Wong
	 * @param value
	 *            传入的字符串
	 * @return 是true，否则false
	 */
	public static boolean isDouble(String value) {
		Pattern pattern = Pattern.compile("^[-\\+]?\\d+\\.\\d+$");
		return pattern.matcher(value).matches();
	}

	/**
	 * 判断是否为整数
	 * 
	 * @author Lucifer Wong
	 * @param value
	 *            传入的字符串
	 * @return 是true，否则false
	 */
	public static boolean isInteger(String value) {
		Pattern pattern = Pattern.compile("^[-\\+]?[\\d]+$");
		return pattern.matcher(value).matches();
	}

	/**
	 * --15位身份证号码：第7、8位为出生年份(两位数)，第9、10位为出生月份，第11、12位代表出生日期，第15位代表性别，奇数为男，偶数为女。
	 * --18位身份证号码
	 * ：第7、8、9、10位为出生年份(四位数)，第11、第12位为出生月份，第13、14位代表出生日期，第17位代表性别，奇数为男，偶数为女。
	 * 
	 * @author Lucifer Wong
	 *
	 */
	@UBTCompatible
	public abstract static class IdcardValidator {

		/**
		 * 省，直辖市代码表： { 11:"北京",12:"天津",13:"河北",14:"山西",15:"内蒙古",
		 * 21:"辽宁",22:"吉林",23:"黑龙江",31:"上海",32:"江苏",
		 * 33:"浙江",34:"安徽",35:"福建",36:"江西",37:"山东",41:"河南",
		 * 42:"湖北",43:"湖南",44:"广东",45:"广西",46:"海南",50:"重庆",
		 * 51:"四川",52:"贵州",53:"云南",54:"西藏",61:"陕西",62:"甘肃",
		 * 63:"青海",64:"宁夏",65:"新疆",71:"台湾",81:"香港",82:"澳门",91:"国外"}
		 */

		// 每位加权因子
		private static final int power[] = { 7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2 };

		/**
		 * 
		 * <p>
		 * 判断18位身份证的合法性
		 * </p>
		 * 根据〖中华人民共和国国家标准GB11643-1999〗中有关公民身份号码的规定，公民身份号码是特征组合码，
		 * 由十七位数字本体码和一位数字校验码组成。 排列顺序从左至右依次为：六位数字地址码，八位数字出生日期码，三位数字顺序码和一位数字校验码。
		 * <p>
		 * 顺序码: 表示在同一地址码所标识的区域范围内，对同年、同月、同 日出生的人编定的顺序号，顺序码的奇数分配给男性，偶数分配 给女性。
		 * </p>
		 * <p>
		 * 1.前1、2位数字表示：所在省份的代码； 2.第3、4位数字表示：所在城市的代码； 3.第5、6位数字表示：所在区县的代码；
		 * 4.第7~14位数字表示：出生年、月、日； 5.第15、16位数字表示：所在地的派出所的代码；
		 * 6.第17位数字表示性别：奇数表示男性，偶数表示女性；
		 * 7.第18位数字是校检码：也有的说是个人信息码，一般是随计算机的随机产生，用来检验身份证的正确性。校检码可以是0~9的数字，
		 * 有时也用x表示。
		 * </p>
		 * <p>
		 * 第十八位数字(校验码)的计算方法为： 1.将前面的身份证号码17位数分别乘以不同的系数。从第一位到第十七位的系数分别为：7 9 10 5
		 * 8 4 2 1 6 3 7 9 10 5 8 4 2
		 * </p>
		 * <p>
		 * 2.将这17位数字和系数相乘的结果相加。
		 * </p>
		 * <p>
		 * 3.用加出来和除以11，看余数是多少？
		 * </p>
		 * 4.余数只可能有0 1 2 3 4 5 6 7 8 9 10这11个数字。其分别对应的最后一位身份证的号码为1 0 X 9 8 7 6 5
		 * 4 3 2。
		 * <p>
		 * 5.通过上面得知如果余数是2，就会在身份证的第18位数字上出现罗马数字的Ⅹ。如果余数是10，身份证的最后一位号码就是2。
		 * </p>
		 * 
		 * @author Lucifer Wong
		 * @param idcard
		 *            待验证的身份证
		 * @return 是true，否则false
		 */
		public static boolean isValidate18Idcard(String idcard) {
			// 非18位为假
			if (idcard == null || idcard.length() != 18) {
				return false;
			}
			// 获取前17位
			String idcard17 = idcard.substring(0, 17);
			// 获取第18位
			String idcard18Code = idcard.substring(17, 18);
			char c[] = null;
			String checkCode = "";
			// 是否都为数字
			if (StringUtil.isDigital(idcard17)) {
				c = idcard17.toCharArray();
			} else {
				return false;
			}

			if (null != c) {
				int bit[] = new int[idcard17.length()];
				bit = ConvertUtil.converChars2Int(c, null);
				int sum17 = 0;
				sum17 = getPowerSum(bit);
				// 将和值与11取模得到余数进行校验码判断
				checkCode = getCheckCodeBySum(sum17);
				if (null == checkCode) {
					return false;
				}
				// 将身份证的第18位与算出来的校码进行匹配，不相等就为假
				if (!idcard18Code.equalsIgnoreCase(checkCode)) {
					return false;
				}
			}

			return true;
		}

		/**
		 * 18位身份证号码的基本数字和位数验校
		 * 
		 * @author Lucifer Wong
		 *
		 * @param idcard
		 *            待验证的身份证
		 * @return 是true，否则false
		 */
		public static boolean is18Idcard(String idcard) {
			return Pattern.matches("^[1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}([\\d|x|X]{1})$",
					idcard);
		}

		/**
		 * 将身份证的每位和对应位的加权因子相乘之后，再得到和值
		 * 
		 * @author Lucifer Wong
		 * @param bit
		 * @return 将身份证的每位和对应位的加权因子相乘之后，再得到和值
		 */
		private static int getPowerSum(int[] bit) {
			int sum = 0;
			if (power.length != bit.length) {
				return sum;
			}

			for (int i = 0; i < bit.length; i++) {
				for (int j = 0; j < power.length; j++) {
					if (i == j) {
						sum = sum + bit[i] * power[j];
					}
				}
			}

			return sum;
		}

		/**
		 * 检验对象是否为空,String 中只有空格在对象中也算空.
		 * 
		 * @param <T>
		 *            typeValue
		 * @param <V>
		 *            typeValue
		 * @param <K>
		 *            typeValue
		 * @author Lucifer Wong
		 * @param object
		 *            待检测的对象
		 * @return 为空返回true,否则false.
		 */
		@SuppressWarnings("unchecked")
		public static <T, V, K> boolean isEmpty(Object object) {
			if (null == object)
				return true;
			else if (object instanceof String)
				return "".equals(object.toString().trim());
			else if (object instanceof Iterable)
				return !((Iterable<T>) object).iterator().hasNext();
			else if (object.getClass().isArray())
				return Array.getLength(object) == 0;
			else if (object instanceof Map)
				return ((Map<K, V>) object).size() == 0;
			else if (Number.class.isAssignableFrom(object.getClass()))
				return false;
			else if (Date.class.isAssignableFrom(object.getClass()))
				return false;
			else
				return false;
		}

		/**
		 * 将和值与11取模得到余数进行校验码判断
		 * 
		 * @author Lucifer Wong
		 * @param sum17
		 * @return 将和值与11取模得到余数进行校验码判断
		 */
		private static String getCheckCodeBySum(int sum17) {
			String checkCode = null;
			switch (sum17 % 11) {
			case 10:
				checkCode = "2";
				break;
			case 9:
				checkCode = "3";
				break;
			case 8:
				checkCode = "4";
				break;
			case 7:
				checkCode = "5";
				break;
			case 6:
				checkCode = "6";
				break;
			case 5:
				checkCode = "7";
				break;
			case 4:
				checkCode = "8";
				break;
			case 3:
				checkCode = "9";
				break;
			case 2:
				checkCode = "x";
				break;
			case 1:
				checkCode = "0";
				break;
			case 0:
				checkCode = "1";
				break;
			}
			return checkCode;
		}

		/**
		 * 身份证信息中代表性别的数值
		 * 
		 * @author Lucifer Wong
		 * @param idno
		 *            身份证信息中代表性别的数值
		 * @return 身份证信息中代表性别的数值
		 */
		public static int getUserSex(String idno) {
			String sex = "1";
			if (idno != null) {
				if (idno.length() > 15) {
					sex = idno.substring(16, 17);
				}
			}

			return Integer.parseInt(sex) % 2 == 0 ? 0 : 1;
		}
	}
}
