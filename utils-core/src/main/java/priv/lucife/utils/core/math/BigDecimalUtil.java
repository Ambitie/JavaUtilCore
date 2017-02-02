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

import java.math.BigDecimal;

import priv.lucife.utils.core.annotation.UBTCompatible;
import priv.lucife.utils.core.base.ValidatorUtil;

/**
 * 提供精确的加减乘除运算
 * 
 * @author Lucifer Wong
 */
@UBTCompatible
public abstract class BigDecimalUtil {

	/**
	 * 默认四舍五入规则为：向上舍入
	 */
	private static int DEFAULT_ROUND = BigDecimal.ROUND_HALF_UP;

	/**
	 * 默认保留位：2
	 */
	private static int DEFAULT_SCALE = 2;

	/**
	 * 
	 * 加法运算
	 * 
	 * @author Lucifer Wong
	 * @param v1
	 *            加数1
	 * @param v2
	 *            加数2
	 * @return 结果
	 */
	public static String add(String v1, String v2) {
		BigDecimal b1 = new BigDecimal(v1);
		BigDecimal b2 = new BigDecimal(v2);
		return b1.add(b2).toString();
	}

	/**
	 * 将BigDecimal 转换成integer
	 * 
	 * @author Lucifer Wong
	 * @param value
	 *            source
	 * @return 转化的结果
	 */
	public static Integer bigDecimalToInteger(BigDecimal value) {
		if (value != null) {
			return new Integer(value.intValue());
		}
		return null;
	}

	/**
	 * 将BigDecimal 转换成Long
	 * 
	 * @author Lucifer Wong
	 * @param value
	 *            source
	 * @return 转化的结果
	 */
	public static Long bigDecimalToLong(BigDecimal value) {
		if (value != null) {
			return new Long(value.longValue());
		}
		return null;
	}

	/**
	 * 比较两个数<br>
	 * v1 &gt; v2 return 1<br>
	 * v1 = v2 return 0<br>
	 * v1 &lt; v2 return -1 <br>
	 * Compares this {@code BigDecimal} with the specified {@code BigDecimal}.
	 * Two {@code BigDecimal} objects that are equal in value but have a
	 * different scale (like 2.0 and 2.00) are considered equal by this method.
	 * This method is provided in preference to individual methods for each of
	 * the six boolean comparison operators ({@literal <}, ==, {@literal >},
	 * {@literal >=}, !=, {@literal <=}). The suggested idiom for performing
	 * these comparisons is: {@code (x.compareTo(y)} &lt; <i>op</i>&gt;
	 * {@code 0)}, where &lt;<i>op</i>&gt; is one of the six comparison
	 * operators.
	 * 
	 * @author Lucifer Wong
	 * @param v1
	 *            比较数
	 * @param v2
	 *            被比较数
	 * @return 比较结果的结果
	 */
	public static int compareTo(String v1, String v2) {
		BigDecimal b1 = new BigDecimal(v1);
		BigDecimal b2 = new BigDecimal(v2);
		return b1.compareTo(b2);
	}

	/**
	 * 除法运算<br>
	 * 当发生除不尽的情况时，由scale参数指定精度，以后的数字四舍五入。
	 * 
	 * @author Lucifer Wong
	 * @param v1
	 *            除数
	 * @param v2
	 *            被除数
	 * @param scale
	 *            精确精度
	 * @param round
	 *            近似规则
	 * @return 计算结果
	 */
	public static String div(String v1, String v2, int scale, int round) {
		if (scale < 0) {
			throw new IllegalArgumentException("The scale must be a positive integer or zero");
		}

		if (ValidatorUtil.isEmpty(new Object[] { scale })) {
			scale = DEFAULT_SCALE;
		}

		if (ValidatorUtil.isEmpty(new Object[] { round })) {
			round = DEFAULT_ROUND;
		}

		BigDecimal b1 = new BigDecimal(v1);
		BigDecimal b2 = new BigDecimal(v2);
		return b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP).toString();
	}

	/**
	 * 将object转换为Bigdecimal
	 * 
	 * @author Lucifer Wong
	 * @param value
	 *            待转换的数值
	 * @return 转化结果
	 */
	public static BigDecimal getBigDecimal(Object value) {

		BigDecimal resultValue = new BigDecimal(0);
		if (value instanceof String) {
			resultValue = new BigDecimal((String) value);
		} else if (value instanceof Integer) {
			resultValue = new BigDecimal((Integer) value);
		} else if (value instanceof Long) {
			resultValue = new BigDecimal((Long) value);
		} else if (value instanceof Double) {
			resultValue = new BigDecimal((Double) value);
		} else {
			resultValue = (BigDecimal) value;
		}

		return resultValue;
	}

	/**
	 * 将object转换为Bigdecimal,若object为空，则返回resultValue
	 * 
	 * @author Lucifer Wong
	 * @param value
	 *            待转换的数值
	 * @param resultValue
	 *            内定值
	 * @return 转化结果
	 */
	public static BigDecimal getBigDecimal(Object value, BigDecimal resultValue) {
		if (ValidatorUtil.isEmpty(new Object[] { value })) {
			return resultValue;
		}

		resultValue = getBigDecimal(resultValue);

		return resultValue;
	}

	/**
	 * 处理BigDecimal数据，保留scale位小数
	 * 
	 * @author Lucifer Wong
	 * @param value
	 *            待转换的数值
	 * @param scale
	 *            保留scale位小数
	 * @return 转化结果
	 */
	public static BigDecimal getValue(BigDecimal value, int scale) {
		if (!ValidatorUtil.isEmpty(new Object[] { value })) {
			return value.setScale(scale, BigDecimal.ROUND_HALF_UP);
		}
		return value;
	}

	/**
	 * 返回较大数
	 * 
	 * @author Lucifer Wong
	 * @param v1
	 *            v1
	 * @param v2
	 *            v2
	 * @return 返回较大数
	 */
	public static String returnMax(String v1, String v2) {
		BigDecimal b1 = new BigDecimal(v1);
		BigDecimal b2 = new BigDecimal(v2);
		return b1.max(b2).toString();
	}

	/**
	 * 返回较小数
	 * 
	 * @author Lucifer Wong
	 * @param v1
	 *            v1
	 * @param v2
	 *            v2
	 * @return 返回较小数
	 */
	public static String returnMin(String v1, String v2) {
		BigDecimal b1 = new BigDecimal(v1);
		BigDecimal b2 = new BigDecimal(v2);
		return b1.min(b2).toString();
	}
}
