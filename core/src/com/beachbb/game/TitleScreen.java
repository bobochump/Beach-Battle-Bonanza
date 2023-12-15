package com.beachbb.game;

import com.badlogic.gdx.*;
import com.badlogic.gdx.audio.Music;
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
    private Music musDefault;
    private Music musSha;
    private Music musArt;
    private Music musBod;
    private int charNum;
    private boolean muted;
    private boolean hasConnected = false;

    public TitleScreen (BeachBB game, boolean wasMuted) {
        bbbGame = game;
        serverOverlayActive = false;
        clientOverlayActive = false;
        charNum = 0;

        // load sprites for UI elements
        uiTitleTexture = new Texture(Gdx.files.internal("bbb-ui-connection.png"));
        titleSprite = new TextureRegion(uiTitleTexture, 358, 0, 471, 281);
        serverSprite = new TextureRegion(uiTitleTexture, 0, 0, 357, 53);
        clientSprite = new TextureRegion(uiTitleTexture, 0, 54, 357, 53);
        waitingSprite = new TextureRegion(uiTitleTexture, 0, 109, 357, 58);

        //load in the music
        muted = wasMuted;
        musDefault = Gdx.audio.newMusic(Gdx.files.internal("bbb-music-title.ogg"));
        musSha = Gdx.audio.newMusic(Gdx.files.internal("bbb-music-title-shark.ogg"));
        musArt = Gdx.audio.newMusic(Gdx.files.internal("bbb-music-title-artificer.ogg"));
        musBod = Gdx.audio.newMusic(Gdx.files.internal("bbb-music-title-bodega.ogg"));
        musDefault.setLooping(false);
        musSha.setLooping(false);
        musArt.setLooping(false);
        musBod.setLooping(false);
        swapMusic();
        musDefault.play();
        musSha.play();
        musArt.play();
        musBod.play();

        //code to swap music to the looped version
        musDefault.setOnCompletionListener(new Music.OnCompletionListener() {
            @Override
            public void onCompletion(Music music) {
                System.out.println("Looped");
                musDefault = Gdx.audio.newMusic(Gdx.files.internal("bbb-music-title-loop.ogg"));
                musSha = Gdx.audio.newMusic(Gdx.files.internal("bbb-music-title-loop-shark.ogg"));
                musArt = Gdx.audio.newMusic(Gdx.files.internal("bbb-music-title-loop-artificer.ogg"));
                musBod = Gdx.audio.newMusic(Gdx.files.internal("bbb-music-title-loop-bodega.ogg"));
                musDefault.setLooping(true);
                musSha.setLooping(true);
                musArt.setLooping(true);
                musBod.setLooping(true);
                swapMusic();  //bit of a hack, but whatevs
                musDefault.play();
                musSha.play();
                musArt.play();
                musBod.play();
            }
        });

    }
    public void show() {
        Gdx.app.log("TitleScreen", "show");
    }

    private void swapMusic(){
        //mute everything
        musDefault.setVolume(0);
        musSha.setVolume(0);
        musArt.setVolume(0);
        musBod.setVolume(0);
        if(!muted){
            //only play correct track if we are not muted
            switch (charNum){
                case 1:
                    musSha.setVolume(0.5f);
                    break;
                case 2:
                    musArt.setVolume(0.5f);
                    break;
                case 3:
                    musBod.setVolume(0.5f);
                    break;
                default:
                    musDefault.setVolume(0.5f);
                    break;
            }
        }
    }

    private void toggleMute(){
        //mute everything
        musDefault.setVolume(0);
        musSha.setVolume(0);
        musArt.setVolume(0);
        musBod.setVolume(0);

        //check if we were already muted
        if(muted){
            muted = false;
            //switch case to unmute correct track
            switch(charNum){
                case 1:
                    musSha.setVolume(0.5f);
                    break;
                case 2:
                    musArt.setVolume(0.5f);
                    break;
                case 3:
                    musBod.setVolume(0.5f);
                    break;
                default:
                    musDefault.setVolume(0.5f);
                    break;
            }
        } else {
            muted = true;
        }

    }

    public void update(float delta) {
        // set up a play screen with the player type equal to the number pressed
        // later, change to wait for other player's input before moving to PlayScreen
        if(state == 0) {
            if (Gdx.input.isKeyJustPressed(Input.Keys.S)) {
                state = 1;
                network = new Server(address, 1234, queue);
                network.start();
                serverOverlayActive = true;
            }
            else if(Gdx.input.isKeyJustPressed(Input.Keys.C)){
                state = 1;
                network = new Client(address, 1234, queue);
                network.start();
                clientOverlayActive = true;
                //TODO: Figure out how to retry client connections if the server refuses/does not exist yet
            }
        }
        boolean charSwapFlag = false;
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_1) || Gdx.input.isKeyJustPressed(Input.Keys.NUMPAD_1)) {
            charNum = 1;
            charSwapFlag = true;
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_2) || Gdx.input.isKeyJustPressed(Input.Keys.NUMPAD_2)) {
            charNum = 2;
            charSwapFlag = true;
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_3) || Gdx.input.isKeyJustPressed(Input.Keys.NUMPAD_3)) {
            charNum = 3;
            charSwapFlag = true;
        }
        //swap music and attempt to send char over network only when swapping characters
        if(charSwapFlag){
            swapMusic();
            if(state == 1 && network.isConnected()){  //only send if network has been initialized, and we are connected
                network.sendCharCommand(charNum);
            }
        }
        //upon first connecting, send the current char
        //this is so you can select char, then connect
        if(!hasConnected && state == 1 && network.isConnected()){
            hasConnected = true;
            if(charNum != 0){
                network.sendCharCommand(charNum);
            }
        }
        if(Gdx.input.isKeyJustPressed(Input.Keys.M)){
            toggleMute();
        }



        //wait until we are networked, queue is not empty, and player has chosen a character to proceed
        if(state == 1 && !queue.isEmpty() && charNum != 0) {
            do{
                charMessage = queue.poll().substring(2);
            } while(!queue.isEmpty());
            musDefault.dispose();
            musSha.dispose();
            musArt.dispose();
            musBod.dispose();
            bbbGame.setScreen(new PlayScreen(bbbGame, charNum, Integer.parseInt(charMessage), network, queue, muted));
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