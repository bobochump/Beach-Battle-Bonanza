package com.beachbb.game.attacks;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.beachbb.game.AttackEntity;
import com.beachbb.game.Tile;

import java.util.ArrayList;

public class Laser3Wide implements AttackEntity {
    final private float initialDelta;
    private int sourceX;
    private int sourceY;
    private float x; //center of where to draw attack
    private float y;
    private int tileX;
    private int tileY;
    private int prevTileX;
    private int prevTileY;
    private boolean enemyAttack; //true if coming from enemy, false if coming from player
    private Texture laser;
    private int flag;
    private float currentDelta;

    public Laser3Wide(float delta, int playerX, int playerY){
        initialDelta = delta;
        sourceX = playerX;
        sourceY = playerY;
        enemyAttack = sourceY > 2;
        tileX = sourceX;
        if(enemyAttack){
            tileY = sourceY - 1;
        } else {
            tileY = sourceY + 1;
        }
        x = tileX * 126;
        y = tileY * 110;
        prevTileX = -1;
        prevTileY = -1;
        flag = 0;
        currentDelta = 0;
        laser = new Texture(Gdx.files.internal("bbb-bullet.png"));
    }
    public int updateAttack(float delta, ArrayList<Tile> grid){
        //float currentDelta = delta - initialDelta;
        currentDelta += delta;

        if(flag == 0 && currentDelta > 0.25) {
            flag = 1;
            if(enemyAttack){
                for (int i = tileY; i >= 0; i--){
                    grid.get(i * 5 + sourceX).setDanger(true);
                }
            } else {
                for (int i = tileY; i < 6; i++){
                    grid.get(i * 5 + sourceX).setDanger(true);
                }
            }
        }
        if(flag == 1 && currentDelta > 0.5) {
            flag = 2;
            if(enemyAttack){
                for (int i = tileY; i >= 0; i--){
                    if(sourceX != 0){
                        grid.get(i * 5 + sourceX - 1).setDanger(true);
                    }
                    if(sourceX != 4) {
                        grid.get(i * 5 + sourceX + 1).setDanger(true);
                    }
                }
            } else {
                for (int i = tileY; i < 6; i++){
                    if(sourceX != 0){
                        grid.get(i * 5 + sourceX - 1).setDanger(true);
                    }
                    if(sourceX != 4) {
                        grid.get(i * 5 + sourceX + 1).setDanger(true);
                    }
                }
            }
        }
        if(flag == 2 && currentDelta > 3.0) {
            flag = 3;
            if(enemyAttack){
                for (int i = tileY; i >= 0; i--){
                    if(sourceX != 0){
                        grid.get(i * 5 + sourceX - 1).setDanger(false);
                    }
                    grid.get(i * 5 + sourceX).setDanger(false);
                    if(sourceX != 4) {
                        grid.get(i * 5 + sourceX + 1).setDanger(false);
                    }
                }
            } else {
                for (int i = tileY; i < 6; i++){
                    if(sourceX != 0){
                        grid.get(i * 5 + sourceX - 1).setDanger(false);
                    }
                    grid.get(i * 5 + sourceX).setDanger(false);
                    if(sourceX != 4) {
                        grid.get(i * 5 + sourceX + 1).setDanger(false);
                    }
                }
            }
            return 1;
        }
        return 0;
    }
    public void drawAttack(SpriteBatch batch){
        batch.draw(laser, x + 171 + 63 - 16,  y + 76 + 55 - 16);
    }
}
