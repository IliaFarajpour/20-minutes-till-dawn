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

public class GameOverScreen implements Screen {
    private Game game;
    private Stage stage;
    private Skin skin;
    private UserModel user;
    private HashMap<String, UserModel> registeredUsers;
    private String username;
    private int surviveTimeSeconds;
    private int kills;
    private int score;
    private boolean won;

    public GameOverScreen(Game game, String username, int surviveTimeSeconds, int kills, boolean won,HashMap<String, UserModel> registeredUsers, UserModel currentUser) {
        this.game = game;
        this.username = username;
        this.surviveTimeSeconds = surviveTimeSeconds;
        this.kills = kills;
        this.score = surviveTimeSeconds * kills;
        this.won = won;
        this.registeredUsers = registeredUsers;
        this.user = currentUser;
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);
        skin = new Skin(Gdx.files.internal("skin/pixthulhu-ui.json"));
        Gdx.input.setCursorCatched(false);
        Gdx.graphics.setSystemCursor(Cursor.SystemCursor.Arrow);
        Table table = new Table();
        table.setFillParent(true);
        table.center();
        stage.addActor(table);

        Label titleLabel = new Label("Game Over", skin, "title");
        Label userLabel = new Label("Player: " + username, skin);
        Label timeLabel = new Label("Survived Time: " + surviveTimeSeconds + " seconds", skin);
        Label killsLabel = new Label("Kills: " + kills, skin);
        Label scoreLabel = new Label("Score: " + score, skin);
        Label resultLabel = new Label(won ? "You Win! 🎉" : "You Lost! 💀", skin);

        TextButton mainMenuBtn = new TextButton("Main Menu", skin);
        TextButton exitBtn = new TextButton("Exit Game", skin);

        table.add(titleLabel).padBottom(20);
        table.row();
        table.add(userLabel).padBottom(10);
        table.row();
        table.add(timeLabel).padBottom(10);
        table.row();
        table.add(killsLabel).padBottom(10);
        table.row();
        table.add(scoreLabel).padBottom(20);
        table.row();
        table.add(resultLabel).padBottom(30);
        table.row();
        table.add(mainMenuBtn).width(300).pad(10);
        table.row();
        table.add(exitBtn).width(300).pad(10);

        mainMenuBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (user != null) {
                    user.setKills(kills);
                    user.setSurviveTimeSeconds(surviveTimeSeconds);
                    user.setScore(score);
                    registeredUsers.put(user.getUsername(), user);
                }
                game.setScreen(new MainMenuScreen(game, user, registeredUsers));
            }
        });

        exitBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.app.exit();
            }
        });
    }

    @Override
    public void show() { }

    @Override
    public void render(float delta) {
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
