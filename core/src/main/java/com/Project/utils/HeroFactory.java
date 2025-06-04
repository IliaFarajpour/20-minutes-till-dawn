package com.Project.utils;

import com.Project.model.HeroInfo;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class HeroFactory {

    public static HeroInfo createShana() {
        String basePath = "characters/Shana/";
        Animation<TextureRegion> idleAnimation = AnimationLoader.loadAnimation(basePath + "idle/", 4, 0.1f);
        Animation<TextureRegion> walkAnimation = AnimationLoader.loadAnimation(basePath + "walk/", 7, 0.1f);
        Texture mainTexture = new Texture(Gdx.files.internal(basePath + "idle/1.png"));
        return new HeroInfo("Shana", walkAnimation, idleAnimation, mainTexture,4,4);
    }
    public static HeroInfo createDasher() {
        String basePath = "characters/Dasher/";
        Animation<TextureRegion> idleAnimation = AnimationLoader.loadAnimation(basePath + "idle/", 6, 0.12f);
        Animation<TextureRegion> walkAnimation = AnimationLoader.loadAnimation(basePath + "walk/", 3, 0.1f);
        Texture mainTexture = new Texture(Gdx.files.internal(basePath + "idle/1.png"));
        return new HeroInfo("Dasher", walkAnimation, idleAnimation, mainTexture,10,2);
    }

    public static HeroInfo createDiamond() {
        String basePath = "characters/Diamond/";
        Animation<TextureRegion> idleAnimation = AnimationLoader.loadAnimation(basePath + "idle/", 6, 0.15f);
        Animation<TextureRegion> walkAnimation = AnimationLoader.loadAnimation(basePath + "walk/", 8, 0.1f);
        Texture mainTexture = new Texture(Gdx.files.internal(basePath + "idle/1.png"));
        return new HeroInfo("Diamond", walkAnimation, idleAnimation, mainTexture,1,7);
    }

    public static HeroInfo createScarlet() {
        String basePath = "characters/Scarlet/";
        Animation<TextureRegion> idleAnimation = AnimationLoader.loadAnimation(basePath + "idle/", 6, 0.1f);
        Animation<TextureRegion> walkAnimation = AnimationLoader.loadAnimation(basePath + "walk/", 3, 0.1f);
        Texture mainTexture = new Texture(Gdx.files.internal(basePath + "idle/1.png"));
        return new HeroInfo("Scarlet", walkAnimation, idleAnimation, mainTexture,5,3);
    }

    public static HeroInfo createLilith() {
        String basePath = "characters/Lilith/";
        Animation<TextureRegion> idleAnimation = AnimationLoader.loadAnimation(basePath + "idle/", 6, 0.14f);
        Animation<TextureRegion> walkAnimation = AnimationLoader.loadAnimation(basePath + "walk/", 3, 0.12f);
        Texture mainTexture = new Texture(Gdx.files.internal(basePath + "idle/1.png"));
        return new HeroInfo("Lilith", walkAnimation, idleAnimation, mainTexture,3,5);
    }
}
