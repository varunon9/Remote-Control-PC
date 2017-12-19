
import java.io.*;
import java.net.*;

public class serveur2
{
    public static void main(String args[])
    {
	ServerSocket socketserver;
	Socket socket;
	DataOutputStream out;

	try {
	    socketserver = new ServerSocket(3000);
	    Thread t = new Thread(new accept(socketserver));
	    t.start();
	    
	}
	catch (IOException e) {
	    System.out.println("Error " + e.getMessage());
	    e.printStackTrace();
	}

    }
}



class accept implements Runnable
{
    private ServerSocket serversocket;
    private Socket socket;
    private int nb_client;
    DataOutputStream out;
    
    public accept (ServerSocket server) {
	this.serversocket = server;
	this.nb_client = 0;

    }

    public void run(){
	
	try {
	    while(true){
		socket = serversocket.accept();
	    System.out.println("Connection !");

	    out = new DataOutputStream(socket.getOutputStream());
	    
	    out.writeInt(++nb_client);
	    out.flush();
	    socket.close();

	    }
	}
	catch (IOException e) {
	    System.out.println("Error " + e.getMessage());
	    e.printStackTrace();
	}

    }
}
