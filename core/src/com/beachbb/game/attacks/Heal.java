package com.beachbb.game.attacks;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.beachbb.game.AttackEntity;
import com.beachbb.game.Tile;

import java.util.ArrayList;

public class Heal implements AttackEntity {
    private boolean firstRun; //used for moving between different stages of the attack
    private boolean enemyAttack;

    public Heal(float delta, int playerX, int playerY){
        firstRun = true;
        enemyAttack = playerY > 2;
    }
    public int updateAttack(float delta, ArrayList<Tile> grid){
        if(enemyAttack) {
            return 1;
        }
        if(firstRun) {
            firstRun = false;
            return 2;
        }
        return 1;
    }
    public void drawAttack(SpriteBatch batch){}
}
