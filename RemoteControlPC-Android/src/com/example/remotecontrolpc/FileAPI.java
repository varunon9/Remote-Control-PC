package com.example.remotecontrolpc;

import java.io.File;

import android.os.Environment;

public class FileAPI {
	public String getExternalStoragePath() {
		String path = "";
		String state = Environment.getExternalStorageState();
		if (Environment.MEDIA_MOUNTED.equals(state) || Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
			//we can read the external storage
			File primaryExternalStorage = Environment.getExternalStorageDirectory();
			String externalStorageRootDirectory;
			if ((externalStorageRootDirectory = primaryExternalStorage.getParent()) == null) {
				path = primaryExternalStorage.toString();
			} else {
				/* getting root directory of path
				 * /storage/emulated/0 -> /storage
				 * */
				File externalStorageRoot;
				while (true) {
					externalStorageRoot = new File(externalStorageRootDirectory);
					if (externalStorageRoot.getParent().equals("/")) {
						break;
					} else {
						externalStorageRootDirectory = externalStorageRoot.getParent();
					}
				}
				File files[] = externalStorageRoot.listFiles();
				//bug here, might not return desired directory, test on multiple devices
				for (File file: files) {
					if (file.isDirectory() && file.canRead() && (file.listFiles().length > 2)) {
						path = file.getAbsolutePath();
						/*System.out.println(path + " " + file.listFiles().length);
						File internalFiles[] = file.listFiles();
						for (File internalFile: internalFiles) {
							System.out.println(internalFile.getAbsolutePath());
						}*/
						return path;
					}
				}
			}
		}
		return path;
	}
}
