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

/**
 * 此类用来表示若干具有一定容量的栈的集合<br>
 * 当第一个栈push满后，push第二个栈，以此类推<br>
 * 当最后一个栈pop空后，pop倒数第二个栈，依次类推<br>
 * 
 * @author Lucifer Wong
 * @see Node
 */
public class StacksSet<T> {
	List<StackCapacity<T>> stacks = new ArrayList<StackCapacity<T>>();

	public static int index = -1;
	private int capacity;

	public StacksSet(int capacity) {
		this.capacity = capacity;
	}

	public void push(T item) {
		StackCapacity<T> last = getLastStack();
		if (last != null && !last.isFull()) {
			last.push(item);
		} else {
			StackCapacity<T> stack = new StackCapacity<T>(capacity);
			stack.push(item);
			stacks.add(stack);
			index++;
		}
	}

	public T pop() {
		StackCapacity<T> last = getLastStack();
		T value = null;
		if (last != null) {
			value = last.pop();
			if (last.getIndex() == 0) {
				stacks.remove(index--);
			}
		}
		return value;
	}

	public T peek() {
		StackCapacity<T> last = getLastStack();
		T value = null;
		if (last != null) {
			value = last.peek();
		}
		return value;
	}

	public boolean isEmpty() {
		StackCapacity<T> last = getLastStack();
		return (last == null);
	}

	/**
	 * 返回最后的栈
	 * 
	 * @author Lucifer Wong
	 * @return
	 */
	private StackCapacity<T> getLastStack() {
		if (stacks.size() == 0) {
			return null;
		} else {
			return stacks.get(index);
		}
	}

}
