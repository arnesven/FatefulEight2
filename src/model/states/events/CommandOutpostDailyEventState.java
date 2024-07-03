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
import view.subviews.PortraitSubView;

import java.util.ArrayList;
import java.util.List;

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
        int choice = multipleOptionArrowMenu(model, 26, 26, List.of("Join a unit", "Direct the battle",
                "Skip the battle"));
        boolean victorious;
        if (choice == 2) { // TODO: Remove this cheat
            print("Do you want to win(Y) or lose the battle(N)? ");
            victorious = yesNoInput();
        } else {
            // TODO: Implement joining a unit
            leaderSay("I think we'll stay with you. I'm sure we'll have some tactical insights we could share.");
            portraitSay("All right! Let's go give the enemy a taste of our zeal!");
            BattleState battle = new BattleState(model, war, givenByAggressor);
            battle.run(model);
            setCurrentTerrainSubview(model);
            showExplicitPortrait(model, fieldGeneralAppearance, "Field General");
            victorious = battle.wasVictorious();
        }

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
                println("Each party member gains 100 XP!");
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
                println("We may be able to bring up some reinforcements. Muster some more troops even. " +
                        "But there is limited time and resources. I would love to hear your input on what kind of " +
                        "units we should focus on.");
                // TODO: Reinforcements dialog instead of generalReinforce for one side.
                generalReinforce(war.getAggressorUnits());
                generalReinforce(war.getDefenderUnits());

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
                generalReinforce(war.getAggressorUnits());
                generalReinforce(war.getDefenderUnits());
            }
        }

        if (warIsOver) {
            model.getWarHandler().endWar(war);
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

    private void generalReinforce(List<BattleUnit> units) {
        List<BattleUnit> newUnits = new ArrayList<>();
        for (BattleUnit bu : units) {
            int reinforceTotal = bu.getCount() + bu.getReinforceCount();
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
    }
}
