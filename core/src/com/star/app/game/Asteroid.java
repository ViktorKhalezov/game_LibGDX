package com.star.app.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.star.app.screen.ScreenManager;

public class Asteroid {
    private Texture texture;
    private Vector2 position;
    private float velocity;
    private float angle;
    private final int widthHalf = ScreenManager.SCREEN_WIDTH / 2;
    private final int heightHalf = ScreenManager.SCREEN_HEIGHT / 2;


    public Asteroid() {
        this.texture = new Texture("asteroid.png");
        int x = generateRandomX();
        int y = generateRandomY();
        this.position = new Vector2(x, y);
        this.angle = generateAngle(x, y);
        this.velocity = MathUtils.random(50, 200);
    }

    public void render(SpriteBatch batch) {
        batch.draw(texture, position.x - 128, position.y - 128, 128, 128,
                256, 256, 1, 1, angle, 0, 0, 256, 256,
                true, true);

    }

    public void update(float dt) {
        position.x += MathUtils.cosDeg(angle) * velocity * dt;
        position.y += MathUtils.sinDeg(angle) * velocity * dt;
        checkBorders();
    }

    public void checkBorders() {
        if(position.x < -256 || position.x > 1536) {
            position.x = generateRandomX();
            angle = generateAngle((int) position.x, (int) position.y);
        }
        if(position.y < -256 || position.y > 976) {
           position.y = generateRandomY();
            angle = generateAngle((int) position.x, (int) position.y);
        }
    }

    private int generateRandomX() {
        int smallX = MathUtils.random(-256, -128);
        int bigX = MathUtils.random(1418, 1536);
        double determinant = Math.random();
        if(determinant >= 0.5) {
            return bigX;
        } else {
            return smallX;
        }
    }

    private int generateRandomY() {
        int smallY = MathUtils.random(-256, -128);
        int bigY = MathUtils.random(848, 976);
        double determinant = Math.random();
        if(determinant >= 0.5) {
            return bigY;
        } else {
            return smallY;
        }
    }

    private float generateAngle(int x, int y) {
        float angle = 0f;
        if(x < widthHalf && y < heightHalf) {
          //  angle = MathUtils.random(0, 90);
            angle = MathUtils.random(0, 90);
        } else if(x < widthHalf && y >= heightHalf) {
          //  angle = MathUtils.random(-90, 0);
            angle = MathUtils.random(270, 360);
        } else if(x >= widthHalf && y >= heightHalf) {
          //  angle = MathUtils.random(-90, 0);
            angle = MathUtils.random(180, 270);
        } else if(x >= widthHalf && y < heightHalf) {
           // angle = MathUtils.random(0, 90);
            angle = MathUtils.random(90, 180);
        }
        return angle;
    }

}
