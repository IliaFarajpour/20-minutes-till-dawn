package com.Project.Screen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.Project.controller.LoginController;
import com.Project.view.LoginView;


import java.util.HashMap;
import com.Project.model.UserModel;

public class LoginScreen implements Screen {
    private LoginView view;
    private LoginController controller;

    public LoginScreen(Game game, HashMap<String, UserModel> registeredUsers) {
        view = new LoginView();
        controller = new LoginController(game, view, registeredUsers);
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
