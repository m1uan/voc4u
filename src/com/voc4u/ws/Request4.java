package com.voc4u.ws;

import com.wildfuse.wilda.network.Request;

public class Request4 extends Request{
	public static final String HOST = "http://192.168.1.3:9999";
	
	public Request4(final String func)
	{
		super(HOST+"/wordctrl");
		addUrlParam("m", func);
	}
}
