package com.lira.speedfreak;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;

public class Player {
    public static final float COLLISION_WIDTH = 40;
    public static final float COLLISION_HEIGHT = 80;
    private static final float MAX_X_SPEED = 10;
    private static final float MAX_Y_SPEED = 5;
    private static final float MAX_JUMP_DISTANCE = 8 * COLLISION_HEIGHT;
    private Rectangle collisionRectangle;
    private float x;
    private float y;
    private float xSpeed;
    private float ySpeed;
    private boolean blockJump = false;
    private float jumpYDistance = 0;
    private Animation walking;
    private TextureRegion jumping;
    private TextureRegion standing;
    private TextureRegion ducking;
    private float animationTimer = 0;
    private boolean amDucking = false;
    public static final int LEFT = 1, RIGHT = 2;
    private int dir = RIGHT;

    private static float COLLISION_RECT_WIDTH = 30;
    private static float COLLISION_RECT_HEIGHT = 30;
    //private Rectangle collisionRect;
    //private float speed = 6;
    private static final int SINGLE = 1, DOUBLE = 2;
    private int currentWeapon = SINGLE;
    private long shootDelay = 300; //1000 = 1 second
    private long lastShot;


    public Player(float x, float y, Texture t) {
        x = 110;
        y = 110;
        this.x = x;
        this.y = y;
        collisionRectangle = new Rectangle(x, y,
                COLLISION_WIDTH,
                COLLISION_HEIGHT);
        TextureRegion[] regions = TextureRegion.split(t, (int) COLLISION_WIDTH,
                (int) COLLISION_HEIGHT)[0];
        walking = new Animation(0.25f, regions[1], regions[2]);
        walking.setPlayMode(Animation.PlayMode.LOOP);
        standing = regions[0];
        ducking = regions[3];
        jumping = regions[4];
    }

    public void updatePosition(float x, float y) {
        this.x = x;
        this.y = y;
        updateCollisionRectangle();
    }

    public void updateCollisionRectangle() {
        collisionRectangle.setPosition(x, y);
    }

    public void update(float delta) {
        animationTimer += delta;
        Input input = Gdx.input;
        //right
        if (input.isKeyPressed(Input.Keys.RIGHT)) {
            xSpeed = MAX_X_SPEED;
            dir = RIGHT;
        }
        //left
        else if (input.isKeyPressed(Input.Keys.LEFT)) {
            xSpeed = -MAX_X_SPEED;
            dir = LEFT;
        }
        //no x-movement
        else {
            xSpeed = 0;
        }

        //duck
        if (input.isKeyPressed(Input.Keys.DOWN)) {
            amDucking = true;
        } else {
            amDucking = false;
        }

        //jump
        if (input.isKeyPressed(Input.Keys.UP) && !blockJump) {
            ySpeed = MAX_Y_SPEED;
            jumpYDistance += ySpeed;
            blockJump = jumpYDistance > MAX_JUMP_DISTANCE;
        }
        //he's not jumping
        else {
            ySpeed = -MAX_Y_SPEED;
            blockJump = jumpYDistance > 0;
        }

        x += xSpeed;
        y += ySpeed;
        updateCollisionRectangle();
    }


    public void landed() {
        blockJump = false;
        jumpYDistance = 0;
        ySpeed = 0;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public void drawDebug(ShapeRenderer shapeRenderer) {
        shapeRenderer.rect(collisionRectangle.x,
                collisionRectangle.y,
                collisionRectangle.width,
                collisionRectangle.height);
    }

    public void draw(SpriteBatch batch) {
        TextureRegion toDraw = standing;

        if (xSpeed == 0 && amDucking) {
            toDraw = ducking;
        }

        if (ySpeed != 0) {
            toDraw = jumping;
        } else if (xSpeed > 0) {
            toDraw = walking.getKeyFrame(animationTimer);
            if (toDraw.isFlipX())
                toDraw.flip(true, false);
        } else if (xSpeed < 0) {
            toDraw = walking.getKeyFrame(animationTimer);
            if (!toDraw.isFlipX())
                toDraw.flip(true, false);
        }

        batch.draw(toDraw, x, y);
    }

    public Rectangle getCollisionRectangle() {
        return collisionRectangle;
    }

    public void shoot(Array<Bullet> bullets) {
        if (lastShot + shootDelay < System.currentTimeMillis()) {
            lastShot = System.currentTimeMillis();
            Bullet b = new Bullet(x + COLLISION_WIDTH,
                    y + COLLISION_HEIGHT - 20);
            if(dir == RIGHT)
                b.setVelocity(200, 0);
            else
                b.setVelocity(-200, 0);

            bullets.add(b);

        }
    }


}
