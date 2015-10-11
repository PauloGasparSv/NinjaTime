package com.paulogaspar.ninja.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.paulogaspar.ninja.MyGame;
 	
public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "Ninja Time";
		config.useGL30 = false;
		config.width = 800;
		config.height = 600;
		config.resizable = true;
		//1024 768
		new LwjglApplication(new MyGame(), config);
	}
}
