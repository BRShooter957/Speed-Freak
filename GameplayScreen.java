package com.lira.speedfreak;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.sun.javafx.geom.Vec2d;

import java.util.Iterator;

public class GameplayScreen implements Screen {

    private static final float WORLD_WIDTH = 1280;
    private static final float WORLD_HEIGHT = 720;
    private static final float CELL_SIZE = 80;
    private SpriteBatch batch; //draw graphics
    private ShapeRenderer shapeRenderer; //draw shapes
    private OrthographicCamera camera; //the players view of the world
    private Viewport viewport; //control the view of the world
    public static MyGdxGame game;
    private TiledMap stage1;
    private OrthogonalTiledMapRenderer mapRenderer;//2d map renderer
    Array<Bullet> playerBullets = new Array<Bullet>();
    private Player player;
    private Array<Enemy> enemies = new Array<Enemy>();
    private float timeCount;
    private Integer worldTimer;
    private Label countdownLabel;
    // float bulletcirclesX = 135;
    //float bulletcirclesY = 135;
    private BitmapFont countdownTimer;

    public GameplayScreen(MyGdxGame myGdxGame) {


        game = myGdxGame;

    }


    @Override
    public void show() {
        camera = new OrthographicCamera(); //2D camera
        camera.position.set(WORLD_WIDTH / 2, WORLD_HEIGHT / 2, 0);
        camera.update();
        viewport = new FitViewport(WORLD_WIDTH, WORLD_HEIGHT, camera);
        viewport.apply(true);
        shapeRenderer = new ShapeRenderer();
        shapeRenderer.setAutoShapeType(true);
        batch = new SpriteBatch();
        stage1 = game.getAssetManager().get("stage 1.tmx");
        mapRenderer = new OrthogonalTiledMapRenderer(stage1, batch);
        mapRenderer.setView(camera);
        player = new Player(200, 100, (Texture) game.getAssetManager().get("tileset spreadsheet.png"));
        // timer
        enemies.add(new Enemy(2080, 320));
        enemies.add(new Enemy(4960, 80));
        enemies.add(new Enemy(2480, 1600));
        countdownTimer = new BitmapFont(Gdx.files.internal("timer_font.fnt"));
    }

    @Override
    public void render(float delta) {
        clearScreen();
        update(delta);
        player.update(delta);
        getUserInput();
        handlePlayerCollision();

        camera.position.set(player.getX(), player.getY(), 0);
        camera.update();

        mapRenderer.setView(camera);
        mapRenderer.render();


        //batch.setProjectionMatrix(camera.projection);
        //batch.setTransformMatrix(camera.view);

        Vector3 screenCoords = new Vector3(20, 20, 0);
        Vector3 worldCoords = camera.unproject(screenCoords);

        //all graphics drawing goes here
        batch.begin();
        //player.draw(batch);
//        countdownTimer.draw(batch, "Countdown:" + timeCount,225,600);
        //  enemy.draw(batch);
        for (int i = 0; i < playerBullets.size; i++) {
            playerBullets.get(i).draw(batch);
        }
        countdownTimer.draw(batch, "" + (50 - timeCount), worldCoords.x, worldCoords.y);
        batch.end();

        shapeRenderer.setProjectionMatrix(camera.projection);
        shapeRenderer.setTransformMatrix(camera.view);
        //all graphics drawing goes here
        shapeRenderer.begin();

        player.drawDebug(shapeRenderer);
        for (int i = 0; i < playerBullets.size; i++) {
            playerBullets.get(i).drawDebug(shapeRenderer);
        }
        for (int i = 0; i < enemies.size; i++) {
            enemies.get(i).drawDebug(shapeRenderer);
        }
        //player.drawDebug(shapeRenderer);
        //  enemy.drawDebug(shapeRenderer);
        shapeRenderer.end();


    }


    private void removeBulletsOffScreen() {
        for (int i = 0; i < playerBullets.size; i++) {
            Bullet b = playerBullets.get(i);

            if (b.getY() > WORLD_HEIGHT+2160) {
                playerBullets.removeIndex(i);
                i--;
            } else if (b.getY() + b.getDiameter() < 0) {
                playerBullets.removeIndex(i);
                i--;
            } else if (b.getX() + b.getDiameter() < 0) {
                playerBullets.removeIndex(i);
                i--;
            } else if (b.getX() > WORLD_WIDTH+3840) {
                playerBullets.removeIndex(i);
                i--;
            } else if (b.isAlive() == false) {
                playerBullets.removeIndex(i);
                i--;
            }
        }
    }

    private void clearScreen() {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }

    private void update(float delta) {

        for (int i = 0; i < playerBullets.size; i++) {
            playerBullets.get(i).update(delta);
        }

        for (int i = 0; i < playerBullets.size; i++) {
            playerBullets.get(i).update(delta);
        }
        player.update(delta);

        if (checkForCollision()) {
            //crashSound.play();
            restartLevel();
            //game.setScreen(new startScreen(game));
        }

            timeCount += delta;
            if (timeCount >= 50) {
                //countdownLabel.setText(String.format("%03d" , worldTimer));
                player.restartLevel();
                timeCount = 0;
            }

            removeBulletsOffScreen();
        }


        @Override
        public void resize ( int width, int height){
            viewport.update(width, height);
        }

        @Override
        public void pause () {

        }

        @Override
        public void resume () {

        }

        @Override
        public void hide () {

        }

        @Override
        public void dispose () {

        }

    private void getUserInput() {
        if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
            player.shoot(playerBullets);

        }
    }

    public boolean checkForCollision() {
        for (int i = 0; i < enemies.size; i++) {
            if (enemies.get(i).isPlayerColliding(player)) {
                return true;
            }
        }
        return false;
    }

    public Array<CollisionCell> whichCellsDoesPlayerCover() {
        float x = player.getX();
        float y = player.getY();
        Array<CollisionCell> cellsCovered = new Array<CollisionCell>();
        float cellRow = x / CELL_SIZE;
        float cellCol = y / CELL_SIZE;

        int bottomLeftCellRow = MathUtils.floor(cellRow);
        int bottomLeftCellCol = MathUtils.floor(cellCol);

        TiledMapTileLayer tiledMapTileLayer = (TiledMapTileLayer) stage1.getLayers().get(0);

        cellsCovered.add(new CollisionCell(tiledMapTileLayer.getCell(bottomLeftCellRow,
                bottomLeftCellCol), bottomLeftCellRow, bottomLeftCellCol));

        if (cellRow % 1 != 0 && cellCol % 1 != 0) {
            int topRightCellRow = bottomLeftCellRow + 1;
            int topRightCellCol = bottomLeftCellCol + 1;
            cellsCovered.add(new CollisionCell(tiledMapTileLayer.getCell(topRightCellRow,
                    topRightCellCol), topRightCellRow, topRightCellCol));
        }
        if (cellRow % 1 != 0) {
            int bottomRightCellRow = bottomLeftCellRow + 1;
            int bottomRightCellCol = bottomLeftCellCol;
            cellsCovered.add(new CollisionCell(tiledMapTileLayer.getCell(bottomRightCellRow,
                    bottomRightCellCol), bottomRightCellRow, bottomRightCellCol));

        }
        if (cellCol % 1 != 0) {
            int topLeftCellRow = bottomLeftCellRow;
            int topLeftCellCol = bottomLeftCellCol + 1;
            cellsCovered.add(new CollisionCell(tiledMapTileLayer.getCell(topLeftCellRow,
                    topLeftCellCol), topLeftCellRow, topLeftCellCol));
        }

        return cellsCovered;
    }

    private Array<CollisionCell> filterOutNonCollisionCells(Array<CollisionCell> cells) {
        for (Iterator<CollisionCell> iter = cells.iterator(); iter.hasNext(); ) {
            CollisionCell collisionCell = iter.next();

            if (collisionCell.isEmpty()) {
                iter.remove();
            } else if (collisionCell.getId() == 3) {
                iter.remove();
            } else if (collisionCell.getId() == 33) {
                iter.remove();
            } else if (collisionCell.getId() == 34) {
                iter.remove();
            } else if (collisionCell.getId() == 35) {
                iter.remove();
            } else if (collisionCell.getId() == 36) {
                iter.remove();
            } else if (collisionCell.getId() == 1) {
                restartLevel();
            } else if (collisionCell.getId() == 132) {
                player.launch();
            } else if (collisionCell.getId() == 133) {
                player.launch();
            }

        }

        return cells;
    }

    private void restartLevel() {
        player.updatePosition(200, 230);
        timeCount = 0;
    }


    public void handlePlayerCollision() {
        Array<CollisionCell> playerCells = whichCellsDoesPlayerCover();
        playerCells = filterOutNonCollisionCells(playerCells);
        for (CollisionCell cell : playerCells) {
            float cellLevelX = cell.getCellRow() * CELL_SIZE;
            float cellLevelY = cell.getCellCol() * CELL_SIZE;
            Rectangle intersection = new Rectangle();
            Intersector.intersectRectangles(player.getCollisionRectangle(),
                    new Rectangle(cellLevelX, cellLevelY, CELL_SIZE, CELL_SIZE),
                    intersection);
            if (intersection.getHeight() < intersection.getWidth()) {

                if (intersection.getY() == player.getY()) {
                    player.updatePosition(player.getX(), intersection.getY() + intersection.getHeight());
                    player.landed();
                }
                if (intersection.getY() > player.getY()) {
                    player.updatePosition(player.getX(), intersection.getY() - player.COLLISION_HEIGHT);
                }
            } else if (intersection.getWidth() < intersection.getHeight()) {
                if (intersection.getX() == player.getX()) {
                    player.updatePosition(intersection.getX() + intersection.getWidth(),
                            player.getY());
                }
                if (intersection.getX() > player.getX()) {
                    player.updatePosition(intersection.getX() - player.COLLISION_WIDTH,
                            player.getY());
                }
            }
        }
    }

    private void checkForEnemyCollision() {
        //for every player bullet
        for (int i=0; i < playerBullets.size; i++) {
            //for revery enemy wave
            for (int j=0; j < enemies.size; j++) {
                enemies.get(j).checkForhit(playerBullets.get(i));
            }
        }
    }




}
