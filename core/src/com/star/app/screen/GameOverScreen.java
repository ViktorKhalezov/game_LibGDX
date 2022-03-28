package com.star.app.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.ScreenUtils;
import com.star.app.screen.utils.Assets;

public class GameOverScreen extends AbstractScreen {

    private Stage stage;
    private BitmapFont font72;
    private BitmapFont font32;
    private int score;
    private int money;

    public GameOverScreen(SpriteBatch batch) {
        super(batch);
    }


    @Override
    public void show() {
        this.stage = new Stage(ScreenManager.getInstance().getViewport(), batch);
        this.font72 = Assets.getInstance().getAssetManager().get("fonts/font72.ttf");
        this.font32 = Assets.getInstance().getAssetManager().get("fonts/font32.ttf");
        this.score = ScreenManager.getInstance().getGameScreen().getGameController().getHero().getScore();
        this.money = ScreenManager.getInstance().getGameScreen().getGameController().getHero().getMoney();
    }

    public void update(float dt) {
        if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
            ScreenManager.getInstance().changeScreen(ScreenManager.ScreenType.GAME);
        }

    }


    @Override
    public void render(float delta) {
        ScreenUtils.clear(0.0f, 0.0f, 0.2f, 1);
        batch.begin();
        font72.draw(batch, "GAME OVER", 0, 600, 1280, 1, false);
        font32.draw(batch, "SCORE: " + score, 0, 400, 1280, 1, false);
        font32.draw(batch, "MONEY: " + money, 0, 350, 1280, 1, false);
        batch.end();
        update(delta);
    }

    @Override
    public void dispose() {

    }


}
