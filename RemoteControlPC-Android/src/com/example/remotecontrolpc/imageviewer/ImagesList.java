package com.example.remotecontrolpc.imageviewer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import com.example.remotecontrolpc.CallbackReceiver;
import com.example.remotecontrolpc.MusicImageAvatar;
import com.example.remotecontrolpc.R;
import com.example.remotecontrolpc.Utility;

public abstract class ImagesList extends AsyncTask<Void, Void, ArrayList<MusicImageAvatar>> implements CallbackReceiver {
	Context context;
	public ImagesList (Context context) {
		this.context = context;
	}
	@Override
	protected ArrayList<MusicImageAvatar> doInBackground(Void... params) {
		ArrayList <MusicImageAvatar> imagesList = new ArrayList<MusicImageAvatar>();
		ContentResolver musicResolver = context.getContentResolver();
    	Uri imageUri = android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        Cursor musicCursor = musicResolver.query(imageUri, null, null, null, null);
        Utility utility = new Utility();
    	if (musicCursor != null && musicCursor.moveToFirst()) {
    		int titleColumn = musicCursor.getColumnIndex(android.provider.MediaStore.Images.Media.DISPLAY_NAME);
    		int dataColumn = musicCursor.getColumnIndex(android.provider.MediaStore.Images.Media.DATA);
    		int sizeColumn = musicCursor.getColumnIndex(android.provider.MediaStore.Images.Media.SIZE);
    		int dateColumn = musicCursor.getColumnIndex(android.provider.MediaStore.Images.Media.DATE_TAKEN);
    		do {
    			String thisTitle = musicCursor.getString(titleColumn);
    			String thisData = musicCursor.getString(dataColumn);
    			String thisDate = musicCursor.getString(dateColumn);
    			int thisSize = musicCursor.getInt(sizeColumn);//in bytes
    			int icon = R.drawable.image;
    			String subHeading = utility.getSize(thisSize) + ", " + utility.getDate(thisDate, "dd MMM yyyy hh:mm a");
    			imagesList.add(new MusicImageAvatar(icon, thisTitle, subHeading, thisData, "image"));
    		} while (musicCursor.moveToNext());
    	}
    	Collections.sort(imagesList, new Comparator<MusicImageAvatar>() {
			public int compare(MusicImageAvatar a,MusicImageAvatar b) {
				return a.getHeading().compareTo(b.getHeading());
			}
		});
		return imagesList;
	}
	@Override
	protected void onPostExecute(ArrayList<MusicImageAvatar> imagesList) {
		receiveData(imagesList);
	}
	@Override
	public abstract void receiveData(Object result);
}
