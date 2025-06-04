package com.Project.Screen;

import com.Project.controller.GameController;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.Project.controller.SettingsController;
import com.Project.view.SettingsView;
import com.Project.model.UserModel;
import java.util.HashMap;

public class SettingsScreen implements Screen {
    private SettingsView view;
    private SettingsController controller;
    private Game game;
    private GameController gameController;
    public SettingsScreen(Game game, UserModel user, HashMap<String, UserModel> registeredUsers) {
        this.game = game;
        view = new SettingsView(user);
        controller = new SettingsController(game, view, user, registeredUsers,gameController);
    }

    @Override
    public void show() {
    }

    @Override
    public void render(float delta) {
        controller.render(delta);
    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
    }

    @Override
    public void dispose() {
        controller.dispose();
    }
}
