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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import priv.lucife.utils.core.annotation.UBTCompatible;

/**
 * 数组工具类
 * 
 * @author Lucifer Wong
 */
@UBTCompatible
public abstract class ArrayUtil {

	/**
	 * 清除字符串数组中的null
	 * 
	 * @author Lucifer Wong
	 * @param array
	 *            字符串数组
	 * @return 清除null后的字符串数组
	 */
	public static String[] clearNull(String[] array) {
		ArrayList<String> list = new ArrayList<String>();
		for (int i = 0; i < array.length; i++) {
			if (array[i] != null) {
				list.add(array[i]);
			}
		}
		return toArray(list, null);
	}

	/**
	 * 联合两个数组
	 * 
	 * @author Lucifer Wong
	 * @param first
	 *            第一个数组
	 * @param last
	 *            另一个数组
	 * @return 内容合并后的数组
	 */
	public static Object[] combine(Object[] first, Object[] last) {
		if (first.length == 0 && last.length == 0) {
			return null;
		}
		Object[] result = new Object[first.length + last.length];
		System.arraycopy(first, 0, result, 0, first.length);
		System.arraycopy(last, 0, result, first.length, last.length);
		return result;
	}

	/**
	 * 判断字符串数组是否包含指定的字符串
	 * 
	 * @author Lucifer Wong
	 * @param array
	 *            字符串数组
	 * @param str
	 *            指定的字符串
	 * @return 包含true，否则false
	 */
	public static boolean contains(String[] array, String str) {

		if (ValidatorUtil.isEmpty((Object[]) array))
			return false;
		for (int i = 0; i < array.length; i++)
			if (array[i].equals(str))
				return true;
		return false;
	}

	/**
	 * 得到数组指定位置的子数组
	 * 
	 * @author Lucifer Wong
	 * @param array
	 *            指定数组
	 * @param start
	 *            起点
	 * @param end
	 *            终点
	 * @param <T>
	 *            typeValue
	 * @return 子数组
	 */
	public static <T> T[] getArraySubset(T[] array, int start, int end) {
		return Arrays.copyOfRange(array, start, end);
	}

	/**
	 * 取得数组的第一个元素
	 * 
	 * @author Lucifer Wong
	 * @param args
	 *            数组
	 * @param defaultValue
	 *            失败的返回值
	 * @return 数组的第一个元素
	 */
	public static Object getFirst(Object[] args, Object defaultValue) {
		if (ValidatorUtil.isEmpty(args))
			return defaultValue;
		return args[0];
	}

	/**
	 * 取得字符串数组的第一个元素
	 * 
	 * @author Lucifer Wong
	 * @param args
	 *            字符串数组
	 * @param defaultValue
	 *            失败的返回值
	 * @return 字符串数组的第一个元素
	 */
	public static String getFirst(String[] args, String defaultValue) {
		if (ValidatorUtil.isEmpty((Object[]) args))
			return defaultValue;
		return args[0];
	}

	/**
	 * 判断字符串数组是否有不为Empty的值
	 * 
	 * @author Lucifer Wong
	 * @param args
	 *            字符串数组
	 * @return 有true，否则false
	 */
	public static boolean hasEmptyValue(String[] args) {
		if (ValidatorUtil.isEmpty((Object[]) args))
			return false;
		for (int i = 0, length = args.length; i < length; i++)
			if (!ValidatorUtil.isEmpty(args[i]))
				return true;
		return false;
	}

	/**
	 * 移除数组指定位置上的元素
	 * 
	 * @author Lucifer Wong
	 * @param array
	 *            被指定的数组
	 * @param pos
	 *            指定位置
	 * 
	 * @param <T>
	 *            typeValue
	 * @return 移除数组指定位置上的元素
	 */
	public static <T> T[] removeItem(T[] array, int pos) {
		int length = Array.getLength(array);
		@SuppressWarnings("unchecked")
		T[] dest = (T[]) Array.newInstance(array.getClass().getComponentType(), length - 1);

		System.arraycopy(array, 0, dest, 0, pos);
		System.arraycopy(array, pos + 1, dest, pos, length - pos - 1);
		return dest;
	}

	/**
	 * 使用循环的方式实现二分查找
	 * 
	 * @author Lucifer Wong
	 * @param array
	 *            数组
	 * @param value
	 *            要查找的值
	 * @return 找到的话就返回 找不到就null
	 */
	public static Integer searchCirculation(int[] array, int value) {
		int low = 0;
		int high = array.length - 1;
		int middle;

		while (low <= high) {
			middle = (low + high) / 2;
			if (value < array[middle]) {
				high = middle - 1;
			} else if (value > array[middle]) {
				low = middle + 1;
			} else {
				return array[middle];
			}
		}

		return null;
	}

	/**
	 * 使用递归的方式实现二分查找
	 * 
	 * @author Lucifer Wong
	 * @param array
	 *            数组
	 * @param value
	 *            要查找的值
	 * @return 找到的话就返回 找不到就null
	 * 
	 */
	public static Integer searchRecursive(int[] array, int value) {
		return searchRecursive(array, value, 0, array.length - 1);
	}

	private static Integer searchRecursive(int[] array, int value, int low, int high) {
		if (high < low) {
			return null;
		}

		int middle = (low + high) / 2;

		if (value < array[middle]) {
			return searchRecursive(array, value, low, middle - 1);
		} else if (value > array[middle]) {
			return searchRecursive(array, value, middle + 1, high);
		} else {
			return array[middle];
		}
	}

	/**
	 * 求一个数组的长度 避免空指针
	 * 
	 * @author Lucifer Wong
	 * @param array
	 *            数组
	 * @return 数组长度
	 */
	public static int sizeof(final Object array) {
		return array == null ? 0 : Array.getLength(array);
	}

	/**
	 * 把List转换成字符串数组
	 * 
	 * @author Lucifer Wong
	 * @param args
	 *            字符串List
	 * @param defaultValue
	 *            转化失败的内定值
	 * @return 字符串数组
	 */
	public static String[] toArray(List<String> args, String[] defaultValue) {
		if (ValidatorUtil.isEmpty(args))
			return defaultValue;
		return args.toArray(new String[args.size()]);
	}

	/**
	 * 把Set转换成字符串数组
	 * 
	 * @param defaultValue
	 *            转化失败的内定值
	 * @author Lucifer Wong
	 * @param args
	 *            字符串Set
	 * @return 字符串数组
	 */
	public static String[] toArray(Set<String> args, String[] defaultValue) {
		if (ValidatorUtil.isEmpty(args))
			return defaultValue;
		return args.toArray(new String[args.size()]);
	}

	/**
	 * 把数组转换成 列表，如果数组为 null，则会返回一个空列表。
	 * 
	 * @author Lucifer Wong
	 * @param array
	 *            数组
	 * @return 列表对象
	 */
	public static List<Object> toList(Object[] array) {
		ArrayList<Object> list = new ArrayList<Object>();
		if (array == null) {
			return list;
		}

		for (int i = 0; i < array.length; i++) {
			list.add(array[i]);
		}
		return list;
	}

	/**
	 * 将数组以","隔开，形成字符串
	 * 
	 * @author Lucifer Wong
	 * @param args
	 *            字符串数组
	 * @return 字符串数组的内容
	 */
	public static String toString(String[] args) {
		return toString(args, ",", null);
	}

	/**
	 * 将数组以","隔开，形成字符串
	 * 
	 * @author Lucifer Wong
	 * @param args
	 *            字符串数组
	 * @param defaultValue
	 *            转换失败 的返回值
	 * @return 字符串数组的内容
	 */
	public static String toString(String[] args, String defaultValue) {
		return toString(args, ",", defaultValue);
	}

	/**
	 * @author Lucifer Wong
	 * @param args
	 *            字符串数组
	 * @param separator
	 *            分隔符
	 * @param defaultValue
	 *            转换失败的返回值
	 * @return 符串数组的内容
	 */
	public static String toString(String[] args, String separator, String defaultValue) {
		if (ValidatorUtil.isEmpty((Object[]) args))
			return defaultValue;
		StringBuilder buffer = new StringBuilder();
		for (int i = 0; i < args.length; i++) {
			if (i > 0)
				buffer.append(separator);
			buffer.append(args[i]);
		}
		return buffer.toString();
	}

	/**
	 * 冒泡排序<br>
	 * 时间复杂度: 平均情况与最差情况都是O(n^2)<br>
	 * 空间复杂度: O(1)
	 * 
	 * @author Lucifer Wong
	 * @param array
	 *            排序的数组
	 */
	public void bubbleSort(int[] array) {
		int temp = 0;
		for (int i = 0; i < array.length; i++) {
			for (int j = 0; j < array.length - 1; j++) {
				if (array[j] > array[j + 1]) {
					temp = array[j];
					array[j] = array[j + 1];
					array[j + 1] = temp;
				}
			}
		}
	}

	/**
	 * 使用二分查找法返回插入的位置
	 * 
	 * @author Lucifer Wong
	 * @param array
	 *            排序的数组
	 * @param value
	 *            插入的值
	 * @return 排序后数组
	 */
	private int getInsertIndex(int[] array, int length, int value) {
		int low = 0;
		int high = length - 1;
		int middle = -1;

		while (low <= high) {
			middle = (low + high) / 2;

			if (array[middle] > value) {
				high = middle - 1;
			} else if (array[middle] < value) {
				low = middle + 1;
			} else {
				return middle;
			}
		}

		if (array[middle] <= value) {
			middle++;
		}

		return middle;
	}

	/**
	 * 优化的插入排序实现
	 * 
	 * @author Lucifer Wong
	 * @param array
	 *            排序的数组
	 */
	public void insertOptimizeSort(int[] array) {
		for (int i = 1; i < array.length; i++) {
			int index = getInsertIndex(array, i, array[i]);
			if (i != index) {
				int j = i;
				int temp = array[i];
				while (j > index) {
					array[j] = array[j - 1];
					j--;
				}

				array[j] = temp;
			}
		}
	}

	/**
	 * 插入排序实现
	 * 
	 * @author Lucifer Wong
	 * @param array
	 *            排序的数组
	 */
	public void insertSort(int[] array) {

		for (int i = 1; i < array.length; i++) {
			int temp = array[i];
			int j = i - 1;
			while (j >= 0 && array[j] > temp) {
				array[j + 1] = array[j];
				j--;
			}

			if (j != i - 1) {
				array[j + 1] = temp;
			}
		}
	}

	/**
	 * 归并array
	 * 
	 * @author Lucifer Wong
	 * @param array
	 *            排序的数组
	 * @param low
	 *            低位
	 * @param middle
	 *            中间
	 * @param high
	 *            高位
	 */
	private void merge(int[] array, int low, int middle, int high) {
		// 辅助数组
		int[] helper = new int[array.length];
		for (int i = 0; i <= high; i++) {
			helper[i] = array[i];
		}

		int helperLeft = low;
		int helperRight = middle + 1;
		int current = low;

		/**
		 * 迭代访问helper数组，比较左右两半元素 并将较小的元素复制到原先的数组中
		 */
		while (helperLeft <= middle && helperRight <= high) {
			if (helper[helperLeft] <= helper[helperRight]) {
				array[current] = helper[helperLeft];
				helperLeft++;
			} else {
				array[current] = helper[helperRight];
				helperRight++;
			}
			current++;
		}

		/**
		 * 将数组左半剩余元素复制到原先的数组中
		 */
		int remaining = middle - helperLeft;
		for (int i = 0; i <= remaining; i++) {
			array[current + i] = helper[helperLeft + i];
		}
	}

	/**
	 * 归并排序<br>
	 * 时间复杂度: 平均情况与最差情况都是O(nlog(n))<br>
	 * 空间复杂度: It Depends
	 * 
	 * @author Lucifer Wong
	 * @param array
	 *            排序的数组
	 */
	public void mergeSort(int[] array) {
		mergeSort(array, 0, array.length - 1);
	}

	/**
	 * 从索引low到high归并排序数组array 归并排序<br>
	 * 时间复杂度: 平均情况与最差情况都是O(nlog(n))<br>
	 * 空间复杂度: It Depends
	 * 
	 * @author Lucifer Wong
	 * @param array
	 *            排序的数组
	 * @param low
	 *            索引low
	 * @param high
	 *            索引high
	 */
	public void mergeSort(int[] array, int low, int high) {
		if (low < high) {
			int middle = (low + high) / 2;
			mergeSort(array, low, middle);
			mergeSort(array, middle + 1, high);

			merge(array, low, middle, high);
		}
	}

	/**
	 * 找出一个基准点，排列数组array左边的都小于它，右边的都大于它
	 * 
	 * @author Lucifer Wong
	 * @param array
	 *            排序的数组
	 * @param left
	 *            左侧大小
	 * @param right
	 *            右侧大小
	 * @return 基准值数组索引
	 */
	private int partition(int[] array, int left, int right) {
		int pivot = array[(left + right) / 2];
		int temp;

		while (left < right) {
			while (array[left] < pivot)
				left++;
			while (array[right] > pivot)
				right--;

			if (left < right) {
				temp = array[left];
				array[left] = array[right];
				array[right] = temp;
				left++;
				right--;
			}
		}

		return left;
	}

	/**
	 * 快速排序<br>
	 * 时间复杂度: 平均情况是O(nlog(n)),最差情况是O(n^2)<br>
	 * 空间复杂度: O(nlog(n))
	 * 
	 * @author Lucifer Wong
	 * @param array
	 *            排序的数组
	 */
	public void quickSort(int[] array) {
		quickSort(array, 0, array.length - 1);
	}

	/**
	 * 从left到right排序数组array 快速排序<br>
	 * 时间复杂度: 平均情况是O(nlog(n)),最差情况是O(n^2)<br>
	 * 空间复杂度: O(nlog(n))
	 * 
	 * @author Lucifer Wong
	 * @param array
	 *            排序的数组
	 * @param left
	 *            left
	 * @param right
	 *            right
	 */
	public void quickSort(int[] array, int left, int right) {
		int index = partition(array, left, right);
		if (left < index - 1) {
			quickSort(array, left, index - 1);
		}
		if (index + 1 < right) {
			quickSort(array, index, right);
		}
	}

	/**
	 * 选择排序<br>
	 * 时间复杂度: 平均情况与最差情况都是O(n^2)<br>
	 * 空间复杂度: O(1)
	 * 
	 * @author Lucifer Wong
	 * @param array
	 *            排序的数组
	 */
	public void selectionSort(int[] array) {
		int temp = 0;

		for (int i = 0; i < array.length; i++) {
			temp = array[i];
			for (int j = i; j < array.length; j++) {
				if (temp > array[j]) {
					temp = array[j];
				}
			}

			if (temp != array[i]) {
				array[i] = temp;
			}
		}
	}
}
