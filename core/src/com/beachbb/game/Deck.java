package com.beachbb.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.util.ArrayList;
import java.util.Random;

public class Deck {
    private ArrayList<Card> cards;
    private ArrayList<Card> discard;
    private Texture cardBackTexture;
    private TextureRegion cardBackSprite;
    private TextureRegion cardSelectedSprite;
    private TextureRegion cardLowManaSprite;
    private TextureRegion cardSelectedLowManaSprite;
    private int selectedCard;
    private int previousMana;
    private int maxCards;
    //vars used to render the cards sliding down
    private float cardGravity;
    private float cardTerminalVelocity;
    private float[] cardSpeed;
    private float[] cardRenderedPos;
    public Deck(int characterNum) {
        selectedCard = 0;
        previousMana = 0;
        cards = new ArrayList<Card>();
        discard = new ArrayList<Card>();
        switch (characterNum) {
            case 3: //Bodega Worker
                discard.add(new Card("Basic Attack", 1, 11, 10));
                discard.add(new Card("Lingering Bomb", 4, 12, 13));
                discard.add(new Card("Basic Attack", 1, 13, 12));
                discard.add(new Card("Basic Attack", 1, 14, 14));
                discard.add(new Card("Lingering Laser", 15, 15, 11));
                discard.add(new Card("Basic Attack", 1, 1, 10));
                maxCards = 6;
                break;
            case 2: //Artificer
                discard.add(new Card("Basic Attack", 1, 6, 5));
                discard.add(new Card("Web Bomb", 4, 7, 6));
                discard.add(new Card("Basic Attack", 1, 8, 9));
                discard.add(new Card("Basic Attack", 1, 9, 8));
                discard.add(new Card("Tactical Laser", 15, 10, 7));
                discard.add(new Card("Basic Attack", 1, 11, 5));
                maxCards = 6;
                break;
            default: //default is character 1, the Shark
                discard.add(new Card("Basic Attack", 1, 1, 0));
                discard.add(new Card("Big Bomb", 4, 2, 3));
                discard.add(new Card("Basic Attack", 1, 3, 2));
                discard.add(new Card("Basic Attack", 1, 4, 4));
                discard.add(new Card("MEGA DEATH LASER", 15, 5, 1));
                discard.add(new Card("Basic Attack", 1, 6, 0));
                maxCards = 6;
                break;
        }
        shuffle();
        cardBackTexture = new Texture(Gdx.files.internal("bbb-base-card-back.png"));
        cardBackSprite = new TextureRegion(cardBackTexture, 0, 0, 252, 139);
        cardSelectedSprite = new TextureRegion(cardBackTexture, 504, 0, 252, 139);
        cardLowManaSprite = new TextureRegion(cardBackTexture, 252, 0, 252, 139);
        cardSelectedLowManaSprite = new TextureRegion(cardBackTexture, 756, 0, 252, 139);
        cardGravity = 100.0F;  //might need to tweak these values
        cardTerminalVelocity = 500F;
        cardSpeed = new float[] {0, 0, 0, 0, 0};
        cardRenderedPos = new float[] {20 + ((139 + 11) * 0), 20 + ((139 + 11) * 1), 20 + ((139 + 11) * 2), 20 + ((139 + 11) * 3), 20 + ((139 + 11) * 4)};
    }
    void changeSelection(int newCard) {
        //-1 is go up a card, -2 is go down a card, 0-4 is go to that card
        if(newCard == -1) {
            selectedCard += 1;
            if(selectedCard == 5 || selectedCard >= cards.size()) {
                selectedCard = 0;
            }
        }
        else if(newCard == -2) {
            selectedCard -= 1;
            if(selectedCard == -1) {
                selectedCard = 4;
            }
            if(selectedCard >= cards.size()) {
                selectedCard = cards.size() - 1;
            }
        } else {
            if(newCard < cards.size()){
                selectedCard = newCard;
            }
        }

    }

    int useCard(int currentMana) {
        //returns -1 if card was not used, otherwise return the effect id
        Card cardToUse;
        if(cards.size() > selectedCard) {
            cardToUse = cards.get(selectedCard);
        } else {
            return -1;
        }
        if(cardToUse.getCardCost() > currentMana) {
            return -1;
        } else {
            discard.add(cardToUse);
            cards.remove(selectedCard);
            //check if deck is empty. If so, need to shuffle the discard pile back into it
            if(cards.isEmpty()) {
                shuffle();
                cardRenderedPos = new float[] {20 + ((139 + 11) * 0), 20 + ((139 + 11) * 1), 20 + ((139 + 11) * 2), 20 + ((139 + 11) * 3), 20 + ((139 + 11) * 4)};
            } else {
                for(int i = selectedCard; i < 5; i++){
                    cardRenderedPos[i] += 139 + 11;
                }
            }
            previousMana = cardToUse.getCardCost();
            if(selectedCard >= cards.size()) {
                selectedCard = cards.size() - 1;
            }
            return cardToUse.getEffectID();
        }
    }

    int getUsedCardMana() {
        return previousMana;
    }
    float getCardsRemainingPercentage() {
        return (float) cards.size() / maxCards;
    }

    void shuffle() {
        int numCards = discard.size();
        Random rand = new Random();
        for(int i = numCards; i > 0; i--) {
            int randomCard = rand.nextInt(i);
            Card cardRemoved = discard.remove(randomCard);
            cards.add(cardRemoved);
            numCards--;
        }
    }

    public void DrawDeck(SpriteBatch batch, float delta, float currentMana) {
        for(int i = 0; i < 5; i++) {
            if(cardRenderedPos[i] > 20 + ((139 + 11) * i)){
                cardSpeed[i] += cardGravity * delta;
                if (cardSpeed[i] > cardTerminalVelocity) {
                    cardSpeed[i] = cardTerminalVelocity;
                }
                cardRenderedPos[i] -= cardSpeed[i];
            }
            if(cardRenderedPos[i] < 20 + ((139 + 11) * i)) {
                cardRenderedPos[i] = 20 + ((139 + 11) * i);
                cardSpeed[i] = 0;
            }
            if(cards.size() > i) {
                Card cardToDraw = cards.get(i);
                if (selectedCard == i) {
                    if(cards.get(i).getCardCost() > currentMana) {
                        batch.draw(cardSelectedLowManaSprite, 979, cardRenderedPos[i]);
                    } else {
                        batch.draw(cardSelectedSprite, 979, cardRenderedPos[i]);
                    }
                } else {
                    if(cards.get(i).getCardCost() > currentMana) {
                        batch.draw(cardLowManaSprite, 979, cardRenderedPos[i]);
                    } else {
                        batch.draw(cardBackSprite, 979, cardRenderedPos[i]);
                    }
                }
                cardToDraw.drawCard(batch, 979, (int) cardRenderedPos[i]);
            }
        }
    }


}
