package shortcut;

public class Shortcut {
    public Shortcut(){}

    public void execShortcut(String name){
	if(System.getProperty("os.name").equals("Linux")) {
	    try {
		Runtime.getRuntime().exec(name);
	    }
	    catch (Exception e) {
		System.out.println("Error " + e.getMessage());
		e.printStackTrace();
	    }
	}
    }
}
