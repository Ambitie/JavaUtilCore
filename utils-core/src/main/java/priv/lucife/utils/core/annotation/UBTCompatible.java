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
package priv.lucife.utils.core.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 
 * The presence of this annotation on a type indicates that the type may be used
 * with the UtilsBaseToolkit, When applied to a method, the return type of the
 * method is UBT compatible. It's useful to indicate that an instance created by
 * factory methods has a UBT serializable type.
 * 
 * @author Lucifer Wong
 *
 */
@Retention(RetentionPolicy.CLASS)
@Target({ ElementType.TYPE, ElementType.METHOD })
@Documented
public @interface UBTCompatible {

	/**
	 * When {@code true}, the annotated type or the type of the method return
	 * value is UBT serializable.
	 * 
	 * @return serializable
	 */
	boolean serializable() default false;

	/**
	 * When {@code true}, the annotated type is emulated in UBT. The emulated
	 * source (also known as super-source) is different from the implementation
	 * used by the JVM.
	 * 
	 * @return emulated
	 */
	boolean emulated() default false;
}
