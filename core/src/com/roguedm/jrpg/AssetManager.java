package com.roguedm.jrpg;

import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGeneratorLoader;
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;

public class AssetManager {

    public static final String CREATURES = "gfx/creatures.pack";
    public static final String CREATURES_REGION = "oryx_16bit_fantasy_creatures";
    public static final String FX_LARGE = "gfx/fx_large.pack";
    public static final String FX_LARGE_REGION = "oryx_16bit_fantasy_fx";
    public static final String FX_SMALL = "gfx/fx.pack";
    public static final String FX_SMALL_REGION = "oryx_16bit_fantasy_fx2";
    public static final String FONT = "fonts/kenneyrocket.ttf";


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

        FileHandleResolver resolver = new InternalFileHandleResolver();
        assetManager.setLoader(FreeTypeFontGenerator.class, new FreeTypeFontGeneratorLoader(resolver));
        assetManager.setLoader(BitmapFont.class, ".ttf", new FreetypeFontLoader(resolver));

        FreetypeFontLoader.FreeTypeFontLoaderParameter parameter = new FreetypeFontLoader.FreeTypeFontLoaderParameter();
        parameter.fontFileName = FONT;
        parameter.fontParameters.size = 18;
        assetManager.load(FONT, BitmapFont.class, parameter);

        assetManager.load(CREATURES, TextureAtlas.class);
        assetManager.load(FX_LARGE, TextureAtlas.class);
        assetManager.load(FX_SMALL, TextureAtlas.class);

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
        if (assetManager.isLoaded(fileName)) {
            return assetManager.get(fileName, TiledMap.class);
        }
        assetManager.load(fileName, TiledMap.class);
        assetManager.finishLoading();
        return assetManager.get(fileName, TiledMap.class);
    }

    public BitmapFont getFont() {
        return assetManager.get(FONT, BitmapFont.class);
    }

    public static void dispose() {
        if (instance != null && instance.assetManager != null) {
            instance.assetManager.clear();
            instance.assetManager.dispose();
        }
        instance = null;
    }

}
