package com.ssbb.game;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;

/**
 * Where we make the magic, could maybe be broken up more
 * Created by calvin on 2014/09/28.
 */
public class Creator {

    SquishyBlock game;

    public Creator(SquishyBlock game) {
        this.game = game;
    }

    public void newGame(String mapName) {
        // Clear so we avoid stuff persisting
        game.colliders.clear();

        // HUD elements
        game.heart = new Texture("f.png");
        game.halfHeart = new Texture("h.png");
        game.noHeart = new Texture("n.png");
        game.pIcon = new Texture("p2.png");

        // Setup walking animation
        game.animationTexture = new Texture("pwt.png");
        TextureRegion[][] tmp = TextureRegion.split(game.animationTexture, game.animationTexture.getWidth() / game.animationColumns, game.animationTexture.getHeight() / game.animationRows);
        game.animationFrames = new TextureRegion[11];
        int index = 0;
        for (int i = 0; i < game.animationRows && index < 11; i++) {
            for (int j = 0; j < game.animationColumns && index < 11; j++) {
                game.animationFrames[index++] = tmp[i][j];
            }
        }
        game.playerAnimation = new Animation(1 / 12f, game.animationFrames);
        game.animationState = 0f;

        // Setup collidable objects in the game
        game.map = new TmxMapLoader().load(mapName);
        game.layer = game.map.getLayers().get("collisio");
        game.objects = game.layer.getObjects();
        game.obstacles = game.objects.getByType(RectangleMapObject.class);
        game.tiledMapRenderer = new OrthogonalTiledMapRenderer(game.map, 1);

        // Get the useful properties from the game
        MapProperties properties = game.map.getProperties();
        game.mapWidth = properties.get("width", Integer.class) * 64;
        game.mapHeight = properties.get("height", Integer.class) * 64;

        // Usual initializers
        game.batch = new SpriteBatch();

        // Setup Camera
        game.camera = new OrthographicCamera();
        game.camera.setToOrtho(false, 1920, 1080);
        game.camera.update();
        game.cameraController = new CameraController(game.camera, game.player, game.mapWidth, game.mapHeight);

        // Setup Block of death
        game.tetronimo = new Block("block.png", game.cameraController);
        game.tetronimo.x = 500;
        game.tetronimo.y = 500;

        game.colliders.add(game.player);
        game.colliders.add(game.tetronimo);

        // Initial positions
        game.player.x = 40;
        game.player.y = 64;

        // Power Ups and the final Door
        game.layer = game.map.getLayers().get("special");
        game.objects = game.layer.getObjects();
        for (MapObject o : game.objects) {
            String type = o.getName();
                if (type.equals("PowerUp")) {
                Rectangle tt = ((RectangleMapObject) o).getRectangle();
                PowerUp temp = new PowerUp("gem.png", game.player);
                temp.x = (int) tt.x;
                temp.y = (int) tt.y;
                game.colliders.add(temp);
            } else if (type.equals("door")) {
                Rectangle tt = ((RectangleMapObject) o).getRectangle();
                Door t = new Door("door.png");
                t.x = (int) tt.x;
                t.y = (int) tt.y;
                game.colliders.add(t);
            } else if (type.equals("life")){
                Rectangle tt = ((RectangleMapObject) o).getRectangle();
                PowerUp temp = new Life("p2.png", game.player);
                temp.x = (int) tt.x;
                temp.y = (int) tt.y;
                game.colliders.add(temp);
            } else if (type.equals("tutorial")) {
                Rectangle tt = ((RectangleMapObject) o).getRectangle();
                Tutorial temp = new Tutorial("", game, tt);
                temp.x = (int) tt.x;
                temp.y = (int) tt.y;
                game.colliders.add(temp);
            }
        }

        game.aStar = new PathFinder(game);

        // Enemies
        game.layer = game.map.getLayers().get("Enemy");
        game.objects = game.layer.getObjects();
        for (MapObject o : game.objects) {
            String type = o.getName();
            if (type.equals("block")) {
                Rectangle tt = ((RectangleMapObject) o).getRectangle();
                GroundEnemy temp = new GroundEnemy("enemy.png", game.player);
                temp.x = (int) tt.x;
                temp.y = (int) tt.y;
                game.colliders.add(temp);
            } else if (type.equals("bee")) {
                Rectangle tt = ((RectangleMapObject) o).getRectangle();
                FlyingEnemy t = new FlyingEnemy("bee.png", game.player, game.aStar);
                t.x = (int) tt.x;
                t.y = (int) tt.y;
                game.colliders.add(t);
            }
        }
    }
}
