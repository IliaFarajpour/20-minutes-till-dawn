package com.Project.controller;

import com.Project.Screen.LoginScreen;
import com.Project.model.UserModel;
import com.Project.view.ForgotPasswordView;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

import java.util.HashMap;

public class ForgotPasswordController {
    private ForgotPasswordView view;
    private Game game;
    private HashMap<String, UserModel> registeredUsers;

    private UserModel currentUser = null;

    public ForgotPasswordController(Game game, ForgotPasswordView view, HashMap<String, UserModel> registeredUsers) {
        this.game = game;
        this.view = view;
        this.registeredUsers = registeredUsers;

        view.submitUsernameButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                String username = view.usernameField.getText().trim();
                view.errorLabel.setText("");
                if (username.isEmpty()) {
                    view.errorLabel.setText("Please enter username.");
                    return;
                }
                if (!registeredUsers.containsKey(username)) {
                    view.errorLabel.setText("Username not found.");
                    return;
                }

                currentUser = registeredUsers.get(username);

                view.securityQuestionLabel.setText(currentUser.getSecurityQuestion());
                view.securityQuestionLabel.setVisible(true);
                view.answerField.setVisible(true);
                view.newPasswordField.setVisible(true);
                view.submitAnswerButton.setVisible(true);

                view.usernameField.setDisabled(true);
                view.submitUsernameButton.setDisabled(true);
            }
        });

        view.submitAnswerButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (currentUser == null) {
                    view.errorLabel.setText("Please submit a valid username first.");
                    return;
                }
                String answer = view.answerField.getText().trim();
                String newPassword = view.newPasswordField.getText().trim();

                view.errorLabel.setColor(Color.RED);
                view.errorLabel.setText("");

                if (answer.isEmpty() || newPassword.isEmpty()) {
                    view.errorLabel.setText("Answer and new password are required.");
                    return;
                }

                if (!currentUser.getSecurityAnswer().equalsIgnoreCase(answer)) {
                    view.errorLabel.setText("Incorrect answer to security question.");
                    return;
                }

                if (UserModel.isWeakPassword(newPassword)) {
                    view.errorLabel.setText("Weak password! Use 8+ chars, upper, digit, special.");
                    return;
                }

                currentUser.setPassword(newPassword);

                view.errorLabel.setColor(Color.GREEN);
                view.errorLabel.setText("Password reset successfully!");

                 game.setScreen(new LoginScreen(game, registeredUsers));
            }
        });
    }

    public void render(float delta) {
        view.render(delta);
    }

    public void dispose() {
        view.dispose();
    }
}
