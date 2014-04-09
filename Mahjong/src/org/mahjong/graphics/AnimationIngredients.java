package org.mahjong.graphics;

import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.resources.client.DataResource;

public interface AnimationIngredients extends ClientBundle{

	@Source("animation_ingredients/wind_east.png")
	ImageResource windEast();
	
	@Source("animation_ingredients/empty.png")
	ImageResource empty();
	
	@Source("animation_ingredients/ini_sound.mp3")
	DataResource initialSound();
	
}