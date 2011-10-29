package com.voc4u.setting;

import java.util.Locale;

public class LangType
{		
	public final int id;
	public final String code;
	//public final String name;
	
	
	public LangType(int i, String cd)
	{
		id = i;
		code = cd;
		//resId = res;
		
		//int res = ctx.getResources().getIdentifier("string/lang" + cd, null, ctx.getPackageName());
		//name = ctx.getResources().getString(res);
	}
	
	@Override
	public String toString()
	{
		Locale loc = new Locale(code);
		return loc.getDisplayLanguage();
	}

}
