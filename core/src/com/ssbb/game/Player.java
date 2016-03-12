package com.ssbb.game;

/**
 * Our Hero!
 * Created by calvin on 2014/09/26.
 */
public class Player extends Collidable {
    SquishyBlock game;
    public int life;
    public int lives;
    boolean hit;
    int timer;

    public Player(String name, SquishyBlock game) {
        super(name);
        direction = 1;
        this.game = game;
        life = 10;
        lives = 3;
    }

    public void update() {
        // Life stuff
        if (life > 10) {
            life = 10;
        } else if (life < 1) {
            dead = true;
            game.playerDead();
        }

        // Disable controls when hit
        if (hit) {
            timer++;
            if (timer > 15) {
                hit = false;
            }
        }

        // Player movement
        ay -= SquishyBlock.GRAVITY;
        if(ay < -60){
            ay = -60;
        }
        y += ay;
        if (y < 64) {
            y = 64;
            ay = 0;
            grounded = true;
        }
        if (ax > 0) {
            ax -= SquishyBlock.FRICTION;
        } else if (ax < 0) {
            ax += SquishyBlock.FRICTION;
        }
        x += ax;

        // Keep player on screen
        if (x < 0) {
            x = 0;
        }
        while (x > game.mapWidth - this.sprite.getWidth()) {
            x--;
        }
    }

    public void hit() {
        // Ouch! If hit, get thrown in the opposite direction
        hit = true;
        timer = 0;
        if (ax < 0) {
            ax = 25;
            ay = 10;
        } else {
            ax = -25;
            ay = 10;
        }
        x += ax;
    }


}
