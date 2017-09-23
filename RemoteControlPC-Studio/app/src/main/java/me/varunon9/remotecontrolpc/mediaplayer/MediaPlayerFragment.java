package me.varunon9.remotecontrolpc.mediaplayer;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;

import me.varunon9.remotecontrolpc.MainActivity;
import me.varunon9.remotecontrolpc.MusicControlActivity;
import me.varunon9.remotecontrolpc.MusicImageAvatar;
import me.varunon9.remotecontrolpc.MusicImageAvatarAdapter;
import me.varunon9.remotecontrolpc.R;
import me.varunon9.remotecontrolpc.filetransfer.TransferFileToServer;

/**
 * A simple {@link Fragment} subclass.
 */
public class MediaPlayerFragment extends Fragment {

    ListView mediaPlayerListView;
    ProgressBar avatarProgressBar;

    public MediaPlayerFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_media_player, container, false);
        avatarProgressBar = (ProgressBar) rootView.findViewById(R.id.musicImageAvatarProgressBar);
        mediaPlayerListView = (ListView) rootView.findViewById(R.id.mediaPlayerListView);
        new SongsList(getActivity()) {
            public void receiveData(Object result) {
                avatarProgressBar.setVisibility(View.GONE);
                ArrayList<MusicImageAvatar> songs = (ArrayList<MusicImageAvatar>) result;
                mediaPlayerListView.setAdapter(new MusicImageAvatarAdapter(getActivity(),
                        R.layout.music_image_avatar, songs));
            }
        }.execute();
        mediaPlayerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

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

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle(getResources().getString(R.string.media_player));
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
    public void onDestroy() {
        super.onDestroy();
        MainActivity.sendMessageToServer("STOP_MUSIC");
    }

}
