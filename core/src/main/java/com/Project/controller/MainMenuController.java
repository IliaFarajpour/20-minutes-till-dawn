package com.Project.controller;

import com.Project.Screen.*;
import com.Project.model.UserModel;
import com.Project.view.MainMenuView;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

import java.util.HashMap;

public class MainMenuController {
    private Game game;
    private MainMenuView view;
    private UserModel user;
    private HashMap<String, UserModel> registeredUsers;

    public MainMenuController(Game game, MainMenuView view, UserModel user, HashMap<String, UserModel> registeredUsers) {
        this.game = game;
        this.view = view;
        this.user = user;
        this.registeredUsers = registeredUsers;

        view.settingsButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                dispose();
                game.setScreen(new SettingsScreen(game, user,registeredUsers));

            }
        });

        view.profileButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                dispose();
                game.setScreen(new ProfileScreen(game, user,registeredUsers));

            }
        });

        view.preGameButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                dispose();
               game.setScreen(new PreGameMenuScreen(game, user,registeredUsers));

            }
        });

        view.leaderboardButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                dispose();
                game.setScreen(new ScoreboardScreen(game, user, registeredUsers));

            }
        });

        view.hintMenuButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                dispose();
                game.setScreen(new TalentHintScreen(game, user,registeredUsers));

            }
        });

        view.continueButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {

              //  game.setScreen(new SavedGameScreen(game, user));
                dispose();
            }
        });

        view.logoutButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                dispose();
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
