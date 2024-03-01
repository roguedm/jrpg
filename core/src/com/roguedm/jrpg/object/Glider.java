package com.roguedm.jrpg.object;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.roguedm.jrpg.AssetManager;

import squidpony.squidai.DijkstraMap;
import squidpony.squidgrid.Measurement;
import squidpony.squidmath.Coord;

public class Glider extends Sprite {

    protected Animation<Sprite> animation = null;
    protected Sprite[] frames;
    protected float stateTime = 0;
    protected Facing facing;
    protected int indexA;
    protected int indexB;

    protected Coord start;
    protected Coord end;
    protected Coord coord;
    protected float change;
    protected int width;
    protected int height;

    public DijkstraMap toSelf;
    public DijkstraMap toCursor;

    public Glider(int indexA, int indexB) {
        super();

        coord = Coord.get(0, 0);
        start = Coord.get(0, 0);
        end = Coord.get(0, 0);

        this.indexA = indexA;
        this.indexB = indexB;

        this.facing = Facing.Right;
    }

    public void init() {
        setFrames(((facing == Facing.Left) ? false : true));
    }

    public void init(char[][] map) {
        this.toCursor = new DijkstraMap(map, Measurement.MANHATTAN);
        this.toSelf = new DijkstraMap(map, Measurement.MANHATTAN);
        init();
    }

    public void setFrames(boolean flip) {
        this.frames = new Sprite[2];

        this.frames[0] = new Sprite(AssetManager.getInstance().getCreature(indexA));
        this.frames[1] = new Sprite(AssetManager.getInstance().getCreature(indexB));

        this.frames[0].flip(flip, false);
        this.frames[1].flip(flip, false);

        this.animation = new Animation<Sprite>(.25f, frames);

        if (flip) {
            facing = Facing.Left;
        } else {
            facing = Facing.Right;
        }
    }

    public void setFacing(Facing facing) {
        this.facing = facing;
        if (facing == Facing.Right && !frames[0].isFlipX()) {
            frames[0].flip(true, false);
            frames[1].flip(true, false);
        } else if (facing == Facing.Left && frames[0].isFlipX()) {
            frames[0].flip(true, false);
            frames[1].flip(true, false);
        }
    }

    public void act(float delta) {
        if (animation != null) {
            setRegion(animation.getKeyFrame(stateTime += delta, true));
        }
    }

    public float getX() {
        if (change >= 1f) {
            return (start = end).x;
        }
        return MathUtils.lerp(start.x, end.x, change);
    }

    public float getY() {
        if (change >= 1f) {
            return (start = end).y;
        }
        return MathUtils.lerp(start.y, end.y, change);
    }

    public void set(Coord coord) {
        this.coord = coord;
        this.start = coord;
        this.end = coord;
        if (coord != null) {
            setPosition(coord.getX(), coord.getY());
        }
    }

    public Coord getStart() {
        return start;
    }

    public void setStart(Coord start) {
        this.start = start;
    }

    public Coord getEnd() {
        return end;
    }

    public void setEnd(Coord end) {
        this.end = end;
    }

    public Coord getCoord() {
        return coord;
    }

    public void setCoord(Coord coord) {
        this.coord = coord;
    }

    public float getChange() {
        return change;
    }

    public void setChange(float change) {
        this.change = change;
    }

    public void clear() {
        this.toCursor.clearGoals();
        this.toCursor.resetMap();
        this.toCursor.setGoal(this.coord);
    }

    public void render(SpriteBatch spriteBatch, float x, float y, int width, int height) {
        if (spriteBatch != null && spriteBatch.isDrawing() && getTexture() != null) {
            spriteBatch.draw(this, x, y + 1, width, height);
        }
    }

    public void dispose() {
    }

}