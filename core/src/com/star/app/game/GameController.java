package com.star.app.game;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Align;
import com.star.app.screen.ScreenManager;
import com.star.app.screen.utils.Assets;

public class GameController {

    private Background background;
    private BulletController bulletController;
    private AsteroidController asteroidController;
    private ParticleController particleController;
    private PowerUpsController powerUpsController;
    private InfoController infoController;
    private BotController botController;
    private RocketController rocketController;
    private Hero hero;
    private Vector2 tempVec;
    private Stage stage;
    private boolean pause;
    private int level;
    private float timer;
    private Music music;

    public RocketController getRocketController() {
        return rocketController;
    }

    public float getTimer() {
        return timer;
    }

    public int getLevel() {
        return level;
    }

    public void setPause(boolean pause) {
        this.pause = pause;
    }

    public Stage getStage() {
        return stage;
    }

    public BotController getBotController() {
        return botController;
    }

    public InfoController getInfoController() {
        return infoController;
    }

    public PowerUpsController getPowerUpsController() {
        return powerUpsController;
    }

    public ParticleController getParticleController() {
        return particleController;
    }

    public AsteroidController getAsteroidController() {
        return asteroidController;
    }

    public BulletController getBulletController() {
        return bulletController;
    }

    public Background getBackground() {
        return background;
    }

    public Hero getHero() {
        return hero;
    }

    public GameController(SpriteBatch batch) {
        this.background = new Background(this);
        this.bulletController = new BulletController(this);
        this.asteroidController = new AsteroidController(this);
        this.particleController = new ParticleController();
        this.powerUpsController = new PowerUpsController(this);
        this.infoController = new InfoController();
        this.botController = new BotController(this);
        this.rocketController = new RocketController(this);
        this.hero = new Hero(this);
        this.tempVec = new Vector2();
        this.stage = new Stage(ScreenManager.getInstance().getViewport(), batch);
        this.stage.addActor(hero.getShop());
        Gdx.input.setInputProcessor(stage);
        this.level = 1;
        generateBigAsteroids(2);

        botController.setup(100, 100);

        this.music = Assets.getInstance().getAssetManager().get("audio/mortal.mp3");
        this.music.setLooping(true);
        this.music.play();

    }

    public void generateBigAsteroids(int count) {
        for (int i = 0; i < count; i++) {
            asteroidController.setup(MathUtils.random(0, ScreenManager.SCREEN_WIDTH),
                    MathUtils.random(0, ScreenManager.SCREEN_HEIGHT),
                    MathUtils.random(-150, 150), MathUtils.random(-150, 150), 1.0f);
        }
    }

    public void update(float dt) {
        if (pause) {
            return;
        }
        timer += dt;
        background.update(dt);
        bulletController.update(dt);
        asteroidController.update(dt);
        particleController.update(dt);
        rocketController.update(dt);
        powerUpsController.update(dt);
        infoController.update(dt);
        botController.update(dt);
        hero.update(dt);
        stage.act(dt);
        checkCollisions();
        if (!hero.isAlive()) {
            ScreenManager.getInstance().changeScreen(ScreenManager.ScreenType.GAMEOVER, hero);
        }
        if (asteroidController.getActiveList().size() == 0 && botController.getActiveList().size() == 0) {
            level++;
            generateBigAsteroids(level + 2);
            timer = 0;
        }
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
                    powerUpsAndBotsArising(a);
                }
                hero.takeDamage(2 * level);
            }
        }

        //столкновение астероидов и ботов
        for (int i = 0; i < asteroidController.getActiveList().size(); i++) {
            Asteroid a = asteroidController.getActiveList().get(i);
            for (int j = 0; j < botController.getActiveList().size(); j++) {
                Bot b = botController.getActiveList().get(j);

                if (b.getHitArea().overlaps(a.getHitArea())) {
                    float dst = a.getPosition().dst(b.getPosition());
                    float halfOverLen = (a.getHitArea().radius + b.getHitArea().radius - dst) / 2.0f;
                    tempVec.set(b.getPosition()).sub(a.getPosition()).nor();
                    b.getPosition().mulAdd(tempVec, halfOverLen);
                    a.getPosition().mulAdd(tempVec, -halfOverLen);

                    float sumScl = b.getHitArea().radius * 2 + a.getHitArea().radius;
                    b.getVelocity().mulAdd(tempVec, 200.0f * a.getHitArea().radius / sumScl);
                    a.getVelocity().mulAdd(tempVec, -200.0f * b.getHitArea().radius / sumScl);

                    a.takeDamage(1);
                    b.takeDamage(level);
                }
            }
        }

        //столкновение пуль и астероидов
        for (int i = 0; i < bulletController.getActiveList().size(); i++) {
            Bullet b = bulletController.getActiveList().get(i);
            for (int j = 0; j < asteroidController.getActiveList().size(); j++) {
                Asteroid a = asteroidController.getActiveList().get(j);
                if (a.getHitArea().contains(b.getPosition())) {
                    particleController.getEffectBuilder().bulletCollideWithAsteroid(b);
                    b.deactivate();
                    if (a.takeDamage(b.getOwner().getCurrentWeapon().getDamage())) {
                        if (b.getOwner().getOwnerType() == OwnerType.PLAYER) {
                            hero.addScore(a.getHpMax() * 100);
                        }
                       powerUpsAndBotsArising(a);
                    }
                    break;
                }
            }
        }

        // Столкновение поверапсов и героя
        for (int i = 0; i < powerUpsController.getActiveList().size(); i++) {
            PowerUp pu = powerUpsController.getActiveList().get(i);
            if (hero.getMagneticField().contains(pu.getPosition())) {
                tempVec.set(hero.getPosition()).sub(pu.getPosition()).nor();
                pu.getVelocity().mulAdd(tempVec, 100);
            }

            if (hero.getHitArea().contains(pu.getPosition())) {
                hero.consume(pu);
                particleController.getEffectBuilder().takePowerUpsEffect(pu);
                pu.deactivate();
            }
        }

        //столкновение пуль и кораблей
        for (int i = 0; i < bulletController.getActiveList().size(); i++) {
            Bullet b = bulletController.getActiveList().get(i);
            if (b.getOwner().getOwnerType() == OwnerType.BOT) {
                if (hero.getHitArea().contains(b.getPosition())) {
                    hero.takeDamage(b.getOwner().getCurrentWeapon().getDamage());
                    b.deactivate();
                }
            }

            if (b.getOwner().getOwnerType() == OwnerType.PLAYER) {
                for (int j = 0; j < botController.getActiveList().size(); j++) {
                    Bot bot = botController.getActiveList().get(j);
                    if (bot.getHitArea().contains(b.getPosition())) {
                        bot.takeDamage(b.getOwner().getCurrentWeapon().getDamage());
                        hero.addScore(bot.getHpMax());
                        b.deactivate();
                    }
                }
            }
        }

        // столкновение ракеты и астероиодов
        for(int i = 0; i < rocketController.getActiveList().size(); i++) {
            Rocket rocket = rocketController.getActiveList().get(i);
            for(int j = 0; j < asteroidController.getActiveList().size(); j++) {
                Asteroid asteroid = asteroidController.getActiveList().get(j);
                if(rocket.getHitArea().overlaps(asteroid.getHitArea())) {
                    rocket.getExplosionSound().play();
                    if(asteroid.takeDamage(rocket.getHitAreaDamage())){
                        hero.addScore(asteroid.getHpMax() * 100);
                        powerUpsAndBotsArising(asteroid);
                    }
                    particleController.getEffectBuilder().takeRocketExplosionEffect(rocket);
                    rocket.setShockWave(new Circle(rocket.getPosition().x, rocket.getPosition().y, 128));
                    rocket.setShockWaveDamage(50);
                    shockWaveImpact(rocket);
                    rocket.deactivate();
                }
            }
        }


        // cтолкновение ракеты и бота
        for(int i = 0; i < rocketController.getActiveList().size(); i++) {
            Rocket rocket = rocketController.getActiveList().get(i);
            for(int j = 0; j < botController.getActiveList().size(); j++) {
                Bot bot = botController.getActiveList().get(j);
                if(rocket.getHitArea().overlaps(bot.getHitArea())) {
                    rocket.getExplosionSound().play();
                    bot.takeDamage(rocket.getHitAreaDamage());
                    hero.addScore(bot.getHpMax());
                    particleController.getEffectBuilder().takeRocketExplosionEffect(rocket);
                    rocket.setShockWave(new Circle(rocket.getPosition().x, rocket.getPosition().y, 128));
                    rocket.setShockWaveDamage(50);
                    shockWaveImpact(rocket);
                    rocket.deactivate();
                }
            }
        }

        // столкновение ракеты и пуль
        for(int i = 0; i < bulletController.getActiveList().size(); i++) {
            Bullet bullet = bulletController.getActiveList().get(i);
            for(int j = 0; j < rocketController.getActiveList().size(); j++) {
                Rocket rocket = rocketController.getActiveList().get(j);
                if(rocket.getHitArea().contains(bullet.getPosition())) {
                    rocket.takeDamage(bullet.getOwner().getCurrentWeapon().getDamage());
                    bullet.deactivate();
                    if(rocket.getHp() <= 0) {
                        rocket.getExplosionSound().play();
                        particleController.getEffectBuilder().takeRocketExplosionEffect(rocket);
                        rocket.setShockWave(new Circle(rocket.getPosition().x, rocket.getPosition().y, 128));
                        rocket.setShockWaveDamage(50);
                        shockWaveImpact(rocket);
                        rocket.deactivate();
                    }
                }
            }
        }

        // столкновение героя и ракеты
//        for(int i = 0; i < rocketController.getActiveList().size(); i++) {
//            Rocket rocket = rocketController.getActiveList().get(i);
//            if(rocket.getHitArea().overlaps(hero.getHitArea())) {
//                rocket.getExplosionSound().play();
//                hero.takeDamage(rocket.getHitAreaDamage());
//                particleController.getEffectBuilder().takeRocketExplosionEffect(rocket);
//                rocket.setShockWave(new Circle(rocket.getPosition().x, rocket.getPosition().y, 128));
//                rocket.setShockWaveDamage(50);
//                shockWaveImpact(rocket);
//                rocket.deactivate();
//            }
//        }

        // столкновение ботов с друг другом
        for(int i = 0; i < botController.getActiveList().size(); i++) {
           Bot bot = botController.getActiveList().get(i);
           for(int j = 0; j < botController.getActiveList().size(); j++) {
                Bot otherBot = botController.getActiveList().get(j);
                if(bot.getHitArea().overlaps(otherBot.getHitArea())) {
                    float dst = bot.getPosition().dst(otherBot.getPosition());
                    float halfOverLen = (bot.getHitArea().radius + otherBot.getHitArea().radius - dst) / 2.0f;
                    tempVec.set(otherBot.getPosition()).sub(bot.getPosition()).nor();
                    otherBot.getPosition().mulAdd(tempVec, halfOverLen);
                    bot.getPosition().mulAdd(tempVec, -halfOverLen);

                    float sumScl = otherBot.getHitArea().radius + bot.getHitArea().radius;
                    otherBot.getVelocity().mulAdd(tempVec, 200.0f * bot.getHitArea().radius / sumScl);
                    bot.getVelocity().mulAdd(tempVec, -200.0f * otherBot.getHitArea().radius / sumScl);
                }
           }

        }


        // столкновение ботов и героя
        for(int i = 0; i < botController.getActiveList().size(); i++) {
            Bot bot = botController.getActiveList().get(i);
            if(hero.getHitArea().overlaps(bot.getHitArea())) {
                float dst = bot.getPosition().dst(hero.getPosition());
                float halfOverLen = (bot.getHitArea().radius + hero.getHitArea().radius - dst) / 2.0f;
                tempVec.set(hero.getPosition()).sub(bot.getPosition()).nor();
                hero.getPosition().mulAdd(tempVec, halfOverLen);
                bot.getPosition().mulAdd(tempVec, -halfOverLen);

                float sumScl = hero.getHitArea().radius + bot.getHitArea().radius;
                hero.getVelocity().mulAdd(tempVec, 200.0f * bot.getHitArea().radius / sumScl);
                bot.getVelocity().mulAdd(tempVec, -200.0f * hero.getHitArea().radius / sumScl);

                bot.takeDamage(10);
                hero.takeDamage(bot.getHpMax() / 10);
                hero.addScore(bot.getHpMax());
            }
        }

    }

    public void dispose() {
        background.dispose();
    }

    private void shockWaveImpact(Rocket rocket) {
        // столкновение ударной волны и астероидов
        for(int i = 0; i < asteroidController.getActiveList().size(); i++) {
            Asteroid asteroid = asteroidController.getActiveList().get(i);
            if(rocket.getShockWave().overlaps(asteroid.getHitArea()) || rocket.getShockWave().contains(asteroid.getHitArea())) {
                if(asteroid.takeDamage(rocket.getShockWaveDamage())){
                    hero.addScore(asteroid.getHpMax() * 100);
                    powerUpsAndBotsArising(asteroid);
                }
            }
        }

        // столкновение ударной волны и ботов
        for(int i = 0; i < botController.getActiveList().size(); i++) {
            Bot bot = botController.getActiveList().get(i);
            if(rocket.getShockWave().overlaps(bot.getHitArea()) || rocket.getShockWave().contains(bot.getHitArea())) {
                bot.takeDamage(rocket.getShockWaveDamage());
                hero.addScore(bot.getHpMax());
            }
        }

        // столкновение ударной волны и героя
        if(rocket.getShockWave().overlaps(hero.getHitArea()) || rocket.getShockWave().contains(hero.getHitArea())) {
            hero.takeDamage(rocket.getShockWaveDamage());
        }

    }

    private void powerUpsAndBotsArising(Asteroid asteroid) {
        for (int k = 0; k < 3; k++) {
            powerUpsController.setup(asteroid.getPosition().x, asteroid.getPosition().y, asteroid.getScale() * 0.25f);
        }
        if (MathUtils.random(0, 100) < 10 * asteroid.getScale()) {
            botController.setup(asteroid.getPosition().x, asteroid.getPosition().y);
        }
    }

}
