package com.star.app.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.star.app.game.helpers.ObjectPool;

public class PullupController extends ObjectPool<Pullup> {


    @Override
    protected Pullup newObject() {
        return new Pullup();
    }

    public void render(SpriteBatch batch) {
        for (int i = 0; i < activeList.size(); i++) {
            Pullup pullup = activeList.get(i);
            pullup.render(batch);
        }
    }

    public void setup(float x, float y){
        getActiveElement().activate(x, y);
    }


    public void update() {
        checkPool();
    }

}
