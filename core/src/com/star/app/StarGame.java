package com.star.app;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.star.app.game.Asteroid;
import com.star.app.game.Background;
import com.star.app.game.Hero;
import com.star.app.screen.GameScreen;

public class StarGame extends Game {
	private SpriteBatch batch;
	private GameScreen gameScreen;


	@Override
	public void create () {
		batch = new SpriteBatch();
		this.gameScreen = new GameScreen(batch);
		setScreen(gameScreen);
	}

	@Override
	public void render () {
		float dt = Gdx.graphics.getDeltaTime();
		gameScreen.render(dt);
	}

	@Override
	public void dispose () {
		batch.dispose();
	}

}
