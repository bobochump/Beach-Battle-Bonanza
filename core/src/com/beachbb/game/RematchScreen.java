package com.beachbb.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.ScreenUtils;

import java.util.concurrent.ConcurrentLinkedQueue;

public class RematchScreen extends ScreenAdapter {

    private BeachBB bbbGame;
    private Texture uiTitleTexture;
    private TextureRegion titleSprite;
    private TextureRegion waitingSprite;
    private String charMessage;
    private ConcurrentLinkedQueue<String> queue;
    private NetworkEntity network;
    private boolean muted ;
    private Music musDefault;
    private Music musSha;
    private Music musArt;
    private Music musBod;
    private int charNum;

    public RematchScreen (BeachBB game, NetworkEntity connection, ConcurrentLinkedQueue<String> Q, boolean wasMuted) {
        bbbGame = game;
        network = connection;
        queue = Q;
        queue.clear();
        muted = wasMuted;
        charNum = 0;

        // load sprites for UI elements
        uiTitleTexture = new Texture(Gdx.files.internal("bbb-ui-connection.png"));
        titleSprite = new TextureRegion(uiTitleTexture, 358, 0, 471, 281);
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
            network.sendCharCommand(charNum);
        }
        if(Gdx.input.isKeyJustPressed(Input.Keys.M)){
            toggleMute();
        }

        //wait until queue is not empty, and player has chosen a character to proceed
        if(!queue.isEmpty() && charNum != 0) {
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
        bbbGame.batch.draw(waitingSprite, 882, 35);
        bbbGame.batch.draw(titleSprite, 772, 494);
        bbbGame.batch.end();

    }
}

