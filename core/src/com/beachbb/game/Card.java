package com.beachbb.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Card {
    private String name;
    private int cost;
    private int effect_id;
    private Texture cardTexture;
    private TextureRegion cardSprite;

    public Card(String card_name, int card_cost, int card_effect_id, int textureID) {
        name = card_name;
        cost = card_cost;
        effect_id = card_effect_id;
        cardTexture = new Texture(Gdx.files.internal("bbb-card-front.png"));
        cardSprite = new TextureRegion(cardTexture, 0, 139 * textureID, 252, 139);
    }

    public String getCardName() {
        return name;
    }

    public int getCardCost() {
        return cost;
    }

    public int getEffectID() {
        return effect_id;
    }

    public void drawCard(SpriteBatch batch, int x, int y) {
        batch.draw(cardSprite, x, y);
    }

}
