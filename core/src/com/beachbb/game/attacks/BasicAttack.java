package com.beachbb.game.attacks;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.beachbb.game.AttackEntity;
import com.beachbb.game.Tile;

import java.util.ArrayList;

public class BasicAttack implements AttackEntity {
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
    private Texture bullet;

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
        x = tileX * 126;
        y = tileY * 110;
        prevTileX = -1;
        prevTileY = -1;
        bullet = new Texture(Gdx.files.internal("bbb-bullet.png"));
    }
    public int updateAttack(float delta, ArrayList<Tile> grid){
        float speed = 256;
        if(enemyAttack){
            y -= speed * delta;
        } else {
            y += speed * delta;
        }

        //update tileX and tileY
//        tileX = (int) (x - 171 - 63) % 126;
//        tileY = (int) (y - 76 - 55) % 110;
        tileX = (int) (x + 63) / 126;
        tileY = (int) (y + 55) / 110;
        if((tileY > 5) || (tileY < 0) || (tileX > 4) || (tileX < 0) || (y < -55)){
            grid.get(prevTileY * 5 + prevTileX).setDanger(false);
            return 1;
        }

        if(prevTileX != tileX || prevTileY != tileY){
//            System.out.println("PrevTileX = " + prevTileX + ", PrevTileY = " + prevTileY);
//            System.out.println("TileX = " + tileX + ", TileY = " + tileY + "\n");
            grid.get(tileY * 5 + tileX).setDanger(true);
            if(prevTileY >= 0 || prevTileX >= 0){
                grid.get(prevTileY * 5 + prevTileX).setDanger(false);
            }
        }

        prevTileX = tileX;
        prevTileY = tileY;
        return 0;
    }
    public void drawAttack(SpriteBatch batch){
        batch.draw(bullet, x + 171 + 63 - 16,  y + 76 + 55 - 16);
    }
}
