
package com.Project.view;

import com.Project.model.GameAssets;
import com.Project.model.HeroInfo;
import com.Project.model.Weapon;
import com.Project.utils.HeroFactory;
import com.Project.utils.WeaponFactory;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class PreGameMenuView {
    public Stage stage;
    public SelectBox<HeroInfo> heroSelectBox;
    public SelectBox<Weapon> weaponSelectBox;
    public SelectBox<String> durationSelectBox;
    public TextButton startGameButton;
    public Label errorLabel;

    public PreGameMenuView() {
        Skin skin = GameAssets.getGameAssets().getSkin();
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        heroSelectBox = new SelectBox<>(skin);
        heroSelectBox.setItems(
            HeroFactory.createDasher(),
            HeroFactory.createDiamond(),
            HeroFactory.createScarlet(),
            HeroFactory.createLilith(),
            HeroFactory.createShana()
        );

        weaponSelectBox = new SelectBox<>(skin);
        weaponSelectBox.setItems(
            WeaponFactory.createRevolver(),
            WeaponFactory.createShotgun(),
            WeaponFactory.createDualSMGs()
        );

        durationSelectBox = new SelectBox<>(skin);
        durationSelectBox.setItems("2", "5", "10", "20");

        startGameButton = new TextButton("Start Game", skin);
        errorLabel = new Label("", skin);
        errorLabel.setColor(Color.RED);

        table.defaults().pad(80).left();

        table.add(new Label("Choose Hero:", skin)).width(150);
        table.add(heroSelectBox).width(500);
        table.row();

        table.add(new Label("Choose Weapon:", skin)).width(150);
        table.add(weaponSelectBox).width(500);
        table.row();

        table.add(new Label("Game Duration (min):", skin)).width(150);
        table.add(durationSelectBox).width(500);
        table.row();

        table.add(startGameButton).colspan(2).width(400).center();
        table.row();

        table.add(errorLabel).colspan(2).width(400);
    }

    public void render(float delta) {
        stage.act(delta);
        stage.draw();
    }

    public void dispose() {
        stage.dispose();
    }
}
