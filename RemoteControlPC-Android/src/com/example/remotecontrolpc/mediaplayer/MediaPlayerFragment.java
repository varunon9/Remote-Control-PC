package com.example.remotecontrolpc.mediaplayer;

import java.util.ArrayList;

import com.example.remotecontrolpc.MainActivity;
import com.example.remotecontrolpc.MusicImageAvatar;
import com.example.remotecontrolpc.MusicImageAvatarAdapter;
import com.example.remotecontrolpc.R;
import com.example.remotecontrolpc.mediaplayer.SongsList;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;

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
		return rootView;
		
	}
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		((MainActivity) activity).onSectionAttached(getArguments().getInt(
				ARG_SECTION_NUMBER));
	}
}
