package me.varunon9.remotecontrolpc;

public class MusicImageAvatar {

	private int icon, duration;
	private String avatarHeading, avatarSubheading, avatarData, type;
	
	public MusicImageAvatar (int icon, int duration,  String avatarHeading, 
			String avatarSubheading, String avatarData, String type) {
		this.icon = icon;
		this.duration = duration;
		this.avatarHeading = avatarHeading;
		this.avatarSubheading = avatarSubheading;
		this.avatarData = avatarData;
		this.type = type;
	}
	
	public String getHeading() {
		return avatarHeading;
	}
	
	public String getData() {
		return avatarData;
	}
	
	public String getType() {
		return type;
	}
	
	public String getSubheading() {
		return avatarSubheading;
	}
	
	public int getIcon() {
		return icon;
	}
	
	public int getDuration() {
		return duration;
	}
}
