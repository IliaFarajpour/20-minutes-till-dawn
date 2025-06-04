package com.Project.controller;

import com.Project.Screen.MainMenuScreen;
import com.Project.Screen.LoginScreen;
import com.Project.model.UserModel;
import com.Project.view.RegisterView;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

import java.util.HashMap;
import java.util.Random;

public class RegisterController {
    private RegisterView view;
    private Game game;

    private static HashMap<String, UserModel> registeredUsers = new HashMap<>();

    private static final String[] avatars = {
        "avatars/avatar1.png",
        "avatars/avatar2.png",
        "avatars/avatar3.png",
        "avatars/avatar4.png",
        "avatars/avatar5.png",
        "avatars/avatar6.png"
    };

    public RegisterController(Game game, RegisterView view, HashMap<String, UserModel> externalUsers) {
        this.game = game;
        this.view = view;

        if (externalUsers != null) {
            registeredUsers = externalUsers;
        }
        InputListener clearErrorListener = new InputListener() {
            @Override
            public boolean keyTyped(InputEvent event, char character) {
                if (!view.errorLabel.getText().toString().isEmpty()) {
                    view.errorLabel.setText("");
                }
                return true;
            }
        };
        view.usernameField.addListener(clearErrorListener);
        view.passwordField.addListener(clearErrorListener);
        view.answerField.addListener(clearErrorListener);

        view.securityQuestionList.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (!view.errorLabel.getText().toString().isEmpty()) {
                    view.errorLabel.setText("");
                }
            }
        });

        view.registerButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                view.errorLabel.setText("");
                view.errorLabel.setColor(Color.RED);

                String username = view.usernameField.getText().trim();
                String password = view.passwordField.getText().trim();
                String answer = view.answerField.getText().trim();
                String question = view.securityQuestionList.getSelected();

                if (username.isEmpty() || password.isEmpty() || answer.isEmpty() || question == null) {
                    view.errorLabel.setText("All fields are required!");
                    return;
                }

                if (registeredUsers.containsKey(username)) {
                    view.errorLabel.setText("Username already exists!");
                    return;
                }

                if (UserModel.isWeakPassword(password)) {
                    view.errorLabel.setText("Weak password! Use 8+ chars, upper, digit, special.");
                    return;
                }

                String avatar = getRandomAvatar();
                UserModel newUser = new UserModel(username, password, question, answer, avatar);
                registeredUsers.put(username, newUser);

                System.out.println("User registered: " + newUser.getUsername());
                System.out.println("Security Question: " + newUser.getSecurityQuestion());
                System.out.println("Answer: " + newUser.getSecurityAnswer());
                System.out.println("Assigned Avatar: " + newUser.getAvatar());

                view.errorLabel.setColor(Color.GREEN);
                view.errorLabel.setText("Registered successfully!");

                dispose();
                game.setScreen(new LoginScreen(game, registeredUsers));
            }
        });

        view.guestButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                System.out.println("Continue as guest");
                dispose();
                game.setScreen(new MainMenuScreen(game, null, registeredUsers));
            }
        });
    }

    private String getRandomAvatar() {
        Random random = new Random();
        return avatars[random.nextInt(avatars.length)];
    }

    public void render(float delta) {
        view.render(delta);
    }

    public void dispose() {
        view.dispose();
    }

    public static HashMap<String, UserModel> getRegisteredUsers() {
        return registeredUsers;
    }
}
