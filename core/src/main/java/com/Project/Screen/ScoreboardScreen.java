package com.Project.Screen;

import com.Project.model.UserModel;
import com.Project.model.GameAssets;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import java.util.*;
import java.util.List;

public class ScoreboardScreen implements Screen {
    private Game game;
    private UserModel currentUser;
    private HashMap<String, UserModel> registeredUsers;
    private Stage stage;
    private Skin skin;

    private Table mainTable;
    private Table buttonTable;
    private Table scoreboardTable;
    private String currentSort = "score";

    public ScoreboardScreen(Game game, UserModel currentUser, Map<String, UserModel> registeredUsers) {
        this.game = game;
        this.currentUser = currentUser;
        this.registeredUsers = new HashMap<>(registeredUsers); // تبدیل به HashMap

        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);
        skin = GameAssets.getGameAssets().getSkin();

        mainTable = new Table();
        mainTable.setFillParent(true);
        stage.addActor(mainTable);

        buttonTable = new Table();
        scoreboardTable = new Table();

        createSortButtons();
        renderTable("score");

        mainTable.top().padTop(20);
        mainTable.add(buttonTable).center().row();
        mainTable.add(scoreboardTable).padTop(30).expand().top().row();

        addBackButton();
    }

    private void createSortButtons() {
        buttonTable.clear();
        String[] sorts = {"score", "username", "kills", "surviveTime"};

        for (String sort : sorts) {
            TextButton btn = new TextButton("Sort by " + sort, skin);
            btn.setTransform(true);
            btn.setScale(0.8f);
            btn.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    currentSort = sort;
                    renderTable(currentSort);
                }
            });
            buttonTable.add(btn).pad(5);
        }
    }

    private void renderTable(String sortKey) {
        scoreboardTable.clear();

        List<UserModel> sortedUsers = new ArrayList<>(registeredUsers.values());

        switch (sortKey) {
            case "username":
                sortedUsers.sort(Comparator.comparing(UserModel::getUsername));
                break;
            case "kills":
                sortedUsers.sort(Comparator.comparing(UserModel::getKills).reversed());
                break;
            case "surviveTime":
                sortedUsers.sort(Comparator.comparing(UserModel::getSurviveTimeSeconds).reversed());
                break;
            case "score":
            default:
                sortedUsers.sort(Comparator.comparing(UserModel::getScore).reversed());
                break;
        }

        Label rankHeader = new Label("Rank", skin);
        Label usernameHeader = new Label("Username", skin);
        Label killsHeader = new Label("Kills", skin);
        Label timeHeader = new Label("Survive Time", skin);
        Label scoreHeader = new Label("Score", skin);

        rankHeader.setAlignment(Align.center);
        usernameHeader.setAlignment(Align.center);
        killsHeader.setAlignment(Align.center);
        timeHeader.setAlignment(Align.center);
        scoreHeader.setAlignment(Align.center);

        scoreboardTable.add(rankHeader).pad(20).expandX();
        scoreboardTable.add(usernameHeader).pad(20).expandX();
        scoreboardTable.add(killsHeader).pad(20).expandX();
        scoreboardTable.add(timeHeader).pad(20).expandX();
        scoreboardTable.add(scoreHeader).pad(20).expandX();
        scoreboardTable.row();

        for (int i = 0; i < Math.min(10, sortedUsers.size()); i++) {
            UserModel u = sortedUsers.get(i);
            Color rowColor = Color.WHITE;

            if (i == 0) rowColor = Color.GOLD;
            else if (i == 1) rowColor = new Color(0.75f, 0.75f, 0.75f, 1f); // Silver
            else if (i == 2) rowColor = new Color(0.8f, 0.5f, 0.2f, 1f); // Bronze

            if (u.getUsername().equals(currentUser.getUsername())) {
                rowColor = Color.MAGENTA;
            }

            Label rankLabel = new Label((i + 1) + "", skin);
            Label userLabel = new Label(u.getUsername(), skin);
            Label killsLabel = new Label(u.getKills() + "", skin);
            Label timeLabel = new Label((int) u.getSurviveTimeSeconds() + "s", skin);
            Label scoreLabel = new Label(u.getScore() + "", skin);

            rankLabel.setColor(rowColor);
            userLabel.setColor(rowColor);
            killsLabel.setColor(rowColor);
            timeLabel.setColor(rowColor);
            scoreLabel.setColor(rowColor);

            scoreboardTable.add(rankLabel).pad(5);
            scoreboardTable.add(userLabel).pad(5);
            scoreboardTable.add(killsLabel).pad(5);
            scoreboardTable.add(timeLabel).pad(5);
            scoreboardTable.add(scoreLabel).pad(5);
            scoreboardTable.row();
        }
    }

    private void addBackButton() {
        TextButton backButton = new TextButton("Back", skin);
        backButton.setTransform(true);
        backButton.setScale(0.8f);
        backButton.setPosition(
            Gdx.graphics.getWidth() / 2f - backButton.getWidth() * backButton.getScaleX() / 2f,
            15
        );
        backButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new MainMenuScreen(game, currentUser, registeredUsers));
                dispose();
            }
        });
        stage.addActor(backButton);
    }

    @Override public void show() {}
    @Override public void render(float delta) {
        stage.act(delta);
        stage.draw();
    }
    @Override public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}
    @Override public void dispose() {
        stage.dispose();
    }
}
