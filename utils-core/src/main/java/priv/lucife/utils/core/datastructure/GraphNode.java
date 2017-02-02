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

import java.util.ArrayList;
import java.util.List;

import priv.lucife.utils.core.annotation.UBTDS;

/**
 * 图的节点
 * 
 * @author Lucifer Wong
 */
@UBTDS
public class GraphNode<T> {
	T data;
	List<GraphNode<T>> neighborList;
	boolean visited;

	public GraphNode(T data) {
		this.data = data;
		neighborList = new ArrayList<GraphNode<T>>();
		visited = false;
	}

	public boolean equals(GraphNode<T> node) {
		return this.data.equals(node.data);
	}

	/**
	 * 还原图中所有节点为未访问
	 */
	public void restoreVisited() {
		restoreVisited(this);
	}

	/**
	 * 还原node的图所有节点为未访问
	 * 
	 * @author Lucifer Wong
	 * @param node
	 *            节点
	 */
	private void restoreVisited(GraphNode<T> node) {
		if (node.visited) {
			node.visited = false;
		}

		List<GraphNode<T>> neighbors = node.neighborList;
		for (int i = 0; i < neighbors.size(); i++) {
			restoreVisited(neighbors.get(i));
		}

	}
}
