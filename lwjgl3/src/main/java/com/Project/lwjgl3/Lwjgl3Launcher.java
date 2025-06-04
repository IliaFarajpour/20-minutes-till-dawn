package com.Project.lwjgl3;

import com.Project.Main;
import com.badlogic.gdx.Graphics.DisplayMode;
import com.badlogic.gdx.backends.lwjgl3.*;

public class Lwjgl3Launcher {
    private static Main mainGame;

    public static void main(String[] args) {
        if (StartupHelper.startNewJvmIfRequired()) return;
        createApplication();
    }

    private static Lwjgl3Application createApplication() {
        mainGame = new Main();
        Lwjgl3ApplicationConfiguration config = getDefaultConfiguration();

        config.setWindowListener(new Lwjgl3WindowAdapter() {
            @Override
            public void filesDropped(String[] files) {
                if (files.length > 0 && mainGame.getProfileView() != null) {
                    mainGame.getProfileView().handleFileDrop(files[0]);
                }
            }
        });

        return new Lwjgl3Application(mainGame, config);
    }

    private static Lwjgl3ApplicationConfiguration getDefaultConfiguration() {
        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        config.setTitle("Project");
        config.useVsync(true);
        config.setForegroundFPS(60);

        DisplayMode displayMode = Lwjgl3ApplicationConfiguration.getDisplayMode();
        config.setFullscreenMode(displayMode);

        config.setWindowIcon("libgdx128.png", "libgdx64.png", "libgdx32.png", "libgdx16.png");
        return config;
    }
}
