package com.example.remotecontrolpc.filedownload;

import java.util.ArrayList;
import java.util.Stack;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.example.remotecontrolpc.AvatarFileAdapter;
import com.example.remotecontrolpc.MainActivity;
import com.example.remotecontrolpc.R;

import file.AvatarFile;

public class FileDownloadFragment extends Fragment implements OnClickListener {
	private Button backButton;
	private TextView pathTextView;
	private ListView fileDownloadListView;
	private Stack<String> pathStack;
	private static final String ARG_SECTION_NUMBER = "section_number";
	public View onCreateView (LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.file_transfer_fragment, container, false);
		backButton = (Button) rootView.findViewById(R.id.backButton);
		pathTextView = (TextView) rootView.findViewById(R.id.pathTextView);
		fileDownloadListView = (ListView) rootView.findViewById(R.id.fileTransferListView);
		pathStack = new Stack<String>();
		pathStack.push("/");
		pathTextView.setText(pathStack.peek());
		backButton.setEnabled(false);
		backButton.setOnClickListener(this);
		getFiles();
		fileDownloadListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				AvatarFile file = (AvatarFile) parent.getItemAtPosition(position);
				String path = file.getPath();
				if (file.getType().equals("folder")) {
					pathStack.push(path);
					String currentPath = pathStack.peek();
					pathTextView.setText(currentPath);
					backButton.setEnabled(true);
					getFiles();
				} else {
					//Toast.makeText(getActivity(), "Downloading " + file.getHeading(), Toast.LENGTH_LONG).show();
					if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
						downloadFile(file.getHeading(), file.getPath());
					} else {
						checkForPermissionAndDownload(file.getHeading(), file.getPath());
					}
				}
			}
			
		});
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
		int id = v.getId();
		if (id == R.id.backButton) {
			pathStack.pop();
			String currentPath = pathStack.peek();
			getFiles();
			pathTextView.setText(currentPath);
			if (! currentPath.equals("/")) {
			} else {
				backButton.setEnabled(false);
			}
		}
		
	}
	
	private void getFiles() {
		String message = "FILE_DOWNLOAD_LIST_FILES";
		MainActivity.sendMessageToServer(message);
		message = pathStack.peek();
		MainActivity.sendMessageToServer(message);
		new GetFilesList(fileDownloadListView, getActivity()).execute(pathStack.peek());
	}
	
	
	@TargetApi(Build.VERSION_CODES.M)
	private void checkForPermissionAndDownload(String name, String path) {
		if (getActivity().checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
				!= PackageManager.PERMISSION_GRANTED) {
		    // Should we show an explanation?
		    if (getActivity().shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
		    	Toast.makeText(getActivity(), "Read Permission is necessary to transfer ", Toast.LENGTH_LONG).show();
		    } else {
		        getActivity().requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
		        //1 is integer constant for WRITE_EXTERNAL_STORAGE permission, uses in onRequestPermissionResult
		    }
        } else {
        	downloadFile(name, path);
        }
	}
	
	protected void downloadFile(String name, String path) {
		if (MainActivity.clientSocket != null) {
			MainActivity.sendMessageToServer("FILE_DOWNLOAD_REQUEST");
			MainActivity.sendMessageToServer(path);
			new DownloadFileFromServer(getActivity()).execute(name);
		} else {
			Toast.makeText(getActivity(), "Not Connected", Toast.LENGTH_LONG).show();
		}
	}
}

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
					R.layout.music_image_avatar, filesInFolder));
		} else {
			Toast.makeText(context, "Not Connected to PC", Toast.LENGTH_LONG).show();
		}
		
	}
	
}
