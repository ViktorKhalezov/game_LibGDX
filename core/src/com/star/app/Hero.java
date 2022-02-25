package com.star.app;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class Hero {
    private Texture texture;
    private Vector2 position;
    private Vector2 lastDisplacement;
    private float angle;

    public Vector2 getPosition() {
        return position;
    }

    public Vector2 getLastDisplacement() {
        return lastDisplacement;
    }

    public float getAngle() {
        return angle;
    }

    public Hero() {
        this.texture = new Texture("ship.png");
        this.position = new Vector2(640, 360);
        this.lastDisplacement = new Vector2(0, 0);
        this.angle = 0.0f;
    }

    public void render(SpriteBatch batch) {
        batch.draw(texture, position.x - 32, position.y - 32, 32, 32,
                64, 64, 1, 1, angle, 0, 0, 64, 64,
                false, false);
    }

    public void update(float dt) {
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            angle += 180 * dt;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            angle -= 180 * dt;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            position.x += MathUtils.cosDeg(angle) * 500f * dt;
            position.y += MathUtils.sinDeg(angle) * 500f * dt;
            lastDisplacement.set(MathUtils.cosDeg(angle) * 500f * dt, MathUtils.sinDeg(angle) * 500f * dt);
        }
        if(Gdx.input.isKeyPressed(Input.Keys.S)) {
            position.x -= MathUtils.cosDeg(angle) * 250f * dt;
            position.y -= MathUtils.sinDeg(angle) * 250f * dt;
            lastDisplacement.set(MathUtils.cosDeg(angle) * 250f * dt, MathUtils.sinDeg(angle) * 250f * dt);
        } else {
            lastDisplacement.set(0, 0);
        }
        checkBorders();
    }

    public void checkBorders() {
        if (position.x < 32) {
            position.x = 32f;
        }
        if (position.x > ScreenManager.SCREEN_WIDTH - 32) {
            position.x = ScreenManager.SCREEN_WIDTH - 32f;
        }
        if (position.y < 32) {
            position.y = 32f;
        }
        if (position.y > ScreenManager.SCREEN_HEIGHT - 32) {
            position.y = ScreenManager.SCREEN_HEIGHT - 32f;
        }
    }

}
