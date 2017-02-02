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
 * 实现树的前序,中序,后续遍历搜索
 * 
 * @author Lucifer Wong
 * @see TreeNode
 */
@UBTDS
public class TreeSearch<T> {
	StringBuffer searchPath = new StringBuffer();
	private boolean isSearched = false;

	/**
	 * 前序遍历root查询item
	 * 
	 * @author Lucifer Wong
	 * @param root
	 *            根节点
	 * @param data
	 *            被查找的子节点
	 */
	public void preorderTraversal(TreeNode<T> root, T data) {
		if (root == null) {
			return;
		}

		if (!isSearched) {
			if (!searchPath.toString().equals("")) {
				searchPath.append("->");
			}
			searchPath.append(root.data);
			if (root.data.equals(data))
				isSearched = true;
		}

		if (!isSearched)
			preorderTraversal(root.left, data);
		if (!isSearched)
			preorderTraversal(root.right, data);
	}

	/**
	 * 中序遍历root查询item
	 * 
	 * @author Lucifer Wong
	 * @param root
	 *            根节点
	 * @param data
	 *            被查找的子节点
	 */
	public void inorderTraversal(TreeNode<T> root, T data) {
		if (root == null) {
			return;
		}

		if (!isSearched)
			inorderTraversal(root.left, data);

		if (!isSearched) {
			if (!searchPath.toString().equals("")) {
				searchPath.append("->");
			}
			searchPath.append(root.data);
			if (root.data.equals(data))
				isSearched = true;
		}

		if (!isSearched)
			inorderTraversal(root.right, data);
	}

	/**
	 * 后续遍历root查询item
	 * 
	 * @author Lucifer Wong
	 * @param root
	 *            根节点
	 * @param data
	 *            被查找的子节点
	 */
	public void postorderTraversal(TreeNode<T> root, T data) {
		if (root == null) {
			return;
		}

		if (!isSearched)
			postorderTraversal(root.left, data);

		if (!isSearched)
			postorderTraversal(root.right, data);

		if (!isSearched) {
			if (!searchPath.toString().equals("")) {
				searchPath.append("->");
			}
			searchPath.append(root.data);
			if (root.data.equals(data))
				isSearched = true;
		}
	}
}
