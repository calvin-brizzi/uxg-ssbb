package com.ssbb.game;

import com.badlogic.gdx.Gdx;

/**
 * A tiledMapRenderer to create our world
 * Created by calvin on 2014/09/26.
 */
public class Renderer {

    // We need the game to render
    SquishyBlock game;

    public Renderer(SquishyBlock game) {
        this.game = game;
    }

    public void render() {


        // Animation and camera
        game.animationState += Gdx.graphics.getDeltaTime();
        game.cameraController.update();
        game.batch.setProjectionMatrix(game.camera.combined);

        // Map
        game.tiledMapRenderer.setView(game.camera);
        game.tiledMapRenderer.render();

        // Render sprites
        game.batch.begin();

        for (Collidable c : game.colliders) {
            if (c != game.player) {
                // Non animated parts
                game.batch.draw(c.sprite, c.sprite.getX(), c.sprite.getY());
            } else {
                // The animated player
                if (game.player.ax == 0) {
                    game.animationState = 1 / 12f * 10;
                }
                game.currentFrame = game.playerAnimation.getKeyFrame(game.animationState, true);
                if (game.player.ax < 0 && !game.currentFrame.isFlipX()) {
                    game.currentFrame.flip(true, false);
                } else if (game.player.ax > 0 && game.currentFrame.isFlipX()) {
                    game.currentFrame.flip(true, false);
                }
                game.batch.draw(game.currentFrame, c.x, c.y);
            }
        }
        // Draw lives, scores to come?
        renderHUD();
        game.batch.end();


    }

    private void renderHUD() {

        // A lot of code to draw the lives nicely
        int totalLives = 10;
        int lives = game.player.life;
        int fullHearts = lives / 2;
        int halfHearts = lives % 2;
        int noHearts = (totalLives / 2 - fullHearts - halfHearts);
        for (int i = 0; i < fullHearts; i++) {
            game.batch.draw(game.heart, (game.camera.position.x - game.cameraController.camWidth) + 20 * i + 15, (game.camera.position.y - game.cameraController.camHeight) + 10);
        }
        for (int i = 0; i < halfHearts; i++) {
            game.batch.draw(game.halfHeart, (game.camera.position.x - game.cameraController.camWidth) + 20 * (i + fullHearts) + 15, (game.camera.position.y - game.cameraController.camHeight) + 10);
        }
        for (int i = 0; i < noHearts; i++) {
            game.batch.draw(game.noHeart, (game.camera.position.x - game.cameraController.camWidth) + 20 * (i + fullHearts + halfHearts) + 15, 10 + (game.camera.position.y - game.cameraController.camHeight));
        }

        for (int i = 0; i < game.player.lives; i++) {
            game.batch.draw(game.pIcon, game.camera.position.x + game.cameraController.camWidth - 50 - 20 * i,  10 + (game.camera.position.y - game.cameraController.camHeight));
        }
    }
}
