package model.quests;

import model.Model;
import model.Party;
import model.characters.GameCharacter;
import util.MyLists;

import java.io.Serializable;

public class Reward implements Serializable {
    private final int rep;
    private final int gold;
    private final int exp;
    private final int notoriety;

    public Reward(int partyRep, int gold, int exp, int notoriety) {
        this.rep = partyRep;
        this.gold = gold;
        this.exp = exp;
        this.notoriety = notoriety;
    }

    public Reward(int partyRep, int gold, int exp) {
        this(partyRep, gold, exp, 0);
    }

    public Reward(int partyRep, int gold) {
        this(partyRep, gold, 0);
    }

    public int getReputation() {
        return rep;
    }

    public int getGold() {
        return gold;
    }

    public int getExp() {
        return exp;
    }

    public void giveYourself(Model model) {
        Party party = model.getParty();
        party.addToGold(gold);
        party.addToReputation(rep);
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
        String reputation = ".";
        if (rep > 0) {
            reputation = ", and your reputation will increase.";
        } else if (rep < 0) {
            reputation = ", but your reputation will DECREASE.";
        }
        return "you will be paid " + gold + " gold" + experience + noto + reputation;
    }

    public int getNotoriety() {
        return notoriety;
    }
}
