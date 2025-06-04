package com.Project.Screen;

import com.Project.controller.ForgotPasswordController;
import com.Project.view.ForgotPasswordView;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;

import java.util.HashMap;
import com.Project.model.UserModel;

public class ForgotPasswordScreen implements Screen {
    private ForgotPasswordView view;
    private ForgotPasswordController controller;

    public ForgotPasswordScreen(Game game, HashMap<String, UserModel> registeredUsers) {
        view = new ForgotPasswordView();
        controller = new ForgotPasswordController(game, view, registeredUsers);

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
