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
package priv.lucife.utils.core.db;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import priv.lucife.utils.core.annotation.UBTCompatible;
import priv.lucife.utils.core.base.StringUtil;
import priv.lucife.utils.core.base.ValidatorUtil;
import priv.lucife.utils.core.date.DateUtil;

/**
 * JDBC 工具类,
 * 
 * @author Lucifer Wong
 */
@UBTCompatible
public abstract class JdbcUtil {

	/**
	 * SQL执行处理器接口, 用于处理带IN子句的SQL语句中IN中参数过长的问题
	 * 
	 * @author Lucifer Wong
	 */
	public interface InSQLProcessor {

		/**
		 * 执行SQL的方法.
		 * 
		 * @param sql
		 *            SQL语句
		 * @param args
		 *            语句中的参数
		 */
		public void executeSQL(String sql, Object[] args);

	}

	/**
	 * 动态查询 SQL 语句生成工具类
	 * 
	 * @author Lucifer Wong
	 */
	@UBTCompatible
	public final class SqlCreator {

		private final List<Object> args;
		private final List<Integer> argTypes;
		private boolean hasOrderBy = false;
		private boolean hasWhere = true;
		private boolean isFirst = true;
		private final StringBuilder sql;

		/**
		 * 构造方法。
		 * 
		 * @author Lucifer Wong
		 * @param baseSQL
		 *            带有 WHERE 关键字的原始 sql
		 */
		public SqlCreator(String baseSQL) {
			this(baseSQL, true);
		}

		/**
		 * 构造方法。
		 * 
		 * @author Lucifer Wong
		 * @param baseSQL
		 *            原始 sql
		 * @param hasWhere
		 *            原始 sql 是否带有 WHERE 关键字
		 */
		public SqlCreator(String baseSQL, boolean hasWhere) {
			if (ValidatorUtil.isEmpty(baseSQL)) {
				throw new IllegalArgumentException("baseSQL can't be null");
			}

			args = new ArrayList<Object>();
			argTypes = new ArrayList<Integer>();
			sql = new StringBuilder();
			sql.append(baseSQL.trim());
			this.hasWhere = hasWhere;
		}

		/**
		 * 增加查询条件
		 * 
		 * @author Lucifer Wong
		 * @param operator
		 *            操作，比如：AND、OR
		 * @param expression
		 *            表达式，比如：id=1
		 * @param precondition
		 *            先决条件，当为true时才会增加查询条件，比如 user != null
		 */
		public void addExpression(String operator, String expression, boolean precondition) {
			addExpression(operator, expression, null, precondition);
		}

		/**
		 * 增加查询条件
		 * 
		 * @author Lucifer Wong
		 * @param operator
		 *            操作，比如：AND、OR
		 * @param expression
		 *            表达式，比如：id=?
		 * @param arg
		 *            表达式中的参数的值
		 * @param precondition
		 *            先决条件，当为true时才会增加查询条件，比如 id != null
		 */
		public void addExpression(String operator, String expression, Object arg, boolean precondition) {
			addExpression(operator, expression, arg, Integer.MIN_VALUE, precondition);
		}

		/**
		 * 增加查询条件
		 * 
		 * @author Lucifer Wong
		 * @param operator
		 *            操作，比如：AND、OR
		 * @param expression
		 *            表达式，比如：id=?
		 * @param arg
		 *            表达式中的参数的值
		 * @param argType
		 *            表达式中的参数的类型
		 * @param precondition
		 *            先决条件，当为true时才会增加查询条件，比如 id != null
		 */
		public void addExpression(String operator, String expression, Object arg, int argType, boolean precondition) {
			if (precondition) {
				if (isFirst) {
					if (hasWhere) {
						if (!sql.toString().toLowerCase().endsWith("where")) {
							sql.append(" " + operator);
						}
					} else {
						sql.append(" WHERE");
					}
					isFirst = false;
				} else {
					sql.append(" " + operator);
				}

				sql.append(" " + expression);

				if (arg != null) {
					args.add(arg);
				}

				if (argType != Integer.MIN_VALUE) {
					argTypes.add(argType);
				}
			}
		}

		/**
		 * 增加AND查询条件
		 * 
		 * @author Lucifer Wong
		 * @param expression
		 *            表达式
		 * @param precondition
		 *            先决条件
		 */
		public void and(String expression, boolean precondition) {
			addExpression("AND", expression, precondition);
		}

		/**
		 * 增加AND查询条件
		 * 
		 * @author Lucifer Wong
		 * @param expression
		 *            表达式
		 * @param arg
		 *            参数的值
		 * @param precondition
		 *            先决条件
		 */
		public void and(String expression, Object arg, boolean precondition) {
			addExpression("AND", expression, arg, precondition);
		}

		/**
		 * 增加AND查询条件
		 * 
		 * @author Lucifer Wong
		 * @param expression
		 *            表达式
		 * @param arg
		 *            参数的值
		 * @param argType
		 *            参数的类型
		 * @param precondition
		 *            先决条件
		 */
		public void and(String expression, Object arg, int argType, boolean precondition) {
			addExpression("AND", expression, arg, argType, precondition);
		}

		/**
		 * 增加 AND IN 查询条件，比如AND id IN (?, ?, ?);
		 * 
		 * @author Lucifer Wong
		 * @param columnName
		 *            列名称，比如 id
		 * @param args
		 *            参数的值数组，比如 new String[] {"1", "2", "3"}
		 * @param argType
		 *            参数的类型
		 * @param precondition
		 *            先决条件
		 */
		public void andIn(String columnName, Object[] args, int argType, boolean precondition) {
			if (precondition && args.length > 0) {
				if (isFirst) {
					if (hasWhere) {
						if (!sql.toString().toLowerCase().endsWith("where")) {
							sql.append(" AND");
						}
					} else {
						sql.append(" WHERE");
					}
					sql.append(" ");
					isFirst = false;
				} else {
					sql.append(" AND ");
				}

				sql.append(columnName);
				sql.append(" IN ");
				sql.append(JdbcUtil.getInSQL(args.length));
				for (int i = 0; i < args.length; i++) {
					this.args.add(args[i]);
					argTypes.add(argType);
				}
			}
		}

		/**
		 * 取得所有参数的值数组
		 * 
		 * @author Lucifer Wong
		 * @return 所有参数的值数组
		 */
		public Object[] getArgs() {
			return args.toArray();
		}

		/**
		 * 取得所有参数的类型数组
		 * 
		 * @author Lucifer Wong
		 * @return 所有参数的类型数组
		 */
		public int[] getArgTypes() {
			Integer[] objectTypes = argTypes.toArray(new Integer[argTypes.size()]);
			int[] intTypes = new int[objectTypes.length];
			for (int i = 0; i < objectTypes.length; i++) {
				intTypes[i] = objectTypes[i].intValue();
			}
			return intTypes;
		}

		/**
		 * 取得最后生成查询sql
		 * 
		 * @author Lucifer Wong
		 * @return 查询sql
		 */
		public String getSQL() {
			return sql.toString();
		}

		/**
		 * 添加 GROUP BY 语句。
		 * 
		 * @author Lucifer Wong
		 * @param columnNames
		 *            列名
		 */
		public void groupBy(String... columnNames) {
			if (ValidatorUtil.isEmpty((Object[]) columnNames)) {
				return;
			}

			sql.append(" GROUP BY ");
			for (String columnName : columnNames) {
				sql.append(columnName).append(", ");
			}
			sql.delete(sql.length() - 2, sql.length() - 1);
		}

		/**
		 * 增加OR查询条件
		 * 
		 * @author Lucifer Wong
		 * @param expression
		 *            表达式
		 * @param precondition
		 *            先决条件
		 */
		public void or(String expression, boolean precondition) {
			addExpression("OR", expression, precondition);
		}

		/**
		 * 增加OR查询条件
		 * 
		 * @author Lucifer Wong
		 * @param expression
		 *            表达式
		 * @param arg
		 *            参数的值
		 * @param precondition
		 *            先决条件
		 */
		public void or(String expression, Object arg, boolean precondition) {
			addExpression("OR", expression, arg, precondition);
		}

		/**
		 * 增加OR查询条件
		 * 
		 * @author Lucifer Wong
		 * @param expression
		 *            表达式
		 * @param arg
		 *            参数的值
		 * @param argType
		 *            参数的类型
		 * @param precondition
		 *            先决条件
		 */
		public void or(String expression, Object arg, int argType, boolean precondition) {
			addExpression("OR", expression, arg, argType, precondition);
		}

		/**
		 * 升序排序
		 * 
		 * @author Lucifer Wong
		 * @param columnName
		 *            列名
		 */
		public void orderBy(String columnName) {
			orderBy(columnName, false);
		}

		/**
		 * 排序
		 * 
		 * @author Lucifer Wong
		 * @param columnName
		 *            列名
		 * @param isDesc
		 *            是否降序
		 */
		public void orderBy(String columnName, boolean isDesc) {
			if (!hasOrderBy) {
				sql.append(" ORDER BY ");
			} else {
				sql.append(", ");
			}

			sql.append(columnName);
			if (isDesc) {
				sql.append(" DESC");
			}

			hasOrderBy = true;
		}

		/**
		 * 降序排序
		 * 
		 * @author Lucifer Wong
		 * @param columnName
		 *            列名
		 */
		public void orderByDesc(String columnName) {
			orderBy(columnName, true);
		}

	}

	/**
	 * 控制带 IN 子句的 SQL 语句中 IN 子句参数数目最多为 300 个（ASE 最大限制数），若超出就分批执行。<br>
	 * 用于解决带 IN 子句的 SQL 中 IN 子句参数数目超出最大限制数时出错的问题。
	 * 
	 * @author Lucifer Wong
	 * @param inSQL
	 *            带 "IN" 的 sql 语句, e.g. SELECT * FROM table_name WHERE
	 *            field_name IN
	 * @param inArgs
	 *            IN 子句中的所有参数
	 * @param otherArgs
	 *            其他参数
	 * @param processor
	 *            处理每次 SQL 执行结果的接口，例如实现中可获取每次查询结果，然后将它们累加
	 */
	public static void executeInSQL(String inSQL, Object[] inArgs, Object[] otherArgs, InSQLProcessor processor) {
		if (inArgs == null || inArgs.length == 0) {
			return;
		}

		// IN子句中最多允许的参数数目
		int inArgsMaxNum = 300;

		// 其他参数数目
		int otherArgsNum = (otherArgs == null) ? 0 : otherArgs.length;

		// 查询执行的次数
		int execNum = (inArgs.length % inArgsMaxNum == 0) ? inArgs.length / inArgsMaxNum
				: inArgs.length / inArgsMaxNum + 1;

		// 分批执行SQL
		for (int i = 0; i < execNum; i++) {
			// 每次执行SQL时IN子句中的参数数目
			// 如果是最后一次执行的SQL, 参数计算有区别
			int inArgsNum = ((i + 1) == execNum) ? inArgs.length - inArgsMaxNum * i : inArgsMaxNum;

			String sql = inSQL + JdbcUtil.getInSQL(inArgsNum); // 产生sql语句

			int count = inArgsNum + otherArgsNum; // 每次执行时总的参数数目
			Object[] args = new Object[count]; // 总的参数

			// 初始化其他参数
			for (int j = 0; j < otherArgsNum; j++) {
				args[j] = otherArgs[j];
			}

			// 每次执行时IN子句中第一个参数在数组中的索引
			int startParamIndex = inArgsMaxNum * i;

			// 初始化IN子句参数
			for (int j = otherArgsNum; j < count; j++) {
				// 注意索引
				args[j] = inArgs[startParamIndex + j - otherArgsNum];
			}

			processor.executeSQL(sql, args); // Call back
		}
	}

	/**
	 * 从记录集中获得指定列的值
	 * 
	 * @author Lucifer Wong
	 * @param columnIndex
	 *            列序号，从1开始
	 * @param argType
	 *            列的类型
	 * @param rs
	 *            记录集
	 * @return 指定列的值
	 * @throws SQLException
	 *             空指针 或者 失败转化
	 */
	public static Object getColumnValueFromResultSet(int columnIndex, int argType, ResultSet rs) throws SQLException {
		switch (argType) {
		case Types.INTEGER:
			return Integer.valueOf(rs.getInt(columnIndex));
		case Types.BOOLEAN:
			return Boolean.valueOf(rs.getBoolean(columnIndex));
		case Types.FLOAT:
			return new Float(rs.getFloat(columnIndex));
		case Types.DOUBLE:
			return new Double(rs.getDouble(columnIndex));
		case Types.CHAR:
		case Types.VARCHAR:
			return rs.getString(columnIndex);
		case Types.DATE:
			return rs.getDate(columnIndex);
		case Types.TIMESTAMP:
			return rs.getTimestamp(columnIndex);
		default:
			return rs.getObject(columnIndex);
		}
	}

	/**
	 * 取得执行count的sql
	 * 
	 * @author Lucifer Wong
	 * @param sql
	 *            执行查询的sql
	 * @return 执行count的sql
	 */
	public static String getCountSQL(String sql) {
		String normalSql = sql;
		String lowerCaseSql = sql.toLowerCase();

		int index = lowerCaseSql.indexOf(" order ");
		if (index != -1) {
			normalSql = normalSql.substring(0, index);
			lowerCaseSql = normalSql.toLowerCase();
		}

		int fromIndex = StringUtil.getFirstPairIndex(lowerCaseSql, "select ", " from ");
		if (fromIndex == -1) {
			throw new IllegalArgumentException("Could not get count sql[" + sql + "]");
		}

		int groupByIndex = StringUtil.getFirstPairIndex(lowerCaseSql, " group ", " by ");
		if (groupByIndex != -1 || lowerCaseSql.contains(" union ")) {
			return "SELECT COUNT(1) FROM (" + normalSql + ") temp_rs";
		} else {
			return "SELECT COUNT(1)" + normalSql.substring(fromIndex);
		}
	}

	/**
	 * 根据参数个数生成IN括弧里面的部分sql，包含括弧
	 * 
	 * @author Lucifer Wong
	 * @param size
	 *            参数个数
	 * @return IN括弧里面的部分sql
	 */
	public static String getInSQL(int size) {
		StringBuilder inSQL = new StringBuilder();

		inSQL.append("(");
		for (int i = 0; i < size; i++) {
			if (i == 0) {
				inSQL.append("?");
			} else {
				inSQL.append(",?");
			}
		}
		inSQL.append(")");

		return inSQL.toString();
	}

	/**
	 * 取得填充参数后的sql
	 * 
	 * @author Lucifer Wong
	 * @param preparedSQL
	 *            预编译sql
	 * @param args
	 *            参数数组
	 * @return 填充参数后的sql
	 */
	public static String getSQL(String preparedSQL, Object[] args) {
		if (args == null || args.length == 0) {
			return preparedSQL;
		}

		StringBuilder sql = new StringBuilder();

		int index = 0;
		int parameterIndex = 0;

		while ((index = preparedSQL.indexOf('?')) > 0) {
			sql.append(preparedSQL.substring(0, index));
			preparedSQL = preparedSQL.substring(index + 1);

			Object arg = args[parameterIndex++];

			if (arg == null) {
				sql.append("null");
			} else if (arg instanceof String) {
				sql.append("'");
				sql.append(arg);
				sql.append("'");
			} else if (arg instanceof java.util.Date) {
				sql.append("'");
				sql.append(DateUtil.date2String((java.util.Date) arg));
				sql.append("'");
			} else {
				sql.append(arg);
			}
		}

		sql.append(preparedSQL);

		return sql.toString();
	}

	/**
	 * 将参数以Object类型填入预制式sql语句中.
	 * 
	 * @author Lucifer Wong
	 * @param args
	 *            参数
	 * @param ps
	 *            预制式sql语句对象
	 * @throws SQLException
	 *             空指针 或者 失败转化
	 */
	public static void setParamsToStatement(Object[] args, PreparedStatement ps) throws SQLException {
		// Set the parameters
		for (int i = 0, index; i < args.length; i++) {
			index = i + 1;
			if (args[i] instanceof java.util.Date) {
				args[i] = new Timestamp(((java.util.Date) args[i]).getTime());
			}
			ps.setObject(index, args[i]);
		}
	}

	/**
	 * 将参数以合适的类型填入预制式sql语句中.
	 * 
	 * @author Lucifer Wong
	 * @param args
	 *            参数
	 * @param argTypes
	 *            参数类型
	 * @param ps
	 *            预制式sql语句对象
	 * @throws SQLException
	 *             空指针 或者 失败转化
	 */
	public static void setSuitedParamsToStatement(Object[] args, int[] argTypes, PreparedStatement ps)
			throws SQLException {
		for (int i = 0, index; i < args.length; i++) {
			index = i + 1;

			if (args[i] == null) {
				ps.setNull(index, argTypes[i]);
				continue;
			}

			switch (argTypes[i]) {

			case Types.INTEGER:
				ps.setInt(index, ((Integer) args[i]).intValue());
				break;
			case Types.BOOLEAN:
				ps.setBoolean(index, ((Boolean) args[i]).booleanValue());
				break;
			case Types.FLOAT:
				ps.setFloat(index, ((Float) args[i]).floatValue());
				break;
			case Types.DOUBLE:
				ps.setDouble(index, ((Double) args[i]).doubleValue());
				break;
			case Types.CHAR:
			case Types.VARCHAR:
				ps.setString(index, (String) args[i]);
				break;
			case Types.DATE:
				ps.setDate(index, new Date(((java.util.Date) args[i]).getTime()));
				break;
			case Types.TIMESTAMP:
				ps.setTimestamp(index, new Timestamp(((java.util.Date) args[i]).getTime()));
				break;

			default:
				ps.setObject(index, args[i]);
				break;
			}
		}
	}

}
