package com.ssbb.game;

import com.badlogic.gdx.graphics.OrthographicCamera;
import java.util.Random;

/**
 * Makes out Camera play nice
 * Created by Calvin on 2014/09/27.
 */
public class CameraController {

    // Random so we can shake things up
    Random mizer = new Random();
    int shake;
    boolean shaking;

    // Camera needs to follow player
    OrthographicCamera cam;

    Player player;
    // Useful variables
    int mapWidth;
    int mapHeight;
    int camWidth;
    int camHeight;

    public CameraController(OrthographicCamera camera, Player player, int width, int height) {
        this.player = player;
        cam = camera;

        // So we can centre things nicely
        camWidth = (int) cam.viewportWidth / 2;
        camHeight = (int) cam.viewportHeight / 2;
        mapWidth = width - camWidth;
        mapHeight = height - camHeight;
    }

    public void shake(int magnitude) {
        // Allows caller to set duration of shake
        shaking = true;
        shake = magnitude;
    }

    public void update() {

        // We want to be a little ahead of the player
        cam.position.x -= (cam.position.x - (player.x + 50 * player.direction)) / 32;
        cam.position.y -= (cam.position.y - (player.y) - 100) / 32;


        // Horizontal axis
        if (cam.position.x < camWidth) {
            cam.position.x = camWidth;
        } else if (cam.position.x >= mapWidth) {
            cam.position.x = mapWidth;
        }

        // Vertical axis
        if (cam.position.y <= camHeight) {
            cam.position.y = camHeight;
        } else if (cam.position.y >= mapHeight) {
            cam.position.y = mapHeight;
        }
        // Screen Shake
        if (shaking) {
            // Shake diminishes over time
            shake -= 1;
            if (shake < 1) {
                shaking = false;

            } else if (shake % 2 == 0) {
                cam.position.x += mizer.nextInt(shake);
                cam.position.y -= mizer.nextInt(shake);
            } else {
                cam.position.x -= mizer.nextInt(shake);
                cam.position.y += mizer.nextInt(shake);
            }
        }

        // Make sure it updates
        cam.update();
    }
}
