package com.Project.model;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import java.util.ArrayList;

public class Enemy {
    protected Vector2 position;
    protected int health;
    protected float radius = 12f;
    protected EnemyType type;

    private float speed = 130f;
    private float shootCooldown = 3f;
    private float shootTimer = 0f;

    private ArrayList<Texture> frames;
    private Animation<TextureRegion> animation;
    private float animationTimer = 0f;

    private Texture deathTexture;
    private float deathAlpha = 1f;
    private final float deathAnimationDuration = 0.5f;
    private float deathTimer = 0f;
    private boolean isDying = false;

    // برای دشمن BOSS
    private float dashTimer = 0f;
    private static final float DASH_INTERVAL = 5f; // هر ۵ ثانیه دَش می‌کند
    private boolean isDashing = false;

    public Enemy(Vector2 pos, EnemyType type) {
        this.position = pos;
        this.type = type;

        frames = new ArrayList<>();

        switch (type) {
            case TENTACLE_MONSTER:
                health = 25;
                speed = 100f;
                radius = 12f;
                loadFrames("tentacle_monster/frame_", 4);
                break;
            case EYEBAT:
                health = 50;
                speed = 150f;
                radius = 14f;
                loadFrames("eyebat/frame_", 2);
                break;
            case BOSS:
                health = 400;
                speed = 80f;
                radius = 30f;
                loadFrames("boss/frame_", 4);
                break;
        }

        createAnimation();

        deathTexture = new Texture("enemy_death.png");
    }

    private void loadFrames(String basePath, int frameCount) {
        for (int i = 1; i <= frameCount; i++) {
            frames.add(new Texture(basePath + i + ".png"));
        }
    }

    private void createAnimation() {
        TextureRegion[] regions = new TextureRegion[frames.size()];
        for (int i = 0; i < frames.size(); i++) {
            regions[i] = new TextureRegion(frames.get(i));
        }
        animation = new Animation<>(0.2f, regions);
    }
    public void performDash(Player player) {
        // دَش سریع به سمت بازیکن
        Vector2 dashDirection = new Vector2(player.getPosition()).sub(position).nor();
        float dashDistance = 200f;  // میزان فاصله دَش (می‌تونی تنظیم کنی)
        position.mulAdd(dashDirection, dashDistance);
    }
    public void update(float delta, Player player, Array<Bullet> playerBullets, Array<Bullet> enemyBullets) {
        if (isDying) {
            deathTimer += delta;
            deathAlpha = 1f - (deathTimer / deathAnimationDuration);
            if (deathTimer >= deathAnimationDuration) {
                health = 0;
            }
            return;
        }

        animationTimer += delta;

        // حرکت دشمن‌ها
        if (type == EnemyType.BOSS) {
            dashTimer += delta;
            if (dashTimer >= DASH_INTERVAL) {
                dashTimer = 0f;
                isDashing = true;
            }

            float moveSpeed = isDashing ? 600f : speed;
            Vector2 direction = new Vector2(player.getPosition()).sub(position).nor();
            position.mulAdd(direction, moveSpeed * delta);

            if (isDashing) {
                isDashing = false; // دَش فقط یک بار انجام می‌شود
            }
        } else {
            Vector2 direction = new Vector2(player.getPosition()).sub(position).nor();
            position.mulAdd(direction, speed * delta);
        }

        // تیراندازی فقط برای EYEBAT
        if (type == EnemyType.EYEBAT) {
            shootTimer += delta;
            if (shootTimer >= shootCooldown) {
                shootTimer = 0f;
                shootAtPlayer(player, enemyBullets);
            }
        }

        // برخورد گلوله‌های بازیکن
        for (int i = playerBullets.size - 1; i >= 0; i--) {
            Bullet bullet = playerBullets.get(i);
            if (getBounds().overlaps(bullet.getBounds())) {
                takeDamage(10);
                playerBullets.removeIndex(i);
            }
        }
    }

    private void shootAtPlayer(Player player, Array<Bullet> enemyBullets) {
        Vector2 bulletStartPos = new Vector2(position);
        Bullet bullet = Bullet.createHomingBullet(bulletStartPos, player.getPosition(), "eyebat_bullet.png");
        enemyBullets.add(bullet);
    }

    public void takeDamage(int amount) {
        if (isDying) return;
        health -= amount;
        if (health <= 0 && !isDying) {
            startDeathAnimation();
        }
    }

    private void startDeathAnimation() {
        isDying = true;
        deathTimer = 0f;
        deathAlpha = 1f;
    }

    public boolean isDead() {
        return health <= 0 && deathTimer >= deathAnimationDuration;
    }

    public Vector2 getPosition() {
        return position;
    }

    public float getRadius() {
        return radius;
    }

    public Rectangle getBounds() {
        return new Rectangle(position.x - radius, position.y - radius, radius * 2, radius * 2);
    }

    public Animation<TextureRegion> getAnimation() {
        return animation;
    }

    public float getAnimationTimer() {
        return animationTimer;
    }

    public void render(SpriteBatch batch) {
        if (isDying) {
            Color prevColor = batch.getColor();
            batch.setColor(1f, 1f, 1f, deathAlpha);
            float scale = 1.75f;
            float width = deathTexture.getWidth() * scale;
            float height = deathTexture.getHeight() * scale;
            batch.draw(deathTexture, position.x - width / 2f, position.y - height / 2f, width, height);
            batch.setColor(prevColor);
        } else {
            TextureRegion frame = animation.getKeyFrame(animationTimer, true);
            float scale = (type == EnemyType.BOSS) ? 4.5f : 1.75f;
            float width = frame.getRegionWidth() * scale;
            float height = frame.getRegionHeight() * scale;
            batch.draw(frame, position.x - width / 2f, position.y - height / 2f, width, height);
        }
    }

    public void dispose() {
        for (Texture t : frames) {
            t.dispose();
        }
        frames.clear();

        if (deathTexture != null) {
            deathTexture.dispose();
            deathTexture = null;
        }
    }

    public EnemyType getType() {
        return type;
    }
}
