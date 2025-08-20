package model.states.events;

import model.Model;
import model.characters.GameCharacter;
import model.characters.PersonalityTrait;
import model.characters.appearance.AdvancedAppearance;
import model.classes.Classes;
import model.classes.Skill;
import model.enemies.ConstableEnemy;
import model.states.DailyEventState;
import util.MyLists;
import util.MyRandom;
import util.MyStrings;
import view.subviews.PortraitSubView;

import java.util.ArrayList;
import java.util.List;

public class ConstableEvent extends DailyEventState {
    private final String perp;
    private final Integer sentence;
    private final boolean withIntro;
    private final AdvancedAppearance app;
    private boolean runAway = false;

    public ConstableEvent(Model model, boolean withIntro) {
        super(model);
        this.app = PortraitSubView.makeRandomPortrait(Classes.CONSTABLE);
        if (withIntro) {
            List<String> perpList = List.of("troublemaker", "thief", "bandit", "saboteur", "spy", "murderer");
            List<Integer> sentenceList = List.of(1, 2, 3, 5, 7, 10);
            int random = MyRandom.randInt(perpList.size());
            this.perp = perpList.get(random);
            this.sentence = sentenceList.get(random) + model.getParty().getNotoriety() / 25;
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
        showExplicitPortrait(model, app, "Constable");
        boolean gender = app.getGender();
        if (withIntro) {
            print("The party encounters a constable on the street. " +
                    heOrSheCap(gender) + " approaches you, do you run away? (Y/N) ");
            if (yesNoInput()) {
                runAway = true;
                return;
            }
        }
        if (constableAssess(model, "constable", false)) {
            return;
        }
        print("The constable is attempting to arrest you. You could fight the " +
                "constable, but doing so will give increase your notoriety. Do you resist arrest? (Y/N) ");
        if (yesNoInput()) {
            resistArrest(model);
        } else {
            goToJail(model);
        }
    }

    public boolean constableAssess(Model model, String lawMan, boolean canBeBribed) {
        showExplicitPortrait(model, app, MyStrings.capitalize(lawMan));
        portraitSay("We're looking for a " + perp + " in this area. You lot " +
                "seen anything suspicious?");
        boolean didSay = randomSayIfPersonality(PersonalityTrait.rude, List.of(model.getParty().getLeader()),
                "Stop hassling us! Don't you have a sweet roll to get back to?");
        model.getParty().randomPartyMemberSay(model, List.of("Nope, not a thing.", "No sir.",
                "I don't know what you're talking about.", "I don't remember..."));
        portraitSay("You're not from around here are you?");
        println("The " + lawMan + " squints and carefully looks at the party members...");
        int sum = calculatePartyAlignment(model, this);
        sum -= model.getParty().getNotoriety() / 10;
        String wordToDescribe = null;
        if (sum <= -5) {
            wordToDescribe = "downright evil!";
        } else if (sum <= -2) {
            wordToDescribe = "like you're up to no good.";
        } else if (sum <= 0) {
            wordToDescribe = "rather suspicious.";
        } else if (sum >= 3) {
            wordToDescribe = "like good-natured souls.";
        } else {
            wordToDescribe = "a bit odd.";
        }
        if (sum >= 1) {
            portraitSay("Hmm, you fellows look " + wordToDescribe + " You can go about your business.");
            return true;
        }
        portraitSay("Hmm, you fellows look " + wordToDescribe + " You should come with me for questioning.");
        if (canBeBribed && model.getParty().getGold() > 0) {
            int selected = multipleOptionArrowMenu(model, 24, 25, List.of("Attempt bribe", "Attempt persuade"));
            if (selected == 0) {
                return attemptBribe(model, lawMan);
            }
        }
        return attemptPersuade(model, lawMan, sum);
    }

    private boolean attemptBribe(Model model, String lawMan) {
        int amount = 0;
        do {
            try {
                print("How large bribe would you like to present to the " + lawMan + "? ");
                amount = Integer.parseInt(lineInput());
                if (amount > model.getParty().getGold() || amount < 1) {
                    println("Please enter a valid amount.");
                } else {
                    break;
                }
            } catch (NumberFormatException nfe) {
                println("Please enter a valid amount.");
            }
        } while (true);
        String coinString = "these coins";
        if (amount == 1) {
            coinString = "this coin";
        }
        model.getParty().spendGold(amount);
        leaderSay(MyStrings.capitalize(lawMan) + "... I think you dropped " + coinString + " on the ground...");
        int x = model.getParty().getNotoriety() - amount;
        if (MyRandom.rollD10() < x) {
            portraitSay("What's this? A bribe? Don't you know bribing a " + lawMan + " is against the law!");
            GeneralInteractionEvent.addToNotoriety(model, this, 5);
            return false;
        }
        portraitSay("What now? Oh, I see. How kind of you to return " + coinString + " to me. Perhaps " +
                    "I can let you off with a warning this time.");
        leaderSay("Much appreciated " + lawMan + ".");
        return true;
    }

    private boolean attemptPersuade(Model model, String lawMan, int sum) {
        model.getParty().randomPartyMemberSay(model, List.of("Hang on a second!",
                "I think there's been some kind of misunderstanding.", "Wait just a minute.",
                "Please, sir. Hear us out."));
        boolean result = model.getParty().doSoloSkillCheck(model, this, Skill.Persuade, 6-sum);
        if (result) {
            println("You manage to convince the " + lawMan + " you are completely innocent.");
            portraitSay("Hmm, well... Stay out of trouble!");
            model.getParty().randomPartyMemberSay(model, List.of("Phew... close one."));
            return true;
        }
        println("The " + lawMan + " seems unconvinced by your story.");
        return false;
    }

    private void resistArrest(Model model) {
        boolean didSay = randomSayIfPersonality(PersonalityTrait.lawful, new ArrayList<>(),
                MyRandom.sample(List.of("Are we really doing this?", "This is wrong", "I don't feel good about this.")));
        didSay = randomSayIfPersonality(PersonalityTrait.mischievous, new ArrayList<>(), "Let's get him!") || didSay;
        if (!didSay) {
            model.getParty().randomPartyMemberSay(model, List.of("We're in over our heads here..."));
        }
        ConstableEnemy enm = new ConstableEnemy('A');
        runCombat(List.of(enm));
        if (enm.isDead()) {
            GeneralInteractionEvent.addMurdersToNotoriety(model, this, 1);
        } else {
            GeneralInteractionEvent.addToNotoriety(model, this, 30);
        }
    }

    public void goToJail(Model model) {
        println("You come along to the courthouse where a council promptly charges you and convicts you of being a " + perp + ".");
        leaderSay("But I'm innocent!");
        showRandomPortrait(model, Classes.OFFICIAL, "Clerk");
        portraitSay("You can either pay a fine of " + getFine() + " gold or spend " + getJailTime() +
                " days in the town jail.");
        if (getFine() > model.getParty().getGold()) {
            spendTimeInJail(model);
        } else {
            print("Pay the fine? (Y/N) ");
            if (yesNoInput()) {
                payFine(model);
            } else {
                spendTimeInJail(model);
            }
        }
        model.getParty().addToNotoriety(-model.getParty().getNotoriety());
        leaderSay("Coming here was a mistake.");
    }

    private void spendTimeInJail(Model model) {
        println("The party spends " + getJailTime() + " days in jail.");
        for (int i = 0; i < getJailTime(); ++i) {
            if (i > 0) {
                println("The party is in jail.");
            }
            stepToNextDay(model);
        }
        println("The party is released from jail. You feel happy to get out, but sitting in that cell for " +
                "days has left you tired and disillusioned.");
        println("Each party member loses all SP.");
        MyLists.forEach(model.getParty().getPartyMembers(), (GameCharacter gc) -> gc.addToSP(-gc.getSP()));
    }

    private void payFine(Model model) {
        println("The party lost " + getFine() + " gold.");
        model.getParty().loseGold(getFine());
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
