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
 * 图的广度优先搜索和深度优先搜索实现
 * 
 * @author Lucifer Wong
 * @see GraphNode
 */
@UBTDS
public class GraphSearch<T> {

	public StringBuffer searchPathDFS = new StringBuffer();
	public StringBuffer searchPathBFS = new StringBuffer();

	/**
	 * 深度优先搜索实现
	 * 
	 * @author Lucifer Wong
	 * @param root
	 *            根节点
	 */
	public void searchDFS(GraphNode<T> root) {
		if (root == null) {
			return;
		}

		// visited root
		if (searchPathDFS.length() > 0) {
			searchPathDFS.append("->");
		}
		searchPathDFS.append(root.data.toString());
		root.visited = true;

		for (GraphNode<T> node : root.neighborList) {
			if (!node.visited) {
				searchDFS(node);
			}
		}
	}

	/**
	 * 广度优先搜索实现,使用队列
	 * 
	 * @author Lucifer Wong
	 * @param root
	 *            根节点
	 */
	public void searchBFS(GraphNode<T> root) {

		Queue<GraphNode<T>> queue = new Queue<GraphNode<T>>();

		// visited root
		if (searchPathBFS.length() > 0) {
			searchPathBFS.append("->");
		}
		searchPathBFS.append(root.data.toString());
		root.visited = true;

		// 加到队列队尾
		queue.enqueue(root);

		while (!queue.isEmpty()) {
			GraphNode<T> r = queue.dequeue();
			for (GraphNode<T> node : r.neighborList) {
				if (!node.visited) {
					searchPathBFS.append("->");
					searchPathBFS.append(node.data.toString());
					node.visited = true;

					queue.enqueue(node);
				}
			}
		}
	}

}
