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

import java.util.List;

import priv.lucife.utils.core.annotation.UBTDS;

/**
 * 给出有向图的两个节点判断两者之间是否有一路径
 * 
 * @author Lucifer Wong
 * @see GraphNode
 */
@UBTDS
public class DirectedGraphPathCheck {

	/**
	 * 利用深度优先搜索进行路径判断p - q 之间是否有路径
	 * 
	 * @author Lucifer Wong
	 * @param p
	 *            图节点p
	 * @param q
	 *            图节点q
	 * @return 有路径true 无路径false
	 */
	public static boolean pathCheckDFS(GraphNode<Integer> p, GraphNode<Integer> q) {
		boolean isFound = false;

		p.restoreVisited();
		isFound |= pathOrderCheckDFS(p, q);

		q.restoreVisited();
		isFound |= pathOrderCheckDFS(q, p);

		return isFound;
	}

	/**
	 * 利用深度优先搜索进行路径判断 p -> q 是否有路径
	 * 
	 * @author Lucifer Wong
	 * @param p
	 *            图节点p
	 * @param q
	 *            图节点q
	 * @return 有路径true 无路径false
	 */
	private static boolean pathOrderCheckDFS(GraphNode<Integer> p, GraphNode<Integer> q) {
		if (p.equals(q)) {
			return true;
		}

		boolean isFound = false;
		List<GraphNode<Integer>> pNeighborList = p.neighborList;
		for (int i = 0; i < pNeighborList.size(); i++) {
			GraphNode<Integer> neighbor = pNeighborList.get(i);
			if (!neighbor.visited) {
				neighbor.visited = true;
				if (neighbor.equals(q)) {
					return true;
				}

				isFound = isFound || pathOrderCheckDFS(neighbor, q);
			}
		}

		return isFound;
	}

	/**
	 * 利用广度优先搜索进行路径判断 p - q 之间是否有路径
	 * 
	 * @author Lucifer Wong
	 * @param p
	 *            图节点p
	 * @param q
	 *            图节点q
	 * @return 有路径true 无路径false
	 */
	public static boolean pathCheckBFS(GraphNode<Integer> p, GraphNode<Integer> q) {
		boolean isFound = false;

		p.restoreVisited();
		isFound |= pathOrderCheckBFS(p, q);

		q.restoreVisited();
		isFound |= pathOrderCheckBFS(q, p);

		return isFound;
	}

	/**
	 * 利用广度优先搜索进行路径判断 p -> q 是否有路径
	 * 
	 * @author Lucifer Wong
	 * @param p
	 *            图节点p
	 * @param q
	 *            图节点q
	 * @return 有路径true 无路径false
	 */
	private static boolean pathOrderCheckBFS(GraphNode<Integer> p, GraphNode<Integer> q) {
		Queue<GraphNode<Integer>> queue = new Queue<GraphNode<Integer>>();

		if (!p.visited && p.equals(q)) {
			return true;
		}

		p.visited = true;
		queue.enqueue(p);

		while (!queue.isEmpty()) {
			List<GraphNode<Integer>> neighbors = queue.dequeue().neighborList;
			for (int i = 0; i < neighbors.size(); i++) {
				GraphNode<Integer> neighbor = neighbors.get(i);

				if (!neighbor.visited && neighbor.equals(q)) {
					return true;
				}
				neighbor.visited = true;
				queue.enqueue(neighbor);
			}
		}

		return false;
	}
}
