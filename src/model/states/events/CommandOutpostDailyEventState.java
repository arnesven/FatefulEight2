package model.states.events;

import model.Model;
import model.Party;
import model.characters.GameCharacter;
import model.characters.appearance.CharacterAppearance;
import model.classes.Classes;
import model.classes.Skill;
import model.classes.SkillCheckResult;
import model.items.Item;
import model.items.accessories.*;
import model.items.clothing.*;
import model.items.weapons.*;
import model.journal.JournalEntry;
import model.map.CastleLocation;
import model.map.wars.KingdomWar;
import model.states.DailyEventState;
import model.states.battle.BattleState;
import model.states.battle.BattleUnit;
import model.tasks.BattleDestinationTask;
import util.MyLists;
import util.MyRandom;
import view.ChooseBattleReinforcementsView;
import view.ChooseStartingItemView;
import view.MyColors;
import view.subviews.PortraitSubView;

import java.util.*;

public class CommandOutpostDailyEventState extends DailyEventState {

    private final KingdomWar war;
    private final boolean givenByAggressor;
    private final BattleDestinationTask task;

    public CommandOutpostDailyEventState(Model model, KingdomWar war, boolean givenByAggressor,
                                         BattleDestinationTask battleDestinationTask) {
        super(model);
        this.war = war;
        this.givenByAggressor = givenByAggressor;
        this.task = battleDestinationTask;
    }

    @Override
    protected void doEvent(Model model) {
        CharacterAppearance fieldGeneralAppearance = war.getGeneralAppearance();
        println("You wander into the army camp. It is a large camp with tents, wagons and training dummies everywhere. " +
                "There are plenty of soldiers about along with armor, weapons, clothes, provisions and everything " +
                "else an army needs to sustain itself. There's a larger, more colorful tent up on a little hill. You " +
                "assume this is the command tent. You walk up to it and step inside. A muscular " +
                fieldGeneralAppearance.getRace().getName().toLowerCase() + " greets you as you enter.");
        showExplicitPortrait(model, fieldGeneralAppearance, "Field General");
        if (war.isInitialBattle()) {
            portraitSay("Hello there! You must be the ones the Commander mentioned in his message. " +
                    "I've been expecting you.");
        } else {
            portraitSay("There you are friend. I was beginning to worry you weren't coming.");
        }
        leaderSay("Are you preparing for battle?");
        portraitSay("Yes. Our scouts have spotted the enemy's units. We're just about to move out and deploy in the field. " +
                "You got here just in time!");
        leaderSay("Good. What can we help with?");
        portraitSay("Well, you can go join any unit you like. Or you can stay with me and direct the battle. " +
                "Which would you prefer?");
        int choice = multipleOptionArrowMenu(model, 24, 26, List.of("Join a unit", "Direct the battle"));
        boolean victorious;
        if (choice == 0) {
            leaderSay("We can handle ourselves. Just show us where the enemy is.");
            portraitSay("I admire your fervor. Just head on down the hill and you'll soon see the ranks. " +
                    "Just find any unit and report to the lieutenant.");
            leaderSay("See you on the other side general.");
            List<BattleUnit> enemies = givenByAggressor ? war.getDefenderUnits() : war.getAggressorUnits();
            List<BattleUnit> allies = givenByAggressor ? war.getAggressorUnits() : war.getDefenderUnits();
            FightInUnitDuringBattleEvent fightEvent = new FightInUnitDuringBattleEvent(model,
                    givenByAggressor ? war.getAggressor() : war.getDefender(),
                    enemies);
            model.getLog().waitForAnimationToFinish();
            removePortraitSubView(model);
            fightEvent.run(model);
            if (model.getParty().isWipedOut()) {
                return;
            }
            decimateRandomly(enemies, 0);
            decimateRandomly(allies, +1);
            victorious = fightEvent.isVictorious();
        } else {
            leaderSay("I think we'll stay with you. I'm sure we'll have some tactical insights we could share.");
            portraitSay("All right! Let's go give the enemy a taste of our zeal!");
            BattleState battle = new BattleState(model, war, givenByAggressor);
            battle.run(model);
            victorious = battle.wasVictorious();
        }
        setCurrentTerrainSubview(model);
        showExplicitPortrait(model, fieldGeneralAppearance, "Field General");

        task.setCompleted(true);

        boolean warIsOver;
        if (victorious) {
            warIsOver = war.advance(givenByAggressor);
            if (warIsOver) {
                portraitSay("Victory! Victory is ours friend!");
                leaderSay("What a battle... the enemy... they have surrendered...");
                String loser = givenByAggressor ? war.getDefender() : war.getAggressor();
                portraitSay("Yes the " + CastleLocation.placeNameToKingdom(loser) + " have sued for peace and " +
                        "our lord is currently negotiating the spoils of the war.");
                leaderSay("So that's it. The war is over?");
                portraitSay("Justice has prevailed my friend. Now we can return to our homes, sleep soundly in " +
                        "our beds knowing that our safety has been secured. Now we shall have peace.");
                leaderSay("I'm happy to hear it. We are weary of war general.");
                portraitSay("So am I. Farewell friend, until we meet again.");
                leaderSay("Goodbye general.");
                println("Your party gains 1 Reputation and each party member gains 100 XP!");
                model.getParty().addToReputation(1);
                MyLists.forEach(model.getParty().getPartyMembers(),
                        (GameCharacter gc) -> model.getParty().giveXP(model, gc, 100));
            } else {
                portraitSay("What a glorious day, victory is ours!");
                leaderSay("Yes. The troops fought well.");
                portraitSay("Don't discount yourself. Without your effort, defeat would have surely found us.");
                leaderSay("Perhaps. Now it found the enemy instead. What now general?");
                portraitSay("We must advance our positions. Our scouts have already discovered " +
                        "the location of the enemy's rear camp. But it will take some time for our troops to reach it. " +
                        "I'll mark it on your map for now.");
                task.setSuccess(true);

                giveNewTask(model);

                leaderSay("Splendid. We'll meet you on the front line then.");
                portraitSay("Yes. But before you leave. I was wondering if you could offer some advice.");
                leaderSay("About what?");
                portraitSay("We may be able to bring up some reinforcements. Muster some more troops even. " +
                        "But there is limited time and resources. I would love to hear your input on what kind of " +
                        "units we should prioritize.");
                model.getLog().waitForAnimationToFinish();
                List<BattleUnit> unitsToReinforce = givenByAggressor ? war.getAggressorUnits() : war.getDefenderUnits();
                ChooseBattleReinforcementsView reinforcementsView = new ChooseBattleReinforcementsView(model,
                        unitsToReinforce, unitsToReinforce.get(0).getColor());
                print(" ");
                model.transitionToDialog(reinforcementsView);
                model.getLog().waitForAnimationToFinish();

                generalReinforce(war.getAggressorUnits(), reinforcementsView.getPriorityMap());
                List<BattleUnit> otherUnits = givenByAggressor ? war.getDefenderUnits() : war.getAggressorUnits();
                generalReinforce(otherUnits, null);

                leaderSay("... That's my view of the situation.");
                portraitSay("Thank you. Your insights are much appreciated. Now, then... Until we meet again friend.");
                leaderSay("So long general.");
                println("Each party member gains 25 XP!");
                MyLists.forEach(model.getParty().getPartyMembers(),
                        (GameCharacter gc) -> model.getParty().giveXP(model, gc, 25));
            }
            print("Would you like to search through the battle field for any salvageable equipment? (Y/N) ");
            if (yesNoInput()) {
                lootBattlefield(model);
            }
        } else {
            warIsOver = war.advance(!givenByAggressor);
            if (warIsOver) {
                String winner = givenByAggressor ? war.getDefender() : war.getAggressor();
                String loser = givenByAggressor ? war.getAggressor() : war.getDefender();
                println("The battle has clearly been lost and the " + CastleLocation.placeNameShort(winner) +
                        " forces have forced the " + CastleLocation.placeNameToKingdom(loser) + " to sue for peace. " +
                        "When the negotiations have concluded you meet with the field general once more.");
                portraitSay("Well friend. We lost. At least now the war will be over and we are lucky to have " +
                        "escaped with our lives.");
                leaderSay("I'm sorry. It was a tough battle.");
                portraitSay("It was a tough war. We fought well, but the enemy gained the upper hand. Sometimes " +
                        "there is just no path to victory, no matter much you desire it.");
                leaderSay("What will you do now?");
                portraitSay("I shall return to my farm. My " + (MyRandom.flipCoin() ? "wife" : "husband") + " is waiting for me. " +
                        "What will you do?");
                leaderSay("I'm not sure... now that the war is over. Well, it has left me a empty.");
                portraitSay("Perhaps another adventure awaits for you out there somewhere?");
                leaderSay("Perhaps. Farewell general.");
                println("Each party member gains 50 XP!");
                MyLists.forEach(model.getParty().getPartyMembers(),
                        (GameCharacter gc) -> model.getParty().giveXP(model, gc, 50));
                print("Would you like to search through the battle field for any salvageable equipment? (Y/N) ");
                if (yesNoInput()) {
                    lootBattlefield(model);
                }
            } else {
                println("In the hectic aftermath of the lost battle, you see the general barking order at " +
                        "the confused units, or what remains of them.");
                portraitSay("Retreat! Retreat I say!");
                leaderSay("General... we did our best but...");
                portraitSay("Fortune was not with us today, needless to say. We must make haste to our " +
                        "rear positions before the enemy overruns us.");
                leaderSay("Of course. Where is that?");
                portraitSay("I'll mark it on your map.");
                task.setCompleted(false);
                task.setSuccess(false);
                giveNewTask(model);
                portraitSay("Now make haste friend or we shall surely meet the same fate as many of our fallen comrades.");
                leaderSay("Yes general. See you on the other side.");
                generalReinforce(war.getAggressorUnits(), null);
                generalReinforce(war.getDefenderUnits(), null);
            }
        }

        if (warIsOver) {
            model.getWarHandler().endWar(war);
        }
    }

    private void decimateRandomly(List<BattleUnit> units, int rollBonus) {
        for (BattleUnit bu : units) {
            int dieRoll = MyRandom.rollD6() + rollBonus;
            if (dieRoll < 3) {
                bu.setCount(0);
            } else {
                bu.setCount(Math.max(1, bu.getCount() / (8 - dieRoll)));
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
        return MyRandom.sample(List.of(new RustyChestPlate(), new RustyRingMail(), new Spear(),
                new LongBow(), new Longsword(), new Broadsword(), new LargeShield(), new SkullCap(),
                new LeatherCap()));
    }

    private void giveNewTask(Model model) {
        CastleLocation loc = (CastleLocation) model.getWorld().getUrbanLocationByPlaceName(
                givenByAggressor ? war.getAggressor() : war.getDefender());
        model.getParty().addDestinationTask(new BattleDestinationTask(war, loc));
        JournalEntry.printJournalUpdateMessage(model);
    }

    private void generalReinforce(List<BattleUnit> units, Map<String, Integer> priorityMap) {
        System.out.println("Reinforcing. Before:");
        printUnits(units);
        List<BattleUnit> oldUnits = collapse(units);
        List<BattleUnit> newUnits = new ArrayList<>();
        for (BattleUnit bu : oldUnits) {
            int multiplier = 2;
            if (priorityMap != null) {
                multiplier = priorityMap.get(bu.getName()) + 1;
            }

            int reinforceTotal = bu.getCount() + bu.getReinforceCount(multiplier);
            if (reinforceTotal <= bu.maximumUnitSize()) {
                bu.setCount(reinforceTotal);
                newUnits.add(bu);
            } else {
                newUnits.add(bu.createNew((int) Math.ceil(reinforceTotal / 2.0)));
                newUnits.add(bu.createNew(reinforceTotal / 2));
            }
        }

        units.clear();
        units.addAll(newUnits);

        System.out.println("         After:");
        printUnits(units);
        System.out.println(" ");
    }

    private void printUnits(List<BattleUnit> units) {
        for (BattleUnit unit : units) {
            System.out.printf("%-26s %3d%n", unit.getQualifiedName(), unit.getCount());
        }
    }

    private List<BattleUnit> collapse(List<BattleUnit> units) {
        Map<String, BattleUnit> map = new HashMap<>();
        for (BattleUnit bu : units) {
            if (map.containsKey(bu.getName())) {
                BattleUnit prev = map.get(bu.getName());
                map.get(bu.getName()).setCount(prev.getCount() + bu.getCount());
            } else {
                map.put(bu.getName(), bu);
            }
        }
        return new ArrayList<>(map.values());
    }
}
