package com.beachbb.game.attacks;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.beachbb.game.AttackEntity;
import com.beachbb.game.Tile;

import java.util.ArrayList;

public class Laser1Wide implements AttackEntity {
    private int sourceX;  //used for where on the grid the attack comes from
    private int sourceY;
    private float x; //screen co-ords for where to draw the attack
    private float y;
    private boolean enemyAttack; //true if coming from enemy, false if coming from player
    private Texture laser; //texture
                            //TODO: Get proper texture for the laser
    private int flag; //used for moving between different stages of the attack
    private float timeElapsed;  //used to keep track of how long the attack has gone for

    public Laser1Wide(float delta, int playerX, int playerY){
        sourceX = playerX;
        sourceY = playerY;
        enemyAttack = sourceY > 2;
        if(enemyAttack){
            sourceY -= 1;
        } else {
            sourceY += 1;
        }
        x = sourceX * 126;
        y = sourceY * 110;
        flag = 0;
        timeElapsed = 0;
        laser = new Texture(Gdx.files.internal("bbb-bullet.png"));
    }
    public int updateAttack(float delta, ArrayList<Tile> grid){
        timeElapsed += delta;

        //Give a bit of startup lag, and start with only center column
        if(flag == 0 && timeElapsed > 0.1) {
            flag = 1;
            if(enemyAttack){
                for (int i = sourceY; i >= 0; i--){
                    grid.get(i * 5 + sourceX).setDanger(true);
                }
            } else {
                for (int i = sourceY; i < 6; i++){
                    grid.get(i * 5 + sourceX).setDanger(true);
                }
            }
        }

        //After a bit longer, attack ends
        if(flag == 1 && timeElapsed > 5.0) {
            flag = 2;
            if(enemyAttack){
                for (int i = sourceY; i >= 0; i--){
                    grid.get(i * 5 + sourceX).setDanger(false);
                }
            } else {
                for (int i = sourceY; i < 6; i++){
                    grid.get(i * 5 + sourceX).setDanger(false);
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
