package com.star.app.game;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
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
    private Hero hero;
    private Vector2 tempVec;
    private Stage stage;
    private boolean pause;
    private int level;
    private float timer;
    private Music music;
    private Bot bot;

    public Bot getBot() {
        return bot;
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
        this.hero = new Hero(this);
        this.tempVec = new Vector2();
        this.stage = new Stage(ScreenManager.getInstance().getViewport(), batch);
        this.stage.addActor(hero.getShop());
        Gdx.input.setInputProcessor(stage);
        this.level = 1;
        this.bot = new Bot(this);
        generateBigAsteroids(2);

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
        powerUpsController.update(dt);
        infoController.update(dt);
        hero.update(dt);
        botBehavior(dt);
        stage.act(dt);
        checkCollisions();
        if (!hero.isAlive()) {
            ScreenManager.getInstance().changeScreen(ScreenManager.ScreenType.GAMEOVER, hero);
        }
        if (asteroidController.getActiveList().size() == 0 && !bot.isAlive()) {
            level++;
            generateBigAsteroids(level + 2);
            timer = 0;
            bot = new Bot(this);
        }
    }


    public void checkCollisions() {
        //столкновение астероидов с героем и ботом
        for (int i = 0; i < asteroidController.getActiveList().size(); i++) {
            Asteroid a = asteroidController.getActiveList().get(i);

            // столкновение астероидов и героя
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
                }
                hero.takeDamage(2 * level);
            }

           // столкновение астероидов и бота (астероид и бот отталкиваются друг от друга и не получают урона)
            if (bot.getHitArea().overlaps(a.getHitArea())) {
                float dst = a.getPosition().dst(bot.getPosition());
                float halfOverLen = (a.getHitArea().radius + bot.getHitArea().radius - dst) / 2.0f;
                tempVec.set(bot.getPosition()).sub(a.getPosition()).nor();
                bot.getPosition().mulAdd(tempVec, halfOverLen);
                a.getPosition().mulAdd(tempVec, -halfOverLen);

                float sumScl = bot.getHitArea().radius * 2 + a.getHitArea().radius;
                bot.getVelocity().mulAdd(tempVec, 200.0f * a.getHitArea().radius / sumScl);
                a.getVelocity().mulAdd(tempVec, -200.0f * bot.getHitArea().radius / sumScl);
            }
        }

        //столкновение пуль с астероидом, ботом, героем
        for (int i = 0; i < bulletController.getActiveList().size(); i++) {
            Bullet b = bulletController.getActiveList().get(i);

            // столкновение героя с пулями
            if(hero.getHitArea().contains(b.getPosition()) && b.getOwner().equals(bot)) {
                particleController.setup(b.getPosition().x + MathUtils.random(-4, 4), b.getPosition().y + MathUtils.random(-4, 4),
                        b.getVelocity().x * -0.3f + MathUtils.random(-30, 30), b.getVelocity().y * -0.3f + MathUtils.random(-30, 30),
                        0.2f,
                        2.5f, 1.2f,
                        1.0f, 1.0f, 1.0f, 1.0f,
                        0.0f, 0.1f, 1.0f, 0.0f);

                b.deactivate();
                hero.takeDamage(bot.getCurrentWeapon().getDamage());
            }

            // столкновение бота с пулями
            if(bot.getHitArea().contains(b.getPosition()) && b.getOwner().equals(hero) && bot.isAlive()) {
                particleController.setup(b.getPosition().x + MathUtils.random(-4, 4), b.getPosition().y + MathUtils.random(-4, 4),
                        b.getVelocity().x * -0.3f + MathUtils.random(-30, 30), b.getVelocity().y * -0.3f + MathUtils.random(-30, 30),
                        0.2f,
                        2.5f, 1.2f,
                        1.0f, 1.0f, 1.0f, 1.0f,
                        0.0f, 0.1f, 1.0f, 0.0f);

                b.deactivate();
                bot.takeDamage(hero.getCurrentWeapon().getDamage());
                hero.addScore(bot.getHpMax() * 50);
            }

            // столкновение астероидов с пулями
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
                    if (a.takeDamage(hero.getCurrentWeapon().getDamage())) {
                        hero.addScore(a.getHpMax() * 100);
                        for (int k = 0; k < 3; k++) {
                            powerUpsController.setup(a.getPosition().x, a.getPosition().y, a.getScale() * 0.25f);
                        }
                    }
                    break;
                }
            }
        }

        // Столкновение поверапсов, героя и бота
        for (int i = 0; i < powerUpsController.getActiveList().size(); i++) {
            PowerUp pu = powerUpsController.getActiveList().get(i);

            // столкновение поверапсов и героя
            if (hero.getMagneticField().contains(pu.getPosition())) {
                tempVec.set(hero.getPosition()).sub(pu.getPosition()).nor();
                pu.getVelocity().mulAdd(tempVec, 100);
            }
            if (hero.getHitArea().contains(pu.getPosition())) {
                hero.consume(pu);
                particleController.getEffectBuilder().takePowerUpsEffect(pu);
                pu.deactivate();
            }

            // столкновение бота с поверапсами (поверасы просто исчезают)
            if(bot.getHitArea().contains(pu.getPosition())) {
                particleController.getEffectBuilder().takePowerUpsEffect(pu);
                pu.deactivate();
            }
        }

        // столкновение бота и героя
        if(hero.getHitArea().overlaps(bot.getHitArea()) && bot.isAlive()) {
            hero.takeDamage(10 + level);
            bot.takeDamage(10);
            hero.addScore(50 + 10 * level);
            float dst = bot.getPosition().dst(hero.getPosition());
            float halfOverLen = (bot.getHitArea().radius + hero.getHitArea().radius - dst) / 2.0f;
            tempVec.set(hero.getPosition()).sub(bot.getPosition()).nor();
            hero.getPosition().mulAdd(tempVec, halfOverLen);
            bot.getPosition().mulAdd(tempVec, -halfOverLen);

            float sumScl = hero.getHitArea().radius * 2 + bot.getHitArea().radius * 2;
            hero.getVelocity().mulAdd(tempVec, 200.0f * bot.getHitArea().radius / sumScl);
            bot.getVelocity().mulAdd(tempVec, -200.0f * hero.getHitArea().radius / sumScl);
        }

    }

    public void botBehavior(float dt) {
        bot.update(dt);
        tempVec.set(hero.getPosition()).sub(bot.getPosition()).nor();
        bot.setAngle(tempVec.angleDeg());
        float dst = bot.getPosition().dst(hero.getPosition());
        if(dst > 150.0f) {
            bot.setMoving(true);
            bot.getVelocity().mulAdd(tempVec, bot.getEnginePower() * dt);
        } else {
            bot.setMoving(false);
            bot.getVelocity().mulAdd(tempVec, 0);
       }
        if(dst < 300.0f) {
            bot.tryToFire();
        }

    }

    public void dispose() {
        background.dispose();
    }

}
