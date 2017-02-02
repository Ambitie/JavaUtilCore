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
 * 查找两个节点的共通祖先
 * 
 * @author Lucifer Wong
 * @see TreeNode
 */
@UBTDS
public class CommonAncestorSearch {

	/**
	 * 判断p节点是否是root节点的子孙节点
	 * 
	 * @author Lucifer Wong
	 * 
	 * @param root
	 *            根节点
	 * @param p
	 *            节点p
	 * @return 是子孙TRUE 不是子孙FALSE
	 */
	private static boolean isDescendant(TreeNode<Integer> root, TreeNode<Integer> p) {
		if (root == null) {
			return false;
		}
		if (root == p) {
			return true;
		}

		return isDescendant(root.left, p) || isDescendant(root.right, p);
	}

	/**
	 * 返回节点p和节点q的第一个祖先节点<br>
	 * 时间复杂度为O(n)
	 * 
	 * @author Lucifer Wong
	 * @param root
	 *            根节点
	 * @param p
	 *            节点p
	 * @param q
	 *            节点q
	 * @return 节点p和节点q的第一个祖先节点
	 */
	public static TreeNode<Integer> searchCommonAncestor(TreeNode<Integer> root, TreeNode<Integer> p,
			TreeNode<Integer> q) {
		// 确保p和q都是root的子孙节点
		if (!isDescendant(root, p) || !isDescendant(root, q)) {
			return null;
		}

		if (root == q || root == q) {
			return root;
		}

		boolean is_p_on_left = isDescendant(root.left, p);
		boolean is_q_on_left = isDescendant(root.left, q);

		// 如果p和q在root的两边,则返回root
		if (is_p_on_left != is_q_on_left) {
			return root;
		}

		// 如果p和q在root的同一边,则遍历访问那一边
		TreeNode<Integer> treeSide = is_p_on_left ? root.left : root.right;
		return searchCommonAncestor(treeSide, p, q);
	}
}
