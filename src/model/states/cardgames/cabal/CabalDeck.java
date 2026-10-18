package model.states.cardgames.cabal;

import model.states.cardgames.CardGameDeck;

import java.util.Collections;

public class CabalDeck extends CardGameDeck {
    public CabalDeck(int numberOfPlayers) {
        this.clear();
        for (int i = 0; i < numberOfPlayers + 1; i++) {
            add(new StarCabalCard());
        }
        add(new DaggerCabalCard());
        add(new DaggerCabalCard());

        while (size() < numberOfPlayers * 5) {
            add(new JunkCabalCard());
        }
        Collections.shuffle(this);
    }
}
