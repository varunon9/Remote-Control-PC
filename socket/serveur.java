import java.io.*;
import java.net.*;

public class serveur
{
    public static void main(String args[])
    {
	ServerSocket socketserver;
	Socket socket;
	//DataOutputStream out;
	//BufferedReader in;
  DataInputStream dis;

	try {
	    while(true){
		socketserver = new ServerSocket(3000);
		socket = socketserver.accept();
		System.out.println("Connexion !");
/*
		out = new DataOutputStream(socket.getOutputStream());
		//out.println("5");
		//out.flush();
		//byte[4096];
		out.writeInt(5);
		out.flush();

		in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

		System.out.println(in.readLine());
*/
    dis = new DataInputStream(socket.getInputStream());
    System.out.println(dis.readInt());
    dis.close();
		socket.close();
		socketserver.close();
	    }
	}
	catch (IOException e) {
	    System.out.println("Error " + e.getMessage());
	    e.printStackTrace();
	}

    }
}
