package com.lira.speedfreak;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

/**
 * Created by llira18 on 2/15/2017.
 */
public class Enemy {
    protected Array<Enemy> enemies;
    public float x;
    public float y;
    public static final float COLLISION_WIDTH_ENEMY = 80;
    public static final float COLLISION_HEIGHT_ENEMY = 80;
    protected Rectangle collisionRect;
    protected float speed = 100f;
    private Texture enemyImage;
    public Enemy(float x, float y) {
        this.x = x;
        this.y = y;
        collisionRect = new Rectangle(x,
                y,
                COLLISION_WIDTH_ENEMY,
                COLLISION_HEIGHT_ENEMY);
       // enemyImage = new Texture("");
    }

    public void setPosition(float x, float y) {
        this.x = x;
        this.y = y;
        updateEnemyCollisionRectangle();
    }



    public void updatePosition (float x, float y) {
        this.x = x;
        this.y = y;
        updateEnemyCollisionRectangle();
    }
    public void updateEnemyCollisionRectangle() {
        collisionRect.setPosition(x, y);
    }


    public void drawDebug(ShapeRenderer shapeRenderer) {
        shapeRenderer.rect(collisionRect.x,
                collisionRect.y,
                collisionRect.width,
                collisionRect.height);
    }

    public boolean isPlayerColliding(Player player) {
        Rectangle playerCollisionRectangle = player.getCollisionRectangle();
        return Intersector.overlaps(playerCollisionRectangle,getEnemyCollisionRectangle() );

    }


    public Rectangle getEnemyCollisionRectangle() {
        return collisionRect;
    }


    public void update(float delta) {
        setPosition(150,150);
    }


    public void draw(SpriteBatch spriteBatch){
        //batch.draw(toDraw, x, y);
    }

    public void checkForhit(Bullet b) {
        for (int i=0; i < enemies.size; i++) {
           // if (enemies.get(i).isHit(b)) {
                enemies.removeIndex(i);
                i--;
                b.setAlive(false);
            }
        }
    }
    //do later
    //public void isHit(Bullet b) {
        //return Intersector.overlaps (collisionRect,
               // b.getCollision_circle());

    //}

