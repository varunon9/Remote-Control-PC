package com.example.remotecontrolpc.connect;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

import com.example.remotecontrolpc.CallbackReceiver;
import com.example.remotecontrolpc.MainActivity;

import android.content.Context;
import android.os.AsyncTask;

public abstract class MakeConnection extends AsyncTask<Void, Void, Socket> implements CallbackReceiver {
    String ipAddress, port;
    Context context;
    Socket clientSocket;
	MakeConnection(String ipAddress, String port, Context context) {
	    this.ipAddress = ipAddress;
	    this.port = port;
	    this.context = context;
	}
	@Override
	protected Socket doInBackground(Void... params) {
		try {
			int portNumber = Integer.parseInt(port);
			SocketAddress socketAddress = new InetSocketAddress(ipAddress, portNumber);
			clientSocket = new Socket();
			//3s timeout
			clientSocket.connect(socketAddress, 3000);
			MainActivity.objectInputStream = new ObjectInputStream(clientSocket.getInputStream());
			MainActivity.objectOutputStream = new ObjectOutputStream(clientSocket.getOutputStream());
		} catch(Exception e) {
			e.printStackTrace();
			clientSocket = null;
		}
		return clientSocket;
		// TODO Auto-generated method stub
		
	}
    
	protected void onPostExecute(Socket clientSocket) {
		receiveData(clientSocket);
	}
	@Override
	public abstract void receiveData(Object result);
}
