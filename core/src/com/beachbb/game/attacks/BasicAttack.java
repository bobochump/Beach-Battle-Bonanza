package com.beachbb.game.attacks;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.beachbb.game.AttackEntity;
import com.beachbb.game.Tile;

import java.util.ArrayList;

public class BasicAttack implements AttackEntity {
    public void updateAttack(float delta, ArrayList<Tile> grid){
        grid.get(1).setDanger(true);
    }
    public void drawAttack(SpriteBatch batch){

    }
}
