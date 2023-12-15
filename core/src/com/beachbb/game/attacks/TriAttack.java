package com.beachbb.game.attacks;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.beachbb.game.AttackConstructor;
import com.beachbb.game.AttackEntity;
import com.beachbb.game.Tile;

import java.util.ArrayList;

public class TriAttack implements AttackEntity {
    private AttackEntity leftBullet;
    private AttackEntity centerBullet;
    private AttackEntity rightBullet;
    private boolean leftActive;
    private boolean centerActive;
    private boolean rightActive;


    public TriAttack(float delta, int playerX, int playerY){
        leftActive = false;
        if(playerX > 0) {
            leftBullet = new BasicAttack(delta, playerX - 1, playerY);
            leftActive = true;
        }
        centerBullet = new BasicAttack(delta, playerX, playerY);
        centerActive = true;
        rightActive = false;
        if(playerX < 4) {
            rightBullet = new BasicAttack(delta, playerX + 1, playerY);
            rightActive = true;
        }
    }
    public int updateAttack(float delta, ArrayList<Tile> grid){
        if(leftActive) {
            if(leftBullet.updateAttack(delta, grid) == 1) {
                leftActive = false;
            }
        }
        if(centerActive) {
            if(centerBullet.updateAttack(delta, grid) == 1) {
                centerActive = false;
            }
        }
        if(rightActive) {
            if(rightBullet.updateAttack(delta, grid) == 1) {
                rightActive = false;
            }
        }
        if(!leftActive && !centerActive && !rightActive) {
            return 1;
        }
        return 0;
    }
    public void drawAttack(SpriteBatch batch){
        if(leftActive){leftBullet.drawAttack(batch);}
        if(centerActive){centerBullet.drawAttack(batch);}
        if(rightActive){rightBullet.drawAttack(batch);}
    }
}
