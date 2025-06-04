package com.Project.controller;

import com.Project.Screen.GameScreen;
import com.Project.model.HeroInfo;
import com.Project.model.UserModel;
import com.Project.model.Weapon;
import com.Project.view.PreGameMenuView;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

import java.util.HashMap;

public class PreGameMenuController {
    private Game game;
    private PreGameMenuView view;
    private UserModel user;
    private HashMap<String, UserModel> registeredUsers;
    public PreGameMenuController(Game game, PreGameMenuView view, UserModel user, HashMap<String, UserModel> registeredUsers) {
        this.game = game;
        this.view = view;
        this.user = user;
        this.registeredUsers = registeredUsers;

        view.startGameButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                HeroInfo selectedHero = view.heroSelectBox.getSelected();
                Weapon selectedWeapon = view.weaponSelectBox.getSelected();  // 🔁 String → Weapon
                String duration = view.durationSelectBox.getSelected();

                if (selectedHero == null || selectedWeapon == null || duration == null) {
                    view.errorLabel.setText("Please select all options.");
                    return;
                }


                int timeMinutes = Integer.parseInt(duration);
                int timeSeconds = timeMinutes * 60;
                view.errorLabel.setText("Starting game with " +
                    selectedHero.getName() + ", " + selectedWeapon.getName() + ", " + timeSeconds + " min");

                game.setScreen(new GameScreen(game ,user, selectedHero, selectedWeapon, timeSeconds,registeredUsers,user));
                dispose();
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
