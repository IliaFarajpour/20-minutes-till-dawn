package com.Project.model;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Seed {
    private Vector2 position;
    private Texture texture;
    private boolean collected = false;

    public Seed(Vector2 position) {
        this.position = position;
        this.texture = new Texture("seed.png");
    }

    public void update(float delta) {

    }

    public void collect() {
        collected = true;
    }

    public boolean isCollected() {
        return collected;
    }

    public Vector2 getPosition() {
        return position;
    }

    public Texture getTexture() {
        return texture;
    }

    public Rectangle getBounds() {
        return new Rectangle(
            position.x - texture.getWidth() / 2f,
            position.y - texture.getHeight() / 2f,
            texture.getWidth(),
            texture.getHeight()
        );
    }

    public void dispose() {
        texture.dispose();
    }
}
