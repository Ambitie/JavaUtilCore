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
package priv.lucife.utils.core.io;

import priv.lucife.utils.core.annotation.UBTCompatible;
import priv.lucife.utils.core.base.HexByteUtil;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.Flushable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLConnection;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import java.util.zip.Inflater;
import java.util.zip.InflaterInputStream;

/**
 * 简化 io tasks
 * 
 * @author Lucifer Wong
 */
@UBTCompatible
public abstract class IOUtil {
	private static final int TRANSFER_BUFFER = 32768;

	/**
	 * 得到一个URL的输入流
	 * 
	 * @author Lucifer Wong
	 * @param c
	 *            Connection to transfer output
	 * @return 得到一个URL的输入流
	 * @throws IOException
	 *             IO出错
	 */
	public static InputStream getInputStream(URLConnection c) throws IOException {
		InputStream is = c.getInputStream();
		String enc = c.getContentEncoding();

		if ("gzip".equalsIgnoreCase(enc) || "x-gzip".equalsIgnoreCase(enc)) {
			is = new GZIPInputStream(is, TRANSFER_BUFFER);
		} else if ("deflate".equalsIgnoreCase(enc)) {
			is = new InflaterInputStream(is, new Inflater(), TRANSFER_BUFFER);
		}

		return new BufferedInputStream(is);
	}

	/**
	 * 将一个文件输入到一个URLConnection
	 * 
	 * @author Lucifer Wong
	 * @param f
	 *            输入文件
	 * @param c
	 *            Connection to transfer output
	 * @param cb
	 *            回调函数
	 * @throws Exception
	 *             IO出错
	 */
	public static void transfer(File f, URLConnection c, TransferCallback cb) throws Exception {
		InputStream in = null;
		OutputStream out = null;
		try {
			in = new BufferedInputStream(new FileInputStream(f));
			out = new BufferedOutputStream(c.getOutputStream());
			transfer(in, out, cb);
		} finally {
			close(in);
			close(out);
		}
	}

	/**
	 * 将一个URLConnection输入到一个文件中
	 * 
	 * @author Lucifer Wong
	 * @param c
	 *            Connection to transfer output
	 * @param f
	 *            目标文件
	 * @param cb
	 *            回调函数
	 * @throws Exception
	 *             IO出错
	 */
	public static void transfer(URLConnection c, File f, TransferCallback cb) throws Exception {
		InputStream in = null;
		try {
			in = getInputStream(c);
			transfer(in, f, cb);
		} finally {
			close(in);
		}
	}

	/**
	 * 将一个InputStream输入到一个文件中
	 * 
	 * @author Lucifer Wong
	 * @param s
	 *            输入流
	 * @param f
	 *            目标文件
	 * @param cb
	 *            回调函数
	 * @throws Exception
	 *             IO出错
	 */
	public static void transfer(InputStream s, File f, TransferCallback cb) throws Exception {
		try (OutputStream out = new BufferedOutputStream(new FileOutputStream(f))) {
			transfer(s, out, cb);
		}
	}

	/**
	 * 从一个输入写到一个输出当中
	 * 
	 * @author Lucifer Wong
	 * @param in
	 *            输入流
	 * @param out
	 *            输出流
	 * @param cb
	 *            回调函数
	 * @throws IOException
	 *             IO出错
	 */
	public static void transfer(InputStream in, OutputStream out, TransferCallback cb) throws IOException {
		byte[] bytes = new byte[TRANSFER_BUFFER];
		int count;
		while ((count = in.read(bytes)) != -1) {
			out.write(bytes, 0, count);
			if (cb != null) {
				cb.bytesTransferred(bytes, count);
				if (cb.isCancelled()) {
					break;
				}
			}
		}
	}

	/**
	 * 将特定字节读入输入流中
	 * 
	 * @author Lucifer Wong
	 * @param in
	 *            输入流
	 * @param bytes
	 *            特定字节
	 * @throws IOException
	 *             IO出错
	 */
	public static void transfer(InputStream in, byte[] bytes) throws IOException {
		// Read in the bytes
		int offset = 0;
		int numRead;
		while (offset < bytes.length && (numRead = in.read(bytes, offset, bytes.length - offset)) >= 0) {
			offset += numRead;
		}

		if (offset < bytes.length) {
			throw new IOException("Retry:  Not all bytes were transferred correctly.");
		}
	}

	/**
	 * 将输入流中的字节读到输出流中
	 * 
	 * @author Lucifer Wong
	 * @param in
	 *            输入流
	 * @param out
	 *            输出流
	 * @throws IOException
	 *             IO出错
	 */
	public static void transfer(InputStream in, OutputStream out) throws IOException {
		byte[] bytes = new byte[TRANSFER_BUFFER];
		int count;
		while ((count = in.read(bytes)) != -1) {
			out.write(bytes, 0, count);
		}
	}

	/**
	 * 将一个文件读到指定的输出流中
	 * 
	 * @author Lucifer Wong
	 * @param file
	 *            文件
	 * @param out
	 *            输出流
	 * @throws IOException
	 *             IO出错
	 */
	public static void transfer(File file, OutputStream out) throws IOException {
		try (InputStream in = new BufferedInputStream(new FileInputStream(file), TRANSFER_BUFFER)) {
			transfer(in, out);
		} finally {
			flush(out);
		}
	}

	/**
	 * 关闭一个Closeable
	 * 
	 * @param c
	 *            一个Closeable
	 */
	public static void close(Closeable c) {
		try {
			if (c != null) {
				c.close();
			}
		} catch (IOException ignore) {
		}
	}

	/**
	 * flush 一个 Flushable A Flushable is a destination of data that can be
	 * flushed. The flush method is invoked to write any buffered output to the
	 * underlying stream.
	 * 
	 * @param f
	 *            Flushable
	 */
	public static void flush(Flushable f) {
		try {
			if (f != null) {
				f.flush();
			}
		} catch (IOException ignore) {
		}
	}

	/**
	 * 将输入流以字节的方式返回
	 * 
	 * @author Lucifer Wong
	 * @param in
	 *            输入流
	 * @return IO出错
	 */
	public static byte[] inputStreamToBytes(InputStream in) {
		try {
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			transfer(in, out);
			return out.toByteArray();
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 将一个URL链接输出到指定输出流中
	 * 
	 * @author Lucifer Wong
	 * @param c
	 *            Connection to transfer output
	 * @param bytes
	 *            the bytes to send
	 * @throws IOException
	 *             IO出错
	 */
	public static void transfer(URLConnection c, byte[] bytes) throws IOException {
		try (OutputStream out = new BufferedOutputStream(c.getOutputStream())) {
			out.write(bytes);
		}
	}

	/**
	 * 压缩字节
	 * 
	 * @author Lucifer Wong
	 * @param original
	 *            original
	 * 
	 * @param compressed
	 *            compressed
	 * @throws IOException
	 *             IO出错
	 */
	public static void compressBytes(ByteArrayOutputStream original, ByteArrayOutputStream compressed)
			throws IOException {
		DeflaterOutputStream gzipStream = new GZIPOutputStream(compressed, TRANSFER_BUFFER);
		original.writeTo(gzipStream);
		gzipStream.flush();
		gzipStream.close();
	}

	/**
	 * 压缩字节
	 * 
	 * @author Lucifer Wong
	 * @param bytes
	 *            压缩的字节
	 * @return 压缩字节
	 */
	public static byte[] compressBytes(byte[] bytes) {
		try (ByteArrayOutputStream byteStream = new ByteArrayOutputStream(bytes.length)) {
			try (GZIPOutputStream gzipStream = new GZIPOutputStream(byteStream)) {
				gzipStream.write(bytes);
				gzipStream.flush();
			}
			return byteStream.toByteArray();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bytes;
	}

	/**
	 * 解压缩字节
	 * 
	 * @author Lucifer Wong
	 * @param bytes
	 *            解压缩字节
	 * @return 解压缩的字节
	 */
	public static byte[] uncompressBytes(byte[] bytes) {
		if (HexByteUtil.isGzipped(bytes)) {
			try (ByteArrayInputStream byteStream = new ByteArrayInputStream(bytes)) {
				try (GZIPInputStream gzipStream = new GZIPInputStream(byteStream, 8192)) {
					return inputStreamToBytes(gzipStream);
				}
			} catch (Exception e) {
				throw new RuntimeException("Error uncompressing bytes", e);
			}
		}
		return bytes;
	}

	/**
	 * 进度显示的回调接口
	 * 
	 * @author Lucifer Wong
	 *
	 */
	public interface TransferCallback {
		/**
		 * 可用于显示进度
		 * 
		 * @param bytes
		 *            传输的字节
		 * @param count
		 *            字节数量
		 */
		void bytesTransferred(byte[] bytes, int count);

		/**
		 * 是否取消转化
		 * 
		 * @return 取消否？
		 */
		boolean isCancelled();
	}
}
