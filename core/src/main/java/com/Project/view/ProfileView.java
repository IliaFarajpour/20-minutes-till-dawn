package com.Project.view;

import com.Project.model.GameAssets;
import com.Project.model.UserModel;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class ProfileView {
    public Stage stage;
    public TextField usernameField, passwordField;
    public SelectBox<String> avatarSelectBox;
    public TextButton browseAvatarButton;
    public TextButton saveButton, deleteButton, backButton;
    public Label errorLabel;

    private Skin skin;
    private UserModel user;
    private Image avatarPreview;

    public ProfileView(UserModel user) {
        this.user = user;
        this.skin = GameAssets.getGameAssets().getSkin();
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        usernameField = new TextField(user.getUsername(), skin);
        passwordField = new TextField("", skin);
        passwordField.setMessageText("New Password");

        avatarSelectBox = new SelectBox<>(skin);
        avatarSelectBox.setItems("avatar1.png", "avatar2.png", "avatar3.png", "avatar4.png");

        browseAvatarButton = new TextButton("Choose File...", skin);
        saveButton = new TextButton("Save Changes", skin);
        deleteButton = new TextButton("Delete Account", skin);
        backButton = new TextButton("Back", skin);
        errorLabel = new Label("", skin);
        errorLabel.setColor(com.badlogic.gdx.graphics.Color.RED);


        avatarPreview = new Image();
        avatarPreview.setSize(100, 100);

        table.defaults().pad(10);

        table.add(new Label("Username:", skin));
        table.add(usernameField).width(200);
        table.row();

        table.add(new Label("New Password:", skin));
        table.add(passwordField).width(450);
        table.row();

        table.add(new Label("Choose Avatar:", skin));
        table.add(avatarSelectBox).width(450);
        table.row();

        table.add(new Label("Or upload custom:", skin));
        table.add(browseAvatarButton).width(400);
        table.row();

        table.add(new Label("Preview:", skin));
        table.add(avatarPreview).size(100).pad(10);
        table.row();

        table.add(saveButton).colspan(2);
        table.row();
        table.add(deleteButton).colspan(2);
        table.row();
        table.add(backButton).colspan(2);
        table.row();
        table.add(errorLabel).colspan(2);
    }

    public void render(float delta) {
        stage.act(delta);
        stage.draw();
    }

    public void dispose() {
        stage.dispose();
    }

    public void updateAvatarPreview(String imagePath) {
        Gdx.app.postRunnable(() -> {
            try {
                FileHandle file = Gdx.files.absolute(imagePath);
                if (!file.exists()) {
                    errorLabel.setText("File doesn't exist!");
                    return;
                }

                Texture texture = new Texture(file);
                avatarPreview.setDrawable(new TextureRegionDrawable(new TextureRegion(texture)));
                user.setAvatar(imagePath);
                errorLabel.setText("Avatar preview updated!");
            } catch (Exception e) {
                errorLabel.setText("Failed to load image!");
                e.printStackTrace();
            }
        });
    }

    public void handleFileDrop(String filePath) {
        FileHandle file = Gdx.files.absolute(filePath);
        if (file.exists() && (filePath.endsWith(".png") || filePath.endsWith(".jpg"))) {
            updateAvatarPreview(filePath);
            errorLabel.setText("Avatar updated via drag & drop!");
        } else {
            errorLabel.setText("Unsupported file!");
        }
    }
}
