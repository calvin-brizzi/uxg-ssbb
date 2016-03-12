package com.ssbb.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.Rectangle;

/**
 * Our main weapon
 * Created by Calvin on 2014/09/26.
 */
public class Block extends Collidable {

    Sound drop;


    // For nice behaviour
    boolean dropping;
    boolean rising;
    boolean canDrop = true;
    int riseTo = 400;

    // Need to keep camera so we make sure it's always on screen
    CameraController cam;

    public Block(String name, CameraController cam){
        super(name);
        this.cam = cam;
        drop = Gdx.audio.newSound(Gdx.files.internal("drop.ogg"));

    }

    public void update(){

        // Make sure it's on screen
        if(x < cam.cam.position.x - cam.camWidth){
            x = (int)cam.cam.position.x - cam.camWidth;
        } else if (x + sprite.getWidth() > cam.cam.position.x + cam.camWidth){
            x = (int)(cam.cam.position.x + cam.camWidth - sprite.getWidth());
        }

        if(y > cam.cam.position.y + cam.camHeight - 20){
            y = (int)cam.cam.position.y + cam.camHeight - 20;
        } else if (!dropping && y < cam.cam.position.y - cam.camHeight + 40){
            y = (int)cam.cam.position.y - cam.camHeight + 40;
        }

        // Get it to the right height
        if (rising){
            y += 30;
            if (y > riseTo - 100){
                canDrop = true;
            }
            if (y > riseTo){
                rising = false;
            }

        }

        // Drop has been set to true
        if(dropping){
            y -= 50;
            if (y < 64){
                drop.play();
                y = 64;
                cam.shake(30);
                dropping = false;
                rising = true;
                riseTo = Math.min(y + 350, 500);
            }
        } else if (y<200){
            // Avoid it going too low
            y = 200;
        }
    }

    public void resolve(Rectangle r){
        // If it hits something before the ground
        if(dropping){
            drop.play();
            cam.shake(30);
            dropping = false;
            rising = true;
            riseTo = Math.min(y + 350, 500);
        }
    }
}
