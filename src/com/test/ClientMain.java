package com.test;

import xl.socket.WClientSocket;
import xl.sys.Log;

public class ClientMain {
	
	/**
	 * @说明 ： 测试WSocket客户端
	 * @备注 ：调用服务器端类注解为：ServiceClass  方法注解为：sum  传入参数为："5","20"
	 **/
	public static void main(String[] args) {
		//单次请求服务器
		WClientSocket socket = new WClientSocket("127.0.0.1", 12306,0);
		String result = socket.method("ServiceClass", "sum", "5","20");
		Log.e("方法1返回："+result);
		
		//多次请求服务器
		WClientSocket socket2 = new WClientSocket("127.0.0.1", 12306,1);
		String result2 = socket2.method("ServiceClass", "sum2");
		Log.e("方法2返回："+result2);
		
		String result3 = socket2.method("ServiceClass", "sum", "5","20");
		Log.e("方法3返回："+result3);
		socket2.close();
		
		//建立服务器调用客户端方法连接
		WClientSocket socket_Tem = new WClientSocket("127.0.0.1", 12306,"com.client");
		System.out.println("key:"+socket_Tem.getKey());
	}
}
