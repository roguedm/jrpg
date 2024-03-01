package com.roguedm.jrpg.world;

public class LevelManager {

    private static LevelManager levelManager;

    private LevelManager() {
    }

    public static final LevelManager getInstance() {
        if (levelManager == null) {
            levelManager = new LevelManager();
        }
        return levelManager;
    }

    public Level getLevel() {

        // Generate by data files in the future
        Level level = new Level();
        level.setName("Test Level");
        level.setFileName("maps/test.tmx");
        return level;
    }

    public void dispose() {
        levelManager = null;
    }

}
