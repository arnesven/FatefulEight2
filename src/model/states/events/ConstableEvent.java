package model.states.events;

import model.Model;
import model.characters.GameCharacter;
import model.classes.Classes;
import model.classes.Skill;
import model.enemies.ConstableEnemy;
import model.states.DailyEventState;
import util.MyRandom;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConstableEvent extends DailyEventState {
    private final String perp;
    private final Integer sentence;
    private final boolean withIntro;
    private boolean runAway = false;

    public ConstableEvent(Model model, boolean withIntro) {
        super(model);
        if (withIntro) {
            List<String> perpList = List.of("troublemaker", "thief", "bandit", "saboteur", "spy", "murderer");
            List<Integer> sentenceList = List.of(1, 2, 3, 5, 7, 10);
            int random = MyRandom.randInt(perpList.size());
            this.perp = perpList.get(random);
            this.sentence = sentenceList.get(random);
        } else {
            this.perp = "burglar";
            this.sentence = 6;
        }
        this.withIntro = withIntro;
    }

    public ConstableEvent(Model model) {
        this(model, true);
    }

    @Override
    protected void doEvent(Model model) {
        showRandomPortrait(model, Classes.CONSTABLE, "Constable");
        boolean gender = MyRandom.randInt(2) == 0;
        if (withIntro) {
            print("The party encounters a constable on the street. " +
                    heOrSheCap(gender) + " approaches you, do you run away? (Y/N) ");
            if (yesNoInput()) {
                runAway = true;
                return;
            }
        }
        portraitSay(model, "We're looking for a " + perp + " in this area. You lot " +
                "seen anything suspicious?");
        model.getParty().randomPartyMemberSay(model, List.of("Nope, not a thing.", "No sir.",
                "I don't know what you're talking about.", "I don't remember..."));
        portraitSay(model, "You're not from around here are you?");
        println("The constable squints and carefully looks at the party members...");
        int sum = getPartyAlignment(model, this);
        sum += model.getParty().getReputation();
        String wordToDescribe = null;
        if (sum <= -5) {
            wordToDescribe = "downright evil!";
        } else if (sum <= -2) {
            wordToDescribe = "like you're up to no good.";
        } else if (sum < 2) {
            wordToDescribe = "rather suspicious.";
        } else if (sum >= 3) {
            wordToDescribe = "like good-natured souls.";
        } else {
            wordToDescribe = "a bit odd.";
        }
        if (sum >= 3) {
            portraitSay(model, "Hmm, you fellows look " + wordToDescribe + " You can go about your business.");
            return;
        } else {
            portraitSay(model, "Hmm, you fellows look " + wordToDescribe + " You should come with me for questioning.");
        }
        model.getParty().randomPartyMemberSay(model, List.of("Hang on a second!",
                "I think there's been some kind of misunderstanding.", "Wait just a minute.",
                "Please, sir. Hear us out."));
        boolean result = model.getParty().doSoloSkillCheck(model, this, Skill.Persuade, 6-sum);
        if (result) {
            println("You manage to convince the constable you are completely innocent.");
            portraitSay(model, "Hmm, well... Stay out of trouble!");
            model.getParty().randomPartyMemberSay(model, List.of("Phew... close one."));
        } else {
            print("The constable is unconvinced and is attempting to arrest you. You could fight the " +
                    "constable, but doing so will give you a -1 penalty to your reputation. Do you resist arrest? (Y/N) ");
            if (yesNoInput()) {
                resistArrest(model);
            } else {
                goToJail(model);
            }
        }
    }

    private void resistArrest(Model model) {
        model.getParty().randomPartyMemberSay(model, List.of("I don't feel good about this.",
                "Let's get him!", "Are we really doing this?", "We're in over our heads here..."));
        println("The party gets -1 reputation!");
        model.getParty().addToReputation(-1);
        runCombat(List.of(new ConstableEnemy('A')));
    }

    private void goToJail(Model model) {
        println("You come along to the courthouse where a council promptly charges you and convicts you of being a " + perp + ".");
        model.getParty().partyMemberSay(model, model.getParty().getLeader(), "But I'm innocent!");
        println("Clerk: \"You can either pay a fine of " + getFine() + " gold or spend " + getJailTime() +
                " days in the town jail.\"");
        if (sentence*5 > model.getParty().getGold()) {
            spendTimeInJail(model);
        } else {
            print("Pay the fine? (Y/N) ");
            if (yesNoInput()) {
                payFine(model);
            } else {
                spendTimeInJail(model);
            }
        }
        model.getParty().partyMemberSay(model, model.getParty().getLeader(), "Coming here was a mistake.");
    }

    private void spendTimeInJail(Model model) {
        println("The party spends " + getJailTime() + " days in jail.");
        for (int i = 0; i < getJailTime(); ++i) {
            if (i > 0) {
                println("The party is in jail.");
            }
            model.incrementDay();
        }
        println("The party is released from jail. You feel happy to get out, but sitting in that cell for " +
                "days has left you tired and disillusioned.");
        println("Each party member loses all SP.");
        for (GameCharacter gc : model.getParty().getPartyMembers()) {
            gc.addToSP(-gc.getSP());
        }
    }

    private void payFine(Model model) {
        println("The party lost " + getFine() + " gold.");
        model.getParty().addToGold(-getFine());
    }

    private int getJailTime() {
        return sentence*2;
    }

    private int getFine() {
        return sentence*5;
    }

    @Override
    public boolean haveFledCombat() {
        return runAway;
    }
}
