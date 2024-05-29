package model.states.events;

import model.Model;
import model.characters.GameCharacter;
import model.classes.Skill;
import model.classes.SkillCheckResult;
import model.enemies.Enemy;
import model.enemies.FormerPartyMemberEnemy;
import model.states.DailyEventState;
import util.MyLists;
import util.MyRandom;
import view.MyColors;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PartyMemberWantsToLeaveEvent extends DailyEventState {
    public PartyMemberWantsToLeaveEvent(Model model) {
        super(model);
    }

    @Override
    protected void doEvent(Model model) {
        List<GameCharacter> candidates = MyLists.filter(model.getParty().getPartyMembers(),
                (GameCharacter gc) ->
                    (gc != model.getParty().getLeader() &&
                     gc.getAttitude(model.getParty().getLeader()) < 0));

        if (candidates.isEmpty()) {
            checkForDissidents(model);
        } else {
            rollForLeaderHaters(model, candidates);
            showPartyAttitudesSubView(model);
        }
    }

    private void checkForDissidents(Model model) {
        List<GameCharacter> candidates = new ArrayList<>();
        for (GameCharacter gc : model.getParty().getPartyMembers()) {
            int dislikes = 0;
            for (GameCharacter other : model.getParty().getPartyMembers()) {
                if (other != gc && gc.getAttitude(other) <= 10) {
                    dislikes++;
                }
            }
            if (dislikes >= 2) {
                candidates.add(gc);
            }
        }
        if (candidates.isEmpty()) {
            new NoEventState(model).doTheEvent(model);
            return;
        }
        Collections.shuffle(candidates);
        GameCharacter potentialLeaver = candidates.get(0);
        println(potentialLeaver.getFirstName() + " approaches you.");
        partyMemberSay(potentialLeaver, "Hey... I've been thinking about leaving the party.");
        leaderSay("What's the matter " + potentialLeaver.getFirstName() + "? Don't like my style of leadership?");
        partyMemberSay(potentialLeaver, "No it's not you, it's some of these other guys. They don't seem to get me.");
        SkillCheckResult result = model.getParty().getLeader().testSkill(model, Skill.Persuade, 10,
                model.getParty().getLeader().getRankForSkill(Skill.Leadership));
        if (result.isSuccessful()) {
            leaderSay(potentialLeaver.getFirstName() + ", you are a great asset to this company. We need you. " +
                    "Just ignore the others for the time being. And... I'll talk to them. Okay?");
            partyMemberSay(potentialLeaver, "I... alright. I'll stay.");
            println(model.getParty().getLeader() + " has managed to persuade " + potentialLeaver.getName() +
                    " to stay for now (Persuade " + result.asString());
            showPartyAttitudesSubView(model);
        } else {
            leaderSay(potentialLeaver.getFirstName() + ", if you want to leave, I won't stand in your way.");
            println(model.getParty ().getLeader() + " failed to persuade " + potentialLeaver.getName() +
                    " to stay in the party. (Persuade " + result.asString());
            wantsToLeave(model, potentialLeaver, false);
        }
    }

    private void rollForLeaderHaters(Model model, List<GameCharacter> candidates) {
        for (GameCharacter gc : candidates) {
            if (MyRandom.randInt(30) > 30 + gc.getAttitude(model.getParty().getLeader())) {
                if (wantsToLeave(model, gc, true)) {
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

    private boolean wantsToLeave(Model model, GameCharacter gc, boolean angryAtLeader) {
        if (angryAtLeader) {
            model.getParty().partyMemberSay(model, gc, List.of("I'm sick of this.", "It's time for me to go my own way.",
                    "I don't want to do this anymore.", "I want to leave the party.", "Guys... I'm done. I want out.",
                    "I think my adventuring days are over.", "I'm fed up with you " + model.getParty().getLeader().getFirstName() + "."));
        }
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
            printAlert(gc.getName() + " has left the party!");
        } else if (result == offset) {
            sayGoodbye(model, gc);
            model.getParty().remove(gc, false, false, 0);
            printAlert(gc.getName() + " has left the party!");
        } else {
            leaderSay("I'm sorry " + gc.getName() + " but I can't pay you, and I'm going to need those items.");

            List<GameCharacter> separtists = MyLists.filter(model.getParty().getPartyMembers(), (GameCharacter gc2) ->
                    (gc2 != model.getParty().getLeader() && gc2 != gc && gc2.getAttitude(model.getParty().getLeader()) < 0));

            if (separtists.isEmpty()) {
                partyMemberSay(gc, "... Fine... here you go. But I'm out of here.");
                model.getParty().remove(gc, true, false, 0);
                printAlert(gc.getName() + " has left the party!");
            } else {
                partyMemberSay(gc, "Oh yeah? Well I'm not going to let you walk all over me! I'm leaving, and anyone else " +
                        "who's sick of " + model.getParty().getLeader().getFirstName() + " is welcome to join my new party.");
                model.getParty().remove(gc, false, false, 0);
                printAlert(gc.getName() + " has left the party!");
                for (GameCharacter s : separtists) {
                    model.getParty().partyMemberSay(model, s,
                            List.of("Me too.", "I'm leaving too.", "I'm out of here too.", "Yeah. I'm out."));
                    model.getParty().remove(s, false, false, 0);
                    printAlert(gc.getName() + " has left the party!");
                }
                separtists.add(gc);
                runCombat(transformToEnemies(separtists), model.getCurrentHex().getCombatTheme(), true);
                return true;
            }
        }
        return false;
    }

    private List<Enemy> transformToEnemies(List<GameCharacter> separtists) {
        return MyLists.transform(separtists, FormerPartyMemberEnemy::new);
    }

    private void sayGoodbye(Model model, GameCharacter gc) {
        model.getParty().partyMemberSay(model, gc,
                List.of("See ya!", "Bye", "So long", "See you around I guess.", "Farewell",
                        "Until we meet again.", "I'm out of here."));
    }
}
