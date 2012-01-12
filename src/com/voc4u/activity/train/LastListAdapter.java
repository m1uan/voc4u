package com.voc4u.activity.train;

import com.voc4u.controller.WordController;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import com.voc4u.controller.*;

public class LastListAdapter extends BaseAdapter
{

	private final WordController mWCtrl;

	public LastListAdapter(Context context)
	{
		mWCtrl = WordController.getInstance(context);
	}
	// TODO Auto-generated constructor stub
	@Override
	public int getCount()
	{
		if(mWCtrl.getLastList().size() < 1)
			return 0;
		else
			return mWCtrl.getLastList().size() -1;
	}

	@Override
	public PublicWord getItem(int position)
	{
		position = getCount() - position -1;
		return mWCtrl.getLastList().get(position);
	}

	@Override
	public long getItemId(int position)
	{
		return getItem(position).getId();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		return new LastItem(parent.getContext(), getItem(position));
	}

}
