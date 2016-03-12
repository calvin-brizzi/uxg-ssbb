package com.ssbb.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;

/**
 * A simple power up, just restores life for now, but could be improved
 * Created by Calvin on 2014/09/28.
 */
public class PowerUp extends Collidable {
    Player player;
    Sound powerSound;

    public PowerUp(String name, Player player) {
        super(name);
        this.player = player;
        powerSound  = Gdx.audio.newSound(Gdx.files.internal("power.ogg"));
    }

    public void get() {
        powerSound.play();
        player.life++;
        // Marks itself as removable
        dead = true;
    }
}
