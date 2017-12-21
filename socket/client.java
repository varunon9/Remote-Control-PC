import java.net.*;
import java.io.*;

public class client
{
    public static void main(String args[])
    {
	Socket socket;
	DataInputStream in;
	PrintWriter out;
	
	try {
	    socket = new Socket(InetAddress.getLocalHost(), 3000);

	    in = new DataInputStream(socket.getInputStream());
	    //String msg = in.readLine();
	    //System.out.println(msg);
	    System.out.println("J'ai trouvé "+in.readInt()); //Reception

	    out = new PrintWriter(socket.getOutputStream());
	    out.println("Bien reçu !");
	    out.flush();
	    socket.close();
	}
	catch (UnknownHostException e) {
	    System.out.println("Error " + e.getMessage());
	    e.printStackTrace();
	}
	catch (IOException e){
	    System.out.println("Error" + e.getMessage());
	    e.printStackTrace();
	}


    }
}

