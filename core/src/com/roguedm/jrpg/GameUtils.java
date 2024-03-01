package com.roguedm.jrpg;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.roguedm.jrpg.world.World;

public class GameUtils {

    public static final int SCREEN_WIDTH = 800;
    public static final int SCREEN_HEIGHT = 480;

    public static int TILE_SIZE = 72;

    private GameUtils() {
    }

    public static void setScreen(final Screen from, final Screen to) {
        ApplicationListener listener = Gdx.app.getApplicationListener();
        if (to != null) {
            if (listener != null && listener instanceof Game) {
                ((Game) Gdx.app.getApplicationListener()).setScreen(to);
            }
            if (from != null) {
                from.dispose();
            }
        }
    }

    public static SpriteBatch getSpriteBatch() {
        ApplicationListener listener = Gdx.app.getApplicationListener();
        if (listener != null && listener instanceof JRPGGame) {
            JRPGGame parent = ((JRPGGame) Gdx.app.getApplicationListener());
            return parent.getSpriteBatch();
        }
        return null;
    }

    public static World getCurrentWorld() {
        ApplicationListener listener = Gdx.app.getApplicationListener();
        if (listener != null && listener instanceof JRPGGame) {
            JRPGGame parent = ((JRPGGame) Gdx.app.getApplicationListener());
            if (parent.getGameState() != null) {
                return parent.getGameState().getWorld();
            }
        }
        return null;
    }

    public static Texture getDisplayBar(Color color, int min, int max, int width, int height) {
        float ratio = (float) min / (float) max;
        Pixmap background = new Pixmap(width, height, Pixmap.Format.RGBA8888);
        background.setColor(Color.BLACK);
        background.fill();
        Pixmap foreground = new Pixmap((int) (width * ratio), height, Pixmap.Format.RGBA8888);
        foreground.setColor(color);
        foreground.fill();
        background.drawPixmap(foreground, 0, 0);
        Texture texture = new Texture(background);
        foreground.dispose();
        background.dispose();
        return texture;
    }

}
