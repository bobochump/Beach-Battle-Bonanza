package com.beachbb.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class Player extends Actor {
    private static final int FRAME_COLS = 3, FRAME_ROWS = 8;
    private int playerType;
    private int playerSide;
    private float totalHP;
    private float currentHP;
    private float attack;
    private float defense;
    private float animationTimer;
    private float countdownTimer; // second timer used to determine when to end an animation loop
    private boolean updateCountdown; // true when countdownTimer is active
    private int x;
    private int y;
    private int behavior; // determines which sprite to draw; 0=idle, 1=attack, 2=buff, 3=damage
    private Texture playerTexture;
    private TextureRegion playerSprite;
    private Animation<TextureRegion> animationIdle;
    private Animation<TextureRegion> animationAttack;
    private Animation<TextureRegion> animationBuff;
    private Animation<TextureRegion> animationHit;

    /**
     * Creates a new Player object that represents one of two controllable characters on screen.
     * Assigns the appropriate sprite, animation frames, and stats based on the type of player selected.
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
                playerTexture = new Texture(Gdx.files.internal("bbb-sprite-sheet-mage.png"));
                break;
            case 3: // rat
                totalHP = 160;
                attack = 180;
                defense = 100;
                playerTexture = new Texture(Gdx.files.internal("bbb-sprite-sheet-rat.png"));
                break;
            default: // shark
                totalHP = 180;
                attack = 200;
                defense = 100;
                playerTexture = new Texture(Gdx.files.internal("bbb-sprite-sheet-shark.png"));
                break;
        }

        currentHP = totalHP;

        // split the sprite sheet texture into a 2D array, then create new arrays for each animation sequence
        TextureRegion[][] splitTexture = TextureRegion.split(playerTexture,
                (playerTexture.getWidth()/FRAME_COLS), (playerTexture.getHeight()/FRAME_ROWS));
        TextureRegion[] idleFrames = new TextureRegion[2];
        TextureRegion[] attackFrames = new TextureRegion[3];
        TextureRegion[] buffFrames = new TextureRegion[1];
        TextureRegion[] hitFrames = new TextureRegion[2];

        // sets player sprite to initial tile position on grid and sets the corresponding texture
        if (playerSide == 1) {
            x = 1;
            y = 1;
            idleFrames[0] = splitTexture[4][0];
            idleFrames[1] = splitTexture[4][1];
            attackFrames[0] = splitTexture[5][0];
            attackFrames[1] = splitTexture[5][1];
            attackFrames[2] = splitTexture[5][2];
            buffFrames[0] = splitTexture[6][0];
            hitFrames[0] = splitTexture[7][1];
            hitFrames[1] = splitTexture[7][0];

        } else {
            x = 3;
            y = 4;
            idleFrames[0] = splitTexture[0][0];
            idleFrames[1] = splitTexture[0][1];
            attackFrames[0] = splitTexture[1][0];
            attackFrames[1] = splitTexture[1][1];
            attackFrames[2] = splitTexture[1][2];
            buffFrames[0] = splitTexture[2][0];
            hitFrames[0] = splitTexture[3][1];
            hitFrames[1] = splitTexture[3][0];
        }
        setPosition(x * 126 + 171, y * 110+ 76);

        animationIdle = new Animation<TextureRegion>(0.6f, idleFrames);
        animationAttack = new Animation<TextureRegion>(0.2f, attackFrames);
        animationBuff = new Animation<TextureRegion>(1f, buffFrames);
        animationHit = new Animation<TextureRegion>(0.2f, hitFrames);

        animationTimer = 0f;
        countdownTimer = 0f;
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

    public void setBehavior(int b) {
        behavior = b;
        if (behavior == 1 || behavior == 3) {
            updateCountdown = true;
        }
    }

    public void takeDamage(float multiplier) {
        currentHP -= 1 * multiplier;
    }

    public void resetHP() {
        currentHP = totalHP;
    }

    public int getTileX(){
        return x;
    }

    public int getTileY(){
        return y;
    }

    /**
     * Returns the matching TileID of the Tile that the player is standing on
     * @return
     */
    public int getPlayerTileID() {
        return 5 * y + x;
    }

    public float getPercentageHP() {
        return currentHP/totalHP;
    }
    public int getPlayerNum(){
        return playerType;
    }

    public void updatePlayerSprite() {
        animationTimer += Gdx.graphics.getDeltaTime();
        if (updateCountdown) {
            countdownTimer += Gdx.graphics.getDeltaTime();
        }
        switch (behavior) {
            case 1:
                playerSprite = animationAttack.getKeyFrame(animationTimer, true);
                if (countdownTimer >= 0.6f) {
                    updateCountdown = false;
                    countdownTimer = 0f;
                    behavior = 0;
                }
                break;
            case 2:
                playerSprite = animationBuff.getKeyFrame(animationTimer, true);
                break;
            case 3:
                playerSprite = animationHit.getKeyFrame(animationTimer, true);
                if (countdownTimer >= 1.0f) {
                    updateCountdown = false;
                    countdownTimer = 0f;
                    behavior = 0;
                }
                break;
            default:
                playerSprite = animationIdle.getKeyFrame(animationTimer, true);
                break;
        }
    }

    public void drawPlayer(SpriteBatch batch) {
        batch.draw(playerSprite, x * 126 + 171, y * 110 + 76);
    }
}
