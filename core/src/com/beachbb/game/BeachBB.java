package com.beachbb.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

public class BeachBB extends Game {
	AssetManager am;
	SpriteBatch batch;
	public static final String TEX_SCREEN_LOADING = "base-loading-screen.png";
	public static final String TEX_SCREEN_TITLE = "bbb-title-artwork.png";
	public static final String TEX_SHEET_TITLE = "bbb-ui-connection.png";
	public static final String TEX_SCREEN_GAME = "bbb-final-game-screen.png";
	public static final String TEX_SHEET_TILE = "bbb-base-tiles.png";
	public static final String TEX_SHEET_CARD_BACK = "bbb-base-card-back.png";
	public static final String TEX_SHEET_CARD_FRONT = "bbb-card-front2.png";
	public static final String TEX_SHEET_PLAYER1 = "bbb-sprite-sheet-shark.png";
	public static final String TEX_SHEET_PLAYER2 = "bbb-sprite-sheet-mage.png";
	public static final String TEX_SHEET_PLAYER3 = "bbb-sprite-sheet-rat.png";
	public static final String TEX_SHEET_UI = "bbb-bars.png";
	public static final String TEX_SHEET_BULLET = "bbb-bullet.png";
	public static final String TEX_OVERLAY_WAITING = "bbb-base-waiting.png";
	public static final String TEX_OVERLAY_WIN = "bbb-base-win01.png";
	public static final String TEX_OVERLAY_WIN2 = "bbb-base-win02.png";
	public static final String TEX_OVERLAY_LOSE = "bbb-base-lose01.png";
	public static final String TEX_OVERLAY_LOSE2 = "bbb-base-lose02.png";
	public static final String MUS_TITLE = "bbb-music-title.ogg";
	public static final String MUS_TITLE_SHA = "bbb-music-title-shark.ogg";
	public static final String MUS_TITLE_ART = "bbb-music-title-artificer.ogg";
	public static final String MUS_TITLE_BOD = "bbb-music-title-bodega.ogg";
	public static final String MUS_TITLE_LOOP = "bbb-music-title-loop.ogg";
	public static final String MUS_TITLE_LOOP_SHA = "bbb-music-title-loop-shark.ogg";
	public static final String MUS_TITLE_LOOP_ART = "bbb-music-title-loop-artificer.ogg";
	public static final String MUS_TITLE_LOOP_BOD = "bbb-music-title-loop-bodega.ogg";
	
	@Override
	public void create () {
		am = new AssetManager();

		am.load(TEX_SCREEN_LOADING, Texture.class);
		am.load(TEX_SCREEN_TITLE, Texture.class);
		am.load(TEX_SHEET_TITLE, Texture.class);
		am.load(TEX_SCREEN_GAME, Texture.class);
		am.load(TEX_SHEET_TILE, Texture.class);
		am.load(TEX_SHEET_CARD_BACK, Texture.class);
		am.load(TEX_SHEET_CARD_FRONT, Texture.class);
		am.load(TEX_SHEET_PLAYER1, Texture.class);
		am.load(TEX_SHEET_PLAYER2, Texture.class);
		am.load(TEX_SHEET_PLAYER3, Texture.class);
		am.load(TEX_SHEET_UI, Texture.class);
		am.load(TEX_SHEET_BULLET, Texture.class);
		am.load(TEX_OVERLAY_WAITING, Texture.class);
		am.load(TEX_OVERLAY_WIN, Texture.class);
		am.load(TEX_OVERLAY_WIN2, Texture.class);
		am.load(TEX_OVERLAY_LOSE, Texture.class);
		am.load(TEX_OVERLAY_LOSE2, Texture.class);
		am.load(MUS_TITLE, Music.class);
		am.load(MUS_TITLE_SHA, Music.class);
		am.load(MUS_TITLE_ART, Music.class);
		am.load(MUS_TITLE_BOD, Music.class);
		am.load(MUS_TITLE_LOOP, Music.class);
		am.load(MUS_TITLE_LOOP_SHA, Music.class);
		am.load(MUS_TITLE_LOOP_ART, Music.class);
		am.load(MUS_TITLE_LOOP_BOD, Music.class);


		batch = new SpriteBatch();

		setScreen(new LoadScreen(this));
	}
	
	@Override
	public void dispose () {
		am.dispose();
		batch.dispose();
	}
}
