package com.Project.controller;

import com.Project.Main;
import com.Project.Screen.MainMenuScreen;
import com.Project.Screen.RegisterScreen;
import com.Project.model.UserModel;
import com.Project.view.ProfileView;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.util.HashMap;

public class ProfileController {
    private Game game;
    private ProfileView view;
    private UserModel user;
    private HashMap<String, UserModel> registeredUsers;

    public ProfileController(Game game, ProfileView view, UserModel user, HashMap<String, UserModel> registeredUsers) {
        this.game = game;
        this.view = view;
        this.user = user;
        this.registeredUsers = registeredUsers;

        // برای دسترسی به profileView از launcher
        if (game instanceof Main) {
            ((Main) game).setProfileView(view);
        }

        // دکمه ذخیره
        view.saveButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                handleSave();
            }
        });

        // دکمه حذف اکانت
        view.deleteButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                registeredUsers.remove(user.getUsername());
                game.setScreen(new RegisterScreen(game, registeredUsers));
                dispose();
            }
        });

        // دکمه بازگشت
        view.backButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new MainMenuScreen(game, user, registeredUsers));
                dispose();
            }
        });

        view.browseAvatarButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                new Thread(() -> {
                    JFileChooser fileChooser = new JFileChooser();
                    fileChooser.setDialogTitle("Choose Avatar");
                    fileChooser.setFileFilter(new FileNameExtensionFilter("Image Files", "png", "jpg", "jpeg"));
                    int result = fileChooser.showOpenDialog(null);

                    if (result == JFileChooser.APPROVE_OPTION) {
                        String path = fileChooser.getSelectedFile().getAbsolutePath();
                        FileHandle selectedFile = Gdx.files.absolute(path);

                        if (selectedFile.exists()) {
                            Gdx.app.postRunnable(() -> {
                                user.setAvatar(selectedFile.path());
                                view.errorLabel.setText("Avatar changed to: " + selectedFile.name());
                                Gdx.input.setInputProcessor(view.stage);
                                Gdx.graphics.setSystemCursor(Cursor.SystemCursor.Arrow);
                            });
                        }
                    }
                }).start();
            }
        });
    }

    private void handleSave() {
        String newUsername = view.usernameField.getText().trim();
        String newPassword = view.passwordField.getText().trim();

        if (!newUsername.equals(user.getUsername()) && registeredUsers.containsKey(newUsername)) {
            view.errorLabel.setText("Username already exists!");
            return;
        }

        if (!newPassword.isEmpty() && UserModel.isWeakPassword(newPassword)) {
            view.errorLabel.setText("Password too weak!");
            return;
        }

        if (!newUsername.equals(user.getUsername())) {
            registeredUsers.remove(user.getUsername());
            user.setUsername(newUsername);
            registeredUsers.put(newUsername, user);
        }

        if (!newPassword.isEmpty()) {
            user.setPassword(newPassword);
        }

        // اگر کاربر چیزی از لیست انتخاب کرده باشه
        String selectedAvatar = view.avatarSelectBox.getSelected();
        if (selectedAvatar != null && !selectedAvatar.isEmpty()) {
            String path = "avatars/" + selectedAvatar;
            user.setAvatar(path);
            // view.updateAvatarPreview(path);
        }

        view.errorLabel.setText("Changes saved successfully!");
    }

    public void render(float delta) {
        view.render(delta);
    }

    public void dispose() {
        view.dispose();
    }
}
