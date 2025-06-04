package com.Project.Screen;

import com.Project.model.UserModel;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import java.util.HashMap;

public class PauseMenuScreen implements Screen {
    private Game game;
    private Screen gameScreen;
    private Stage stage;
    private Skin skin;
    private GameScreen gameScreenCast;
    private HashMap<String, UserModel> registeredUsers;
    private UserModel currentUser;

    public PauseMenuScreen(Game game, Screen gameScreen, HashMap<String, UserModel> registeredUsers, UserModel currentUser) {
        this.game = game;
        this.gameScreen = gameScreen;
        this.registeredUsers = registeredUsers;
        this.currentUser = currentUser;

        if (gameScreen instanceof GameScreen) {
            this.gameScreenCast = (GameScreen) gameScreen;
        }

        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);
        Gdx.input.setCursorCatched(false);
        Gdx.graphics.setSystemCursor(Cursor.SystemCursor.Arrow);

        skin = new Skin(Gdx.files.internal("skin/pixthulhu-ui.json"));

        Table table = new Table();
        table.setFillParent(true);
        table.center();
        stage.addActor(table);

        Label titleLabel = new Label("Game Paused", skin, "title");
        TextButton resumeBtn = new TextButton("Resume", skin);
        TextButton giveUpBtn = new TextButton("Give Up", skin);

        CheckBox grayscaleCheckbox = new CheckBox("Grayscale Mode", skin);
        grayscaleCheckbox.setChecked(currentUser.isGrayscale());

        Label cheatInfoLabel = new Label("Cheat code 'minute' will skip 1 minute of game time.", skin);
        Label cheatInfoLabel2 = new Label("Cheat code 'level' will instantly level up and open ability selection.", skin);

        table.add(titleLabel).padBottom(30);
        table.row();
        table.add(resumeBtn).width(300).pad(10);
        table.row();
        table.add(giveUpBtn).width(300).pad(10);
        table.row();
        table.add(grayscaleCheckbox).padTop(20);
        table.row();
        table.add(cheatInfoLabel).padTop(10);
        table.row();
        table.add(cheatInfoLabel).padTop(20);
        table.row();
        table.add(cheatInfoLabel2).padBottom(10);
        table.row();


        resumeBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                currentUser.setGrayscale(grayscaleCheckbox.isChecked());
                game.setScreen(gameScreen);
                Gdx.input.setInputProcessor(gameScreenCast);
                Gdx.input.setCursorCatched(true);
            }
        });

        giveUpBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                currentUser.setGrayscale(grayscaleCheckbox.isChecked());
                if (gameScreenCast != null && gameScreenCast.getUser() != null) {
                    String username = gameScreenCast.getUser().getUsername();
                    float surviveTime = gameScreenCast.getUser().getSurviveTimeSeconds();
                    int kills = gameScreenCast.getUser().getKills();
                    boolean won = false;
                    game.setScreen(new GameOverScreen(game, username, (int) surviveTime, kills, won, registeredUsers, currentUser));
                } else {
                    game.setScreen(new GameOverScreen(game, "Unknown", 0, 0, false, registeredUsers, currentUser));
                }
            }
        });

        // 🎛 رویداد تغییر وضعیت چک‌باکس
        grayscaleCheckbox.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                currentUser.setGrayscale(grayscaleCheckbox.isChecked());
                System.out.println("Grayscale mode from pause: " + grayscaleCheckbox.isChecked());
            }
        });
    }

    @Override public void show() { }

    @Override public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(delta);
        stage.draw();
    }

    @Override public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override public void pause() { }
    @Override public void resume() { }
    @Override public void hide() { }

    @Override public void dispose() {
        stage.dispose();
        skin.dispose();
    }
}
