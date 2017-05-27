package com.playground.bukahadiah.helper.image;

import android.os.AsyncTask;
import android.os.ParcelFileDescriptor;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;

abstract public class CopyFile extends AsyncTask<Void, Void, Void> {
	private String picturePath;
	private String fileSavePath;
	private ParcelFileDescriptor parcelFileDescriptor;
	protected boolean save;
	
	public CopyFile(String picturePath, String fileSavePath) {
		this.picturePath = picturePath;
		this.fileSavePath = fileSavePath;
		save = false;
	}
	
	public CopyFile(ParcelFileDescriptor parcelFileDescriptor, String fileSavePath) {
		this.parcelFileDescriptor = parcelFileDescriptor;
		this.fileSavePath = fileSavePath;
		save = false;
	}
	
	@Override
	protected Void doInBackground(Void... params) {
		if (picturePath != null){
			try {
				FileInputStream inputStream = new FileInputStream(picturePath);
				File file = new File(fileSavePath);
				FileOutputStream output = new FileOutputStream(file);
				int bufferSize = 2048;
				byte[] buffer = new byte[bufferSize];
				int len = 0;
				while ((len = inputStream.read(buffer)) != -1) {
				    output.write(buffer, 0, len);
				}
				inputStream.close();
				output.close();
				save = true;
			}catch (Exception e) {
				e.printStackTrace();
			}
		}else if (parcelFileDescriptor != null){
			try {
                FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
                FileInputStream inputStream = new FileInputStream(fileDescriptor);
                File file = new File(fileSavePath);
				FileOutputStream output = new FileOutputStream(file);
				int bufferSize = 2048;
				byte[] buffer = new byte[bufferSize];
				int len = 0;
				while ((len = inputStream.read(buffer)) != -1) {
				    output.write(buffer, 0, len);
				}
				inputStream.close();
				output.close();
				parcelFileDescriptor.close();
				save = true;
            } catch (Exception e) {
                e.printStackTrace();
            }
		}
		return null;
	}

}
