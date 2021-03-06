package com.lira.speedfreak;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;

import static sun.audio.AudioPlayer.player;

public class Player {
    public static final float COLLISION_WIDTH = 40;
    public static final float COLLISION_HEIGHT = 80;
    private static final float MAX_X_SPEED = 8;
    private static final float MAX_Y_SPEED = 3;
    private static final float MAX_JUMP_DISTANCE = 1.5f * COLLISION_HEIGHT;
    private Rectangle collisionRectangle;
    private float x;
    private float y;
    private float xSpeed;
    private float ySpeed;
    private boolean blockJump = false;
    private float jumpYDistance = 0;
    private Animation walking;
    private TextureRegion jumping;
    private TextureRegion lookingLeft;
    private TextureRegion lookingRight;
    private float animationTimer = 0;
    private boolean amDucking = false;
    public static final int LEFT = 1, RIGHT = 2;
    private int dir = RIGHT;
    private Player player;
    //private Rectangle collisionRect;
    //private float speed = 6;
    private static final int SINGLE = 1;
    private int currentWeapon = SINGLE;
    private long shootDelay = 500; //1000 = 1 second
    private long lastShot;
    public float launch;
    public float bulletCount = 2;

    //Running animation (RL = running left) (RR = running right)
    private TextureRegion leftImageRL;
    private TextureRegion middleImageRL;
    private TextureRegion rightImageRL;
    private TextureRegion leftImageRR;
    private TextureRegion middleImageRR;
    private TextureRegion rightImageRR;

    //Idle image (FL = facing left) (FR = facing right)
    private TextureRegion IdleFL;
    private TextureRegion IdleFR;


    public Player(float x, float y, Texture t) {
        x = 110;
        y = 110;
        this.x = x;
        this.y = y;
        collisionRectangle = new Rectangle(x, y,
                COLLISION_WIDTH,
                COLLISION_HEIGHT);
        Texture spriteSheet = new Texture("IdleRight.png");

        TextureRegion[] regions = TextureRegion.split(t, (int) COLLISION_WIDTH,
                (int) COLLISION_HEIGHT)[0];
        walking = new Animation(0.25f, regions[1], regions[2]);
        walking.setPlayMode(Animation.PlayMode.LOOP);
        lookingLeft= regions[0];
        lookingRight = regions[3];
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


    public void restartLevel() {
        updatePosition(110, 110);
    }

    public void update(float delta) {
        animationTimer += delta;
        Input input = Gdx.input;

        if (input.isKeyJustPressed(Input.Keys.R)) {
            restartLevel();
        }

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

    public void launch() {
        blockJump = false;
        jumpYDistance = 8;
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
        //TextureRegion toDraw = lookingRight;

        //if (xSpeed == 0 && amDucking) {
            //toDraw = jumping;
        }

        //if (ySpeed != 0) {
            //toDraw = jumping;
        //} else if (xSpeed > 0) {
          //  toDraw = walking.getKeyFrame(animationTimer);
          //  if (toDraw.isFlipX())
                //toDraw.flip(true, false);
        //} else if (xSpeed < 0) {
          //  toDraw = walking.getKeyFrame(animationTimer);
          //  if (!toDraw.isFlipX())
          //      toDraw.flip(true, false);
        //}

        //batch.draw(toDraw, x, y);
    //}
    public void subtractBullets(){
    }

    public Rectangle getCollisionRectangle() {
        return collisionRectangle;
    }

    public void shoot(Array<Bullet> bullets) {
        if (lastShot + shootDelay < System.currentTimeMillis()) {
            lastShot = System.currentTimeMillis();
            Bullet b = new Bullet(x + COLLISION_WIDTH,
                    y + COLLISION_HEIGHT - 20);

            if (dir == RIGHT) {
                b.setVelocity(150, 0);
                b.setPosition(x + COLLISION_WIDTH, y + COLLISION_HEIGHT - 30);
            } else {
                b.setVelocity(-150, 0);
                b.setPosition(x, y + COLLISION_HEIGHT - 30);
            }

            bullets.add(b);


        }

    }

}
