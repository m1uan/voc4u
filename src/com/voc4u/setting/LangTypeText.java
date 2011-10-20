package com.voc4u.setting;

import android.content.Context;

public class  LangTypeText extends LangType
{
	final String name;
	
	public LangTypeText(final LangType lt, Context ctx)
	{
		super(lt.id, lt.code);
	
		int res = ctx.getResources().getIdentifier("string/lang" + lt.code, null, ctx.getPackageName());
		name = ctx.getResources().getString(res);
	}
	
	@Override
	public String toString()
	{
		return name;
	}
}