package com.beachbb.game;

import com.beachbb.game.attacks.BasicAttack;
import com.beachbb.game.attacks.Laser1Wide;
import com.beachbb.game.attacks.Laser2Beams;
import com.beachbb.game.attacks.Laser3Wide;

public class AttackConstructor {
    AttackEntity buildAttack(int effectID, float delta, int playerX, int playerY) {
        switch (effectID) {
            case 5:
                return new Laser3Wide(delta, playerX, playerY);
            case 10:
                return new Laser2Beams(delta, playerX, playerY);
            case 15:
                return new Laser1Wide(delta, playerX, playerY);
            default:
                return new BasicAttack(delta, playerX, playerY);
        }
    }
}
