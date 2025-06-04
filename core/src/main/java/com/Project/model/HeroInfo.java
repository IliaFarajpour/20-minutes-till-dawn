package com.Project.model;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class HeroInfo {
    private String name;
    private Animation<TextureRegion> walkAnimation;
    private Animation<TextureRegion> idleAnimation;
    private Texture mainTexture;

    private int speed;
    private int hp;

    public HeroInfo(String name, Animation<TextureRegion> walkAnimation,
                    Animation<TextureRegion> idleAnimation, Texture mainTexture,
                    int speed, int hp) {
        this.name = name;
        this.walkAnimation = walkAnimation;
        this.idleAnimation = idleAnimation;
        this.mainTexture = mainTexture;
        this.speed = speed;
        this.hp = hp;
    }

    public String getName() {
        return name;
    }

    public Animation<TextureRegion> getWalkAnimation() {
        return walkAnimation;
    }

    public Animation<TextureRegion> getIdleAnimation() {
        return idleAnimation;
    }

    public Texture getMainTexture() {
        return mainTexture;
    }

    public int getSpeed() {
        return speed;
    }

    public int getHp() {
        return hp;
    }

    @Override
    public String toString() {
        return name + " (Speed: " + speed + ", HP: " + hp + ")";
    }

    public void dispose() {
        for (TextureRegion region : walkAnimation.getKeyFrames()) {
            region.getTexture().dispose();
        }
        for (TextureRegion region : idleAnimation.getKeyFrames()) {
            region.getTexture().dispose();
        }
        if (mainTexture != null) {
            mainTexture.dispose();
        }
    }
}
