package com.Project.controller;

import com.Project.Screen.GameOverScreen;
import com.Project.Screen.GameScreen;
import com.Project.model.*;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.math.Rectangle;

import java.util.HashMap;


public class GameController {
    private Player player;
    private OrthographicCamera camera;
    private EnemySpawner enemySpawner;
    private GameScreen gameScreen;
    private Game game;
    private HashMap<String, UserModel> registeredUsers;
    private UserModel currentUser;
    private int keyUp = Input.Keys.W;
    private int keyDown = Input.Keys.S;
    private int keyLeft = Input.Keys.A;
    private int keyRight = Input.Keys.D;
    private int keyReload = Input.Keys.R;
    private int keyAutoAim = Input.Keys.SPACE;
    private int mouseShootButton = Input.Buttons.LEFT;
    private StringBuilder cheatCodeBuffer = new StringBuilder();
    private final String CHEAT_MINUTE = "minute";
    private final float CHEAT_TIME_SKIP = 60f;
    private boolean autoAim = false;
    private final String CHEAT_LEVEL = "level";
    private float reloadTimer = 0f;
    private float shootCooldown = 0f;
    private final float shootInterval = 0.2f;
    private float remainingTime;

    private String reloadMessage = "";
    private String autoAimMessage = "Auto-Aim: OFF";

    public GameController(Game game, Player player, OrthographicCamera camera, GameScreen gameScreen, int totalGameDurationSeconds, HashMap<String, UserModel> registeredUsers, UserModel currentUser) {
        this.game = game;
        this.player = player;
        this.camera = camera;
        this.gameScreen = gameScreen;
        this.remainingTime = totalGameDurationSeconds;
        this.registeredUsers = registeredUsers;
        this.currentUser = currentUser;
        enemySpawner = new EnemySpawner(800, 600, totalGameDurationSeconds);
    }

    public void update(float delta) {
        remainingTime -= delta;

        if (gameScreen.getUser() != null) {
            gameScreen.getUser().updateSurviveTime(delta);
        }

        if (remainingTime <= 0) {
            String username = (gameScreen.getUser() != null) ? gameScreen.getUser().getUsername() : "Unknown";
            float surviveTime = (gameScreen.getUser() != null) ? gameScreen.getUser().getSurviveTimeSeconds() : 0f;
            int kills = (gameScreen.getUser() != null) ? gameScreen.getUser().getKills() : 0;

            game.setScreen(new GameOverScreen(game, username, (int) surviveTime, kills, true,registeredUsers, currentUser));
            return;
        }

        handleMovement(delta);
        handleReload(delta);
        enemySpawner.update(delta, player, gameScreen.getUser());
        player.update(delta);

        checkBulletCollisions();
        checkPlayerDamage(delta);

        if (player.getHp() <= 0) {
            String username = (gameScreen.getUser() != null) ? gameScreen.getUser().getUsername() : "Unknown";
            float surviveTime = (gameScreen.getUser() != null) ? gameScreen.getUser().getSurviveTimeSeconds() : 0f;
            int kills = (gameScreen.getUser() != null) ? gameScreen.getUser().getKills() : 0;

            game.setScreen(new GameOverScreen(game, username, (int) surviveTime, kills, false, registeredUsers, currentUser));
            return;
        }

        if (player.getAmmo() <= 0 && !player.isReloading()) {
            setReloadMessage("Out of ammo!");
        } else if (!player.isReloading()) {
            setReloadMessage("");
        }

        if (Gdx.input.isButtonPressed(mouseShootButton) && !player.isReloading()) {
            shootCooldown -= delta;
            if (shootCooldown <= 0) {
                if (player.getAmmo() > 0) {
                    Vector2 shootDir = getShootDirection();
                    player.shoot(shootDir);
                    shootCooldown = shootInterval;
                    setReloadMessage("");
                } else {
                    setReloadMessage("Out of ammo!");
                }
            }
        } else {
            shootCooldown = 0;
        }

        Array<Seed> seeds = enemySpawner.getDroppedSeeds();
        for (int i = seeds.size - 1; i >= 0; i--) {
            Seed seed = seeds.get(i);

            if (!seed.isCollected()) {
                Rectangle playerBounds = new Rectangle(
                    player.getPosition().x - 16,
                    player.getPosition().y - 16,
                    32, 32
                );

                if (seed.getBounds().overlaps(playerBounds)) {
                    seed.collect();
                    player.addXP(30);
                    seeds.removeIndex(i);
                }
            }
        }
    }

    private Vector2 getShootDirection() {
        if (autoAim) {
            Enemy nearestEnemy = findNearestEnemy();
            if (nearestEnemy != null) {
                return new Vector2(nearestEnemy.getPosition()).sub(player.getPosition()).nor();
            }
        }

        Vector3 mouseScreen = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
        Vector3 mouseWorld = camera.unproject(mouseScreen);

        Vector2 direction = new Vector2(mouseWorld.x, mouseWorld.y).sub(player.getPosition());

        if (direction.len() < 0.001f) {
            return new Vector2(1, 0);
        }
        return direction.nor();
    }

    private void handleMovement(float delta) {
        Vector2 dir = new Vector2();
        if (Gdx.input.isKeyPressed(keyUp)) dir.y += 1;
        if (Gdx.input.isKeyPressed(keyDown)) dir.y -= 1;
        if (Gdx.input.isKeyPressed(keyLeft)) dir.x -= 1;
        if (Gdx.input.isKeyPressed(keyRight)) dir.x += 1;
        if (dir.len() > 0) {
            dir.nor();
        }
        player.move(dir, delta);
    }

    private void handleReload(float delta) {
        if (player.isReloading()) {
            reloadTimer -= delta;
            setReloadMessage("Reloading...");
            if (reloadTimer <= 0) {
                player.setReloading(false);
                player.refillBullets();
                setReloadMessage("Reload complete");
            }
        }
    }

    public void onKeyDown(int keycode) {
        if (keycode == keyAutoAim) {
            autoAim = !autoAim;
            if (autoAim) {
                setAutoAimMessage("Auto-Aim ON");
            } else {
                setAutoAimMessage("Auto-Aim OFF");
            }
            System.out.println("Auto-Aim: " + (autoAim ? "ON" : "OFF"));
        } else if (keycode == keyReload) {
            if (!player.isReloading()) {
                player.setReloading(true);
                reloadTimer = player.getWeapon().getReloadTimeSeconds();
                setReloadMessage("Reloading...");
            }
        }

        if (keycode >= Input.Keys.A && keycode <= Input.Keys.Z) {
            // تبدیل keycode به حروف a-z
            char c = (char) ('a' + (keycode - Input.Keys.A));
            cheatCodeBuffer.append(c);

            if (cheatCodeBuffer.length() > 10) {
                cheatCodeBuffer.deleteCharAt(0);
            }

            String currentCheat = cheatCodeBuffer.toString();

            if (currentCheat.contains(CHEAT_MINUTE)) {
                remainingTime = Math.max(0, remainingTime - CHEAT_TIME_SKIP);
                Gdx.app.log("CHEAT", "زمان بازی 1 دقیقه جلو رفت.");
                cheatCodeBuffer.setLength(0);
            }

            if (currentCheat.contains(CHEAT_LEVEL)) {
                player.addXP(player.getXpToNextLevel());
                Gdx.app.log("CHEAT", "سطح کاراکتر 1 واحد افزایش یافت. سطح فعلی: " + player.getLevel());
                cheatCodeBuffer.setLength(0);
            }
        }
    }

    public void onMouseClick(int screenX, int screenY, int button) {
        if (button == mouseShootButton && !player.isReloading() && player.getAmmo() > 0) {
            Vector3 worldCoords = camera.unproject(new Vector3(screenX, screenY, 0));
            Vector2 shootDir;

            if (autoAim) {
                Enemy nearestEnemy = findNearestEnemy();
                if (nearestEnemy != null) {
                    shootDir = new Vector2(nearestEnemy.getPosition()).sub(player.getPosition()).nor();
                } else {
                    shootDir = new Vector2(worldCoords.x - player.getPosition().x, worldCoords.y - player.getPosition().y).nor();
                }
            } else {
                shootDir = new Vector2(worldCoords.x - player.getPosition().x, worldCoords.y - player.getPosition().y).nor();
            }

            player.shoot(shootDir);
            shootCooldown = shootInterval;
            setReloadMessage("");
        }
    }

    private void checkBulletCollisions() {
        Array<Bullet> bullets = player.getBullets();
        Array<Enemy> enemies = enemySpawner.getEnemies();

        int damage = player.getWeapon().getDamage();

        for (int i = bullets.size - 1; i >= 0; i--) {
            Bullet bullet = bullets.get(i);
            Vector2 bulletPos = bullet.getPosition();

            for (Enemy enemy : enemies) {
                if (enemy.isDead()) continue;
                if (enemy.getPosition().dst(bulletPos) < 12) {
                    enemy.takeDamage(damage);
                    bullets.removeIndex(i);
                    break;
                }
            }
        }
    }

    public Enemy findNearestEnemy() {
        Array<Enemy> enemies = enemySpawner.getEnemies();
        Enemy nearest = null;
        float minDist = Float.MAX_VALUE;
        Vector2 playerPos = player.getPosition();

        for (Enemy enemy : enemies) {
            if (enemy.isDead()) continue;
            float dist = enemy.getPosition().dst(playerPos);
            if (dist < minDist) {
                minDist = dist;
                nearest = enemy;
            }
        }
        return nearest;
    }

    private void checkPlayerDamage(float delta) {
        Vector2 playerPos = player.getPosition();

        for (Enemy enemy : enemySpawner.getEnemies()) {
            if (enemy.isDead()) continue;
            if (enemy.getPosition().dst(playerPos) < 30) {
                if (!player.isInvincible()) {
                    player.takeDamage(1,false);
                }
            }
        }

        Array<Bullet> enemyBullets = enemySpawner.getEnemyBullets();
        for (int i = enemyBullets.size - 1; i >= 0; i--) {
            Bullet bullet = enemyBullets.get(i);
            if (bullet.getPosition().dst(playerPos) < 20) {
                if (!player.isInvincible()) {
                    player.takeDamage(1,true);
                    gameScreen.showDamageEffect();
                    enemyBullets.removeIndex(i);
                }
            }
        }

        if (gameScreen.getTreeSpawner() != null) {
            Array<com.Project.model.Tree> trees = gameScreen.getTreeSpawner().getTrees();
            for (com.Project.model.Tree tree : trees) {
                if (tree.getX() - 30 < playerPos.x && playerPos.x < tree.getX() + 30
                    && tree.getY() - 30 < playerPos.y && playerPos.y < tree.getY() + 30) {
                    if (!player.isInvincible()) {
                        player.takeDamage(1,false);
                    }
                }
            }
        }
    }

    public void setControlsFromUser(UserModel user) {
        if (user != null) {
            setKeyUp(user.getKeyUp());
            setKeyDown(user.getKeyDown());
            setKeyLeft(user.getKeyLeft());
            setKeyRight(user.getKeyRight());
            setKeyReload(user.getKeyReload());
            setKeyAutoAim(user.getKeyAutoAim());
            setMouseShootButton(user.getMouseShootButton());
        } else {
            setKeyUp(Input.Keys.W);
            setKeyDown(Input.Keys.S);
            setKeyLeft(Input.Keys.A);
            setKeyRight(Input.Keys.D);
            setKeyReload(Input.Keys.R);
            setKeyAutoAim(Input.Keys.SPACE);
            setMouseShootButton(Input.Buttons.LEFT);
        }
    }

    public void setKeyUp(int key) { this.keyUp = key; }
    public void setKeyDown(int key) { this.keyDown = key; }
    public void setKeyLeft(int key) { this.keyLeft = key; }
    public void setKeyRight(int key) { this.keyRight = key; }
    public void setKeyReload(int key) { this.keyReload = key; }
    public void setKeyAutoAim(int key) { this.keyAutoAim = key; }
    public void setMouseShootButton(int button) { this.mouseShootButton = button; }

    public int getKeyUp() { return keyUp; }
    public int getKeyDown() { return keyDown; }
    public int getKeyLeft() { return keyLeft; }
    public int getKeyRight() { return keyRight; }
    public int getKeyReload() { return keyReload; }
    public int getKeyAutoAim() { return keyAutoAim; }
    public int getMouseShootButton() { return mouseShootButton; }

    public EnemySpawner getEnemySpawner() {
        return enemySpawner;
    }

    public boolean isAutoAim() {
        return autoAim;
    }


    public String getReloadMessage() {
        return reloadMessage;
    }

    public void setReloadMessage(String reloadMessage) {
        this.reloadMessage = reloadMessage;
        if (gameScreen != null) {
            gameScreen.setReloadMessage(reloadMessage);
        }
    }

    public String getAutoAimMessage() {
        return autoAimMessage;
    }

    public void setAutoAimMessage(String autoAimMessage) {
        this.autoAimMessage = autoAimMessage;
        if (gameScreen != null) {
            gameScreen.setAutoAimMessage(autoAimMessage);
        }
    }
    public float getRemainingTime() {
        return remainingTime;
    }

}
