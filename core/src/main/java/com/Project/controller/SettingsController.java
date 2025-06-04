package com.Project.controller;

import com.Project.Screen.MainMenuScreen;
import com.Project.model.UserModel;
import com.Project.view.SettingsView;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.Actor;

import java.util.HashMap;

public class SettingsController {
    private Game game;
    private SettingsView view;
    private UserModel user;
    private HashMap<String, UserModel> registeredUsers;
    private Music backgroundMusic;
    private GameController gameController;

    public SettingsController(Game game, SettingsView view, UserModel user, HashMap<String, UserModel> registeredUsers, GameController gameController) {
        this.game = game;
        this.view = view;
        this.user = user;
        this.registeredUsers = registeredUsers;
        this.gameController = gameController;

        backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("music/track1.wav"));
        backgroundMusic.setLooping(true);
        backgroundMusic.setVolume(view.musicVolumeSlider.getValue() / 100f);
        backgroundMusic.play();

        view.backButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                System.out.println("Back button clicked");
                saveSettings();

                view.dispose();
                game.setScreen(new MainMenuScreen(game, user, registeredUsers));

            }
        });

        view.musicVolumeSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                float volume = view.musicVolumeSlider.getValue() / 100f;
                if (backgroundMusic != null) {
                    backgroundMusic.setVolume(volume);
                }
                System.out.println("Music volume: " + volume);
            }
        });

        view.musicTrackSelectBox.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                String selectedTrack = view.musicTrackSelectBox.getSelected();
                if (backgroundMusic != null) {
                    backgroundMusic.stop();
                    backgroundMusic.dispose();
                }

                String filePath = "music/";
                switch (selectedTrack) {
                    case "Track 1":
                        filePath += "track1.wav";
                        break;
                    case "Track 2":
                        filePath += "track2.wav";
                        break;
                    case "Track 3":
                        filePath += "track3.mp3";
                        break;
                    default:
                        filePath += "track1.mp3";
                }

                backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal(filePath));
                backgroundMusic.setLooping(true);
                backgroundMusic.setVolume(view.musicVolumeSlider.getValue() / 100f);
                backgroundMusic.play();

                System.out.println("Selected track: " + filePath);
            }
        });

        view.sfxCheckBox.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                boolean sfxEnabled = view.sfxCheckBox.isChecked();
                user.setSfxEnabled(sfxEnabled);
            }
        });

        view.changeControlsButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {

            }
        });
        view.grayscaleCheckbox.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                user.setGrayscale(view.grayscaleCheckbox.isChecked());
                System.out.println("Grayscale mode: " + view.grayscaleCheckbox.isChecked());
            }
        });

        InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(view.stage);
        multiplexer.addProcessor(new InputAdapter() {
            @Override
            public boolean keyDown(int keycode) {
                return true;
            }
        });
        Gdx.input.setInputProcessor(multiplexer);

        view.setOnKeyChangedListener(new SettingsView.OnKeyChangedListener() {
            @Override
            public void onKeyChanged(String action, int keycode) {
                try {
                    if (keycode == -1) {
                        System.out.println("Invalid keycode received, ignoring");
                        return;
                    }
                    System.out.println("Key changed: " + action + " -> " + com.badlogic.gdx.Input.Keys.toString(keycode));

                    if (user == null || gameController == null) {
                        System.out.println("User or GameController is null!");
                        return;
                    }

                    switch (action) {
                        case "keyUp":
                            user.setKeyUp(keycode);
                            gameController.setKeyUp(keycode);
                            break;
                        case "keyDown":
                            user.setKeyDown(keycode);
                            gameController.setKeyDown(keycode);
                            break;
                        case "keyLeft":
                            user.setKeyLeft(keycode);
                            gameController.setKeyLeft(keycode);
                            break;
                        case "keyRight":
                            user.setKeyRight(keycode);
                            gameController.setKeyRight(keycode);
                            break;
                        case "keyReload":
                            user.setKeyReload(keycode);
                            gameController.setKeyReload(keycode);
                            break;
                        case "keyAutoAim":
                            user.setKeyAutoAim(keycode);
                            gameController.setKeyAutoAim(keycode);
                            break;
                        case "mouseShootButton":
                            user.setMouseShootButton(keycode);
                            gameController.setMouseShootButton(keycode);
                            break;
                        default:
                            System.out.println("Unknown action: " + action);
                    }
                } catch (Exception e) {
                    System.err.println("Error in onKeyChanged: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        });
    }

    private void saveSettings() {
        registeredUsers.put(user.getUsername(), user);
        System.out.println("Settings saved for user: " + user.getUsername());
    }

    public void render(float delta) {
        view.render(delta);
    }

    public void dispose() {
        if (backgroundMusic != null) {
            backgroundMusic.stop();
            backgroundMusic.dispose();
            backgroundMusic = null;
        }
        view.dispose();
    }
}
