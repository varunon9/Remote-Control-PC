package levelcontrol;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class LevelControl
{
    private boolean check(float level, String os) {
	return level >=0 && level <= 100 && System.getProperty("os.name").contains(os);
    }
    
    public void setBrightness(float level) {
	
	try {
	    if(check(level, "Linux")) {
		Process err = Runtime.getRuntime().exec("echo " + (level*5000/100) + " | tee /sys/class/backlight/intel_backlight/brightness");

		err.getOutputStream().close();

		String line;

		BufferedReader stderr = new BufferedReader(new InputStreamReader(err.getErrorStream()));

		line = stderr.readLine();

		if(line != null) {
		    do {
			System.err.println(line);
		    }while((line = stderr.readLine()) != null);
		}

		stderr.close();
	    }
	    else {
		if(check(level, "Windows")) {
		    Runtime.getRuntime().exec("nircmd.exe setbrightness "+(level/100));
		}
	    }
		
	}
	catch (Exception e) {
	    System.out.println("Error " + e.getMessage());
	    e.printStackTrace();
	}

	
	   
	
    }

    public void setVolume(float level) {
	
	try {
	    if(check(level, "Linux")) {
		Runtime.getRuntime().exec("amixer sset 'Master' "+level+"%");
	    }
	    else {
		if(check(level, "Windows"))
		    Runtime.getRuntime().exec("nircmd.exe setsysvolume "+(level*32768/100));
	    }			
		
	}
	catch (Exception e) {
	    System.out.println("Error " + e.getMessage());
	    e.printStackTrace();
	}
    }
}
