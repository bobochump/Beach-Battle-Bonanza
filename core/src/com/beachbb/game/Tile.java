package com.beachbb.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class Tile extends Actor {
    private int tileID;
    private int tileX;
    private int tileY;
    private int tileCoordX;
    private int tileCoordY;
    private int tileSide; // 1 for Player 1's tile, 2 for Player 2's tile
    private boolean isDanger; // yellow tile, use later to represent tiles that cause damage
    private boolean isBlocked; // red tile, use later to represent non-traversable tiles
    private int dangerSources;
    private int blockedSources;
    private Texture tileTexture;
    private TextureRegion tileSprite;

    public Tile (int id, int x, int y, int side) {
        tileID = id;
        tileX = x;
        tileY = y;
        tileCoordX = tileX * 126 + 171;
        tileCoordY = tileY * 110 + 76;
        tileSide = side;
        isDanger = false;
        isBlocked = false;

        tileTexture = new Texture(Gdx.files.internal("bbb-base-tiles.png"));

        updateSprite();

        setPosition(tileCoordX, tileCoordY);
    }

    public void setDanger(boolean danger) {
        if(danger){
            dangerSources++;
        } else {
            dangerSources--;
        }
        if(dangerSources <= 0){
            isDanger = false;
        } else{
            isDanger = true;
        }
        updateSprite();
    }
    public void setBlocked(boolean blocked) {
        if(blocked){
            blockedSources++;
        } else {
            blockedSources--;
        }
        if(blockedSources <= 0){
            isBlocked = false;
        } else{
            isBlocked = true;
        }
        updateSprite();
    }
    private void updateSprite() {
        if(isBlocked){
            tileSprite = new TextureRegion(tileTexture, 352, 0, 117, 101);
        }
        else if(isDanger){
            tileSprite = new TextureRegion(tileTexture, 235, 0, 117, 101);
        }
        else if (tileSide == 1){
            tileSprite = new TextureRegion(tileTexture, 0, 0, 117, 101);
        }
        else {
            tileSprite = new TextureRegion(tileTexture, 118, 0, 117, 101);
        }
    }

    public boolean getDanger() {
        return isDanger;
    }

    public void drawTile(SpriteBatch batch) {
        batch.draw(tileSprite, tileCoordX, tileCoordY);
    }
}
