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
package priv.lucife.utils.core.date;

import java.text.DateFormat;
import java.text.FieldPosition;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TimeZone;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import priv.lucife.utils.core.annotation.UBTCompatible;
import priv.lucife.utils.core.base.StringUtil;
import priv.lucife.utils.core.base.ValidatorUtil;

/**
 * 时间处理工具类 <blockquote>
 * <table border=0 cellspacing=3 cellpadding=0 summary=
 * "Examples of date and time patterns interpreted in the U.S. locale">
 * <tr style="background-color:#ccccff">
 * <th align=left>Date and Time Pattern
 * <th align=left>Result
 * <tr>
 * <td><code>"yyyy.MM.dd G 'at' HH:mm:ss z"</code>
 * <td><code>2001.07.04 AD at 12:08:56 PDT</code>
 * <tr style="background-color:#eeeeff">
 * <td><code>"EEE, MMM d, ''yy"</code>
 * <td><code>Wed, Jul 4, '01</code>
 * <tr>
 * <td><code>"h:mm a"</code>
 * <td><code>12:08 PM</code>
 * <tr style="background-color:#eeeeff">
 * <td><code>"hh 'o''clock' a, zzzz"</code>
 * <td><code>12 o'clock PM, Pacific Daylight Time</code>
 * <tr>
 * <td><code>"K:mm a, z"</code>
 * <td><code>0:08 PM, PDT</code>
 * <tr style="background-color:#eeeeff">
 * <td><code>"yyyyy.MMMMM.dd GGG hh:mm aaa"</code>
 * <td><code>02001.July.04 AD 12:08 PM</code>
 * <tr>
 * <td><code>"EEE, d MMM yyyy HH:mm:ss Z"</code>
 * <td><code>Wed, 4 Jul 2001 12:08:56 -0700</code>
 * <tr style="background-color:#eeeeff">
 * <td><code>"yyMMddHHmmssZ"</code>
 * <td><code>010704120856-0700</code>
 * <tr>
 * <td><code>"yyyy-MM-dd'T'HH:mm:ss.SSSZ"</code>
 * <td><code>2001-07-04T12:08:56.235-0700</code>
 * <tr style="background-color:#eeeeff">
 * <td><code>"yyyy-MM-dd'T'HH:mm:ss.SSSXXX"</code>
 * <td><code>2001-07-04T12:08:56.235-07:00</code>
 * <tr>
 * <td><code>"YYYY-'W'ww-u"</code>
 * <td><code>2001-W27-3</code>
 * </table>
 * </blockquote> * <blockquote>
 * <table border=0 cellspacing=3 cellpadding=0 summary=
 * "Chart shows pattern letters, date/time component, presentation, and examples."
 * >
 * <tr style="background-color:#ccccff">
 * <th align=left>Letter
 * <th align=left>Date or Time Component
 * <th align=left>Presentation
 * <th align=left>Examples
 * <tr>
 * <td><code>G</code>
 * <td>Era designator
 * <td>Text
 * <td><code>AD</code>
 * <tr style="background-color:#eeeeff">
 * <td><code>y</code>
 * <td>Year
 * <td>Year
 * <td><code>1996</code>; <code>96</code>
 * <tr>
 * <td><code>Y</code>
 * <td>Week year
 * <td>Year
 * <td><code>2009</code>; <code>09</code>
 * <tr style="background-color:#eeeeff">
 * <td><code>M</code>
 * <td>Month in year
 * <td>Month
 * <td><code>July</code>; <code>Jul</code>; <code>07</code>
 * <tr>
 * <td><code>w</code>
 * <td>Week in year
 * <td>Number
 * <td><code>27</code>
 * <tr style="background-color:#eeeeff">
 * <td><code>W</code>
 * <td>Week in month
 * <td>Number
 * <td><code>2</code>
 * <tr>
 * <td><code>D</code>
 * <td>Day in year
 * <td>Number
 * <td><code>189</code>
 * <tr style="background-color:#eeeeff">
 * <td><code>d</code>
 * <td>Day in month
 * <td>Number
 * <td><code>10</code>
 * <tr>
 * <td><code>F</code>
 * <td>Day of week in month
 * <td>Number
 * <td><code>2</code>
 * <tr style="background-color:#eeeeff">
 * <td><code>E</code>
 * <td>Day name in week
 * <td>Text
 * <td><code>Tuesday</code>; <code>Tue</code>
 * <tr>
 * <td><code>u</code>
 * <td>Day number of week (1 = Monday, ..., 7 = Sunday)
 * <td>Number
 * <td><code>1</code>
 * <tr style="background-color:#eeeeff">
 * <td><code>a</code>
 * <td>Am/pm marker
 * <td>Text
 * <td><code>PM</code>
 * <tr>
 * <td><code>H</code>
 * <td>Hour in day (0-23)
 * <td>Number
 * <td><code>0</code>
 * <tr style="background-color:#eeeeff">
 * <td><code>k</code>
 * <td>Hour in day (1-24)
 * <td>Number
 * <td><code>24</code>
 * <tr>
 * <td><code>K</code>
 * <td>Hour in am/pm (0-11)
 * <td>Number
 * <td><code>0</code>
 * <tr style="background-color:#eeeeff">
 * <td><code>h</code>
 * <td>Hour in am/pm (1-12)
 * <td>Number
 * <td><code>12</code>
 * <tr>
 * <td><code>m</code>
 * <td>Minute in hour
 * <td>Number
 * <td><code>30</code>
 * <tr style="background-color:#eeeeff">
 * <td><code>s</code>
 * <td>Second in minute
 * <td>Number
 * <td><code>55</code>
 * <tr>
 * <td><code>S</code>
 * <td>Millisecond
 * <td>Number
 * <td><code>978</code>
 * <tr style="background-color:#eeeeff">
 * <td><code>z</code>
 * <td>Time zone
 * <td>General time zone
 * <td><code>Pacific Standard Time</code>; <code>PST</code>;
 * <code>GMT-08:00</code>
 * <tr>
 * <td><code>Z</code>
 * <td>Time zone
 * <td>RFC 822 time zone
 * <td><code>-0800</code>
 * <tr style="background-color:#eeeeff">
 * <td><code>X</code>
 * <td>Time zone
 * <td>ISO 8601 time zone
 * <td><code>-08</code>; <code>-0800</code>; <code>-08:00</code>
 * </table>
 * </blockquote>
 * 
 * @author Lucifer Wong
 */
@UBTCompatible
public abstract class DateUtil {

	/**
	 * 线程安全的时间Format
	 */
	private static final ThreadLocal<Map<String, SimpleDateFormat>> _dateFormats = new ThreadLocal<Map<String, SimpleDateFormat>>() {
		public Map<String, SimpleDateFormat> initialValue() {
			return new ConcurrentHashMap<>();
		}
	};

	private static final String ADAY = "(monday|mon|tuesday|tues|tue|wednesday|wed|thursday|thur|thu|friday|fri|saturday|sat|sunday|sun)";

	private static final String AMONTH = "(Jan|January|Feb|February|Mar|March|Apr|April|May|Jun|June|Jul|July|Aug|August|Sep|Sept|September|Oct|October|Nov|November|Dec|December)";

	public static final String DATE_DAY = "dd";

	public static final String DATE_FORMAT1 = "yyyy-MM-dd";

	public static final String DATE_FORMAT2 = "yyyy-MM-dd HH:mm:ss";

	public static final String DATE_HOUR = "HH";

	public static final String DATE_MINUTE = "mm";

	public static final String DATE_MONTH = "MM";

	public static final String DATE_NOFUll_FORMAT = "yyyyMMdd";

	public static final String DATE_SECONDES = "ss";

	public static final String DATE_YEAR = "yyyy";
	/**
	 * "(\\d{4})[./-](\\d{1,2})[./-](\\d{1,2})"
	 */
	private static final Pattern DATEPATTERN1 = Pattern.compile("(\\d{4})[./-](\\d{1,2})[./-](\\d{1,2})");
	/**
	 * "(\\d{1,2})[./-](\\d{1,2})[./-](\\d{4})"
	 */
	private static final Pattern DATEPATTERN2 = Pattern.compile("(\\d{1,2})[./-](\\d{1,2})[./-](\\d{4})");
	/**
	 * {@value #AMONTH} +
	 * "[ ]*[,]?[ ]*(\\d{1,2})(st|nd|rd|th|)[ ]*[,]?[ ]*(\\d{4})"
	 */
	private static final Pattern DATEPATTERN3 = Pattern
			.compile(AMONTH + "[ ]*[,]?[ ]*(\\d{1,2})(st|nd|rd|th|)[ ]*[,]?[ ]*(\\d{4})", Pattern.CASE_INSENSITIVE);
	/**
	 * "(\\d{1,2})(st|nd|rd|th|)[ ]*[,]?[ ]*" + {@value #AMONTH} +
	 * "[ ]*[,]?[ ]*(\\d{4})",
	 */
	private static final Pattern DATEPATTERN4 = Pattern.compile(
			"(\\d{1,2})(st|nd|rd|th|)[ ]*[,]?[ ]*" + AMONTH + "[ ]*[,]?[ ]*(\\d{4})", Pattern.CASE_INSENSITIVE);
	/**
	 * "(\\d{4})[ ]*[,]?[ ]*"+ {@value #AMONTH} +
	 * "[ ]*[,]?[ ]*(\\d{1,2})(st|nd|rd|th|)"
	 */
	private static final Pattern DATEPATTERN5 = Pattern.compile(
			"(\\d{4})[ ]*[,]?[ ]*" + AMONTH + "[ ]*[,]?[ ]*(\\d{1,2})(st|nd|rd|th|)", Pattern.CASE_INSENSITIVE);
	/**
	 * 
	 * {@value #ADAY} + "[ ]+" + {@value #AMONTH} +
	 * "[ ]+(\\d{1,2})[ ]+(\\d{2}:\\d{2}:\\d{2})[ ]+[A-Z]{1,3}\\s+(\\d{4})"
	 */
	private static final Pattern DATEPATTERN6 = Pattern.compile(
			ADAY + "[ ]+" + AMONTH + "[ ]+(\\d{1,2})[ ]+(\\d{2}:\\d{2}:\\d{2})[ ]+[A-Z]{1,3}\\s+(\\d{4})",
			Pattern.CASE_INSENSITIVE);

	private static final int[] DAY_OF_MONTH = new int[] { 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };

	private static final Pattern DAYPATTERN = Pattern.compile(ADAY, Pattern.CASE_INSENSITIVE);

	private static final Map<String, String> MONTHS = new LinkedHashMap<>();

	public static final String TIME_FORMAT_SSS = "yyyy-MM-dd HH:mm:ss|SSS";

	public static final String TIME_NOFUll_FORMAT = "yyyyMMddHHmmss";

	private static final Pattern TIMEPATTERN1 = Pattern
			.compile("(\\d{2})[:.](\\d{2})[:.](\\d{2})[.](\\d{1,10})([+-]\\d{2}[:]?\\d{2}|Z)?");

	private static final Pattern TIMEPATTERN2 = Pattern
			.compile("(\\d{2})[:.](\\d{2})[:.](\\d{2})([+-]\\d{2}[:]?\\d{2}|Z)?");

	private static final Pattern TIMEPATTERN3 = Pattern.compile("(\\d{2})[:.](\\d{2})([+-]\\d{2}[:]?\\d{2}|Z)?");

	private static final String[] WEEKS = { "星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六" };

	static {
		// Month name to number map
		MONTHS.put("jan", "1");
		MONTHS.put("january", "1");
		MONTHS.put("feb", "2");
		MONTHS.put("february", "2");
		MONTHS.put("mar", "3");
		MONTHS.put("march", "3");
		MONTHS.put("apr", "4");
		MONTHS.put("april", "4");
		MONTHS.put("may", "5");
		MONTHS.put("jun", "6");
		MONTHS.put("june", "6");
		MONTHS.put("jul", "7");
		MONTHS.put("july", "7");
		MONTHS.put("aug", "8");
		MONTHS.put("august", "8");
		MONTHS.put("sep", "9");
		MONTHS.put("sept", "9");
		MONTHS.put("september", "9");
		MONTHS.put("oct", "10");
		MONTHS.put("october", "10");
		MONTHS.put("nov", "11");
		MONTHS.put("november", "11");
		MONTHS.put("dec", "12");
		MONTHS.put("december", "12");
	}

	/**
	 * @param obj
	 *            must be a Number or a Date.
	 * @param _format
	 *            指定目标格式
	 * @param toAppendTo
	 *            the string buffer for the returning time string.
	 * @param pos
	 *            keeps track of the position of the field within the returned
	 *            string. On input: an alignment field, if desired. On output:
	 *            the offsets of the alignment field. For example, given a time
	 *            text "1996.07.10 AD at 15:08:56 PDT", if the given
	 *            fieldPosition is DateFormat.YEAR_FIELD, the begin index and
	 *            end index of fieldPosition will be set to 0 and 4,
	 *            respectively. Notice that if the same time field appears more
	 *            than once in a pattern, the fieldPosition will be set for the
	 *            first occurrence of that time field. For instance, formatting
	 *            a Date to the time string "1 PM PDT (Pacific Daylight Time)"
	 *            using the pattern "h a z (zzzz)" and the alignment field
	 *            DateFormat.TIMEZONE_FIELD, the begin index and end index of
	 *            fieldPosition will be set to 5 and 8, respectively, for the
	 *            first occurrence of the timezone pattern character 'z'.
	 * @return the string buffer passed in as toAppendTo, with formatted text
	 *         appended.
	 */
	public static StringBuffer _format(Object obj, String _format, StringBuffer toAppendTo, FieldPosition pos) {
		return _getDateFormat(_format).format(obj, toAppendTo, pos);
	}

	/**
	 * 线程安全的时间Format<br>
	 * 得到其SimpleDateFormat
	 * 
	 * @author Lucifer Wong
	 * @param format
	 *            指定目标格式
	 * @return 线程安全的 SimpleDateFormat
	 */
	public static SimpleDateFormat _getDateFormat(String format) {
		Map<String, SimpleDateFormat> formatters = _dateFormats.get();
		SimpleDateFormat formatter = formatters.get(format);
		if (formatter == null) {
			formatter = new SimpleDateFormat(format);
			formatters.put(format, formatter);
		}
		return formatter;
	}

	/**
	 * 线程安全的时间Format<br>
	 * 
	 * @author Lucifer Wong
	 * @param day
	 *            A String whose beginning should be parsed
	 * @param _format
	 *            指定格式
	 * @return Date
	 * @throws ParseException
	 *             转化失败
	 */
	public static Date _parse(String day, String _format) throws ParseException {
		return _getDateFormat(_format).parse(day);
	}

	/**
	 * 线程安全的时间Format<br>
	 * 
	 * @author Lucifer Wong
	 * @param source
	 *            A String, part of which should be parsed.
	 * @param _format
	 *            指定的格式
	 * @param pos
	 *            A ParsePosition object with index and error index information
	 *            as described above.
	 * @return SimpleDateFormat
	 */
	public static Object _parseObject(String source, String _format, ParsePosition pos) {
		return _getDateFormat(_format).parse(source, pos);
	}

	/**
	 * 取得指定天数后的时间
	 * 
	 * @author Lucifer Wong
	 * @param date
	 *            基准时间
	 * @param dayAmount
	 *            指定天数，允许为负数
	 * @return 指定天数后的时间
	 */
	public static Date addDay(Date date, int dayAmount) {
		if (date == null) {
			return null;
		}

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DATE, dayAmount);
		return calendar.getTime();
	}

	/**
	 * 给指定日期增加天数，为空时默认当前时间
	 * 
	 * @author Lucifer Wong
	 * @param day
	 *            增加天数 正数相加、负数相减
	 * @param date
	 *            指定日期
	 * @param format
	 *            日期格式 为空默认 yyyy-mm-dd HH:mm:ss
	 * @return String 指定输出格式
	 */
	public static String addDayToDate(int day, Date date, String format) {
		Calendar calendar = getCalendar(date, format);
		SimpleDateFormat sdf = DateUtil.getFormat(format);

		calendar.add(Calendar.DATE, day);

		return sdf.format(calendar.getTime());
	}

	/**
	 * 给指定日期增加天数，为空时默认当前时间
	 * 
	 * @author Lucifer Wong
	 * @param day
	 *            增加天数 正数相加、负数相减
	 * @param date
	 *            指定日期
	 * @param format
	 *            日期格式 为空默认 yyyy-mm-dd HH:mm:ss
	 * @return String 指定输出格式
	 */
	public static String addDayToDate(int day, String date, String format) {
		Date newDate = new Date();
		if (!ValidatorUtil.isEmpty(date)) {
			newDate = string2Date(date, format);
		}

		return addDayToDate(day, newDate, format);
	}

	/**
	 * 取得指定小时数后的时间
	 * 
	 * @author Lucifer Wong
	 * @param date
	 *            基准时间
	 * @param hourAmount
	 *            指定小时数，允许为负数
	 * @return 指定小时数后的时间
	 */
	public static Date addHour(Date date, int hourAmount) {
		if (date == null) {
			return null;
		}

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.HOUR, hourAmount);
		return calendar.getTime();
	}

	/**
	 * 给指定日期增加小时，为空时默认当前时间
	 * 
	 * @author Lucifer Wong
	 * @param hour
	 *            增加小时 正数相加、负数相减
	 * @param date
	 *            指定日期
	 * @param format
	 *            日期格式 为空默认 yyyy-mm-dd HH:mm:ss
	 * @return String 指定输出格式
	 */
	public static String addHourToDate(int hour, Date date, String format) {
		Calendar calendar = getCalendar(date, format);
		SimpleDateFormat sdf = DateUtil.getFormat(format);

		calendar.add(Calendar.HOUR, hour);

		return sdf.format(calendar.getTime());
	}

	/**
	 * 给指定日期增加小时，为空时默认当前时间
	 * 
	 * @author Lucifer Wong
	 * @param hour
	 *            增加小时 正数相加、负数相减
	 * @param date
	 *            指定日期
	 * @param format
	 *            日期格式 为空默认 yyyy-mm-dd HH:mm:ss
	 * @return String 指定输出格式
	 */
	public static String addHourToDate(int hour, String date, String format) {
		Date newDate = new Date();
		if (null != date && !"".equals(date)) {
			newDate = string2Date(date, format);
		}

		return addHourToDate(hour, newDate, format);
	}

	/**
	 * 取得指定分钟数后的时间
	 * 
	 * @author Lucifer Wong
	 * @param date
	 *            基准时间
	 * @param minuteAmount
	 *            指定分钟数，允许为负数
	 * @return 指定分钟数后的时间
	 */
	public static Date addMinute(Date date, int minuteAmount) {
		if (date == null) {
			return null;
		}

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.MINUTE, minuteAmount);
		return calendar.getTime();
	}

	/**
	 * 给指定的日期增加分钟，为空时默认当前时间
	 * 
	 * @author Lucifer Wong
	 * @param minute
	 *            增加分钟 正数相加、负数相减
	 * @param date
	 *            指定日期
	 * @param format
	 *            日期格式 为空默认 yyyy-mm-dd HH:mm:ss
	 * @return String 指定输出格式
	 */
	public static String addMinuteToDate(int minute, Date date, String format) {
		Calendar calendar = getCalendar(date, format);
		SimpleDateFormat sdf = DateUtil.getFormat(format);

		calendar.add(Calendar.MINUTE, minute);

		return sdf.format(calendar.getTime());
	}

	/**
	 * 给指定的日期增加分钟，为空时默认当前时间
	 * 
	 * @author Lucifer Wong
	 * @param minute
	 *            增加分钟 正数相加、负数相减
	 * @param date
	 *            指定日期
	 * @param format
	 *            日期格式 为空默认 yyyy-mm-dd HH:mm:ss
	 * @return String 指定输出格式
	 */
	public static String addMinuteToDate(int minute, String date, String format) {
		Date newDate = new Date();
		if (null != date && !"".equals(date)) {
			newDate = string2Date(date, format);
		}

		return addMinuteToDate(minute, newDate, format);
	}

	/**
	 * 给指定日期增加月份 为空时默认当前时间
	 * 
	 * @author Lucifer Wong
	 * @param month
	 *            增加月份 正数相加、负数相减
	 * @param date
	 *            指定时间
	 * @param format
	 *            指定格式 为空默认 yyyy-mm-dd HH:mm:ss
	 * @return String 指定输出格式
	 */
	public static String addMothToDate(int month, Date date, String format) {
		Calendar calender = getCalendar(date, format);
		SimpleDateFormat sdf = DateUtil.getFormat(format);

		calender.add(Calendar.MONTH, month);

		return sdf.format(calender.getTime());
	}

	/**
	 * 给指定日期增加月份 为空时默认当前时间
	 * 
	 * @author Lucifer Wong
	 * @param month
	 *            增加月份 正数相加、负数相减
	 * @param date
	 *            指定时间
	 * @param format
	 *            指定格式 为空默认 yyyy-mm-dd HH:mm:ss
	 * @return String 指定输出格式
	 */
	public static String addMothToDate(int month, String date, String format) {
		Date newDate = new Date();
		if (null != date && !"".equals(date)) {
			newDate = string2Date(date, format);
		}

		return addMothToDate(month, newDate, format);
	}

	/**
	 * 给指定日期增加秒，为空时默认当前时间
	 * 
	 * @author Lucifer Wong
	 * @param second
	 *            增加秒 正数相加、负数相减
	 * @param date
	 *            指定日期
	 * @param format
	 *            日期格式 为空默认 yyyy-mm-dd HH:mm:ss
	 * @return String 指定输出格式
	 */
	public static String addSecondToDate(int second, Date date, String format) {
		Calendar calendar = getCalendar(date, format);
		SimpleDateFormat sdf = DateUtil.getFormat(format);

		calendar.add(Calendar.SECOND, second);

		return sdf.format(calendar.getTime());
	}

	/**
	 * 给指定日期增加秒，为空时默认当前时间
	 * 
	 * @author Lucifer Wong
	 * @param second
	 *            增加秒 正数相加、负数相减
	 * @param date
	 *            指定日期
	 * @param format
	 *            日期格式 为空默认 yyyy-mm-dd HH:mm:ss
	 * @return String 指定输出格式
	 */
	public static String addSecondToDate(int second, String date, String format) {
		Date newDate = new Date();
		if (null != date && !"".equals(date)) {
			newDate = string2Date(date, format);
		}

		return addSecondToDate(second, newDate, format);
	}

	/**
	 * 给指定日期加入年份，为空时默认当前时间
	 * 
	 * @author Lucifer Wong
	 * @param year
	 *            年份 正数相加、负数相减
	 * @param date
	 *            为空时，默认为当前时间
	 * @param format
	 *            默认格式为：yyyy-MM-dd HH:mm:ss
	 * @return String 指定输出格式
	 */
	public static String addYearToDate(int year, Date date, String format) {
		Calendar calender = getCalendar(date, format);
		SimpleDateFormat sdf = DateUtil.getFormat(format);

		calender.add(Calendar.YEAR, year);

		return sdf.format(calender.getTime());
	}

	/**
	 * 给指定日期加入年份，为空时默认当前时间
	 * 
	 * @author Lucifer Wong
	 * @param year
	 *            年份 正数相加、负数相减
	 * @param date
	 *            为空时，默认为当前时间
	 * @param format
	 *            默认格式为：yyyy-MM-dd HH:mm:ss
	 * @return String 指定输出格式
	 */
	public static String addYearToDate(int year, String date, String format) {
		Date newDate = new Date();
		if (null != date && !"".equals(date)) {
			newDate = string2Date(date, format);
		}

		return addYearToDate(year, newDate, format);
	}

	/**
	 * 比较两个日期的大小。<br>
	 * 若date1 &gt; date2 则返回 1<br>
	 * 若date1 = date2 则返回 0<br>
	 * 若date1 &lt; date2 则返回-1
	 * 
	 * @author Lucifer Wong
	 *
	 * @param date1
	 *            日期1
	 * @param date2
	 *            日期2
	 * @param format
	 *            待转换的格式
	 * @return 比较结果
	 */
	public static int compare(String date1, String date2, String format) {
		DateFormat df = DateUtil.getFormat(format);
		try {
			Date dt1 = df.parse(date1);
			Date dt2 = df.parse(date2);
			if (dt1.getTime() > dt2.getTime()) {
				return 1;
			} else if (dt1.getTime() < dt2.getTime()) {
				return -1;
			} else {
				return 0;
			}
		} catch (Exception exception) {
			exception.printStackTrace();
		}
		return 0;
	}

	/**
	 * 比较两个日期相隔多少天(月、年) <br>
	 * 例：<br>
	 * &nbsp;compareDate("2009-09-12", null, 0);//比较天 <br>
	 * &nbsp;compareDate("2009-09-12", null, 1);//比较月 <br>
	 * &nbsp;compareDate("2009-09-12", null, 2);//比较年 <br>
	 * 
	 * @author Lucifer Wong
	 * @param startDay
	 *            需要比较的时间 不能为空(null),需要正确的日期格式 ,如：2009-09-12
	 * @param endDay
	 *            被比较的时间 为空(null)则为当前时间
	 * @param stype
	 *            返回值类型 0为多少天，1为多少个月，2为多少年
	 * @return int 两个日期相隔多少天(月、年)
	 */
	public static int compareDate(String startDay, String endDay, int stype) {
		int n = 0;
		startDay = DateUtil.formatDate(startDay, DATE_FORMAT1);
		endDay = DateUtil.formatDate(endDay, DATE_FORMAT1);

		String formatStyle = DATE_FORMAT1;
		if (1 == stype) {
			formatStyle = "yyyy-MM";
		} else if (2 == stype) {
			formatStyle = "yyyy";
		}

		endDay = endDay == null ? getCurrentTime(DATE_FORMAT1) : endDay;

		DateFormat df = new SimpleDateFormat(formatStyle);
		Calendar c1 = Calendar.getInstance();
		Calendar c2 = Calendar.getInstance();
		try {
			c1.setTime(df.parse(startDay));
			c2.setTime(df.parse(endDay));
		} catch (Exception e) {
			e.printStackTrace();
		}
		while (!c1.after(c2)) { // 循环对比，直到相等，n 就是所要的结果
			n++;
			if (stype == 1) {
				c1.add(Calendar.MONTH, 1); // 比较月份，月份+1
			} else {
				c1.add(Calendar.DATE, 1); // 比较天数，日期+1
			}
		}
		n = n - 1;
		if (stype == 2) {
			n = (int) n / 365;
		}
		return n;
	}

	/**
	 * 比较两日期对象中的小时和分钟部分的大小.
	 * 
	 * @author Lucifer Wong
	 * @param date
	 *            日期对象1, 如果为 <code>null</code> 会以当前时间的日期对象代替
	 * @param anotherDate
	 *            日期对象2, 如果为 <code>null</code> 会以当前时间的日期对象代替
	 * @return 如果日期对象1大于日期对象2, 则返回大于0的数; 反之返回小于0的数; 如果两日期对象相等, 则返回0.
	 */
	public static int compareHourAndMinute(Date date, Date anotherDate) {
		if (date == null) {
			date = new Date();
		}

		if (anotherDate == null) {
			anotherDate = new Date();
		}

		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		int hourOfDay1 = cal.get(Calendar.HOUR_OF_DAY);
		int minute1 = cal.get(Calendar.MINUTE);

		cal.setTime(anotherDate);
		int hourOfDay2 = cal.get(Calendar.HOUR_OF_DAY);
		int minute2 = cal.get(Calendar.MINUTE);

		if (hourOfDay1 > hourOfDay2) {
			return 1;
		} else if (hourOfDay1 == hourOfDay2) {
			// 小时相等就比较分钟
			return minute1 > minute2 ? 1 : (minute1 == minute2 ? 0 : -1);
		} else {
			return -1;
		}
	}

	/**
	 * 比较两日期对象的大小, 忽略秒, 只精确到分钟.
	 * 
	 * @author Lucifer Wong
	 * @param date
	 *            日期对象1, 如果为 <code>null</code> 会以当前时间的日期对象代替
	 * @param anotherDate
	 *            日期对象2, 如果为 <code>null</code> 会以当前时间的日期对象代替
	 * @return 如果日期对象1大于日期对象2, 则返回大于0的数; 反之返回小于0的数; 如果两日期对象相等, 则返回0.
	 */
	public static int compareIgnoreSecond(Date date, Date anotherDate) {
		if (date == null) {
			date = new Date();
		}

		if (anotherDate == null) {
			anotherDate = new Date();
		}

		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		date = cal.getTime();

		cal.setTime(anotherDate);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		anotherDate = cal.getTime();

		return date.compareTo(anotherDate);
	}

	/**
	 * 比较两个时间相差多少小时(分钟、秒)
	 * 
	 * @author Lucifer Wong
	 * @param startTime
	 *            需要比较的时间 不能为空，且必须符合正确格式：2012-12-12 12:12:
	 * @param endTime
	 *            需要被比较的时间 若为空则默认当前时间
	 * @param type
	 *            1：小时 2：分钟 3：秒
	 * @return int 两个时间相差多少小时(分钟、秒)
	 */
	public static int compareTime(String startTime, String endTime, int type) {
		// endTime是否为空，为空默认当前时间

		if (ValidatorUtil.isEmpty(endTime)) {
			endTime = getCurrentTime();
		}

		SimpleDateFormat sdf = DateUtil.getFormat("");
		int value = 0;
		try {
			Date begin = sdf.parse(startTime);
			Date end = sdf.parse(endTime);
			long between = (end.getTime() - begin.getTime()) / 1000; // 除以1000转换成豪秒
			if (type == 1) { // 小时
				value = (int) (between % (24 * 36000) / 3600);
			} else if (type == 2) {
				value = (int) (between % 3600 / 60);
			} else if (type == 3) {
				value = (int) (between % 60 / 60);
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return value;
	}

	/**
	 * 取得当前时间的字符串表示
	 * 
	 * @author Lucifer Wong
	 * @return 当前时间的字符串表示
	 */
	public static String currentDate2String() {
		return date2String(new Date());
	}

	/**
	 * 取得当前时间的字符串表示
	 * 
	 * @author Lucifer Wong
	 * @return 当前时间的字符串表示
	 */
	public static String currentDate2StringByDay() {
		return date2StringByDay(new Date());
	}

	/**
	 * 取得今天的最后一个时刻
	 * 
	 * @author Lucifer Wong
	 * @return 今天的最后一个时刻
	 */
	public static Date currentEndDate() {
		return getEndDate(new Date());
	}

	/**
	 * 取得今天的第一个时刻
	 * 
	 * @author Lucifer Wong
	 * @return 今天的第一个时刻
	 */
	public static Date currentStartDate() {
		return getStartDate(new Date());
	}

	/**
	 * 日期转换为字符串
	 * 
	 * @author Lucifer Wong
	 * @param value
	 *            日期
	 * @return yyyy-MM-dd HH:mm:ss
	 */
	public static String date2String(Date value) {
		if (value == null) {
			return null;
		}

		SimpleDateFormat sdf = DateUtil.getFormat(DateUtil.DATE_FORMAT2);
		return sdf.format(value);
	}

	/**
	 * 将日期格式转换成String
	 * 
	 * @author Lucifer Wong
	 * @param value
	 *            需要转换的日期
	 * @param format
	 *            日期格式
	 * @return String 将日期格式转换成String
	 */
	public static String date2String(Date value, String format) {
		if (value == null) {
			return null;
		}

		SimpleDateFormat sdf = DateUtil.getFormat(format);
		return sdf.format(value);
	}

	/**
	 * 把时间转换成字符串，格式为2000-01-01
	 * 
	 * @author Lucifer Wong
	 * @param date
	 *            时间
	 * @return 时间字符串
	 */
	public static String date2StringByDay(Date date) {
		return date2String(date, DATE_FORMAT1);
	}

	/**
	 * 把时间转换成字符串，格式为yyyy-MM-dd HH:mm
	 * 
	 * @author Lucifer Wong
	 * @param date
	 *            时间
	 * @return 时间字符串
	 */
	public static String date2StringByMinute(Date date) {
		return date2String(date, "yyyy-MM-dd HH:mm");
	}

	/**
	 * 把时间转换成字符串，格式为yyyy-MM-dd HH:mm:ss
	 * 
	 * @param date
	 *            时间
	 * @return 时间字符串
	 */
	public static String date2StringBySecond(Date date) {
		return date2String(date, DATE_FORMAT2);
	}

	/**
	 * 格式化日期
	 * 
	 * @author Lucifer Wong
	 * @param value
	 *            时间
	 * @return yyyy-MM-dd HH:mm:ss
	 */
	public static String formatDate(Date value) {
		return formatDate(DateUtil.date2String(value));
	}

	/**
	 * 格式化日期
	 * 
	 * @author Lucifer Wong
	 * 
	 * @param date
	 *            时间
	 * @param format
	 *            指定的格式
	 * @return 按指定格式输出format为 null 或者"" 则输出yyyy年MM月dd日
	 */
	public static String formatDate(Date date, String format) {
		return formatDate(DateUtil.date2String(date), format);
	}

	/**
	 * 格式化是时间，采用默认格式（yyyy-MM-dd HH:mm:ss）
	 * 
	 * @author Lucifer Wong
	 *
	 * @param value
	 *            时间
	 * @return 格式化的时间
	 */
	public static String formatDate(String value) {
		return getFormat(DATE_FORMAT2).format(DateUtil.string2Date(value, DATE_FORMAT2));
	}

	/**
	 * 按指定格式输出{@code format}为 null 或者"" 则输出yyyy年MM月dd日
	 * 
	 * @author Lucifer Wong
	 * @param date
	 *            时间
	 * @param format
	 *            指定格式
	 * @return 格式化的时间
	 */
	public static String formatDate(String date, String format) {
		if (ValidatorUtil.isEmpty(date)) {
			return "";
		}
		Date dt = null;
		SimpleDateFormat inFmt = null;
		SimpleDateFormat outFmt = null;
		ParsePosition pos = new ParsePosition(0);
		date = date.replace("-", "").replace(":", "");
		if ((date == null) || ("".equals(date.trim())))
			return "";
		try {
			if (Long.parseLong(date) == 0L)
				return "";
		} catch (Exception nume) {
			return date;
		}
		try {
			switch (date.trim().length()) {
			case 14:
				inFmt = new SimpleDateFormat("yyyyMMddHHmmss");
				break;
			case 12:
				inFmt = new SimpleDateFormat("yyyyMMddHHmm");
				break;
			case 10:
				inFmt = new SimpleDateFormat("yyyyMMddHH");
				break;
			case 8:
				inFmt = new SimpleDateFormat("yyyyMMdd");
				break;
			case 6:
				inFmt = new SimpleDateFormat("yyyyMM");
				break;
			case 7:
			case 9:
			case 11:
			case 13:
			default:
				return date;
			}
			if ((dt = inFmt.parse(date, pos)) == null)
				return date;
			if (ValidatorUtil.isEmpty(format)) {
				outFmt = new SimpleDateFormat("yyyy年MM月dd日");
			} else {
				outFmt = new SimpleDateFormat(format);
			}
			return outFmt.format(dt);
		} catch (Exception ex) {
		}
		return date;
	}

	/**
	 * 
	 * 格式转换<br>
	 * yyyy-MM-dd hh:mm:ss 和 yyyyMMddhhmmss 相互转换<br>
	 * yyyy-mm-dd 和yyyymmss 相互转换
	 * 
	 * @author Lucifer Wong
	 * @param value
	 *            日期
	 * @return String 格式化的时间
	 */
	public static String formatString(String value) {
		String sReturn = "";
		if (ValidatorUtil.isEmpty(value))
			return sReturn;
		if (value.length() == 14) { // 长度为14格式转换成yyyy-mm-dd hh:mm:ss
			sReturn = value.substring(0, 4) + "-" + value.substring(4, 6) + "-" + value.substring(6, 8) + " "
					+ value.substring(8, 10) + ":" + value.substring(10, 12) + ":" + value.substring(12, 14);
			return sReturn;
		}
		if (value.length() == 19) { // 长度为19格式转换成yyyymmddhhmmss
			sReturn = value.substring(0, 4) + value.substring(5, 7) + value.substring(8, 10) + value.substring(11, 13)
					+ value.substring(14, 16) + value.substring(17, 19);
			return sReturn;
		}
		if (value.length() == 10) { // 长度为10格式转换成yyyymmhh
			sReturn = value.substring(0, 4) + value.substring(5, 7) + value.substring(8, 10);
		}
		if (value.length() == 8) { // 长度为8格式转化成yyyy-mm-dd
			sReturn = value.substring(0, 4) + "-" + value.substring(4, 6) + "-" + value.substring(6, 8);
		}
		return sReturn;
	}

	/**
	 * 获取指定格式指定时间的日历
	 * 
	 * @author Lucifer Wong
	 * @param date
	 *            时间
	 * @param format
	 *            格式
	 * @return Calendar 格式化的时间
	 */
	public static Calendar getCalendar(Date date, String format) {
		if (date == null) {
			date = getCurrentDate(format);
		}

		Calendar calender = Calendar.getInstance();
		calender.setTime(date);

		return calender;
	}

	/**
	 * 根据某星期几的英文名称来获取该星期几的中文数. <br>
	 * 
	 * @param englishWeekName
	 *            星期的英文名称
	 * @return 星期的中文数
	 */
	public static String getChineseWeekNumber(String englishWeekName) {
		if ("monday".equalsIgnoreCase(englishWeekName)) {
			return "一";
		}

		if ("tuesday".equalsIgnoreCase(englishWeekName)) {
			return "二";
		}

		if ("wednesday".equalsIgnoreCase(englishWeekName)) {
			return "三";
		}

		if ("thursday".equalsIgnoreCase(englishWeekName)) {
			return "四";
		}

		if ("friday".equalsIgnoreCase(englishWeekName)) {
			return "五";
		}

		if ("saturday".equalsIgnoreCase(englishWeekName)) {
			return "六";
		}

		if ("sunday".equalsIgnoreCase(englishWeekName)) {
			return "日";
		}

		return null;
	}

	/**
	 * 获取当前时间，格式为yyyy-MM-dd HH:mm:ss
	 * 
	 * @author Lucifer Wong
	 * @return Date 格式化的时间
	 */
	public static Date getCurrentDate() {
		return getCurrentDate(DateUtil.DATE_FORMAT2);
	}

	/**
	 * 获取指定格式的当前时间：为空时格式为yyyy-mm-dd HH:mm:ss
	 * 
	 * @author Lucifer Wong
	 * @param format
	 *            指定格式
	 * @return Date 格式化的时间
	 */
	public static Date getCurrentDate(String format) {
		SimpleDateFormat sdf = DateUtil.getFormat(format);
		String dateS = getCurrentTime(format);
		Date date = null;
		try {
			date = sdf.parse(dateS);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}

	/**
	 * 获取指定日期的日
	 * 
	 * @author Lucifer Wong
	 * @param value
	 *            日期
	 * @return int 日
	 */
	public static int getCurrentDay(Date value) {
		String date = date2String(value, DateUtil.DATE_DAY);
		return Integer.valueOf(date);
	}

	/**
	 * 获取指定日期的日
	 * 
	 * @author Lucifer Wong
	 * @param value
	 *            日期
	 * @return int 日
	 */
	public static int getCurrentDay(String value) {
		Date date = string2Date(value, DateUtil.DATE_DAY);
		Calendar calendar = getCalendar(date, DateUtil.DATE_DAY);

		return calendar.get(Calendar.DATE);
	}

	/**
	 * 获取指定日期的小时
	 * 
	 * @author Lucifer Wong
	 * @param value
	 *            日期
	 * @return int 小时
	 */
	public static int getCurrentHour(Date value) {
		String date = date2String(value, DateUtil.DATE_HOUR);
		return Integer.valueOf(date);
	}

	/**
	 * 获取指定日期的小时
	 * 
	 * @author Lucifer Wong
	 * @param value
	 *            日期
	 * @return int 小时
	 */
	public static int getCurrentHour(String value) {
		Date date = string2Date(value, DateUtil.DATE_HOUR);
		Calendar calendar = getCalendar(date, DateUtil.DATE_HOUR);

		return calendar.get(Calendar.DATE);
	}

	/**
	 * 获取指定日期的分钟
	 * 
	 * @author Lucifer Wong
	 * @param value
	 *            日期
	 * @return int 分钟
	 */
	public static int getCurrentMinute(Date value) {
		String date = date2String(value, DateUtil.DATE_MINUTE);
		return Integer.valueOf(date);
	}

	/**
	 * 获取指定日期的分钟
	 * 
	 * @author Lucifer Wong
	 * @param value
	 *            日期
	 * @return int 分钟
	 */
	public static int getCurrentMinute(String value) {
		Date date = string2Date(value, DateUtil.DATE_MINUTE);
		Calendar calendar = getCalendar(date, DateUtil.DATE_MINUTE);

		return calendar.get(Calendar.MINUTE);
	}

	/**
	 * 获取指定日期的月份
	 * 
	 * @author Lucifer Wong
	 * @param value
	 *            日期
	 * @return int 月份
	 */
	public static int getCurrentMonth(Date value) {
		String date = date2String(value, DateUtil.DATE_MONTH);
		return Integer.valueOf(date);
	}

	/**
	 * 获取指定日期的月份
	 * 
	 * @author Lucifer Wong
	 * @param value
	 *            日期
	 * @return int 月份
	 */
	public static int getCurrentMonth(String value) {
		Date date = string2Date(value, DateUtil.DATE_MONTH);
		Calendar calendar = getCalendar(date, DateUtil.DATE_MONTH);

		return calendar.get(Calendar.MONTH);
	}

	/**
	 * 获取当前时间，格式为：yyyy-MM-dd HH:mm:ss
	 * 
	 * @author Lucifer Wong
	 * @return String 当前时间
	 */
	public static String getCurrentTime() {
		return getCurrentTime(DateUtil.DATE_FORMAT2);
	}

	/**
	 * 根据指定格式获取当前时间
	 * 
	 * @author Lucifer Wong
	 * @param format
	 *            指定格式
	 * @return String 当前时间
	 */
	public static String getCurrentTime(String format) {
		SimpleDateFormat sdf = DateUtil.getFormat(format);
		Date date = new Date();
		return sdf.format(date);
	}

	/**
	 * 获取当前日期为星期几
	 * 
	 * @author Lucifer Wong
	 * @param value
	 *            日期
	 * @return String 星期几
	 */
	public static String getCurrentWeek(Date value) {
		Calendar calendar = getCalendar(value, DateUtil.DATE_FORMAT1);
		int weekIndex = calendar.get(Calendar.DAY_OF_WEEK) - 1 < 0 ? 0 : calendar.get(Calendar.DAY_OF_WEEK) - 1;

		return WEEKS[weekIndex];
	}

	/**
	 * 获取当前日期为星期几
	 * 
	 * @author Lucifer Wong
	 * @param value
	 *            日期
	 * @return String 星期几
	 */
	public static String getCurrentWeek(String value) {
		Date date = string2Date(value, DateUtil.DATE_FORMAT1);
		return getCurrentWeek(date);
	}

	/**
	 * 获取指定日期的年份
	 * 
	 * @author Lucifer Wong
	 * @param value
	 *            日期
	 * @return int 年
	 */
	public static int getCurrentYear(Date value) {
		String date = date2String(value, DateUtil.DATE_YEAR);
		return Integer.valueOf(date);
	}

	/**
	 * 获取指定日期的年份
	 * 
	 * @author Lucifer Wong
	 * @param value
	 *            日期
	 * @return int 年
	 */
	public static int getCurrentYear(String value) {
		Date date = string2Date(value, DateUtil.DATE_YEAR);
		Calendar calendar = getCalendar(date, DateUtil.DATE_YEAR);
		return calendar.get(Calendar.YEAR);
	}

	/**
	 * 根据指定的年, 月, 日等参数获取日期对象.
	 * 
	 * @author Lucifer Wong
	 * @param year
	 *            年
	 * @param month
	 *            月
	 * @param date
	 *            日
	 * @return 对应的日期对象
	 */
	public static Date getDate(int year, int month, int date) {
		return getDate(year, month, date, 0, 0);
	}

	/**
	 * 根据指定的年, 月, 日, 时, 分等参数获取日期对象.
	 * 
	 * @author Lucifer Wong
	 * @param year
	 *            年
	 * @param month
	 *            月
	 * @param date
	 *            日
	 * @param hourOfDay
	 *            时(24小时制)
	 * @param minute
	 *            分
	 * @return 对应的日期对象
	 */
	public static Date getDate(int year, int month, int date, int hourOfDay, int minute) {
		return getDate(year, month, date, hourOfDay, minute, 0);
	}

	/**
	 * 根据指定的年, 月, 日, 时, 分, 秒等参数获取日期对象.
	 * 
	 * @author Lucifer Wong
	 * @param year
	 *            年
	 * @param month
	 *            月
	 * @param date
	 *            日
	 * @param hourOfDay
	 *            时(24小时制)
	 * @param minute
	 *            分
	 * @param second
	 *            秒
	 * @return 对应的日期对象
	 */
	public static Date getDate(int year, int month, int date, int hourOfDay, int minute, int second) {
		Calendar cal = Calendar.getInstance();
		cal.set(year, month - 1, date, hourOfDay, minute, second);
		cal.set(Calendar.MILLISECOND, 0);

		return cal.getTime();
	}

	/**
	 * 获取本周的指定星期的日期。
	 * 
	 * @author Lucifer Wong
	 * @param dayOfWeek
	 *            星期几，取值范围是 {@link Calendar#MONDAY} - {@link Calendar#SUNDAY}
	 * @return 本周的指定星期的日期
	 */
	public static Date getDateOfCurrentWeek(int dayOfWeek) {
		if (dayOfWeek > 7 || dayOfWeek < 1) {
			throw new IllegalArgumentException("参数必须是1-7之间的数字");
		}

		return getDateOfRange(dayOfWeek, 0);
	}

	/**
	 * 获取下周的指定星期的日期。
	 * 
	 * @author Lucifer Wong
	 * @param dayOfWeek
	 *            星期几，取值范围是 {@link Calendar#MONDAY} - {@link Calendar#SUNDAY}
	 * @return 下周的指定星期的日期
	 */
	public static Date getDateOfNextWeek(int dayOfWeek) {
		if (dayOfWeek > 7 || dayOfWeek < 1) {
			throw new IllegalArgumentException("参数必须是1-7之间的数字");
		}

		return getDateOfRange(dayOfWeek, 7);
	}

	/**
	 * 获取上周的指定星期的日期。
	 * 
	 * @author Lucifer Wong
	 * @param dayOfWeek
	 *            星期几，取值范围是 {@link Calendar#MONDAY} - {@link Calendar#SUNDAY}
	 * @return 上周的指定星期的日期
	 */
	public static Date getDateOfPreviousWeek(int dayOfWeek) {
		if (dayOfWeek > 7 || dayOfWeek < 1) {
			throw new IllegalArgumentException("参数必须是1-7之间的数字");
		}

		return getDateOfRange(dayOfWeek, -7);
	}

	private static Date getDateOfRange(int dayOfWeek, int dayOfRange) {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.DAY_OF_WEEK, dayOfWeek);
		cal.set(Calendar.DATE, cal.get(Calendar.DATE) + dayOfRange);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return cal.getTime();
	}

	/**
	 * 取得某个日期是星期几，星期日是1，依此类推
	 * 
	 * @author Lucifer Wong
	 * @param date
	 *            日期
	 * @return 星期几
	 */
	public static int getDayOfWeek(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal.get(Calendar.DAY_OF_WEEK);
	}

	/**
	 * 获取某天的结束时间, e.g. 2005-10-01 23:59:59.999
	 * 
	 * @author Lucifer Wong
	 * @param date
	 *            日期对象
	 * @return 该天的结束时间
	 */
	public static Date getEndDate(Date date) {

		if (date == null) {
			return null;
		}

		Calendar cal = Calendar.getInstance();
		cal.setTime(date);

		cal.set(Calendar.HOUR_OF_DAY, 23);
		cal.set(Calendar.MINUTE, 59);
		cal.set(Calendar.SECOND, 59);
		cal.set(Calendar.MILLISECOND, 999);

		return cal.getTime();
	}

	/**
	 * 获取日期显示格式，为空默认为yyyy-mm-dd HH:mm:ss
	 * 
	 * @author Lucifer Wong
	 * @param format
	 *            指定格式
	 * @return SimpleDateFormat 日期显示格式
	 */
	protected static SimpleDateFormat getFormat(String format) {
		if (format == null || "".equals(format)) {
			format = DATE_FORMAT2;
		}
		return new SimpleDateFormat(format);
	}

	/**
	 * 取得一个月最多的天数
	 * 
	 * @author Lucifer Wong
	 * @param year
	 *            年份
	 * @param month
	 *            月份，0表示1月，依此类推
	 * @return 最多的天数
	 */
	public static int getMaxDayOfMonth(int year, int month) {
		if (month == 1 && isLeapYear(year)) {
			return 29;
		}
		return DAY_OF_MONTH[month];
	}

	/**
	 * 获取指定月份的第一天
	 * 
	 * @author Lucifer Wong
	 * @param date
	 *            指定日期
	 * @return 指定月份的第一天
	 */
	public static String getMonthFirstDate(String date) {
		date = DateUtil.formatDate(date);
		return DateUtil.formatDate(date, "yyyy-MM") + "-01";
	}

	/**
	 * 获取指定月份的最后一天
	 * 
	 * @author Lucifer Wong
	 * @param date
	 *            指定日期
	 * @return 指定月份的最后一天
	 */
	public static String getMonthLastDate(String date) {
		Date strDate = DateUtil.string2Date(getMonthFirstDate(date));
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(strDate);
		calendar.add(Calendar.MONTH, 1);
		calendar.add(Calendar.DAY_OF_YEAR, -1);
		return DateUtil.formatDate(calendar.getTime());
	}

	/**
	 * 得到指定日期的下一天
	 * 
	 * @author Lucifer Wong
	 * @param date
	 *            日期对象
	 * @return 同一时间的下一天的日期对象
	 */
	public static Date getNextDay(Date date) {
		return addDay(date, 1);
	}

	/**
	 * 获取某天的起始时间, e.g. 2005-10-01 00:00:00.000
	 * 
	 * @author Lucifer Wong
	 * @param date
	 *            日期对象
	 * @return 该天的起始时间
	 */
	public static Date getStartDate(Date date) {
		if (date == null) {
			return null;
		}

		Calendar cal = Calendar.getInstance();
		cal.setTime(date);

		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);

		return cal.getTime();
	}

	/**
	 * 根据日期对象来获取日期中的时间(HH:mm:ss).
	 * 
	 * @author Lucifer Wong
	 * @param date
	 *            日期对象
	 * @return 时间字符串, 格式为: HH:mm:ss
	 */
	public static String getTime(Date date) {
		if (date == null) {
			return null;
		}

		SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
		return format.format(date);
	}

	/**
	 * 根据日期对象来获取日期中的时间(HH:mm).
	 * 
	 * @author Lucifer Wong
	 * @param date
	 *            日期对象
	 * @return 时间字符串, 格式为: HH:mm
	 */
	public static String getTimeIgnoreSecond(Date date) {
		if (date == null) {
			return null;
		}

		SimpleDateFormat format = new SimpleDateFormat("HH:mm");
		return format.format(date);
	}

	/**
	 * 获取所在星期的第一天
	 * 
	 * @author Lucifer Wong
	 * @param date
	 *            日期对象
	 * @return 所在星期的第一天
	 */
	public static Date getWeekFirstDate(Date date) {
		Calendar now = Calendar.getInstance();
		now.setTime(date);
		int today = now.get(Calendar.DAY_OF_WEEK);
		int first_day_of_week = now.get(Calendar.DATE) + 2 - today; // 星期一
		now.set(Calendar.DATE, first_day_of_week);
		return now.getTime();
	}

	/**
	 * 取得一年中的第几周。
	 * 
	 * @author Lucifer Wong
	 * @param date
	 *            日期对象
	 * @return 一年中的第几周
	 */
	public static int getWeekOfYear(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal.get(Calendar.WEEK_OF_YEAR);
	}

	/**
	 * 获取所在星期的最后一天
	 * 
	 * @author Lucifer Wong
	 * @param date
	 *            日期对象
	 * @return 所在星期的最后一天
	 */
	public static Date geWeektLastDate(Date date) {
		Calendar now = Calendar.getInstance();
		now.setTime(date);
		int today = now.get(Calendar.DAY_OF_WEEK);
		int first_day_of_week = now.get(Calendar.DATE) + 2 - today; // 星期一
		int last_day_of_week = first_day_of_week + 6; // 星期日
		now.set(Calendar.DATE, last_day_of_week);
		return now.getTime();
	}

	/**
	 * 判断是否是闰年
	 * 
	 * @author Lucifer Wong
	 * @param year
	 *            年份
	 * @return 是true，否则false
	 */
	public static boolean isLeapYear(int year) {
		Calendar calendar = Calendar.getInstance();
		return ((GregorianCalendar) calendar).isLeapYear(year);
	}

	/**
	 * 日期转化<br>
	 * 根据正则匹配 {@link #DATEPATTERN1}、{@link #DATEPATTERN2}、{@link #DATEPATTERN3}
	 * {@link #DATEPATTERN4}、{@link #DATEPATTERN5}、{@link #DATEPATTERN6} 返回日期
	 * 
	 * @author Lucifer Wong
	 * @param dateStr
	 *            被指定的字符串
	 * @return 结果字符串
	 */
	public static Date parseDate(String dateStr) {

		if (ValidatorUtil.isEmpty(dateStr)) {
			return null;
		}

		// Determine which date pattern (Matcher) to use
		Matcher matcher = DATEPATTERN1.matcher(dateStr);

		String year, month = null, day, mon = null, remains;

		if (matcher.find()) {
			year = matcher.group(1);
			month = matcher.group(2);
			day = matcher.group(3);
			remains = matcher.replaceFirst("");
		} else {
			matcher = DATEPATTERN2.matcher(dateStr);
			if (matcher.find()) {
				month = matcher.group(1);
				day = matcher.group(2);
				year = matcher.group(3);
				remains = matcher.replaceFirst("");
			} else {
				matcher = DATEPATTERN3.matcher(dateStr);
				if (matcher.find()) {
					mon = matcher.group(1);
					day = matcher.group(2);
					year = matcher.group(4);
					remains = matcher.replaceFirst("");
				} else {
					matcher = DATEPATTERN4.matcher(dateStr);
					if (matcher.find()) {
						day = matcher.group(1);
						mon = matcher.group(3);
						year = matcher.group(4);
						remains = matcher.replaceFirst("");
					} else {
						matcher = DATEPATTERN5.matcher(dateStr);
						if (matcher.find()) {
							year = matcher.group(1);
							mon = matcher.group(2);
							day = matcher.group(3);
							remains = matcher.replaceFirst("");
						} else {
							matcher = DATEPATTERN6.matcher(dateStr);
							if (!matcher.find()) {
								return null;
							}
							year = matcher.group(5);
							mon = matcher.group(2);
							day = matcher.group(3);
							remains = matcher.group(4);
						}
					}
				}
			}
		}

		if (mon != null) { // Month will always be in Map, because regex forces
							// this.
			month = MONTHS.get(mon.trim().toLowerCase());
		}

		// Determine which date pattern (Matcher) to use
		String hour = null, min = null, sec = "00", milli = "0", tz = null;
		remains = remains.trim();
		matcher = TIMEPATTERN1.matcher(remains);
		if (matcher.find()) {
			hour = matcher.group(1);
			min = matcher.group(2);
			sec = matcher.group(3);
			milli = matcher.group(4);
			if (matcher.groupCount() > 4) {
				tz = matcher.group(5);
			}
		} else {
			matcher = TIMEPATTERN2.matcher(remains);
			if (matcher.find()) {
				hour = matcher.group(1);
				min = matcher.group(2);
				sec = matcher.group(3);
				if (matcher.groupCount() > 3) {
					tz = matcher.group(4);
				}
			} else {
				matcher = TIMEPATTERN3.matcher(remains);
				if (matcher.find()) {
					hour = matcher.group(1);
					min = matcher.group(2);
					if (matcher.groupCount() > 2) {
						tz = matcher.group(3);
					}
				} else {
					matcher = null;
				}
			}
		}

		if (matcher != null) {
			remains = matcher.replaceFirst("");
		}

		// Clear out day of week (mon, tue, wed, ...)
		if (StringUtil.trimLength(remains) > 0) {
			Matcher dayMatcher = DAYPATTERN.matcher(remains);
			if (dayMatcher.find()) {
				remains = dayMatcher.replaceFirst("").trim();
			}
		}
		if (StringUtil.trimLength(remains) > 0) {
			remains = remains.trim();
			if (!remains.equals(",") && (!remains.equals("T"))) {
				return null;
			}
		}

		Calendar c = Calendar.getInstance();
		c.clear();
		if (tz != null) {
			if ("z".equalsIgnoreCase(tz)) {
				c.setTimeZone(TimeZone.getTimeZone("GMT"));
			} else {
				c.setTimeZone(TimeZone.getTimeZone("GMT" + tz));
			}
		}

		// Regex prevents these from ever failing to parse
		int y = Integer.parseInt(year);
		int m = Integer.parseInt(month) - 1; // months are 0-based
		int d = Integer.parseInt(day);

		if (m < 0 || m > 11) {
			return null;
		}
		if (d < 1 || d > 31) {
			return null;
		}

		if (matcher == null) { // no [valid] time portion
			c.set(y, m, d);
		} else {
			// Regex prevents these from ever failing to parse.
			int h = Integer.parseInt(hour);
			int mn = Integer.parseInt(min);
			int s = Integer.parseInt(sec);
			int ms = Integer.parseInt(milli);

			if (h > 23) {
				return null;
			}
			if (mn > 59) {
				return null;
			}
			if (s > 59) {
				return null;
			}

			// regex enforces millis to number
			c.set(y, m, d, h, mn, s);
			c.set(Calendar.MILLISECOND, ms);
		}
		return c.getTime();
	}

	/**
	 * 字符串转换为日期，日期格式为 yyyy-MM-dd HH:mm:ss
	 * 
	 * @author Lucifer Wong
	 * @param value
	 *            String
	 * @return Date
	 */
	public static Date string2Date(String value) {
		if (ValidatorUtil.isEmpty(value)) {
			return null;
		}

		SimpleDateFormat sdf = DateUtil.getFormat(DateUtil.DATE_FORMAT2);
		Date date = null;

		try {
			value = DateUtil.formatDate(value, DateUtil.DATE_FORMAT2);
			date = sdf.parse(value);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return date;
	}

	/**
	 * 将字符串(格式符合规范)转换成Date
	 * 
	 * @author Lucifer Wong
	 * @param value
	 *            需要转换的字符串
	 * @param format
	 *            日期格式
	 * @return Date
	 */
	public static Date string2Date(String value, String format) {
		if (ValidatorUtil.isEmpty(value)) {
			return null;
		}

		SimpleDateFormat sdf = DateUtil.getFormat(format);
		Date date = null;

		try {
			value = DateUtil.formatDate(value, format);
			date = sdf.parse(value);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return date;
	}

	/**
	 * 把字符串转换成日期，格式为yyyy-MM-dd HH:mm:ss
	 * 
	 * @author Lucifer Wong
	 * @param str
	 *            字符串
	 * @return 日期
	 */
	public static Date string2DateTime(String str) {
		return string2Date(str, DATE_FORMAT2);
	}

}
