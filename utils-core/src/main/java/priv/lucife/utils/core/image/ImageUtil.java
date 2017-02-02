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
package priv.lucife.utils.core.image;

import com.sun.image.codec.jpeg.*;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import priv.lucife.utils.core.annotation.UBTCompatible;

/**
 * 图像处理<br>
 * 对图片进行压缩、水印、伸缩变换、透明处理、格式转换操作
 * 
 * @author Lucifer Wong
 */
@UBTCompatible
public abstract class ImageUtil {

	public static final float DEFAULT_QUALITY = 0.2125f;

	/**
	 * 
	 * 添加文字水印操作,返回BufferedImage对象
	 * 
	 * @author Lucifer Wong
	 * @param imgPath
	 *            待处理图片
	 * @param text
	 *            水印文字
	 * @param font
	 *            水印字体信息 不写默认值为宋体
	 * @param color
	 *            水印字体颜色
	 * @param x
	 *            水印位于图片左上角的 x 坐标值
	 * @param y
	 *            水印位于图片左上角的 y 坐标值
	 * @param alpha
	 *            水印透明度 0.1f ~ 1.0f
	 * @return 处理后的图片对象
	 * @throws Exception
	 *             空指针或者转化失败
	 */

	public static BufferedImage addWaterMark(String imgPath, String text, Font font, Color color, float x, float y,
			float alpha) throws Exception {
		BufferedImage targetImage = null;
		try {
			Font Dfont = (font == null) ? new Font("宋体", 20, 13) : font;
			Image img = ImageIO.read(new File(imgPath));
			// 创建目标图像文件
			targetImage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_RGB);
			Graphics2D g = targetImage.createGraphics();
			g.drawImage(img, 0, 0, null);
			g.setColor(color);
			g.setFont(Dfont);
			g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, alpha));
			g.drawString(text, x, y);
			g.dispose();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return targetImage;
	}

	/**
	 * 
	 * 添加文字水印操作(物理存盘,使用默认格式)
	 * 
	 * @author Lucifer Wong
	 * @param imgPath
	 *            待处理图片
	 * @param text
	 *            水印文字
	 * @param font
	 *            水印字体信息 不写默认值为宋体
	 * @param color
	 *            水印字体颜色
	 * @param x
	 *            水印位于图片左上角的 x 坐标值
	 * @param y
	 *            水印位于图片左上角的 y 坐标值
	 * @param alpha
	 *            水印透明度 0.1f ~ 1.0f
	 * @param destPath
	 *            文件存放路径
	 * @throws Exception
	 *             空指针或者转化失败
	 */
	public static void addWaterMark(String imgPath, String text, Font font, Color color, float x, float y, float alpha,
			String destPath) throws Exception {
		try {
			BufferedImage bufferedImage = addWaterMark(imgPath, text, font, color, x, y, alpha);
			ImageIO.write(bufferedImage, imageExg(imgPath), new File(destPath));
		} catch (Exception e) {
			throw new RuntimeException("图片添加文字水印异常");
		}
	}

	/**
	 * 
	 * 添加文字水印操作(物理存盘,自定义格式)
	 * 
	 * @author Lucifer Wong
	 * @param imgPath
	 *            待处理图片
	 * @param text
	 *            水印文字
	 * @param font
	 *            水印字体信息 不写默认值为宋体
	 * @param color
	 *            水印字体颜色
	 * @param x
	 *            水印位于图片左上角的 x 坐标值
	 * @param y
	 *            水印位于图片左上角的 y 坐标值
	 * @param alpha
	 *            水印透明度 0.1f ~ 1.0f
	 * @param format
	 *            添加水印后存储的格式
	 * @param destPath
	 *            文件存放路径
	 * @throws Exception
	 *             空指针或者转化失败
	 */
	public static void addWaterMark(String imgPath, String text, Font font, Color color, float x, float y, float alpha,
			String format, String destPath) throws Exception {
		try {
			BufferedImage bufferedImage = addWaterMark(imgPath, text, font, color, x, y, alpha);
			ImageIO.write(bufferedImage, format, new File(destPath));
		} catch (Exception e) {
			throw new RuntimeException("图片添加文字水印异常");
		}
	}

	/**
	 * 
	 * 添加图片水印操作,返回BufferedImage对象
	 * 
	 * @author Lucifer Wong
	 * @param imgPath
	 *            待处理图片
	 * @param markPath
	 *            水印图片
	 * @param x
	 *            水印位于图片左上角的 x 坐标值
	 * @param y
	 *            水印位于图片左上角的 y 坐标值
	 * @param alpha
	 *            水印透明度 0.1f ~ 1.0f
	 * @return 处理后的图片对象
	 * @throws Exception
	 *             空指针或者转化失败
	 * 
	 */
	public static BufferedImage addWaterMark(String imgPath, String markPath, int x, int y, float alpha)
			throws Exception {
		BufferedImage targetImage = null;
		try {
			// 加载待处理图片文件
			Image img = ImageIO.read(new File(imgPath));

			// 创建目标图象文件
			targetImage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_RGB);
			Graphics2D g = targetImage.createGraphics();
			g.drawImage(img, 0, 0, null);

			// 加载水印图片文件
			Image markImg = ImageIO.read(new File(markPath));
			g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, alpha));
			g.drawImage(markImg, x, y, null);
			g.dispose();
		} catch (Exception e) {
			throw new RuntimeException("添加图片水印操作异常");
		}
		return targetImage;

	}

	/**
	 * 添加图片水印操作(物理存盘,使用默认格式)
	 * 
	 * @author Lucifer Wong
	 * @param imgPath
	 *            待处理图片
	 * @param markPath
	 *            水印图片
	 * @param x
	 *            水印位于图片左上角的 x 坐标值
	 * @param y
	 *            水印位于图片左上角的 y 坐标值
	 * @param alpha
	 *            水印透明度 0.1f ~ 1.0f
	 * @param destPath
	 *            文件存放路径
	 * @throws Exception
	 *             空指针或者转化失败
	 * 
	 */
	public static void addWaterMark(String imgPath, String markPath, int x, int y, float alpha, String destPath)
			throws Exception {
		try {
			BufferedImage bufferedImage = addWaterMark(imgPath, markPath, x, y, alpha);
			ImageIO.write(bufferedImage, imageExg(imgPath), new File(destPath));
		} catch (Exception e) {
			throw new RuntimeException("添加图片水印异常");
		}
	}

	/**
	 * 
	 * 添加图片水印操作(物理存盘,自定义格式)
	 * 
	 * @author Lucifer Wong
	 * @param imgPath
	 *            待处理图片
	 * @param markPath
	 *            水印图片
	 * @param x
	 *            水印位于图片左上角的 x 坐标值
	 * @param y
	 *            水印位于图片左上角的 y 坐标值
	 * @param alpha
	 *            水印透明度 0.1f ~ 1.0f
	 * @param format
	 *            添加水印后存储的格式
	 * @param destPath
	 *            文件存放路径
	 * @throws Exception
	 *             空指针或者转化失败
	 * 
	 */
	public static void addWaterMark(String imgPath, String markPath, int x, int y, float alpha, String format,
			String destPath) throws Exception {
		try {
			BufferedImage bufferedImage = addWaterMark(imgPath, markPath, x, y, alpha);
			ImageIO.write(bufferedImage, format, new File(destPath));
		} catch (Exception e) {
			throw new RuntimeException("添加图片水印异常");
		}
	}

	/**
	 * 
	 * 压缩图片操作,返回BufferedImage对象
	 * 
	 * @author Lucifer Wong
	 * @param imgPath
	 *            待处理图片
	 * @param quality
	 *            图片质量(0-1之間的float值)
	 * @param width
	 *            输出图片的宽度 输入负数参数表示用原来图片宽
	 * @param height
	 *            输出图片的高度 输入负数参数表示用原来图片高
	 * @param autoSize
	 *            是否等比缩放 true表示进行等比缩放 false表示不进行等比缩放
	 * @return 处理后的图片对象
	 * @throws Exception
	 *             空指针或者转化失败
	 */
	public static BufferedImage compressImage(String imgPath, float quality, int width, int height, boolean autoSize)
			throws Exception {
		BufferedImage targetImage = null;
		if (quality < 0F || quality > 1F) {
			quality = DEFAULT_QUALITY;
		}
		try {
			Image img = ImageIO.read(new File(imgPath));
			// 如果用户输入的图片参数合法则按用户定义的复制,负值参数表示执行默认值
			int newwidth = (width > 0) ? width : img.getWidth(null);
			// 如果用户输入的图片参数合法则按用户定义的复制,负值参数表示执行默认值
			int newheight = (height > 0) ? height : img.getHeight(null);
			// 如果是自适应大小则进行比例缩放
			if (autoSize) {
				// 为等比缩放计算输出的图片宽度及高度
				double Widthrate = ((double) img.getWidth(null)) / (double) width + 0.1;
				double heightrate = ((double) img.getHeight(null)) / (double) height + 0.1;
				double rate = Widthrate > heightrate ? Widthrate : heightrate;
				newwidth = (int) (((double) img.getWidth(null)) / rate);
				newheight = (int) (((double) img.getHeight(null)) / rate);
			}
			// 创建目标图像文件
			targetImage = new BufferedImage(newwidth, newheight, BufferedImage.TYPE_INT_RGB);
			Graphics2D g = targetImage.createGraphics();
			g.drawImage(img, 0, 0, newwidth, newheight, null);
			// 如果添加水印或者文字则继续下面操作,不添加的话直接返回目标文件----------------------
			g.dispose();

		} catch (Exception e) {
			throw new RuntimeException("图片压缩操作异常");
		}
		return targetImage;
	}

	/**
	 * 压缩图片操作(文件物理存盘,使用默认格式)
	 * 
	 * @author Lucifer Wong
	 * @param imgPath
	 *            待处理图片
	 * @param quality
	 *            图片质量(0-1之間的float值)
	 * @param width
	 *            输出图片的宽度 输入负数参数表示用原来图片宽
	 * @param height
	 *            输出图片的高度 输入负数参数表示用原来图片高
	 * @param autoSize
	 *            是否等比缩放 true表示进行等比缩放 false表示不进行等比缩放
	 * @param destPath
	 *            文件存放路径
	 * @throws Exception
	 *             空指针或者转化失败
	 */
	public static void compressImage(String imgPath, float quality, int width, int height, boolean autoSize,
			String destPath) throws Exception {
		try {
			BufferedImage bufferedImage = compressImage(imgPath, quality, width, height, autoSize);
			ImageIO.write(bufferedImage, imageExg(imgPath), new File(destPath));
		} catch (Exception e) {
			throw new RuntimeException("图片压缩异常");
		}

	}

	/**
	 * 
	 * 压缩图片操作(文件物理存盘,可自定义格式)
	 * 
	 * @author Lucifer Wong
	 * @param imgPath
	 *            待处理图片
	 * @param quality
	 *            图片质量(0-1之間的float值)
	 * @param width
	 *            输出图片的宽度 输入负数参数表示用原来图片宽
	 * @param height
	 *            输出图片的高度 输入负数参数表示用原来图片高
	 * @param autoSize
	 *            是否等比缩放 true表示进行等比缩放 false表示不进行等比缩放
	 * @param format
	 *            压缩后存储的格式
	 * @param destPath
	 *            文件存放路径
	 * @throws Exception
	 *             空指针或者转化失败
	 */
	public static void compressImage(String imgPath, float quality, int width, int height, boolean autoSize,
			String format, String destPath) throws Exception {
		try {
			BufferedImage bufferedImage = compressImage(imgPath, quality, width, height, autoSize);
			ImageIO.write(bufferedImage, format, new File(destPath));
		} catch (Exception e) {
			throw new RuntimeException("图片压缩异常");
		}
	}

	/**
	 * 执行透明化的核心算法
	 * 
	 * @author Lucifer Wong
	 * @param img
	 *            图片对象
	 * @param alpha
	 *            透明度
	 * @throws Exception
	 *             空指针或者转化失败
	 */
	public static void executeRGB(BufferedImage img, int alpha) throws Exception {
		int rgb = 0;// RGB值
					// x表示BufferedImage的x坐标，y表示BufferedImage的y坐标
		for (int x = img.getMinX(); x < img.getWidth(); x++) {
			for (int y = img.getMinY(); y < img.getHeight(); y++) {
				// 获取点位的RGB值进行比较重新设定
				rgb = img.getRGB(x, y);
				int R = (rgb & 0xff0000) >> 16;
				int G = (rgb & 0xff00) >> 8;
				int B = (rgb & 0xff);
				if (((255 - R) < 30) && ((255 - G) < 30) && ((255 - B) < 30)) {
					rgb = ((alpha + 1) << 24) | (rgb & 0x00ffffff);
					img.setRGB(x, y, rgb);
				}
			}
		}
	}

	/**
	 * 图片格式转化操作返回BufferedImage对象
	 * 
	 * @author Lucifer Wong
	 * @param bufferedImag
	 *            BufferedImage图片转换对象
	 * @param format
	 *            待转换的格式 jpeg,gif,png,bmp等
	 * @param destPath
	 *            目标文件地址
	 * @throws Exception
	 *             空指针或者转化失败
	 */
	public static void formatConvert(BufferedImage bufferedImag, String format, String destPath) throws Exception {
		try {
			ImageIO.write(bufferedImag, format, new File(destPath));
		} catch (IOException e) {
			throw new RuntimeException("文件格式转换出错");
		}
	}

	/**
	 * 图片格式转化操作(文件物理存盘)
	 * 
	 * @author Lucifer Wong
	 * @param imgPath
	 *            原始图片存放地址
	 * @param format
	 *            待转换的格式 jpeg,gif,png,bmp等
	 * @param destPath
	 *            目标文件地址
	 * @throws Exception
	 *             空指针或者转化失败
	 */
	public static void formatConvert(String imgPath, String format, String destPath) throws Exception {
		try {
			BufferedImage bufferedImage = ImageIO.read(new File(imgPath));
			ImageIO.write(bufferedImage, format, new File(destPath));
		} catch (IOException e) {
			throw new RuntimeException("文件格式转换出错");
		}
	}

	/**
	 * 获取图片文件的后缀
	 * 
	 * @author Lucifer Wong
	 * @param imgPath
	 *            图片原文件存放地址
	 * @return 图片格式
	 * @throws Exception
	 *             空指针或者转化失败
	 */
	private static String imageExg(String imgPath) throws Exception {
		String[] filess = imgPath.split("\\\\");
		String[] formats = filess[filess.length - 1].split("\\.");
		return formats[formats.length - 1];
	}

	/**
	 * 图片黑白化操作(文件物理存盘,使用默认格式)
	 * 
	 * @author Lucifer Wong
	 * @param imgPath
	 *            处理的图片对象
	 * @param destPath
	 *            目标文件地址
	 * @throws Exception
	 *             空指针或者转化失败
	 *
	 */
	public static void imageGray(String imgPath, String destPath) throws Exception {
		imageGray(imgPath, imageExg(imgPath), destPath);
	}

	/**
	 * 图片黑白化操作(文件物理存盘,可自定义格式)
	 * 
	 * @author Lucifer Wong
	 * @param imgPath
	 *            处理的图片对象
	 * @param format
	 *            图片格式
	 * @param destPath
	 *            目标文件地址
	 * @throws Exception
	 *             空指针或者转化失败
	 * 
	 */
	public static void imageGray(String imgPath, String format, String destPath) throws Exception {
		try {
			BufferedImage bufferedImage = ImageIO.read(new File(imgPath));
			ColorSpace cs = ColorSpace.getInstance(ColorSpace.CS_GRAY);
			ColorConvertOp op = new ColorConvertOp(cs, null);
			bufferedImage = op.filter(bufferedImage, null);
			ImageIO.write(bufferedImage, format, new File(destPath));
		} catch (Exception e) {
			throw new RuntimeException("图片灰白化异常");
		}
	}

	/**
	 * 图片透明化操作返回BufferedImage对象
	 * 
	 * @author Lucifer Wong
	 * @param imgPath
	 *            图片路径
	 * @return 透明化后的图片对象
	 * @throws Exception
	 *             空指针或者转化失败
	 */
	public static BufferedImage lucency(String imgPath) throws Exception {
		BufferedImage targetImage = null;
		try {
			// 读取图片
			BufferedImage img = ImageIO.read(new FileInputStream(imgPath));
			// 透明度
			int alpha = 0;
			// 执行透明化
			executeRGB(img, alpha);
			targetImage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_RGB);
			Graphics2D g = targetImage.createGraphics();
			g.drawImage(img, 0, 0, null);
			g.dispose();
		} catch (Exception e) {
			throw new RuntimeException("图片透明化执行异常");
		}
		return targetImage;
	}

	/**
	 * 图片透明化操作(文件物理存盘,使用默认格式)
	 * 
	 * @author Lucifer Wong
	 * @param imgPath
	 *            图片路径
	 * @param destPath
	 *            图片存放路径
	 * @throws Exception
	 *             空指针或者转化失败
	 */
	public static void lucency(String imgPath, String destPath) throws Exception {
		try {
			BufferedImage bufferedImage = lucency(imgPath);
			ImageIO.write(bufferedImage, imageExg(imgPath), new File(destPath));
		} catch (Exception e) {
			throw new RuntimeException("图片透明化异常");
		}
	}

	/**
	 * 图片透明化操作(文件物理存盘,可自定义格式)
	 * 
	 * @author Lucifer Wong
	 * @param imgPath
	 *            图片路径
	 * @param format
	 *            图片格式
	 * @param destPath
	 *            图片存放路径
	 * @throws Exception
	 *             空指针或者转化失败
	 */
	public static void lucency(String imgPath, String format, String destPath) throws Exception {
		try {
			BufferedImage bufferedImage = lucency(imgPath);
			ImageIO.write(bufferedImage, format, new File(destPath));
		} catch (Exception e) {
			throw new RuntimeException("图片透明化异常");
		}
	}

	/**
	 * 等比例压缩算法： 算法思想：根据压缩基数和压缩比来压缩原图，生产一张图片效果最接近原图的缩略图
	 * 
	 * @author Lucifer Wong
	 * @param srcURL
	 *            原图地址
	 * @param deskURL
	 *            缩略图地址
	 * @param comBase
	 *            压缩基数
	 * @param scale
	 *            压缩限制(宽/高)比例 一般用1：
	 *            当scale&gt;=1,缩略图height=comBase,width按原图宽高比例;若scale&lt;1,
	 *            缩略图width= comBase,height按原图宽高比例
	 * @throws Exception
	 *             空指针或者转化失败
	 */
	@SuppressWarnings("restriction")
	public static void saveMinPhoto(String srcURL, String deskURL, double comBase, double scale) throws Exception {
		File srcFile = new java.io.File(srcURL);
		Image src = ImageIO.read(srcFile);
		int srcHeight = src.getHeight(null);
		int srcWidth = src.getWidth(null);
		int deskHeight = 0;// 缩略图高
		int deskWidth = 0;// 缩略图宽
		double srcScale = (double) srcHeight / srcWidth;
		/** 缩略图宽高算法 */
		if ((double) srcHeight > comBase || (double) srcWidth > comBase) {
			if (srcScale >= scale || 1 / srcScale > scale) {
				if (srcScale >= scale) {
					deskHeight = (int) comBase;
					deskWidth = srcWidth * deskHeight / srcHeight;
				} else {
					deskWidth = (int) comBase;
					deskHeight = srcHeight * deskWidth / srcWidth;
				}
			} else {
				if ((double) srcHeight > comBase) {
					deskHeight = (int) comBase;
					deskWidth = srcWidth * deskHeight / srcHeight;
				} else {
					deskWidth = (int) comBase;
					deskHeight = srcHeight * deskWidth / srcWidth;
				}
			}
		} else {
			deskHeight = srcHeight;
			deskWidth = srcWidth;
		}
		BufferedImage tag = new BufferedImage(deskWidth, deskHeight, BufferedImage.TYPE_3BYTE_BGR);
		tag.getGraphics().drawImage(src, 0, 0, deskWidth, deskHeight, null); // 绘制缩小后的图
		FileOutputStream deskImage = new FileOutputStream(deskURL); // 输出到文件流
		JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(deskImage);
		encoder.encode(tag); // 近JPEG编码
		deskImage.close();
	}

	/**
	 * 保存文件到服务器临时路径(用于文件上传)
	 * 
	 * @author Lucifer Wong
	 * @param fileName
	 *            输出文件的临时名
	 * @param is
	 *            输入流
	 * @return 文件全路径
	 */
	public static String writeFile(String fileName, InputStream is) {
		if (fileName == null || fileName.trim().length() == 0) {
			return null;
		}
		try {
			/** 首先保存到临时文件 */
			FileOutputStream fos = new FileOutputStream(fileName);
			byte[] readBytes = new byte[512];// 缓冲大小
			int readed = 0;
			while ((readed = is.read(readBytes)) > 0) {
				fos.write(readBytes, 0, readed);
			}
			fos.close();
			is.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return fileName;
	}

	/**
	 * 直接指定压缩后的宽高： (先保存原文件，再压缩、上传)
	 * 
	 * @author Lucifer Wong
	 * @param oldFile
	 *            要进行压缩的文件全路径
	 * @param width
	 *            压缩后的宽度
	 * @param height
	 *            压缩后的高度
	 * @param quality
	 *            压缩质量
	 * @param smallIcon
	 *            文件名的小小后缀(注意，非文件后缀名称),入压缩文件名是yasuo.jpg,则压缩后文件名是yasuo(+smallIcon
	 *            ).jpg
	 * @return 返回压缩后的文件的全路径
	 */
	@SuppressWarnings("restriction")
	public static String zipImageFile(String oldFile, int width, int height, float quality, String smallIcon) {
		if (oldFile == null) {
			return null;
		}
		String newImage = null;
		try {
			/** 对服务器上的临时文件进行处理 */
			Image srcFile = ImageIO.read(new File(oldFile));
			/** 宽,高设定 */
			BufferedImage tag = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
			tag.getGraphics().drawImage(srcFile, 0, 0, width, height, null);
			String filePrex = oldFile.substring(0, oldFile.indexOf('.'));
			/** 压缩后的文件名 */
			newImage = filePrex + smallIcon + oldFile.substring(filePrex.length());
			/** 压缩之后临时存放位置 */
			FileOutputStream out = new FileOutputStream(newImage);
			JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
			JPEGEncodeParam jep = JPEGCodec.getDefaultJPEGEncodeParam(tag);
			/** 压缩质量 */
			jep.setQuality(quality, true);
			encoder.encode(tag, jep);
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return newImage;
	}

}