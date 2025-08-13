package model.quests;

import model.Model;
import model.Party;
import model.characters.GameCharacter;
import util.MyLists;

import java.io.Serializable;

public class Reward implements Serializable {
    private final int gold;
    private final int exp;
    private final int notoriety;

    public Reward(int gold, int exp, int notoriety) {
        this.gold = gold;
        this.exp = exp;
        this.notoriety = notoriety;
    }

    public Reward(int gold, int exp) {
        this(gold, exp, 0);
    }

    public Reward(int gold) {
        this(gold, 0);
    }

    public int getGold() {
        return gold;
    }

    public int getExp() {
        return exp;
    }

    public void giveYourself(Model model) {
        Party party = model.getParty();
        party.earnGold(gold);
        party.addToNotoriety(notoriety);
        MyLists.forEach(party.getPartyMembers(), (GameCharacter gc) -> party.giveXP(model, gc, exp));
    }

    public String getDescription() {
        String experience = "";
        if (exp > 0) {
            experience = ", each party member will gain " + exp + " experience";
        }
        String noto = "";
        if (notoriety > 0) {
            noto = ", your notoriety increased";
        }
        return "you will be paid " + gold + " gold" + experience + noto + ".";
    }

    public int getNotoriety() {
        return notoriety;
    }
}
