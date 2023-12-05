package com.beachbb.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.ArrayList;

public interface AttackEntity {
    void updateAttack(float delta, ArrayList<Tile> grid);
    void drawAttack(SpriteBatch batch);
}
