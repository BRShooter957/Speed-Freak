package com.lira.speedfreak;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

/**
 * Created by llira18 on 2/28/2017.
 */
public class LoadingScreen implements Screen {

    private static final float WORLD_WIDTH = 1280;
    private static final float WORLD_HEIGHT= 720;
    private static final float PROGRESS_BAR_WIDTH = 100;
    private static final float PROGRESS_BAR_HEIGHT = 25;
    private ShapeRenderer shapeRenderer;
    private Viewport viewport;
    private OrthographicCamera camera;
    private float progress = 0;
    private MyGdxGame game;
    public Texture BulletImage;


    public LoadingScreen(MyGdxGame myGdxGame) {
        game = myGdxGame;
    }

    @Override
    public void show() {
        camera = new OrthographicCamera();

        //set world coordinate
        camera.position.set(WORLD_WIDTH/2,WORLD_HEIGHT/2,0);
        camera.update();


        viewport = new FitViewport(WORLD_WIDTH,WORLD_HEIGHT,camera);
        viewport.apply(true);

        shapeRenderer = new ShapeRenderer();
        shapeRenderer.setAutoShapeType(true);

        game.getAssetManager().load("stage 1.tmx", TiledMap.class);
        game.getAssetManager().load("Bullet right.png", Texture.class);
        game.getAssetManager().load("EnemyImage.png", Texture.class);
    }
    private void clearScreen() {
        Gdx.gl.glClearColor(0,0,.3f,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }



    @Override
    public void render(float delta) {
        clearScreen();
        update();


    }

    private void update() {
        if (game.getAssetManager().update()) {
            game.setScreen(new GameplayScreen(game));
        }
        else {
            progress = game.getAssetManager().getProgress();
        }
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
