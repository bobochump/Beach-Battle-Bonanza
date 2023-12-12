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
import java.util.concurrent.ConcurrentLinkedQueue;

public class PlayScreen extends ScreenAdapter {
    private BeachBB bbbGame;
    private Player player1;
    private Player player2;
    private enum SubState { PREP, PLAY, PAUSE, END };
    private SubState state;
    private ArrayList<Tile> grid;
    private NetworkEntity network;
    private ConcurrentLinkedQueue<String> queue;
    private Deck playersDeck;
    private int currentMana;
    private float p1hpPercent;
    private float p2hpPercent;
    private ArrayList<AttackEntity> currentAttacks;
    private AttackConstructor attackConstructor;
    private Texture uiTexture;
    private TextureRegion hpBarP1;
    private TextureRegion hpBarP2;
    private TextureRegion manaBar;
    private TextureRegion deckBar;
    private boolean oppRematch;
    private boolean playerRematch;

    public PlayScreen(BeachBB game, int p1, int p2, NetworkEntity playerNetwork, ConcurrentLinkedQueue<String> playerQueue) {
        bbbGame = game;
        player1 = new Player(p1, 1);
        player2 = new Player(p2, 2);
        network = playerNetwork;
        queue = playerQueue;
        playersDeck = new Deck(p1);
        currentMana = 1000;
        p1hpPercent = 100;
        p2hpPercent = 100;
        oppRematch = false;
        playerRematch = false;

        // make a grid and add tiles to it
        grid = new ArrayList<>(30);
        attackConstructor = new AttackConstructor();
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

        currentAttacks = new ArrayList<>(10);

        // load sprites for UI elements
        uiTexture = new Texture(Gdx.files.internal("bbb-bars.png"));
        hpBarP1 = new TextureRegion(uiTexture, 60, 0, 616, 24);
        hpBarP2 = new TextureRegion(uiTexture, 60, 0, 616, 24);
        manaBar = new TextureRegion(uiTexture, 0, 0, 53, 152);
        deckBar = new TextureRegion(uiTexture, 63, 70, 69, 82);
    }

    public void show() {
        Gdx.app.log("PlayScreen", "show");
        state = SubState.PREP;
    }

    public void update(float delta) {
        if (state == SubState.PREP) {
            // to-do: make sure both players are synced and initiates countdown
            // move onto play state once countdown finishes
            currentMana = 1000;
            p1hpPercent = 1;
            p2hpPercent = 1;
            player1.resetHP();
            player2.resetHP();
            p1hpPercent = player1.getPercentageHP();
            network.sendOpponentHP(p1hpPercent);
            state = SubState.PLAY;
        }
        final float currentDelta = delta; //gives me error if I try to pass delta into the second attack constructor. Not the first one tho?
        if (state == SubState.PLAY) {
            if (!queue.isEmpty()) {
                String command;
                command = queue.poll();
                String commandType = command.substring(0, 2);

                // Checks for command types
                if (commandType.equals("01")) { // Checks for movement command
                    String movement = command.substring(2);
                    player2.movePlayer(Integer.parseInt(movement));
                } else if (commandType.equals("02")) { // Checks for attack command
                    currentAttacks.add(attackConstructor.buildAttack(Integer.parseInt(command.substring(2)), currentDelta, player2.getTileX(), player2.getTileY()));
                }
                if (commandType.equals("04")) { // Checks opponent's hp percentage
                    String parseHP = command.substring(2);
                    p2hpPercent = (Float.parseFloat(parseHP));
                }
            }

            Gdx.input.setInputProcessor(new InputAdapter() {
                public boolean keyDown(int keycode) {
                    switch (keycode) {
                        case Input.Keys.W:
                            network.sendMoveCommand(3);
                            player1.movePlayer(1);
                            break;
                        case Input.Keys.D:
                            network.sendMoveCommand(4);
                            player1.movePlayer(2);
                            break;
                        case Input.Keys.S:
                            network.sendMoveCommand(1);
                            player1.movePlayer(3);
                            break;
                        case Input.Keys.A:
                            network.sendMoveCommand(2);
                            player1.movePlayer(4);
                            break;
                        case Input.Keys.UP:
                            //player2.movePlayer(1);
                            playersDeck.changeSelection(-1);
                            break;
                        case Input.Keys.RIGHT:
                            //player2.movePlayer(2);
                            break;
                        case Input.Keys.DOWN:
                            //player2.movePlayer(3);
                            playersDeck.changeSelection(-2);
                            break;
                        case Input.Keys.LEFT:
                            //player2.movePlayer(4);
                            break;
                        case Input.Keys.NUMPAD_1:
                            playersDeck.changeSelection(0);
                            break;
                        case Input.Keys.NUMPAD_2:
                            playersDeck.changeSelection(1);
                            break;
                        case Input.Keys.NUMPAD_3:
                            playersDeck.changeSelection(2);
                            break;
                        case Input.Keys.NUMPAD_4:
                            playersDeck.changeSelection(3);
                            break;
                        case Input.Keys.NUMPAD_5:
                            playersDeck.changeSelection(4);
                            break;
                        case Input.Keys.ENTER:
                        case Input.Keys.NUMPAD_ENTER:
                            int effectID = playersDeck.useCard(currentMana);
                            if (effectID == -1) {
                                //play sound to say that you cant use that card
                            } else {
                                //summon the effect and send it to the other player
                                currentAttacks.add(attackConstructor.buildAttack(effectID, currentDelta, player1.getTileX(), player1.getTileY()));
                                network.sendAttackCommand(effectID);
                            }
                    }
                    return true;
                }
            });

            ArrayList<AttackEntity> attacksToKeep = new ArrayList<>();

            for (AttackEntity a : currentAttacks) {
                int returnVal = a.updateAttack(delta, grid);
                attacksToKeep.add(a);
                if (returnVal == 1) {
                    attacksToKeep.remove(a);
                }
            }
            currentAttacks.clear();
            currentAttacks.addAll(attacksToKeep);
            attacksToKeep.clear();

            // collision check for your player and danger tile; subtracts multiplier * 1 hp per tick
            if (grid.get(player1.getPlayerTileID()).getDanger()) {
                player1.takeDamage(2.0f);

                // update hp values, then ends game when one player's hp reaches 0
                p1hpPercent = player1.getPercentageHP();
                network.sendOpponentHP(p1hpPercent);
            }

            if (p1hpPercent <= 0 || p2hpPercent <= 0) {
                state = SubState.END;
            }
        }

        if (state == SubState.END) { // The state for when the game has ended
            if (!queue.isEmpty()) {
                String command;
                command = queue.poll();
                String commandType = command.substring(0, 2);
                if (commandType.equals("98")){ // Checks for rematch command
                    oppRematch = true;
                }
                if (commandType.equals("99")){ // Checks for exit command
                    bbbGame.setScreen(new TitleScreen(bbbGame)); // Sets to titlescreen is opponent disconnected
                }
            }
            if(playerRematch && oppRematch){ // Checks if both players accepted rematch.
                System.out.println("Rematch accepted.");
            }
            Gdx.input.setInputProcessor(new InputAdapter() {
                public boolean keyDown(int keycode) {
                    switch(keycode){
                        case Input.Keys.ESCAPE: // Checks if the player hit escape and ends the connection
                            System.out.println("Ending connection.");
                            network.sendEndConnection(); // Sends a message to tell the other player to end connection
                            network.stopNetwork(); // Stops the network entity
                            bbbGame.setScreen(new TitleScreen(bbbGame)); // Sets game back to title screen
                            break;
                        case Input.Keys.SPACE: // Checks if the player hit space and prepares for rematch
                            if(!playerRematch) {
                                System.out.println("Rematch initiated.");
                                network.sendRematch(); // Sends a message to tell other player that they want to rematch
                                playerRematch = true;
                            }

                    }
                    return true;
                }
            });
        }
    }

    public void render(float delta) {
        update(delta);
        ScreenUtils.clear(1,1,1,1);
        bbbGame.batch.begin();

        bbbGame.batch.draw(bbbGame.am.get(bbbGame.TEX_SCREEN_GAME, Texture.class), 0, 0);

        // draw UI elements
        bbbGame.batch.draw(hpBarP1, 172, 40, (616*p1hpPercent), 24);
        bbbGame.batch.draw(hpBarP2, 172, 740, (616*p2hpPercent), 24);
        bbbGame.batch.draw(deckBar, 860, 694);
        bbbGame.batch.draw(manaBar, 876, 70);

        playersDeck.DrawDeck(bbbGame.batch);

        // draw grid, players, and effects
        for (Iterator<Tile> ti = grid.iterator(); ti.hasNext();) {
            Tile t = ti.next();
            t.drawTile(bbbGame.batch);
        }

        player2.drawPlayer(bbbGame.batch);

        for (AttackEntity a : currentAttacks) {
            a.drawAttack(bbbGame.batch);
        }

        player1.drawPlayer(bbbGame.batch);

        if (state == SubState.END) {
            if (p1hpPercent <= 0) {
                bbbGame.batch.draw(bbbGame.am.get(bbbGame.TEX_OVERLAY_LOSE, Texture.class), 0, 0);
            } else {
                bbbGame.batch.draw(bbbGame.am.get(bbbGame.TEX_OVERLAY_WIN, Texture.class), 0, 0);
            }
        }

        bbbGame.batch.end();
    }
}
