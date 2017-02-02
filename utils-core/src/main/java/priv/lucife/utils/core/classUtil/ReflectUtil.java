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
package priv.lucife.utils.core.classUtil;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Proxy;
import java.lang.reflect.TypeVariable;
import java.util.ArrayList;

import priv.lucife.utils.core.annotation.UBTCompatible;
import priv.lucife.utils.core.base.ValidatorUtil;

/**
 * 反射工具类
 * 
 * @author Lucifer Wong
 */
@UBTCompatible
@SuppressWarnings({ "rawtypes", "unchecked" })
public abstract class ReflectUtil {

	/**
	 * Returns an instance of a proxy class for the specified interfaces that
	 * dispatches method invocations to the specified invocation handler.
	 * 
	 * @author Lucifer Wong
	 * @param intf
	 *            the interface for the proxy to implement
	 * @param h
	 *            the invocation handler to dispatch method invocations to
	 * @param <T>
	 *            anyType
	 * @return a proxy instance with the specified invocation handler of a proxy
	 *         class that is defined by the specified class loader and that
	 *         implements the specified interfaces
	 * @throws IllegalArgumentException
	 *             if any of the restrictions on the parameters that may be
	 *             passed to <code>getProxyClass</code> are violated
	 * @throws NullPointerException
	 *             if the <code>interfaces</code> array argument or any of its
	 *             elements are <code>null</code>, or if the invocation handler,
	 *             <code>h</code>, is <code>null</code>
	 */
	public static <T> T create(Class<T> intf, InvocationHandler h) {
		return create(h.getClass().getClassLoader(), intf, h);
	}

	/**
	 * Returns an instance of a proxy class for the specified interfaces that
	 * dispatches method invocations to the specified invocation handler.
	 * 
	 * @author Lucifer Wong
	 * @param loader
	 *            the class loader to define the proxy class
	 * @param intf
	 *            the interface for the proxy to implement
	 * @param <T>
	 *            anyType
	 * @param h
	 *            the invocation handler to dispatch method invocations to
	 * @return a proxy instance with the specified invocation handler of a proxy
	 *         class that is defined by the specified class loader and that
	 *         implements the specified interfaces
	 * @throws IllegalArgumentException
	 *             if any of the restrictions on the parameters that may be
	 *             passed to <code>getProxyClass</code> are violated
	 * @throws NullPointerException
	 *             if the <code>interfaces</code> array argument or any of its
	 *             elements are <code>null</code>, or if the invocation handler,
	 *             <code>h</code>, is <code>null</code>
	 */
	public static <T> T create(ClassLoader loader, Class<T> intf, InvocationHandler h) {
		return (T) Proxy.newProxyInstance(loader, new Class[] { intf }, h);
	}

	/**
	 * 从object中得到类
	 * 
	 * @author Lucifer Wong
	 * @param object
	 *            目标类的一个对象
	 * @return 从object中得到类
	 */
	public static Class<?> getClassFromObject(Object object) {
		if (ValidatorUtil.isEmpty(object))
			return null;
		return object.getClass();
	}

	/**
	 * 找出所有被实现的interfaces
	 * 
	 * @author Lucifer Wong
	 * @param classInstance
	 *            目标类的一个对象
	 * @return 找出所有被实现的interfaces
	 */
	public static ArrayList<String> getClassInterfaces(Class<?> classInstance) {
		if (ValidatorUtil.isEmpty(classInstance))
			return null;
		ArrayList<String> res = new ArrayList<String>();
		Class cc[];
		cc = classInstance.getInterfaces();
		for (Class cite : cc)
			res.add(cite.getName());
		return res;
	}

	/**
	 * 找出class隶属的package
	 * 
	 * @author Lucifer Wong
	 * @param classInstance
	 *            目标类的一个对象
	 * @return class隶属的package
	 */
	public static Package getClassPackage(Class<?> classInstance) {

		Package p;
		p = classInstance.getPackage();

		if (p != null)
			return p;
		else {
			return null;
		}

	}

	/**
	 * 得到一个类的父类
	 * 
	 * @author Lucifer Wong
	 * @param classInstance
	 *            目标类的一个对象
	 * @return 父类
	 */
	public static Class getClassSupperClasss(Class<?> classInstance) {
		if (ValidatorUtil.isEmpty(classInstance))
			return null;
		Class supClass = classInstance.getSuperclass();
		return supClass;

	}

	/**
	 * 返回所有的TypeVariable 。such as &lt;K&gt; &lt;V&gt; &lt;K,V,T&gt;
	 * 
	 * @author Lucifer Wong
	 * @param classInstance
	 *            目标类的一个对象
	 * @return the types of generic declaration that declared the underlying
	 *         type variable.
	 */
	public static TypeVariable<?>[] getClassTypeVariable(Class<?> classInstance) {
		if (ValidatorUtil.isEmpty(classInstance))
			return null;
		return classInstance.getTypeParameters();
	}

	/**
	 * 从类中得到全路径名
	 * 
	 * @author Lucifer Wong
	 * @param classInstance
	 *            目标类的一个对象
	 * @return 全路径名
	 */
	public static String getFullClassName(Class<?> classInstance) {
		if (ValidatorUtil.isEmpty(classInstance))
			return null;
		String name = classInstance.getName();
		return name;
	}

	/**
	 * 得到一个类的所有内部类
	 * 
	 * @author Lucifer Wong
	 * @param classInstance
	 *            目标类的一个对象
	 * @return 所有内部类
	 */
	public static Class<?>[] getInnerClasses(Class<?> classInstance) {
		if (ValidatorUtil.isEmpty(classInstance))
			return null;
		return classInstance.getDeclaredClasses();

	}

	/**
	 * 用指定参数列表产生一个对象
	 * 
	 * @author Lucifer Wong
	 * @param classInstance
	 *            目标类的一个对象
	 * @param params
	 *            指定参数列表
	 * @return 目标对象
	 * @throws Exception
	 *             找不到类
	 */
	public static Object getObjectFromClass(Class<?> classInstance, Object... params) throws Exception {

		Class<?>[] pTypes = getParasClass(params);

		Constructor<?> ctor = classInstance.getConstructor(pTypes);
		Object obj = null;
		obj = ctor.newInstance(params);
		System.out.println(obj);

		return obj;
	}

	/**
	 * 从一个包.类名 产生一个对象
	 * 
	 * @author Lucifer Wong
	 * @param classPath
	 *            类路径
	 * @return 目标对象
	 * @throws Exception
	 *             找不到类
	 */
	public static Object getObjectFromClass(String classPath) throws Exception {
		Class<?> c = Class.forName(classPath);
		Object obj = null;
		obj = c.newInstance(); // 不带自变量
		return obj;
	}

	/**
	 * 用指定参数列表产生一个对象
	 * 
	 * @author Lucifer Wong
	 * @param classPath
	 *            类路径
	 * @param params
	 *            参数列表
	 * @return 目标对象
	 * @throws Exception
	 *             找不到类
	 */
	public static Object getObjectFromClass(String classPath, Object... params) throws Exception {
		Class<?> classInstance = Class.forName(classPath);
		return getObjectFromClass(classInstance, params);
	}

	/**
	 * 得到传入数组对应 的类数组
	 * 
	 * @author Lucifer Wong
	 * @param params
	 *            传入数组
	 * @return Class[] 类数组
	 */
	private static Class<?>[] getParasClass(Object[] params) {

		Class<?>[] pTypes = new Class[params.length];

		for (int i = 0; i < pTypes.length; i++) {
			pTypes[i] = params[i].getClass();
		}
		return pTypes;
	}

	/**
	 * 从Class中得到类名
	 * 
	 * @param classInstance
	 *            目标类的一个对象
	 * @return 类名
	 */
	public static String getSimpleClassName(Class<?> classInstance) {
		// Class cls = java.util.Date.class;
		// 加载字节码 封装成一个Class对象
		String name = classInstance.getSimpleName();
		return name;
	}

	/**
	 * 得到指定的系统参数值 e.g PATH、JAVA_HOME<br>
	 * Gets the system property indicated by the specified key.<br>
	 * First, if there is a security manager, its checkPropertyAccess method is
	 * called with the key as its argument. If there is no current set of system
	 * properties, a set of system properties is first created and initialized
	 * in the same manner as for the getProperties method.
	 * 
	 * @author Lucifer Wong
	 * @param var
	 *            系统变量名
	 * @return 系统变量值
	 */
	public static String getSystemVariable(String var) {
		String value = System.getProperty(var);
		if (ValidatorUtil.isEmpty(value)) {
			value = System.getenv(var);
		}
		return ValidatorUtil.isEmpty(value) ? null : value;
	}

	/**
	 * 得到一个类的外部类
	 * <code>If the class or interface represented by this Class object is a member of another class, returns the Class object representing the class in which it was declared. This method returns null if this class or interface is not a member of any other class. If this Class object represents an array class, a primitive type, or void,then this method returns null.</code>
	 * 
	 * @author Lucifer Wong
	 * @param classInstance
	 *            目标类的一个对象
	 * @return 其外部类
	 */
	public static Class<?> OuterClasses(Class<?> classInstance) {
		if (ValidatorUtil.isEmpty(classInstance))
			return null;
		return classInstance.getDeclaringClass();

	}

	/**
	 * 打印类的字段、函数
	 * 
	 * @author Lucifer Wong
	 * @param classInstance
	 *            目标类的一个对象
	 */
	public static void printClassDeclaredFields(Class<?> classInstance) {

		Field[] ff = classInstance.getDeclaredFields();
		String x;

		for (int i = 0; i < ff.length; i++) {
			x = ff[i].getType().getName();
			System.out.println("字段类型名:" + x);
		}

		System.out.println("=============================");

		Constructor<?>[] cn = classInstance.getDeclaredConstructors();
		for (int i = 0; i < cn.length; i++) {
			Class cx[] = cn[i].getParameterTypes();
			System.out.println("构造函数 :" + cn[i].getName());
			for (int j = 0; j < cx.length; j++) {
				x = cx[j].getName();
				System.out.println("---------构造函数 参数类型 :" + x);
			}
		}

		System.out.println("=============================");
		Method[] mm = classInstance.getDeclaredMethods();

		for (int i = 0; i < mm.length; i++) {
			System.out.println("函数名 :" + mm[i].getName());

			int md = mm[i].getModifiers();
			System.out.print("修饰符 :" + Modifier.toString(md));

			Class cx[] = mm[i].getParameterTypes();
			for (int j = 0; j < cx.length; j++) {
				x = cx[j].getName();
				System.out.println("---------函数参数类型:" + x);
			}

			x = mm[i].getReturnType().getName();
			System.out.println("---------函数返回值类型:" + x);
		}
	}

	/**
	 * 打印出类型的定义 eg: 打印出<br>
	 * <code>public abstract interface java.io.Serializable</code>
	 * 
	 * @author Lucifer Wong
	 * @param classInstance
	 *            目标类的一个对象
	 */
	public static void printClassModifiers(Class<?> classInstance) {
		if (ValidatorUtil.isEmpty(classInstance))
			return;
		int mod = classInstance.getModifiers();
		System.out.print(Modifier.toString(mod)); // 整个modifier

		if (Modifier.isInterface(mod))
			System.out.print(" "); // 关键词 "interface"已含于modifier
		else
			System.out.print(" class "); // 关键词 "class"

		System.out.print(classInstance.getName()); // class名称

	}

	/**
	 * 根据类名 返回 静态值i 对应的静态字段名称
	 * 
	 * @author Lucifer Wong
	 * @param className
	 *            要去搜索的类
	 * @param i
	 *            要查的值
	 * @return 静态字段的名称
	 * @throws Exception
	 *             类不存在
	 */
	public static String reflect(String className, Integer i) throws Exception {

		Class<?> clazz = Class.forName(className);
		Field[] fields = clazz.getFields();

		for (Field field : fields) {
			if (field.getInt(clazz) != i) {
				continue;
			} else {
				return field.getName();
			}
		}
		return i.toString();
		// throw new NullPointerException();
	}
}
