package com.beachbb.game;

interface NetworkEntity {
    void sendMoveCommand(int direction);
    void sendCharCommand(int charNum);
    void sendAttackCommand(int effectID);
    void sendOpponentHP(float p1hpPercent);
    void sendOpponentBehavior(int behavior);
    void sendEndConnection();
    void sendRematch();
    void start();
    void stopNetwork();
    boolean isConnected();
}
