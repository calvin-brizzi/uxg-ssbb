package com.ssbb.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Rectangle;
import java.util.ArrayList;

/**
 * A grid for more efficient collision detection
 * Now also deals with the collisions
 * Also, been collapsed into an array
 * Created by Calvin on 2014/09/07.
 */
public class Grid {

    private ArrayList<Collidable>[] grid = new ArrayList[6];
    private int width;
    SquishyBlock game;
    int counter = 0;
    Sound hit;

    public Grid(int width, SquishyBlock game) {
        // A simple constructor

        this.width = width;
        for (int i = 0; i < 6; i++) {
            grid[i] = new ArrayList<Collidable>();
        }
        this.game = game;
        hit = Gdx.audio.newSound(Gdx.files.internal("hit.ogg"));
    }

    public void clear() {
        // Clear all objects
        for (int i = 0; i < 6; i++) {
            grid[i].clear();
        }
    }

    public void add(Collidable entity) {
        // Need to find all grid positions it touches
        // Because our sprites < size of a grid cell, we can just check positions of vertices
        Rectangle bounds = entity.sprite.getBoundingRectangle();

        int cells = cell(bounds.x, bounds.y);
        safeAdd(cells, entity);
        cells = cell(bounds.x + bounds.width, bounds.y);
        safeAdd(cells, entity);
        cells = cell(bounds.x + bounds.width, bounds.y + bounds.height);
        safeAdd(cells, entity);
        cells = cell(bounds.x, bounds.y + bounds.height);
        safeAdd(cells, entity);
    }

    public void addEmpty(Tutorial tut) {
        // Need to find all grid positions it touches
        // Because our sprites < size of a grid cell, we can just check positions of vertices
        Collidable entity = (Collidable)tut;
        Rectangle bounds = tut.tt;
        int cells = cell(bounds.x, bounds.y);
        safeAdd(cells, entity);
        cells = cell(bounds.x + bounds.width, bounds.y);
        safeAdd(cells, entity);
        cells = cell(bounds.x + bounds.width, bounds.y + bounds.height);
        safeAdd(cells, entity);
        cells = cell(bounds.x, bounds.y + bounds.height);
        safeAdd(cells, entity);
        for(int i = (int)bounds.x; i < bounds.x + bounds.width; i++) {
            for (int j = (int)bounds.y; j < bounds.y + bounds.height; j++) {
                cells = cell(i, j);
                safeAdd(cells, entity);
            }
        }
    }

    private void safeAdd(int cell, Collidable entity) {
        // Add only if not present
        if (!grid[cell].contains(entity)) {
            grid[cell].add(entity);
        }
    }

    public int cell(float x, float y) {
        // Checks the correct location in most naive way ever
        int cell = 0;
        int t = (int) (width / x);
        if (t > 6) {
            cell = 0;
        } else if (t > 3) {
            cell = 1;
        } else if (t > 2) {
            cell = 2;
        } else if (t > 1.5) {
            cell = 3;
        } else if (t > 5 / 6) {
            cell = 4;
        } else {
            cell = 5;
        }
        return cell;
    }

    public ArrayList<Collidable> get(int x) {
        // Returns all elements within a given grid
        return grid[x];
    }

    public void resolveCollisions() {

        counter++;
        if(counter > 30){
            game.aStar.resetPaths();
        }
        // Get the colliders and list those that need to be deleted
        ArrayList<Collidable> colliders = game.colliders;
        ArrayList<Collidable> toDelete = new ArrayList<Collidable>();
        Player player = game.player;

        // Clear grid, then add everything to it
        this.clear();
        for (Collidable c : colliders) {
            c.update();

            if (!c.dead) {
                // Add if not dead
                if(c instanceof Tutorial){
                    addEmpty((Tutorial)c);
                } else {
                    this.add(c);
                }
                c.sprite.setPosition(c.x, c.y);
            } else {
                toDelete.add(c);
            }
        }

        for (Collidable c : toDelete) {
            colliders.remove(c);
        }

        // Checking each grid cell for more than one entity
        for (int i = 0; i < 6; i++) {
            ArrayList<Collidable> collidables = this.get(i);
            if (collidables.size() > 1) {
                // Loop through the collidables and check them against all others
                for (int indexOfFirst = 0; indexOfFirst < collidables.size(); indexOfFirst++) {
                    // Start second index at index of first + 1 to avoid double checking
                    Collidable first = collidables.get(indexOfFirst);

                    for (int indexOfSecond = indexOfFirst + 1; indexOfSecond < collidables.size(); indexOfSecond++) {
                        // So we can play nicely
                        Collidable second = collidables.get(indexOfSecond);

                        if (first.sprite.getBoundingRectangle().overlaps(second.sprite.getBoundingRectangle())) {
                            if (first == player) {
                                // Player checks
                                if (second == game.tetronimo && !player.hit) {
                                    if (game.tetronimo.dropping) {
                                        // You hit yourself
                                        player.life -= 5;
                                        player.hit();
                                    } else {
                                        // You're probably standing on it
                                        game.player.resolve(second.sprite.getBoundingRectangle());
                                    }
                                } else if (second instanceof PowerUp) {
                                    // Pickup the powerup!

                                    ((PowerUp) second).get();

                                } else if (second instanceof Door) {
                                    // Winnar!
                                    game.win();
                                } else if (second != game.tetronimo) {
                                    // You've hit an enemy!
                                    hit.play();
                                    second.dead = true;
                                    player.life -= 3;
                                    player.hit();
                                    game.cameraController.shake(15);
                                }
                            }

                            if (first == game.tetronimo && game.tetronimo.dropping && !(second instanceof Door)) {
                                // You squished an enemy!
                                second.dead = true;
                            }
                        }
                    }
                }
            }
        }

        for (Collidable c : colliders) {
            // update positions
            c.sprite.setPosition(c.x, c.y);
        }

        for (RectangleMapObject o : game.obstacles) {
            // Check against world objects
            if (game.player.sprite.getBoundingRectangle().overlaps(o.getRectangle())) {
                game.player.resolve(o.getRectangle());
            }
            if (game.tetronimo.sprite.getBoundingRectangle().overlaps(o.getRectangle())) {
                game.tetronimo.resolve(o.getRectangle());
            }
        }
    }

}
