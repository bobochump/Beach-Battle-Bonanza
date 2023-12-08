package com.beachbb.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.ArrayList;

public interface AttackEntity {
    int updateAttack(float delta, ArrayList<Tile> grid);
        //return 1 when attack is finished
        //return 0 otherwise
    void drawAttack(SpriteBatch batch);
}
