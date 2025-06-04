package com.Project.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

public class Tree {
    private float x;
    private float y;
    private float alpha = 0f; // برای fade-in

    private Animation<TextureRegion> animation;
    private float stateTime = 0f;

    private static final float FRAME_DURATION = 0.8f;
    private static final float WIDTH = 150;
    private static final float HEIGHT = 200;
    private static final float FADE_IN_SPEED = 1.5f; // سرعت محو شدن

    public Tree(float x, float y, Animation<TextureRegion> animation) {
        this.x = x;
        this.y = y;
        this.animation = animation;
    }

    public void render(SpriteBatch batch) {
        stateTime += Gdx.graphics.getDeltaTime();

        if (alpha < 1f) {
            alpha += Gdx.graphics.getDeltaTime() * FADE_IN_SPEED;
        }

        TextureRegion currentFrame = animation.getKeyFrame(stateTime, true);

        batch.setColor(1f, 1f, 1f, Math.min(alpha, 1f));
        batch.draw(currentFrame, x, y, WIDTH, HEIGHT);
        batch.setColor(1f, 1f, 1f, 1f);
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, WIDTH, HEIGHT);
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public static Animation<TextureRegion> loadAnimation() {
        Array<TextureRegion> frames = new Array<>();

        Texture texture0 = new Texture(Gdx.files.internal("Tree/T_TreeMonster_0.png"));
        Texture texture1 = new Texture(Gdx.files.internal("Tree/T_TreeMonster_1.png"));
        Texture texture2 = new Texture(Gdx.files.internal("Tree/T_TreeMonster_2.png"));

        frames.add(new TextureRegion(texture0));
        frames.add(new TextureRegion(texture1));
        frames.add(new TextureRegion(texture2));

        return new Animation<>(FRAME_DURATION, frames, Animation.PlayMode.LOOP);
    }
}
