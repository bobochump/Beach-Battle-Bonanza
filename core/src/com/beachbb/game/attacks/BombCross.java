package com.beachbb.game.attacks;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.beachbb.game.AttackEntity;
import com.beachbb.game.Tile;

import java.util.ArrayList;

public class BombCross implements AttackEntity {
    private int sourceX; //used to keep track of where the attack originated from
    private int sourceY;
    private float x; //center of where to draw attack
    private float y;
    private int tileX;  //used to keep track of which tile the attack is on
    private int tileY;
    private float destY;
    private int destTileY;
    private boolean stillTraveling;
    private float timeSinceDetonation;
    private int flag;
    private boolean enemyAttack; //true if coming from enemy, false if coming from player
    private Texture bullet;  //texture

    public BombCross(float delta, int playerX, int playerY){
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
        if(enemyAttack){
            destY = y - 110*2;
            destTileY = tileY - 2;
        } else {
            destY = y + 110*2;
            destTileY = tileY + 2;
        }
        stillTraveling = true;
        timeSinceDetonation = 0;
        flag = 0;

        bullet = new Texture(Gdx.files.internal("bbb-bullet.png"));
    }
    public int updateAttack(float delta, ArrayList<Tile> grid){
        float speed = 128;
        if (stillTraveling){
            if(enemyAttack){
                y -= speed * delta;
                if (y < destY){
                    y = destY;
                    stillTraveling = false;
                }
            } else {
                y += speed * delta;
                if (y > destY){
                    y = destY;
                    stillTraveling = false;
                }
            }
        } else {
            timeSinceDetonation += delta;
            //start in middle
            if(flag == 0 && timeSinceDetonation > 0.1) {
                flag = 1;
                grid.get(destTileY * 5 + tileX).setBlocked(true);
            }
            //do the plus next
            if(flag == 1 && timeSinceDetonation > 0.2) {
                flag = 2;
                if(tileX < 4 && destTileY < 5) {
                    grid.get((destTileY + 1) * 5 + tileX + 1).setDanger(true);
                }
                if(tileX < 4 && destTileY > 0) {
                    grid.get((destTileY - 1) * 5 + tileX + 1).setDanger(true);
                }
                if(tileX > 0 && destTileY < 5) {
                    grid.get((destTileY + 1) * 5 + tileX - 1).setDanger(true);
                }
                if(tileX > 0 && destTileY > 0) {
                    grid.get((destTileY - 1) * 5 + tileX - 1).setDanger(true);
                }
            }
            if(flag == 2 && timeSinceDetonation > 1.8) {
                flag = 3;
                if(tileX < 4 && destTileY < 5) {
                    grid.get((destTileY + 1) * 5 + tileX + 1).setDanger(false);
                }
                if(tileX < 4 && destTileY > 0) {
                    grid.get((destTileY - 1) * 5 + tileX + 1).setDanger(false);
                }
                if(tileX > 0 && destTileY < 5) {
                    grid.get((destTileY + 1) * 5 + tileX - 1).setDanger(false);
                }
                if(tileX > 0 && destTileY > 0) {
                    grid.get((destTileY - 1) * 5 + tileX - 1).setDanger(false);
                }

            }
            if(flag == 3 && timeSinceDetonation > 3.5) {
                grid.get(destTileY * 5 + tileX).setBlocked(false);
                return 1;
            }
        }
        return 0;
    }
    public void drawAttack(SpriteBatch batch){
        batch.draw(bullet, x + 171 + 63 - 16,  y + 76 + 55 - 16);
    }
}
