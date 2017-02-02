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
 * 栈的实现
 * 
 * @author Lucifer Wong
 * @param <T>
 *            typeValue
 * @see Node
 */
@UBTDS
public class Stack<T extends Object> {

	private Node<T> top;

	public T pop() {
		if (top != null) {
			T item = top.data;
			top = top.next;
			return item;
		}

		return null;
	}

	public void push(T item) {
		Node<T> t = new Node<T>(item);
		t.next = top;
		top = t;
	}

	public T peek() {
		if (top != null) {
			return top.data;
		}
		return null;
	}

	public boolean isEmpty() {
		return (top == null);
	}
}
