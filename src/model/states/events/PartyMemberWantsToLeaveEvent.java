package model.states.events;

import model.Model;
import model.characters.GameCharacter;
import model.enemies.Enemy;
import model.enemies.FormerPartyMemberEnemy;
import model.states.DailyEventState;
import util.MyRandom;

import java.util.ArrayList;
import java.util.List;

public class PartyMemberWantsToLeaveEvent extends DailyEventState {
    public PartyMemberWantsToLeaveEvent(Model model) {
        super(model);
    }

    @Override
    protected void doEvent(Model model) {
        List<GameCharacter> candidates = new ArrayList<>();
        for (GameCharacter gc : model.getParty().getPartyMembers()) {
            if (gc != model.getParty().getLeader() && gc.getAttitude(model.getParty().getLeader()) < 0) {
                candidates.add(gc);
            }
        }
        if (candidates.isEmpty()) {
            new NoEventState(model).doEvent(model);
            return;
        }
        for (GameCharacter gc : candidates) {
            if (MyRandom.randInt(30) > 30 + gc.getAttitude(model.getParty().getLeader())) {
                if (wantsToLeave(model, gc)) {
                    return;
                }
            } else {
                model.getParty().partyMemberSay(model, gc,
                        List.of("Maybe joining up with you was a bad idea...",
                        "I hope things will be picking up soon.",
                        "Perhaps it's time for some new leadership?",
                        "Anybody else feel like things could be better?"));
                print(gc.getName() + " was thinking about leaving the party but has decided to stay for now.");
            }
        }
    }

    private boolean wantsToLeave(Model model, GameCharacter gc) {
        model.getParty().partyMemberSay(model, gc, List.of("I'm sick of this.", "It's time for me to go my own way.",
                "I don't want to do this anymore.", "I want to leave the party.", "Guys... I'm done. I want out.",
                "I think my adventuring days are over.", "I'm fed up with you " + model.getParty().getLeader().getFirstName() + "."));
        int goldDemanded = gc.getCharClass().getStartingGold() + gc.getLevel() * 5 + 5;
        print(gc.getName() + " wants to leave the party and will return all equipped items, but demands " + goldDemanded + " gold as a wage.");
        List<String> options = new ArrayList<>(List.of("Don't pay wage, lose items", "Refuse to pay, demand items"));
        int offset = 0;
        if (model.getParty().getGold() >= goldDemanded) {
            options.add(0, "Pay wage and keep items");
            offset = 1;
        }
        int result = multipleOptionArrowMenu(model, 24, 15, options);
        if (result == offset - 1) {
            sayGoodbye(model, gc);
            model.getParty().remove(gc, true, true, goldDemanded);
            print("!" + gc.getName() + " has left the party!");
        } else if (result == offset) {
            sayGoodbye(model, gc);
            model.getParty().remove(gc, false, false, 0);
            print("!" + gc.getName() + " has left the party!");
        } else {
            leaderSay("I'm sorry " + gc.getName() + " but I can't pay you, and I'm going to need those items.");

            List<GameCharacter> separtists = new ArrayList<>();
            for (GameCharacter gc2 : model.getParty().getPartyMembers()) {
                if (gc2 != model.getParty().getLeader() && gc2 != gc && gc2.getAttitude(model.getParty().getLeader()) < 0) {
                    separtists.add(gc2);
                }
            }
            if (separtists.isEmpty()) {
                partyMemberSay(gc, "... Fine... here you go. But I'm out of here.");
                model.getParty().remove(gc, true, false, 0);
                print("!" + gc.getName() + " has left the party!");
            } else {
                partyMemberSay(gc, "Oh yeah? Well I'm not going to let you walk all over me! I'm leaving, and anyone else " +
                        "who's sick of " + model.getParty().getLeader().getFirstName() + " is welcome to join my new party.");
                model.getParty().remove(gc, false, false, 0);
                print("!" + gc.getName() + " has left the party!");
                for (GameCharacter s : separtists) {
                    model.getParty().partyMemberSay(model, s,
                            List.of("Me too.", "I'm leaving too.", "I'm out of here too.", "Yeah. I'm out."));
                    model.getParty().remove(s, false, false, 0);
                    print("!" + gc.getName() + " has left the party!");
                }
                separtists.add(gc);
                runCombat(transformToEnemies(separtists), model.getCurrentHex().getCombatTheme(), true);
                return true;
            }
        }
        return false;
    }

    private List<Enemy> transformToEnemies(List<GameCharacter> separtists) {
        List<Enemy> enemies = new ArrayList<>();
        for (GameCharacter gc : separtists) {
            enemies.add(new FormerPartyMemberEnemy(gc));
        }
        return enemies;
    }

    private void sayGoodbye(Model model, GameCharacter gc) {
        model.getParty().partyMemberSay(model, gc,
                List.of("See ya!", "Bye", "So long", "See you around I guess.", "Farewell",
                        "Until we meet again.", "I'm out of here."));
    }
}
