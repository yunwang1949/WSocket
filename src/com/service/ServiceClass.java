package com.service;

import xl.annotation.WSocket;
import xl.annotation.WSocketMethod;

@WSocket(value="ServiceClass")
public class ServiceClass {
	
	@WSocketMethod(value="sum")
	public String sum(String str1,String str2){
		int i1 =Integer.valueOf(str1);
		int i2 =Integer.valueOf(str2);
		return "ServiceClass:"+String.valueOf(i1+i2);
	}
	@WSocketMethod(value="sum2")
	public String sum2(){
		int i1 =11;
		int i2 =22;
		return String.valueOf("ServiceClass 11+22="+i1+i2);
	}
}
