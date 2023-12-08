package com.beachbb.game.attacks;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.beachbb.game.AttackEntity;
import com.beachbb.game.Tile;

import java.util.ArrayList;

public class BasicAttack implements AttackEntity {
    final private float initialDelta;
    private int sourceX;
    private int sourceY;
    private int x; //center of where to draw attack
    private int y;
    private int tileX;
    private int tileY;
    private int prevTileX;
    private int prevTileY;
    private boolean enemyAttack; //true if coming from enemy, false if coming from player
    public BasicAttack(float delta, int playerX, int playerY){
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
        x = tileX * 126 + 171 + 63;
        y = tileY * 110 + 76 + 55;
    }
    public int updateAttack(float delta, ArrayList<Tile> grid){
        int speed = 32;
        if(enemyAttack){
            y -= speed * delta;
        } else {
            y += speed * delta;
        }

        //update tileX and tileY
        tileX = (x - 171 - 63) % 126;
        tileY = (y - 76 - 55) % 110;
        if(tileY > 5 || tileY < 0 || tileX > 4 || tileX < 0){
            return 1;
        }

        if(prevTileX != tileX || prevTileY != tileY){
            grid.get(tileY * 5 + tileX).setDanger(true);
            grid.get(prevTileY * 5 + prevTileX).setDanger(false);
        }

        prevTileX = tileX;
        prevTileY = tileY;
        return 0;
    }
    public void drawAttack(SpriteBatch batch){

    }
}
