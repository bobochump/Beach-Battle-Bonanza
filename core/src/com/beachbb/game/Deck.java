package com.beachbb.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.util.ArrayList;

public class Deck {
    private ArrayList<Card> cards;
    private Texture cardBackTexture;
    private TextureRegion cardBackSprite;
    public Deck(int characterNum) {
        cards = new ArrayList<Card>();
        switch (characterNum) {
            case 3:
                cards.add(new Card("card 11", 11, 11, 10));
                cards.add(new Card("card 12", 12, 12, 11));
                cards.add(new Card("card 13", 13, 13, 12));
                cards.add(new Card("card 14", 14, 14, 13));
                cards.add(new Card("card 15", 15, 15, 14));
            case 2:
                cards.add(new Card("card 6", 6, 6, 5));
                cards.add(new Card("card 7", 7, 7, 6));
                cards.add(new Card("card 8", 8, 8, 7));
                cards.add(new Card("card 9", 9, 9, 8));
                cards.add(new Card("card 10", 10, 10, 9));
            default: //default is character 1
                cards.add(new Card("card 1", 1, 1, 0));
                cards.add(new Card("card 2", 2, 2, 1));
                cards.add(new Card("card 3", 3, 3, 2));
                cards.add(new Card("card 4", 4, 4, 3));
                cards.add(new Card("card 5", 5, 5, 4));
        }
        cardBackTexture = new Texture(Gdx.files.internal("bbb-base-card-back.png"));
        cardBackSprite = new TextureRegion(cardBackTexture, 0, 0, 252, 139);
    }

    public void DrawDeck(SpriteBatch batch) {
        for(int i = 0; i < 5; i++) {
            Card cardToDraw = cards.get(i);
            if(cardToDraw != null) {
                batch.draw(cardBackSprite, 979 + ((139 + 11) * i), 20);
                cardToDraw.drawCard(batch, 979 + ((139 + 11) * i), 20);
            }
        }
    }


}
