import java.io.*;
import java.net.*;

public class serveur
{
    public static void main(String args[])
    {
	ServerSocket socketserver;
	Socket socket;
	DataOutputStream out;

	try {
	    socketserver = new ServerSocket(3000);
	    socket = socketserver.accept();
	    System.out.println("Connection !");

	    out = new DataOutputStream(socket.getOutputStream());
	    //out.println("5");
	    //out.flush();
	    //byte[4096];
	    out.writeInt(5);
	    out.flush();
	    socket.close();
	    socketserver.close();

	}
	catch (IOException e) {
	    System.out.println("Error " + e.getMessage());
	    e.printStackTrace();
	}

    }
}

