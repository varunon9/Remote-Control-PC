package com.example.remotecontrolpc;

import java.util.ArrayList;

import com.example.remotecontrolpc.MusicImageAvatarAdapter.MusicImageAvatarHolder;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class AvatarFileAdapter extends ArrayAdapter<AvatarFile> {
	Context context;
	int layoutResourceID;
	ArrayList <AvatarFile> objects;
	public AvatarFileAdapter(Context context, int layoutResourceID,
			ArrayList <AvatarFile> objects) {
		super(context, layoutResourceID, objects);
		this.context = context;
		this.layoutResourceID = layoutResourceID;
		this. objects = objects;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;
		AvatarFileHolder holder = null;
		if (row == null) {
			LayoutInflater inflater = NavigationDrawerItemAdapter.scanForActivity(context).getLayoutInflater();
			row = inflater.inflate(layoutResourceID, parent, false);
			holder = new AvatarFileHolder();
			holder.icon = (ImageView) row.findViewById(R.id.avatarImageView);
			holder.avatarHeading = (TextView) row.findViewById(R.id.avatarHeadingTextView);
			holder.avatarSubheading = (TextView) row.findViewById(R.id.avatarSubheadingTextView);
			row.setTag(holder);
		} else {
			holder = (AvatarFileHolder) row.getTag();
		}
		AvatarFile item = objects.get(position);
		Bitmap bitmap;
		Utility utility = new Utility();
		//change to lowercase and match
		if (item.getHeading().endsWith("jpg") || item.getHeading().endsWith("JPG") || item.getHeading().endsWith("Jpg")
				|| item.getHeading().endsWith("Jpeg") || item.getHeading().endsWith("JPEG") || item.getHeading().endsWith("jpeg")) {
			bitmap = utility.decodeImageFile(item.getPath());
			holder.icon.setImageBitmap(bitmap);
		} else {
			holder.icon.setImageResource(item.getIcon());
		}
		holder.avatarHeading.setText(item.getHeading());
		holder.avatarSubheading.setText(item.getSubheading());
		return row;
	}
    static class AvatarFileHolder {
    	ImageView icon;
    	TextView avatarHeading, avatarSubheading;
    }
}
