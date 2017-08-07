package me.varunon9.remotecontrolpc.mediaplayer;

import java.util.ArrayList;

import me.varunon9.remotecontrolpc.MainActivity;
import me.varunon9.remotecontrolpc.MusicControlActivity;
import me.varunon9.remotecontrolpc.MusicImageAvatar;
import me.varunon9.remotecontrolpc.MusicImageAvatarAdapter;
import me.varunon9.remotecontrolpc.R;
import me.varunon9.remotecontrolpc.filetransfer.TransferFileToServer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

public class MediaPlayerFragment extends Fragment {
	private static final String ARG_SECTION_NUMBER = "section_number";
	ListView mediaPlayerListView;
	ProgressBar avatarProgressBar;
	public View onCreateView (LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.media_player_fragment, container, false);
		avatarProgressBar = (ProgressBar) rootView.findViewById(R.id.musicImageAvatarProgressBar);
		mediaPlayerListView = (ListView) rootView.findViewById(R.id.mediaPlayerListView);
		new SongsList(getActivity()) {
			public void receiveData(Object result) {
				avatarProgressBar.setVisibility(View.GONE);
				ArrayList <MusicImageAvatar> songs = (ArrayList<MusicImageAvatar>) result;
				mediaPlayerListView.setAdapter(new MusicImageAvatarAdapter(getActivity(),
						R.layout.music_image_avatar, songs));
			}
		}.execute();
		mediaPlayerListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				MusicImageAvatar song = (MusicImageAvatar) parent.getItemAtPosition(position);
				String fileName = song.getHeading();
				String path = song.getData();
				int duration = song.getDuration();
				duration /= 1000; //in seconds
				transferFile(fileName, path, duration);
			}
			
		});
		return rootView;
		
	}
	
	private void transferFile(final String name, String path, final int duration) {
		if (MainActivity.clientSocket != null) {
			MainActivity.sendMessageToServer("FILE_TRANSFER_REQUEST");
			MainActivity.sendMessageToServer(name);
			Toast.makeText(getActivity(), "Wait for music controls", Toast.LENGTH_LONG).show();
			new TransferFileToServer(getActivity()){

				@Override
				public void receiveData(Object result) {
					Intent intent = new Intent(getActivity(), MusicControlActivity.class);
					Bundle bundle = new Bundle();
					bundle.putString("MUSIC_FILE_NAME", name);
					bundle.putInt("MUSIC_DURATION", duration);
					intent.putExtras(bundle);
					startActivity(intent);
					
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
		MainActivity.sendMessageToServer("STOP_MUSIC");
	}
}
