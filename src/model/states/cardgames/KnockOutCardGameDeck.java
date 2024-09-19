package model.states.cardgames;

import view.MyColors;

import java.util.Collections;

public class KnockOutCardGameDeck extends CardGameDeck {

    public KnockOutCardGameDeck() {
        this.clear();
        for (int i = 0; i < 9; ++i) {
            this.add(new CardGameCard(1, MyColors.BLACK));
        }

        for (int i = 0; i < 3; ++i) {
            this.add(new CardGameCard(2, MyColors.BROWN));
            this.add(new CardGameCard(3, MyColors.PURPLE));
            this.add(new CardGameCard(4, MyColors.GREEN));
            this.add(new CardGameCard(5, MyColors.BLUE));
        }

        this.add(new CardGameCard(6, MyColors.ORANGE));
        this.add(new CardGameCard(7, MyColors.RED));
        this.add(new CardGameCard(8, MyColors.DARK_GREEN));
        Collections.shuffle(this);
    }
}
