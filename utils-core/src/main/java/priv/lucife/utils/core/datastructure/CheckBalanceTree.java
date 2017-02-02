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
 * 检查是否平衡树,此处平衡树的定义是两棵子树的高度差不超过1
 * 
 * @author Lucifer Wong
 * @see TreeNode
 */
@UBTDS
public class CheckBalanceTree {

	/**
	 * 返回树的高度
	 * @param root
	 *            根节点
	 * @return 树的高度
	 */
	private static int getHeight(TreeNode<Integer> root) {
		if (root == null) {
			return 0;
		}

		return Math.max(getHeight(root.left), getHeight(root.right)) + 1;
	}

	/**
	 * 判断树是否平衡<br>
	 * 此方法时间复杂度为O(NlogN),效率不高
	 * 
	 * @param root
	 *            根节点
	 * @return 判断树是否平衡
	 */
	public static boolean isBalanced(TreeNode<Integer> root) {
		if (root == null) {
			return true;
		}

		int heightDiff = getHeight(root.left) - getHeight(root.right);
		if (Math.abs(heightDiff) > 1) {
			return false;
		} else {
			return isBalanced(root.left) && isBalanced(root.right);
		}
	}

	/**
	 * 检查树的高度,若子树不平衡直接返回-1
	 * 
	 * @param root
	 *            根节点
	 * @return 检查树的高度,若子树不平衡直接返回-1
	 */
	private static int checkHeight(TreeNode<Integer> root) {
		if (root == null) {
			return 0;
		}

		int leftHeight = checkHeight(root.left);
		if (leftHeight == -1) {
			return -1;
		}

		int rightHeight = checkHeight(root.right);
		if (rightHeight == -1) {
			return -1;
		}

		int heightDiff = leftHeight - rightHeight;
		if (Math.abs(heightDiff) > 1) {
			return -1;
		} else {
			return Math.max(leftHeight, rightHeight) + 1;
		}
	}

	/**
	 * 检查是否是平衡树
	 * 
	 * @author Lucifer Wong
	 * @param root
	 *            根节点
	 * @return true 平衡，false 不平衡
	 */
	public static boolean isBalancedGood(TreeNode<Integer> root) {
		if (checkHeight(root) == -1) {
			return false;
		} else {
			return true;
		}
	}

}
