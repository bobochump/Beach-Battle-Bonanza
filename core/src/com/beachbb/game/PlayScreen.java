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
    private Server server;
    private ArrayList<Tile> grid;
    private NetworkEntity network;
    private ConcurrentLinkedQueue<String> queue;
    private Deck playersDeck;

    public PlayScreen(BeachBB game, int p1, int p2, NetworkEntity playerNetwork, ConcurrentLinkedQueue<String> playerQueue) {
        bbbGame = game;
        player1 = new Player(p1, 1);
        player2 = new Player(p2, 2);
        network = playerNetwork;
        queue = playerQueue;
        playersDeck = new Deck(p1);

        // make a grid and add tiles to it
        grid = new ArrayList<>(30);
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
            // to-do: make sure both players are synced and initiates countdown
            // move onto play state once countdown finishes
            state = SubState.PLAY;
        }
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
                        case Input.Keys.NUMPAD_2:_2:
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

        for (Iterator<Tile> ti = grid.iterator(); ti.hasNext();) {
            Tile t = ti.next();
            t.drawTile(bbbGame.batch);
        }

        player2.drawPlayer(bbbGame.batch);
        player1.drawPlayer(bbbGame.batch);

        playersDeck.DrawDeck(bbbGame.batch);


        bbbGame.batch.end();
    }
}
