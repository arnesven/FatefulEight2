package model.states.cardgames;

import model.races.Race;
import view.MyColors;

public abstract class RunnyCardGamePlayer extends CardGamePlayer {
    public RunnyCardGamePlayer(String firstName, boolean gender, Race race, int obols, boolean isNPC) {
        super(firstName, gender, race, obols, isNPC);
    }

    public boolean hasWinningHand() {
        int lastValue = -1;
        MyColors lastSuit = null;
        int setCounter = 0;
        int runCounter = 0;
        for (int i = 0; i < numberOfCardsInHand(); ++i) {
            CardGameCard card = getCard(i);
            if (lastValue == card.getValue()) {
                setCounter++;
            } else if (lastValue == card.getValue()-1 && lastSuit == card.getSuit()) {
                runCounter++;
            } else {
                if (lastValue != -1 && setCounter < 3 && runCounter < 3) {
                    return false;
                } else {
                    setCounter = 0;
                    runCounter = 0;
                }
            }
            lastValue = card.getValue();
            lastSuit = card.getSuit();
        }
        return setCounter >= 3 || runCounter >= 3;
    }

}
