package xl.util;

import java.lang.reflect.Method;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import xl.annotation.WSocketMethod;
import xl.sys.Log;

/**
 * @说明 ：提供给WSocket的工具类 
 * @作者 ：WangXL
 * @时间 ：2018 8 21
 **/
public class WSocketUtil {

	 
    /**
     * @说明 ： 根据json调用指定的注解类方法 - 【服务端使用】
     * @参数 ：@param json  需要调用的类注解json
     * @参数 ：@param map   已存在注解的类信息
     * @参数 ：@return
     * @返回 ：String
     * @作者 ：WangXL
     * @时间 ：2018 8 21
     **/
    @SuppressWarnings("unchecked")
	public static String  callMethodByJson(JSONObject json,Map<String,Class> map){
    	try{
    		if(json ==null){
    			return null;
    		}
    		String className = json.getString("className");
    		String methodName = json.getString("methodName");
    		JSONArray param = json.getJSONArray("param");
    		if(param != null){
    			Class classObj = map.get(className);
    			if(classObj!=null){
    				//此处未获得类的方法 - 待改
    				Method fields[]=classObj.getMethods();
    				for (Method field : fields) {
    					//如果该字段不包含@WSocketMethod注解  遍历下一个
    					if(!field.isAnnotationPresent(WSocketMethod.class)){
    						continue;
    					}
    					WSocketMethod column=field.getAnnotation(WSocketMethod.class);
    					String val = column.value();
    					//找到对应的注解方法
    					if(val != null && val.equals(methodName)){
    						String fieldName=field.getName();
    						Class[] classArr = new Class[param.length()];
    						Object[] ObjectArr = new Object[param.length()];
    						for(int i=0;i<param.length();i++){
    							ObjectArr[i] = param.get(i);
    							classArr[i] = ObjectArr[i].getClass();
    						}
    						Method method = classObj.getMethod(fieldName,classArr);
							return (String) method.invoke(classObj.newInstance(),ObjectArr);
    					}
    				}
    			}
    		}
    	}catch(Exception e){
    		Log.e("WServerSocket 调用注解类方法出现异常", e);
    	}
    	return null;
    }
}
