package com.beachbb.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class Player extends Actor {
    private int playerType;
    private int playerSide;
    private float totalHP;
    private float currentHP;
    private float attack;
    private float defense;
    private int x;
    private int y;
    private int behavior; // determines which sprite to draw, will utilize this later
    private Texture playerTexture;
    private TextureRegion playerSprite;

    /**
     * Creates a new Player object that represents one of two controllable characters on screen.
     * Assigns the appropriate sprite and stats based on the type of player selected.
     * Sets the default location on the screen depending on which side the character is on.
     * X and Y correspond to the grid position of the sprite.
     * @param type: 1 = shark, 2 = mage, 3 = rat
     * @param side: 1 = Player 1 (blue, lower grid), 2 = Player 2 (red, upper grid)
     */
    public Player (int type, int side) {
        playerType = type;
        playerSide = side;
        behavior = 0;

        // sets the stats of the character to arbitrary values; likely will be changed later
        switch (playerType) {
            case 2: // mage
                totalHP = 120;
                attack = 220;
                defense = 100;
                playerTexture = new Texture(Gdx.files.internal("bbb-base-sprite-mage.png"));
                break;
            case 3: // rat
                totalHP = 160;
                attack = 180;
                defense = 100;
                playerTexture = new Texture(Gdx.files.internal("bbb-base-sprite-rat.png"));
                break;
            default: // shark
                totalHP = 180;
                attack = 200;
                defense = 100;
                playerTexture = new Texture(Gdx.files.internal("bbb-base-sprite-shark.png"));
                break;
        }

        currentHP = totalHP;
        playerSprite = new TextureRegion(playerTexture, 0, 0, 117, 146);

        // sets player sprite to initial tile position on grid
        if (playerSide == 1) {
            x = 1;
            y = 1;
        } else {
            x = 3;
            y = 4;
        }
        setPosition(x * 126 + 171, y * 110+ 76);
    }

    /**
     * Changes the location of the player on the grid depending on the direction passed in,
     * as long as the direction points to a valid grid location. Otherwise, does nothing.
     * @param direction: 1 = north, 2 = east, 3 = south, 4 = west
     */
    public void movePlayer(int direction) {
        switch (direction) {
            case 1: // north
                if (playerSide == 1) {
                    if (y < 2) { y++; }
                } else {
                    if (y < 5) { y++; }
                }
                break;
            case 2: // east
                if (x < 4) { x++; }
                break;
            case 3: // south
                if (playerSide == 1) {
                    if (y > 0) { y--; }
                } else {
                    if (y > 3) { y--; }
                }
                break;
            case 4: // west
                if (x > 0) { x--; }
                break;
            default: // no movement
                break;
        }
    }

    public void drawPlayer(SpriteBatch batch) {
        batch.draw(playerSprite, x * 126 + 171, y * 110 + 76);
    }
}
