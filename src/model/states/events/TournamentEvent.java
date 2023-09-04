package model.states.events;

import model.items.ItemDeck;
import model.Model;
import model.actions.Loan;
import model.characters.GameCharacter;
import model.characters.appearance.AdvancedAppearance;
import model.characters.appearance.CharacterAppearance;
import model.classes.CharacterClass;
import model.classes.Classes;
import model.enemies.Enemy;
import model.enemies.TournamentEnemy;
import model.items.Equipment;
import model.items.RedKnightsHelm;
import model.items.accessories.*;
import model.items.clothing.*;
import model.items.weapons.*;
import model.map.CastleLocation;
import model.races.Race;
import model.states.DailyEventState;
import model.states.GameState;
import util.MyRandom;
import view.subviews.PortraitSubView;

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
    private static final List<String> ADJECTIVES =  List.of("strong", "mysterious", "rugged",
            "tough", "hardened", "capable", "powerful", "skilled",
            "vicious", "fierce");

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
        println("Outside the castle walls many tents and pavilions have been erected. And there, " +
                "in the middle, is the fighting pit.");
        println("As you wander around you see knights, fair ladies, noblemen, merchants and commoners " +
                "all bustling about and getting ready for the tournament. Some people are lining up at a little booth " +
                "where a small gentleman in fancy clothing is accepting coins and writing things down in big ledgers.");
        model.getLog().waitForAnimationToFinish();
        showOfficial();
        portraitSay("Yes, we're still accepting participants. Are you here to fight in the tournament?");
        leaderSay("Perhaps. What are the parameters?");
        portraitSay("The entry fee is " + ENTRY_FEE + " gold. Eight champions will enter the tournament. There will be one-on-one " +
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
        removePortraitSubView(model);
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
            fighter.setLevel((int)Math.ceil(GameState.calculateAverageLevel(model)));
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
            return new Equipment(blade, randomArmor(false), new KiteShield());
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

    protected int calculateFighterStrength(GameCharacter fighter) {
        boolean hasHeavy = fighter.getEquipment().anyHeavy();
        return fighter.getHP() + (fighter.getSpeed() / 2) + (fighter.getAP() * (hasHeavy?1:2)) +
                (int)Math.ceil(fighter.calcAverageDamage());
    }

    protected GameCharacter performOneFight(Model model, GameCharacter fighterA, GameCharacter fighterB) {
        if (model.getParty().getPartyMembers().contains(fighterA)) {
            return runRealCombat(model, fighterA, fighterB);
        }
        if (model.getParty().getPartyMembers().contains(fighterB)) {
            return runRealCombat(model, fighterB, fighterA);
        }

        println("You sit down on one of the benches overlooking the fighting pit.");
        announcerStartOfCombat(fighterA, fighterB);
        print("Do you want to skip the details of the fight? (Y/N) ");
        if (yesNoInput()) {
            runAbstractedNPCFight(model, fighterA, fighterB);
            return announceOutcomeOfCombat(fighterA, fighterB, fighterA.getHP() <= 2);
        }
        runDetailedNPCFight(model, fighterA, fighterB);
        return announceOutcomeOfCombat(fighterA, fighterB, fighterA.getHP() <= 2);
    }

    private GameCharacter runRealCombat(Model model, GameCharacter partyMember, GameCharacter npcFighter) {
        println(partyMember.getName() + " enters the fighting pit...");
        announcerStartOfCombat(partyMember, npcFighter);
        for (GameCharacter gc : model.getParty().getPartyMembers()) {
            if (gc != partyMember) {
                model.getParty().benchPartyMembers(List.of(gc));
            }
        }
        GameCharacter oldLeader = model.getParty().getLeader();
        model.getParty().setLeader(partyMember);
        List<Enemy> enemies = List.of(new TournamentEnemy(npcFighter));
        runCombat(enemies);
        if (!oldLeader.isDead()) {
            model.getParty().setLeader(oldLeader);
        }
        npcFighter.addToHP(-(enemies.get(0).getMaxHP() - enemies.get(0).getHP()));
        setCurrentTerrainSubview(model);
        return announceOutcomeOfCombat(partyMember, npcFighter, haveFledCombat());
    }

    private void runAbstractedNPCFight(Model model, GameCharacter fighterA, GameCharacter fighterB) {
        println("The two combatants fight well, but in the end, one of them comes out on top.");
        int strA = calculateFighterStrength(fighterA);
        int strB = calculateFighterStrength(fighterB);
        if (MyRandom.randInt(0, strA + strB) < strB) {
            fighterB.addToHP(-(fighterB.getMaxHP()/2));
            fighterA.addToHP(-fighterA.getMaxHP());
            if (MyRandom.flipCoin()) {
                fighterA.addToHP(1);
            }
        } else {
            fighterA.addToHP(-(fighterB.getMaxHP()/2));
            fighterB.addToHP(-fighterB.getMaxHP());
            if (MyRandom.flipCoin()) {
                fighterB.addToHP(1);
            }
        }
    }

    private GameCharacter announceOutcomeOfCombat(GameCharacter fighterA, GameCharacter fighterB, boolean aYield) {
        announcerSay("Well ladies and gentlemen, it's over.");
        if (fighterA.isDead()) {
            announcerSay(fighterA.getName() + " has been slain, but " + heOrShe(fighterA.getGender()) +
                    " has perished with honor!");
            announcerSay(fighterB.getName() + " is the victor of the fight!");
            return fighterB;
        }
        if (fighterB.isDead()) {
            announcerSay(fighterB.getName() + " has been slain, but " + heOrShe(fighterB.getGender()) +
                    " has perished with honor!");
            announcerSay(fighterA.getName() + " is the victor of the fight!");
            return fighterA;
        }
        if (aYield) {
            announcerSay(fighterA.getName() + " has yielded and must withdraw from the tournament to tend to " +
                    hisOrHer(fighterA.getGender()) + " wounds!");
            announcerSay(fighterB.getName() + " is the victor of the fight!");
            return fighterB;
        }
        announcerSay(fighterB.getName() + " has yielded and must withdraw from the tournament to tend to " +
                hisOrHer(fighterB.getGender()) + " wounds!");
        announcerSay(fighterA.getName() + " is the victor of the fight!");
        return fighterA;
    }

    private void announcerStartOfCombat(GameCharacter fighterA, GameCharacter fighterB) {
        announcerSay("And now, ladies and gentlemen, we are about to see a " +
                MyRandom.sample(List.of("fierce", "exciting", "hectic")) + " " +
                MyRandom.sample(List.of("fight", "face off", "combat", "bout", "match")) + " between two skilled opponents!");
        announcerSay("In one corner, we have a" + present(fighterA) + ". Let's have a big round of applause for... " +
                fighterA.getName() + "!");
        announcerSay("And in the other corner, we have a" + present(fighterB) + ". Let's have another big round of applause for... " +
                fighterB.getName() + "!");
        getModel().getLog().waitForAnimationToFinish();
    }

    private String present(GameCharacter fighter) {
        if (fighter.getCharClass() == Classes.None) {
            return "... uh, well a fellow..";
        }
        return " " + MyRandom.sample(ADJECTIVES) + " " + fighter.getCharClass().getFullName().toLowerCase();
    }

    protected void announcerSay(String s) {
        showAnnouncer();
        portraitSay(s);
    }

    protected void addToEntryFeeToLoan(Model model) {
        if (model.getParty().getLoan() != null) {
            Loan currentLoan = model.getParty().getLoan();
            model.getParty().setLoan(new Loan(currentLoan.getAmount() + ENTRY_FEE, currentLoan.getDay()));
        } else {
            model.getParty().setLoan(new Loan(ENTRY_FEE, model.getDay()));
        }
        model.getTutorial().loans(model);
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
