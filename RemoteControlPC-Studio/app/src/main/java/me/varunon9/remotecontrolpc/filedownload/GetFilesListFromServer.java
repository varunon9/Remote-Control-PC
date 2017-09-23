package me.varunon9.remotecontrolpc.filedownload;

import android.os.AsyncTask;

import java.io.ObjectInputStream;
import java.util.ArrayList;

import file.AvatarFile;
import me.varunon9.remotecontrolpc.CallbackReceiver;
import me.varunon9.remotecontrolpc.MainActivity;

public abstract class GetFilesListFromServer extends AsyncTask<String, Void, ArrayList<AvatarFile>> implements CallbackReceiver {

	public abstract void receiveData(Object result);

	@Override
	protected ArrayList<AvatarFile> doInBackground(String... params) {
		String path = params[0];
		ArrayList<AvatarFile> myFiles = null;
		try {
			if (MainActivity.clientSocket != null) {
				if (MainActivity.objectInputStream == null) {
					MainActivity.objectInputStream = new ObjectInputStream(
							MainActivity.clientSocket.getInputStream());
				}
				myFiles = (ArrayList<AvatarFile>) MainActivity.objectInputStream.readObject();
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		return myFiles;
	}

	protected void onPostExecute(ArrayList<AvatarFile> myFiles) {
		receiveData(myFiles);
	}

}
