package model.states.cardgames.cabal;

import model.states.cardgames.CardGameCard;
import model.states.cardgames.CardGameState;
import view.MyColors;

public class CabalCard extends CardGameCard {
    private final String name;

    public CabalCard(int i, MyColors suit, String name) {
        super(i, suit);
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void commentOnCard(CardGameState state, CabalCardGame game, CabalCardGamePlayer performer, CabalCardGamePlayer target) { }
}
