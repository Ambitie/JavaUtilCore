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

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import priv.lucife.utils.core.annotation.UBTCompatible;
import priv.lucife.utils.core.date.DateUtil;

/**
 * 转换工具类<br>
 * 若待转换值为null或者出现异常，则使用默认值
 *
 * @author Lucifer Wong
 */
@UBTCompatible
public abstract class ConvertUtil {

	private static final Byte BYTE_ZERO = (byte) 0;
	private static final Byte BYTE_ONE = (byte) 1;
	private static final Short SHORT_ZERO = (short) 0;
	private static final Short SHORT_ONE = (short) 1;
	private static final Integer INTEGER_ZERO = 0;
	private static final Integer INTEGER_ONE = 1;
	private static final Long LONG_ZERO = 0L;
	private static final Long LONG_ONE = 1L;
	private static final Float FLOAT_ZERO = 0.0f;
	private static final Float FLOAT_ONE = 1.0f;
	private static final Double DOUBLE_ZERO = 0.0d;
	private static final Double DOUBLE_ONE = 1.0d; // matters

	/**
	 * 常用的工具转化类
	 * 
	 * @author Lucifer Wong
	 * @param fromInstance
	 *            要被转化的对象
	 * @param toType
	 *            包括
	 *            String、Long(long)、Integer(int)、Boolean(boolean)、Double(double)
	 *            、Byte(byte)、Float(float)、Short(short)、Date、BigDecimal、
	 *            BigInteger、
	 *            java.sql.Date、Timestamp、AtomicInteger、AtomicLong、AtomicBoolean
	 *            等
	 * @return 转化成功则为常用的目标值，失败的话转化为null
	 */
	public static Object convert(Object fromInstance, @SuppressWarnings("rawtypes") Class toType) {
		if (toType == null) {
			return null;
		}

		if (toType == String.class) {
			if (fromInstance == null || fromInstance instanceof String) {
				return fromInstance;
			} else if (fromInstance instanceof BigDecimal) {
				return ((BigDecimal) fromInstance).stripTrailingZeros().toPlainString();
			} else if (fromInstance instanceof Number || fromInstance instanceof Boolean
					|| fromInstance instanceof AtomicBoolean) {
				return fromInstance.toString();
			} else if (fromInstance instanceof Date) {
				return DateUtil._getDateFormat("yyyy-MM-dd'T'HH:mm:ss").format(fromInstance);
			} else if (fromInstance instanceof Calendar) {
				return DateUtil._getDateFormat("yyyy-MM-dd'T'HH:mm:ss").format(((Calendar) fromInstance).getTime());
			} else if (fromInstance instanceof Character) {
				return "" + fromInstance;
			}
			return null;
		} else if (toType == long.class) {
			return fromInstance == null ? 0L : convertLong(fromInstance);
		} else if (toType == Long.class) {
			return fromInstance == null ? null : convertLong(fromInstance);
		} else if (toType == int.class) {
			return fromInstance == null ? 0 : convertInteger(fromInstance);
		} else if (toType == Integer.class) {
			return fromInstance == null ? null : convertInteger(fromInstance);
		} else if (toType == Date.class) {
			if (fromInstance == null) {
				return null;
			}
			try {
				if (fromInstance instanceof String) {
					return convertStr2Date(((String) fromInstance).trim(), null);
				} else if (fromInstance instanceof java.sql.Date) { // convert
																	// from
																	// java.sql.Date
																	// to
																	// java.util.Date
					return new Date(((java.sql.Date) fromInstance).getTime());
				} else if (fromInstance instanceof Timestamp) {
					Timestamp timestamp = (Timestamp) fromInstance;
					return new Date(timestamp.getTime());
				} else if (fromInstance instanceof Date) { // Return a clone,
															// not the same
															// instance because
															// Dates are not
															// immutable
					return new Date(((Date) fromInstance).getTime());
				} else if (fromInstance instanceof Calendar) {
					return ((Calendar) fromInstance).getTime();
				} else if (fromInstance instanceof Long) {
					return new Date((Long) fromInstance);
				} else if (fromInstance instanceof AtomicLong) {
					return new Date(((AtomicLong) fromInstance).get());
				}
			} catch (Exception e) {
				return null;
			}
			return null;
		} else if (toType == BigDecimal.class) {
			if (fromInstance == null) {
				return null;
			}

			try {
				if (fromInstance instanceof String) {
					if (ValidatorUtil.isEmpty((String) fromInstance)) {
						return BigDecimal.ZERO;
					}

					return new BigDecimal(((String) fromInstance).trim());
				} else if (fromInstance instanceof BigDecimal) {
					return fromInstance;
				} else if (fromInstance instanceof BigInteger) {
					return new BigDecimal((BigInteger) fromInstance);
				} else if (fromInstance instanceof Number) {
					return new BigDecimal(((Number) fromInstance).doubleValue());
				} else if (fromInstance instanceof Boolean) {
					return (Boolean) fromInstance ? BigDecimal.ONE : BigDecimal.ZERO;
				} else if (fromInstance instanceof AtomicBoolean) {
					return ((AtomicBoolean) fromInstance).get() ? BigDecimal.ONE : BigDecimal.ZERO;
				} else if (fromInstance instanceof Date) {
					return new BigDecimal(((Date) fromInstance).getTime());
				} else if (fromInstance instanceof Calendar) {
					return new BigDecimal(((Calendar) fromInstance).getTime().getTime());
				}
			} catch (Exception e) {
				return null;
			}
			return null;
		} else if (toType == BigInteger.class) {
			if (fromInstance == null) {
				return null;
			}
			try {
				if (fromInstance instanceof String) {
					if (ValidatorUtil.isEmpty((String) fromInstance)) {
						return BigInteger.ZERO;
					}
					return new BigInteger(((String) fromInstance).trim());
				} else if (fromInstance instanceof BigInteger) {
					return fromInstance;
				} else if (fromInstance instanceof BigDecimal) {
					return ((BigDecimal) fromInstance).toBigInteger();
				} else if (fromInstance instanceof Number) {
					return new BigInteger(Long.toString(((Number) fromInstance).longValue()));
				} else if (fromInstance instanceof Boolean) {
					return (Boolean) fromInstance ? BigInteger.ONE : BigInteger.ZERO;
				} else if (fromInstance instanceof AtomicBoolean) {
					return ((AtomicBoolean) fromInstance).get() ? BigInteger.ONE : BigInteger.ZERO;
				} else if (fromInstance instanceof Date) {
					return new BigInteger(Long.toString(((Date) fromInstance).getTime()));
				} else if (fromInstance instanceof Calendar) {
					return new BigInteger(Long.toString(((Calendar) fromInstance).getTime().getTime()));
				}
			} catch (Exception e) {
				return null;
			}
			return null;
		} else if (toType == java.sql.Date.class) {
			if (fromInstance == null) {
				return null;
			}
			try {
				if (fromInstance instanceof java.sql.Date) { // Return a clone
																// of the
																// current date
																// time because
																// java.sql.Date
																// is mutable.
					return new java.sql.Date(((java.sql.Date) fromInstance).getTime());
				} else if (fromInstance instanceof Timestamp) {
					Timestamp timestamp = (Timestamp) fromInstance;
					return new java.sql.Date(timestamp.getTime());
				} else if (fromInstance instanceof Date) { // convert from
															// java.util.Date to
															// java.sql.Date
					return new java.sql.Date(((Date) fromInstance).getTime());
				} else if (fromInstance instanceof String) {
					Date date = convertStr2Date(((String) fromInstance).trim(), null);
					return new java.sql.Date(date.getTime());
				} else if (fromInstance instanceof Calendar) {
					return new java.sql.Date(((Calendar) fromInstance).getTime().getTime());
				} else if (fromInstance instanceof Long) {
					return new java.sql.Date((Long) fromInstance);
				} else if (fromInstance instanceof AtomicLong) {
					return new java.sql.Date(((AtomicLong) fromInstance).get());
				}
			} catch (Exception e) {
				return null;
			}
			return null;
		} else if (toType == Timestamp.class) {
			if (fromInstance == null) {
				return null;
			}
			try {
				if (fromInstance instanceof java.sql.Date) { // convert from
																// java.sql.Date
																// to
																// java.util.Date
					return new Timestamp(((java.sql.Date) fromInstance).getTime());
				} else if (fromInstance instanceof Timestamp) { // return a
																// clone of the
																// Timestamp
																// because it is
																// mutable
					return new Timestamp(((Timestamp) fromInstance).getTime());
				} else if (fromInstance instanceof Date) {
					return new Timestamp(((Date) fromInstance).getTime());
				} else if (fromInstance instanceof String) {
					Date date = convertStr2Date(((String) fromInstance).trim(), null);
					return new Timestamp(date.getTime());
				} else if (fromInstance instanceof Calendar) {
					return new Timestamp(((Calendar) fromInstance).getTime().getTime());
				} else if (fromInstance instanceof Long) {
					return new Timestamp((Long) fromInstance);
				} else if (fromInstance instanceof AtomicLong) {
					return new Timestamp(((AtomicLong) fromInstance).get());
				}
			} catch (Exception e) {
				return null;
			}
			return null;
		} else if (toType == AtomicInteger.class) {
			if (fromInstance == null) {
				return null;
			}
			try {
				if (fromInstance instanceof AtomicInteger) { // return a new
																// instance
																// because
																// AtomicInteger
																// is mutable
					return new AtomicInteger(((AtomicInteger) fromInstance).get());
				} else if (fromInstance instanceof String) {
					if (ValidatorUtil.isEmpty((String) fromInstance)) {
						return new AtomicInteger(0);
					}
					return new AtomicInteger(Integer.valueOf(((String) fromInstance).trim()));
				} else if (fromInstance instanceof Number) {
					return new AtomicInteger(((Number) fromInstance).intValue());
				} else if (fromInstance instanceof Boolean) {
					return (Boolean) fromInstance ? new AtomicInteger(1) : new AtomicInteger(0);
				} else if (fromInstance instanceof AtomicBoolean) {
					return ((AtomicBoolean) fromInstance).get() ? new AtomicInteger(1) : new AtomicInteger(0);
				}
			} catch (Exception e) {
				return null;
			}
			return null;
		} else if (toType == AtomicLong.class) {
			if (fromInstance == null) {
				return null;
			}
			try {
				if (fromInstance instanceof String) {
					if (ValidatorUtil.isEmpty((String) fromInstance)) {
						return new AtomicLong(0);
					}
					return new AtomicLong(Long.valueOf(((String) fromInstance).trim()));
				} else if (fromInstance instanceof AtomicLong) { // return a
																	// clone of
																	// the
																	// AtomicLong
																	// because
																	// it is
																	// mutable
					return new AtomicLong(((AtomicLong) fromInstance).get());
				} else if (fromInstance instanceof Number) {
					return new AtomicLong(((Number) fromInstance).longValue());
				} else if (fromInstance instanceof Date) {
					return new AtomicLong(((Date) fromInstance).getTime());
				} else if (fromInstance instanceof Boolean) {
					return (Boolean) fromInstance ? new AtomicLong(1L) : new AtomicLong(0L);
				} else if (fromInstance instanceof AtomicBoolean) {
					return ((AtomicBoolean) fromInstance).get() ? new AtomicLong(1L) : new AtomicLong(0L);
				} else if (fromInstance instanceof Calendar) {
					return new AtomicLong(((Calendar) fromInstance).getTime().getTime());
				}
			} catch (Exception e) {
				return null;
			}
			return null;
		} else if (toType == AtomicBoolean.class) {
			if (fromInstance == null) {
				return null;
			} else if (fromInstance instanceof String) {
				if (ValidatorUtil.isEmpty((String) fromInstance)) {
					return new AtomicBoolean(false);
				}
				String value = (String) fromInstance;
				return new AtomicBoolean("true".equalsIgnoreCase(value));
			} else if (fromInstance instanceof AtomicBoolean) { // return a
																// clone of the
																// AtomicBoolean
																// because it is
																// mutable
				return new AtomicBoolean(((AtomicBoolean) fromInstance).get());
			} else if (fromInstance instanceof Boolean) {
				return new AtomicBoolean((Boolean) fromInstance);
			} else if (fromInstance instanceof Number) {
				return new AtomicBoolean(((Number) fromInstance).longValue() != 0);
			}
			return null;

		} else if (toType == boolean.class) {
			return fromInstance == null ? Boolean.FALSE : convertBoolean(fromInstance);
		} else if (toType == Boolean.class) {
			return fromInstance == null ? null : convertBoolean(fromInstance);
		} else if (toType == double.class) {
			return fromInstance == null ? DOUBLE_ZERO : convertDouble(fromInstance);
		} else if (toType == Double.class) {
			return fromInstance == null ? null : convertDouble(fromInstance);
		} else if (toType == byte.class) {
			return fromInstance == null ? BYTE_ZERO : convertByte(fromInstance);
		} else if (toType == Byte.class) {
			return fromInstance == null ? null : convertByte(fromInstance);
		} else if (toType == float.class) {
			return fromInstance == null ? FLOAT_ZERO : convertFloat(fromInstance);
		} else if (toType == Float.class) {
			return fromInstance == null ? null : convertFloat(fromInstance);
		} else if (toType == short.class) {
			return fromInstance == null ? SHORT_ZERO : convertShort(fromInstance);
		} else if (toType == Short.class) {
			return fromInstance == null ? null : convertShort(fromInstance);
		}
		return null;
	}

	/**
	 * 转化为Byte类型
	 * 
	 * @author Lucifer Wong
	 * @param fromInstance
	 *            要被转化的对象
	 * @return 转化成功则为目标值，失败的话转化为0
	 */
	public static Object convertByte(Object fromInstance) {
		try {
			if (fromInstance instanceof String) {
				if (ValidatorUtil.isEmpty((String) fromInstance)) {
					return BYTE_ZERO;
				}
				return Byte.valueOf(((String) fromInstance).trim());
			} else if (fromInstance instanceof Byte) {
				return fromInstance;
			} else if (fromInstance instanceof Number) {
				return ((Number) fromInstance).byteValue();
			} else if (fromInstance instanceof Boolean) {
				return (Boolean) fromInstance ? BYTE_ONE : BYTE_ZERO;
			} else if (fromInstance instanceof AtomicBoolean) {
				return ((AtomicBoolean) fromInstance).get() ? BYTE_ONE : BYTE_ZERO;
			}
		} catch (Exception e) {
			return null;
		}
		return null;
	}

	/**
	 * 转化成short类型
	 * 
	 * @author Lucifer Wong
	 * @param fromInstance
	 *            要被转化的对象
	 * @return 转化成功则为目标值，失败的话转化为0
	 */
	public static Object convertShort(Object fromInstance) {
		try {
			if (fromInstance instanceof String) {
				if (ValidatorUtil.isEmpty((String) fromInstance)) {
					return SHORT_ZERO;
				}
				return Short.valueOf(((String) fromInstance).trim());
			} else if (fromInstance instanceof Short) {
				return fromInstance;
			} else if (fromInstance instanceof Number) {
				return ((Number) fromInstance).shortValue();
			} else if (fromInstance instanceof Boolean) {
				return (Boolean) fromInstance ? SHORT_ONE : SHORT_ZERO;
			} else if (fromInstance instanceof AtomicBoolean) {
				return ((AtomicBoolean) fromInstance).get() ? SHORT_ONE : SHORT_ZERO;
			}
		} catch (Exception e) {
			return null;
		}
		return null;
	}

	/**
	 * 转化成整型
	 * 
	 * @author Lucifer Wong
	 * @param fromInstance
	 *            要被转化的对象
	 * @return 转化成功则为目标值，失败的话转化为0
	 */
	public static Object convertInteger(Object fromInstance) {
		try {
			if (fromInstance instanceof Integer) {
				return fromInstance;
			} else if (fromInstance instanceof Number) {
				return ((Number) fromInstance).intValue();
			} else if (fromInstance instanceof String) {
				if (ValidatorUtil.isEmpty((String) fromInstance)) {
					return INTEGER_ZERO;
				}
				return Integer.valueOf(((String) fromInstance).trim());
			} else if (fromInstance instanceof Boolean) {
				return (Boolean) fromInstance ? INTEGER_ONE : INTEGER_ZERO;
			} else if (fromInstance instanceof AtomicBoolean) {
				return ((AtomicBoolean) fromInstance).get() ? INTEGER_ONE : INTEGER_ZERO;
			}
		} catch (Exception e) {
			return null;
		}
		return null;
	}

	/**
	 * 转化成长整形
	 * 
	 * @author Lucifer Wong
	 * @param fromInstance
	 *            要被转化的对象
	 * @return 转化成功则为目标值，失败的话转化为0
	 */
	public static Object convertLong(Object fromInstance) {
		try {
			if (fromInstance instanceof Long) {
				return fromInstance;
			} else if (fromInstance instanceof Number) {
				return ((Number) fromInstance).longValue();
			} else if (fromInstance instanceof String) {
				if (ValidatorUtil.isEmpty((String) fromInstance)) {
					return LONG_ZERO;
				}
				return Long.valueOf(((String) fromInstance).trim());
			} else if (fromInstance instanceof Date) {
				return ((Date) fromInstance).getTime();
			} else if (fromInstance instanceof Boolean) {
				return (Boolean) fromInstance ? LONG_ONE : LONG_ZERO;
			} else if (fromInstance instanceof AtomicBoolean) {
				return ((AtomicBoolean) fromInstance).get() ? LONG_ONE : LONG_ZERO;
			} else if (fromInstance instanceof Calendar) {
				return ((Calendar) fromInstance).getTime().getTime();
			}
		} catch (Exception e) {
			return null;
		}
		return null;
	}

	/**
	 * 转化成浮点型
	 * 
	 * @author Lucifer Wong
	 * @param fromInstance
	 *            要被转化的对象
	 * @return 转化成功则为目标值，失败的话转化为0
	 */
	public static Object convertFloat(Object fromInstance) {
		try {
			if (fromInstance instanceof String) {
				if (ValidatorUtil.isEmpty((String) fromInstance)) {
					return FLOAT_ZERO;
				}
				return Float.valueOf(((String) fromInstance).trim());
			} else if (fromInstance instanceof Float) {
				return fromInstance;
			} else if (fromInstance instanceof Number) {
				return ((Number) fromInstance).floatValue();
			} else if (fromInstance instanceof Boolean) {
				return (Boolean) fromInstance ? FLOAT_ONE : FLOAT_ZERO;
			} else if (fromInstance instanceof AtomicBoolean) {
				return ((AtomicBoolean) fromInstance).get() ? FLOAT_ONE : FLOAT_ZERO;
			}
		} catch (Exception e) {
			return null;
		}
		return null;
	}

	/**
	 * 转化成双精度浮点型
	 * 
	 * @author Lucifer Wong
	 * @param fromInstance
	 *            要被转化的对象
	 * @return 转化成功则为目标值，失败的话转化为0
	 */
	public static Object convertDouble(Object fromInstance) {
		try {
			if (fromInstance instanceof String) {
				if (ValidatorUtil.isEmpty((String) fromInstance)) {
					return DOUBLE_ZERO;
				}
				return Double.valueOf(((String) fromInstance).trim());
			} else if (fromInstance instanceof Double) {
				return fromInstance;
			} else if (fromInstance instanceof Number) {
				return ((Number) fromInstance).doubleValue();
			} else if (fromInstance instanceof Boolean) {
				return (Boolean) fromInstance ? DOUBLE_ONE : DOUBLE_ZERO;
			} else if (fromInstance instanceof AtomicBoolean) {
				return ((AtomicBoolean) fromInstance).get() ? DOUBLE_ONE : DOUBLE_ZERO;
			}
		} catch (Exception e) {
			return null;
		}
		return null;
	}

	/**
	 * 转化成布尔型
	 * 
	 * @author Lucifer Wong
	 * @param fromInstance
	 *            要被转化的对象
	 * @return 转化成功则为目标值，失败的话转化为false
	 */
	public static Object convertBoolean(Object fromInstance) {
		if (fromInstance instanceof Boolean) {
			return fromInstance;
		} else if (fromInstance instanceof Number) {
			return ((Number) fromInstance).longValue() != 0;
		} else if (fromInstance instanceof String) {
			if (ValidatorUtil.isEmpty((String) fromInstance)) {
				return Boolean.FALSE;
			}
			String value = (String) fromInstance;
			return "true".equalsIgnoreCase(value) ? Boolean.TRUE : Boolean.FALSE;
		} else if (fromInstance instanceof AtomicBoolean) {
			return ((AtomicBoolean) fromInstance).get();
		}
		return null;
	}

	/**
	 * 字符串转换日期
	 * 
	 * @author Lucifer Wong
	 *
	 * @param str
	 *            待转换的字符串
	 * @param defaultValue
	 *            默认日期
	 * @return yyyy-MM-dd HH:mm:ss
	 */
	public static java.util.Date convertStr2Date(String str, java.util.Date defaultValue) {
		return convertStr2Date(str, "yyyy-MM-dd HH:mm:ss", defaultValue);
	}

	/**
	 * 字符串转换为指定格式的日期
	 *
	 * @author Lucifer Wong
	 * @param str
	 *            待转换的字符串
	 * @param format
	 *            日期格式
	 * @param defaultValue
	 *            默认日期
	 * @return 字符串转换为指定格式的日期
	 */
	public static java.util.Date convertStr2Date(String str, String format, java.util.Date defaultValue) {
		SimpleDateFormat fmt = new SimpleDateFormat(format);
		try {
			defaultValue = fmt.parse(str);
		} catch (Exception e) {
		}
		return defaultValue;
	}

	/**
	 * 日期转换为字符串
	 *
	 * @author Lucifer Wong
	 *
	 * @param date
	 *            待转换的日期
	 * @param defaultValue
	 *            默认字符串
	 * @return 日期转换为字符串 yyyy-MM-dd HH:mm:ss
	 */
	public static String convertDate2Str(java.util.Date date, String defaultValue) {
		return convertDate2Str(date, "yyyy-MM-dd HH:mm:ss", defaultValue);
	}

	/**
	 * 日期转换为指定格式的字符串
	 * 
	 * @author Lucifer Wong
	 *
	 * @param date
	 *            待转换的日期
	 * @param format
	 *            指定格式
	 * @param defaultValue
	 *            默认值
	 * @return 日期转换为指定格式的字符串
	 */
	public static String convertDate2Str(java.util.Date date, String format, String defaultValue) {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		try {
			defaultValue = sdf.format(date);
		} catch (Exception e) {
		}
		return defaultValue;
	}

	/**
	 * 如果字符串为空则使用默认字符串
	 * 
	 * @author Lucifer Wong
	 *
	 * @param str
	 *            字符串
	 * @param defaultValue
	 *            默认值
	 * @return 如果字符串为空则使用默认字符串
	 */
	public static String convertStr2Str(String str, String defaultValue) {
		if (ValidatorUtil.isEmpty(str))
			defaultValue = str;
		return str;
	}

	/**
	 * util date 转换为 sqldate
	 * 
	 * @author Lucifer Wong
	 * @param date
	 *            待转化的date
	 * @return util date 转换为 sqldate
	 */
	public static java.sql.Date convertDate2SqlDate(java.util.Date date) {
		if (ValidatorUtil.isEmpty(new Object[] { date }))
			return null;
		return new java.sql.Date(date.getTime());
	}

	/**
	 * sql date 转换为 util date
	 * 
	 * @author Lucifer Wong
	 * @param date
	 *            待转化的date
	 * @return sql date 转换为 util date
	 */
	public static java.util.Date convertSqlDate2Date(java.sql.Date date) {
		if (ValidatorUtil.isEmpty(new Object[] { date }))
			return null;
		return new java.util.Date(date.getTime());
	}

	/**
	 * date 转换为 timestamp
	 *
	 * @author Lucifer Wong
	 * @param date
	 *            待转化的date
	 * @return date 转换为 timestamp
	 */
	public static Timestamp convertDate2SqlTimestamp(java.util.Date date) {
		if (ValidatorUtil.isEmpty(new Object[] { date }))
			return null;
		return new Timestamp(date.getTime());
	}

	/**
	 * timestamp 转换为date
	 *
	 * @author Lucifer Wong
	 * @param date
	 *            待转化的date
	 * @return timestamp 转换为date
	 */
	public static java.util.Date qlTimestamp2Date(Timestamp date) {
		if (ValidatorUtil.isEmpty(new Object[] { date }))
			return null;
		return new java.util.Date(date.getTime());
	}

	/**
	 * Bean转换为Map
	 *
	 * @param object
	 *            待转化的object
	 * @return String-Object的HashMap
	 *
	 * @author Lucifer Wong
	 */
	public static Map<String, Object> bean2MapObject(Object object) {
		if (object == null) {
			return null;
		}

		Map<String, Object> map = new HashMap<String, Object>();
		try {
			BeanInfo beanInfo = Introspector.getBeanInfo(object.getClass());
			PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
			for (PropertyDescriptor property : propertyDescriptors) {
				String key = property.getName();
				// 过滤class属性
				if (!key.equals("class")) {
					// 得到property对应的getter方法
					Method getter = property.getReadMethod();
					Object value = getter.invoke(object);
					map.put(key, value);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return map;
	}

	/**
	 * Map转换为Java Bean
	 *
	 * @param map
	 *            待转换的Map
	 * @param object
	 *            Java Bean
	 * @return java.lang.Object
	 * @author Lucifer Wong
	 * @param <K>
	 *            typeValue
	 * @param <V>
	 *            typeValue
	 * @throws Exception
	 *             转化失败
	 */
	public static <K, V> Object map2Bean(Map<K, V> map, Object object) throws Exception {
		if (map == null || object == null) {
			return null;
		}
		BeanInfo beanInfo = Introspector.getBeanInfo(object.getClass());
		PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();

		for (PropertyDescriptor property : propertyDescriptors) {
			String key = property.getName();
			if (map.containsKey(key)) {
				Object value = map.get(key);
				// 得到property对应的setter方法
				Method setter = property.getWriteMethod();
				setter.invoke(object, value);
			}
		}

		return object;
	}

	/**
	 * 将字符串数组转化成整型数组 String.valueOf(c)
	 * 
	 * @author Lucifer Wong
	 * @param c
	 *            待转化的数组
	 * @param object
	 *            转化失败的返回值
	 * @return 将字符串数组转化成整型数组
	 */
	public static int[] converChars2Int(char[] c, int[] object) {

		try {
			int[] a = new int[c.length];
			int k = 0;
			for (char temp : c) {
				a[k++] = Integer.parseInt(String.valueOf(temp));
			}
			return a;
		} catch (Exception e) {
			return object;
		}

	}

}
