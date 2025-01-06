package model.states.events;

import model.Model;
import model.characters.GameCharacter;
import model.classes.Skill;
import model.classes.SkillCheckResult;
import model.combat.conditions.CowardlyCondition;
import model.combat.conditions.RoutedCondition;
import model.enemies.FormerPartyMemberEnemy;
import model.items.Item;
import model.items.potions.BeerPotion;
import model.items.potions.IntoxicatingPotion;
import model.items.potions.WinePotion;
import model.races.Race;
import model.states.DailyEventState;
import util.MyLists;
import util.MyRandom;

import java.util.ArrayList;
import java.util.List;

public class IncapacitateNPCEvent extends DailyEventState {
    private final GameCharacter npc;
    private final int persuadeDifficulty;
    private final String argument;
    private final int maxBribe;
    private final int drinkLimit;
    private boolean success = false;
    private int drinkCount = 0;

    public IncapacitateNPCEvent(Model model, GameCharacter npc, int maxBribe,
                                int persuadeDifficulty, String persuasionArgument) {
        super(model);
        this.npc = npc;
        this.maxBribe = maxBribe;
        this.persuadeDifficulty = persuadeDifficulty;
        this.argument = persuasionArgument;
        this.drinkLimit = findDrinkLimit(npc);
    }

    private int findDrinkLimit(GameCharacter npc) {
        int raceAddition = MyRandom.randInt(2, 4);
        if (npc.getRace().id() == Race.DWARF.id()) {
            raceAddition = MyRandom.randInt(3, 6);
        } else if (npc.getRace().id() == Race.HALF_ORC.id()) {
            raceAddition = MyRandom.randInt(2, 5);
        } else if (npc.getRace().id() == Race.HALFLING.id()) {
            raceAddition = MyRandom.randInt(1, 2);
        }
        int genderExtra = npc.getGender() ? 0 : 1;
        return raceAddition + genderExtra;
    }

    @Override
    protected void doEvent(Model model) {
        showExplicitPortrait(model, npc.getAppearance(), npc.getName());
        leaderSay("Hello there " + npc.getName() + ".");
        portraitSay(MyRandom.sample(List.of("Hello. Did you want something?",
                "Yes, can I help you?",
                "Yes, what is it?")));

        do {
            List<String> options = new ArrayList<>(List.of("Attack", "Persuade", "Intimidate"));
            if (model.getParty().getGold() > minimumBribe()) {
                options.add("Bribe");
            }
            if (model.getParty().size() > 1) {
                options.add("Knock unconscious");
            }
            if (hasDrinks(model)) {
                options.add("Offer drink");
            }
            options.add("Cancel");
            int choice = multipleOptionArrowMenu(model, 24, 24, options);
            if (choice == options.size()-1) {
                leaderSay("Never mind.");
                break;
            }
            if (choice == 0) {
                leaderSay("You're dead meat " + npc.getName() + "!");
                portraitSay("What are you doing? Help! I'm being attacked!");
                attack(model);
                break;
            }
            if (choice == 1) {
                persuade(model);
                break;
            }
            if (choice == 2) {
                intimidate(model);
                break;
            }
            if (options.get(choice).contains("Knock")) {
                knockUnconscious(model);
                break;
            }

            if (options.get(choice).equals("Bribe")) {
                if (bribeMenu(model)) {
                    break;
                }
            } else if (options.get(choice).equals("drink")) {
                if (offerDrink(model)) {
                    break;
                }
            }
        } while (true);
        if (success) {
            removePortraitSubView(model);
        }
    }

    private boolean offerDrink(Model model) {
        Item wine = MyLists.find(model.getParty().getInventory().getAllItems(), (Item it) -> it instanceof WinePotion);
        Item beer = MyLists.find(model.getParty().getInventory().getAllItems(), (Item it) -> it instanceof BeerPotion);
        if (wine != null && beer != null) {
            print("You have both beer and wine in your inventory. Do you offer beer (Y) or wine (N) to " + npc.getName() + "? ");
            if (yesNoInput()) {
                wine = null;
            } else {
                beer = null;
            }
        }
        if (wine != null) {
            return offerBeerOrWine(model, (IntoxicatingPotion) wine);
        }
        return offerBeerOrWine(model, (IntoxicatingPotion) beer);
    }

    private boolean offerBeerOrWine(Model model, IntoxicatingPotion drink) {
        if (drinkCount == 0) {
            leaderSay("I was wondering if I could offer you a celebratory drink?");
        } else {
            leaderSay("How about another drink?");
        }
        portraitSay("Uhm, sure. Why not?");
        String drinkName = drink.getName().toLowerCase();
        println("You offer the " + drinkName + " to " + npc.getName() + ".");
        model.getParty().getInventory().remove(drink);
        if (drink.doesReject(model, npc.getRace())) {
            portraitSay(drink.getName() + "? I detest " + drinkName + "!");
            println(npc.getName() + " is annoyed and walks off.");
            leaderSay("Pearls for swine...#");
            return true;
        }
        portraitSay("Ahh... That was refreshing! Many thanks friend!");
        drinkCount++;
        if (drinkCount == drinkLimit) {
            portraitSay("Uhh... I don't feel so good.");
            leaderSay("You look a little pale friend. Why don't you sit down for a minute?");
            portraitSay("Good idea. I think I'll just... sit down, or lie down, for just a minute.");
            println(npc.getName() + " lies down. After a minute, " + heOrShe(npc.getGender()) + " starts snoring loudly.");
            leaderSay(heOrShe(npc.getGender()) + "'s out.");
            success = true;
            return true;
        } else if (drinkCount > 1) {
            println(npc.getName() + MyRandom.sample(List.of(
                    "'s nose is getting a little red.",
                    " is starting to mumble a little while talking.",
                    " is swaying back and forth a little.")));
        }
        return false;
    }

    private void knockUnconscious(Model model) {
        GameCharacter talker = model.getParty().getLeader();
        GameCharacter sneaker = model.getParty().getRandomPartyMember(model.getParty().getLeader());
        if (model.getParty().size() > 2) {
            print("Who should attempt to sneak up on " + npc.getName() + " and knock " +
                    himOrHer(npc.getGender()) + " out?");
            sneaker = model.getParty().partyMemberInput(model, this, sneaker);
            if (sneaker == talker) {
                talker = model.getParty().getRandomPartyMember(sneaker);
            }
        }
        print("While " + talker.getFirstName() + " distracts " + npc.getName() + ", " + sneaker.getName() +
                " sneaks around from behind ");
        SkillCheckResult result = model.getParty().doSkillCheckWithReRoll(model, this,
                sneaker, Skill.Sneak, 8, 10, 0);
        println("(Sneak " + result.asString() + ").");
        if (!result.isSuccessful()) {
            println("But " + heOrShe(sneaker.getGender()) + " is spotted by " + npc.getName() + ".");
            portraitSay("What's going on here? What are you trying to pull?");
            println(npc.getName() + " attacks you!");
            attack(model);
            return;
        }

        print(sneaker.getName() + " tries to knock " + npc.getName() + " out with a well placed thump on the head ");
        result = model.getParty().doSkillCheckWithReRoll(model, this,
                sneaker, Skill.BluntWeapons, 8, 20, 0);
        println("(Blunt Weapons " + result.asString() + ").");
        if (!result.isSuccessful()) {
            println("But " + heOrShe(sneaker.getGender()) + " misses.");
            portraitSay("Ouch! Hey, what are you doing! Help, I'm being attacked!");
            attack(model);
            return;
        }

        println("Bonk! " + npc.getName() + " collapses in front of " + talker.getName() + ".");
        partyMemberSay(talker, "Nice one " + sneaker.getFirstName() + ".");
        partyMemberSay(sneaker, MyRandom.sample(List.of("It's what I do.", "Child's play.",
                "Thanks. Good teamwork.", "An easy mark.")));
        success = true;
    }

    private boolean bribeMenu(Model model) {
        print("How large of a bribe would you like to offer " + npc.getName() + "?");
        List<Integer> opts = new ArrayList<>(List.of(minimumBribe(), mediumBribe(), maxBribe));
        opts.removeIf(i -> i > model.getParty().getGold());
        List<String> options = MyLists.transform(opts, i -> i + " gold");
        options.add("Cancel");
        int choice = multipleOptionArrowMenu(model, 24, 24, options);
        if (choice == options.size() - 1) {
            return false;
        }

        int bribe = opts.get(choice);
        leaderSay("Listen " + npc.getName() + ". I've got " + bribe + " gold here. It's yours if " +
                "you just leave right now. Okay?");
        if (choice == 0) {
            portraitSay("What? I won't be swayed by such a paltry sum!");
            println(npc.getName() + " turns away. Clearly you've offended " + himOrHer(npc.getGender()) + ".");
            return true;
        }
        if (choice == 1) {
            portraitSay(bribe + " gold? Hmm... no I don't think so.");
            return false;
        }

        portraitSay("Okay, it's a deal. Hand it over and you won't see me again.");
        println("You hand over " + bribe + " gold to " + npc.getName() + ".");
        model.getParty().addToGold(-bribe);
        success = true;
        return true;
    }

    private int mediumBribe() {
        return maxBribe / 2;
    }

    private int minimumBribe() {
        return maxBribe / 10;
    }

    private void persuade(Model model) {
        print("Who should attempt to reason with " + npc.getName() + "?");
        GameCharacter talker = model.getParty().partyMemberInput(model, this, model.getParty().getPartyMember(0));
        SkillCheckResult result = model.getParty().doSkillCheckWithReRoll(model, this,talker, Skill.Persuade,
                persuadeDifficulty, 20, 0);
        if (result.isSuccessful()) {
            leaderSay(argument);
            portraitSay("Hmm... you make a good point. Okay, I'll leave.");
            leaderSay("It's decided.");
            success = true;
        } else {
            partyMemberSay(talker, "Any chance I could persuade you to just leave?");
            portraitSay("Uhm. No. Now excuse me.");
        }
    }

    private void intimidate(Model model) {
        print("Who should attempt to frighten " + npc.getName() + "?");
        GameCharacter talker = model.getParty().partyMemberInput(model, this, model.getParty().getPartyMember(0));
        SkillCheckResult result = model.getParty().doIntimidationSkillCheck(model, this, talker, 0, 20);
        if (result.isSuccessful()) {
            partyMemberSay(talker, "Listen " + npc.getName() + ". You better skedaddle if you know what's good for you. " +
                    "We don't want anybody to get hurt, do we?");
            portraitSay("Fine, I get the message. No need to get physical. Consider me gone.");
            println(npc.getName() + " leaves in a hurry.");
            success = true;
        } else {
            partyMemberSay(talker, "Look at my big muscles! I'm gonna hurt you! Roar!");
            portraitSay("Uhm, are you feeling all right?");
            partyMemberSay(talker, "I'm fine...");
            println(npc.getName() + " walks off, slightly annoyed");
        }
    }

    private void attack(Model model) {
        model.getLog().waitForAnimationToFinish();
        FormerPartyMemberEnemy enemy = new FormerPartyMemberEnemy(npc);
        enemy.addCondition(new RoutedCondition());
        runCombat(List.of(enemy));
        setCurrentTerrainSubview(model);
        if (enemy.isDead()) {
            GeneralInteractionEvent.addMurdersToNotoriety(model, this, 1);
            leaderSay("Hmm... " + heOrShe(npc.getGender()) + " is dead.");
        } else {
            GeneralInteractionEvent.addToNotoriety(model, this, 1);
            leaderSay("Well, " + heOrShe(npc.getGender()) + "'s gone.");
        }
        this.success = true;
    }

    private boolean hasDrinks(Model model) {
        return MyLists.find(model.getParty().getInventory().getAllItems(),
                it -> it instanceof IntoxicatingPotion) != null;
    }

    public boolean wasSuccess() {
        return success;
    }
}
