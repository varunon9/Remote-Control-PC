package me.varunon9.remotecontrolpc.filetransfer;


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

import java.io.File;

import file.AvatarFile;
import me.varunon9.remotecontrolpc.FileAPI;
import me.varunon9.remotecontrolpc.MainActivity;
import me.varunon9.remotecontrolpc.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class FileTransferFragment extends Fragment implements View.OnClickListener {

    private Button backButton;
    private TextView pathTextView;
    private ListView fileTransferListView;
    private File currentDirectory;
    private String currentPath, rootPath;

    public FileTransferFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_file_transfer, container, false);

        backButton = (Button) rootView.findViewById(me.varunon9.remotecontrolpc.R.id.backButton);
        pathTextView = (TextView) rootView.findViewById(me.varunon9.remotecontrolpc.R.id.pathTextView);
        fileTransferListView = (ListView) rootView.findViewById(me.varunon9.remotecontrolpc.R.id.fileTransferListView);
        currentPath = new FileAPI().getExternalStoragePath();
        rootPath = currentPath;
        currentDirectory = new File(currentPath);
        backButton.setEnabled(false);
        backButton.setOnClickListener(this);
        new GetFilesList(fileTransferListView, getActivity()).execute(currentPath);
        fileTransferListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

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
                    transferFile(file.getHeading(), file.getPath());
                }
            }

        });
        pathTextView.setText(currentPath);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle(getResources().getString(R.string.file_transfer));
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == me.varunon9.remotecontrolpc.R.id.backButton) {
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
