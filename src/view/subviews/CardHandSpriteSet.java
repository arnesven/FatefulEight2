package view.subviews;

import model.races.Race;
import model.states.cardgames.CardGamePlayer;
import view.MyColors;
import view.sprites.Sprite;
import view.sprites.Sprite16x16;

import java.util.HashMap;
import java.util.Map;

public class CardHandSpriteSet {

    private static final int NO_OF_HAND_SPRITES = 10;
    private Map<Integer, Sprite16x16[][]> spriteMap;

    public CardHandSpriteSet(MyColors cardBackColor) {
        spriteMap = new HashMap<>();
        for (Race race : Race.allRaces) {
            Sprite16x16[][] sprites = new Sprite16x16[4][NO_OF_HAND_SPRITES];
            for (int rotation = 0; rotation < 360; rotation+=90) {
                for (int i = 0; i < NO_OF_HAND_SPRITES; ++i) {
                    Sprite16x16 handSprite = new Sprite16x16("handsprite" + i + "rot"+rotation, "cardgame.png", 0x30 + i,
                            MyColors.BLACK, race.getColor(), cardBackColor, MyColors.PINK);
                    handSprite.setRotation(rotation);
                    sprites[rotation/90][i] = handSprite;
                }
            }
            spriteMap.put(race.id(), sprites);
        }
    }

    public Sprite16x16 get(Race race, int num, int rotation) {
        return spriteMap.get(race.id())[rotation/90][num];
    }

    public Sprite getCardHandSprite(CardGamePlayer npc, int num, int rotation) {
        int cardsInHand = npc.getCards().size();
        switch (cardsInHand) {
            case 0:
                break;
            case 1:
                if (num == 2) {
                    num++;
                }
                break;
            case 2:
                if (num == 2) {
                    num += 2;
                }
                break;
            case 3:
                if (num > 0) {
                    num += 4;
                }
                break;
            case 4:
                if (num > 0) {
                    num += 6;
                }
                break;
            default:
                if (num == 1) {
                    num += 6;
                } else if (num == 2) {
                    num += 7;
                }
                break;
        }
        return get(npc.getRace(), num, rotation);
    }
}
