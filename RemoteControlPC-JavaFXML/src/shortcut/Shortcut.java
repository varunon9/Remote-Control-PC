package shortcut;

import java.io.File;

public class Shortcut {
	public Shortcut() {
		search();
	}

	private void search() {
		/*File file = new File("/usr/bin");

		for (File f : file.listFiles()) {
			System.out.println(f.toString());
		}*/

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