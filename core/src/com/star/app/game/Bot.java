package com.star.app.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.star.app.screen.utils.Assets;

public class Bot extends Ship {

    private boolean isMoving;


    public Bot(GameController gc) {
        super(gc, 700, 50 + 10 * gc.getLevel());
        this.texture = Assets.getInstance().getAtlas().findRegion("ship"); // можно будет поменять
        this.position = new Vector2(MathUtils.random(0, 1280), MathUtils.random(0, 720));
        this.hitArea = new Circle(position, 28); // поменять радиус, если будет другой размер бота
        setWeapon(gc.getLevel());

    }

    public boolean isMoving() {
        return isMoving;
    }

    public void setMoving(boolean moving) {
        isMoving = moving;
    }

    public void setWeapon(int level) {
        if(level < weapons.length) {
            currentWeapon = weapons[level - 1];
        }
    }

    public void render(SpriteBatch batch) {
        if(isAlive()) {
            batch.draw(texture, position.x - 32, position.y - 32, 32, 32,
                    64, 64, 1, 1, angle);
        }
    }

    public void update(float dt) {
        if(isAlive()) {
            super.update(dt);
            if(isMoving) {
                float bx = position.x + MathUtils.cosDeg(angle + 180) * 25;
                float by = position.y + MathUtils.sinDeg(angle + 180) * 25;
                for (int i = 0; i < 3; i++) {
                    gc.getParticleController().setup(bx + MathUtils.random(-4, 4), by + MathUtils.random(-4, 4),
                            velocity.x * -0.1f + MathUtils.random(-20, 20), velocity.y * -0.1f + MathUtils.random(-20, 20),
                            0.4f,
                            1.2f, 0.2f,
                            1.0f, 0.0f, 0.0f, 1.0f,
                            1.0f, 0.0f, 0.0f, 0.0f);
                }
            }
        }
    }


}
