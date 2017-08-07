package me.varunon9.remotecontrolpc;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class NavigationDrawerItemAdapter extends ArrayAdapter<NavigationDrawerItem> {
	Context context;
	int layoutResourceID;
	NavigationDrawerItem objects[];
	public NavigationDrawerItemAdapter(Context context, int layoutResourceID,
			NavigationDrawerItem[] objects) {
		super(context, layoutResourceID, objects);
		this.context = context;
		this.layoutResourceID = layoutResourceID;
		this. objects = objects;
	}
	
	public View getView (int position, View convertView, ViewGroup parent) {
		View row = convertView;
		NavigationDrawerItemHolder holder = null;
		if (row == null) {
			LayoutInflater inflater = (scanForActivity(context)).getLayoutInflater();
			row = inflater.inflate(layoutResourceID, parent, false);
			holder = new NavigationDrawerItemHolder();
			holder.imgIcon = (ImageView) row.findViewById(R.id.imgIcon);
			holder.textTitle = (TextView) row.findViewById(R.id.textTitle);
			row.setTag(holder);
		} else {
			holder = (NavigationDrawerItemHolder) row.getTag();
		}
		NavigationDrawerItem item = objects[position];
		holder.textTitle.setText(item.title);
		holder.imgIcon.setImageResource(item.icon);
		return row;
	}
	
	static class NavigationDrawerItemHolder {
		ImageView imgIcon;
		TextView textTitle;
	}
	/*http://stackoverflow.com/questions/21657045/contextthemewrapper-cannot-be-cast-to-activity*/
	public static Activity scanForActivity(Context cont) {
	    if (cont == null)
	        return null;
	    else if (cont instanceof Activity)
	        return (Activity)cont;
	    else if (cont instanceof ContextWrapper)
	        return scanForActivity(((ContextWrapper)cont).getBaseContext());

	    return null;
	}
}
