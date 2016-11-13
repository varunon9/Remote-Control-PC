package com.example.remotecontrolpc.filetransfer;

import java.io.File;
import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.remotecontrolpc.AvatarFileAdapter;
import com.example.remotecontrolpc.FileAPI;
import com.example.remotecontrolpc.MainActivity;
import com.example.remotecontrolpc.AvatarFile;
import com.example.remotecontrolpc.R;
import com.example.remotecontrolpc.Utility;

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
		listFiles(currentPath);
		fileTransferListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				AvatarFile file = (AvatarFile) parent.getItemAtPosition(position);
				String path = file.getPath();
				if (new File(path).isDirectory()) {
					backButton.setEnabled(true);
					listFiles(path);
				} else {
					Toast.makeText(getActivity(), "Sending " + file.getHeading(), Toast.LENGTH_LONG).show();
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
	private void listFiles(String path) {
		currentPath = path;
		currentDirectory = new File(currentPath);
		pathTextView.setText(path);
		ArrayList<AvatarFile> filesInFolder = getFiles(path);
		fileTransferListView.setAdapter(new AvatarFileAdapter(getActivity(),
				R.layout.music_image_avatar, filesInFolder));
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		int id = v.getId();
		if (id == R.id.backButton) {
			if (! currentPath.equals(rootPath)) {
				currentPath = currentDirectory.getParent();
				//currentDirectory = new File(currentPath);
				listFiles(currentPath);
			} else {
				backButton.setEnabled(false);
			}
		}
	}
	private ArrayList<AvatarFile> getFiles(String path) {
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
	        			itemsOrSize = 0 + " items";
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
}
