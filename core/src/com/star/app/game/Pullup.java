package com.star.app.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.star.app.game.helpers.Poolable;
import com.star.app.game.helpers.PullupType;
import com.star.app.screen.utils.Assets;

public class Pullup implements Poolable {
    private Vector2 position;
    private boolean active;
    private PullupType type;
    private Circle hitArea;
    private int points;
    private TextureRegion texture;

    private final float BASE_SIZE = 32.0f;
    private final float BASE_RADIUS = BASE_SIZE / 2;


    public Pullup() {
        this.position = new Vector2();
        this.active = false;
        this.hitArea = new Circle(0, 0, 0);
    }

    @Override
    public boolean isActive() {
        return active;
    }

    public int getPoints() {
        return points;
    }

    public PullupType getType() {
        return type;
    }

    public Circle getHitArea() {
        return hitArea;
    }

    private void defineTexture() {
       int typeNumber = MathUtils.random(1, 3);
       switch(typeNumber) {
           case 1:
               this.type = PullupType.AMMO;
               this.texture = Assets.getInstance().getAtlas().findRegion("ammo");
               break;
           case 2:
               this.type = PullupType.CURE;
               this.texture = Assets.getInstance().getAtlas().findRegion("cure");
               break;
           case 3:
               this.type = PullupType.COIN;
               this.texture = Assets.getInstance().getAtlas().findRegion("coin");
       }

    }

    public void render(SpriteBatch batch) {
        batch.draw(texture, position.x - 16, position.y - 16, 16, 16,
                256, 256, 0.2f, 0.2f, 0);
    }


    public void activate(float x, float y) {
        position.set(x, y);
        hitArea.set(x, y, BASE_RADIUS * 0.9f);
        defineTexture();
        points = MathUtils.random(10, 50);
        active = true;

    }

    public void deactivate() {
        active = false;
    }


}
