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
package priv.lucife.utils.core.base;

import priv.lucife.utils.core.annotation.UBTCompatible;

/**
 * 
 * @author Lucifer Wong
 *
 */
@UBTCompatible
public abstract class HexByteUtil {
	private static final char[] _hex = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E',
			'F' };

	/**
	 * 将一组16进制的的字符串转化成byte数组(16进制的asii码串)
	 * 
	 * @author Lucifer Wong
	 * @param s
	 *            一组16进制的的字符串
	 * @return If string is not even length, return null.
	 */
	public static byte[] decode(final String s) {
		int len = s.length();
		if (len % 2 != 0) {
			return null;
		}

		byte[] bytes = new byte[len / 2];
		int pos = 0;

		for (int i = 0; i < len; i += 2) {
			byte hi = (byte) Character.digit(s.charAt(i), 16);
			byte lo = (byte) Character.digit(s.charAt(i + 1), 16);
			bytes[pos++] = (byte) (hi * 16 + lo);
		}

		return bytes;
	}

	/**
	 * 将一组字节数组转成相应的16进制字符串(16进制的asii码串)，一个字节转2位
	 * 
	 * @author Lucifer Wong
	 *
	 * @param bytes
	 *            array representation
	 * @return String hex digits
	 */
	public static String encode(final byte[] bytes) {
		StringBuilder sb = new StringBuilder(bytes.length << 1);
		for (byte aByte : bytes) {
			// 8字节的高四位
			sb.append(convertDigit(aByte >> 4));
			// 低4位
			sb.append(convertDigit(aByte & 0x0f));
		}
		return sb.toString();
	}

	/**
	 * 将0..15转化成0..F
	 * 
	 * @author Lucifer Wong
	 * @param value
	 *            to be converted
	 * @return '0'..'F' in char format.
	 */
	public static char convertDigit(final int value) {
		return _hex[value & 0x0f];
	}

	/**
	 * @param bytes
	 *            byte[] of bytes to test
	 * @return true if bytes are gzip compressed, false otherwise.
	 */
	public static boolean isGzipped(byte[] bytes) {
		return bytes[0] == (byte) 0x1f && bytes[1] == (byte) 0x8b;
	}
}
