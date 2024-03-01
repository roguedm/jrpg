package com.roguedm.jrpg.object.fx;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

import squidpony.squidmath.Coord;

public class AnimatedImage extends Image {

    protected Animation<TextureRegion> animation = null;
    protected float stateTime = 0;

    public AnimatedImage(float speed, int size, TextureRegion... regions) {
        super(regions[0]);
        this.animation = new Animation<TextureRegion>(speed, regions);
        this.setSize(size, size);
    }

    @Override
    public void act(float delta) {
        ((TextureRegionDrawable) getDrawable()).setRegion(animation.getKeyFrame(stateTime+=delta, true));
        super.act(delta);
    }

    public boolean isFinished() {
        return animation.isAnimationFinished(stateTime);
    }


    public void set(Coord coord) {
        setX(coord.getX());
        setY(coord.getY());
    }


    public Coord get() {
        return Coord.get((int) getX(), (int) getY());
    }


    public void dispose() {
    }

}