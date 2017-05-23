package com.example.remotecontrolpc.filetransfer;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectOutputStream;

import com.example.remotecontrolpc.CallbackReceiver;
import com.example.remotecontrolpc.MainActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

public abstract class TransferFileToServer extends AsyncTask<String, String, Void> implements CallbackReceiver {

	Context context;
    ProgressDialog progressDialog;
    
    public TransferFileToServer(Context context) {
    	this.context = context;
    }
	@Override
	protected void onPreExecute() {
		progressDialog = new ProgressDialog(context);
    	progressDialog.setTitle("Transfering File");
    	progressDialog.setMessage("Please Wait...");
    	progressDialog.setCancelable(false);
    	progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setProgress(0);
    	progressDialog.show();
	}
	
	@Override
	protected Void doInBackground(String... params) {
		String name = params[0];
		String path = params[1];
		progressDialog.setMessage(name);
		FileInputStream fis = null;
		try {
			if (MainActivity.clientSocket != null) {
				if (MainActivity.objectOutputStream == null) {
					MainActivity.objectOutputStream = new ObjectOutputStream(
							MainActivity.clientSocket.getOutputStream());
				}
				File file = new File(path);
	            long fileSize = file.length();
	            MainActivity.sendMessageToServer(fileSize);
	            fis = new FileInputStream(file);
	            byte[] buffer = new byte[4096];
	            int read = 0;
	            long totalRead = 0;
	            int remaining = (int) fileSize;
	            while (totalRead < fileSize && (read = fis.read(buffer, 0, Math.min(buffer.length, remaining))) > 0) {
	                totalRead += read;
	                remaining -= read;
	                publishProgress("" + (int) ((totalRead * 100) / fileSize));
	                MainActivity.objectOutputStream.write(buffer, 0, read);
	                MainActivity.objectOutputStream.flush();
	            }
	            MainActivity.objectOutputStream.flush();
			}
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			try {
                if (fis != null) {
                    fis.close();
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
		receiveData(result);
	}
	@Override
	public abstract void receiveData(Object result);
}
