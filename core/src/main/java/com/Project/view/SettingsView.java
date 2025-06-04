package com.Project.view;

import com.Project.model.UserModel;
import com.Project.model.GameAssets;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class SettingsView {
    public Stage stage;

    public Slider musicVolumeSlider;
    public SelectBox<String> musicTrackSelectBox;
    public CheckBox sfxCheckBox;
    public TextButton changeControlsButton;
    public TextButton backButton;

    public Label keyUpLabel;
    public TextButton keyUpButton;
    public Label keyDownLabel;
    public TextButton keyDownButton;
    public Label keyLeftLabel;
    public TextButton keyLeftButton;
    public Label keyRightLabel;
    public TextButton keyRightButton;
    public Label keyReloadLabel;
    public TextButton keyReloadButton;
    public Label keyAutoAimLabel;
    public TextButton keyAutoAimButton;

    public Label mouseShootLabel;
    public TextButton mouseShootButton;

    private Skin skin;
    private UserModel user;

    private TextButton waitingForKeyButton = null;
    public CheckBox grayscaleCheckbox;
    public interface OnKeyChangedListener {
        void onKeyChanged(String action, int keycode);
    }

    private OnKeyChangedListener onKeyChangedListener;

    public void setOnKeyChangedListener(OnKeyChangedListener listener) {
        this.onKeyChangedListener = listener;
    }

    public SettingsView(UserModel user) {
        this.user = user;

        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        skin = GameAssets.getGameAssets().getSkin();

        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        musicVolumeSlider = new Slider(0, 100, 1, false, skin);
        musicTrackSelectBox = new SelectBox<>(skin);
        sfxCheckBox = new CheckBox("Enable SFX", skin);
        changeControlsButton = new TextButton("Change Keyboard Controls", skin);
        backButton = new TextButton("Back", skin);

        musicTrackSelectBox.setItems("Track 1", "Track 2", "Track 3");

        keyUpLabel = new Label("Move Up:", skin);
        keyUpButton = new TextButton(Input.Keys.toString(user.getKeyUp()), skin);

        keyDownLabel = new Label("Move Down:", skin);
        keyDownButton = new TextButton(Input.Keys.toString(user.getKeyDown()), skin);

        keyLeftLabel = new Label("Move Left:", skin);
        keyLeftButton = new TextButton(Input.Keys.toString(user.getKeyLeft()), skin);

        keyRightLabel = new Label("Move Right:", skin);
        keyRightButton = new TextButton(Input.Keys.toString(user.getKeyRight()), skin);

        keyReloadLabel = new Label("Reload:", skin);
        keyReloadButton = new TextButton(Input.Keys.toString(user.getKeyReload()), skin);

        keyAutoAimLabel = new Label("Auto-Aim Toggle:", skin);
        keyAutoAimButton = new TextButton(Input.Keys.toString(user.getKeyAutoAim()), skin);

        mouseShootLabel = new Label("Shoot Button:", skin);
        mouseShootButton = new TextButton(getKeyOrMouseName(user.getMouseShootButton()), skin);
        grayscaleCheckbox = new CheckBox("Grayscale Mode", skin);
        table.add(grayscaleCheckbox).colspan(2);
        table.row();
        table.defaults().pad(10).center();

        table.add(new Label("Music Volume", skin));
        table.add(musicVolumeSlider).width(500);
        table.row();

        table.add(new Label("Music Track", skin));
        table.add(musicTrackSelectBox).width(500);
        table.row();

        table.add(sfxCheckBox).colspan(2);
        table.row();

        Table pair1 = new Table(skin);
        pair1.add(keyUpLabel).padRight(25);
        pair1.add(keyUpButton).width(300).padRight(20);
        pair1.add(keyDownLabel).padRight(25);
        pair1.add(keyDownButton).width(300);
        table.add(pair1).colspan(20);
        table.row();

        Table pair2 = new Table(skin);
        pair2.add(keyLeftLabel).padRight(30);
        pair2.add(keyLeftButton).width(300).padRight(40);
        pair2.add(keyRightLabel).padRight(30);
        pair2.add(keyRightButton).width(300);
        table.add(pair2).colspan(20);
        table.row();

        Table pair3 = new Table(skin);
        pair3.add(keyReloadLabel).padRight(30);
        pair3.add(keyReloadButton).width(300).padRight(40);
        pair3.add(keyAutoAimLabel).padRight(30);
        pair3.add(keyAutoAimButton).width(300);
        table.add(pair3).colspan(20);
        table.row();

        table.add(mouseShootLabel).padRight(20);
        table.add(mouseShootButton).width(300);
        table.row();

        table.add(backButton).colspan(2).width(300).padTop(30);

        addKeyChangeListener(keyUpButton);
        addKeyChangeListener(keyDownButton);
        addKeyChangeListener(keyLeftButton);
        addKeyChangeListener(keyRightButton);
        addKeyChangeListener(keyReloadButton);
        addKeyChangeListener(keyAutoAimButton);
        addKeyChangeListener(mouseShootButton);

        stage.addListener(new InputListener() {
            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                if (waitingForKeyButton != null) {
                    setKeyForButton(waitingForKeyButton, keycode, false);
                    waitingForKeyButton = null;
                    return true;
                }
                return false;
            }

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (waitingForKeyButton != null) {
                    setKeyForButton(waitingForKeyButton, button, true);
                    waitingForKeyButton = null;
                    return true;
                }
                return false;
            }
        });
    }

    private void addKeyChangeListener(final TextButton button) {
        button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                waitingForKeyButton = button;
                if (button == mouseShootButton) {
                    button.setText("Press a key or click mouse...");
                } else {
                    button.setText("Press a key or click mouse...");
                }
            }
        });
    }

    private void setKeyForButton(TextButton button, int code, boolean isMouse) {
        if (button == keyUpButton) {
            user.setKeyUp(code);
            button.setText(isMouse ? getMouseButtonName(code) : Input.Keys.toString(code));
            if (onKeyChangedListener != null)
                onKeyChangedListener.onKeyChanged("keyUp", code);
        } else if (button == keyDownButton) {
            user.setKeyDown(code);
            button.setText(isMouse ? getMouseButtonName(code) : Input.Keys.toString(code));
            if (onKeyChangedListener != null)
                onKeyChangedListener.onKeyChanged("keyDown", code);
        } else if (button == keyLeftButton) {
            user.setKeyLeft(code);
            button.setText(isMouse ? getMouseButtonName(code) : Input.Keys.toString(code));
            if (onKeyChangedListener != null)
                onKeyChangedListener.onKeyChanged("keyLeft", code);
        } else if (button == keyRightButton) {
            user.setKeyRight(code);
            button.setText(isMouse ? getMouseButtonName(code) : Input.Keys.toString(code));
            if (onKeyChangedListener != null)
                onKeyChangedListener.onKeyChanged("keyRight", code);
        } else if (button == keyReloadButton) {
            user.setKeyReload(code);
            button.setText(isMouse ? getMouseButtonName(code) : Input.Keys.toString(code));
            if (onKeyChangedListener != null)
                onKeyChangedListener.onKeyChanged("keyReload", code);
        } else if (button == keyAutoAimButton) {
            user.setKeyAutoAim(code);
            button.setText(isMouse ? getMouseButtonName(code) : Input.Keys.toString(code));
            if (onKeyChangedListener != null)
                onKeyChangedListener.onKeyChanged("keyAutoAim", code);
        } else if (button == mouseShootButton) {
            user.setMouseShootButton(code);
            button.setText(isMouse ? getMouseButtonName(code) : Input.Keys.toString(code));
            if (onKeyChangedListener != null)
                onKeyChangedListener.onKeyChanged("mouseShootButton", code);
        }
    }

    private String getMouseButtonName(int buttonCode) {
        switch (buttonCode) {
            case Input.Buttons.LEFT:
                return "Left Click";
            case Input.Buttons.RIGHT:
                return "Right Click";
            case Input.Buttons.MIDDLE:
                return "Middle Click";
            default:
                return "Mouse Button " + buttonCode;
        }
    }

    private String getKeyOrMouseName(int code) {
        if (code >= 0 && code < 256) { // assume keyboard keycodes are < 256
            return Input.Keys.toString(code);
        } else {
            return getMouseButtonName(code);
        }
    }

    public void render(float delta) {
        stage.act(delta);
        stage.draw();
    }

    public void dispose() {
        stage.dispose();
    }
}
