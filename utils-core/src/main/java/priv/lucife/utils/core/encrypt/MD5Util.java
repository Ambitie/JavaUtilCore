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

import java.security.MessageDigest;

import priv.lucife.utils.core.annotation.UBTCompatible;

/**
 * MD5加密
 * 
 * @author Lucifer Wong
 */
@UBTCompatible
public abstract class MD5Util {
	protected final static String MD5_KEY = "MD5";

	protected final static String SHA_KEY = "SHA1";

	/**
	 * @author Lucifer Wong
	 * @param byteArray
	 * @return
	 */
	private static String byteArrayToHex(byte[] byteArray) {

		// 首先初始化一个字符数组，用来存放每个16进制字符
		char[] hexDigits = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
		// new一个字符数组，这个就是用来组成结果字符串的（解释一下：一个byte是八位二进制，也就是2位十六进制字符（2的8次方等于16的2次方））
		char[] resultCharArray = new char[byteArray.length * 2];
		// 遍历字节数组，通过位运算（位运算效率高），转换成字符放到字符数组中去
		int index = 0;
		for (byte b : byteArray) {
			resultCharArray[index++] = hexDigits[b >>> 4 & 0xf];
			resultCharArray[index++] = hexDigits[b & 0xf];
		}
		// 字符数组组合成字符串返回
		return new String(resultCharArray);
	}

	/**
	 * MD5加密
	 * 
	 * @author Lucifer Wong
	 * @param value
	 *            待加密的文字
	 * @param key
	 *            秘钥
	 * @return 加密后的字符串
	 * @throws Exception
	 *             加密失败
	 */
	public static String encrypt(String value, String key) throws Exception {

		// 拿到一个MD5转换器（如果想要SHA1参数换成”SHA1”）
		MessageDigest messageDigest = MessageDigest.getInstance(key);
		// 输入的字符串转换成字节数组
		byte[] inputByteArray = value.getBytes();
		// inputByteArray是输入字符串转换得到的字节数组
		messageDigest.update(inputByteArray);
		// 转换并返回结果，也是字节数组，包含16个元素
		byte[] resultByteArray = messageDigest.digest();
		// 字符数组转换成字符串返回
		return byteArrayToHex(resultByteArray);

	}
}
