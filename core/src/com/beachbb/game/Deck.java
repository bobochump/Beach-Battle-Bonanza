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
    private int selectedCard;
    public Deck(int characterNum) {
        selectedCard = 0;
        cards = new ArrayList<Card>();
        discard = new ArrayList<Card>();
        switch (characterNum) {
            case 3:
                discard.add(new Card("card 11", 11, 11, 10));
                discard.add(new Card("card 12", 12, 12, 11));
                discard.add(new Card("card 13", 13, 13, 12));
                discard.add(new Card("card 14", 14, 14, 13));
                discard.add(new Card("card 15", 15, 15, 14));
                discard.add(new Card("card 1", 1, 1, 0));
                break;
            case 2:
                discard.add(new Card("card 6", 6, 6, 5));
                discard.add(new Card("card 7", 7, 7, 6));
                discard.add(new Card("card 8", 8, 8, 7));
                discard.add(new Card("card 9", 9, 9, 8));
                discard.add(new Card("card 10", 10, 10, 9));
                discard.add(new Card("card 11", 11, 11, 10));
                break;
            default: //default is character 1
                discard.add(new Card("card 1", 1, 1, 0));
                discard.add(new Card("card 2", 2, 2, 1));
                discard.add(new Card("card 3", 3, 3, 2));
                discard.add(new Card("card 4", 4, 4, 3));
                discard.add(new Card("card 5", 5, 5, 4));
                discard.add(new Card("card 6", 6, 6, 5));
                break;
        }
        shuffle();
        cardBackTexture = new Texture(Gdx.files.internal("bbb-base-card-back.png"));
        cardBackSprite = new TextureRegion(cardBackTexture, 0, 0, 252, 139);
        cardSelectedSprite = new TextureRegion(cardBackTexture, 504, 0, 252, 139);
    }
    void changeSelection(int newCard) {
        //-1 is go up a card, -2 is go down a card, 0-4 is go to that card
        if(newCard == -1) {
            selectedCard += 1;
            if(selectedCard == 5) {
                selectedCard = 0;
            }
        }
        else if(newCard == -2) {
            selectedCard -= 1;
            if(selectedCard == -1) {
                selectedCard = 4;
            }
        } else {
            selectedCard = newCard;
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
            }
            return cardToUse.getEffectID();
        }
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

    public void DrawDeck(SpriteBatch batch) {
        for(int i = 0; i < 5; i++) {
            if(cards.size() > i) {
                Card cardToDraw = cards.get(i);
                if (selectedCard == i) {
                    batch.draw(cardSelectedSprite, 979, 20 + ((139 + 11) * i));
                } else {
                    batch.draw(cardBackSprite, 979, 20 + ((139 + 11) * i));
                }
                cardToDraw.drawCard(batch, 979, 20 + ((139 + 11) * i));
            }
        }
    }


}
