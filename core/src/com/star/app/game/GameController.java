package com.star.app.game;


import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.star.app.game.helpers.PullupType;
import com.star.app.screen.ScreenManager;

public class GameController {
    private Background background;
    private BulletController bulletController;
    private AsteroidController asteroidController;
    private ParticleController particleController;
    private PullupController pullupController;
    private Hero hero;
    private Vector2 tempVec;

    public ParticleController getParticleController() {
        return particleController;
    }

    public AsteroidController getAsteroidController() {
        return asteroidController;
    }

    public BulletController getBulletController() {
        return bulletController;
    }

    public PullupController getPullupController() {
        return pullupController;
    }

    public Background getBackground() {
        return background;
    }

    public Hero getHero() {
        return hero;
    }

    public GameController() {
        this.background = new Background(this);
        this.bulletController = new BulletController(this);
        this.asteroidController = new AsteroidController(this);
        this.particleController = new ParticleController();
        this.pullupController = new PullupController();
        this.hero = new Hero(this);
        this.tempVec = new Vector2();

        for (int i = 0; i < 3; i++) {
            asteroidController.setup(MathUtils.random(0, ScreenManager.SCREEN_WIDTH),
                    MathUtils.random(0, ScreenManager.SCREEN_HEIGHT),
                    MathUtils.random(-150, 150), MathUtils.random(-150, 150), 1.0f);
        }

    }

    public void update(float dt) {
        background.update(dt);
        bulletController.update(dt);
        asteroidController.update(dt);
        pullupController.update();
        particleController.update(dt);
        hero.update(dt);
        checkCollisions();
    }


    public void checkCollisions() {
        //столкновение астероидов и героя
        for (int i = 0; i < asteroidController.getActiveList().size(); i++) {
            Asteroid a = asteroidController.getActiveList().get(i);
            if (hero.getHitArea().overlaps(a.getHitArea())) {
                float dst = a.getPosition().dst(hero.getPosition());
                float halfOverLen = (a.getHitArea().radius + hero.getHitArea().radius - dst) / 2.0f;
                tempVec.set(hero.getPosition()).sub(a.getPosition()).nor();
                hero.getPosition().mulAdd(tempVec, halfOverLen);
                a.getPosition().mulAdd(tempVec, -halfOverLen);

                float sumScl = hero.getHitArea().radius * 2 + a.getHitArea().radius;
                hero.getVelocity().mulAdd(tempVec, 200.0f * a.getHitArea().radius / sumScl);
                a.getVelocity().mulAdd(tempVec, -200.0f * hero.getHitArea().radius / sumScl);

                if (a.takeDamage(2)) {
                    hero.addScore(a.getHpMax() * 50);
                    if(a.isActive() == false) {
                        pullupArise(a.getPosition().x, a.getPosition().y);
                    }
                }
                hero.takeDamage(2);
            }
        }

        //столкновение пуль и астероидов
        for (int i = 0; i < bulletController.getActiveList().size(); i++) {
            Bullet b = bulletController.getActiveList().get(i);
            for (int j = 0; j < asteroidController.getActiveList().size(); j++) {
                Asteroid a = asteroidController.getActiveList().get(j);
                if (a.getHitArea().contains(b.getPosition())) {
                    particleController.setup(b.getPosition().x + MathUtils.random(-4, 4), b.getPosition().y + MathUtils.random(-4, 4),
                            b.getVelocity().x * -0.3f + MathUtils.random(-30, 30), b.getVelocity().y * -0.3f + MathUtils.random(-30, 30),
                            0.2f,
                            2.5f, 1.2f,
                            1.0f, 1.0f, 1.0f, 1.0f,
                            0.0f, 0.1f, 1.0f, 0.0f);

                    b.deactivate();
                    if (a.takeDamage(1)) {
                        hero.addScore(a.getHpMax() * 100);
                        if(a.isActive() == false) {
                            pullupArise(a.getPosition().x, a.getPosition().y);
                        }
                    }
                    break;
                }
            }
        }

        //столкновение героя и pullups
        for(int i = 0; i < pullupController.getActiveList().size(); i++) {
            Pullup pullup = pullupController.getActiveList().get(i);
            if(hero.getHitArea().overlaps(pullup.getHitArea())) {
                getBonuses(pullup.getType(), pullup.getPoints());
                pullup.deactivate();
            }
        }

    }


    public void pullupArise(float x, float y) {
        int probabilityNumber = MathUtils.random(1, 3);
        if(probabilityNumber == 1) {
            pullupController.setup(x, y);
        }
    }

    private void getBonuses(PullupType type, int bonusPoints) {
        switch(type) {
            case AMMO:
                Weapon currentWeapon = hero.getCurrentWeapon();
                currentWeapon.addBullets(bonusPoints);
                if(currentWeapon.getCurBullets() > currentWeapon.getMaxBullets()) {
                    currentWeapon.setCurBullets(currentWeapon.getMaxBullets());
                }
                break;
            case COIN:
                hero.addCoins(bonusPoints);
                break;
            case CURE:
                hero.addHp(bonusPoints);
                if(hero.getHp() > hero.getHpMax()) {
                    hero.setHp(hero.getHpMax());
                }
        }
    }

}
