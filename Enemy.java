package com.lira.speedfreak;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.assets.loaders.resolvers.AbsoluteFileHandleResolver;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
/**
 * Created by llira18 on 2/15/2017.
 */
public class Enemy {
    public float x;
    public float y;
    public static final float COLLISION_WIDTH_ENEMY = 80;
    public static final float COLLISION_HEIGHT_ENEMY = 80;
    protected Rectangle COLLISION_RECT_ENEMY;
    protected float speed = 100f;
    protected static long shootdelay = 700;

    public Enemy(float x, float y) {
        x = 110;
        y = 110;
        this.x = x;
        this.y = y;
        COLLISION_RECT_ENEMY = new Rectangle(x,
                y,
                COLLISION_WIDTH_ENEMY,
                COLLISION_HEIGHT_ENEMY);

    }
    public void updatePosition (float x, float y) {
        this.x = x;
        this.y = y;
        updateEnemyCollisionRectangle();
    }
    public void updateEnemyCollisionRectangle() {
        COLLISION_RECT_ENEMY.setPosition(x, y);
    }
    public void drawDebug(ShapeRenderer shapeRenderer) {
        shapeRenderer.rect(COLLISION_RECT_ENEMY.x,
                COLLISION_RECT_ENEMY.y,
                COLLISION_RECT_ENEMY.width,
                COLLISION_RECT_ENEMY.height);
    }
    public Rectangle getEnemyCollisionRectangle() {
        return COLLISION_RECT_ENEMY;
    }
    public void update(float delta) {

    }
    public void draw(SpriteBatch spriteBatch){
       // batch.draw(toDraw, x, y);
    }
}
