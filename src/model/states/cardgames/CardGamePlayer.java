package model.states.cardgames;

import model.races.Race;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CardGamePlayer {
    private String name;
    private boolean gender;
    private Race race;
    private int obols;
    private List<CardGameCard> cards = new ArrayList<>();

    public CardGamePlayer(String firstName, boolean gender, Race race, int obols) {
        this.name = firstName;
        this.gender = gender;
        this.race = race;
        this.obols = obols;
    }

    public Race getRace() {
        return race;
    }

    public String getName() {
        return name;
    }

    public void giveCard(CardGameCard card) {
        cards.add(card);
    }

    public List<CardGameCard> getCards() {
        return cards;
    }

    public int getObols() {
        return obols;
    }
}
