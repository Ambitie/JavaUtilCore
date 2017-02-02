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
 * 一个能随时获取栈中最小值的栈的实现
 * 
 * @author Lucifer Wong
 * @see Node
 */
@UBTDS
public class StackWithMin extends Stack<Integer> {
	Stack<Integer> stackMin;

	public StackWithMin() {
		stackMin = new Stack<Integer>();
	}

	public void push(Integer item) {
		if (item <= min()) {
			stackMin.push(item);
		}
		super.push(item);
	}

	public Integer pop() {
		int value = super.pop();
		if (value == min()) {
			stackMin.pop();
		}
		return value;
	}

	/**
	 * 取得栈中的最小值
	 * 
	 * @author Lucifer Wong
	 * @return 栈中的最小值
	 */
	public Integer min() {
		if (stackMin.isEmpty()) {
			return Integer.MAX_VALUE;
		} else {
			return stackMin.peek();
		}
	}
}
