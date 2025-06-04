package com.Project.Screen;

import com.Project.controller.RegisterController;
import com.Project.model.UserModel;
import com.Project.view.RegisterView;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;

import java.util.HashMap;

public class RegisterScreen implements Screen {
    private RegisterController controller;
    private RegisterView view;

    public RegisterScreen(Game game, HashMap<String, UserModel> registeredUsers) {
        view = new RegisterView();
        controller = new RegisterController(game, view, registeredUsers);
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
