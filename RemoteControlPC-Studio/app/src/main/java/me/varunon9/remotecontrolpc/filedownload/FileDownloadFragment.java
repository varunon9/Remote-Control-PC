package me.varunon9.remotecontrolpc.filedownload;


import android.Manifest;
import android.annotation.TargetApi;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Stack;

import file.AvatarFile;
import me.varunon9.remotecontrolpc.MainActivity;
import me.varunon9.remotecontrolpc.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class FileDownloadFragment extends Fragment implements View.OnClickListener {

    private Button backButton;
    private TextView pathTextView;
    private ListView fileDownloadListView;
    private Stack<String> pathStack;

    public FileDownloadFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_file_download, container, false);
        backButton = (Button) rootView.findViewById(me.varunon9.remotecontrolpc.R.id.backButton);
        pathTextView = (TextView) rootView.findViewById(me.varunon9.remotecontrolpc.R.id.pathTextView);
        fileDownloadListView = (ListView) rootView.findViewById(me.varunon9.remotecontrolpc.R.id.fileTransferListView);
        pathStack = new Stack<String>();
        pathStack.push("/");
        pathTextView.setText(pathStack.peek());
        backButton.setEnabled(false);
        backButton.setOnClickListener(this);
        getFiles();
        fileDownloadListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

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
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle(getResources().getString(R.string.file_download));
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == me.varunon9.remotecontrolpc.R.id.backButton) {
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
                Toast.makeText(getActivity(), "Write Permission is necessary to download", Toast.LENGTH_LONG).show();
            } else {
                getActivity().requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
                //2 is integer constant for WRITE_EXTERNAL_STORAGE permission, uses in onRequestPermissionResult
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
