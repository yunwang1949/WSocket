/**
 * Copyright (C) GuangZhou Siang Software, Inc.
 * All Rights Reserved.  
 */
package xl.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;

import xl.sys.Log;

/**
 * 说明：文件工具类
 * @author wangxl
 * 2018 1 18
 */
public class Doc {
	public static final String Encode = "utf-8";
	/**
	 * 将字符串写入到文件中 (支持覆盖写入)   文件不存在时返回false
	 * @param str 要写入的字符串
	 * @param path  文件的物理路径
	 * @param isType  是否追加写入true追加/false覆盖
	 * @return boolean false失败/true成功
	 * @throws
	 */
	public static boolean writeDocFromStr(String str, String path, boolean isType){
		if (path == null || path.length() == 0) {
			return false;
		}
		try {
				File file = new File(path); // 判断该路径是否为文件
				if (!file.isFile()) {
					return false;
				}
				FileOutputStream fos = new FileOutputStream(file,isType);
				fos.write(str.getBytes(Encode));
				fos.close();
		} catch (Exception e) {
			Log.e(path+":文件写入失败!",e);
			e.printStackTrace();
			return false;
		}
		return true;
	}
	/**
	 * 将字符串写入到文件中 (支持覆盖写入) 文件不存在自动创建
	 * @param str 要写入的字符串
	 * @param path  文件的物理路径
	 * @param isType  是否追加写入true追加/false覆盖
	 * @return boolean false失败/true成功
	 * @throws
	 */
	public static boolean writeDocFromStr2(String str, String path, boolean isType){
		if (path == null || path.length() == 0) {
			return false;
		}
		try {
				File file = new File(path); // 判断该路径是否为文件
				if(!file.exists())
					file.getParentFile().mkdirs();
				if (!file.isFile()) {
					if(!createNewDoc(path)){
						return false;
					}
				}
				FileOutputStream fos = new FileOutputStream(file,isType);
				fos.write(str.getBytes(Encode));
				fos.close();
		} catch (Exception e) {
			Log.e(path+":文件写入失败!",e);
			e.printStackTrace();
			return false;
		}
		return true;
	}
	/**
	 * 将字节写入文件中
	 * @param s_Path
	 * @param args
	 * @return
	 */
	public static boolean createdocbyte(String s_Path, byte[] args) {
		if (s_Path == null || s_Path.length() == 0) {
			return false;
		}
		if (args == null || args.length == 0) {
			return false;
		}
		try {
				File file = new File(s_Path); // 判断该路径是否为文件
				if (!file.isFile()) {
					return false;
				}
				FileOutputStream fos = new FileOutputStream(file);
				fos.write(args);
				fos.flush();
				fos.close();
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * 获取一个输入流
	 * @param path
	 *            文件的物理路径
	 * @return InputStream
	 * @throws
	 */
	public static InputStream getInputStream(String path) throws Exception {
		if (path == null || path.length() == 0) {
			return null;
		}
		try {
				File file = new File(path); // 判断该路径是否为文件
				if (!file.isFile()) {
					return null;
				}
				return new FileInputStream(path);
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * 将一个输入流写入到文件中
	 * 
	 * @param ins
	 *            输入流
	 * @return boolean
	 * @throws
	 */
	public static boolean writeDocFromInputStream(InputStream ins, String path) throws Exception {
		if (path == null || path.length() == 0) {
			return false;
		}
		FileOutputStream fout = null;
		try {
			if (ins != null) {
				fout = new FileOutputStream(path);
				byte[] buffer = new byte[1024];
				int i = 0;
				while ((i = ins.read(buffer)) != -1) {
					fout.write(buffer, 0, i);
				}
				return true;
			}
		} catch (Exception e) {
			throw e;
		} finally {
			try {
				if (fout != null) {
					fout.close();
				}
			} catch (Exception e2) {
			}
		}
		return false;
	}

	/**
	 * 获取某个文件/文件夹的纯粹的文件名称(不含扩展名)
	 * 
	 * @param Path
	 *            文件的全名或路径
	 * @return 文件的纯名称
	 */
	public static String getPureName(String path) {
		if (path == null || path.length() == 0) {
			return null;
		}
		String FileName = getFullName(path);
		if (FileName == null || FileName.length() == 0)
			return null;

		int i = FileName.lastIndexOf(".");
		if (i >= 0)
			return FileName.substring(0, i);
		else
			return FileName;
	}

	/**
	 * 将 //转换成 / 将 \转换成 /
	 * 
	 * @param path
	 * @return
	 */
	public static String parsePath(String path) {
		int index = 0;
		while ((index = path.indexOf("//")) >= 0)
			path = path.substring(0, index) + path.substring(index + 1);
		while ((index = path.indexOf("\\")) >= 0)
			path = path.substring(0, index) + "/" + path.substring(index + 1);
		return path;
	}

	/**
	 * 获取一个文件类型的图片Url地址
	 * 
	 * @param type
	 *            文件类型
	 * @return String
	 * @throws
	 */
	public static String getImgByDocType(String type) {
		if (type == null || type.length() == 0) {
			return null;
		}
		type = type.toLowerCase();
		String Img = "/siang/img/" + type + ".gif";
		return Img;
	}

	/**
	 * 判断一个路径是否为文件
	 * 
	 * @param path
	 *            物理路径
	 * @return boolean
	 * @throws
	 */
	public static boolean isFile(String path) {
		if (path == null || path.length() == 0) {
			return false;
		}

		try {
				return new File(path).isFile();
		} catch (Exception e) {
			return false;
		}

	}

	
	/**
	 * @方法说明： 使用文件通道的方式复制文件
	 * @作者：XiaoLin 
	 * @参数： @param s 源文件
	 * @参数： @param t 复制到的新文件
	 * @时间： 2016-7-12 上午9:18:10
	 */
    public static void CopyfileChannel(File s, File t) {
        FileInputStream fi = null;
        FileOutputStream fo = null;
        FileChannel in = null;
        FileChannel out = null;
        try {
            fi = new FileInputStream(s);
            fo = new FileOutputStream(t);
            in = fi.getChannel();//得到对应的文件通道
            out = fo.getChannel();//得到对应的文件通道
            in.transferTo(0, in.size(), out);//连接两个通道，并且从in通道读取，然后写入out通道
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                fi.close();
                in.close();
                fo.close();
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    /** 
     * @方法说明： 复制一个目录及其子目录、文件到另外一个目录 (不能用于直接复制文件)
     * @作者：XiaoLin 
     * @参数： @param src原文件夹
     * @参数： @param dest目标路径
     * @参数： @throws IOException 
     * @时间： 2016-7-11 下午2:53:27
     */
    public static void copyFolder(File src, File dest) throws IOException {  
        if (src.isDirectory()) {  
            if (!dest.exists()) {  
                dest.mkdir();  
            }  
            String files[] = src.list();  
            for (String file : files) {  
                File srcFile = new File(src, file);  
                File destFile = new File(dest, file);  
                // 递归复制  
                copyFolder(srcFile, destFile);  
            }  
        } else {  
            InputStream in = new FileInputStream(src);  
            OutputStream out = new FileOutputStream(dest);  
      
            byte[] buffer = new byte[1024];  
      
            int length;  
              
            while ((length = in.read(buffer)) > 0) {  
                out.write(buffer, 0, length);  
            }  
            in.close();  
            out.close();  
        }  
    }  
	/**
	 * 判断一个路径是否存在
	 * 
	 * @param s_path
	 *            绝对路径
	 * @return boolean
	 * @throws Exception
	 */
	public static boolean existPath(String s_Path) {
		if (s_Path == null || s_Path.length() == 0) {
			return false;
		}
		try {
			File file = new File(s_Path);
			return file.exists();
		} catch (Exception e) {

			return false;
		}
	}

	/**
	 * 判断一个路径是否为目录
	 * 
	 * @param s_path
	 *            绝对路径
	 * @return boolean
	 * @throws Exception
	 */
	public static boolean isDir(String s_path) {
		if (null == s_path || s_path.length() == 0) {
			return false;
		}
		try {
			String tmppath = parsePath(s_path);
			File file1 = new File(tmppath);
			return file1.isDirectory();
		} catch (Exception e) {

			return false;
		}
	}

	

	

	/**
	 * 判断一个路径是否为文档
	 * 
	 * @param s_path
	 *            绝对路径
	 * @return boolean
	 * @throws Exception
	 */
	public static boolean isDoc(String s_path) {
		if (null == s_path || s_path.length() == 0) {
			return false;
		}
		try {
			File file = new File(s_path);
			return file.isFile();
		} catch (Exception e) {

			return false;
		}
	}

	/**
	 * 获取一个文档的输入流
	 * 
	 * @param s_path
	 *            绝对路径
	 * @return FileChannel
	 * @throws Exception
	 */
	public static InputStream getDocInStream(String s_path) {
		// 检查参数
		if (null == s_path || s_path.length() == 0) {
			return null;
		}

		// 检查该路径是否为文件
		if (!isDoc(s_path)) {
			return null;
		}
		try {
				return new FileInputStream(s_path);
		} catch (Exception e) {

			return null;
		}
	}

	/**
	 * 创建一个空文档
	 * 
	 * @param location
	 * @return
	 */
	public static boolean createNewDoc(String location) throws IOException {
		if (location != null && location.length() > 255) {
			return false;
		}

		try {
			File file = new File(location);
			if (file.exists())
				return false;
			file.createNewFile();
			return new File(location).exists();
		} catch (Exception e) {
			Log.e(location+" 文件创建失败",e);
			return false;
		}

	}

	/**
	 * TODO 创建一个文档
	 * 
	 * @param s_Path
	 *            文件路径
	 * @param fcin
	 *            输入流
	 * @return boolean
	 * @throws Exception
	 */
	public static boolean createDoc(String s_path, java.io.InputStream fcin) throws Exception {
		// 检查参数
		if (null == s_path || s_path.length() == 0) {
			return false;
		}

		// 检查路径长度是否大于255
		if (null != s_path && s_path.length() > 255) {
			return false;
		}
		// 检查该路径是否存在
		if (Doc.existPath(s_path)) {
			return false;
		}
		FileOutputStream outputStream = null;
		try {
				File file = new File(s_path);
				file.createNewFile();
				int flag = 0;
				if (Doc.isDoc(s_path)) {
					// 获取文档输出流
					outputStream = new FileOutputStream(file);
					// 写文件
					if (fcin != null) {
						byte[] buffer = new byte[1024];
						int i = 0;
						while ((i = fcin.read(buffer)) != -1) {
							outputStream.write(buffer, 0, i);
						}
						flag = 1;
					}
					if (flag == 0) {
						return false;
					}
				}

			return true;
		} catch (Exception e) {
			return false;
		} finally {
			try {
				if (outputStream != null) {
					outputStream.close();
				}
				if (fcin != null) {
					fcin.close();
				}
			} catch (Exception e) {
				return false;
			}
		}
	}

	/**
	 * 创建一个文档
	 * 
	 * @param s_Path
	 *            文件路径
	 * @param fcin
	 *            输入流
	 * @return boolean
	 * @throws Exception
	 */
	public static boolean duandianCreateDoc(String s_path, java.io.InputStream fcin) throws Exception {
		// 检查参数
		if (null == s_path || s_path.length() == 0) {
			return false;
		}
		// 检查路径长度是否大于255
		if (null != s_path && s_path.length() > 255) {
			return false;
		}
		RandomAccessFile ra = null;
		try {

			
				ra = new RandomAccessFile(s_path, "rw");
				long skipLength = ra.length();
				byte[] buff = new byte[1024];
				int readed = -1;
				ra.seek(skipLength);
				long tmpLength = 0;
				while ((readed = fcin.read(buff)) > 0) {
					tmpLength += readed;
					if (skipLength == 0) {
						ra.write(buff, 0, readed);
					} else {
						if (tmpLength > skipLength) {
							ra.write(buff, 0, readed);
						}
					}
				}
			return true;
		} catch (Exception e) {
			return false;
		} finally {
			try {
				if (ra != null) {
					ra.close();
				}
			} catch (Exception e) {

				return false;
			}
		}
	}

	/**
	 * 创建一个目录结构 支持多层如果已经存在则不创建
	 * 
	 * @param s_Path
	 */
	public static boolean createDirs(String s_Path) {
		if (s_Path == null || s_Path.length() == 0) {
			return false;
		}
		if (s_Path.length() > 255) {
			return false;
		}
		try {
			File dirs = new File(s_Path);
			if (dirs.exists()) {
				return false;
			}
			return dirs.mkdirs();
		} catch (Exception e) {

			return false;
		}
	}

	/**
	 * 获取一个路径的上级路径
	 * 
	 * @param path
	 *            文件的物理路径
	 * @return String
	 * @throws
	 */
	public static String getParentPath(String path) {
		if (path == null || path.length() == 0)
			return "";

		if (path.equals("/"))
			return "";

		int index = path.lastIndexOf("/");
		if (index < 0) {
			// 这不是一个路径
			return "";
		} else if (index == 0) {
			return "/";
		} else {
			return path.substring(0, index);
		}
	}

	/**
	 * 根据一个文件路径获取文件全名
	 * 
	 * @param path
	 *            文件的物理路径
	 * @return String
	 * @throws
	 */
	public static String getFullName(String path) {
		if (path == null || path.length() == 0)
			return null;
		if (path.equals("/")) {
			return path;
		}
		int i = path.lastIndexOf("/");
		if (i >= 0)
			return path.substring(i + 1);
		else
			return path;
	}

	/**
	 * 根据一个文件路径获取名称，不包含扩展名
	 * 
	 * @param path
	 *            文件的物理路径
	 * @return String
	 * @throws
	 */
	public static String getName(String path) {
		if (path == null || path.length() == 0)
			return null;
		if (path.equals("/")) {
			return path;
		}
		int i = path.lastIndexOf("/");
		int j = path.lastIndexOf(".");
		if (i >= 0)
			return path.substring(i + 1, j);
		else
			return path;
	}

	/**
	 * 根据一个文件路径获取文件扩展名
	 * 
	 * @param path
	 *            可以是文件全名、文件相对位置、文件绝对位置
	 * @return String
	 * @throws
	 */
	public static String getDocType(String path) {
		if (path == null || path.length() == 0) {
			return null;
		}
		int charAt = path.lastIndexOf(".");
		if (charAt > 0) {
			return path.substring(charAt + 1, path.length());
		}
		return "";

	}

	/**
	 * 创建一个指定路径的文件夹（不支持多层创建）。 如果该路径已经存在文件夹，则抛出异常 如果该路径不存在，则尝试创建该文件夹。返回创建结果
	 * 
	 * @param path
	 *            统一命名空间中的路径
	 * @return
	 */
	public static Integer createDir(String path) throws Exception {
		if (existPath(path)) {
			return 3;// 该路径已存在文档或目录
			// throw new Exception("该路径已存在文档或目录: " + path);
		}
		if (path.length() > 255) {
			return 4;// 文档或者目录名称过长
			// throw new Exception("文档或者目录名称过长: " + path);
		}
		if (!isDir(getParentPath(path))) {
			return 5;// 目录或者文档路径不存在
			// throw new Exception("目录或者文档路径不存在: " + getParentPath(path));
		}

		try {
			if (new File(path).mkdir()) {
				return 1;
			} else {
				return 2;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return 2;
		}
	}

	/**
	 * 删除一个指定路径的文件夹(要求该文件夹为空文件夹) 如果该路径不存在文件夹，则返回false 如果该文件夹非空，抛出异常 
	 * 
	 * @param path
	 *            统一命名空间中的路径
	 * @return
	 */
	public static boolean deleteEmptyDir(String path) throws Exception {
		// 如果文件夹名称为空，直接返回false
		if (path == null || path.length() == 0)
			return false;
		if (!isDir(path)) {
			return false;
		}
		try {
			// 删除空文件夹
			boolean flag = delete(path);
			return flag;
		} catch (Exception e) {

			return false;
		}
	}

	/**
	 * 删除指定文件 如果指定路径为空，返回false 如果找不到指定路径，抛出异常，提示找不到该路径
	 * 
	 * @param path
	 * @return
	 */
	public static boolean deleteDoc(String path) throws Exception {
		if (path == null || path.length() == 0)
			return false;
		if (!isDoc(path)) {
			return false;
		}
		try {
			// 删除空文件
			boolean flag = delete(path);
			return flag;
		} catch (Exception e) {

			return false;
		}
	}

	/**
	 * 删除一个文件或者空文件夹 如果该路径不存在，则返回false 如果是非空文件夹，返回false
	 * 
	 * @param location
	 * @return
	 */

	public static boolean delete(String location) throws IOException {
		try {
				File file = new File(location);
				if (file.isFile()) { // 是否为文件
					file.delete();
				}
				if (file.isDirectory()) { // 是否为文件夹
					if (file.list() == null || file.list().length == 0) {
						file.delete();
					}
				}
				if (file.exists()) { // 如果存在，则返回false
					return false;
				} else {
					return true;
				}
		} catch (Exception e) {
			e.printStackTrace();

			return false;
		}
	}

	/**
	 * 重命名一个文件 如果文件路径为空，返回false 如果文件名为空，返回false; 如果文件路径不存在，报异常 黄杰加
	 * 
	 * @param s_SrcPath
	 *            要重命名文件的路径
	 * @param s_NewName
	 *            要给文件命名的新名称
	 * @return
	 * @throws Exception
	 */
	public static boolean renameDoc(String s_SrcPath, String s_NewName) throws Exception {
		if (null == s_SrcPath || s_SrcPath.length() == 0) {
			return false;
		}
		if (null == s_NewName || s_NewName.length() == 0) {
			return false;
		}
		if (!existPath(s_SrcPath)) {
			throw new Exception("目录或者文档路径不存在: " + s_SrcPath);
		}
		try {
				File file = new File(s_SrcPath);
				File file2 = new File(s_NewName);
				boolean result = file.renameTo(file2);
				return result;
		} catch (Exception e) {
			return false;
		}

	}

	/**
	 * 重命名一个文件夹 如果文件路径为空，返回false 如果文件名为空，返回false; 如果文件路径不存在，报异常
	 * 
	 * @param s_SrcPath
	 *            要重命名文件的路径
	 * @param s_NewName
	 *            要给文件命名的新名称
	 * @return
	 * @throws Exception
	 */
	public static boolean renameDir(String s_SrcPath, String s_NewName) throws Exception {
		if (null == s_SrcPath || s_SrcPath.length() == 0) {
			return false;
		}
		if (null == s_NewName || s_NewName.length() == 0) {
			return false;
		}
		if (!existPath(s_SrcPath)) {
			throw new Exception("目录或者文档路径不存在: " + s_SrcPath);
		}
		try {
				File file = new File(s_SrcPath);
				File file2 = new File(s_NewName);
				boolean result = file.renameTo(file2);
				return result;
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * 删除一个非空文件夹，该函数请谨慎使用！
	 * 
	 * @param location
	 * @return
	 * @throws IOException
	 */
	public static boolean deleteDir(String s_Path) throws IOException {
		File file = new File(s_Path);
		if (!file.isDirectory())
			return false;
		String[] list = file.list();
		if (list != null && list.length > 0) {
			for (int i = 0; i < list.length; i++) {
				String tmpPath = s_Path + "/" + list[i];
				File tmpFile = new File(tmpPath);
				if (tmpFile.isFile()) {
					if (!delete(tmpPath))
						return false;
				} else {
					if (!deleteDir(tmpPath))
						return false;
				}
			}
		}
		return delete(s_Path);
	}
	
}
