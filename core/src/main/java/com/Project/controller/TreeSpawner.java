package com.Project.controller;

import com.Project.model.Player;
import com.Project.model.Tree;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class TreeSpawner {
    private final Array<Tree> trees = new Array<>();
    private final Animation<TextureRegion> treeAnimation;
    private final Player player;

    private float spawnTimer = 0f;
    private static final float SPAWN_INTERVAL = 0.5f;
    private static final int TREES_PER_SPAWN = 2;
    private static final int MAX_TREES = 12;

    public TreeSpawner(Player player) {
        this.player = player;
        this.treeAnimation = Tree.loadAnimation();
    }

    private void spawnTreesAroundPlayer() {
        float px = player.getPosition().x;
        float py = player.getPosition().y;

        int treesToSpawn = TREES_PER_SPAWN;
        int attempts = 0;
        int maxAttempts = 50;

        while (treesToSpawn > 0 && trees.size < MAX_TREES && attempts < maxAttempts) {
            attempts++;

            float angle = (float) (Math.random() * Math.PI * 2);
            float distance = 300 + (float) Math.random() * 400;
            float x = px + (float) Math.cos(angle) * distance;
            float y = py + (float) Math.sin(angle) * distance;

            boolean tooClose = false;
            for (Tree t : trees) {
                float dx = t.getX() - x;
                float dy = t.getY() - y;
                float dist = (float) Math.sqrt(dx * dx + dy * dy);
                if (dist < 500) {
                    tooClose = true;
                    break;
                }
            }

            if (!tooClose) {
                trees.add(new Tree(x, y, treeAnimation));
                treesToSpawn--;
            }
        }
    }

    private void removeFarTrees() {
        float px = player.getPosition().x;
        float py = player.getPosition().y;

        for (int i = trees.size - 1; i >= 0; i--) {
            Tree tree = trees.get(i);
            float dx = tree.getX() - px;
            float dy = tree.getY() - py;
            float dist = (float) Math.sqrt(dx * dx + dy * dy);
            if (dist > 2500) {
                trees.removeIndex(i);
            }
        }
    }

    public void update() {
        spawnTimer += com.badlogic.gdx.Gdx.graphics.getDeltaTime();
        if (spawnTimer >= SPAWN_INTERVAL) {
            spawnTreesAroundPlayer();
            spawnTimer = 0f;
        }

        removeFarTrees();
    }

    public void render(SpriteBatch batch) {
        for (Tree tree : trees) {
            tree.render(batch);
        }
    }

    public Array<Tree> getTrees() {
        return trees;
    }
}
