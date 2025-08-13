package model.states.events;

import model.Model;
import model.characters.GameCharacter;
import model.characters.PersonalityTrait;
import model.classes.Classes;
import model.classes.Skill;
import model.classes.SkillCheckResult;
import model.combat.conditions.RowdyCondition;
import model.enemies.BrotherhoodCronyEnemy;
import model.enemies.Enemy;
import model.map.HexLocation;
import model.map.InnLocation;
import model.map.UrbanLocation;
import util.MyRandom;
import view.subviews.CollapsingTransition;
import view.subviews.ImageSubView;
import view.combat.MansionTheme;

import java.util.ArrayList;
import java.util.List;

public class TavernBrawlEvent extends PersonalityTraitEvent {
    private static final ImageSubView subView = new ImageSubView("theinnalt", "EVENT", "", true);

    public TavernBrawlEvent(Model model, PersonalityTrait trait, GameCharacter mainCharacter) {
        super(model, trait, mainCharacter);
    }

    @Override
    public boolean isApplicable(Model model) {
        HexLocation location = model.getCurrentHex().getLocation();
        if (location == null) {
            return false;
        }
        return location instanceof InnLocation || location instanceof UrbanLocation;
    }

    @Override
    protected void doEvent(Model model) {
        String innOrTavern = innOrTavern(model.getCurrentHex().getLocation());
        GameCharacter main = getMainCharacter();
        CollapsingTransition.transition(model, subView);
        println("You are enjoying a brew at the " + innOrTavern + ", when suddenly...");
        partyMemberSay(main, "What did you call me!!??");
        showRandomPortrait(model, Classes.None, "Patron");
        portraitSay("A thug! You spilled beer on my boots! Rowdy thugs like you should be thrown out of this place!");
        partyMemberSay(main, "Nobody calls me a thug!");
        leaderSay("Hey now... Let's just calm down a bit.");
        int chosen = multipleOptionArrowMenu(model, 24, 30, List.of("Offer to buy drink", "Persuade",
                "Intimidate", "Attack"));
        if (chosen == 0) {
            bribe(model, main);
        } else if (chosen == 1) {
            persuade(model, main);
        } else if (chosen == 2) {
            intimidate(model, main);
        } else {
            println("The patron is caught off guard by your sudden punch to his throat.");
            partyMemberSay(main, "That's what I'm talking about!");
            main.addToAttitude(model.getParty().getLeader(), 10);
            attack(model, main);
        }

    }

    private void bribe(Model model, GameCharacter main) {
        leaderSay("Why don't I pay for your next round? " +
                "Then we can forget about this and go back to our own business?");
        if (model.getParty().getGold() == 0) {
            println("You reach for your pouch and realize it's empty.");
            portraitSay("What is this? Are you some kind of comedian?");
            attack(model, main);
        } else {
            model.getParty().loseGold(1);
            println("You lose 1 gold.");
            println("You slap a coin on the bar and the bartender hands you another jug of beer, " +
                    "which you then hand over to the patron.");
            leaderSay("To your good health.");
            portraitSay("Bah... tourists...");
            partyMemberSay(main, "You should've let me punch that dotard.");
            leaderSay("Cool it " + main.getFirstName() + ". There will be plenty of fights to come.");
            println(main.getFirstName() + " just rolls " + hisOrHer(main.getGender()) + " eyes.");
            main.addToAttitude(model.getParty().getLeader(), -3);
            println(model.getParty().getLeader().getName() + " gains 10 XP.");
            model.getParty().giveXP(model, model.getParty().getLeader(), 10);
        }
    }


    private void persuade(Model model, GameCharacter main) {
        println("You attempt to reason with the patron.");
        SkillCheckResult result = model.getParty().doSkillCheckWithReRoll(model, this, model.getParty().getLeader(), Skill.Persuade,
                8, 10, 0);
        if (result.isSuccessful()) {
            leaderSay("You seem like an intelligent fellow, let's not ruin the atmosphere in here.");
            portraitSay("Of course not. If you lot can calm down, there'll be no trouble.");
            leaderSay("It's decided.");
            partyMemberSay(main, "Bah... that dotard needed to be taught a lesson.");
            main.addToAttitude(model.getParty().getLeader(), -1);
        } else {
            leaderSay("Look, " + main.getFirstName() + "'s beer has improved the color of your boots... " +
                    "How about we pour some more on?");
            portraitSay("That's it! You guys are begging for it!");
            attack(model, main);
        }
    }


    private void intimidate(Model model, GameCharacter main) {
        println("You attempt to frighten the patron.");
        SkillCheckResult result = model.getParty().doIntimidationSkillCheck(model, this, model.getParty().getLeader(), 9, 10);
        if (result.isSuccessful()) {
            leaderSay("Look, you don't want to mess with us. Now it's up to you, what do you want to do?");
            portraitSay("Maybe I was out of line. Sorry.");
            partyMemberSay(main, "Hah... wimp!");
            leaderSay("Don't push your luck " + main.getFirstName() + ".");
        } else {
            leaderSay("Look at my muscles... roar... I'm a tough guy!");
            portraitSay("What is this? Are you some kind of comedian?");
            attack(model, main);
        }
    }

    private void attack(Model model, GameCharacter main) {
        println("A violent bar fight breaks out!");
        List<GameCharacter> toBench = new ArrayList<>(model.getParty().getPartyMembers());
        toBench.remove(main);
        toBench.remove(model.getParty().getLeader());
        model.getParty().benchPartyMembers(toBench);

        main.unequipWeapon();
        model.getParty().getLeader().unequipWeapon();

        List<Enemy> enemies = new ArrayList<>();
        for (int i = MyRandom.randInt(3, 7); i > 0; --i) {
            enemies.add(new Brawler());
        }
        runCombat(enemies, new MansionTheme(), true);
        if (model.getParty().isWipedOut()) {
            return;
        }
        model.getParty().unbenchAll();
        println("You are kicked out of from the " + innOrTavern(model.getCurrentHex().getLocation()) + "!");
    }

    private String innOrTavern(HexLocation location) {
        if (location instanceof InnLocation) {
            return "Inn";
        }
        return "Tavern";
    }

    private static class Brawler extends BrotherhoodCronyEnemy {
        public Brawler() {
            super('B');
            setName("Brawler");
            addCondition(new RowdyCondition());
        }

        @Override
        public int getDamage() {
            return MyRandom.randInt(1, 2);
        }
    }
}
