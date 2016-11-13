package com.example.remotecontrolpc;

public class AvatarFile {
	private int icon;
	private String avatarHeading, avatarSubheading, path;
	
	public AvatarFile (int icon, String avatarHeading, String avatarSubheading, String path) {
		this.icon = icon;
		this.avatarHeading = avatarHeading;
		this.avatarSubheading = avatarSubheading;
		this.path = path;
	}
	public String getHeading () {
		return avatarHeading;
	}
	public String getPath () {
		return path;
	}
	public String getSubheading () {
		return avatarSubheading;
	}
	public int getIcon () {
		return icon;
	}
}
