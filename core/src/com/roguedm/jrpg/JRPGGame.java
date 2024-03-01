package com.roguedm.jrpg;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.roguedm.jrpg.screen.GameScreen;


/***
 *
 * Lock screen while moving
 * Clean up Game Screen
 * Clean up World to render more efficient
 *
 *
 * @Todo Fonts
 * @Todo Level Objects
 *
 * @Todo PartyScreen
 *
 * @Todo Character
 *  - Character Actions
 *
 */
public class JRPGGame extends Game {

	private GameState gameState;

	private SpriteBatch spriteBatch;

	@Override
	public void create () {
		AssetManager.getInstance().load();

		gameState = new GameState();
		spriteBatch = new SpriteBatch();
		setScreen(new GameScreen());
	}

	public SpriteBatch getSpriteBatch() {
		return spriteBatch;
	}

	public GameState getGameState() {
		return gameState;
	}

	@Override
	public void dispose () {
		AssetManager.dispose();
		if(this.getScreen() != null) {
			this.getScreen().dispose();
		}
		if(this.gameState != null) {
			this.gameState.dispose();
		}
		if(this.spriteBatch != null) {
			this.spriteBatch.dispose();
		}
	}

}
