package xl.socket;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.json.JSONArray;
import org.json.JSONObject;

import xl.sys.Log;
import xl.util.ClassUtil;
import xl.util.ClientHandleThread;
import xl.util.ServerCallable;
import xl.util.ServerHandleThread;



/**
 * @说明 ： WSocke - 客户端
 * @作者 ：WangXL
 * @时间 ：2018 8 21
 **/
public class WClientSocket  extends ServerCallable{
	/**
	 * @说明 ： socket端口
	 **/
	private  String ip;
	
	/**
	 * @说明 ： socket端口
	 **/
	private  int port;
	
	/**
	 * @说明 ： socket连接对象
	 **/
	private Socket socket;
	
	/**
	 * @说明 ： 连接模式  0单次请求  1长连接  2用于服务器调用客户端注解方法
	 **/
	private  int type;
	
	/**
	 * @说明 ： 注解类包路径
	 **/
	private  String path;
	
	/**
	 * @说明 ：客户端添加客户端类注解路径
	 **/
	private  List<String> clientAnnotationPath;
	
	/**
	 * @说明 ： 客户端添加客户端类注解类 - 根据上面的路径解析   
	 * Key:类注解名称   value:路径
	 **/
	@SuppressWarnings("rawtypes")
	private  Map<String,Class> clientClass;
	
	/**
	 * @说明 ：type=2 可供服务器调用的socket的EKY
	 * @作者 ：WangXL
	 * @时间 ：2018 8 22
	 **/
	private String key;
	
	/**
	 * @说明 ： 与服务器对接后仅用于服务器调用客户端注解类方法
	 * @参数 ：@param ip	连接服务器socket地址
	 * @参数 ：@param port 服务端口号
	 * @参数 ：@param path 供给服务器调用的注解类包路径
	 **/
	public WClientSocket(String ip,int port,String path){
		this.ip = ip;
		this.port = port;
		this.path = path;
		try{
			setClientAnnotationPath(path);
			socket = new Socket(ip, port);
			JSONObject json = new JSONObject();
    		json.put("type", 2);//连接类型
    		key = UUID.randomUUID().toString().replaceAll("-", "");
    		json.put("key", key);//key专属socket密匙
    		if(!sendMessage(socket, json.toString())){
    			Log.e("WClientSocket 连接异常  IP:"+ip+" 端口："+port);
    			socket.close();
    			return;
    		}	
    		String clientContent = getClientContent(socket);	
    		json = toJSONObject(clientContent);
    		//-1代表结束长连接
    		if(!json.isNull("type") && (int)json.getInt("type")==2){
    			ClientHandleThread client = new ClientHandleThread(socket,clientClass);
    			new Thread(client).start();
    		}else{
    			socket.close();
    		}
		}catch(Exception e){
			Log.e("WClientSocket 连接异常  IP:"+ip+" 端口："+port, e);
			if(socket != null){
				try {
					socket.close();
				} catch (IOException e2) {
					Log.e("WClientSocket 结束连接异常", e2);
				}
			}
		}
	}
	/**
	 * @说明 ： type=0 用于单次服务器请求    type=1 用于连续多次服务器请求
	 * @参数 ：@param ip		ip连接socket地址
	 * @参数 ：@param port 端口号
	 * @参数 ：@param type 连接模式  0单次请求  1长连接
	 **/
	public WClientSocket(String ip,int port,int type){
		this.ip = ip;
		this.port = port;
		this.type = type;
		try{
			socket = new Socket(ip, port);
			//长连接时需先进行请求
			if(this.type == 1){
				JSONObject json = new JSONObject();
	    		json.put("type", this.type);//连接类型
	    		if(!sendMessage(socket, json.toString())){
	    			Log.e("WClientSocket 连接异常  IP:"+ip+" 端口："+port);
	    			return;
	    		}	
	    		getClientContent(socket);	
			}
		}catch(Exception e){
			Log.e("WClientSocket 连接异常  IP:"+ip+" 端口："+port, e);
			if(socket != null){
				try {
					socket.close();
				} catch (IOException e2) {
					Log.e("WClientSocket 结束连接异常", e2);
				}
			}
		}
	}
	
	/**
     * @说明 ： 调用WSocket指定注解的类方法 - 【客户端使用】
     * @参数 ：@param className  类名 (如：Date)
     * @参数 ：@param methodName 方法名 (无需参数和括号 如：setTime)
     * @参数 ：@param param      方法的参数 (不限制个数，方法需要几个参数则传入几个)
     * @返回 ：JSONObject
     * @作者 ：WangXL
     * @时间 ：2018 8 21
     **/
    public  String method(String className,String methodName,String ... param){
    	String result = null;
    	try{
    		if(className ==null || methodName == null){
    			Log.e("WClientSocket.method 类名或方法名为空");
    			return result;
    		}
    		JSONObject json = new JSONObject();
    		json.put("className", className);
    		json.put("methodName", methodName);
    		if(param != null){
    			JSONArray ja = new JSONArray();
        		for(int i=0;i<param.length;i++){
        			ja.put(param[i]);
        		}
        		json.put("param", ja);
    		}
    		if(!sendMessage(socket, json.toString())){
    			Log.e("WClientSocket.method 请求发送失败");
    			return result;
    		}
    		result = getClientContent(socket);	
    		if(this.type==0){
    			socket.close();
    		}
    		return result;
    	}catch(Exception e){
    		Log.e("WClientSocket.method 调用方法异常", e);
    	}
    	return result;
    }

    /**
	 * @说明 ： 添加客户端类注解路径
	 * @参数 ：@param path  类注解路径可以用,号分隔  如：com.util,con.po,con.service
	 * @返回 ：void
	 * @作者 ：WangXL
	 * @时间 ：2018 8 21
	 **/
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public  void setClientAnnotationPath(String path){
		if(path != null){
			clientAnnotationPath = clientAnnotationPath == null ? new ArrayList<String>() : clientAnnotationPath; 
			clientClass = clientClass == null ? new HashMap<String,Class>() : clientClass;
			String [] arr={path};
			if(path.indexOf(",")!=-1){
				arr = path.split(",");
			}
			List<String> list = Arrays.asList(arr);
			if(list != null && list.size()>0){
				clientAnnotationPath.addAll(list);
			}
			//开始反射类
			for(String str : clientAnnotationPath){
				try {
					List<Class<?>> classes = ClassUtil.getClasses(str);
					for (Class clas :classes) {
						//不包含WSocket注解则跳过
						if(!clas.isAnnotationPresent(xl.annotation.WSocket.class)){
							continue;
						}
						//获得注解名
						xl.annotation.WSocket wsocketAnnot= (xl.annotation.WSocket) clas.getAnnotation(xl.annotation.WSocket.class);
						clientClass.put(wsocketAnnot.value(), clas);
					}
				} catch (Exception e) {
					Log.e("WClientSocket 扫描注解地址异常", e);
				} 
			}
		}
	}
    
    public void close(){
    	try{
    		JSONObject json = new JSONObject();
    		json.put("type", -1);//结束连接
    		if(!sendMessage(socket, json.toString())){
    			Log.e("WClientSocket.close 结束Socket异常");
    		}
        	socket.close();
    	}catch(Exception e){
    		Log.e("WClientSocket.close 结束Socket异常", e);
    	}
    }
    
	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public Socket getSocket() {
		return socket;
	}

	public void setSocket(Socket socket) {
		this.socket = socket;
	}
	public String getKey() {
		return key;
	}
}
