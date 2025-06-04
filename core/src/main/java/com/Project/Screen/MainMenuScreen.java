package com.Project.Screen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.Project.controller.MainMenuController;
import com.Project.view.MainMenuView;
import com.Project.model.UserModel;

import java.util.HashMap;

public class MainMenuScreen implements Screen {
    private MainMenuView view;
    private MainMenuController controller;

    public MainMenuScreen(Game game, UserModel user, HashMap<String, UserModel> registeredUsers) {
        view = new MainMenuView(user);
        controller = new MainMenuController(game, view, user, registeredUsers);
    }

    @Override
    public void show() {}

    @Override
    public void render(float delta) {
        controller.render(delta);
    }

    @Override
    public void resize(int width, int height) {}

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {}

    @Override
    public void dispose() {
        controller.dispose();
    }
}
