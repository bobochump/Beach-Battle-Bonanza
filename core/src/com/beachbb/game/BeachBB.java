package com.beachbb.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

public class BeachBB extends Game {
	AssetManager am;
	SpriteBatch batch;
	public static final String TEX_SCREEN_LOADING = "base-loading-screen.png";
	public static final String TEX_SCREEN_TITLE = "bbb-base-title.png";
	public static final String TEX_SCREEN_GAME = "bbb-base-game-screen.png";
	public static final String TEX_SHEET_TILE = "bbb-base-tiles.png";
	public static final String TEX_SHEET_CARD_BACK = "bbb-base-card-back.png";
	public static final String TEX_SHEET_PLAYER1 = "bbb-base-sprite-shark.png";
	public static final String TEX_SHEET_PLAYER2 = "bbb-base-sprite-mage.png";
	public static final String TEX_SHEET_PLAYER3 = "bbb-base-sprite-rat.png";
	
	@Override
	public void create () {
		am = new AssetManager();

		am.load(TEX_SCREEN_LOADING, Texture.class);
		am.load(TEX_SCREEN_TITLE, Texture.class);
		am.load(TEX_SCREEN_GAME, Texture.class);
		am.load(TEX_SHEET_TILE, Texture.class);
		am.load(TEX_SHEET_CARD_BACK, Texture.class);
		am.load(TEX_SHEET_PLAYER1, Texture.class);
		am.load(TEX_SHEET_PLAYER2, Texture.class);
		am.load(TEX_SHEET_PLAYER3, Texture.class);

		batch = new SpriteBatch();

		setScreen(new LoadScreen(this));
	}
	
	@Override
	public void dispose () {
		am.dispose();
		batch.dispose();
	}
}
