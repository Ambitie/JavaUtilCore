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
package priv.lucife.utils.core.datastructure;

import priv.lucife.utils.core.annotation.UBTDS;

/**
 * 将一个有序的数组变成一个最小高度的二叉查找树
 * 
 * @author Lucifer Wong
 */
@UBTDS
public class MinBinaryTree {

	/**
	 * 将一个有序的数组变成一个最小高度的二叉查找树
	 * 
	 * @author Lucifer Wong
	 * @param array
	 *            有序数组
	 * @param start
	 *            起点
	 * @param end
	 *            终点
	 * @return 最小高度的二叉查找树
	 */
	public static TreeNode<Integer> insertNodeFromArray(int[] array, int start, int end) {
		if (end < start) {
			return null;
		}

		int middle = (start + end) / 2;

		TreeNode<Integer> treeNode = new TreeNode<Integer>(array[middle]);

		treeNode.left = insertNodeFromArray(array, start, middle - 1);
		treeNode.right = insertNodeFromArray(array, middle + 1, end);

		return treeNode;
	}
}
