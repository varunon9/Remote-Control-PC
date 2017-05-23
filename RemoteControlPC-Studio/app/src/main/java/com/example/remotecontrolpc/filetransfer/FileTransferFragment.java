package com.example.remotecontrolpc.filetransfer;

import java.io.File;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.remotecontrolpc.AvatarFileAdapter;
import com.example.remotecontrolpc.FileAPI;
import com.example.remotecontrolpc.MainActivity;

import file.AvatarFile;

import com.example.remotecontrolpc.R;
import com.example.remotecontrolpc.filedownload.DownloadFileFromServer;

public class FileTransferFragment extends Fragment implements OnClickListener {
	private Button backButton;
	private TextView pathTextView;
	private ListView fileTransferListView;
	private File currentDirectory;
	private String currentPath, rootPath;
	private static final String ARG_SECTION_NUMBER = "section_number";
	public View onCreateView (LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.file_transfer_fragment, container, false);
		backButton = (Button) rootView.findViewById(R.id.backButton);
		pathTextView = (TextView) rootView.findViewById(R.id.pathTextView);
		fileTransferListView = (ListView) rootView.findViewById(R.id.fileTransferListView);
		currentPath = new FileAPI().getExternalStoragePath();
		rootPath = currentPath;
		currentDirectory = new File(currentPath);
		backButton.setEnabled(false);
		backButton.setOnClickListener(this);
		new GetFilesList(fileTransferListView, getActivity()).execute(currentPath);
		fileTransferListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				AvatarFile file = (AvatarFile) parent.getItemAtPosition(position);
				String path = file.getPath();
				File tempDirectoryOrFile = new File(path);
				if (tempDirectoryOrFile.isDirectory()) {
					File tempArray[] = tempDirectoryOrFile.listFiles();
					//to avoid crash when 0 item
	        		if (tempArray != null && tempArray.length > 0) {
	        			backButton.setEnabled(true);
						currentPath = path;
						currentDirectory = tempDirectoryOrFile;
						pathTextView.setText(currentPath);
						new GetFilesList(fileTransferListView, getActivity()).execute(currentPath);
	        		}
				} else {
					//Toast.makeText(getActivity(), "Sending " + file.getHeading(), Toast.LENGTH_LONG).show();
					transferFile(file.getHeading(), file.getPath());
				}
			}
			
		});
		pathTextView.setText(currentPath);
		return rootView;
		
	}
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		((MainActivity) activity).onSectionAttached(getArguments().getInt(
				ARG_SECTION_NUMBER));
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		int id = v.getId();
		if (id == R.id.backButton) {
			currentPath = currentDirectory.getParent();
			currentDirectory = new File(currentPath);
			new GetFilesList(fileTransferListView, getActivity()).execute(currentPath);
			pathTextView.setText(currentPath);
			if (! currentPath.equals(rootPath)) {
			} else {
				backButton.setEnabled(false);
			}
		}
	}
	
	private void transferFile(String name, String path) {
		if (MainActivity.clientSocket != null) {
			MainActivity.sendMessageToServer("FILE_TRANSFER_REQUEST");
			MainActivity.sendMessageToServer(name);
			new TransferFileToServer(getActivity()){
				@Override
				public void receiveData(Object result) {	
				}
			}.execute(new String[]{name, path});
		} else {
			Toast.makeText(getActivity(), "Not Connected", Toast.LENGTH_LONG).show();
		}
	}
	
}

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
				R.layout.music_image_avatar, filesInFolder));
		
	}
	
}
