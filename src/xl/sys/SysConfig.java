package xl.sys;

import java.io.IOException;
import java.util.Properties;


public class SysConfig {

	/**
	 * 说明：是否保存异常信息到本地日志文件
	 * @author wangxl
	 * 2018 1 18
	 */
	public static boolean IsSaveLogToFile = true; 
	/**
	 * 说明：是否打印日常信息到控制台
	 * @author wangxl
	 * 2018 1 18
	 */
	public static boolean IsShowLogConsole = true; 
	
	/**
	 * 说明：本地日志文件保存路径
	 * @author wangxl
	 * 2018 1 18
	 */
	public static String LogPath = isWindows(); 
	public static int isWindows = 0;
	public static String isWindows() {  
		if(isWindows==0){
			if(System.getProperties().getProperty("os.name").toUpperCase().indexOf("WINDOWS")!= -1) 
				isWindows = 1;
			else
				isWindows = 2;
		}
		if(isWindows==1){
			 return isPath()+"\\Clent\\";
		}
		return "/opt/"; 
	}  
	public static String path = null;
	public static String isPath() {  
		if(path==null){
			String pa = System.getProperty("user.dir"); 
			int index=  pa.indexOf("config\\client");
			if(index != -1){
				path = pa.substring(0,index)+"config\\client";
			}else{
				path = "C:\\WSocket";
			}
		}else{
			return path;
		}
		return path;
	}
	/**
	 *  服务器Socket端口 :12306
	 */
	public static int Server_Socket_Port = 12306;
	
	/**
	 * 初始化全局变量
	 */
	public static boolean init(){
		Properties prop = new Properties();
		try {
			prop.load(SysConfig.class.getResourceAsStream("config.properties"));
			Server_Socket_Port = Integer.valueOf(prop.getProperty("Server_Socket_Port"));
			IsSaveLogToFile = "true".equals(prop.getProperty("IsSaveLogToFile")) ? true : false;
			return true;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}
}
