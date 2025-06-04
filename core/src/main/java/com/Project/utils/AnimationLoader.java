package com.Project.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.util.ArrayList;

public class AnimationLoader {

    public static Animation<TextureRegion> loadAnimation(String folderPath, int frameCount, float frameDuration) {
        ArrayList<TextureRegion> frames = new ArrayList<>();
        for (int i = 1; i <= frameCount; i++) {
            Texture texture = new Texture(Gdx.files.internal(folderPath + i + ".png"));
            frames.add(new TextureRegion(texture));
        }
        return new Animation<>(frameDuration, frames.toArray(new TextureRegion[0]));
    }
}
