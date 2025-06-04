package com.Project.view;

import com.Project.model.UserModel;
import com.Project.model.GameAssets;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class MainMenuView {
    public Stage stage;
    public Label usernameLabel, scoreLabel;
    public Image avatarImage;
    public TextButton settingsButton, profileButton, preGameButton, leaderboardButton, hintMenuButton, continueButton, logoutButton;

    private Skin skin;

    public MainMenuView(UserModel user) {
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        skin = GameAssets.getGameAssets().getSkin();

        Table table = new Table();
        table.setFillParent(true);
        table.center();
        stage.addActor(table);

        usernameLabel = new Label("Username: " + (user != null ? user.getUsername() : "guest"), skin);
        usernameLabel.setColor(Color.WHITE);
        usernameLabel.setAlignment(Align.center);


        scoreLabel = new Label("Score: " + (user != null ? user.getScore() : 0), skin);
        scoreLabel.setColor(Color.WHITE);
        scoreLabel.setAlignment(Align.center);

        Texture avatarTexture;

        if (user != null && user.getAvatar() != null && !user.getAvatar().isEmpty()) {
            avatarTexture = new Texture(Gdx.files.internal(user.getAvatar()));
        } else {
            avatarTexture = new Texture(Gdx.files.internal("avatars/avatar1.png"));
        }

        avatarImage = new Image(avatarTexture);
        settingsButton = new TextButton("Settings", skin);
        profileButton = new TextButton("Profile", skin);
        preGameButton = new TextButton("Pre-Game Menu", skin);
        leaderboardButton = new TextButton("Leaderboard", skin);
        hintMenuButton = new TextButton("Talent Menu", skin);
        continueButton = new TextButton("Continue Saved Game", skin);
        logoutButton = new TextButton("Logout", skin);

        table.defaults().pad(10).center();

        table.add(avatarImage).size(100, 100);
        table.row();
        table.add(usernameLabel).width(300);
        table.row();
        table.add(scoreLabel).width(300);
        table.row();

        table.add(settingsButton).width(300);
        table.row();
        table.add(profileButton).width(300);
        table.row();
        table.add(preGameButton).width(450);
        table.row();
        table.add(leaderboardButton).width(400);
        table.row();
        table.add(hintMenuButton).width(350);
        table.row();
        table.add(continueButton).width(600);
        table.row();
        table.add(logoutButton).width(300);
    }

    public void render(float delta) {
        stage.act(delta);
        stage.draw();
    }

    public void dispose() {
        stage.dispose();
    }
}
