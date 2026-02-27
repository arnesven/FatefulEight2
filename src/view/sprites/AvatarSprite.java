package view.sprites;

import model.characters.appearance.CharacterAppearance;
import model.races.Dwarf;
import model.races.Halfling;
import model.races.Race;
import util.MyTriplet;
import view.MyColors;

import java.util.List;

public class AvatarSprite extends LoopingSprite {

    public static final int STEALTHY_BASE = 0x00;
    public static final int ARMORED_BASE = 0x20;
    public static final int FANCY_MALE_BASE = 0xA0;
    public static final int FANCY_FEMALE_BASE = 0xC0;
    public static final int ROBED_BASE = 0x160;
    public static final int LOOSE_SHIRT_BASE = 0x220;
    public static final int PLAIN_MALE_BASE = 0x1C0;
    public static final int PLAIN_FEMALE_BASE = 0x1A0;
    public static final int LIGHT_ARMOR_BASE = 0x120;

    private final Race race;
    private final MyColors color2;
    private final MyColors color3;
    private final MyColors color4;
    private final Sprite hairSprite;
    private final Sprite hairFromBack;
    private final int num;
    private final MyTriplet<Sprite, Sprite, Sprite> hat;
    private Sprite32x32 deadSprite;
    private int currentFrame = 0;
    private int count = 0;
    private int delay = 16;

    public AvatarSprite(Race race, int num, MyColors color2, MyColors color3, MyColors color4, Sprite hairSprite, Sprite hairFromBack, MyTriplet<Sprite, Sprite, Sprite> hat) {
        super("avatar"+num+race.getName(), "avatars.png", adjustForRace(num, race), 32, 32, makeOverlayList(hairSprite, hat));
        this.race = race;
        this.num = num;
        this.color2 = color2;
        this.color3 = color3;
        this.color4 = color4;
        this.hairSprite = hairSprite;
        this.hairFromBack = hairFromBack;
        this.hat = hat;
        setFrames(4);
        setColor1(MyColors.BLACK);
        setColor2(color2);
        setColor3(color3);
        setColor4(color4);
        deadSprite = new Sprite32x32("deadavatar", "avatars.png", num+3, MyColors.BLACK, color2, race.getColor(),
                makeDeadHairOrHat());
        deadSprite.setColor4(color4);
    }

    private List<Sprite> makeDeadHairOrHat() {
        Sprite flippedHair = new Sprite(hairFromBack.getName() + "flipped", hairFromBack.getMap(), hairFromBack.getColumn(), hairFromBack.getRow(),
                hairFromBack.getWidth(), hairFromBack.getHeight());
        flippedHair.setColor1(hairFromBack.getColor1());
        flippedHair.setRotation(180);
        flippedHair.shiftUpPx(-4);
        return hat == null ? List.of(flippedHair) : List.of(hat.third);
    }

    private static List<Sprite> makeOverlayList(Sprite hairSprite, MyTriplet<Sprite, Sprite, Sprite> hat) {
        if (hat == null) {
            return List.of(hairSprite);
        }
        return List.of(hairSprite, hat.first);
    }

    public AvatarSprite(Race race, int num, MyColors color2, MyColors color3, MyColors color4, Sprite hairSprite, Sprite hairFromBack) {
        this(race, num, color2, color3, color4, hairSprite, hairFromBack, null);
    }

    private static int adjustForRace(int num, Race race) {
        if (race instanceof Halfling) {
            return num + 7;
        }
        if (race instanceof Dwarf) {
            return num + 4;
        }
        return num;
    }

    public AvatarSprite(Race race, int num, MyColors color2, MyColors color3, Sprite hairSprite, Sprite hairFromBack) {
        this(race, num, color2, color3, color2, hairSprite, hairFromBack);
    }

    public static MyTriplet<Sprite, Sprite, Sprite> makeHat(CharacterAppearance appearance, String name, int num, MyColors color1, MyColors color2, MyColors color3, MyColors color4) {
        Sprite front = new Sprite32x32(name, "hats.png", num, color1, color2, color3, color4);
        Sprite back = new Sprite32x32(name + "back", "hats.png", num + 0x10, color1, color2, color3, color4);
        if (appearance.getRace().isShort()) {
            front.shiftUpPx(-2);
            back.shiftUpPx(-2);
        }
        Sprite dead = new Sprite32x32(name + "dead", "hats.png", num + 0x20, color1, color2, color3, color4);
        return new MyTriplet<>(front, back, dead);
    }

    public Sprite getDead() {
        return deadSprite;
    }

    @Override
    protected int getCurrentFrameIndex(int currentFrame) {
        switch (currentFrame) {
            case 2:
                return 0;
            case 3:
                return 2;
            default:
                return currentFrame;
        }
    }

    public Sprite getAvatarBack() {
        return new AvatarSprite(race, num+0x10, color2, color3, color4, hairFromBack, CharacterAppearance.noHair(),
                hat == null ? null : new MyTriplet<>(hat.second, hat.first, hat.third));
    }
}
