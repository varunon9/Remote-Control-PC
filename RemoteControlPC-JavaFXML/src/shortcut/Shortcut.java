package shortcut;


import java.io.DataInputStream;
import java.io.IOException;


public class Shortcut {
	public final static String[] software = {"studio", "skype", "discord", "chromium", "firefox", "gnome-terminal", "gimp", "emacs"
	,"gedit", "nautilus", "steam"};

	public Shortcut() {}

	public String search() {

		StringBuilder stringBuilder = new StringBuilder();
		try {
			for (int i = 0; i < software.length; i++){
				Process p = Runtime.getRuntime().exec("which "+software[i]);
				DataInputStream stdin = new DataInputStream(p.getInputStream());
				String cmd = stdin.readLine();
				System.out.println(cmd);

				if(cmd != null && !cmd.equals("")) {
					stringBuilder.append(software[i]).append("\n");
				}

			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return stringBuilder.toString();
	}

	public void execShortcut(String name) {
		if (System.getProperty("os.name").equals("Linux")) {
			try {
				Runtime.getRuntime().exec(name);
			} catch (Exception e) {
				System.out.println("Error " + e.getMessage());
				e.printStackTrace();
			}
		}
	}


}