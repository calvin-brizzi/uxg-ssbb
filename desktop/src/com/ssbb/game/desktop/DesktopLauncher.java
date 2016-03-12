package com.ssbb.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.ssbb.game.SquishyBlock;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.height = 1080;
        config.width = 1920;
		new LwjglApplication(new SquishyBlock(), config);
	}
}
