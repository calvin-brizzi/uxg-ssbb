package com.ssbb.game;

/**
 * An enemy that crawls
 * Created by calvin on 2014/09/26.
 */
public class GroundEnemy extends Collidable {

    Player player;
    int speed = 5;

    public GroundEnemy(String name, Player player) {
        super(name);
        this.player = player;
    }

    public void update() {
        // Head for player if they are close enough
        int xd = x - player.x;

        if (xd < 1000) {
            if (xd > 0) {
                x = x - speed;
            } else {
                x = x + speed;
            }
        }
    }
}
