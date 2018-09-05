package xl.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.json.JSONArray;
import org.json.JSONObject;

import com.wakeboy.cache.util.JSONHelper;

import xl.sys.Log;

/**
 * @说明 ： 类型转换工具类
 * @作者 ：WangXL
 * @时间 ：2018 8 31
 **/
public class WUtil {
	
	/**
	 * @说明 ： List集合转换成String
	 * @作者 ：WangXL
	 * @时间 ：2018 8 31
	 **/
	public static String listToStr(List list){
		if(list != null && list.size()>0){
			try{
				String str = list.toString();
				JSONArray json = new JSONArray();
				for(int i=0;i<list.size();i++){
					if(checkObj(list.get(i))){
						json.put(JSONHelper.bean2json(list.get(i)));
					}else{
						json.put(list.get(i));
					}
				}
				return json.toString();
			}catch(Exception e){
				Log.e("WUtil.listToStr 转换异常", e);
			}
		}
		return "";
	}
	
	/**
	 * @说明 ： 判断是否为对象
	 * @返回 ：boolean  true 对象   false 非对象
	 * @作者 ：WangXL
	 * @时间 ：2018 9 3
	 **/
	public static boolean checkObj(Object obj){
		String classStr = obj.getClass().toString();
		if(classStr.indexOf("java.")!=-1){
			return false;
		}else{
			return true;
		}
	}
	/**
	 * @说明 ： String集合转换成List
	 * @作者 ：WangXL
	 * @时间 ：2018 8 31
	 **/
	public static List strToList(String str){
		try{
			JSONArray json = new JSONArray(str);
			if(json != null){
				List list = new ArrayList();
				for(int i=0;i<json.length();i++){
					list.add(json.get(i));
				}
				return list;
			}
		}catch(Exception e){
			Log.e("WUtil.strToList 转换异常", e);
		}
		return null;
	}
	
	/**
	 * @说明 ： map转String
	 * @作者 ：WangXL
	 * @时间 ：2018 8 31
	 **/
	public static String mapToStr(Map map){
		if(map !=null && !map.isEmpty()){
			try{
				JSONObject json = new JSONObject();
				for(Object e :map.entrySet()){
					Map.Entry mape = (Entry) e;
					if(checkObj(mape.getValue())){
						json.put((String)mape.getKey(),JSONHelper.bean2json(mape.getValue()));
					}else{
						json.put((String)mape.getKey(),mape.getValue());
					}
				}
				return json.toString();
			}catch(Exception e){
				Log.e("WUtil.mapToStr 转换异常", e);
			}
		}
		return "";
	}
	/**
	 * @说明 ： String转map
	 * @作者 ：WangXL
	 * @时间 ：2018 8 31
	 **/
	public static Map strToMap(String str){
		if(str !=null){
			try{
				Map map = new HashMap();
				JSONObject json = new JSONObject(str);
				Iterator it = json.keys();  
	            while(it.hasNext()){  
	               String key = (String) it.next();
	               map.put(key, json.get(key));
	            }  
				return map;
			}catch(Exception e){
				Log.e("WUtil.strToMap 转换异常", e);
			}
		}
		return null;
	}
}
