package com.star.app.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.star.app.game.GameController;
import com.star.app.game.WorldRenderer;
import com.star.app.screen.utils.Assets;

public class GameScreen extends AbstractScreen {

    private GameController gc;
    private WorldRenderer worldRenderer;
    private Stage stage;
    private BitmapFont font24;
    private boolean pause;

    public GameScreen(SpriteBatch batch) {
        super(batch);
    }

    @Override
    public void show() {
        Assets.getInstance().loadAssets(ScreenManager.ScreenType.GAME);
        this.gc = new GameController();
        this.worldRenderer = new WorldRenderer(gc, batch);
        this.stage = new Stage(ScreenManager.getInstance().getViewport(), batch);
        this.font24 = Assets.getInstance().getAssetManager().get("fonts/font24.ttf");
        this.pause = false;

        Gdx.input.setInputProcessor(stage);

        Skin skin = new Skin();
        skin.addRegions(Assets.getInstance().getAtlas());

        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.up= skin.getDrawable("simpleButton");
        textButtonStyle.font = font24;
        skin.add("simpleSkin", textButtonStyle);

        Button btnPause = new TextButton("Pause", textButtonStyle);

        btnPause.setPosition(950, 630);

        btnPause.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if(!pause) {
                    pause = true;
                } else {
                    pause = false;
                }
            }
        });

        stage.addActor(btnPause);
        skin.dispose();

    }


    public void update(float dt) {
          stage.act(dt);
    }


    @Override
    public void render(float delta) {
            worldRenderer.render();
            stage.draw();
            if(!pause) {
                update(delta);
                gc.update(delta);
            }
    }

    public GameController getGameController() {
        return gc;
    }
}
