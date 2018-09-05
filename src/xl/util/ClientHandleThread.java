package xl.util;

import java.net.Socket;
import java.util.Map;

import org.json.JSONArray;

import org.json.JSONObject;

import xl.socket.WServerSocket;
import xl.sys.Log;
/**
 * @说明 ：客户端 - 针对请求进行处理 
 * @作者 ：WangXL
 * @时间 ：2018 8 21
 **/
public class ClientHandleThread extends ServerCallable implements Runnable{

    private Socket socket;// 和本线程相关的Socket
    private Map<String,Class> clientClass;
    public ClientHandleThread(Socket socket,Map<String,Class> clientClass) {
        super();
        this.socket = socket;
        this.clientClass = clientClass;
    }
    @Override
    public void run() {
		while(true){
			try {
				String clientContent = getClientContent(socket);
				if(clientContent == null){
					Log.e("WClientSocket 与服务器连接已断开");
					break;
				}
				JSONObject json = toJSONObject(clientContent);
				//-2代表心跳数据
	    		if(!json.isNull("type") && (int)json.getInt("type")==-2){
	    			continue;
	    		}
	    		//-1代表结束长连接
	    		if(!json.isNull("type") && (int)json.getInt("type")==-1){
	    			socket.close();
	    			break;
	    		}
	    		String resutl = WSocketUtil.callMethodByJson(json,clientClass);
	    		if(!sendMessage(socket, resutl)){
	    			Log.e("WClientSocket 数据返回异常");
	    		}
			} catch (Exception e) {
				Log.e("WClientSocket 解析服务器请求出现异常", e);
			} 
		}
    }
    
}