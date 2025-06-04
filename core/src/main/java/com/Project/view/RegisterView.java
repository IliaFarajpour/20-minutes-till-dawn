package com.Project.view;

import com.Project.model.GameAssets;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class RegisterView {
    public Stage stage;
    public TextField usernameField, passwordField, answerField;
    public List<String> securityQuestionList;
    public Label errorLabel;
    public TextButton registerButton, guestButton;

    private Skin skin;

    public RegisterView() {
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
        passwordField.setWidth(400);

        securityQuestionList = new List<>(skin);
        securityQuestionList.setItems(
            "Name of your first pet?",
            "Your favorite color?",
            "What city were you born in?"
        );
        ScrollPane questionScrollPane = new ScrollPane(securityQuestionList, skin);
        questionScrollPane.setFadeScrollBars(false);
        questionScrollPane.setScrollingDisabled(true, false);

        answerField = new TextField("", skin);
        answerField.setMessageText("Enter answer");

        registerButton = new TextButton("Register", skin);
        guestButton = new TextButton("Skip / Guest", skin);

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
        table.add(passwordField).width(400);
        table.row();

        Label questionLabel = new Label("Select Security Question:", skin);
        questionLabel.setAlignment(Align.center);
        table.add(questionLabel).width(800).center();
        table.row();
        table.add(questionScrollPane).width(800).height(200);
        table.row();

        Label answerLabel = new Label("Answer:", skin);
        answerLabel.setAlignment(Align.center);
        table.add(answerLabel).width(300).center();
        table.row();
        table.add(answerField).width(300);
        table.row();

        table.add(registerButton).width(250);
        table.row();
        table.add(guestButton).width(400);
        table.row();
        table.add(errorLabel).width(500).center();
    }

    public void render(float delta) {
        stage.act(delta);
        stage.draw();
    }

    public void dispose() {
        stage.dispose();
    }


}
