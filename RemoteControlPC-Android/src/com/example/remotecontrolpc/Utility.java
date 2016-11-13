package com.example.remotecontrolpc;

import java.io.FileDescriptor;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.content.ContentUris;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.ParcelFileDescriptor;

public class Utility {
	public String getDate(String date, String dateFormat) {
		long milliSeconds = Long.parseLong(date);
		// Create a DateFormatter object for displaying date in specified format.
	    SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);
	    // Create a calendar object that will convert the date and time value in milliseconds to date. 
	     Calendar calendar = Calendar.getInstance();
	     calendar.setTimeInMillis(milliSeconds);
	     return formatter.format(calendar.getTime());
	}
	public String getDate(long milliSeconds, String dateFormat) {
		// Create a DateFormatter object for displaying date in specified format.
	    SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);
	    // Create a calendar object that will convert the date and time value in milliseconds to date. 
	     Calendar calendar = Calendar.getInstance();
	     calendar.setTimeInMillis(milliSeconds);
	     return formatter.format(calendar.getTime());
	}
	/*http://stackoverflow.com/questions/22859350/android-listview-out-of-memory-exception*/
	public Bitmap decodeImageFile(String path) {
		try {
            // Decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(path, o);
            // The new size we want to scale to
            final int REQUIRED_SIZE = 60;
            // Find the correct scale value. It should be the power of 2.
            int scale = 1;
            while (o.outWidth / scale / 2 >= REQUIRED_SIZE && o.outHeight / scale / 2 >= REQUIRED_SIZE)
                scale *= 2;

            // Decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            return BitmapFactory.decodeFile(path, o2);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return null;
	}
	public String getDuration(int duration) {
		String time = "";
		duration /= 1000;//in seconds
		int minutes = duration / 60;
		duration %= 60;
		if (minutes > 0) {
			time += minutes + " mins ";
		}
		time += duration + " secs";
		return time;
	}
	public Bitmap getAlbumart(int albumId, Context context) {
	    Bitmap bm = null;
	    try {
	        final Uri sArtworkUri = Uri.parse("content://media/external/audio/albumart");
	        Uri uri = ContentUris.withAppendedId(sArtworkUri, albumId);
	        ParcelFileDescriptor pfd = context.getContentResolver().openFileDescriptor(uri, "r");
            if (pfd != null) {
	            FileDescriptor fd = pfd.getFileDescriptor();
	            bm = BitmapFactory.decodeFileDescriptor(fd);
	        }
	    } catch (Exception e) {
	    }
	    return bm;
	}
	public String getSize(int size) {
		size /= 1024; 
		return size + "KB";
	}
	public String getSize(long size) {
		size /= 1024; 
		return size + "KB";
	}
}
