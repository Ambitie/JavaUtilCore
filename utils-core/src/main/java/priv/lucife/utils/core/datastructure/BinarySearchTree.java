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
 * 二叉查找树的判断<br>
 * 二叉查找树:对所有节点来说:所有左子树节点都小于等于其根节点;所有右子树节点都大于其根节点
 * 
 * @author Lucifer Wong
 * @see TreeNode
 */
@UBTDS
public class BinarySearchTree {

	/**
	 * Check root左子树所有节点小于等于max,root右子树所有节点大于min
	 * 
	 * @author Lucifer Wong
	 * @param root
	 *            根节点
	 * @param min
	 *            最小值
	 * @param max
	 *            最大值
	 * @return cheched true ,unchecked false
	 */
	private static boolean checkBSTMinMax(TreeNode<Integer> root, Integer min, Integer max) {
		if (root == null) {
			return true;
		}

		if (root.data > max || root.data <= min) {
			return false;
		}

		if (!checkBSTMinMax(root.left, min, root.data) || !checkBSTMinMax(root.right, root.data, max)) {
			return false;
		}

		return true;
	}

	/**
	 * Check root是否二叉查找树
	 * 
	 * @author Lucifer Wong
	 * @param root
	 *            根节点
	 * @return 是 TRUE，不是 FALSE
	 */
	public static boolean checkBST(TreeNode<Integer> root) {
		return checkBSTMinMax(root, Integer.MIN_VALUE, Integer.MAX_VALUE);
	}

}
