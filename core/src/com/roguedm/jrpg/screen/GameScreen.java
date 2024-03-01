package com.roguedm.jrpg.screen;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.ScalingViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.roguedm.jrpg.GameUtils;
import com.roguedm.jrpg.JRPGGame;
import com.roguedm.jrpg.object.Facing;
import com.roguedm.jrpg.object.Glider;
import com.roguedm.jrpg.world.World;

import java.util.ArrayList;
import java.util.List;

import squidpony.squidmath.Coord;

public class GameScreen implements Screen {

    private enum Phase {
        WAIT,
        ANIMATION,
        ACT
    }

    private SpriteBatch spriteBatch;

    private final ArrayList<Coord> awaitedMoves;

    private Glider glider;

    private Phase phase = Phase.WAIT;

    private World world;

    private long animationStart;

    private Viewport viewport;

    public GameScreen() {
        viewport = new ScalingViewport(Scaling.fill, GameUtils.SCREEN_WIDTH, GameUtils.SCREEN_HEIGHT, new OrthographicCamera());
        viewport.setScreenBounds(0, 0, GameUtils.SCREEN_WIDTH, GameUtils.SCREEN_HEIGHT);
        viewport.getCamera().update();

        spriteBatch = GameUtils.getSpriteBatch();
        world = GameUtils.getCurrentWorld();

        ApplicationListener listener = Gdx.app.getApplicationListener();
        if (listener != null && listener instanceof JRPGGame) {
            JRPGGame parent = ((JRPGGame) Gdx.app.getApplicationListener());
            if (parent.getGameState() != null) {
                glider = parent.getGameState().getActor();
                glider.clear();
            }
        }

        awaitedMoves = new ArrayList<Coord>(100);
        animationStart = TimeUtils.millis();

        Gdx.input.setInputProcessor(new InputMultiplexer(new InputProcessor() {
            @Override
            public boolean keyDown(int keycode) {
                return false;
            }

            @Override
            public boolean keyUp(int keycode) {
                switch (keycode) {
                    case Input.Keys.UP: {
                            awaitedMoves.add(glider.getCoord().translate(0, 1));
                        break;
                    }
                    case Input.Keys.DOWN: {
                            awaitedMoves.add(glider.getCoord().translate(0, -1));
                        break;
                    }
                    case Input.Keys.LEFT: {
                            awaitedMoves.add(glider.getCoord().translate(-1, 0));
                        break;
                    }
                    case Input.Keys.RIGHT: {
                            awaitedMoves.add(glider.getCoord().translate(1, 0));
                        break;
                    }
                    case Input.Keys.ENTER: {
                        GameUtils.setScreen(GameScreen.this, new BattleScreen());
                        break;
                    }
                    case Input.Keys.ESCAPE: {
                        Gdx.app.exit();
                        break;
                    }
                }
                return false;
            }

            @Override
            public boolean keyTyped(char character) {
                return false;
            }

            @Override
            public boolean touchDown(final int screenX, final int screenY, int pointer, int button) {
                final Vector3 touchpoint = viewport.getCamera().unproject(new Vector3(screenX, screenY, 0));

                final int x = MathUtils.floor(touchpoint.x / GameUtils.TILE_SIZE);
                final int y = MathUtils.floor(touchpoint.y / GameUtils.TILE_SIZE);

                if(awaitedMoves.isEmpty()) {
                    animationStart = TimeUtils.millis();

                    List<Coord> coords = new ArrayList<Coord>();
                    glider.toCursor.partialScan(13, coords);
                    ArrayList<Coord> toCursor = glider.toCursor.findPathPreScanned(Coord.get(x, y));

                    System.out.println(coords + " | " + toCursor);

                    if (toCursor != null && !toCursor.isEmpty()) {
                        if (toCursor != null && !toCursor.isEmpty()) {
                            awaitedMoves.addAll(toCursor);
                        }
                    }
                    return false;
                }

                System.out.println(x + " | " + y);

                phase = Phase.WAIT;
                return false;
            }

            @Override
            public boolean touchUp(int screenX, int screenY, int pointer, int button) {
                return false;
            }

            @Override
            public boolean touchCancelled(int screenX, int screenY, int pointer, int button) {
                return false;
            }

            @Override
            public boolean touchDragged(int screenX, int screenY, int pointer) {
                return false;
            }

            @Override
            public boolean mouseMoved(int screenX, int screenY) {
                return false;
            }

            @Override
            public boolean scrolled(float amountX, float amountY) {
                return false;
            }

        }));

    }

    private void post() {
        phase = Phase.ACT;
    }

    public void move(int newX, int newY) {
        phase = Phase.ANIMATION;
        if (world.collision(newX, newY)) {
            return;
        }

        if (glider.getCoord().x - newX > 0) {
            glider.setFacing(Facing.Left);
        } else if (glider.getCoord().x - newX < 0) {
            glider.setFacing(Facing.Right);
        }

        glider.setStart(glider.getCoord());
        glider.setEnd(Coord.get(newX, newY));
        glider.setChange(0f);
        glider.setCoord(Coord.get(newX, newY));
        animationStart = TimeUtils.millis();
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 1);
        if (viewport != null && spriteBatch != null) {
            Camera camera = viewport.getCamera();
            if (camera != null) {
                viewport.apply(true);
                spriteBatch.setProjectionMatrix(camera.combined);

                camera.position.x = glider.getX() * GameUtils.TILE_SIZE;
                camera.position.y = glider.getY() * GameUtils.TILE_SIZE;
                camera.update();

                spriteBatch.begin();
                glider.render(spriteBatch, glider.getX() * GameUtils.TILE_SIZE, glider.getY() * GameUtils.TILE_SIZE, GameUtils.TILE_SIZE, GameUtils.TILE_SIZE);
                glider.act(delta);
                world.render(camera);
                spriteBatch.end();
            }
        }

        if (phase == Phase.ACT) {
            float t = TimeUtils.timeSinceMillis(animationStart) * 0x1p-7f;
            if (t >= 1f) {
                phase = Phase.WAIT;
                if (!awaitedMoves.isEmpty()) {
                    Coord m = awaitedMoves.remove(0);
                    move(m.x, m.y);
                }
            }
        } else if (phase == Phase.WAIT && !awaitedMoves.isEmpty()) {
            animationStart = TimeUtils.millis();
            Coord m = awaitedMoves.remove(0);
            move(m.x, m.y);
        } else if (phase == Phase.ANIMATION) {
            glider.setChange(TimeUtils.timeSinceMillis(animationStart) * 0.008f);
            if (glider.getChange() >= 1f) {
                animationStart = TimeUtils.millis();
                phase = Phase.WAIT;
                if (awaitedMoves.isEmpty()) {
                    if (awaitedMoves.isEmpty()) {
                        post();
                        glider.toCursor.clearGoals();
                        glider.toCursor.resetMap();
                        glider.toCursor.setGoal(glider.getCoord());
                    }
                }
            }
        }


    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
    }

    @Override
    public void dispose() {
    }

}
