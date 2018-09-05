package xl.sys;

import xl.util.DateUtil;
import xl.util.Doc;



/**
 * 说明：日志打印类
 * @author wangxl
 * 2018 1 18
 */
public class Log {
	
	/**
	 * 说明：打印日志 - 根据配置类自动判断输出环境
	 * @param value 异常内容
	 * @author wangxl
	 * 2018 1 18
	 */
	public static void  e(String value){
		String thisDate = DateUtil.getDate("yyyy-MM-dd HH:mm:ss");//获得当前时间
		String thisDateYMD = DateUtil.getDate("yyyy-MM-dd");//获得当前时间
		if(thisDate != null){
			thisDate  = "["+thisDate+"] ";
		}
		//写入异常到本地文件
		if(SysConfig.IsSaveLogToFile){
			Doc.writeDocFromStr2(thisDate + value + "\r\n", SysConfig.LogPath+thisDateYMD+".log", true);
		}
		//打印日常信息到控制台
		if(SysConfig.IsShowLogConsole){
			System.err.println(thisDate + value);
		}
	}
	
	/**
	 * 说明：打印日志(异常块中使用) - 根据配置类自动判断输出环境
	 * @param value 异常内容
	 * @param obj Exception对象
	 * @author wangxl
	 * 2018 1 18
	 */
	public static void  e(String value,Object e){
		String excValue = "";
		if(e instanceof Exception){
			excValue = getExceptionDetail((Exception) e);
		}else if(e instanceof String ){
			excValue = (String) e;
		}
		String thisDate = DateUtil.getDate("yyyy-MM-dd HH:mm:ss");//获得当前时间
		String thisDateYMD = DateUtil.getDate("yyyy-MM-dd");//获得当前时间
		if(thisDate != null){
			thisDate  = "["+thisDate+"] ";
		}
		//写入异常到本地文件
		if(SysConfig.IsSaveLogToFile){
			Doc.writeDocFromStr2("\r\n"+thisDate + value+"\r\n"+excValue, SysConfig.LogPath+thisDateYMD+".log", true);
		}
		//打印日常信息到控制台
		if(SysConfig.IsShowLogConsole){
			System.out.println(thisDate + value+"\r\n"+excValue);
		}
	}
	
	/**
	 * 说明：打印日志 - 仅打印控制台
	 * @param value 异常内容
	 * @author wangxl
	 * 2018 1 18
	 */
	public static void  c(String value){
		String thisDate = DateUtil.getDate("yyyy-MM-dd HH:mm:ss");//获得当前时间
		if(thisDate != null){
			thisDate  = "["+thisDate+"] ";
		}
		System.out.println(thisDate + value);
	}
	
	/**
	 * 说明：打印日志 - 仅输出到本地日志文件
	 * @param value 异常内容
	 * @author wangxl
	 * 2018 1 18
	 */
	public static void  f(String value){
		String thisDate = DateUtil.getDate("yyyy-MM-dd HH:mm:ss");//获得当前时间
		String thisDateYMD = DateUtil.getDate("yyyy-MM-dd");//获得当前时间
		if(thisDate != null){
			thisDate  = "\r\n["+thisDate+"] ";
		}
		//写入异常到本地文件
		Doc.writeDocFromStr2(thisDate + value, SysConfig.LogPath+thisDateYMD+".log", true);
	}
	
	/**
	 * 说明：获得异常详细信息
	 * @author wangxl
	 * 2018 1 18
	 */
	public static String getExceptionDetail(Exception e) {
		StringBuffer stringBuffer = new StringBuffer(e.toString() + "\r\n");
		StackTraceElement[] messages = e.getStackTrace();
		int length = messages.length;
		for (int i = 0; i < length; i++) {
			stringBuffer.append("\t"+messages[i].toString()+"\r\n");
		}
		return stringBuffer.toString();
	}
	
}
