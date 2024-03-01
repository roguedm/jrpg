package com.roguedm.jrpg.world;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;

import com.roguedm.jrpg.AssetManager;
import com.roguedm.jrpg.object.Glider;

public class World {

    private static final String COLLISION_LAYER_NAME = "Collisions";

    private TiledMap tiledMap;

    private TiledMapRenderer tiledMapRenderer;

    private char[][] collisionMap;

    public World(String fileName) {
        tiledMap = AssetManager.getInstance().getTileMap(fileName);
        tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap, 3f);
        if (tiledMap.getLayers() != null) {
            TiledMapTileLayer collisionLayer = (TiledMapTileLayer) tiledMap.getLayers().get(COLLISION_LAYER_NAME);
            if (collisionLayer != null) {
                int width = collisionLayer.getWidth();
                int height = collisionLayer.getHeight();
                collisionMap = new char[width][height];
                for (int y = 0; y < height; y++) {
                    for (int x = 0; x < width; x++) {
                        if (collisionLayer.getCell(x, y) != null) {
                            collisionMap[x][y] = '#';
                        } else {
                            collisionMap[x][y] = '.';
                        }
                    }
                }
            }
        }
    }

    public void initialize(Glider glider) {
        glider.init(collisionMap);
    }

    public boolean collision(int x, int y) {
        if (collisionMap != null && collisionMap[x][y] == '#') {
            return true;
        }
        return false;
    }

    public void render(Camera camera) {
        tiledMapRenderer.setView((OrthographicCamera) camera);
        tiledMapRenderer.render();


        for (MapLayer layer : tiledMap.getLayers()) {
            //renderMapLayer(layer);
        }
    }


}
