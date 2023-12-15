package com.beachbb.game.attacks;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.beachbb.game.AttackEntity;
import com.beachbb.game.Tile;

import java.util.ArrayList;

public class Spider implements AttackEntity {
    private int sourceX; //used to keep track of where the attack originated from
    private int sourceY;
    private float x; //center of where to draw attack
    private float y;
    private int tileX;  //used to keep track of which tile the attack is on
    private int tileY;
    private int prevTileX;  //used to keep track of where the attack was last update
    private int prevTileY;
    private boolean enemyAttack; //true if coming from enemy, false if coming from player
    private int flag;
    private float timer;
    private Texture bullet;  //texture

    public Spider(float delta, int playerX, int playerY){
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
        bullet = new Texture(Gdx.files.internal("bbb-bullet.png"));
    }
    public int updateAttack(float delta, ArrayList<Tile> grid){
        float speed = 256;
        boolean changeX = false;
        switch (flag) {
            case 1:
                changeX = true;
                break;
            case 2:
                speed *= -1;
                break;
            case 3:
                changeX = true;
                speed *= -1;
                break;
            case 5:
                changeX = true;
                break;
            case 6:
                speed = 0;
                timer -= delta;
                break;
        }
        if(enemyAttack) {
            speed *= -1;
        }
        if(changeX){
            x += speed * delta;
        } else {
            y += speed * delta;
        }

        //update tileX and tileY
        tileX = (int) (x + 63) / 126;
        tileY = (int) (y + 55) / 110;


        if(flag == 0 && (y < 0 || y > 110 * 5)) {
            flag = 1;
            if(enemyAttack) { y = 0; }
            else { y = 110 * 5; }
            grid.get(tileY * 5 + tileX).setBlocked(true);
        }
        if(flag == 1 && (x < 0 || x > 126 * 4)) {
            flag = 2;
            if(enemyAttack) { x = 0; }
            else { x = 126 * 4; }
            grid.get(tileY * 5 + tileX).setBlocked(true);
        }
        if(flag == 2 && ((enemyAttack && y > 110 * 2) || (!enemyAttack && y < 110 * 3))) {
            flag = 3;
            if(enemyAttack) { y = 110 * 2; }
            else { y = 110 * 3; }
            grid.get(tileY * 5 + tileX).setBlocked(true);
        }
        if(flag == 3 && (x < 0 || x > 126 * 4)) {
            flag = 4;
            if(!enemyAttack) { x = 0; }
            else { x = 126 * 4; }
            grid.get(tileY * 5 + tileX).setBlocked(true);
        }
        if(flag == 4 && (y < 0 || y > 110 * 5)) {
            flag = 5;
            if(enemyAttack) { y = 0; }
            else { y = 110 * 5; }
            grid.get(tileY * 5 + tileX).setBlocked(true);
        }
        if(flag == 5 && (x < 0 || x > 126 * 4)) {
            flag = 6;
            timer = 3.0F;
        }
        if(flag == 6 && timer < 0) {
            if(enemyAttack){
                grid.get(0 * 5 + 0).setBlocked(false);
                grid.get(2 * 5 + 0).setBlocked(false);
                grid.get(0 * 5 + sourceX).setBlocked(false);
                grid.get(2 * 5 + 4).setBlocked(false);
                grid.get(0 * 5 + 4).setBlocked(false);
                grid.get(0 * 5 + 0).setDanger(false);
            } else {
                grid.get(3 * 5 + 0).setBlocked(false);
                grid.get(5 * 5 + 0).setBlocked(false);
                grid.get(5 * 5 + sourceX).setBlocked(false);
                grid.get(3 * 5 + 4).setBlocked(false);
                grid.get(5 * 5 + 4).setBlocked(false);
                grid.get(5 * 5 + 4).setDanger(false);
            }
            return 1;
        }

        if(prevTileX != tileX || prevTileY != tileY){
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
