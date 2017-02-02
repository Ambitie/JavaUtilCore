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

/**
 * 
 * The presence of this annotation on a type indicates that the type may be used
 * with the UtilsBaseToolkit Data Structure, When applied to a method, the
 * return type of the method is UBTS compatible. It's useful to indicate that an
 * instance created by factory methods has a UBTS serializable type.
 * 
 * @author Lucifer Wong
 *
 */
public @interface UBTDS {

}
