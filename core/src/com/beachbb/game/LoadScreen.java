package com.beachbb.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.ScreenUtils;

public class LoadScreen extends ScreenAdapter {
    BeachBB bbbGame;
    boolean titleShown;
    public LoadScreen(BeachBB game) {
        bbbGame = game;
        titleShown = false;
    }

    public void show() {
        Gdx.app.log("LoadScreen", "show");
    }

    public void render(float delta) {
        ScreenUtils.clear(1, 1, 1, 1);
        bbbGame.am.update(10);

        if (bbbGame.am.isLoaded(bbbGame.TEX_SCREEN_LOADING)) {
            bbbGame.batch.begin();
            bbbGame.batch.draw(bbbGame.am.get(bbbGame.TEX_SCREEN_LOADING, Texture.class), 0, 0);
            bbbGame.batch.end();
        }

        if (bbbGame.am.isFinished() && (Gdx.input.isKeyPressed(Input.Keys.ANY_KEY)) && !titleShown) {
            titleShown = true;
            bbbGame.setScreen(new TitleScreen(bbbGame, false));
        }

        Gdx.input.setInputProcessor(new InputAdapter() {
            public boolean touchUp(int x, int y, int pointer, int button) {
            if (bbbGame.am.isFinished() && !titleShown) {
                titleShown = true;
                bbbGame.setScreen(new TitleScreen(bbbGame, false));
            }
            return true;
            }
        });
    }
}
