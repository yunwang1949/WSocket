package xl.socket;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.json.JSONArray;
import org.json.JSONObject;

import xl.sys.Log;
import xl.util.ClassUtil;
import xl.util.ServerCallable;
import xl.util.ServerHandleThread;



/**
 * @说明 ： WSocke - 服务器端
 * @作者 ：WangXL
 * @时间 ：2018 8 21
 **/
public class WServerSocket extends Thread{
	
	/**
	 * @说明 ： socket端口
	 **/
	private  int port;
	
	/**
	 * @说明 ： socket连接数
	 **/
	private  int connectNum;
	/**
	 * @说明 ： 服务器socket
	 **/
	private  ServerSocket serverSocket = null;
	/**
	 * @说明 ：服务器添加服务器类注解路径
	 **/
	private  List<String> serviceAnnotationPath;
	
	/**
	 * @说明 ： 服务器添加服务器类注解类 - 根据上面的路径解析   
	 * Key:类注解名称   value:路径
	 **/
	@SuppressWarnings("rawtypes")
	private  Map<String,Class> serviceClass;
	
	/**
	 * @说明 ： 客户端连接
	 **/
	private  Map<String,Socket> clientSocket;
	
	/**
	 * @参数 ：@param port 端口号,连接默认为50个
	 **/
	public WServerSocket(int port){
		this.port = port;
		this.connectNum = 50;
		clientSocket = new HashMap<String,Socket>();
	}
	/**
	 * @参数 ：@param port 端口号
	 * @参数 ：@param connectNum	连接数
	 **/
	public WServerSocket(int port,int connectNum){
		this.port = port;
		this.connectNum = connectNum;
		clientSocket = new HashMap<String,Socket>();
	}
	/**
	 * @参数 ：@param port	端口号
	 * @参数 ：@param connectNum	连接个数
	 * @参数 ：@param path   注解路径
	 **/
	public WServerSocket(int port,int connectNum,String path){
		this.port = port;
		this.connectNum = connectNum;
		clientSocket = new HashMap<String,Socket>();
		setServiceAnnotationPath(path);
	}
	@Override
	public void run() {
		try{
			serverSocket = new ServerSocket(port); // 监听端口号
			ExecutorService fixedThread = Executors.newFixedThreadPool(connectNum);  //创建个线程池  可连接数
			Log.e("WServerSocket.run 启动成功 端口："+port);
			while(true){
				Socket socket = serverSocket.accept();// 等待客户连接 accept()
				//socket.setSoTimeout(70000);//设置超时时间       70秒未接收客户端数据则断开连接
				fixedThread.execute(new ServerHandleThread(socket,serviceClass,this));
			}
		}catch(Exception e){
			Log.e("WServerSocket.run 启动异常  端口："+port, e);
		}finally{
			if(serverSocket != null){
				try {
					serverSocket.close();
				} catch (IOException e) {
					Log.e("WServerSocket.run 结束服务异常", e);
				}
			}
		}
	}
	/**
     * @说明 ： 调用WSocket指定注解的类方法 - 【服务器端使用】
     * @参数 ：@param className  类名 (如：Date)
     * @参数 ：@param methodName 方法名 (无需参数和括号 如：setTime)
     * @参数 ：@param param      方法的参数 (不限制个数，方法需要几个参数则传入几个)
     * @返回 ：JSONObject
     * @作者 ：WangXL
     * @时间 ：2018 8 21
     **/
    public  String method(Socket socketMethod,String className,String methodName,String ... param){
    	String result = null;
    	try{
    		if(className ==null || methodName == null){
    			Log.e("WServerSocket.method 类名或方法名为空");
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
    		if(!ServerCallable.sendMessage(socketMethod, json.toString())){
    			Log.e("WServerSocket.method 请求发送失败");
    			return result;
    		}
    		result = ServerCallable.getClientContent(socketMethod);	
    		return result;
    	}catch(Exception e){
    		Log.e("WServerSocket.method 调用方法异常", e);
    	}
    	return result;
    }

	/**
	 * @说明 ： 添加服务器类注解路径
	 * @参数 ：@param path  类注解路径可以用,号分隔  如：com.util,con.po,con.service
	 * @返回 ：void
	 * @作者 ：WangXL
	 * @时间 ：2018 8 21
	 **/
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public  void setServiceAnnotationPath(String path){
		if(path != null){
			serviceAnnotationPath = serviceAnnotationPath == null ? new ArrayList<String>() : serviceAnnotationPath; 
			serviceClass = serviceClass == null ? new HashMap<String,Class>() : serviceClass;
			String [] arr={path};
			if(path.indexOf(",")!=-1){
				arr = path.split(",");
			}
			List<String> list = Arrays.asList(arr);
			if(list != null && list.size()>0){
				serviceAnnotationPath.addAll(list);
			}
			//开始反射类
			for(String str : serviceAnnotationPath){
				try {
					List<Class<?>> classes = ClassUtil.getClasses(str);
					for (Class clas :classes) {
						//不包含WSocket注解则跳过
						if(!clas.isAnnotationPresent(xl.annotation.WSocket.class)){
							continue;
						}
						//获得注解名
						xl.annotation.WSocket wsocketAnnot= (xl.annotation.WSocket) clas.getAnnotation(xl.annotation.WSocket.class);
						serviceClass.put(wsocketAnnot.value(), clas);
					}
				} catch (Exception e) {
					Log.e("WServerSocket 扫描注解地址异常", e);
				} 
			}
		}
	}
	
	/**
	 * @说明 ： 结束指定的注解扫描客户端长连接
	 * @参数 ：@param socketTem
	 **/
	public void close(Socket socketTem){
		try{
    		JSONObject json = new JSONObject();
    		json.put("type", -1);//结束连接
    		if(!ServerCallable.sendMessage(socketTem, json.toString())){
    			Log.e("WServerSocket.close 结束Socket异常");
    		}
    		socketTem.close();
    	}catch(Exception e){
    		Log.e("WServerSocket.close 结束Socket异常", e);
    	}
	}
	
	public  Map<String, Class> getServiceClass() {
		return serviceClass;
	}

	public  void setServiceClass(Map<String, Class> serviceClass) {
		this.serviceClass = serviceClass;
	}
	public int getPort() {
		return port;
	}
	public void setPort(int port) {
		this.port = port;
	}
	public int getConnectNum() {
		return connectNum;
	}
	public void setConnectNum(int connectNum) {
		this.connectNum = connectNum;
	}
	public ServerSocket getServerSocket() {
		return serverSocket;
	}
	public void setServerSocket(ServerSocket serverSocket) {
		this.serverSocket = serverSocket;
	}
	public List<String> getServiceAnnotationPath() {
		return serviceAnnotationPath;
	}
	public void setServiceAnnotationPath(List<String> serviceAnnotationPath) {
		this.serviceAnnotationPath = serviceAnnotationPath;
	}
	public Map<String, Socket> getClientSocket() {
		return clientSocket;
	}
	public void setClientSocket(Map<String, Socket> clientSocket) {
		this.clientSocket = clientSocket;
	}
}
