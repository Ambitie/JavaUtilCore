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
 * 队列的实现
 * 
 * @author Lucifer Wong
 * @see Node
 */
@UBTDS
public class Queue<T> {
	Node<T> first, last;

	public void enqueue(T item) {
		if (first == null) {
			last = new Node<T>(item);
			first = last;
		} else {
			last.next = new Node<T>(item);
			last = last.next;
		}
	}

	public T dequeue() {
		if (first != null) {
			T item = first.data;
			first = first.next;
			return item;
		}

		return null;
	}

	public T peek() {
		if (first != null) {
			return first.data;
		}
		return null;
	}

	public boolean isEmpty() {
		return first == null;
	}

}
