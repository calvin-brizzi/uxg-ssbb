package com.ssbb.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.Rectangle;

/**
 * A simple power up, just restores life for now, but could be improved
 * Created by Calvin on 2014/09/28.
 */
public class Tutorial extends Collidable {
    SquishyBlock game;
    Rectangle tt;

    public Tutorial(String name, SquishyBlock g, Rectangle tt) {
        super(name);
        game = g;
        this.tt = tt;
    }

    public void get() {
        game.tuttime();
        // Marks itself as removable
        dead = true;
    }
}
