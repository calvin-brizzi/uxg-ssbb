package com.ssbb.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;

/**
 * A object to keep things that can collide
 * Created by calvin on 2014/09/05.
 */
public class Collidable {
    // Position and Speed
    public int x, y;
    public int ax, ay;
    public int direction;

    // Sprite and some useful booleans
    public Sprite sprite;
    public boolean colliding;
    public boolean grounded;
    public boolean dead;

    public Collidable(String name) {
        // name is the filename, and will setup the sprite
        if(name.length() > 0){
            sprite = new Sprite(new Texture(name));
        }
    }

    public void flip(boolean t) {
        sprite.setFlip(t, false);
        if (t) {
            direction = -1;
        } else {
            direction = 1;
        }
    }

    public void resolve(Rectangle r) {
        // Resolves a collision with another Rectangle
        Rectangle pr = sprite.getBoundingRectangle();
        Rectangle intersection = new Rectangle();

        // Find the size of the intersection
        Intersector.intersectRectangles(pr, r, intersection);

        if (intersection.width > intersection.height + 10) {
            // y direction smaller
            solveY(intersection);
            ay = 0;
        } else {
            // x direction smaller
            if (intersection.height < 20 && pr.y == intersection.y) {
                y += intersection.height;
            }
            solveX(intersection);
            ax = 0;
        }

        sprite.setPosition(x, y);
    }

    private void solveY(Rectangle intersection) {

        Rectangle pr = sprite.getBoundingRectangle();

        if (pr.y != intersection.y) {
            y -= intersection.height;
        } else {
            y += intersection.height;
            grounded = true;
        }
    }

    private void solveX(Rectangle intersection) {

        Rectangle pr = sprite.getBoundingRectangle();

        if (pr.x == intersection.x) {
            x += intersection.width;
        } else {
            x -= intersection.width;
        }
    }

    public void update() {
        // Blank for object that don't move
        // objects that do override this
    }
}
