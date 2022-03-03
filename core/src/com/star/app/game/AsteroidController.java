package com.star.app.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.star.app.game.helpers.ObjectPool;

public class AsteroidController extends ObjectPool<Asteroid> {
    private Texture asteroidTexture;

    public AsteroidController() {
        this.asteroidTexture = new Texture("asteroid.png");
    }

    @Override
    protected Asteroid newObject() {
        return new Asteroid();
    }

    public void render(SpriteBatch batch) {
        for(int i = 0; i < activeList.size(); i++) {
            Asteroid asteroid = activeList.get(i);
            asteroid.render(batch);
        }
    }

    public void setUp() {
        if(activeList.size() == 0) {
            getActiveElement().activate();
        }
    }

    public void update(float dt) {
        for(int i = 0; i < activeList.size(); i++) {
            activeList.get(i).update(dt);
        }
        checkPool();
    }

}
