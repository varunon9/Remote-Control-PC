package volumecontrol;

import java.util.ArrayList;
import java.lang.*;
import javax.sound.sampled.*;

public class VolumeControl
{
    //private ArrayList<Line> speakers = new ArrayList<Line>();
	
    public VolumeControl()
    {
	/*Mixer.Info[] mixers = AudioSystem.getMixerInfo();

	for (Mixer.Info mixerInfo: mixers) {
	    //if(!mixerInfo.getName().equals("Java Sound Audio Engine")) continue;
	    Mixer mixer  = AudioSystem.getMixer(mixerInfo);
	    Line.Info[] lines = mixer.getSourceLineInfo();

	    for (Line.Info lineInfo : lines) {
		try {
		    Line line = mixer.getLine(lineInfo);
		    speakers.add(line);
		}
		catch (Exception e) {
		    System.out.println("Error " + e.getMessage());
		    e.printStackTrace();
		}

	    }

	    }*/

    }

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

/*
public class VolumeControl
{
    public static void main(String args[])
    {
	Volume v = new Volume();
	v.setVolume(35);
    }
}
*/
