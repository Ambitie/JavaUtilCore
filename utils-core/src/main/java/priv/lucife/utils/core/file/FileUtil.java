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
package priv.lucife.utils.core.file;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import priv.lucife.utils.core.annotation.UBTCompatible;
import priv.lucife.utils.core.base.ValidatorUtil;
import priv.lucife.utils.core.date.DateUtil;
import priv.lucife.utils.core.math.MathUtil;
import priv.lucife.utils.core.math.RandomUtil;

/**
 * 文件工具类
 * 
 * @author Lucifer Wong
 */
@UBTCompatible
public abstract class FileUtil {
	private static class Recursiver {

		private static ArrayList<File> files = new ArrayList<File>();

		public List<File> getFileList(File file) {
			File children[] = file.listFiles();

			for (int i = 0; i < children.length; i++) {
				if (children[i].isDirectory()) {
					new Recursiver().getFileList(children[i]);
				} else {
					files.add(children[i]);
				}
			}

			return files;
		}
	}

	private static final int BUFFER_SIZE = 4096;

	private static final char EXTENSION_SEPARATOR = '.';

	private static final String FOLDER_SEPARATOR = "/";

	/**
	 * 文件大小单位：1KB。
	 */
	public static final long ONE_KB = 1024;

	/**
	 * 文件大小单位：1MB。
	 */
	public static final long ONE_MB = ONE_KB * ONE_KB;

	/**
	 * 文件大小单位：1GB。
	 */
	public static final long ONE_GB = ONE_KB * ONE_MB;

	/**
	 * 将以 byte 为单位的文件大小转换为一个可读性更好的文件大小，最终结果精确到一位小数。
	 * 
	 * @author Lucifer Wong
	 * @param size
	 *            以 byte 为单位的文件大小
	 * @return 更具可读性的文件大小（包括单位：GB、MB、KB、B），例如：102 B、1.5 KB、23.8 MB、34.2 GB
	 */
	public static String byteCountToDisplaySize(long size) {
		String displaySize;

		if (size / ONE_GB > 0) {
			displaySize = MathUtil.div(size * 1.0, ONE_GB, 1) + " GB";
		} else if (size / ONE_MB > 0) {
			displaySize = MathUtil.div(size * 1.0, ONE_MB, 1) + " MB";
		} else if (size / ONE_KB > 0) {
			displaySize = MathUtil.div(size * 1.0, ONE_KB, 1) + " KB";
		} else {
			displaySize = String.valueOf(size) + " B";
		}

		return displaySize;
	}

	/**
	 * 关闭 Closeable 对象。
	 * 
	 * @author Lucifer Wong
	 * @param closeable
	 *            Closeable 对象
	 */
	public static void close(Closeable closeable) {
		try {
			if (closeable != null) {
				closeable.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			closeable = null;
		}
	}

	/**
	 * 关闭输入流
	 * 
	 * @author Lucifer Wong
	 * @param in
	 *            输入流
	 */
	public static void close(InputStream in) {
		try {
			if (in != null) {
				in.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			in = null;
		}
	}

	/**
	 * 关闭输出流
	 * 
	 * @author Lucifer Wong
	 * @param out
	 *            输出流
	 */
	public static void close(OutputStream out) {
		try {
			if (out != null) {
				out.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			out = null;
		}
	}

	/**
	 * 关闭 Reader
	 * 
	 * @author Lucifer Wong
	 * @param in
	 *            Reader
	 */
	public static void close(Reader in) {
		try {
			if (in != null) {
				in.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			in = null;
		}
	}

	/**
	 * 关闭 Writer
	 * 
	 * @author Lucifer Wong
	 * @param out
	 *            Writer
	 */
	public static void close(Writer out) {
		try {
			if (out != null) {
				out.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			out = null;
		}
	}

	/**
	 * 复制给定的数组中的内容到输出流中。
	 * 
	 * @author Lucifer Wong
	 * @param in
	 *            需要复制的数组
	 * @param out
	 *            复制到的输出流
	 * @throws IOException
	 *             当发生 I/O 异常时抛出
	 */
	public static void copy(byte[] in, OutputStream out) throws IOException {
		try {
			out.write(in);
		} finally {
			try {
				out.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			} finally {
				out = null;
			}
		}
	}

	/**
	 * 复制文件或者文件夹
	 * 
	 * @author Lucifer Wong
	 * @param inputFile
	 *            源文件
	 * @param outputFile
	 *            目的文件
	 * @param isOverWrite
	 *            是否覆盖文件
	 * @throws java.io.IOException
	 *             当发生 I/O 异常时抛出
	 */
	public static void copy(File inputFile, File outputFile, boolean isOverWrite) throws IOException {
		if (!inputFile.exists()) {
			throw new RuntimeException(inputFile.getPath() + "源目录不存在!");
		}
		copyPri(inputFile, outputFile, isOverWrite);
	}

	/**
	 * 复制文件或者文件夹
	 * 
	 * @author Lucifer Wong
	 * @param inputFile
	 *            源文件
	 * @param outputFile
	 *            目的文件
	 * @param isOverWrite
	 *            是否覆盖文件
	 * @throws java.io.IOException
	 *             当发生 I/O 异常时抛出
	 */
	private static void copyPri(File inputFile, File outputFile, boolean isOverWrite) throws IOException {
		if (inputFile.isFile()) { // 文件
			copySimpleFile(inputFile, outputFile, isOverWrite);
		} else {
			if (!outputFile.exists()) { // 文件夹
				outputFile.mkdirs();
			}
			// 循环子文件夹
			for (File child : inputFile.listFiles()) {
				copy(child, new File(outputFile.getPath() + "/" + child.getName()), isOverWrite);
			}
		}
	}

	/**
	 * 复制单个文件
	 * 
	 * @author Lucifer Wong
	 * @param inputFile
	 *            源文件
	 * @param outputFile
	 *            目的文件
	 * @param isOverWrite
	 *            是否覆盖
	 * @throws java.io.IOException
	 *             当发生 I/O 异常时抛出
	 */
	private static void copySimpleFile(File inputFile, File outputFile, boolean isOverWrite) throws IOException {
		if (outputFile.exists()) {
			if (isOverWrite) { // 可以覆盖
				if (!outputFile.delete()) {
					throw new RuntimeException(outputFile.getPath() + "无法覆盖！");
				}
			} else {
				// 不允许覆盖
				return;
			}
		}
		InputStream in = new FileInputStream(inputFile);
		OutputStream out = new FileOutputStream(outputFile);
		byte[] buffer = new byte[1024];
		int read = 0;
		while ((read = in.read(buffer)) != -1) {
			out.write(buffer, 0, read);
		}
		in.close();
		out.close();
	}

	/**
	 * 创建文件支持多级目录
	 * 
	 * @author Lucifer Wong
	 * @param filePath
	 *            需要创建的文件
	 * @return 是否成功
	 */
	public final static boolean createFiles(String filePath) {
		File file = new File(filePath);
		File dir = file.getParentFile();
		if (!dir.exists()) {
			if (dir.mkdirs()) {
				try {
					return file.createNewFile();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return false;
	}

	/**
	 * 取得文件的后缀名。
	 * 
	 * @author Lucifer Wong
	 * @param fileName
	 *            文件名
	 * @return 后缀名
	 */
	public static String getExtension(String fileName) {
		if (ValidatorUtil.isEmpty(fileName)) {
			return null;
		}

		int pointIndex = fileName.lastIndexOf(".");
		return pointIndex > 0 && pointIndex < fileName.length() ? fileName.substring(pointIndex + 1).toLowerCase()
				: null;
	}

	/**
	 * 获取文件的MD5
	 * 
	 * @author Lucifer Wong
	 * @param file
	 *            文件
	 * @return 文件的MD5值
	 */
	public static String getFileMD5(File file) {
		if (!file.exists() || !file.isFile()) {
			return null;
		}
		MessageDigest digest = null;
		FileInputStream in = null;
		byte buffer[] = new byte[1024];
		int len;
		try {
			digest = MessageDigest.getInstance("MD5");
			in = new FileInputStream(file);
			while ((len = in.read(buffer, 0, 1024)) != -1) {
				digest.update(buffer, 0, len);
			}
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		BigInteger bigInt = new BigInteger(1, digest.digest());
		return bigInt.toString(16);
	}

	/**
	 * 获取文件名，文件构成：当前时间 + 10位随机数
	 * 
	 * @author Lucifer Wong
	 * @return 获取随机文件名
	 */
	public static String getFileName() {
		String date = DateUtil.getCurrentTime("yyyyMMddHH24mmss"); // 当前时间
		String random = RandomUtil.generateNumberString(10); // 10位随机数

		// 返回文件名
		return date + random;
	}

	/**
	 * 获取文件名，文件名构成:当前时间 + 10位随机数 + .type
	 * 
	 * @author Lucifer Wong
	 * @param type
	 *            文件类型
	 * @return 获取随机文件名
	 */
	public static String getFileName(String type) {
		return getFileName(type, "", "");
	}

	/**
	 * 获取文件名，构建结构为 prefix + yyyyMMddHH24mmss + 10位随机数 + suffix + .type
	 * 
	 * @author Lucifer Wong
	 * @param type
	 *            文件类型
	 * @param prefix
	 *            前缀
	 * @param suffix
	 *            后缀
	 * @return 获取随机文件名
	 */
	public static String getFileName(String type, String prefix, String suffix) {
		String date = DateUtil.getCurrentTime("yyyyMMddHH24mmss"); // 当前时间
		String random = RandomUtil.generateNumberString(10); // 10位随机数

		// 返回文件名
		return prefix + date + random + suffix + "." + type;
	}

	/**
	 * 获取指定文件的大小
	 * 
	 * @author Lucifer Wong
	 * @param file
	 *            指定文件
	 * @return 指定文件大小
	 * @throws Exception
	 *             指定文件找不到
	 * @author Lucifer Wong
	 */
	@SuppressWarnings("resource")
	public static long getFileSize(File file) throws Exception {
		long size = 0;
		if (file.exists()) {
			FileInputStream fis = null;
			fis = new FileInputStream(file);
			size = fis.available();
		} else {
			file.createNewFile();
		}
		return size;
	}

	/**
	 * 获取文件的后缀
	 * 
	 * @author Lucifer Wong
	 * @param file
	 *            文件
	 * @return 文件的后缀名
	 */
	public static String getFileSuffix(String file) {
		if (file == null) {
			return null;
		}
		int extIndex = file.lastIndexOf(EXTENSION_SEPARATOR);
		if (extIndex == -1) {
			return null;
		}
		int folderIndex = file.lastIndexOf(FOLDER_SEPARATOR);
		if (folderIndex > extIndex) {
			return null;
		}
		return file.substring(extIndex + 1);
	}

	/**
	 * 递归取得某个目录下所有的文件
	 * 
	 * @author Lucifer Wong
	 * @param path
	 *            目录
	 * @return 文件List
	 */
	public static List<File> getNestedFiles(String path) {
		File directory = new File(path);
		if (!directory.exists() || !directory.isDirectory()) {
			throw new IllegalArgumentException("Nonexistent directory[" + path + "]");
		}

		return new Recursiver().getFileList(directory);
	}

	/**
	 * 判断指定路径是否存在，如果不存在，根据参数决定是否新建
	 * 
	 * @author Lucifer Wong
	 * @param filePath
	 *            指定的文件路径
	 * @param isNew
	 *            true：新建、false：不新建
	 * @return 存在返回TRUE，不存在返回FALSE
	 */
	public static boolean isExist(String filePath, boolean isNew) {
		File file = new File(filePath);
		if (!file.exists() && isNew) {
			return file.mkdirs(); // 新建文件路径
		}
		return false;
	}

	/**
	 * 从系统属性文件中获取相应的值
	 * 
	 * @author Lucifer Wong
	 * @param key
	 *            key
	 * @return 返回value
	 */
	public final static String key(String key) {
		return System.getProperty(key);
	}

	/**
	 * 罗列指定路径下的全部文件
	 * 
	 * @author Lucifer Wong
	 * @param path
	 *            需要处理的文件
	 * @return 返回文件列表
	 */
	public final static List<File> listFile(File path) {
		List<File> list = new ArrayList<>();
		File[] files = path.listFiles();
		if (!ValidatorUtil.isEmpty((Object[]) files)) {
			for (File file : files) {
				if (file.isDirectory()) {
					list.addAll(listFile(file));
				} else {
					list.add(file);
				}
			}
		}
		return list;
	}

	/**
	 * 从输入流读取Properties对象
	 * 
	 * @author Lucifer Wong
	 * @param in
	 *            输入流
	 * @return property的Map
	 */
	public final static Map<String, String> properties(InputStream in) {
		Map<String, String> map = new HashMap<>();
		Properties pps = new Properties();
		try {
			pps.load(in);
		} catch (IOException e) {
			e.printStackTrace();
		}
		Enumeration<?> en = pps.propertyNames();
		while (en.hasMoreElements()) {
			String strKey = (String) en.nextElement();
			String strValue = pps.getProperty(strKey);
			map.put(strKey, strValue);
		}
		return map;
	}

	/**
	 * 从输入流读取Properties对象
	 * 
	 * @author Lucifer Wong
	 * @param in
	 *            输入流
	 * @return Properties对象
	 */
	public static Properties readProperties(InputStream in) {
		Properties properties = new Properties();

		try {
			properties.load(in);
		} catch (IOException e) {
			throw new RuntimeException("Could not read Properties", e);
		} finally {
			close(in);
		}

		return properties;
	}

	/**
	 * 读取指定路径的Properties文件
	 * 
	 * @author Lucifer Wong
	 * @param path
	 *            路径
	 * @return Properties对象
	 */
	public static Properties readProperties(String path) {
		File file = new File(path);
		if (!file.exists()) {
			return null;
		}

		Properties properties = new Properties();
		InputStream in = null;

		try {
			in = new FileInputStream(file);
			properties.load(in);
		} catch (IOException e) {
			throw new RuntimeException("Could not read Properties[" + path + "]", e);
		} finally {
			close(in);
		}

		return properties;
	}

	/**
	 * 根据Key读取Value
	 * 
	 * @author Lucifer Wong
	 * @param filePath
	 *            属性文件
	 * @param key
	 *            需要读取的属性
	 * @return 返回Value值
	 */
	public final static String readProperties(String filePath, String key) {
		Properties pps = new Properties();
		try (InputStream in = new BufferedInputStream(new FileInputStream(filePath))) {
			pps.load(in);
			return pps.getProperty(key);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 读取Properties的全部信息
	 * 
	 * @author Lucifer Wong
	 * @param filePath
	 *            读取的属性文件
	 * @return 返回所有的属性 key:value&lt;&gt;key:value
	 * @throws IOException
	 *             找不到文件
	 */
	public final static Map<String, String> readProperties4Map(String filePath) throws IOException {
		Map<String, String> map = new HashMap<>();
		try (InputStream in = new BufferedInputStream(new FileInputStream(filePath))) {
			return properties(in);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return map;
	}

	/**
	 * 从输入流中读取字符串，使用默认字符集
	 * 
	 * @author Lucifer Wong
	 * @param in
	 *            输入流
	 * @return 流内容的字符串
	 */
	public static String readString(InputStream in) {
		ByteArrayOutputStream out = null;

		try {
			out = new ByteArrayOutputStream();

			byte[] buffer = new byte[BUFFER_SIZE];
			int length;
			while ((length = in.read(buffer)) != -1) {
				out.write(buffer, 0, length);
			}
			return new String(out.toByteArray());
		} catch (IOException e) {
			throw new RuntimeException("Could not read stream", e);
		} finally {
			close(in);
			close(out);
		}
	}

	/**
	 * 从文件中读取字符串，使用默认字符集
	 * 
	 * @author Lucifer Wong
	 * @param path
	 *            文件路径
	 * @return 文件内容的字符串
	 */
	public static String readString(String path) {
		FileInputStream in = null;
		ByteArrayOutputStream out = null;

		try {
			in = new FileInputStream(path);
			out = new ByteArrayOutputStream();

			byte[] buffer = new byte[BUFFER_SIZE];
			int length;
			while ((length = in.read(buffer)) != -1) {
				out.write(buffer, 0, length);
			}
			return new String(out.toByteArray());
		} catch (IOException e) {
			throw new RuntimeException("Could not read String[" + path + "]", e);
		} finally {
			close(in);
			close(out);
		}
	}

	/**
	 * 把Properties对象写到指定路径的文件里
	 * 
	 * @author Lucifer Wong
	 * @param path
	 *            路进
	 * @param properties
	 *            Properties对象
	 */
	public static void writeProperties(String path, Properties properties) {
		OutputStream out = null;

		try {
			out = new FileOutputStream(path);
			properties.store(out, null);
		} catch (IOException e) {
			throw new RuntimeException("Could not write Properties[" + path + "]", e);
		} finally {
			close(out);
		}
	}

	/**
	 * 写入Properties信息
	 * 
	 * @author Lucifer Wong
	 * @param filePath
	 *            写入的属性文件
	 * @param pKey
	 *            属性名称
	 * @param pValue
	 *            属性值
	 * @throws IOException
	 *             找不到文件
	 */
	public final static void WriteProperties(String filePath, String pKey, String pValue) throws IOException {
		Properties props = new Properties();

		props.load(new FileInputStream(filePath));
		// 调用 Hashtable 的方法 put，使用 getProperty 方法提供并行性。
		// 强制要求为属性的键和值使用字符串。返回值是 Hashtable 调用 put 的结果。
		OutputStream fos = new FileOutputStream(filePath);
		props.setProperty(pKey, pValue);
		// 以适合使用 load 方法加载到 Properties 表中的格式，
		// 将此 Properties 表中的属性列表（键和元素对）写入输出流
		props.store(fos, "Update '" + pKey + "' value");

	}

	/**
	 * 把字符串写到文件中
	 * 
	 * @author Lucifer Wong
	 * @param path
	 *            文件路径
	 * @param str
	 *            字符串
	 * @param append
	 *            是否追加，否的话会覆盖原来的内容
	 */
	public static void writeString(String path, String str, boolean append) {
		FileWriter out = null;
		try {
			out = new FileWriter(path, append);
			out.write(str);
		} catch (IOException e) {
			throw new RuntimeException("Could not write String[" + path + "]", e);
		} finally {
			close(out);
		}
	}

	/**
	 * 删除所有文件，包括文件夹
	 * 
	 * @author Lucifer Wong
	 * @param dirpath
	 *            目标文件
	 */
	public void deleteAll(String dirpath) {
		File path = new File(dirpath);
		try {
			if (!path.exists())
				return;// 目录不存在退出
			if (path.isFile()) // 如果是文件删除
			{
				path.delete();
				return;
			}
			File[] files = path.listFiles();// 如果目录中有文件递归删除文件
			for (int i = 0; i < files.length; i++) {
				deleteAll(files[i].getAbsolutePath());
			}
			path.delete();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 文件重命名
	 * 
	 * @author Lucifer Wong
	 * @param oldPath
	 *            老文件
	 * @param newPath
	 *            新文件
	 * @return true if and only if the renaming succeeded; false otherwise
	 */
	public boolean renameDir(String oldPath, String newPath) {
		File oldFile = new File(oldPath);// 文件或目录
		File newFile = new File(newPath);// 文件或目录
		return oldFile.renameTo(newFile);// 重命名
	}
}
