package com.roguedm.jrpg.object.fx;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.utils.Array;

import squidpony.squidmath.Coord;

public class Effect {

    protected Array<EventListener> listeners;
    protected AnimatedImage fx;

    private int offsetX = 0;
    private int offsetY = 0;

    public Effect(final AnimatedImage image, int offsetX, int offsetY) {
        this.listeners = new Array<EventListener>();
        this.fx = image;
        this.offsetX = offsetX;
        this.offsetY = offsetY;
    }

    public void addListener(EventListener listener) {
        this.listeners.add(listener);
    }

    public Array<EventListener> getListeners() {
        return listeners;
    }

    public void initialize(Coord c1, Coord c2) {
        this.fx.setPosition(c1.getX() + offsetX, c1.getY() + offsetY);
    }

    public boolean isFinished() {
        return fx.isFinished();
    }

    public void draw(SpriteBatch batch) {
        fx.draw(batch, 1f);
    }

    public void act(float delta) {
        fx.act(delta);
    }

}

