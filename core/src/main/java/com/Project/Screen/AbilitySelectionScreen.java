package com.Project.Screen;

import com.Project.model.AbilityType;
import com.Project.model.Player;
import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class AbilitySelectionScreen implements Screen {
    private Game game;
    private Player player;
    private GameScreen gameScreen;

    private Stage stage;
    private Skin skin;

    public AbilitySelectionScreen(Game game, Player player, GameScreen gameScreen) {
        this.game = game;
        this.player = player;
        this.gameScreen = gameScreen;

        stage = new Stage(new ScreenViewport());
        skin = new Skin(Gdx.files.internal("skin/pixthulhu-ui.json"));

        Label title = new Label("Choose an Ability", skin, "title");
        title.setFontScale(1f);
        title.setPosition(500, 1000);
        stage.addActor(title);

        AbilityType[] options = player.getAbilityOptions();

        Table table = new Table();
        table.setFillParent(true);
        table.top().padTop(300);
        stage.addActor(table);

        for (int i = 0; i < options.length; i++) {
            final int index = i;
            String desc = getAbilityDescription(options[i]);
            TextButton btn = new TextButton(desc, skin, "default");
            btn.getLabel().setFontScale(1.5f);
            btn.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    player.chooseAbility(index);
                    Gdx.graphics.setSystemCursor(Cursor.SystemCursor.None);
                    Gdx.input.setCursorCatched(true);

                    game.setScreen(gameScreen);
                }
            });
            table.row().pad(20);
            table.add(btn).width(1600).height(200);
        }
    }

    private String getAbilityDescription(AbilityType ability) {
        switch (ability) {
            case VITALITY:
                return "Increase max HP by 1";
            case DAMAGER:
                return "Increase damage by 25% for 10 seconds";
            case PROJECTILE:
                return "Increase projectile count by 1";
            case AMO_INCREASE:
                return "Increase max ammo by 5";
            case SPEEDY:
                return "Double movement speed for 10 seconds";
            default:
                return "";
        }
    }

    @Override
    public void show() {
        // موس رو فعال کن و نشانگر سیستم رو بزار روی Arrow
        Gdx.graphics.setSystemCursor(Cursor.SystemCursor.Arrow);
        Gdx.input.setCursorCatched(false);
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 0.8f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}
    @Override public void dispose() {
        stage.dispose();
        skin.dispose();
    }
}
