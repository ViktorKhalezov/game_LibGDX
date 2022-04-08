package com.star.app.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.star.app.game.helpers.ObjectPool;
import com.star.app.screen.utils.Assets;

public class RocketController extends ObjectPool<Rocket> {

    private TextureRegion texture;
    private GameController gc;
    private float fireTimer;
    private int cost;
    private float rocketSpeed;


    public RocketController(GameController gc) {
        this.gc = gc;
        this.texture = Assets.getInstance().getAtlas().findRegion("rocket");
        this.cost = 500;
        this.fireTimer = 0.0f;
        this.rocketSpeed = 300.0f;
    }

    public float getFireTimer() {
        return fireTimer;
    }

    public void setFireTimer(float fireTimer) {
        this.fireTimer = fireTimer;
    }

    public float getRocketSpeed() {
        return rocketSpeed;
    }

    public int getCost() {
        return cost;
    }

    @Override
    protected Rocket newObject() {
        return new Rocket(gc);
    }

    public void render(SpriteBatch batch) {
        for (int i = 0; i < activeList.size(); i++) {
            Rocket rocket = activeList.get(i);
            rocket.render(batch);
        }
    }

    public void setup(float x, float y, float vx, float vy, float angle){
            getActiveElement().activate(x, y, vx, vy, angle);
    }

    public void update(float dt) {
        fireTimer += dt;
        for (int i = 0; i < activeList.size(); i++) {
            activeList.get(i).update(dt);
        }
        checkPool();
    }

}
