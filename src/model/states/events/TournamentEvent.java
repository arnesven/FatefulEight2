package model.states.events;

import model.ItemDeck;
import model.Model;
import model.characters.GameCharacter;
import model.characters.appearance.AdvancedAppearance;
import model.characters.appearance.CharacterAppearance;
import model.classes.CharacterClass;
import model.classes.Classes;
import model.classes.PaladinClass;
import model.enemies.TournamentEnemy;
import model.items.Equipment;
import model.items.RedKnightsHelm;
import model.items.accessories.*;
import model.items.clothing.*;
import model.items.weapons.*;
import model.map.CastleLocation;
import model.races.Race;
import model.states.DailyEventState;
import model.states.RecruitState;
import model.states.TravelBySeaState;
import util.MyPair;
import util.MyRandom;
import view.subviews.PortraitSubView;
import view.subviews.SubView;
import view.subviews.TournamentSubView;

import java.util.*;

public class TournamentEvent extends DailyEventState {
    protected static final int ENTRY_FEE = 25;
    private static final Map<CharacterClass, List<NameAndGender>> NAMES_FOR_CLASSES = makeFighterNames();
    private static final List<Clothing> HEAVY_ARMORS = List.of(
             new DragonArmor(), new ScaleArmor(), new BreastPlate(), new FullPlateArmor(),
             new ChainMail(), new ChainMail(), new ChainMail());
    private static final List<Clothing> LIGHT_ARMORS = List.of(new StuddedTunic(), new OutlawArmor(),
            new StuddedJerkin(), new RingMail(), new LeatherArmor(), new LeatherArmor());
    private static final List<Weapon> ONE_HANDED_WEAPONS = List.of(
            new ShortSpear(), new Choppa(), new RaidersAxe(),
            new BattleAxe(), new Scepter(), new MorningStar(), new Flail(), new TripleFlail(), new Mace(),
            new Kukri(), new Scimitar(), new Falchion(), new Wakizashi(), new Longsword(), new Broadsword());
    private static final List<Weapon> TWO_HANDED_WEAPONS = List.of(new Spear(), new Trident(), new Glaive(), new Halberd(),
            new Pike(), new BecDeCorbin(), new DoubleAxe(), new GreatAxe(), new TwinHatchets(), new GrandMaul(), new Katana(),
            new TwoHandedSword(), new BastardSword(), new DaiKatana(), new Zweihander());

    private final CastleLocation castle;
    private static final CharacterAppearance official = PortraitSubView.makeRandomPortrait(Classes.OFFICIAL, Race.ALL);
    private static final CharacterAppearance sponsor = PortraitSubView.makeRandomPortrait(Classes.THF, Race.ALL);
    private static final CharacterAppearance announcer = PortraitSubView.makeRandomPortrait(Classes.None, Race.ALL);

    public TournamentEvent(Model model, CastleLocation castleLocation) {
        super(model);
        this.castle = castleLocation;
    }

    @Override
    protected void doEvent(Model model) {
        print("The " + castle.getLordTitle() + " is hosting a melee tournament today. " +
                "Do you wish to attend? (Y/N) ");
        if (!yesNoInput()) {
            return;
        }
        println("Outside the castle walls many tents and pavilions have been erected. and there, " +
                "in the middle, is the fighting pit.");
        println("As you wander around you see knights, fair ladies, noblemen, merchants and commoners " +
                "all bustling about and getting ready for the tournament. Some people are lining up at a little booth " +
                "where a small gentleman in fancy clothing is accepting coins and writing things down in big ledgers.");
        model.getLog().waitForAnimationToFinish();
        showOfficial();
        portraitSay("Yes, we're still accepting participants. Are you here to fight in the tournament?");
        leaderSay("Perhaps. What are the parameters?");
        portraitSay("The entry fee is 25 gold. Eight champions will enter the tournament. There will be one-on-one " +
                "face offs until only one remains. Fights continue until one combatant yields, or to the death! The winner receives " +
                "100 gold and the " + castle.getLordTitle() + " blessing - which is worth more than any bag of gold mind you.");
        portraitSay("But all of you can't fight. We only have room for one more. Are you still interested?");
        boolean sponsored = false;
        if (model.getParty().getGold() < ENTRY_FEE) {
            println("You are about to reply that you can't afford it when a shady fellow in a hood steps up behind you.");
            showSponsor();
            portraitSay("You look like your the capable sort, I can front the money for the fight. But I'll expect half the " +
                    "prize money if you come out on top. What do you say?");
            print("Do you accept the strangers sponsorship? (Y/N) ");
            sponsored = true;
        } else {
            print("Will you enter one of your party members into the tournament (Y), or will you " +
                    "remain on the sidelines and place bets on the combatants (N)? ");
        }
        if (yesNoInput()) {
            new ParticipateInTournamentEvent(model, sponsored, castle).doEvent(model);
        } else {
            new BetOnTournamentEvent(model, castle).doEvent(model);
        }
    }

    protected void runDetailedNPCFight(Model model, GameCharacter fighterA, GameCharacter fighterB) {
        if (fighterA.getSpeed() < fighterB.getSpeed()) {
            runDetailedNPCFight(model, fighterB, fighterA);
            return;
        }
        println("Two fighters enter the fighting pit.");
        print("The first one is ");
        detailDescribe(fighterA);
        print("The second one is ");
        detailDescribe(fighterB);
        new NPCCombatEvent(model, fighterA, fighterB).doTheEvent(model);
        setCurrentTerrainSubview(model);
    }

    private void detailDescribe(GameCharacter fighter) {
        if (fighter.getCharClass() == Classes.None) {
            print("not of any particular class");
        } else if (fighter.getCharClass() == Classes.AMZ || fighter.getCharClass() == Classes.ASN) {
            print("an " + fighter.getCharClass().getFullName().toLowerCase());
        } else {
            print("a " + fighter.getCharClass().getFullName().toLowerCase());
        }
        print(" equipped with " + fighter.getEquipment().getWeapon().getName().toLowerCase());
        if (fighter.getEquipment().getAccessory() instanceof ShieldItem) {
            print(" and a " + fighter.getEquipment().getAccessory().getName().toLowerCase());
        }
        println(".");
        print(heOrSheCap(fighter.getGender()) + " is dressed in " +
                fighter.getEquipment().getClothing().getName().toLowerCase());
        if (!(fighter.getEquipment().getAccessory() instanceof ShieldItem)) {
            print(" and " + fighter.getEquipment().getAccessory().getName().toLowerCase());
        }
        println(".");
    }

    protected void showOfficial() {
        showExplicitPortrait(getModel(), official, "Official");
    }

    protected void showAnnouncer() {
        showExplicitPortrait(getModel(), announcer, "Announcer");
    }

    protected void showSponsor() {
        showExplicitPortrait(getModel(), sponsor, "Mysterious Stranger");
    }

    protected List<GameCharacter> makeFighters(Model model, int number) {
        List<GameCharacter> result = new ArrayList<>();
        List<CharacterClass> fighterClasses = new ArrayList<>(NAMES_FOR_CLASSES.keySet());
        Collections.shuffle(fighterClasses);
        for (int i = 0; i < number; ++i) {

            CharacterClass selectedClass = fighterClasses.get(0);
            fighterClasses.remove(0);

            NameAndGender name = MyRandom.sample(NAMES_FOR_CLASSES.get(selectedClass));
            Race race = Race.allRaces[MyRandom.randInt(Race.allRaces.length)];
            AdvancedAppearance app = PortraitSubView.makeRandomPortrait(selectedClass, race, name.gender);
            GameCharacter fighter = new GameCharacter(name.firstName, name.lastName, race, selectedClass, app,
                    new CharacterClass[]{Classes.None, Classes.None, Classes.None, Classes.None},
                    generateEquipmentFor(selectedClass));
            fighter.setLevel((int)Math.ceil(RecruitState.calculateAverageLevel(model)));
            result.add(fighter);
        }
        return result;
    }

    private static Map<CharacterClass, List<NameAndGender>> makeFighterNames() {
        List.of(Classes.PAL, Classes.CAP, Classes.BBN, Classes.BKN,
                Classes.FOR, Classes.AMZ, Classes.MIN, Classes.ASN, Classes.None, Classes.RED_KNIGHT);
        Map<CharacterClass, List<NameAndGender>> result = new HashMap<>();
        result.put(Classes.PAL, List.of(
                new NameAndGender("Horace", "Hightower", false),
                new NameAndGender("Miranda", "Edelweiss", true),
                new NameAndGender("Derric", "Eastwood", false),
                new NameAndGender("White", "Knight", true)));
        result.put(Classes.CAP, List.of(
                new NameAndGender("Felix", "Azure", false),
                new NameAndGender("Helga", "Hardwater", true),
                new NameAndGender("Gray", "Knight", false)));
        result.put(Classes.BBN, List.of(
                new NameAndGender("Igor", "Stormfist", false),
                new NameAndGender("Olga", "Ouagadoo", true),
                new NameAndGender("Belthor", "Oz", false)));
        result.put(Classes.BKN, List.of(new NameAndGender("Black Knight", "", false)));
        result.put(Classes.FOR, List.of(
                new NameAndGender("Teddy", "Ostermuller", false),
                new NameAndGender("Veronica", "Rockfort", true)));
        result.put(Classes.AMZ, List.of(new NameAndGender("Amazon", "", true)));
        result.put(Classes.MIN, List.of(
                new NameAndGender("Engelbrecht", "Anderson", false),
                new NameAndGender("Maria", "Samuelsen", true)));
        result.put(Classes.ASN, List.of(
                new NameAndGender("Masked Fighter", "", true)));
        result.put(Classes.None, List.of(new NameAndGender("The Fool", "", false)));
        result.put(Classes.RED_KNIGHT, List.of(new NameAndGender("Red", "Knight", false)));
        return result;
    }


    private Equipment generateEquipmentFor(CharacterClass selectedClass) {
        if (selectedClass == Classes.PAL) {
            if (MyRandom.flipCoin()) {
                return new Equipment(randomTwoHandedWeapon(), randomArmor(true), new SteelGauntlets());
            }
            return new Equipment(randomOneHandedWeapon(), randomArmor(true), new HeraldicShield());
        }
        if (selectedClass == Classes.CAP) {
            if (MyRandom.flipCoin()) {
                return new Equipment(randomOneHandedWeapon(), randomArmor(true), new TowerShield());
            }
            return new Equipment(randomTwoHandedWeapon(), randomArmor(true), new SkullCap());
        }
        if (selectedClass == Classes.BBN) {
            return new Equipment(randomTwoHandedWeapon(), new FurArmor(), new GreatHelm());
        }
        if (selectedClass == Classes.BKN) {
            return new Equipment(randomOneHandedWeapon(), randomArmor(true), new PlatedBoots());
        }
        if (selectedClass == Classes.FOR) {
            return new Equipment(MyRandom.sample(ItemDeck.allAxes()), randomArmor(false), new ChainGloves());
        }
        if (selectedClass == Classes.AMZ) {
            return new Equipment(MyRandom.sample(ItemDeck.allSpears()), new FurArmor(), new WolfHead());
        }
        if (selectedClass == Classes.MIN) {
            return new Equipment(new Pickaxe(), randomArmor(true), new LargeShield());
        }
        if (selectedClass == Classes.ASN) {
            Weapon blade = MyRandom.sample(ItemDeck.allBlades());
            if (blade.isTwoHanded()) {
                return new Equipment(blade, randomArmor(false), new LeatherCap());
            }
            return new Equipment(blade, randomArmor(false), new SpikedShield());
        }
        if (selectedClass == Classes.None) {
            return new Equipment(new ShortSword(), new FancyJerkin(), new Buckler());
        }
        if (selectedClass == Classes.RED_KNIGHT) {
            return new Equipment(new BastardSword(), new RedKnightsArmor(), new RedKnightsHelm());
        }
        throw new IllegalArgumentException("Illegal class argument, class " + selectedClass.getShortName());
    }

    private Weapon randomTwoHandedWeapon() {
        return MyRandom.sample(TWO_HANDED_WEAPONS);
    }

    private Weapon randomOneHandedWeapon() {
        return MyRandom.sample(ONE_HANDED_WEAPONS);
    }

    private Clothing randomArmor(boolean heavy) {
        if (heavy) {
            return MyRandom.sample(HEAVY_ARMORS);
        }
        return MyRandom.sample(LIGHT_ARMORS);
    }

    private static class NameAndGender {
        public final String firstName;
        public final String lastName;
        public final boolean gender;
        public NameAndGender(String first, String last, boolean female) {
            this.firstName = first;
            this.lastName = last;
            this.gender = female;
        }
    }
}
