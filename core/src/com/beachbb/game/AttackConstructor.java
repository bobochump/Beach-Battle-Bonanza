package com.beachbb.game;

import com.beachbb.game.attacks.BasicAttack;

public class AttackConstructor {
    AttackEntity buildAttack(int effectID, float delta, int playerX, int playerY) {
//        switch (effectID) {
//            default:
//                return new BasicAttack();
//        }
        return new BasicAttack(delta, playerX, playerY);
    }
}
