package com.orchard.valley.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import mainGame.OrchardValley;



public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "Orchard Valley";
		config.width = 1600;
		config.height = 921;
		config.resizable = false;
		new LwjglApplication(new OrchardValley(), config);
	}
}
