package com.ssbb.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.utils.Array;

import java.util.ArrayList;

/**
 * SquishyBlock, a game about not much, so far
 * basically a "first playable" to check that collision works
 * by Calvin Brizzi
 */

public class SquishyBlock extends ApplicationAdapter {

    // So we know what to render
    public enum GameState {
        RUN,
        MENU,
        PAUSE,
        DEAD,
        END,
        WIN,
        TUT
    }

    GameState state = GameState.MENU;

    public static final int FRICTION = 1;
    public static final int GRAVITY = 1;

    // Used to draw sprites, map and to start the game
    public SpriteBatch batch;
    public Renderer gameRendered = new Renderer(this);
    OrthogonalTiledMapRenderer tiledMapRenderer;
    Creator creator = new Creator(this);

    // Our entities and the list to store them
    public Player player;
    public Block tetronimo;
    public ArrayList<Collidable> colliders = new ArrayList<Collidable>();

    // Input handler, camera and that jazz
    public InputHandler inputHandler;
    public OrthographicCamera camera;
    public CameraController cameraController;

    // Map variables and objects
    public int mapWidth;
    public int mapHeight;
    public MapLayer layer;
    public MapObjects objects;
    public Array<RectangleMapObject> obstacles;
    public TiledMap map;

    // Menus and HUD textures
    public Texture menu;
    public Texture pause;
    public Texture dead;
    public Texture win;
    public Texture heart;
    public Texture halfHeart;
    public Texture noHeart;
    public Texture pIcon;
    public Texture end;

    // "Grid" for collision detection
    public Grid grid;
    int counter = 0;

    // Let's animate!
    int animationColumns = 3;
    int animationRows = 4;
    Animation playerAnimation;
    Texture animationTexture;
    TextureRegion[] animationFrames;
    TextureRegion currentFrame;
    float animationState;

    // Musak
    Music music;
    Sound doorSound;
    Sound startSound;

    // A*
    PathFinder aStar;
    int level;
    int lives;
    String[] levels = {"popup.tmx", "three.tmx"};

    // Timing and tut
    int timing;
    boolean printed;
    int tut_c;
    String[] tut = {"tt-1.png", "tt-2.png", "tt-3.png", "tt-4.png", "tt-5.png"};
    int[] tpoints = {0, 1409, 2500, 4300, 9300};

    @Override
    public void create() {
        music = Gdx.audio.newMusic(Gdx.files.internal("background.ogg"));
        music.setLooping(true);
        doorSound = Gdx.audio.newSound(Gdx.files.internal("door.ogg"));
        startSound = Gdx.audio.newSound(Gdx.files.internal("start.ogg"));
        player = new Player("player.png", this);
        // Pass off most responsibilities
        creator.newGame(levels[level]);
        menu = new Texture("menu.png");
        pause = new Texture("pause.png");
        dead = new Texture("dead.png");
        win = new Texture("win.png");
        end = new Texture("gameEnd.png");

        inputHandler = new InputHandler();
        grid = new Grid(60 * 64, this);
        music.play();
        tut_c = 0;
        printed = false;
    }

    @Override
    public void render() {

        // Let's see what to do
        switch (state) {
            case RUN:
                counter++;
                if (counter == 2) {
                    aStar.createGrid();
                }
                // Make everyone update, then render
                inputHandler.update(player, tetronimo);
                grid.resolveCollisions();
                gameRendered.render();

                if (Gdx.input.isKeyJustPressed(Input.Keys.P)) {
                    state  = GameState.PAUSE;
                }
                if(true && tut_c < 5 && player.x > tpoints[tut_c]){
                    tut_c++;
                    tuttime();
                }
                //state = GameState.PAUSE;
                break;
            case MENU:
                level = 0;
                drawMenu(menu);
                if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
                    player = new Player("player.png", this);
                    creator.newGame(levels[level]);
                    state = GameState.RUN;
                    startSound.play();
                }
                break;
            case PAUSE:
                drawMenu(pause);
                if (Gdx.input.isKeyJustPressed(Input.Keys.P)) {
                    state = GameState.RUN;
                    camera.position.x = player.x;
                    camera.position.y = player.y;
                }
                break;
            case TUT:
                Texture tuttex = new Texture(tut[tut_c - 1]);
                //tut_c += 1;
                drawMenu(tuttex);
                if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE) ||
                    Gdx.input.isKeyPressed(Input.Keys.CONTROL_RIGHT)) {
                    state = GameState.RUN;
                    camera.position.x = player.x;
                    camera.position.y = player.y;
                }
                break;

            case DEAD:
                drawMenu(dead);
                if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
                    player.lives--;
                    if (player.lives < 1) {
                        state = GameState.MENU;
                    } else {
                        lives = player.lives;
                        player = new Player("player.png", this);
                        player.lives = lives;
                        creator.newGame(levels[level]);
                        state = GameState.RUN;
                    }
                }
                break;
            case END:
                drawMenu(win);
                if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
                    if(level == 1){
                        timing = (int) (System.currentTimeMillis() / 1000L);
                    }
                    creator.newGame(levels[level]);
                    state = GameState.RUN;
                }
                break;
            case WIN:
                drawMenu(end);
                if(!printed){
                    printed = true;
                    timing = (int) (System.currentTimeMillis() / 1000L) - timing;
                    System.out.println("It took " + timing);
                }
                if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)){
                    state = GameState.MENU;
                }
                break;

        }
    }

    public void playerDead() {
        state = GameState.DEAD;
        player.life = 10;
    }

    public void win() {
        doorSound.play();
        state = GameState.END;
        level++;
        if (level >= levels.length) {
            state = GameState.WIN;
        }
    }

    public void tuttime() {
        state = GameState.TUT;
    }

    public void drawMenu(Texture menuToDraw) {
        // Sets the camera position to 0,0 so we can see the menu
        camera.position.x = 0;
        camera.position.y = 0;
        cameraController.update();
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        batch.draw(menuToDraw, 0, 0);
        batch.end();
    }
}
