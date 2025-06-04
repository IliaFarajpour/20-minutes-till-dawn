package com.Project.Screen;

import com.Project.controller.PreGameMenuController;
import com.Project.model.UserModel;
import com.Project.view.PreGameMenuView;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;

import java.util.HashMap;

public class PreGameMenuScreen implements Screen {
    private PreGameMenuController controller;
    private PreGameMenuView view;
    private HashMap<String, UserModel> registeredUsers;
    public PreGameMenuScreen(Game game, UserModel user,HashMap<String, UserModel> registeredUsers) {
        this.registeredUsers = registeredUsers;
        view = new PreGameMenuView();
        controller = new PreGameMenuController(game, view, user,registeredUsers);
    }

    @Override
    public void render(float delta) {
        controller.render(delta);
    }

    @Override
    public void resize(int width, int height) {}
    @Override
    public void show() {}
    @Override
    public void hide() {}
    @Override
    public void pause() {}
    @Override
    public void resume() {}
    @Override
    public void dispose() {
        controller.dispose();
    }
}
