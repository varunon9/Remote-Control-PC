package me.varunon9.remotecontrolpc.filedownload;

import android.content.Context;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import file.AvatarFile;
import me.varunon9.remotecontrolpc.AvatarFileAdapter;

class GetFilesList extends GetFilesListFromServer {
    ListView fileDownloadListView;
    Context context;
	GetFilesList(ListView fileDownloadListView, Context context) {
		this.fileDownloadListView = fileDownloadListView;
		this.context = context;
	}
	@Override
	public void receiveData(Object result) {
		ArrayList<AvatarFile> filesInFolder = (ArrayList<AvatarFile>) result;
		if (filesInFolder != null) {
			fileDownloadListView.setAdapter(new AvatarFileAdapter(context,
					me.varunon9.remotecontrolpc.R.layout.music_image_avatar, filesInFolder));
		} else {
			Toast.makeText(context, "Not Connected to PC", Toast.LENGTH_LONG).show();
		}
		
	}
	
}
