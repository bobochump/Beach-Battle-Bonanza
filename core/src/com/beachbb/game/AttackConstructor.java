package com.beachbb.game;

import com.beachbb.game.attacks.*;

public class AttackConstructor {
    AttackEntity buildAttack(int effectID, float delta, int playerX, int playerY) {
        switch (effectID) {
            case 2:
                return new BombSquare(delta, playerX, playerY);
            case 5:
                return new Laser3Wide(delta, playerX, playerY);
            case 10:
                return new Laser2Beams(delta, playerX, playerY);
            case 12:
                return new BombPlus(delta, playerX, playerY);
            case 15:
                return new Laser1Wide(delta, playerX, playerY);
            default:
                return new BasicAttack(delta, playerX, playerY);
        }
    }
}
