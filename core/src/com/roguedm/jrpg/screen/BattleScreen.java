package com.roguedm.jrpg.screen;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScalingViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.roguedm.jrpg.AssetManager;
import com.roguedm.jrpg.GameUtils;
import com.roguedm.jrpg.JRPGGame;
import com.roguedm.jrpg.object.Facing;
import com.roguedm.jrpg.object.menu.Menu;
import com.roguedm.jrpg.object.hero.Hero;
import com.roguedm.jrpg.object.Monster;
import com.roguedm.jrpg.object.fx.EffectManager;
import com.roguedm.jrpg.object.fx.SlashEffect;

import squidpony.squidmath.Coord;

public class BattleScreen implements Screen {

    public static final int OFFSET_X = 100;
    public static final int OFFSET_Y = 100;

    private SpriteBatch spriteBatch;
    private Viewport viewport;

    private EffectManager effectManager;

    private Array<Hero> heroes;
    private Array<Monster> monsters;

    private Menu menu;

    private BitmapFont font;


    public BattleScreen() {
        this.viewport = new ScalingViewport(Scaling.fill, GameUtils.SCREEN_WIDTH, GameUtils.SCREEN_HEIGHT, new OrthographicCamera());
        this.viewport.setScreenBounds(0, 0, GameUtils.SCREEN_WIDTH, GameUtils.SCREEN_HEIGHT);
        this.viewport.getCamera().update();

        this.spriteBatch = GameUtils.getSpriteBatch();
        this.effectManager = new EffectManager();
        this.font = AssetManager.getInstance().getFont();

        this.menu = new Menu();

        this.heroes = new Array<Hero>();
        ApplicationListener listener = Gdx.app.getApplicationListener();
        if (listener != null && listener instanceof JRPGGame) {
            JRPGGame parent = ((JRPGGame) Gdx.app.getApplicationListener());
            if (parent.getGameState() != null) {
                this.heroes = parent.getGameState().getParty();
                if (this.heroes != null) {
                    int c = 0;
                    for (Hero hero : this.heroes) {
                        if (hero != null) {
                            hero.set(Coord.get(OFFSET_X, GameUtils.SCREEN_HEIGHT - OFFSET_Y - (c++ * OFFSET_Y)));
                            hero.update();
                        }
                    }
                }
            }
        }

        this.monsters = new Array<Monster>();
        addMonster();
        addMonster();

        Gdx.input.setInputProcessor(new InputMultiplexer(new InputProcessor() {
            @Override
            public boolean keyDown(int keycode) {
                return false;
            }

            @Override
            public boolean keyUp(int keycode) {
                switch (keycode) {
                    case Input.Keys.ENTER: {
                        GameUtils.setScreen(BattleScreen.this, new GameScreen());
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
                if (monsters != null) {
                    for (final Monster monster : monsters) {
                        if (monster != null && monster.getCoord() != null) {
                            int x = monster.getCoord().getX();
                            Rectangle bounds = monster.getBoundingRectangle();
                            if (bounds != null && bounds.contains(touchpoint.x, touchpoint.y)) {
                                SlashEffect slash = new SlashEffect(GameUtils.TILE_SIZE, 0, 0);
                                slash.initialize(monster.getCoord(), monster.getCoord());
                                slash.addListener(new EventListener() {
                                    @Override
                                    public boolean handle(Event event) {
                                        monster.damage(-1);
                                        if (!isMonsterAlive()) {
                                            GameUtils.setScreen(BattleScreen.this, new GameScreen());
                                        }
                                        return false;
                                    }
                                });
                                effectManager.addEffect(slash);
                            }
                        }
                    }
                }
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

    public void addMonster() {
        if (monsters != null) {
            Monster monster = new Monster();
            monster.init();
            monster.setFacing(Facing.Left);
            monster.set(Coord.get(GameUtils.SCREEN_WIDTH - OFFSET_Y - GameUtils.TILE_SIZE, GameUtils.SCREEN_HEIGHT - ((monsters.size + 1) * OFFSET_Y)));
            monster.update();
            monsters.add(monster);
        }
    }

    public boolean isMonsterAlive() {
        if (monsters != null) {
            for (Monster monster : monsters) {
                if (monster != null) {
                    if (monster.isAlive()) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 1);
        if (viewport != null && spriteBatch != null) {
            viewport.apply(true);
            Camera camera = viewport.getCamera();
            if (camera != null) {
                spriteBatch.setProjectionMatrix(camera.combined);
                camera.update();

                spriteBatch.begin();

                if (heroes != null) {
                    for (Hero hero : heroes) {
                        if (hero != null) {
                            hero.render(spriteBatch);
                            hero.act(delta);
                        }
                    }
                }

                if (monsters != null) {
                    for (Monster monster : monsters) {
                        if (monster != null) {
                            monster.render(spriteBatch);
                            monster.act(delta);
                        }
                    }
                }

                effectManager.render(spriteBatch);


                menu.draw(spriteBatch, font, 10, 120);


                spriteBatch.end();

                effectManager.act(delta);
            }
        }
    }

    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    @Override
    public void show() {

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
        if (monsters != null) {
            for (Monster monster : monsters) {
                if (monster != null) {
                    monster.dispose();
                }
            }
        }
        if (effectManager != null) {
            effectManager.dispose();
        }
    }

}
