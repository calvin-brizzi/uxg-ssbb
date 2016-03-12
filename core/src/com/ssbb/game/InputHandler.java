package com.ssbb.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;

/**
 * All our controls in one nice place
 * Created by calvin on 2014/09/24.
 */
public class InputHandler {
    Sound jump;

    public InputHandler(){
        jump = Gdx.audio.newSound(Gdx.files.internal("jump.ogg"));
    }
    public void update(Player player, Block block) {

        // Player controls
        if (Gdx.input.isKeyPressed(Input.Keys.D) && !player.colliding && !player.hit) {
            player.flip(false);
            player.ax = 10;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.A) && !player.colliding && !player.hit) {
            player.flip(true);
            player.ax = -10;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.SPACE) && !player.colliding && !player.hit) {
            if (player.grounded) {
                player.ay += 22;
                player.grounded = false;
                jump.play();
            }
        }

        // Tetronimo commands
        if (Gdx.input.isKeyPressed(Input.Keys.UP) && !block.dropping && !block.rising) {
            block.y += 10;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN) && !block.dropping && !block.rising) {
            block.y -= 10;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.LEFT) && !block.dropping && !block.rising) {
            block.x -= 10;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) && !block.dropping && !block.rising) {
            block.x += 10;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.CONTROL_RIGHT) && !block.dropping && block.canDrop) {

            block.dropping = true;
            block.canDrop = false;
        }
    }
}
