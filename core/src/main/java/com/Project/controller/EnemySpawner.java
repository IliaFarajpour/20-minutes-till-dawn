package com.Project.controller;

import com.Project.model.*;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

import java.util.Random;

public class EnemySpawner {
    private float spawnTimer = 0f;
    private float eyebatSpawnTimer = 0f;
    private float bossDashTimer = 0f;
    private Array<Bullet> enemyBullets = new Array<>();
    private Array<Enemy> enemies;
    private Array<Seed> droppedSeeds = new Array<>();
    private Random random;
    private int screenWidth, screenHeight;
    private float totalTime = 0f;
    private int t;  // کل زمان بازی بر حسب ثانیه

    private boolean bossSpawned = false;

    public EnemySpawner(int screenWidth, int screenHeight, int t) {
        enemies = new Array<>();
        random = new Random();
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        this.t = t;
    }

    public void update(float delta, Player player, UserModel user) {
        totalTime += delta;
        spawnTimer += delta;
        eyebatSpawnTimer += delta;

        int timeInt = (int) totalTime;
        int currentTentacles = countEnemiesOfType(EnemyType.TENTACLE_MONSTER);
        int maxTentaclesToSpawn = Math.max(1, timeInt / 10);

        if (spawnTimer >= 1f && currentTentacles < maxTentaclesToSpawn){
            spawnEnemy(EnemyType.TENTACLE_MONSTER, player);
            spawnTimer = 0f;
        }

        if (totalTime >= t / 4f) {
            int maxEyebatsToSpawn = 4 * timeInt - t + 30;
            maxEyebatsToSpawn = Math.max(1, maxEyebatsToSpawn);
            int currentEyebats = countEnemiesOfType(EnemyType.EYEBAT);
            if (currentEyebats < maxEyebatsToSpawn / 30 && eyebatSpawnTimer >= 10f) {
                eyebatSpawnTimer = 0f;
                spawnEnemy(EnemyType.EYEBAT, player);
            }
        }

        if (!bossSpawned && totalTime >= t / 2f) {
            spawnEnemy(EnemyType.BOSS, player);
            bossSpawned = true;
            bossDashTimer = 0f;
        }

        for (int idx = enemies.size - 1; idx >= 0; idx--) {
            Enemy enemy = enemies.get(idx);

            if (enemy.getType() == EnemyType.BOSS) {
                bossDashTimer += delta;
                if (bossDashTimer >= 5f) {
                    bossDashTimer = 0f;
                    enemy.performDash(player);
                }
            }

            enemy.update(delta, player, player.getBullets(), enemyBullets);

            if (enemy.isDead()) {
                droppedSeeds.add(new Seed(enemy.getPosition().cpy()));
                enemies.removeIndex(idx);

                if (user != null) {
                    user.addKill();
                }
            }
        }

        for (int j = enemyBullets.size - 1; j >= 0; j--) {
            Bullet b = enemyBullets.get(j);
            if (b == null || player == null || player.getPosition() == null) continue;

            b.update(delta, player.getPosition());

            Vector2 pos = b.getPosition();
            if (pos.x < -500 || pos.x > screenWidth + 1200 || pos.y < -500 || pos.y > screenHeight + 1200) {
                enemyBullets.removeIndex(j);
            }
        }
    }

    private int countEnemiesOfType(EnemyType type) {
        int count = 0;
        for (Enemy e : enemies) {
            if (e.getType() == type && !e.isDead()) {
                count++;
            }
        }
        return count;
    }

    private void spawnEnemy(EnemyType type, Player player) {
        Vector2 spawnPosition = getRandomSpawnPositionNearPlayer(player.getPosition(), 300, 600);
        Enemy enemy = new Enemy(spawnPosition, type);
        enemies.add(enemy);
    }

    private Vector2 getRandomSpawnPositionNearPlayer(Vector2 playerPos, float minDistance, float maxDistance) {
        double angle = random.nextDouble() * 2 * Math.PI;
        float distance = minDistance + random.nextFloat() * (maxDistance - minDistance);
        float x = playerPos.x + (float)(Math.cos(angle) * distance);
        float y = playerPos.y + (float)(Math.sin(angle) * distance);

        return new Vector2(x, y);  // بدون محدود کردن داخل صفحه
    }

    public Array<Enemy> getEnemies() {
        return enemies;
    }

    public Array<Bullet> getEnemyBullets() {
        return enemyBullets;
    }

    public Array<Seed> getDroppedSeeds() {
        return droppedSeeds;
    }
}
