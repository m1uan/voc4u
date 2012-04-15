package yuku.iconcontextmenu;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.res.Resources;

public class IconContextMenu implements DialogInterface.OnCancelListener, DialogInterface.OnDismissListener{
	
	Activity parentActivity;
	IconContextMenuAdapter menuAdapter;
	IconContextMenuOnClickListener clickHandler;
	
	public IconContextMenu(Activity act)
	{
		parentActivity = act;
		menuAdapter = new IconContextMenuAdapter(act);
	}
	
    /**
     * Create menu
     */
    public Dialog createMenu(String menuItitle) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(parentActivity);
        builder.setTitle(menuItitle);
        builder.setAdapter(menuAdapter, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialoginterface, int i) {
                    IconContextMenuItem item = (IconContextMenuItem) menuAdapter.getItem(i);
                    if (clickHandler != null) {
                        clickHandler.onClick(item.actionTag);
                    }
                }
        });
        builder.setInverseBackgroundForced(true);
        AlertDialog dialog = builder.create();
        dialog.setOnCancelListener(this);
        dialog.setOnDismissListener(this);
        return dialog;
    }
 
    /**
     * Add menu item
     * @param menuItem
     */
    public void addItem(Resources res, CharSequence title,
                    int imageResourceId, int actionTag) {
        menuAdapter.addItem(new IconContextMenuItem(res, title, imageResourceId, actionTag));
    }
 
    /**
     * Set menu onclick listener
     * @param listener
     */
    public void setOnClickListener(IconContextMenuOnClickListener listener) {
        clickHandler = listener;
    }

	@Override
	public void onDismiss(DialogInterface dialog) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onCancel(DialogInterface dialog) {
		// TODO Auto-generated method stub
		
	}
}