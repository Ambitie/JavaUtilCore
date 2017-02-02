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

import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.SecretKeySpec;

import priv.lucife.utils.core.annotation.UBTCompatible;

/**
 * AES加解密
 * 
 * @author Lucifer Wong
 */
@UBTCompatible
public abstract class AESUtil {
	/** 默认秘钥 */
	protected static final String KEY = "NOPO3nzMD3dndwS0MccuMeXCHgVlGOoYyFwLdS24Im2e7YyhB0wrUsyYf0";

	private static String aesDecryptByBytes(byte[] encryptBytes, String decryptKey) throws Exception {
		KeyGenerator kgen = KeyGenerator.getInstance("AES");
		kgen.init(128, new SecureRandom(decryptKey.getBytes()));

		Cipher cipher = Cipher.getInstance("AES");
		cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(kgen.generateKey().getEncoded(), "AES"));
		byte[] decryptBytes = cipher.doFinal(encryptBytes);

		return new String(decryptBytes);
	}

	private static byte[] aesEncryptToBytes(String content, String encryptKey) throws Exception {
		KeyGenerator kgen = KeyGenerator.getInstance("AES");
		kgen.init(128, new SecureRandom(encryptKey.getBytes()));

		Cipher cipher = Cipher.getInstance("AES");
		cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(kgen.generateKey().getEncoded(), "AES"));

		return cipher.doFinal(content.getBytes("utf-8"));
	}

	private static byte[] base64Decode(String base64Code) throws Exception {
		return base64Code == null ? null : Base64Util.decrypt(base64Code);
	}

	private static String base64Encode(byte[] bytes) {
		return Base64Util.encrypt(bytes);
	}

	/**
	 * AES解密
	 * 
	 * @author Lucifer Wong
	 * @param encryptValue
	 *            待解密内容
	 * @param key
	 *            秘钥
	 * @return 加密后的字符串
	 * @throws Exception
	 *             加密失败
	 */
	public static String decrypt(String encryptValue, String key) throws Exception {
		return aesDecryptByBytes(base64Decode(encryptValue), key);
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
	 * @throws Exception
	 *             加密失败
	 */
	public static String encrypt(String value, String key) throws Exception {
		return base64Encode(aesEncryptToBytes(value, key));
	}

}
