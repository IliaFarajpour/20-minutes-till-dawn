package com.Project.Screen;

import com.Project.controller.ProfileController;
import com.Project.model.UserModel;
import com.Project.view.ProfileView;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;

import java.util.HashMap;

public class ProfileScreen implements Screen {
    private ProfileView view;
    private ProfileController controller;

    public ProfileScreen(Game game, UserModel user, HashMap<String, UserModel> registeredUsers) {
        view = new ProfileView(user);
        controller = new ProfileController(game, view, user, registeredUsers);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(view.stage);
    }
    @Override
    public void render(float delta) { controller.render(delta); }
    @Override
    public void resize(int width, int height) {
        view.stage.getViewport().update(width, height, true);
    }
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}
    @Override public void dispose() { controller.dispose(); }
}
