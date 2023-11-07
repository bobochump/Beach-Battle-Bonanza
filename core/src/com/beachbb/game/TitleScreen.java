package com.beachbb.game;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.ScreenUtils;

public class TitleScreen extends ScreenAdapter {
    private BeachBB bbbGame;
    private boolean waitingOverlayActive;
    private boolean instructionsOverlayActive;
    private boolean creditsOverlayActive;
    public TitleScreen (BeachBB game) {
        bbbGame = game;
        waitingOverlayActive = false;
        instructionsOverlayActive = false;
        creditsOverlayActive = false;
    }
    public void show() {
        Gdx.app.log("LoadScreen", "show");
    }

    public void update(float delta) {
        // set up a play screen with the player type equal to the number pressed
        // later, change to wait for other player's input before moving to PlayScreen
        if (Gdx.input.isKeyPressed(Input.Keys.NUM_1) || Gdx.input.isKeyPressed(Input.Keys.NUMPAD_1)) {
            bbbGame.setScreen(new PlayScreen(bbbGame, 1, 3));
        } else if (Gdx.input.isKeyPressed(Input.Keys.NUM_2) || Gdx.input.isKeyPressed(Input.Keys.NUMPAD_2)) {
            bbbGame.setScreen(new PlayScreen(bbbGame, 2, 3));
        } else if (Gdx.input.isKeyPressed(Input.Keys.NUM_3) || Gdx.input.isKeyPressed(Input.Keys.NUMPAD_3)) {
            bbbGame.setScreen(new PlayScreen(bbbGame, 3, 3));
        }
        /*
        // input logic to open and close overlay (use for instructions and credits pop-up)
        Gdx.input.setInputProcessor(new InputAdapter() {
            public boolean touchDown(int x, int y, int pointer, int button) {
                if (!creditsOverlayActive) {
                    if (x > 878 && x < 1236) {
                        creditsOverlayActive = true;
                    }
                } else {
                    creditsOverlayActive = false;
                }
                return true;
            }
            public boolean touchUp(int x, int y, int pointer, int button) {
                return true;
            }
        });
        */
    }
    public void render(float delta) {
        update(delta);

        ScreenUtils.clear(1,1,1,1);

        bbbGame.batch.begin();
        bbbGame.batch.draw(bbbGame.am.get(bbbGame.TEX_SCREEN_TITLE, Texture.class), 0, 0);
        //if (creditsOverlayActive) {
            // draw popup box
        //}
        bbbGame.batch.end();

    }
}