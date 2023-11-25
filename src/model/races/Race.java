package model.races;

import model.characters.appearance.NormalNeck;
import model.characters.appearance.TorsoNeck;
import model.classes.Skill;
import util.MyRandom;
import view.MyColors;
import view.sprites.FaceSpriteWithHair;
import view.sprites.Neck;
import view.sprites.PortraitSprite;

import java.io.Serializable;
import java.util.List;

public abstract class Race implements Serializable {

    public static final Race HALF_ORC = new HalfOrc();
    public static final Race NORTHERN_HUMAN = new NorthernHuman();
    public static final Race SOUTHERN_HUMAN = new SouthernHuman();
    public static final Race HIGH_ELF = new HighElf();
    public static final Race DARK_ELF = new DarkElf();
    public static final Race WOOD_ELF = new WoodElf();
    public static final Race HALFLING = new Halfling();
    public static final Race DWARF = new Dwarf();
    public static final Race ALL = new AllRaces();
    public static final Race GOBLIN = new GoblinRace();
    public static final Race WITCH_KING = new WitchKingRace();
    public static final Race[] allRaces = new Race[]{NORTHERN_HUMAN, SOUTHERN_HUMAN, HIGH_ELF, WOOD_ELF, DARK_ELF, HALFLING, DWARF, HALF_ORC};
    private static int nextRaceId = 0;
    private final int id;
    private final String description;
    private String name;
    private MyColors color;
    private int hpModifier;
    private int speedModifier;
    private int carryCap;
    private Skill[] skillBonuses;

    protected Race(String name, MyColors color, int hpModifier, int speed, int carryCap, Skill[] skillBonuses, String description) {
        this.name = name;
        this.color = color;
        this.hpModifier = hpModifier;
        this.speedModifier = speed;
        this.carryCap = carryCap;
        this.skillBonuses = skillBonuses;
        this.id = nextRaceId++;
        this.description = description;
    }

    public static List<Race> getAllRaces() {
        return List.of(NORTHERN_HUMAN, SOUTHERN_HUMAN, HIGH_ELF, WOOD_ELF, DARK_ELF, HALFLING, DWARF, HALF_ORC);
    }

    public String getName() {
        return name;
    }

    public MyColors getColor() {
        return color;
    }

    public int getHPModifier() {
        return hpModifier;
    }

    public abstract PortraitSprite getLeftEar(MyColors hairColor);

    public abstract PortraitSprite getRightEar(MyColors hairColor);

    public int getSpeedModifier() {
        return speedModifier;
    }

    public int getBonusForSkill(Skill skill) {
        for (Skill s : skillBonuses) {
            if (s.areEqual(skill)) {
                return 1;
            }
        }
        return 0;
    }

    public List<Skill> getSkills() {
        return List.of(skillBonuses);
    }

    protected static PortraitSprite normalLeftEar(MyColors hairColor) {
        return new FaceSpriteWithHair(0x70, hairColor);
    }

    protected static PortraitSprite normalRightEar(MyColors hairColor) {
        return new FaceSpriteWithHair(0x80, hairColor);
    }

    public boolean isShort() {
        return false;
    }

    public String getQualifiedName() {
        return getName();
    }

    public int id() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public static Race randomRace() {
        return allRaces[MyRandom.randInt(allRaces.length)];
    }

    public Shoulders makeShoulders(boolean gender) {
        if (gender) {
            return new SlenderShoulders(true);
        }
        return new NormalShoulders();
    }

    public boolean isSkeleton() {
        return false;
    }

    public int getCarryingCapacity() {
        return carryCap;
    }

    public MyColors getMouthDefaultColor() {
        return MyColors.DARK_RED;
    }

    public TorsoNeck makeNeck() {
        return new NormalNeck();
    }
}
