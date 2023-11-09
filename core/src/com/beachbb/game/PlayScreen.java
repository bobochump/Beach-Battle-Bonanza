package com.beachbb.game;

import com.badlogic.gdx.*;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.utils.ScreenUtils;

import java.lang.Math;
import java.util.ArrayList;
import java.util.Iterator;

public class PlayScreen extends ScreenAdapter {
    private BeachBB bbbGame;
    private Player player1;
    private Player player2;
    private enum SubState { PREP, PLAY, PAUSE, END };
    private SubState state;
    private ArrayList<Tile> grid;

    public PlayScreen(BeachBB game, int p1, int p2) {
        bbbGame = game;
        grid = new ArrayList<>(30);
        player1 = new Player(p1, 1);
        player2 = new Player(p2, 2);

        int tileCount = 0;
        for (int col = 0; col < 6; col++) {
            for (int row = 0; row < 5; row++) {
                if (tileCount < 15) {
                    grid.add(new Tile(tileCount, row, col, 1));
                } else {
                    grid.add(new Tile(tileCount, row, col, 2));
                }
                tileCount++;
            }
        }
    }

    public void show() {
        Gdx.app.log("PlayScreen", "show");
        state = SubState.PREP;
    }

    public void update(float delta) {
        if (state == SubState.PREP) {
            // makes sure both players are synced and initiates countdown
            // move onto play state once countdown finishes
        }
    }

    public void render(float delta) {
        update(delta);
        ScreenUtils.clear(1,1,1,1);
        bbbGame.batch.begin();

        bbbGame.batch.draw(bbbGame.am.get(bbbGame.TEX_SCREEN_GAME, Texture.class), 0, 0);

        for (Iterator<Tile> ti = grid.iterator(); ti.hasNext();) {
            Tile t = ti.next();
            t.drawTile(bbbGame.batch);
        }

        player1.drawPlayer(bbbGame.batch);
        player2.drawPlayer(bbbGame.batch);

        bbbGame.batch.end();
    }
}
