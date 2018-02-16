package volumecontrol;

public class VolumeControl
{
    public void setVolume(float level) {
	if(level >=0 && level <= 100 && System.getProperty("os.name").equals("Linux"))
	    {
		try {
		    Runtime.getRuntime().exec("amixer sset 'Master' "+level+"%");
		}
		catch (Exception e) {
		    System.out.println("Error " + e.getMessage());
		    e.printStackTrace();
		}
	    }
		

    }
}
