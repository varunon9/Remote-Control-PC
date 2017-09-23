package me.varunon9.remotecontrolpc.filetransfer;

import android.content.Context;
import android.widget.ListView;

import java.util.ArrayList;

import file.AvatarFile;
import me.varunon9.remotecontrolpc.AvatarFileAdapter;

class GetFilesList extends FilesList {
    ListView fileTransferListView;
    Context context;
	GetFilesList(ListView fileTransferListView, Context context) {
		this.fileTransferListView = fileTransferListView;
		this.context = context;
	}
	@Override
	public void receiveData(Object result) {
		ArrayList<AvatarFile> filesInFolder = (ArrayList<AvatarFile>) result;
		fileTransferListView.setAdapter(new AvatarFileAdapter(context,
				me.varunon9.remotecontrolpc.R.layout.music_image_avatar, filesInFolder));
		
	}
	
}
