package model.states.events;

import model.Model;
import model.characters.GameCharacter;
import model.characters.appearance.AdvancedAppearance;
import model.classes.Classes;
import model.classes.Skill;
import model.classes.SkillCheckResult;
import model.enemies.BoozerEnemy;
import model.enemies.Enemy;
import model.items.Item;
import model.items.ItemDeck;
import model.items.potions.BeerPotion;
import model.items.potions.IntoxicatingPotion;
import model.items.potions.WinePotion;
import model.states.DailyEventState;
import util.MyLists;
import util.MyRandom;
import view.subviews.PortraitSubView;

import java.util.ArrayList;
import java.util.List;

public class BoozersEvent extends DailyEventState {
    private final AdvancedAppearance boozer1;
    private final AdvancedAppearance boozer2;
    private AdvancedAppearance currentPortrait = null;

    public BoozersEvent(Model model) {
        super(model);
        boozer1 = PortraitSubView.makeRandomPortrait(Classes.None);
        boozer2 = PortraitSubView.makeRandomPortrait(Classes.None);
    }

    @Override
    public GuideData getGuideData() {
        return new GuideData("Meet drunks",
                "I know a couple of rowdy drunks, but you probably don't want to meet them");
    }

    @Override
    protected void doEvent(Model model) {
        String pair = "";
        if (boozer1.getGender() && boozer2.getGender()) {
            pair = " of women";
        } else if (!boozer1.getGender() && !boozer2.getGender()) {
            pair = " of men";
        }
        showEventCard("Boozers", "Intending to take a short cut, you cut through an alley. You encounter a pair" + pair + " who seem a bit odd. " +
                "They're a bit rowdy and it's clear they've been drinking. Rather than go back, you attempt to push " +
                "past them in the narrow alley but end up accidentally bumping into one of them.");
        leaderSay("Excuse " + meOrUs() + ".");
        boozer1Say("Hey. Whadda youz think youz doin, ehh?");
        leaderSay("Uhm... pardon?");
        boozer1Say("Can ya believe theez eh? They's all walkin' around likez they ownz da place.");
        boozer2Say("No manners at... hic... all. No manners.");
        boozer1Say("Darn outsiders! hic... shlummin up our town!");
        boozer2Say("Ought to be... hic... ought to be... hic... taught a lessshon.");
        leaderSay("Look. " + iOrWeCap() + " really don't want any trouble. Just going about " + myOrOur() + " business friend.");
        boozer1Say("Who ya callin' friend, shlummy!");
        leaderSay("Oh boy...");
        List<String> options = new ArrayList<>(List.of("Attempt persuade", "Attempt intimidate", "Attack", "Run away"));
        if (hasDrink(model)) {
            options.add("Offer drink");
        }
        int choice = multipleOptionArrowMenu(model, 24, 24, options);
        if (choice == 0) {
            persuade(model);
        } else if (choice == 1) {
            intimidate(model);
        } else if (choice == 2) {
            attack(model);
        } else if (choice == 3) {
            runAway(model);
        } else {
            offerDrink(model);
        }
    }

    private void persuade(Model model) {
        println("You attempt to reason with the drunken " + manOrWoman(boozer1.getGender()) + ".");
        SkillCheckResult result = model.getParty().doSkillCheckWithReRoll(model, this, model.getParty().getLeader(), Skill.Persuade,
                8, 10, 0);
        if (result.isSuccessful()) {
            leaderSay("Look, we're actually on our way out of here. Don't worry we'll be gone from your town in no time.");
            boozer1Say("Darn straight! Don't want shlummies around here anyway...");
            println("The boozers go back to their bottles, and you move on.");
        } else {
            leaderSay("Get your stinking mug out of my face...");
            boozer1Say("Huh, got some cheek to ya? Youz will regrett messhin with ush...");
            attack(model);
        }
    }

    private void intimidate(Model model) {
        println("You attempt to frighten the drunken " + manOrWoman(boozer1.getGender()) + ".");
        SkillCheckResult result = model.getParty().doIntimidationSkillCheck(model, this, model.getParty().getLeader(), 9, 10);
        if (result.isSuccessful()) {
            leaderSay("You see these weapons on our belts? They're not just for show. See the blood stains on them? " +
                    "Now get out of my way. " + iOrWeCap() + " mean business.");
            println("The boozer appears to sober up a little and backs away.");
            boozer1Say("Hic... huh? Hey... I wassh jussht jokin with yer.");
            leaderSay("...");
            println("The boozers go back to their bottles, and you move on.");
        } else {
            leaderSay("Look at my muscles... roar... I'm a tough " + (model.getParty().getLeader().getGender() ? "gal":"guy") + "!");
            portraitSay("Dat shposed to shcare us? Youz will regrett messhin with ush...");
            attack(model);
        }
    }

    private void attack(Model model) {
        runCombat(makeEnemyList());
    }

    private List<Enemy> makeEnemyList() {
        return List.of(new BoozerEnemy('B', boozer1.getRace()),
                new BoozerEnemy('B', boozer2.getRace()));
    }

    private void runAway(Model model) {
        println("You start to turn away from the boozers.");
        boozer2Say("Hey... theyz trying to get away!");
        int minSpeed = MyLists.minimum(model.getParty().getPartyMembers(), GameCharacter::getSpeed);
        if (MyRandom.rollD10() <= minSpeed) {
            println("The drunken duo is no match for your speed and you quickly manage to get away.");
        } else if (MyRandom.rollD10() <= minSpeed + 3) {
            println("You try running away but the two boozers keep chasing you until you reach the city limits. " +
                    "There, they seem to give up the chase.");
            setFledCombat(true);
        } else {
            println("The two drunks quickly catch up to you and attack you.");
            runAmbushCombat(makeEnemyList(), model.getCurrentHex().getCombatTheme(), true);
        }
    }

    private void offerDrink(Model model) {
        Item wine = MyLists.find(model.getParty().getInventory().getAllItems(), (Item it) -> it instanceof WinePotion);
        Item beer = MyLists.find(model.getParty().getInventory().getAllItems(), (Item it) -> it instanceof BeerPotion);
        if (wine != null && beer != null) {
            print("You have both beer and wine in your inventory. Do you offer beer (Y) or wine (N) to the boozer? ");
            if (yesNoInput()) {
                wine = null;
            } else {
                beer = null;
            }
        }
        if (wine != null) {
            offerBeerOrWine(model, (IntoxicatingPotion) wine);
        } else {
            offerBeerOrWine(model, (IntoxicatingPotion) beer);
        }
    }

    private void offerBeerOrWine(Model model, IntoxicatingPotion drink) {
        leaderSay("Relax... we mean no harm. Why don't you have this.");
        String drinkName = drink.getName().toLowerCase();
        println("You offer the " + drinkName + " to the boozer.");
        model.getParty().removeFromInventory(drink);
        boozer1Say("Uh... hic... a drink? Gimme!");
        if (drink.doesReject(model, boozer1.getRace())) {
            println("The boozer is about to have a sip of the " + drinkName + ", but then recoils with a jerk.");
            boozer1Say("Whaaa... what kind of swill is thish?#");
            boozer2Say("Everybody, hic, knowsh your kind hatesh " + drinkName + "!" +
                            "I think " + heOrShe(model.getParty().getLeader().getGender()) + "'s tryin' to inshult ya!");
            println("The drunk throws the drink at you, then attacks!");
            attack(model);
        } else {
            println("The boozer takes a sip of the " + drinkName + ".");
            boozer1Say("Eeeh he he... Maybe thish feller might be alright, eh?");
            boozer2Say("Hic! Yeash, seems like a good egg... hic...");
            boozer1Say("Stay and drink with ussh for a bit eh?");
            model.getLog().waitForAnimationToFinish();
            getPortraitSubView().forceEyesClosed(true);
            print("You stay for a while with the two drunks. They keep bringing out more bottles out of a bag. " +
                    "Soon they are both snoring loudly. Do you want to loot the bag? (Y/N) ");
            if (yesNoInput()) {
                int roll = MyRandom.rollD6();
                if (roll <= 2) {
                    int goldAmount = MyRandom.rollD10() + 4;
                    println("You find " + goldAmount + " gold in the bag.");
                } else if (roll <= 4) {
                    Item it = model.getItemDeck().draw(1, 0.95).get(0);
                    println("You find an item, a " + it.getName() + "!");
                    it.addYourself(model.getParty().getInventory());
                } else if (roll <= 6) {
                    println("You find several more bottles in the bag.");
                    for (int i = MyRandom.randInt(4, 8); i > 0; i--) {
                        Item it = MyRandom.flipCoin() ? new WinePotion() : new BeerPotion();
                        println("You find a " + it.getName() + ".");
                        it.addYourself(model.getParty().getInventory());
                    }
                }
            }
            println("You leave the drunks to their nap and go about your business.");
        }
    }

    private boolean hasDrink(Model model) {
        return MyLists.any(model.getParty().getInventory().getAllItems(),
                (Item it) -> it instanceof IntoxicatingPotion);
    }

    private void boozer1Say(String line) {
        boozerSay(boozer1, "Boozer 1", line);
    }

    private void boozer2Say(String line) {
        boozerSay(boozer2, "Boozer 2", line);
    }

    private void boozerSay(AdvancedAppearance boozer, String name, String line) {
        if (currentPortrait != boozer) {
            if (currentPortrait != null) {
                removePortraitSubView(getModel());
            }
            showExplicitPortrait(getModel(), boozer, name);
            currentPortrait = boozer;
        }
        portraitSay(line);
        getModel().getLog().waitForAnimationToFinish();
    }
}
