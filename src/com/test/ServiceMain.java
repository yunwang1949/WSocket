package com.test;

import java.net.Socket;
import java.util.Map;
import xl.socket.WServerSocket;

public class ServiceMain {
	/**
	 * @说明 ： 测试WSocket服务器端
	 * @备注 : 服务器端口号为：12306    连接数：20    扫描注解包路径：com.test
	 **/
	public static void main(String[] args) throws InterruptedException {
		WServerSocket wsocket = new WServerSocket(12306, 2,"com.service");
		wsocket.start();
		Thread.sleep(5000);
		Map<String, Socket> map= wsocket.getClientSocket();
		if(map != null && map.size()>0){
			for(String key : map.keySet()){
				String rul = wsocket.method(map.get(key), "ClientClass", "sum", "22","33");
				System.out.println(key + "   "+rul);
				String rul2 = wsocket.method(map.get(key), "ClientClass", "sum2");
				System.out.println(key + "   "+rul2);
				//wsocket.close(map.get(key));
			}
		}
	}
}
