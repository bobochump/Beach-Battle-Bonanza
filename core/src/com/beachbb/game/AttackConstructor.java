package com.beachbb.game;

import com.beachbb.game.attacks.*;

public class AttackConstructor {
    AttackEntity buildAttack(int effectID, float delta, int playerX, int playerY) {
        switch (effectID) {
            case 2:
                return new BombSquare(delta, playerX, playerY);
            case 3:
                return new TriAttack(delta, playerX, playerY);
            case 4:
                return new Shield(delta, playerX, playerY);
            case 5:
                return new Laser3Wide(delta, playerX, playerY);
            case 7:
                return new BombCross(delta, playerX, playerY);
            case 8:
                return new Spider(delta, playerX, playerY);
            case 9:
                return new Cloak(delta, playerX, playerY);
            case 10:
                return new Laser2Beams(delta, playerX, playerY);
            case 12:
                return new BombPlus(delta, playerX, playerY);
            case 13:
                return new Boomerang(delta, playerX, playerY);
            case 14:
                return new Heal(delta, playerX, playerY);
            case 15:
                return new Laser1Wide(delta, playerX, playerY);
            default:
                return new BasicAttack(delta, playerX, playerY);
        }
    }
}
