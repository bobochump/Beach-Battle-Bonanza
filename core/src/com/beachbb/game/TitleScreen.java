package com.beachbb.game;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.ScreenUtils;
import java.util.concurrent.*;

public class TitleScreen extends ScreenAdapter {
    private BeachBB bbbGame;
    private boolean serverOverlayActive;
    private boolean clientOverlayActive;
    private Texture uiTitleTexture;
    private TextureRegion titleSprite;
    private TextureRegion serverSprite;
    private TextureRegion clientSprite;
    private TextureRegion waitingSprite;
    private int state = 0;
    private String charMessage;
    private ConcurrentLinkedQueue<String> queue = new ConcurrentLinkedQueue<String>();
    private NetworkEntity network;
    private String address = "127.0.0.1";

    public TitleScreen (BeachBB game) {
        bbbGame = game;
        serverOverlayActive = false;
        clientOverlayActive = false;

        // load sprites for UI elements
        uiTitleTexture = new Texture(Gdx.files.internal("bbb-ui-connection.png"));
        titleSprite = new TextureRegion(uiTitleTexture, 358, 0, 471, 281);
        serverSprite = new TextureRegion(uiTitleTexture, 0, 0, 357, 53);
        clientSprite = new TextureRegion(uiTitleTexture, 0, 54, 357, 53);
        waitingSprite = new TextureRegion(uiTitleTexture, 0, 109, 357, 58);
    }
    public void show() {
        Gdx.app.log("TitleScreen", "show");
    }

    public void update(float delta) {
        // set up a play screen with the player type equal to the number pressed
        // later, change to wait for other player's input before moving to PlayScreen
        if (Gdx.input.isKeyPressed(Input.Keys.S) && state == 0) {
            state = 1;
            network = new Server(address, 1234, queue);
            network.start();
            serverOverlayActive = true;
        }
        else if(Gdx.input.isKeyPressed(Input.Keys.C) && state == 0){
            state = 1;
            network = new Client(address, 1234, queue);
            network.start();
            clientOverlayActive = true;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.NUM_1) || Gdx.input.isKeyPressed(Input.Keys.NUMPAD_1)) {
            if (state == 1) network.sendCharCommand(1);
            // Wait until the queue is not empty before proceeding
            while (queue.isEmpty()) {
                // You can add a short delay here to avoid tight-looping and excessive CPU usage
                try {
                    Thread.sleep(100); // Adjust the sleep duration as needed
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            // Now, you can safely poll the queue
            charMessage = queue.poll().substring(2);
            if (state == 1) bbbGame.setScreen(new PlayScreen(bbbGame, 1, Integer.parseInt(charMessage), network, queue));

        } else if (Gdx.input.isKeyPressed(Input.Keys.NUM_2) || Gdx.input.isKeyPressed(Input.Keys.NUMPAD_2)) {
            if (state == 1) network.sendCharCommand(2);
            // Wait until the queue is not empty before proceeding
            while (queue.isEmpty()) {
                // You can add a short delay here to avoid tight-looping and excessive CPU usage
                try {
                    Thread.sleep(100); // Adjust the sleep duration as needed
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            // Now, you can safely poll the queue
            charMessage = queue.poll().substring(2);
            if (state == 1) bbbGame.setScreen(new PlayScreen(bbbGame, 2, Integer.parseInt(charMessage), network, queue));

        } else if (Gdx.input.isKeyPressed(Input.Keys.NUM_3) || Gdx.input.isKeyPressed(Input.Keys.NUMPAD_3)) {
            if (state == 1) network.sendCharCommand(3);
            // Wait until the queue is not empty before proceeding
            while (queue.isEmpty()) {
                // You can add a short delay here to avoid tight-looping and excessive CPU usage
                try {
                    Thread.sleep(100); // Adjust the sleep duration as needed
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            // Now, you can safely poll the queue
            charMessage = queue.poll().substring(2);
            if (state == 1) bbbGame.setScreen(new PlayScreen(bbbGame, 3, Integer.parseInt(charMessage), network, queue));
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
        if (serverOverlayActive) {
            bbbGame.batch.draw(serverSprite, 0, 719);
            bbbGame.batch.draw(waitingSprite, 882, 35);
        }
        if (clientOverlayActive) {
            bbbGame.batch.draw(clientSprite, 0, 719);
            bbbGame.batch.draw(waitingSprite, 882, 35);
        }
        bbbGame.batch.draw(titleSprite, 772, 494);
        bbbGame.batch.end();

    }
}