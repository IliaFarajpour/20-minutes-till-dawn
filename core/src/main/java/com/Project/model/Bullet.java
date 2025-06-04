package com.Project.model;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Rectangle;

public class Bullet {
    private Vector2 position;
    private Vector2 velocity;
    private float speed = 300f;
    private Texture texture;

    private boolean followsTarget = false;

    public Bullet(Vector2 startPosition, Vector2 direction, String texturePath) {
        this.position = startPosition.cpy();
        this.texture = new Texture(texturePath);
        this.followsTarget = false;
        this.velocity = direction.cpy().nor().scl(speed);
    }

    public static Bullet createHomingBullet(Vector2 startPosition, Vector2 targetPosition, String texturePath) {
        Bullet b = new Bullet();
        b.position = startPosition.cpy();
        b.texture = new Texture(texturePath);
        b.followsTarget =false;
        b.updateVelocity(targetPosition);
        return b;
    }

    private Bullet() {}

    private void updateVelocity(Vector2 targetPosition) {
        Vector2 direction = targetPosition.cpy().sub(position).nor();
        velocity = direction.scl(speed);
    }

    public void update(float delta) {
        position.mulAdd(velocity, delta);
    }

    public void update(float delta, Vector2 currentTargetPosition) {
        if (followsTarget) {
            updateVelocity(currentTargetPosition);
        }
        position.mulAdd(velocity, delta);
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
        if (texture != null) {
            texture.dispose();
            texture = null;
        }
    }
}
