package me.varunon9.remotecontrolpc;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class MusicImageAvatarAdapter extends ArrayAdapter<MusicImageAvatar> {
	
	Context context;
	int layoutResourceID;
	ArrayList<MusicImageAvatar> objects;
	
	public MusicImageAvatarAdapter(Context context, int layoutResourceID,
                                   ArrayList<MusicImageAvatar> objects) {
		super(context, layoutResourceID, objects);
		this.context = context;
		this.layoutResourceID = layoutResourceID;
		this. objects = objects;
	}
	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;
		MusicImageAvatarHolder holder = null;
		if (row == null) {
			LayoutInflater inflater = NavigationDrawerItemAdapter.scanForActivity(context).getLayoutInflater();
			row = inflater.inflate(layoutResourceID, parent, false);
			holder = new MusicImageAvatarHolder();
			holder.icon = (ImageView) row.findViewById(R.id.avatarImageView);
			holder.avatarHeading = (TextView) row.findViewById(R.id.avatarHeadingTextView);
			holder.avatarSubheading = (TextView) row.findViewById(R.id.avatarSubheadingTextView);
			row.setTag(holder);
		} else {
			holder = (MusicImageAvatarHolder) row.getTag();
		}
		MusicImageAvatar item = objects.get(position);
		Bitmap bitmap;
		Utility utility = new Utility();
		if (item.getType().equals("image")) {
			bitmap = utility.decodeImageFile(item.getData());
			holder.icon.setImageBitmap(bitmap);
		} else {
			bitmap = utility.getAlbumart(item.getIcon(), context);
			if (bitmap != null) {
				holder.icon.setImageBitmap(bitmap);
			} else {
				holder.icon.setImageResource(R.mipmap.music_png);
			}
		}
		holder.avatarHeading.setText(item.getHeading());
		holder.avatarSubheading.setText(item.getSubheading());
		return row;
	}
    static class MusicImageAvatarHolder {
    	ImageView icon;
    	TextView avatarHeading, avatarSubheading;
    }
}
