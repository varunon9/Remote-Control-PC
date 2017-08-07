package me.varunon9.remotecontrolpc.filedownload;

import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;

import me.varunon9.remotecontrolpc.FileAPI;
import me.varunon9.remotecontrolpc.MainActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

public class DownloadFileFromServer extends AsyncTask<String, String, Void > {
	
    Context context;
    ProgressDialog progressDialog;
    
    public DownloadFileFromServer(Context context) {
    	this.context = context;
    }
    
    @Override
    protected void onPreExecute() {
    	progressDialog = new ProgressDialog(context);
    	progressDialog.setTitle("Downloading File");
    	progressDialog.setMessage("Please Wait...");
    	progressDialog.setCancelable(false);
    	progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setProgress(0);
    	progressDialog.show();
    }
    
	@Override
	protected Void doInBackground(String... params) {
		String name = params[0];
		progressDialog.setMessage(name);
		FileOutputStream fos = null;
		String path = new FileAPI().getExternalStoragePath();
		path = path + "/RemoteControlPC/" + name;
		File file = new File(path);
		File dirs = new File(file.getParent());
		if (!dirs.exists()) {
			dirs.mkdirs();
		}
		try {
			if (MainActivity.clientSocket != null) {
				if (MainActivity.objectInputStream == null) {
					MainActivity.objectInputStream = new ObjectInputStream(
							MainActivity.clientSocket.getInputStream());
				}
				fos = new FileOutputStream(file);
				byte buffer[] = new byte[4096];
				long fileSize = (long) MainActivity.objectInputStream.readObject();
				int read = 0;
				long totalRead = 0;
				int remaining = (int) fileSize;
				while ((read = MainActivity.objectInputStream.read(buffer, 0, Math.min(buffer.length, remaining))) > 0) {
					totalRead += read;
					remaining -= read;
					publishProgress("" + (int) ((totalRead * 100) / fileSize));
					fos.write(buffer, 0, read);
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (fos != null) {
					fos.close();
				}
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	@Override
	protected void onProgressUpdate(String... progress) {
		progressDialog.setProgress(Integer.parseInt(progress[0]));
	}
	
	@Override
	protected void onPostExecute(Void result) {
		if (progressDialog.isShowing()) {
			progressDialog.dismiss();
		}
	}
}
