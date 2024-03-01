package com.roguedm.jrpg;

import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;

public class AssetManager {

    public static final String CREATURES = "gfx/creatures.pack";
    public static final String CREATURES_REGION = "oryx_16bit_fantasy_creatures";
    public static final String FX_LARGE = "gfx/fx_large.pack";
    public static final String FX_LARGE_REGION = "oryx_16bit_fantasy_fx";
    public static final String FX_SMALL = "gfx/fx.pack";
    public static final String FX_SMALL_REGION = "oryx_16bit_fantasy_fx2";

    private static AssetManager instance;

    private com.badlogic.gdx.assets.AssetManager assetManager;

    public AssetManager() {
        assetManager = new com.badlogic.gdx.assets.AssetManager();
    }

    public static AssetManager getInstance() {
        if (instance == null) {
            instance = new AssetManager();
        }
        return instance;
    }

    public void load() {
        assetManager.setLoader(TiledMap.class, new TmxMapLoader(new InternalFileHandleResolver()));

        assetManager.load(CREATURES, TextureAtlas.class);
        assetManager.load(FX_LARGE, TextureAtlas.class);
        assetManager.load(FX_SMALL, TextureAtlas.class);

        assetManager.load("maps/test.tmx", TiledMap.class);

        assetManager.finishLoading();
    }

    public TextureRegion getCreature(int index) {
        if (assetManager.get(CREATURES) == null) {
            return null;
        }
        return ((TextureAtlas) assetManager.get(CREATURES)).findRegion(CREATURES_REGION, index);
    }

    public TextureRegion getSmallEffect(int index) {
        if (assetManager.get(FX_SMALL) == null) {
            return null;
        }
        return ((TextureAtlas) assetManager.get(FX_SMALL)).findRegion(FX_SMALL_REGION, index);
    }

    public TextureRegion getLargeEffect(int index) {
        if (assetManager.get(FX_LARGE) == null) {
            return null;
        }
        return ((TextureAtlas) assetManager.get(FX_LARGE)).findRegion(FX_LARGE_REGION, index);
    }

    public TiledMap getTileMap(String fileName) {
        return assetManager.get(fileName, TiledMap.class);
    }

    public static void dispose() {
        if (instance != null && instance.assetManager != null) {
            instance.assetManager.clear();
            instance.assetManager.dispose();
        }
        instance = null;
    }

}
