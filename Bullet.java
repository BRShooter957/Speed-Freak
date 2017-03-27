package com.lira.speedfreak;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;

public class Bullet {
    private float x;
    private float y;
    private static final float COLLISION_RADIUS =5f;
    private Circle collision_circle;
    private float xvel;
    private float yvel;
    private boolean alive = true;

    public Bullet(float x, float y) {
        this.x = x;
        this.y = y;
        collision_circle = new Circle(x,
                y,
                COLLISION_RADIUS);
    }

    public void setVelocity(float xvel, float yvel) {
        this.xvel = xvel * 10;
        this.yvel = yvel;
    }

    public void update(float delta) {
        x += xvel * delta;
        y += yvel * delta;
        collision_circle.setPosition(x,y);
    }

    public void drawDebug(ShapeRenderer shapeRenderer) {
        shapeRenderer.circle(collision_circle.x,
                collision_circle.y,
                collision_circle.radius);
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getDiameter() {
        return COLLISION_RADIUS*2;
    }

    public Circle getCollision_circle() {
        return collision_circle;
    }

    public boolean isAlive() {
        return alive;
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }
}
