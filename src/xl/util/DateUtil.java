package xl.util;

import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Pattern;

import xl.sys.Log;

/**
 * 说明：时间工具类
 * @author wangxl
 * 2017 10 17
 */
public class DateUtil {
	
	/**
	 * 说明：获得指定格式的当前系统时间
	 * type:yyyy-MM-dd HH:mm:ss   | yyyy-MM-dd
	 * @author wangxl
	 * 2018 1 18
	 */
	public static String getDate(String type){
		Date day=new Date();    
		try{
			SimpleDateFormat df = new SimpleDateFormat(type); 
			return df.format(day);   
		}catch(Exception e){
			Log.e("DateUtil.getDate : {获得指定格式的当前系统时间} 出现异常!",e);
			e.printStackTrace();
			return day.toString();
		}
	}
	
}
