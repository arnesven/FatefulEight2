package model.states.cardgames.knockout;

import model.states.cardgames.CardGameCard;
import model.states.cardgames.CardGameDeck;
import view.MyColors;

import java.util.Collections;

public class KnockOutCardGameDeck extends CardGameDeck {

    public enum KnockOutValues {
        CUDGEL,
        SPYGLASS,
        RAPIER,
        SHIELD,
        CROSSBOW,
        MAGIC_MIRROR,
        FALSE_SCEPTER,
        SCEPTER
    }

    public static String nameForValue(int value) {
        if (1 <= value && value <= 8) {
            return KnockOutValues.values()[value - 1].toString().replaceAll("_", " ");
        }
        throw new IllegalArgumentException("Bad value for knock out card: " + value);
    }

    public KnockOutCardGameDeck() {
        this.clear();
        for (int i = 0; i < 8; ++i) {
            this.add(new KnockOutCard(1, MyColors.BLACK));
        }

        for (int i = 0; i < 3; ++i) {
            this.add(new KnockOutCard(2, MyColors.BROWN));
            this.add(new KnockOutCard(3, MyColors.PURPLE));
            this.add(new KnockOutCard(4, MyColors.GREEN));
            this.add(new KnockOutCard(5, MyColors.BLUE));
        }

        this.add(new KnockOutCard(6, MyColors.ORANGE));
        this.add(new KnockOutCard(6, MyColors.ORANGE));
        this.add(new KnockOutCard(7, MyColors.RED));
        this.add(new KnockOutCard(8, MyColors.DARK_GREEN));
        Collections.shuffle(this);
    }

    private static class KnockOutCard extends CardGameCard {
        public KnockOutCard(int value, MyColors color) {
            super(value, color);
        }

        @Override
        public String getText() {
            if (isFlipped()) {
                return super.getText();
            }
            return nameForValue(getValue()) + " (" + getValue() + ")";
        }
    }
}
