package model.states.events;

import model.Model;
import model.characters.GameCharacter;
import model.classes.Classes;
import model.classes.Skill;
import model.enemies.ConstableEnemy;
import model.states.DailyEventState;
import util.MyRandom;
import util.MyStrings;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConstableEvent extends DailyEventState {
    private final Map<Integer, Integer> alignmentMap;
    private final String perp;
    private final Integer sentence;
    private boolean runAway;

    public ConstableEvent(Model model) {
        super(model);
        runAway = false;
        alignmentMap = makePartyAlignmentMap();
        List<String> perpList = List.of("troublemaker", "thief", "burglar", "saboteur", "spy", "murderer");
        List<Integer> sentenceList = List.of(1, 2, 3, 5, 7, 10);
        int random = MyRandom.randInt(perpList.size());
        this.perp = perpList.get(random);
        this.sentence = sentenceList.get(random);
    }

    @Override
    protected void doEvent(Model model) {
        boolean gender = MyRandom.randInt(2) == 0;
        print("The party encounters a constable on the street. " +
                heOrSheCap(gender) + " approaches you, do you run away? (Y/N) ");
        if (yesNoInput()) {
            runAway = true;
        } else {

            println("Constable: \"We're looking for a " + perp + " in this area. You lot " +
                    "seen anything suspicious?\"");
            model.getParty().randomPartyMemberSay(model, List.of("Nope, not a thing.", "No sir.",
                    "I don't know what you're talking about.", "I don't remember..."));
            println("Constable: \"You're not from around here are you?\"");
            println("The constable squints and carefully looks at the party members...");
            int sum = 0;
            for (GameCharacter gc : model.getParty().getPartyMembers()) {
                int modifier = 0;
                if (alignmentMap.containsKey(gc.getCharClass().id())) {
                    modifier = alignmentMap.get(gc.getCharClass().id());
                }
                println("... " + gc.getFirstName() + " is a " + gc.getCharClass().getFullName() + ": " + MyStrings.withPlus(modifier));
                sum += modifier;
            }
            println("... Bonus for reputation: " + MyStrings.withPlus(model.getParty().getReputation()));
            println("... Total party alignment: " + MyStrings.withPlus(sum));
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
            print("Constable: \"Hmm, you fellows look " + wordToDescribe);
            if (sum >= 3) {
                println(" You can go about your business.");
                return;
            } else {
                println(" You should come with me for questioning.\"");
            }
            model.getParty().randomPartyMemberSay(model, List.of("Hang on a second!",
                    "I think there's been some kind of misunderstanding.", "Wait just a minute.",
                    "Please, sir. Hear us out."));
            boolean result = model.getParty().doSoloSkillCheck(model, this, Skill.Persuade, 7-sum);
            if (result) {
                println("You manage to convince the constable you are completely innocent.");
                println("Constable: \"Hmm, well... Stay out of trouble!\"");
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

    private static Map<Integer, Integer> makePartyAlignmentMap() {
        Map<Integer, Integer> classMap = new HashMap<>();
        classMap.put(Classes.ASN.id(), -2);
        classMap.put(Classes.BKN.id(), -2);
        classMap.put(Classes.THF.id(), -1);
        classMap.put(Classes.SOR.id(), -1);
        classMap.put(Classes.WIT.id(), -1);
        classMap.put(Classes.BBN.id(), -1);
        classMap.put(Classes.SPY.id(), -1);

        classMap.put(Classes.PRI.id(), +1);
        classMap.put(Classes.PAL.id(), +1);
        classMap.put(Classes.NOB.id(), +1);
        return classMap;
    }

    @Override
    public boolean haveFledCombat() {
        return runAway;
    }
}
