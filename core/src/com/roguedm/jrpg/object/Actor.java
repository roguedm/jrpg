package com.roguedm.jrpg.object;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.roguedm.jrpg.GameUtils;
import com.roguedm.jrpg.Values;

public class Actor extends Glider {

    public Texture healthTexture;

    public Values health;

    public Actor(int indexA, int indexB) {
        super(indexA, indexB);
        health = new Values(5, 5);

        setSize(GameUtils.TILE_SIZE, GameUtils.TILE_SIZE);
    }

    public void update() {
        if (health != null) {
            healthTexture = GameUtils.getDisplayBar(Color.RED, health.getMin(), health.getMax(), GameUtils.TILE_SIZE - 10, 4);
        }
    }

    @Override
    public void render(SpriteBatch spriteBatch, float x, float y, int width, int height) {
        super.render(spriteBatch, x, y, width, height);
        if (spriteBatch != null && spriteBatch.isDrawing() && healthTexture != null) {
            spriteBatch.draw(healthTexture, x + 5, y + height + 2);
        }
    }

    public void damage(int damage) {
        health.setMin(this.health.getMin() + damage);
        if (health.getMin() < 0) {
            health.setMin(0);
        }
        if (health.getMin() > health.getMax()) {
            health.setMin(health.getMax());
        }
        update();
    }

    public boolean isAlive() {
        return (health != null && health.getMin() > 0);
    }

    @Override
    public void dispose() {
        if (healthTexture != null) {
            healthTexture.dispose();
        }
    }

    public void render(SpriteBatch spriteBatch) {
        render(spriteBatch, getX(), getY(), (int) getWidth(), (int) getHeight());
    }

}
