package model.states.events;

import model.Model;
import model.characters.GameCharacter;
import model.characters.appearance.CharacterAppearance;
import model.classes.CharacterClass;
import model.classes.Classes;
import model.classes.Skill;
import model.classes.SkillCheckResult;
import model.items.Item;
import model.items.accessories.*;
import model.items.clothing.*;
import model.items.weapons.*;
import model.map.CastleLocation;
import model.map.WorldBuilder;
import model.map.WorldHex;
import model.map.wars.KingdomWar;
import model.map.wars.PitchedBattleSite;
import model.states.DailyEventState;
import model.states.battle.*;
import util.MyLists;
import util.MyRandom;
import view.ChooseBattleReinforcementsView;
import view.MyColors;
import view.subviews.PortraitSubView;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class OrcsBattleEvent extends DailyEventState {
    public OrcsBattleEvent(Model model) {
        super(model);
    }

    @Override
    protected void doEvent(Model model) {
        println("The party comes upon a battle camp.");
        if (WorldBuilder.isInExtendedRegion(model.getParty().getPosition())) {
            println("The scene is pretty busy and nobody seems to take not of you entering the camp.");
            fightBattle(model);

        } else {
            println("There are guards patrolling the camp and the party must appear like they belong.");
            boolean success = model.getParty().doCollectiveSkillCheck(model, this, Skill.Entertain, 5);
            if (success) {
                println("You successfully pose as soldiers of the kingdom.");
                // TODO: Smith, Quartermaster, Free Lodging.
            } else {
                showRandomPortrait(model, Classes.CAP, "Guard");
                portraitSay("Hey you. You don't belong here. Shove off!");
                leaderSay("Okay okay. No need to be so gruff. We'll just make our camp somewhere else then.");
            }
        }
    }

    private void fightBattle(Model model) {
        CharacterAppearance fieldGeneralAppearance = PortraitSubView.makeRandomPortrait(Classes.PAL);
        CommandOutpostDailyEventState.intro(this, fieldGeneralAppearance.getRace());
        showExplicitPortrait(model, fieldGeneralAppearance, "Field General");
        portraitSay("Hello there. Are you the new recruits? Are are you the military tacticians my lord has sent me?");
        leaderSay("Uhm. Depends. What's going on here?");
        portraitSay("Those damnable orcs, and their lesser minions have gathered an army " +
                "which is threatening the realm. But this can't be news to you. Now please, I'm very busy. What is your business here?");
        boolean victorious;
        CastleLocation castle = findClosestCastle(model);
        KingdomWar war = makeWar(model, castle);
        int choice = multipleOptionArrowMenu(model, 24, 26, List.of("Join a unit", "Direct the battle", "Don't get involved"));
        if (choice == 0) {
            leaderSay("We're the new recruits. Just point us in the direction of this orcish scum and victory shall soon be ours.");
            portraitSay("I admire your fervor. Just head on down the hill and you'll soon see the ranks. " +
                    "Just find any unit and report to the lieutenant.");
            leaderSay("See you on the other side general.");
            FightInUnitDuringBattleEvent fightEvent = new FightInUnitDuringBattleEvent(model, castle.getPlaceName(),
                    war.getAggressorUnits());
            model.getLog().waitForAnimationToFinish();
            removePortraitSubView(model);
            fightEvent.run(model);
            if (model.getParty().isWipedOut()) {
                return;
            }
            victorious = fightEvent.isVictorious();
        } else if (choice == 1) {
            leaderSay("We're the tacticians you mentioned. We'll stay with you and help you lead the battle.");
            portraitSay("All right! Let's go give the enemy a taste of our zeal!");
            BattleState battle = new BattleState(model, war, false);
            battle.run(model);
            victorious = battle.wasVictorious();
        } else {
            leaderSay("Actually, we're just passing through.");
            portraitSay("Then what on gods holy earth are you wasting my time for? GET OUT!");
            leaderSay("Yes sir!");
            return;
        }

        if (victorious) {
            portraitSay("What a glorious day, victory is ours!");
            leaderSay("Yes. The troops fought well.");
            portraitSay("Don't discount yourself. Without your effort, defeat would have surely found us.");
            leaderSay("Perhaps. Now it found the enemy instead. What now general?");
            portraitSay("We have driven the invaders off, but we must remain vigilant. " +
                    "These orcs and goblin scum may rise yet again to strike us.");

            leaderSay("And if they do, we'll be ready.");
            portraitSay("I'm glad we can count on you.");
            leaderSay("So long general.");
            println("Each party member gains 25 XP!");
            MyLists.forEach(model.getParty().getPartyMembers(),
                    (GameCharacter gc) -> model.getParty().giveXP(model, gc, 25));
            print("Would you like to search through the battle field for any salvageable equipment? (Y/N) ");
            if (yesNoInput()) {
                lootBattlefield(model);
            }
        }
    }

    private void lootBattlefield(Model model) {
        for (GameCharacter gc : model.getParty().getPartyMembers()) {
            SkillCheckResult result = gc.testSkill(model, Skill.Search);
            if (result.getModifiedRoll() < 5) {
                println(gc.getName() + " finds nothing.");
            } else if (result.getModifiedRoll() < 7) {
                int materials = MyRandom.randInt(2, 5);
                model.getParty().getInventory().addToMaterials(materials);
                println(gc.getName() + " finds " + materials + " materials.");
            } else if (result.getModifiedRoll() < 10) {
                Item equipment = generateCombatEquipment();
                println(gc.getName() + " finds a " + equipment.getName() + ".");
            } else {
                Item equipment = generateNiceCombatEquipment();
                println(gc.getName() + " finds " + equipment.getName() + "!");
                partyMemberSay(gc, MyRandom.sample(List.of("Lucky!", "Wow. Nice!", "A good find.",
                        "Better not let this go to waste.", "This belongs to me now.",
                        "Rather good quality item this.", "Jackpot!")));

            }
        }
    }

    private Item generateNiceCombatEquipment() {
        return MyRandom.sample(List.of(new Pike(), new Katana(), new Estoc(), new Halberd(),
                new GreatAxe(), new RepeatingCrossbow(), new FullPlateArmor(), new Brigandine(),
                new LeatherArmor(), new GreatHelm()));
    }

    private Item generateCombatEquipment() {
        return MyRandom.sample(List.of(new RustyChestPlate(), new RustyRingMail(), new ShortSpear(),
                new ShortBow(), new Choppa(), new Scimitar(), new Buckler(), new SkullCap(),
                new LeatherCap()));
    }

    private KingdomWar makeWar(Model model, CastleLocation castle) {
        KingdomWar war = new KingdomWar("Green Horde", castle.getPlaceName(), MyColors.RED, castle.getCastleColor(),
                new ArrayList<>(), new PitchedBattleSite(model.getParty().getPosition(), MyColors.GREEN, "Battle with the orcs"),
                new ArrayList<>());
        List<BattleUnit> result = war.getAggressorUnits();
        result.clear();
        for (int i = 0; i < 5; ++i) {
            int roll = MyRandom.rollD6();
            if (roll <= 2) {
                result.add(new OrcWarriorUnit(MyRandom.randInt(12, 20)));
            } else if (roll <= 4) {
                result.add(new GoblinSpearmanUnit(MyRandom.randInt(18, 26)));
            } else {
                result.add(new GoblinSpearmanUnit(MyRandom.randInt(18, 26)));
            }
        }
        if (MyRandom.flipCoin()) {
            result.add(new GoblinWolfRiderUnit(MyRandom.randInt(6, 12)));
        } else {
            result.add(new OrcBoarRiderUnit(MyRandom.randInt(5, 9)));
        }
        return war;
    }

    private CastleLocation findClosestCastle(Model model) {
        model.getWorld().dijkstrasByLand(model.getParty().getPosition());
        List<Point> path = model.getWorld().generalShortestPath(0, worldHex -> worldHex.getLocation() instanceof CastleLocation);
        WorldHex hex = model.getWorld().getHex(path.get(path.size()-1));
        return (CastleLocation) hex.getLocation();
    }

    @Override
    public GuideData getGuideData() {
        return new GuideData("Go to battle camp",
                "I saw a battle camp over in that direction, lot's of soldiers. " +
                "It looked like they were getting ready for a battle or something.");
    }
}
