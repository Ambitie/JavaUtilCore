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
package priv.lucife.utils.core.encrypt;

import priv.lucife.utils.core.annotation.UBTCompatible;

/**
 * 加解密工具类<br>
 * 工具类包括：MD5加密、SHA加密、Base64加解密、DES加解密、AES加解密<br>
 *
 * @author Lucifer Wong
 *
 */
@UBTCompatible
public abstract class EncryptAndDecryptUtil {

	/**
	 * AES解密
	 * 
	 * @author Lucifer Wong
	 * @param value
	 *            待解密内容
	 * @param key
	 *            秘钥
	 * @return 解密后的字符串
	 */
	public static String aesDecrypt(String value, String key) {
		key = key == null ? AESUtil.KEY : key;
		String result = null;
		try {
			if (value != null && !"".equals(value.trim())) { // value is not
																// null
				result = AESUtil.decrypt(value, key);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}

	/**
	 * AES加密
	 * 
	 * @author Lucifer Wong
	 * @param value
	 *            待加密内容
	 * @param key
	 *            秘钥
	 * @return 加密后的字符串
	 */
	public static String aesEncrypt(String value, String key) {
		key = key == null ? AESUtil.KEY : key;
		String result = null;
		try {
			if (value != null && !"".equals(value.trim())) { // value is not
																// null
				result = AESUtil.encrypt(value, key);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}

	/**
	 * BASE64 解密
	 * 
	 * @author Lucifer Wong
	 * @param value
	 *            待解密字符串
	 * @return 解密后的字符串
	 */
	public static String base64Decrypt(String value) {
		String result = null;
		try {
			if (value != null && !"".equals(value.trim())) {
				byte[] bytes = Base64Util.decrypt(value);
				result = new String(bytes);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * BASE64 加密
	 * 
	 * @author Lucifer Wong
	 * @param value
	 *            待加密字符串
	 * @return 加密后的字符串
	 */
	public static String base64Encrypt(String value) {
		String result = null;
		if (value != null && !"".equals(value.trim())) {
			result = Base64Util.encrypt(value.getBytes());
		}
		return result;

	}

	/**
	 * DES解密
	 * 
	 * @author Lucifer Wong
	 * @param value
	 *            待解密字符
	 * @param key
	 *            若key为空，则使用默认key
	 * @return 解密后的字符串
	 */
	public static String desDecrypt(String value, String key) {
		key = key == null ? DESUtil.CONST_DES_KEY_1 : key;
		String result = null;

		try {
			if (value != null && !"".equals(value.trim())) {
				result = DESUtil.decrypt(value, key);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * DES加密<br>
	 * 
	 * @author Lucifer Wong
	 * @param value
	 *            待加密字符
	 * @param key
	 *            若key为空，则使用默认key
	 * @return 加密成功返回密文，否则返回null
	 */
	public static String desEncrypt(String value, String key) {
		key = key == null ? DESUtil.CONST_DES_KEY_1 : key;
		String result = null;

		try {
			if (value != null && !"".equals(value.trim())) {
				result = DESUtil.encrypt(value, key);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * MD5 加密
	 * 
	 * @author Lucifer Wong
	 * @param value
	 *            待加密字符
	 * @return 加密后的字符串
	 */
	public static String md5Encrypt(String value) {
		String result = null;
		if (value != null && !"".equals(value.trim())) {
			try {
				result = MD5Util.encrypt(value, MD5Util.MD5_KEY);
			} catch (Exception e) {
				result = null;
			}
		}
		return result;
	}

	/**
	 * SHA加密
	 * 
	 * @author Lucifer Wong
	 * @param value
	 *            待加密字符
	 * @return 加密后的字符串
	 */
	public static String shaEncrypt(String value) {
		String result = null;
		if (value != null && !"".equals(value.trim())) {
			try {
				result = MD5Util.encrypt(value, MD5Util.SHA_KEY);
			} catch (Exception e) {
				result = null;
			}
		}
		return result;
	}
}
