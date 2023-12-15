package com.beachbb.game.attacks;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.beachbb.game.AttackEntity;
import com.beachbb.game.Tile;

import java.util.ArrayList;

public class Cloak implements AttackEntity {
    private int flag; //used for moving between different stages of the attack
    private float timer;

    public Cloak(float delta, int playerX, int playerY){
        flag = 0;
        timer = 0;
    }
    public int updateAttack(float delta, ArrayList<Tile> grid){
        timer += delta;
        if(flag == 0) {
            flag = 1;
            return 5;
        }
        if(timer > 2.5 && flag == 1){
            flag = 2;
            return 6;
        }
        if(flag == 2) {
            return 1;
        }
        return 0;
    }
    public void drawAttack(SpriteBatch batch){}
}
