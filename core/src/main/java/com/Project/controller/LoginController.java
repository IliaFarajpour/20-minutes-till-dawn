package com.Project.controller;

import com.Project.Screen.ForgotPasswordScreen;
import com.Project.Screen.MainMenuScreen;
import com.Project.Screen.RegisterScreen;
import com.Project.model.UserModel;
import com.Project.view.LoginView;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

import java.util.HashMap;

public class LoginController {
    private LoginView view;
    private Game game;
    private HashMap<String, UserModel> registeredUsers;

    public LoginController(Game game, LoginView view, HashMap<String, UserModel> registeredUsers) {
        this.game = game;
        this.view = view;
        this.registeredUsers = registeredUsers;

        view.loginButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                view.errorLabel.setText("");
                String username = view.usernameField.getText().trim();
                String password = view.passwordField.getText().trim();

                if (username.isEmpty() || password.isEmpty()) {
                    view.errorLabel.setText("Please enter username and password.");
                    return;
                }

                if (!registeredUsers.containsKey(username)) {
                    view.errorLabel.setText("Username not found!");
                    return;
                }

                UserModel user = registeredUsers.get(username);
                if (!user.getPassword().equals(password)) {
                    view.errorLabel.setText("Incorrect password!");
                    return;
                }

                view.errorLabel.setColor(Color.GREEN);
                view.errorLabel.setText("Login successful!");


                game.setScreen(new MainMenuScreen(game, user, registeredUsers));
            }
        });

        view.forgotPasswordButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                System.out.println("Forgot password clicked");
                dispose();
                game.setScreen(new ForgotPasswordScreen(game, registeredUsers));
            }
        });
        view.backButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new RegisterScreen(game, registeredUsers));
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
