package com.example.remotecontrolpc.filetransfer;

import java.io.File;
import java.util.ArrayList;

import com.example.remotecontrolpc.AvatarFile;
import com.example.remotecontrolpc.CallbackReceiver;
import com.example.remotecontrolpc.R;
import com.example.remotecontrolpc.Utility;

import android.os.AsyncTask;
/*
 * The three types used by an asynchronous task are the following:
 * Params, the type of the parameters sent to the task upon execution.
 * Progress, the type of the progress units published during the background computation.
 * Result, the type of the result of the background computation.
 * */
public abstract class FilesList extends AsyncTask<String, Void, ArrayList<AvatarFile>> implements CallbackReceiver {

	@Override
	protected ArrayList<AvatarFile> doInBackground(String... params) {
		String path = params[0];
		ArrayList<AvatarFile> myFiles = new ArrayList<AvatarFile>();
		Utility utility = new Utility();
		File file = new File(path);
		//file.mkdirs();
		File[] files = file.listFiles();
	    if (files.length == 0)
	        return null;
	    else {
	        for (int i = 0; i < files.length; i++) {
	        	String avatarHeading = files[i].getName();
	        	long lastModified = files[i].lastModified();
	        	String lastModifiedDate = utility.getDate(lastModified, "dd MMM yyyy hh:mm a");
	        	int icon;
	        	String itemsOrSize, filePath;
	        	if (files[i].isDirectory()) {
	        		icon = R.drawable.folder;
	        		File tempArray[] = files[i].listFiles();
	        		if (tempArray != null) {
	        			itemsOrSize = files[i].listFiles().length + " items";
	        		} else {
	        			itemsOrSize = 0 + " item";
	        		}
	        	} else {
	        		itemsOrSize = utility.getSize(files[i].length());
	        		if (avatarHeading.endsWith("mp3")) {
	        			icon = R.drawable.music_png;
	        		} else if (avatarHeading.endsWith("jpg")) {
	        			icon = R.drawable.image;
	        		} else if (avatarHeading.endsWith("pdf")) {
	        			icon = R.drawable.pdf;
	        		} else {
	        			icon = R.drawable.file;
	        		}
	        	}
	        	filePath = files[i].getAbsolutePath();
	        	String subHeading = itemsOrSize + " " + lastModifiedDate;
	        	AvatarFile avatarFile = new AvatarFile(
	        			icon, avatarHeading, subHeading, filePath);
	        	myFiles.add(avatarFile);
	        }
	        	
	    }
	    return myFiles;
	}

	protected void onPostExecute(ArrayList<AvatarFile> myFiles) {
		receiveData(myFiles);
	}
	@Override
	public abstract void receiveData(Object result);

}
