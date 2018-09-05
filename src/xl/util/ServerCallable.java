package xl.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.SocketException;

import org.json.JSONException;
import org.json.JSONObject;

import xl.sys.Log;



import com.sun.management.OperatingSystemMXBean;

/**
 * 说明： 用于处理客户端或服务器传送过来的数据数据
 * @author wangxl
 * 2017 7 14
 */
public class ServerCallable{

	 /** 
     * 读取客户端信息  - 中文处理
     * @param inputStream 
     */  
    public static String getClientContent(Socket socket) throws Exception {  
    	try{
    		DataInputStream dataIS = new DataInputStream(socket.getInputStream());
        	InputStreamReader inSR = new InputStreamReader(dataIS, "UTF-8");
        	BufferedReader br = new BufferedReader(inSR);
        	String str="";
        	while((str = br.readLine()) != null) {
        		str = str.trim();
    			break;
        	}
	    	return str;
    	}catch(SocketException e){
    		Log.e("WSocket 读取返回信息异常", e);
    	}
    	return null;
    }  
  
    /** 
     * 读取客户端信息  - 
     * 传入指定字符格式 UTF-8
     * @param inputStream 
     */  
    public static String getClientContent(Socket socket,String code) throws Exception {  
    	try{
    		DataInputStream dataIS = new DataInputStream(socket.getInputStream());
        	InputStreamReader inSR = new InputStreamReader(dataIS, code);
        	BufferedReader br = new BufferedReader(inSR);
        	String str="";
        	while((str = br.readLine()) != null) {
        		str = str.trim();
    			break;
        	}
            return str.toString();
    	}catch(SocketException e){
    		e.printStackTrace();
    	}
    	return null;
    }  
  
    // 向客户发送数据 UTF-8
	 public static boolean sendMessage(Socket socket, String data) {
		 try{
			 DataOutputStream dataOS = new DataOutputStream(socket.getOutputStream());
			 OutputStreamWriter outSW = new OutputStreamWriter(dataOS, "UTF-8");
			 BufferedWriter bw = new BufferedWriter(outSW);
			 bw.write(data.toString()+"\r\n");		//加上分行符，以便服务器按行读取
			 bw.flush();
			 return true;
		 }catch(Exception e){
			 e.printStackTrace();
		 }
		 return false;
	 }
	 // 向客户发送数据 指定字符格式
	 public static boolean sendMessage(Socket socket, String data,String code)  {
		 try{
			 DataOutputStream dataOS = new DataOutputStream(socket.getOutputStream());
			 OutputStreamWriter outSW = new OutputStreamWriter(dataOS, code);//, code
			 BufferedWriter bw = new BufferedWriter(outSW);
			 bw.write(data.toString()+"\r\n");		//加上分行符，以便服务器按行读取
			 bw.flush();
			 return true;
		 }catch(Exception e){
			 e.printStackTrace();
		 }
		 return false;
	 }
    /**
     * 将字符串转换成JSONObject
     * @param str
     * @return
     */
    public static JSONObject toJSONObject(String str){
    	try {
			JSONObject result = new JSONObject(str);
			return result;
		} catch (JSONException e) {
			e.printStackTrace();
		}
    	return new JSONObject();
    }
}
