package com.Project.Screen;

import com.Project.controller.GameController;
import com.Project.controller.TreeSpawner;
import com.Project.model.*;
import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import java.util.HashMap;

public class GameScreen implements Screen, InputProcessor {
    private UserModel user;
    private HeroInfo hero;
    private Weapon weapon;
    private int durationMinutes;
    private float remainingTime;
    private HashMap<String, UserModel> registeredUsers;
    private UserModel currentUser;
    private OrthographicCamera camera;
    private SpriteBatch batch;
    private Player player;
    private GameController controller;
    private TreeSpawner treeSpawner;
    private Game game;

    private Stage stage;
    private Skin skin;

    private Texture glowTexture;
    private Texture cursorTexture;

    private Texture damageTexture;
    private boolean showDamageEffect = false;
    private float damageEffectTimer = 0f;
    private static final float DAMAGE_EFFECT_DURATION = 0.5f;

    private float stateTime = 0f;
    private ShaderProgram grayscaleShader;
    private Label hpLabel, xpLabel, levelLabel, killsLabel, ammoLabel, timeLabel, reloadLabel, autoAimLabel;
    private ProgressBar xpProgressBar;

    public GameScreen(Game game, UserModel user, HeroInfo hero, Weapon weapon, int durationMinutes,HashMap<String,UserModel> registeredUsers,UserModel currentUser) {
        this.game = game;
        this.user = user;
        this.hero = hero;
        this.weapon = weapon;
        this.durationMinutes = durationMinutes / 60;
        this.remainingTime = durationMinutes;
        this.registeredUsers = registeredUsers;
        this.currentUser = currentUser;

        player = new Player(weapon, hero);
        player.setLevelUpListener(() -> {
            Gdx.app.postRunnable(() -> {
                game.setScreen(new AbilitySelectionScreen(game, player, this));
            });
        });

        treeSpawner = new TreeSpawner(player);

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 600);
        batch = new SpriteBatch();

        controller = new GameController(game, player, camera, this, (int) remainingTime,registeredUsers,currentUser);
        controller.setControlsFromUser(user);

        stage = new Stage(new ScreenViewport());
        skin = new Skin(Gdx.files.internal("skin/pixthulhu-ui.json"));

        Gdx.input.setInputProcessor(new InputMultiplexer(stage, this));

        hpLabel = new Label("HP: 100", skin);
        xpLabel = new Label("XP: 0", skin);
        levelLabel = new Label("Level: 1", skin);
        killsLabel = new Label("Kills: 0", skin);
        ammoLabel = new Label("Ammo: 0", skin);
        timeLabel = new Label("Time: 00:00", skin);
        reloadLabel = new Label("", skin);
        autoAimLabel = new Label("Auto-Aim: OFF", skin);

        hpLabel.setPosition(20, 1150);
        xpLabel.setPosition(20, 1120);
        levelLabel.setPosition(20, 1090);
        killsLabel.setPosition(20, 1060);
        ammoLabel.setPosition(20, 1030);
        timeLabel.setPosition(1200, 1100);
        reloadLabel.setPosition(400, 1120);
        autoAimLabel.setPosition(1200, 1070);

        for (Label lbl : new Label[]{hpLabel, xpLabel, levelLabel, killsLabel, ammoLabel, timeLabel, reloadLabel, autoAimLabel}) {
            lbl.setFontScale(1.2f);
            stage.addActor(lbl);
        }

        ProgressBar.ProgressBarStyle barStyle = new ProgressBar.ProgressBarStyle();
        barStyle.background = skin.newDrawable("white", Color.DARK_GRAY);
        barStyle.knob = skin.newDrawable("white", Color.GREEN);
        barStyle.knobBefore = skin.newDrawable("white", Color.GREEN);

        xpProgressBar = new ProgressBar(0, 100, 1, false, barStyle);
        xpProgressBar.setBounds(20, 1000, 400, 20);
        stage.addActor(xpProgressBar);

        glowTexture = new Texture(Gdx.files.internal("glow.png"));
        cursorTexture = new Texture(Gdx.files.internal("cursor.png"));
        damageTexture = new Texture(Gdx.files.internal("damage.png"));  // لود تصویر ضربه

        Gdx.graphics.setSystemCursor(Cursor.SystemCursor.None);
    }

    public void setReloadMessage(String msg) {
        reloadLabel.setText(msg);
    }

    public void setAutoAimMessage(String msg) {
        if (autoAimLabel != null) {
            autoAimLabel.setText(msg);
        }
    }

    public TreeSpawner getTreeSpawner() {
        return treeSpawner;
    }

    /** فعال کردن نمایش انیمیشن ضربه */
    public void showDamageEffect() {
        showDamageEffect = true;
        damageEffectTimer = DAMAGE_EFFECT_DURATION;
    }

    @Override
    public void render(float delta) {
        stateTime += delta;
       // remainingTime -= delta;

        if (showDamageEffect) {
            damageEffectTimer -= delta;
            if (damageEffectTimer <= 0f) {
                showDamageEffect = false;
            }
        }

        controller.update(delta);
        treeSpawner.update();

        hpLabel.setText("HP: " + player.getHp());
        xpLabel.setText("XP: " + player.getXp());
        levelLabel.setText("Level: " + player.getLevel());
        killsLabel.setText("Kills: " + (user != null ? user.getKills() : 0));
        ammoLabel.setText("Ammo: " + player.getAmmo());
        int minutes = (int) (controller.getRemainingTime() / 60);
        int seconds = (int) (controller.getRemainingTime() % 60);
        if (remainingTime < 0) {
            minutes = 0;
            seconds = 0;
        }
        timeLabel.setText(String.format("Time: %02d:%02d", minutes, seconds));

        xpProgressBar.setRange(0, player.getXpToNextLevel());
        xpProgressBar.setValue(player.getXp());

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        Vector2 playerPos = player.getPosition();
        camera.position.set(playerPos.x, playerPos.y, 0);
        camera.update();
        batch.setShader(user != null && user.isGrayscale() ? grayscaleShader : null);
        batch.setProjectionMatrix(camera.combined);
        batch.begin();

        batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE);
        float glowWidth = glowTexture.getWidth() * 3f;
        float glowHeight = glowTexture.getHeight() * 3f;
        batch.draw(glowTexture, playerPos.x - glowWidth / 2f, playerPos.y - glowHeight / 2f, glowWidth, glowHeight);
        batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        Animation<TextureRegion> animationToDraw = player.getVelocity().len() > 0 ? hero.getWalkAnimation() : hero.getIdleAnimation();
        TextureRegion currentFrame = animationToDraw.getKeyFrame(stateTime, true);
        float frameWidth = currentFrame.getRegionWidth() * 3f;
        float frameHeight = currentFrame.getRegionHeight() * 3f;
        batch.draw(currentFrame, playerPos.x - frameWidth / 2f, playerPos.y - frameHeight / 2f, frameWidth, frameHeight);

        if (showDamageEffect) {
            float damageWidth = damageTexture.getWidth() * 2f;
            float damageHeight = damageTexture.getHeight() * 2f;
            batch.draw(damageTexture, playerPos.x - damageWidth / 2f, playerPos.y - damageHeight / 2f, damageWidth, damageHeight);
        }

        for (Enemy enemy : controller.getEnemySpawner().getEnemies()) {
            enemy.render(batch);
        }

        for (Bullet bullet : player.getBullets()) {
            Texture tex = bullet.getTexture();
            Vector2 pos = bullet.getPosition();
            float width = tex.getWidth();
            float height = tex.getHeight();
            batch.draw(tex, pos.x - width / 2f, pos.y - height / 2f, width, height);
        }

        for (Bullet bullet : controller.getEnemySpawner().getEnemyBullets()) {
            Texture tex = bullet.getTexture();
            Vector2 pos = bullet.getPosition();
            float width = tex.getWidth();
            float height = tex.getHeight();
            batch.draw(tex, pos.x - width / 2f, pos.y - height / 2f, width, height);
        }

        for (Seed seed : controller.getEnemySpawner().getDroppedSeeds()) {
            Texture tex = seed.getTexture();
            Vector2 pos = seed.getPosition();
            float width = tex.getWidth() * 1.5f;
            float height = tex.getHeight() * 1.5f;
            batch.draw(tex, pos.x - width / 2f, pos.y - height / 2f, width, height);
        }

        treeSpawner.render(batch);

        if (controller.isAutoAim()) {
            Enemy nearestEnemy = controller.findNearestEnemy();
            if (nearestEnemy != null) {
                Vector2 enemyPos = nearestEnemy.getPosition();
                batch.draw(cursorTexture, enemyPos.x - cursorTexture.getWidth() / 2f, enemyPos.y - cursorTexture.getHeight() / 2f);
            } else {
                Vector3 mouseWorld = camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
                batch.draw(cursorTexture, mouseWorld.x - cursorTexture.getWidth() / 2f, mouseWorld.y - cursorTexture.getHeight() / 2f);
            }
        } else {
            Vector3 mouseWorld = camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
            batch.draw(cursorTexture, mouseWorld.x - cursorTexture.getWidth() / 2f, mouseWorld.y - cursorTexture.getHeight() / 2f);
        }

        batch.end();

        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
        camera.viewportWidth = width;
        camera.viewportHeight = height;
        camera.update();
    }

    @Override
    public void dispose() {
        batch.dispose();
        glowTexture.dispose();
        cursorTexture.dispose();
        damageTexture.dispose();
        stage.dispose();
        hero.dispose();
        for (Enemy enemy : controller.getEnemySpawner().getEnemies()) {
            enemy.dispose();
        }
    }

    @Override
    public void show() {
        Gdx.graphics.setSystemCursor(Cursor.SystemCursor.None);
        InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(stage);
        multiplexer.addProcessor(this);
        Gdx.input.setInputProcessor(multiplexer);
        ShaderProgram.pedantic = false;
        grayscaleShader = new ShaderProgram(
            Gdx.files.internal("shaders/grayscale.vert"),
            Gdx.files.internal("shaders/grayscale.frag")
        );
        if (!grayscaleShader.isCompiled()) {
            System.out.println("Shader compile error: " + grayscaleShader.getLog());
        }
    }

    @Override public boolean keyDown(int keycode) {
        if (keycode == Input.Keys.P) {
            game.setScreen(new PauseMenuScreen(game, this,registeredUsers,user));
            return true;
        }
        controller.onKeyDown(keycode);
        return true;
    }

    @Override public boolean keyUp(int keycode) { return false; }
    @Override public boolean keyTyped(char character) { return false; }
    @Override public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        controller.onMouseClick(screenX, screenY, button);
        return true;
    }
    @Override public boolean touchUp(int screenX, int screenY, int pointer, int button) { return false; }
    @Override public boolean touchDragged(int screenX, int screenY, int pointer) { return false; }
    @Override public boolean mouseMoved(int screenX, int screenY) { return false; }
    @Override public boolean scrolled(float amountX, float amountY) { return false; }
    @Override public boolean touchCancelled(int screenX, int screenY, int pointer, int button) { return false; }

    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}

    public UserModel getUser() {
        return user;
    }
}
