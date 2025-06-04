package com.Project.view;

import com.Project.model.GameAssets;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class ForgotPasswordView {
    public Stage stage;
    public TextField usernameField;
    public Label securityQuestionLabel;
    public TextField answerField;
    public TextField newPasswordField;
    public TextButton submitUsernameButton;
    public TextButton submitAnswerButton;
    public Label errorLabel;

    private Skin skin;

    public ForgotPasswordView() {
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        skin = GameAssets.getGameAssets().getSkin();

        Table table = new Table();
        table.setFillParent(true);
        table.center();
        stage.addActor(table);

        usernameField = new TextField("", skin);
        usernameField.setMessageText("Enter username");

        submitUsernameButton = new TextButton("Submit Username", skin);

        securityQuestionLabel = new Label("", skin);
        securityQuestionLabel.setColor(Color.WHITE);
        securityQuestionLabel.setWrap(true);
        securityQuestionLabel.setAlignment(Align.center);

        answerField = new TextField("", skin);
        answerField.setMessageText("Enter answer");

        newPasswordField = new TextField("", skin);
        newPasswordField.setMessageText("Enter new password");
        newPasswordField.setPasswordMode(true);
        newPasswordField.setPasswordCharacter('*');

        submitAnswerButton = new TextButton("Reset Password", skin);

        errorLabel = new Label("", skin);
        errorLabel.setColor(Color.RED);
        errorLabel.setWrap(true);
        errorLabel.setAlignment(Align.center);

        table.defaults().pad(10).center();

        Label usernameLabel = new Label("Username:", skin);
        table.add(usernameLabel).width(300);
        table.row();
        table.add(usernameField).width(300);
        table.row();
        table.add(submitUsernameButton).width(500);
        table.row();

        table.add(securityQuestionLabel).width(500).colspan(1).padTop(20);
        table.row();
        table.add(answerField).width(300);
        table.row();
        table.add(newPasswordField).width(300);
        table.row();
        table.add(submitAnswerButton).width(500);
        table.row();
        table.add(errorLabel).width(400);

        securityQuestionLabel.setVisible(false);
        answerField.setVisible(false);
        newPasswordField.setVisible(false);
        submitAnswerButton.setVisible(false);
    }

    public void render(float delta) {
        stage.act(delta);
        stage.draw();
    }

    public void dispose() {
        stage.dispose();
    }
}
