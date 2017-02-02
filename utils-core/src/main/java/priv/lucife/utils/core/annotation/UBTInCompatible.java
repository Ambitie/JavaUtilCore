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
 * The presence of this annotation on a method indicates that the method may
 * <em>not</em> be used with the UtilsBaseToolkit(UBT), even though its type is
 * annotated as {@link UBTCompatible} and accessible in UBT. They can cause UBT
 * compilation errors or simply unexpected exceptions when used in UBT.
 *
 * <p>
 * Note that this annotation should only be applied to methods, fields, or inner
 * classes of types which are annotated as {@link UBTCompatible}.
 *
 * @author Lucifer Wong
 */

@Retention(RetentionPolicy.CLASS)
@Target({ ElementType.TYPE, ElementType.METHOD, ElementType.CONSTRUCTOR, ElementType.FIELD })
@Documented
@UBTCompatible
public @interface UBTInCompatible {
	 /**
	   * Describes why the annotated element is incompatible with UBT. Since this is
	   * generally due to a dependence on a type/method which UBT doesn't support,
	   * it is sufficient to simply reference the unsupported type/method. E.g.
	   * "Class.isInstance".
	   * @return E.g. "Class.isInstance".
	   */
	  String value();
}
