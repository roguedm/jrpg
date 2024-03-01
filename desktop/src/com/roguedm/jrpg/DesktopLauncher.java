package com.roguedm.jrpg;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.roguedm.jrpg.JRPGGame;

// Please note that on macOS your application needs to be started with the -XstartOnFirstThread JVM argument
public class DesktopLauncher {
	public static void main (String[] arg) {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		config.setWindowSizeLimits(800, 480, 1980, 1080);
		config.setForegroundFPS(60);
		config.setTitle("JRPG");
		new Lwjgl3Application(new JRPGGame(), config);
	}
}
