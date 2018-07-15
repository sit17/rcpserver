package com.i5i58.service.config;

public enum ChannelVideoConfig {
	
	QUALITY_INIT("quality_init",0),
	
	
	QUALITY_DEFAULT("quality_default",1),
	QUALITY_LOW("quality_low", 2),
	QUALITY_MEDIUM("quality_medium", 3),
	QUALITY_HIGH("quality_high", 4),
	QUALITY_480LEVEL("quality_480level", 5),
	QUALITY_720LEVEL("quality_720level", 6),
	
	MODE_FLOATING_RIGHT_VERTICAL("mode_floating_right_vertical", 7),
	MODE_FLOATING_LEFT_VERTICAL("mode_floating_left_vertical", 8),
	MODE_LATTICE_ASPECT_FIT("mode_lattice_aspect_fit", 9),
	MODE_LATTICE_ASPECT_FILL("mode_lattice_aspect_fill", 10),
	
	LOGO_MARK_TRUE("logo_mark_true", 11), 
	LOGO_MARK_FALSE("logo_mark_false", 12);
	
	public static boolean belongQuality(int quality){
		if(quality > 6 || quality < 1){
			return false;
		}
		return true;
	}
	public static boolean belongMode(int mode){
		if(mode > 10 || mode < 7){
			return false;
		}
		return true;
	}
	public static boolean belongLogo(int logo){
		if(logo > 12 || logo < 11){
			return false;
		}
		return true;
	}
	
	private String code;
	private int value;

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	private ChannelVideoConfig(String code, int value) {
		this.code = code;
		this.value = value;
	}
}
