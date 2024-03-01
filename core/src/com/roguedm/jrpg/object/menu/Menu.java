package com.roguedm.jrpg.object.menu;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;

public class Menu {

    public Array<MenuItem> menuItems;

    public Menu() {
        menuItems = new Array<MenuItem>();

        menuItems.add(new MenuItem("Test 1"));
        menuItems.add(new MenuItem("Test 2"));
        menuItems.add(new MenuItem("Test 3"));
    }

    public void draw(SpriteBatch spriteBatch, BitmapFont bitmapFont, int x, int y) {
        if (spriteBatch != null && spriteBatch.isDrawing() && bitmapFont != null) {
            int count = 0;
            if (menuItems != null) {
                for (MenuItem item : menuItems) {
                    if (item != null) {
                        bitmapFont.draw(spriteBatch, item.getDisplayText(), x, y - ((bitmapFont.getLineHeight() + 10) * count++));
                    }
                }
            }


        }
    }

}
