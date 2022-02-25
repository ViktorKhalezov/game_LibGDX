package com.star.app;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

public class StarGame extends ApplicationAdapter {
	private SpriteBatch batch;
	private Background background;
	private Hero hero;
	private Asteroid asteroid;

	public Hero getHero() {
		return hero;
	}

	public Asteroid getAsteroid() {
		return asteroid;
	}

	@Override
	public void create () {
		batch = new SpriteBatch();
		background = new Background(this);
		hero = new Hero();
		asteroid = new Asteroid();
	}

	@Override
	public void render () {
		float dt = Gdx.graphics.getDeltaTime();
		update(dt);
		ScreenUtils.clear(0, 0, 0.5f, 1);
		batch.begin();
		background.render(batch);
		hero.render(batch);
		asteroid.render(batch);
		batch.end();
	}

	public void update(float dt){
		background.update(dt);
		hero.update(dt);
		asteroid.update(dt);
	}

	@Override
	public void dispose () {
		batch.dispose();
	}

}
