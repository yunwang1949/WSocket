package com.client;

import xl.annotation.WSocket;
import xl.annotation.WSocketMethod;

@WSocket(value="ClientClass")
public class ClientClass {
	
	@WSocketMethod(value="sum")
	public String sum(String str1,String str2){
		int int1 =Integer.valueOf(str1);
		int int2 =Integer.valueOf(str2);
		return String.valueOf(int1+int2);
	}
	
	@WSocketMethod(value="sum2")
	public String sum2(){
		int int1 =11;
		int int2 =22;
		return String.valueOf("11+22="+(int1+int2));
	}
}
