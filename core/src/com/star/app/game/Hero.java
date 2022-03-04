package com.star.app.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.star.app.screen.ScreenManager;
import com.star.app.screen.utils.Assets;


public class Hero {
    private GameController gc;
    private TextureRegion texture;
    private Vector2 position;
    private Vector2 velocity;
    private float angle;
    private float enginePower;
    private float fireTimer;
    private int score;
    private int scoreView;

    public int getScore() {
        return score;
    }

    public int getScoreView() {
        return scoreView;
    }

    public Vector2 getPosition() {
        return position;
    }

    public Vector2 getVelocity() {
        return velocity;
    }

    public float getAngle() {
        return angle;
    }

    public Hero(GameController gc) {
        this.gc = gc;
        this.texture = Assets.getInstance().getAtlas().findRegion("ship");
        this.position = new Vector2(640, 360);
        this.velocity = new Vector2(0, 0);
        this.angle = 0.0f;
        this.enginePower = 700.0f;
    }

    public void addScore(int amount) {
        score += amount;
    }

    public void render(SpriteBatch batch) {
        batch.draw(texture, position.x - 32, position.y - 32, 32, 32,
                64, 64, 1, 1, angle);
    }

    public void update(float dt) {
        fireTimer += dt;
        if (scoreView < score) {
            scoreView += 1500 * dt;
            if (scoreView > score) {
                scoreView = score;
            }
        }


        if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
            if (fireTimer > 0.2) {
                fireTimer = 0.0f;

                float wx = position.x + MathUtils.cosDeg(angle + 90) * 20;
                float wy = position.y + MathUtils.sinDeg(angle + 90) * 20;

                gc.getBulletController().setup(wx, wy,
                        MathUtils.cosDeg(angle) * 500 + velocity.x,
                        MathUtils.sinDeg(angle) * 500 + velocity.y);


                wx = position.x + MathUtils.cosDeg(angle - 90) * 20;
                wy = position.y + MathUtils.sinDeg(angle - 90) * 20;

                gc.getBulletController().setup(wx, wy,
                        MathUtils.cosDeg(angle) * 500 + velocity.x,
                        MathUtils.sinDeg(angle) * 500 + velocity.y);

            }
        }
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            angle += 180 * dt;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            angle -= 180 * dt;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            velocity.x += MathUtils.cosDeg(angle) * enginePower * dt;
            velocity.y += MathUtils.sinDeg(angle) * enginePower * dt;
        }
        if(Gdx.input.isKeyPressed(Input.Keys.S)) {
            velocity.x -= MathUtils.cosDeg(angle) * (enginePower / 2) * dt;
            velocity.y -= MathUtils.sinDeg(angle) * (enginePower / 2) * dt;
        }

        position.mulAdd(velocity, dt);
        float stopKoef = 1.0f - dt;
        if (stopKoef < 0.0f) {
            stopKoef = 0.0f;
        }
        velocity.scl(stopKoef);

        checkBorders();
    }

    public void checkBorders() {
        if (position.x < 32) {
            position.x = 32f;
            velocity.x *= -0.5f;
        }
        if (position.x > ScreenManager.SCREEN_WIDTH - 32) {
            position.x = ScreenManager.SCREEN_WIDTH - 32f;
            velocity.x *= -0.5f;
        }
        if (position.y < 32) {
            position.y = 32f;
            velocity.y *= -0.5f;
        }
        if (position.y > ScreenManager.SCREEN_HEIGHT - 32) {
            position.y = ScreenManager.SCREEN_HEIGHT - 32f;
            velocity.y *= -0.5f;
        }
    }

}
