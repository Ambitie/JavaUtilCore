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
package priv.lucife.utils.core.clone;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Collection;

import priv.lucife.utils.core.annotation.UBTCompatible;

/**
 * 克隆工具类，进行深克隆,包括对象、集合
 * 
 * @author Lucifer Wong
 */
@UBTCompatible
public abstract class CloneUtil {

	/**
	 * 采用对象的序列化完成对象的深克隆
	 * 
	 * @author Lucifer Wong
	 * @param obj
	 *            待克隆的对象
	 * @param <T>
	 *            typeValue
	 * @return 克隆的对象
	 * @throws Exception
	 *             转化的对象
	 */
	@SuppressWarnings("unchecked")
	public static <T extends Serializable> T cloneObject(T obj) throws Exception {
		T cloneObj = null;

		// 写入字节流
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		ObjectOutputStream obs = new ObjectOutputStream(out);
		obs.writeObject(obj);
		obs.close();
		// 分配内存，写入原始对象，生成新对象
		ByteArrayInputStream ios = new ByteArrayInputStream(out.toByteArray());
		ObjectInputStream ois = new ObjectInputStream(ios);
		// 返回生成的新对象
		cloneObj = (T) ois.readObject();
		ois.close();

		return cloneObj;
	}

	/**
	 * 利用序列化完成集合的深克隆
	 * 
	 * @author Lucifer Wong
	 * @param collection
	 *            待克隆的集合
	 * @param <T>
	 *            typeValue
	 * @return 克隆的集合
	 * @throws Exception
	 *             克隆失败
	 */
	@SuppressWarnings("unchecked")
	public static <T> Collection<T> cloneCollection(Collection<T> collection) throws Exception {
		ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
		ObjectOutputStream out = new ObjectOutputStream(byteOut);
		out.writeObject(collection);
		out.close();

		ByteArrayInputStream byteIn = new ByteArrayInputStream(byteOut.toByteArray());
		ObjectInputStream in = new ObjectInputStream(byteIn);
		Collection<T> dest = (Collection<T>) in.readObject();
		in.close();

		return dest;
	}
}
