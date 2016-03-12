package com.ssbb.game;

/**
 * Created by calvin on 2014/10/14.
 */
public class Life extends PowerUp {
    public Life(String name, Player player){
        super(name, player);
    }

    @Override
    public void get(){
        player.lives++;
        dead = true;
        powerSound.play();
    }
}
