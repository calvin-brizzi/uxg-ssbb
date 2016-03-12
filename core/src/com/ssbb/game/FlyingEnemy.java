package com.ssbb.game;

/**
 * A flying enemy!
 * Created by Calvin on 2014/09/26.
 */
public class FlyingEnemy extends Collidable {

    Player player;
    PathFinder aStar;
    State state = State.WAITING;

    // Our simple state machine
    static enum State {
        FINDING,
        CHASING,
        WAITING,
    }

    public FlyingEnemy(String name, Player player, PathFinder aStar) {
        super(name);
        this.player = player;
        this.aStar = aStar;
    }

    public void update() {
        int objectiveX = player.x ;
        int objectiveY = player.y;
        int ySpeed = 7;
        int xSpeed = 1;
        int dtp = (objectiveX- this.x) * (objectiveX- this.x) + (objectiveY- this.y) * (objectiveY- this.y);
        if (dtp < 4096) {
            // We're close enough to go direct
            state = State.CHASING;
        } else if (dtp < 1200000) {
            // Use A*
            state = State.FINDING;
        }

        switch (state) {
            case FINDING:
                // Get the next block
                int[] next = aStar.nextMove(this.sprite, player.sprite);
                objectiveX = next[0] * 64 + 10 ;
                objectiveY = next[1] * 64 + 10;
                ySpeed = 7;
                xSpeed = 1;

                break;
            case WAITING:
                // Chillax
                xSpeed = 0;
                ySpeed = 0;

        }
        if (Math.abs(objectiveX - x) > Math.abs(objectiveY - y) && xSpeed > 0) {
            // Where to dedicate more speed
            xSpeed = 7;
            ySpeed = 1;
        }

        // Movement
        if (x - objectiveX > 0) {
            x -= xSpeed;

        } else if (x - objectiveX < 0) {
            x += xSpeed;
        }

        if (y - objectiveY > 0) {
            y -= ySpeed;

        } else if (y - objectiveY < 0) {
            y += ySpeed;
        }
    }
}
