
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
	    while(true){
		socket = socketserver.accept();
		System.out.println("Connexion");

		Thread t = new Thread(new ConnexionRunnable(socket));
		t.start();
	    }
	    
	    
	    
	}
	catch (Exception e) {
	    System.out.println("Error " + e.getMessage());
	    e.printStackTrace();
	}

    }
}



class ConnexionRunnable implements Runnable
{
    private Socket socket;
    
    DataOutputStream out;
    
    public ConnexionRunnable (Socket socket) {
	this.socket = socket;


    }

    public void run(){
	BufferedReader in;
	try {
	   
		out = new DataOutputStream(socket.getOutputStream());
	    
		out.writeInt(5);
		out.flush();
		in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
	    
		System.out.println(in.readLine());
		socket.close();
		
	    }
	
	catch (IOException e) {
	    System.out.println("Error " + e.getMessage());
	    e.printStackTrace();
	}
    }

}
