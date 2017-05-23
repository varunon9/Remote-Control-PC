package com.example.remotecontrolpc.imageviewer;

import java.util.ArrayList;

import com.example.remotecontrolpc.MainActivity;
import com.example.remotecontrolpc.MusicControlActivity;
import com.example.remotecontrolpc.MusicImageAvatar;
import com.example.remotecontrolpc.MusicImageAvatarAdapter;
import com.example.remotecontrolpc.R;
import com.example.remotecontrolpc.filetransfer.TransferFileToServer;
import com.example.remotecontrolpc.imageviewer.ImagesList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class ImageViewerFragment extends Fragment {
	
	private static final String ARG_SECTION_NUMBER = "section_number";
	ListView mediaPlayerListView;
	ProgressBar avatarProgressBar;
	
	public View onCreateView (LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.media_player_fragment, container, false);
		avatarProgressBar = (ProgressBar) rootView.findViewById(R.id.musicImageAvatarProgressBar);
		mediaPlayerListView = (ListView) rootView.findViewById(R.id.mediaPlayerListView);
		new ImagesList(getActivity()) {
			public void receiveData(Object result) {
				avatarProgressBar.setVisibility(View.GONE);
				ArrayList <MusicImageAvatar> images = (ArrayList<MusicImageAvatar>) result;
				mediaPlayerListView.setAdapter(new MusicImageAvatarAdapter(getActivity(),
						R.layout.music_image_avatar, images));
			}
		}.execute();
		mediaPlayerListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				MusicImageAvatar image = (MusicImageAvatar) parent.getItemAtPosition(position);
				String fileName = image.getHeading();
				String path = image.getData();
				transferFile(fileName, path);
			}
			
		});
		return rootView;		
	}
	
	private void transferFile(final String name, String path) {
		if (MainActivity.clientSocket != null) {
			MainActivity.sendMessageToServer("FILE_TRANSFER_REQUEST");
			MainActivity.sendMessageToServer(name);
			new TransferFileToServer(getActivity()){

				@Override
				public void receiveData(Object result) {
					MainActivity.sendMessageToServer("SHOW_IMAGE");
					MainActivity.sendMessageToServer(name);
				}
				
			}.execute(new String[]{name, path});
		} else {
			Toast.makeText(getActivity(), "Not Connected", Toast.LENGTH_LONG).show();
		}
	}
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		((MainActivity) activity).onSectionAttached(getArguments().getInt(
				ARG_SECTION_NUMBER));
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		MainActivity.sendMessageToServer("CLOSE_IMAGE_VIEWER");
	}
}
