package me.varunon9.remotecontrolpc.imageviewer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import me.varunon9.remotecontrolpc.CallbackReceiver;
import me.varunon9.remotecontrolpc.MusicImageAvatar;
import me.varunon9.remotecontrolpc.Utility;

public abstract class ImagesList extends AsyncTask<Void, Void, ArrayList<MusicImageAvatar>> implements CallbackReceiver {
	Context context;
	public ImagesList (Context context) {
		this.context = context;
	}
	@Override
	protected ArrayList<MusicImageAvatar> doInBackground(Void... params) {
		ArrayList <MusicImageAvatar> imagesList = new ArrayList<MusicImageAvatar>();
		ContentResolver imageResolver = context.getContentResolver();
    	Uri imageUri = android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        Cursor imageCursor = imageResolver.query(imageUri, null, null, null, null);
        Utility utility = new Utility();
    	if (imageCursor != null && imageCursor.moveToFirst()) {
    		int titleColumn = imageCursor.getColumnIndex(android.provider.MediaStore.Images.Media.DISPLAY_NAME);
    		int dataColumn = imageCursor.getColumnIndex(android.provider.MediaStore.Images.Media.DATA);
    		int sizeColumn = imageCursor.getColumnIndex(android.provider.MediaStore.Images.Media.SIZE);
    		int dateColumn = imageCursor.getColumnIndex(android.provider.MediaStore.Images.Media.DATE_TAKEN);
    		do {
    			String thisTitle = imageCursor.getString(titleColumn);
    			String thisData = imageCursor.getString(dataColumn);
    			String thisDate = imageCursor.getString(dateColumn);
    			int thisSize = imageCursor.getInt(sizeColumn);//in bytes
    			int icon = me.varunon9.remotecontrolpc.R.drawable.image;
    			String subHeading = utility.getSize(thisSize) + ", " + utility.getDate(thisDate, "dd MMM yyyy hh:mm a");
    			//duration set to 0 because it is for image
    			imagesList.add(new MusicImageAvatar(icon, 0, thisTitle, subHeading, thisData, "image"));
    		} while (imageCursor.moveToNext());
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
