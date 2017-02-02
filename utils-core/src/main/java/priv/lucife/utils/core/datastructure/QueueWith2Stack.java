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
 * 使用两个栈实现队列
 * 
 * @author Lucifer Wong
 *
 */
@UBTDS
public class QueueWith2Stack<T> {
	Stack<T> stackOldest, stackNewest;

	public QueueWith2Stack() {
		stackOldest = new Stack<T>();
		stackNewest = new Stack<T>();
	}

	public void enqueue(T item) {
		stackNewest.push(item);
	}

	public T dequeue() {
		shiftStacks();
		return stackOldest.pop();
	}

	public T peek() {
		shiftStacks();
		return stackOldest.peek();
	}

	/**
	 * 如果旧栈已空，将新栈所有数据压入旧栈
	 * 
	 * @author Lucifer Wong
	 */
	private void shiftStacks() {
		if (stackOldest.isEmpty()) {
			while (!stackNewest.isEmpty()) {
				stackOldest.push(stackNewest.pop());
			}
		}
	}

	public boolean isEmpty() {
		return stackOldest.isEmpty();
	}

}
