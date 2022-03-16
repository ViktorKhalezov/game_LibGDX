package com.star.app.game;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;

public class Weapon {
    private GameController gc;
    private Hero hero;
    private float firePeriod;
    private int damage;
    private float bulletSpeed;
    private int maxBullets;
    private int curBullets;

    private Vector3[] slots;

    public float getFirePeriod() {
        return firePeriod;
    }

    public int getDamage() {
        return damage;
    }

    public int getMaxBullets() {
        return maxBullets;
    }

    public int getCurBullets() {
        return curBullets;
    }

    public Weapon(GameController gc, Hero hero, float firePeriod, int damage,
                  float bulletSpeed, int maxBullets, Vector3[] slots) {
        this.gc = gc;
        this.hero = hero;
        this.firePeriod = firePeriod;
        this.damage = damage;
        this.bulletSpeed = bulletSpeed;
        this.maxBullets = maxBullets;
        this.curBullets = maxBullets;
        this.slots = slots;
    }

    public void fire() {
        if (curBullets > 0) {
            curBullets--;

            for (int i = 0; i < slots.length; i++) {
                float x, y, vx, vy;
                x = hero.getPosition().x + slots[i].x * MathUtils.cosDeg(hero.getAngle() + slots[i].y) ;
                y = hero.getPosition().y + slots[i].x * MathUtils.sinDeg(hero.getAngle() + slots[i].y) ;
                vx = hero.getVelocity().x + bulletSpeed * MathUtils.cosDeg(hero.getAngle() + slots[i].z);
                vy = hero.getVelocity().y + bulletSpeed * MathUtils.sinDeg(hero.getAngle() + slots[i].z);

                gc.getBulletController().setup(x, y, vx, vy);
            }
        }
    }


}
