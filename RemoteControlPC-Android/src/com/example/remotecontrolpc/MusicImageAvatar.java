package com.example.remotecontrolpc;

public class MusicImageAvatar {
	private int icon;
	private String avatarHeading, avatarSubheading, avatarData, type;
	
	public MusicImageAvatar (int icon, String avatarHeading, String avatarSubheading, String avatarData, String type) {
		this.icon = icon;
		this.avatarHeading = avatarHeading;
		this.avatarSubheading = avatarSubheading;
		this.avatarData = avatarData;
		this.type = type;
	}
	public String getHeading () {
		return avatarHeading;
	}
	public String getData () {
		return avatarData;
	}
	public String getType () {
		return type;
	}
	public String getSubheading () {
		return avatarSubheading;
	}
	public int getIcon () {
		return icon;
	}
}
