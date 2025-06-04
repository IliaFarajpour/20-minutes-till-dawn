package com.Project;

import com.Project.Screen.RegisterScreen;
import com.Project.model.UserModel;
import com.Project.view.ProfileView;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;

import java.util.HashMap;

public class Main extends Game {
    private HashMap<String, UserModel> registeredUsers = new HashMap<>();
    private ProfileView profileView;

    @Override
    public void create() {
        setScreen(new RegisterScreen(this, registeredUsers));
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        super.render();
    }

    @Override
    public void dispose() {
        getScreen().dispose();
    }

    public ProfileView getProfileView() {
        return profileView;
    }

    public void setProfileView(ProfileView profileView) {
        this.profileView = profileView;
    }
}
