package com.Project.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class GameAssets {

    private static GameAssets instance;
    private Skin skin;

    private GameAssets() {
        skin = new Skin(Gdx.files.internal("skin/pixthulhu-ui.json"));
    }

    public static GameAssets getGameAssets() {
        if (instance == null) {
            instance = new GameAssets();
        }
        return instance;
    }

    public Skin getSkin() {
        return skin;
    }

    public void setSkin(Skin skin) {
        this.skin = skin;
    }
}
