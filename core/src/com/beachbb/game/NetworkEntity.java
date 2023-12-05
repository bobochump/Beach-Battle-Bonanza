package com.beachbb.game;

interface NetworkEntity {
    void sendMoveCommand(int direction);
    void sendCharCommand(int charNum);
    void sendAttackCommand(int effectID);
}
