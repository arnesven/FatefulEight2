package model.mainstory.vikings;

import model.Model;
import model.characters.GameCharacter;
import model.characters.PersonalityTrait;
import model.characters.appearance.CharacterAppearance;
import model.classes.Classes;
import model.combat.CombatAdvantage;
import model.combat.loot.CombatLoot;
import model.combat.loot.PersonCombatLoot;
import model.enemies.Enemy;
import model.enemies.FormerPartyMemberEnemy;
import model.items.Equipment;
import model.items.accessories.*;
import model.items.clothing.*;
import model.items.weapons.*;
import model.mainstory.GainSupportOfVikingsTask;
import model.map.WorldBuilder;
import model.map.WorldHex;
import model.races.Race;
import model.states.DailyEventState;
import model.states.GameState;
import model.states.TravelBySeaState;
import model.states.events.GeneralInteractionEvent;
import util.MyLists;
import util.MyRandom;
import view.combat.DungeonTheme;
import view.combat.GrassCombatTheme;
import view.combat.MansionTheme;
import view.subviews.PortraitSubView;

import java.util.ArrayList;
import java.util.List;

public class RaidSixthOrderMonastaryEvent extends DailyEventState {
    private final GainSupportOfVikingsTask task;

    public RaidSixthOrderMonastaryEvent(Model model, GainSupportOfVikingsTask vikingTask) {
        super(model);
        this.task = vikingTask;
    }

    @Override
    protected void doEvent(Model model) {
        setCurrentTerrainSubview(model);
        println("The Vikings gather aboard the Longboat. Their weapons clattering and armor scrambling on the deck.");
        showExplicitPortrait(model, task.getChieftainPortrait(), GainSupportOfVikingsTask.CHIEFTAIN);
        portraitSay("Vikings! Set sail for the Isle of Faith. Let's pay these rich monks a visit!");
        println("With wild cheers and hoots, the ship casts off from the dock and glides out on the river.");
        model.getLog().waitForAnimationToFinish();
        WorldHex destHex = model.getWorld().getHex(WorldBuilder.MONASTERY_POSITION);
        TravelBySeaState.travelBySea(model, destHex, this, TravelBySeaState.SHIP_AVATAR,
                "the Isle of Faith", true, true);
        setCurrentTerrainSubview(model);
        println("The longboat lands on the beach of the Isle of Faith. The vikings jump out of the longboat " +
                "with their swords and axes drawn.");
        leaderSay("Come on people!");
        println("The party members follow the vikings up the beach. The raiding party spot a group of monks " +
                "setting up a feast on the meadow in front of the monastary.");
        portraitSay("Come Vikings! Odiiiiin!");
        println("The vikings scream with blood lust as they rush into battle.");
        List<GameCharacter> vikingAllies = makeVikingAllies();
        runCombat(makeMonkEnemies(), new GrassCombatTheme(), false, CombatAdvantage.Party,
                vikingAllies);
        setCurrentTerrainSubview(model);
        println("The monks lay slain all around you.");
        model.getLog().waitForReturn();
        showExplicitPortrait(model, task.getChieftainPortrait(), GainSupportOfVikingsTask.CHIEFTAIN);
        portraitSay("Don't tarry here friend. The raid isn't over yet.");
        println("The vikings rush up to the monastary. By now the monks have raised the alarm and " +
                "barricaded themselves in the cloister.");
        portraitSay("Bash in the door!");
        println("The Vikings quickly bring up a driftwood log from the beach to use as a battering ram. " +
                "In no time at all the door to the cloister has been bashed in, and the monks flee in panic.");
        portraitSay("For glory! Odiiiiin!");
        vikingAllies = MyLists.filter(vikingAllies, gc -> !gc.isDead());
        runCombat(makeMonkEnemies(), new DungeonTheme(), false, CombatAdvantage.Neither,
                vikingAllies);
        setCurrentTerrainSubview(model);
        println("You help the vikings thoroughly ransack the monastary.");
        randomSayIfPersonality(PersonalityTrait.lawful, List.of(), "We commited a crime here. We will have to " +
                "live with the consequences.");
        randomSayIfPersonality(PersonalityTrait.benevolent, List.of(), "Our consciences will be forever burdened by this atrocity.");
        model.getParty().earnGold(300);
        model.getParty().addToFood(100);
        model.getParty().getInventory().addToIngredients(100);
        println("The party gains 300 gold, 100 food and 100 ingredients!");
        randomSayIfPersonality(PersonalityTrait.cold, List.of(), "They won't be needing it.");
        portraitSay("Ah, good haul! Now let's return to our home. Our loved ones will want to see " +
                "our spoils and share in our victory!");
        print("Preparing to travel back to the Viking Village, press enter to continue.");
        waitForReturn();
        destHex = model.getWorld().getHex(WorldBuilder.VIKING_VILLAGE_LOCATION);
        TravelBySeaState.travelBySea(model, destHex, this, TravelBySeaState.SHIP_AVATAR,
                "the Viking Village", true, true);
        setCurrentTerrainSubview(model);
        showExplicitPortrait(model, task.getChieftainPortrait(), GainSupportOfVikingsTask.CHIEFTAIN);
        portraitSay("There you are friend. Now you are truly one of us, and I will gladly lead my tribe in " +
                "your conquest against Bogdown.");
        leaderSay("Good, see you there!");
        task.setMonastaryRaided();
        GeneralInteractionEvent.addToNotoriety(model, this, 100);
    }

    private List<GameCharacter> makeVikingAllies() {
        List<GameCharacter> viks = new ArrayList<>();
        for (int i = 0; i < 8; ++i) {
            CharacterAppearance appearance = PortraitSubView.makeRandomPortrait(Classes.VIKING, Race.NORTHERN_HUMAN);
            Equipment eq = new Equipment();
            eq.setWeapon(MyRandom.sample(List.of(new Longsword(), new RaidersAxe(), new Broadsword(), new BattleAxe())));
            eq.setClothing(MyRandom.sample(List.of(new PlateMailArmor(), new ChainMail(), new Brigandine(), new LeatherArmor())));
            eq.setAccessory(MyRandom.sample(List.of(new Buckler(), new LargeShield(), new KiteShield(), new SpikedShield())));
            GameCharacter viking = new GameCharacter("Viking", "", appearance.getRace(), Classes.VIKING, appearance,
                    Classes.NO_OTHER_CLASSES, eq);
            viks.add(viking);
        }
        return viks;
    }

    private List<Enemy> makeMonkEnemies() {
        List<Enemy> enms = new ArrayList<>();
        for (int i = MyRandom.randInt(5, 10); i > 0; --i) {
            enms.add(new MonkEnemy(makeMonk()));
        }
        return enms;
    }

    private static GameCharacter makeMonk() {
        CharacterAppearance appearance = PortraitSubView.makeRandomPortrait(Classes.PRI);
        Equipment eq = new Equipment();
        eq.setWeapon(MyRandom.sample(List.of(new Dirk(), new Club(), new LongStaff(), new GrandStaff())));
        eq.setClothing(MyRandom.sample(List.of(new PilgrimsCloak(), new MagesRobes(), new StuddedTunic(), new JustClothes())));
        eq.setAccessory(MyRandom.sample(List.of(new GoldRing(), new SapphireRing(), new TopazRing(), new EmeraldRing(),
                    new ShinyAmulet(), new AnkhPendant())));
        GameCharacter monk = new GameCharacter("Sixth Monk", "", appearance.getRace(), Classes.PRI, appearance,
                Classes.NO_OTHER_CLASSES, eq);
        return monk;
    }

    private class MonkEnemy extends FormerPartyMemberEnemy {
        public MonkEnemy(GameCharacter gameCharacter) {
            super(gameCharacter);
        }

        @Override
        public CombatLoot getLoot(Model model) {
            return new PersonCombatLoot(model);
        }
    }
}
