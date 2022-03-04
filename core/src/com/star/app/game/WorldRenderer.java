package com.star.app.game;


import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.star.app.screen.utils.Assets;

public class WorldRenderer {
    private GameController gc;
    private SpriteBatch batch;
    private BitmapFont font32;
    private StringBuilder score;
    private StringBuilder health;


    public WorldRenderer(GameController gc, SpriteBatch batch) {
        this.gc = gc;
        this.batch = batch;
        this.font32 = Assets.getInstance().getAssetManager().get("fonts/font32.ttf", BitmapFont.class);
        this.score = new StringBuilder();
        this.health = new StringBuilder();
    }

    public void render () {
        ScreenUtils.clear(0, 0, 0.5f, 1);
        batch.begin();
        gc.getBackground().render(batch);
        gc.getBulletController().render(batch);
        gc.getAsteroidController().render(batch);
        gc.getHero().render(batch);
        score.setLength(0);
        score.append("SCORE: ").append(gc.getHero().getScoreView());
        font32.draw(batch, score,20, 700);
        health.setLength(0);
        health.append("Health: ").append(gc.getHero().getHp());
        font32.draw(batch, health, 20, 670);
        batch.end();
    }

}
