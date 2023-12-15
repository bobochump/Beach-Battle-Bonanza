package com.beachbb.game.attacks;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.beachbb.game.AttackEntity;
import com.beachbb.game.Tile;

import java.util.ArrayList;

public class Boomerang implements AttackEntity {
    private int sourceX; //used to keep track of where the attack originated from
    private int sourceY;
    private float x; //center of where to draw attack
    private float y;
    private int tileX;  //used to keep track of which tile the attack is on
    private int tileY;
    private int prevTileX;  //used to keep track of where the attack was last update
    private int prevTileY;
    private boolean enemyAttack; //true if coming from enemy, false if coming from player
    private boolean comingBack;
    private Texture bullet;  //texture

    public Boomerang(float delta, int playerX, int playerY){
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
        comingBack = false;
        bullet = new Texture(Gdx.files.internal("bbb-bullet.png"));
    }
    public int updateAttack(float delta, ArrayList<Tile> grid){
        float speed = 256;
        if(comingBack) {speed = -256;}
        if(enemyAttack){
            y -= speed * delta;
        } else {
            y += speed * delta;
        }

        //update tileX and tileY
        tileX = (int) (x + 63) / 126;
        tileY = (int) (y + 55) / 110;
        if(y < 0 || y > 110 * 5){
            if(comingBack) {
                grid.get(prevTileY * 5 + prevTileX).setDanger(false);
                return 1;
            }
            else {
                comingBack = true;
            }
        }
        else if(prevTileX != tileX || prevTileY != tileY){
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