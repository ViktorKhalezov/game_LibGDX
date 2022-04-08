package com.star.app.game;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.star.app.game.helpers.Poolable;
import com.star.app.screen.ScreenManager;
import com.star.app.screen.utils.Assets;

public class Rocket implements Poolable {

    private GameController gc;
    private Vector2 position;
    private Vector2 velocity;
    private boolean active;
    private Circle hitArea;
    private Circle shockWave;
    private float angle;
    private int hitAreaDamage;
    private int shockWaveDamage;
    private Sound launchSound;
    private Sound explosionSound;
    private TextureRegion texture;
    private int hp;
    private int hpMax;

    public Rocket(GameController gc) {
        this.gc = gc;
        this.position = new Vector2();
        this.velocity = new Vector2();
        this.active = false;
        this.hitArea = new Circle(0, 0, 0);
        this.shockWave = new Circle(0, 0, 0);
        this.hitAreaDamage = 100;
        this.shockWaveDamage = 50;
        this.hpMax = 50;
        this.hp = hpMax;
        this.texture = Assets.getInstance().getAtlas().findRegion("rocket");
        this.launchSound = Assets.getInstance().getAssetManager().get("audio/explose2.mp3");
        this.explosionSound = Assets.getInstance().getAssetManager().get("audio/explose.mp3");
    }

    public int getHp() {
        return hp;
    }

    public void takeDamage(int amount) {
        hp -= amount;
    }

    public Sound getExplosionSound() {
        return explosionSound;
    }

    public void setShockWave(Circle shockWave) {
        this.shockWave = shockWave;
    }

    public void setShockWaveDamage(int shockWaveDamage) {
        this.shockWaveDamage = shockWaveDamage;
    }

    public Circle getShockWave() {
        return shockWave;
    }

    public int getShockWaveDamage() {
        return shockWaveDamage;
    }

    public int getHitAreaDamage() {
        return hitAreaDamage;
    }

    public Circle getHitArea() {
        return hitArea;
    }

    public float getAngle() {
        return angle;
    }

    @Override
    public boolean isActive() {
        return active;
    }

    public Vector2 getPosition() {
        return position;
    }

    public Vector2 getVelocity() {
        return velocity;
    }

    public void deactivate() {
        active = false;
    }

    public void render(SpriteBatch batch) {
        batch.draw(texture, position.x - 64, position.y - 16, 64, 16,
                128, 32, 1, 1, angle);
    }

    public void update(float dt) {
        position.mulAdd(velocity, dt);
        if (position.x < -128 || position.x > ScreenManager.SCREEN_WIDTH + 128 ||
                position.y < -128 || position.y > ScreenManager.SCREEN_HEIGHT + 128) {
            deactivate();
        }
        hitArea.setPosition(position);

        gc.getParticleController().getEffectBuilder().createRocketTrace(this);
    }

    public void activate(float x, float y, float vx, float vy, float angle) {
        active = true;
        position.set(x, y);
        velocity.set(vx, vy);
        this.angle = angle;
        hitArea.setPosition(x, y);
        hitArea.setRadius(32);
        launchSound.play();
    }

}


