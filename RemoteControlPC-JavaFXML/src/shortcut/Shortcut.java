package shortcut;


import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;

public class Shortcut {
	public final static String[] software = {"studio", "skype", "discord", "chromium", "firefox", "gnome-terminal", "gimp", "emacs"
	,"gedit", "nautilus", "steam"};

	public Shortcut() {}

	public String search() {
		String ret = "";

		try {
			for (int i = 0; i < software.length; i++){
				Process p = Runtime.getRuntime().exec("which "+software[i]);
				DataInputStream stdin = new DataInputStream(p.getInputStream());
				String cmd = stdin.readLine();
				System.out.println(cmd);

				if(cmd != null && !cmd.equals(""))
					ret += software[i]+"\n";
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return ret;
	}

	public void execShortcut(String name) {
		if (System.getProperty("os.name").equals("Linux")) {
			try {
				Runtime.getRuntime().exec(name+"&");
			} catch (Exception e) {
				System.out.println("Error " + e.getMessage());
				e.printStackTrace();
			}
		}
	}


}