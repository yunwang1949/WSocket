package xl.util;

import java.net.Socket;
import java.util.Map;

import org.json.JSONArray;

import org.json.JSONObject;

import xl.socket.WServerSocket;
import xl.sys.Log;
/**
 * @说明 ： 针对每个socket请求进行建立线程处理
 * @作者 ：WangXL
 * @时间 ：2018 8 21
 **/
public class ServerHandleThread extends ServerCallable implements Runnable{

    private Socket socket;// 和本线程相关的Socket
    private Map<String,Class> serviceClass;
    private WServerSocket wServerSocket;
    public ServerHandleThread(Socket socket,Map<String,Class> serviceClass,WServerSocket wServerSocket) {
        super();
        this.socket = socket;
        this.serviceClass = serviceClass;
        this.wServerSocket = wServerSocket;
    }
    @Override
    public void run() {
    	try {
    		String clientContent = getClientContent(socket);
    		JSONObject json = toJSONObject(clientContent);
    		//构建长连接
    		if(!json.isNull("type") && (int)json.getInt("type")==1){
    			sendMessage(socket, json.toString());
    			while(true){
					clientContent = getClientContent(socket);
					if(clientContent == null){
						Log.e("WServerSocket 与服务器连接已断开");
						break;
					}
    	    		json = toJSONObject(clientContent);
    	    		//-2代表心跳数据
    	    		if(!json.isNull("type") && (int)json.getInt("type")==-2){
    	    			continue;
    	    		}
    	    		//-1代表结束长连接
    	    		if(!json.isNull("type") && (int)json.getInt("type")==-1){
    	    			socket.close();
    	    			break;
    	    		}
    	    		String resutl = WSocketUtil.callMethodByJson(json,serviceClass);
    	    		if(!sendMessage(socket, resutl)){
    	    			Log.e("WServerSocket 数据返回异常");
    	    		}
    			}
    		}else if(!json.isNull("type") && (int)json.getInt("type")==-1){//结束连接
    			socket.close();
    		}else if(!json.isNull("type") && (int)json.getInt("type")==2){//服务器调用客户端连接
    			if(!sendMessage(socket, json.toString())){
        			Log.e("WServerSocket 数据返回异常");
        		}else{
        			wServerSocket.getClientSocket().put(json.getString("key"),socket);
        		}
    		}else{
    			String resutl = WSocketUtil.callMethodByJson(json,serviceClass);
        		if(!sendMessage(socket, resutl)){
        			Log.e("WServerSocket 数据返回异常");
        		}
        		socket.close();
    		}
        } catch (Exception e) {
            Log.e("WServerSocket 解析客户端请求出现异常", e);
        } 
    }
    
}