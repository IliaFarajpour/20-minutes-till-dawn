package com.Project.view;

import com.Project.model.GameAssets;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class LoginView {
    public Stage stage;
    public TextField usernameField, passwordField;
    public Label errorLabel;
    public TextButton loginButton, forgotPasswordButton,backButton;

    private Skin skin;

    public LoginView() {
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        skin = GameAssets.getGameAssets().getSkin();

        Table table = new Table();
        table.setFillParent(true);
        table.center();
        stage.addActor(table);

        usernameField = new TextField("", skin);
        usernameField.setMessageText("Enter username");

        passwordField = new TextField("", skin);
        passwordField.setMessageText("Enter password");
        passwordField.setPasswordMode(true);
        passwordField.setPasswordCharacter('*');

        loginButton = new TextButton("Login", skin);
        forgotPasswordButton = new TextButton("Forgot Password", skin);
        backButton = new TextButton("Back", skin);
        errorLabel = new Label("", skin);
        errorLabel.setColor(Color.RED);
        errorLabel.setWrap(true);
        errorLabel.setAlignment(Align.center);

        table.defaults().pad(10).center();

        Label usernameLabel = new Label("Username:", skin);
        usernameLabel.setAlignment(Align.center);
        table.add(usernameLabel).width(300).center();
        table.row();
        table.add(usernameField).width(300);
        table.row();

        Label passwordLabel = new Label("Password:", skin);
        passwordLabel.setAlignment(Align.center);
        table.add(passwordLabel).width(400).center();
        table.row();
        table.add(passwordField).width(300);
        table.row();

        table.add(loginButton).width(250);
        table.row();
        table.add(forgotPasswordButton).width(500);
        table.row();
        table.add(backButton).width(250);
        table.row();
        table.add(errorLabel).width(400).center();
    }

    public void render(float delta) {
        stage.act(delta);
        stage.draw();
    }

    public void dispose() {
        stage.dispose();
    }
}
