package yuku.iconcontextmenu;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.Resources.Theme;
import android.util.TypedValue;
import android.view.*;
import android.widget.*;

public class IconContextMenuAdapter extends BaseAdapter {
	private static final int LIST_PREFERED_HEIGHT = 20;

	private Context context;
	Activity parentActivity;
	private ArrayList<IconContextMenuItem> mItems = new ArrayList<IconContextMenuItem>();

	public IconContextMenuAdapter(Activity act)
	{
		parentActivity = act;
		context = act.getApplicationContext();
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mItems.size();
	}

	@Override
	public IconContextMenuItem getItem(int position) {
		return mItems.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
	    IconContextMenuItem item = (IconContextMenuItem) getItem(position);
	    Resources res = parentActivity.getResources();
	   
	    if (convertView == null) {
	        TextView temp = new TextView(context);
	        AbsListView.LayoutParams param = new AbsListView.LayoutParams(AbsListView.LayoutParams.FILL_PARENT, AbsListView.LayoutParams.WRAP_CONTENT);
	        temp.setLayoutParams(param);
	        //temp.setPadding((int)toPixel(res, 15), 0, (int)toPixel(res, 15), 0);
	        temp.setGravity(android.view.Gravity.CENTER_VERTICAL);
	           
	        Theme th = context.getTheme();
	        TypedValue tv = new TypedValue();
	     
	        if (th.resolveAttribute(android.R.attr.textAppearanceLargeInverse, tv, true)) {
	            temp.setTextAppearance(context, tv.resourceId);
	        }
	        temp.setMinHeight(LIST_PREFERED_HEIGHT);
	        //temp.setCompoundDrawablePadding((int)toPixel(res, 14));
	        convertView = temp;
	    }
	    
	    TextView textView = (TextView) convertView;
	    textView.setTag(item);
	    textView.setText(item.text);
	    textView.setCompoundDrawablesWithIntrinsicBounds(item.image, null, null, null);
	          
	    return textView;
	}

	public void addItem(IconContextMenuItem iconContextMenuItem) {
		mItems.add(iconContextMenuItem);
		
	}
	
    
}