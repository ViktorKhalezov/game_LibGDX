package com.star.app.game;


public class GameController {
    private Background background;
    private BulletController bulletController;
    private Hero hero;
  //  private Asteroid asteroid;


    public GameController() {
        this.background = new Background(this);
        this.hero = new Hero(this);
        this.bulletController = new BulletController();
 //       this.asteroid = new Asteroid();
    }

    public Background getBackground() {
        return background;
    }

    public Hero getHero() {
        return hero;
    }

    public BulletController getBulletController() {
        return bulletController;
    }

    //    public Asteroid getAsteroid() {
//        return asteroid;
//    }

    public void update(float dt){
        background.update(dt);
        bulletController.update(dt);
        hero.update(dt);
   //     asteroid.update(dt);
        checkCollisions();
    }


    public void checkCollisions() {
        for(int i = 0; i < bulletController.getActiveList().size(); i++) {
            Bullet b = bulletController.getActiveList().get(i);

            if(hero.getPosition().dst(b.getPosition()) < 32.0f) {
                // b.deactivate()
                // астероид deactivate()
            }
        }
    }

}
