package model.states.cardgames.cabal;

import util.MyLists;
import util.MyPair;

import java.util.List;

public abstract class CabalAI {
    public abstract MyPair<Integer, Integer> makeEarlyClaim(CabalNPC self, CabalCardGame game, int turn);
    public abstract MyPair<Integer, Integer> makeLateClaim(CabalNPC self, CabalCardGame game, int turn);

    protected static MyPair<Integer, Integer> countStarsAndDaggers(CabalNPC self) {
        List<CabalCard> cards = MyLists.generateByIndex(self.numberOfCardsInHand(), i -> (CabalCard) self.getCard(i));
        List<CabalCard> stars = MyLists.filter(cards, c -> c instanceof StarCabalCard);
        List<CabalCard> daggers = MyLists.filter(cards, c -> c instanceof DaggerCabalCard);
        return new MyPair<>(stars.size(), daggers.size());
    }

    public abstract CabalCardGamePlayer findBestPick(CabalCardGame game, CabalNPC self, List<CabalCardGamePlayer> candidates, CabalSuspicions suspicions);

}
