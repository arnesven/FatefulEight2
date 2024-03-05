package model.characters.appearance;

import model.characters.*;
import model.characters.preset.MordKroftHairStyle;
import model.characters.preset.TorhildHairstyle;
import model.characters.preset.VzaniHairStyle;
import util.MyRandom;
import view.MyColors;
import view.sprites.*;

import java.io.Serializable;

public abstract class HairStyle implements Serializable {

    private String description;

    public static MyColors[] allHairColors = MyColors.values();

    public static MyColors[] npcHairColors = new MyColors[]{
            MyColors.DARK_GRAY, MyColors.GRAY,
            MyColors.DARK_BROWN, MyColors.BROWN, MyColors.TAN,
            MyColors.YELLOW, MyColors.LIGHT_YELLOW, MyColors.BEIGE,
            MyColors.LIGHT_GRAY, MyColors.WHITE,
            MyColors.DARK_RED, MyColors.GRAY_RED, MyColors.RED,
            MyColors.ORANGE, MyColors.PEACH
    };

    public static HairStyle[] allHairStyles = new HairStyle[]{
            new BaldHairStyle(), new FemaleLongHairStyle("Long #1"),
            new FemaleLongHairStyle(0x9, 0x03, "Long #2"),
            new FemaleLongHairStyle(0x164, 0x04, "Long #3"),
            new FemaleLongHairStyle(0xA7, 0x05, "Long #4"),
            new HeartHairStyle(new FemaleLongHairStyle("Long #1"), true, true, "Long #5"),
            new ShortFemaleHair("Short #1"),
            new ShortFemaleHair(0x164, 0x13, "Short #2"),
            new ShortFemaleHair(0xA7, 0x15, "Short #3"),
            new HeartHairStyle(new ShortFemaleHair("Short #1"), true, false, "Short #4"),
            new HairStyle3x2(0x9, true, 0x10, "Neat"),
            new PuyetHairStyle(),
            new HairStyle3x2(0x167, true, true, false, false, 0x08, 0x00, "Boy Cut"),
            new HairStyle3x2(0x164, true, true, true, true, 0x09, 0x07, "Combed/Long"),
            new HairStyle3x2(0x164, true, true, true, false, 0x19, 0x00, "Combed/Short"),
            new HairStyle3x2(0x9D, false, 0x18, "Mohawk"),
            new HairStyle3x2(0xC, false, 0x17, "Monk"),
            new HairStyle3x2(0xC, false, true, true, true, 0x27, 0x07, "Monk/Long"),
            new ExplicitHairStyle(true, 0x04, 0xEF, 0x14, 0x05, 0xFC, 0x15, 0x10, 0x00, "Flat"),
            new ExplicitHairStyle(false, 0x02, 0xFF, 0x12, 0x03, 0xFD, 0x13, 0x20, 0x00, "Receeding"),
            new TopKnotHairStyle(MyColors.BEIGE, true, "Top Knot/Bald"),
            new MordKroftHairStyle("Top Knot/Flat"),
            new SpecialMordKroftHairStyle(),
            new HairStyle3x2(6, false, 0x16, "Priest"),
            new HairStyle3x2(0xA7, false, true, false, false, 0x24, 0x00, "Rough"),
            new HairStyle3x2(0xA7, false, true, true, true, 0x05, 0x07, "Rough/Long"),
            new TorhildHairstyle(),
            new PigTailHairStyle(0x9, true, 0x26, "Braids/Flat"),
            new PigTailHairStyle(0xC, true, 0x37, "Braids/Priest"),
            new VzaniHairStyle(),
            new AfroHairStyle(),
            new SpikesHairStyle(),
            new BunsHairStyle(),
            new BunsWithLongHairStyle(new FemaleLongHairStyle("Buns/Long"), true, "Buns/Long"),
            new BunsWithLongHairStyle(new ShortFemaleHair("Buns/Short"), false, "Buns/Short"),
            new BunsWithLongHairStyle(new PuyetHairStyle(), true, "Buns/Straight"),
            new MessyHairStyle(),
            new WavyHairStyle(),
            new BigHairStyle(),
            new HeartHairStyle(new PuyetHairStyle(), true, true, "Heart"),
            new HeartHairStyle(new PigTailHairStyle(0x9, true, 0x26, "Braids"), false, false, "Heart/Braids"),
            new OldManHairStyle()
    };

    public static HairStyle[] femaleHairStyles = new HairStyle[]{
            allHairStyles[1],
            allHairStyles[2], allHairStyles[3],
            allHairStyles[4], allHairStyles[5],
            allHairStyles[6], allHairStyles[7],
            allHairStyles[9], allHairStyles[11],
            allHairStyles[12], allHairStyles[15], allHairStyles[19],
            allHairStyles[20], allHairStyles[23],
            allHairStyles[24], allHairStyles[25],
            allHairStyles[26], allHairStyles[27],
            allHairStyles[30], allHairStyles[31],
            allHairStyles[32], allHairStyles[33],
            allHairStyles[37], allHairStyles[38],
            allHairStyles[39], allHairStyles[40]};

    public static HairStyle[] maleHairStyles = new HairStyle[]{
            allHairStyles[0], allHairStyles[8], allHairStyles[10],
            allHairStyles[13], allHairStyles[14], allHairStyles[16],
            allHairStyles[17], allHairStyles[18], allHairStyles[19],
            allHairStyles[21], allHairStyles[22], allHairStyles[28],
            allHairStyles[29], allHairStyles[34], allHairStyles[35],
            allHairStyles[36], allHairStyles[41]};

    private final boolean onTop;
    private final boolean inForehead;
    private final boolean hairInBack;
    private final boolean longHair;

    public HairStyle(boolean inForehead, boolean hairOnTop, boolean hairInBack, boolean longHair, String description) {
        this.inForehead = inForehead;
        this.onTop = hairOnTop;
        this.hairInBack = hairInBack;
        this.longHair = longHair;
        this.description = description;
    }

    public HairStyle(boolean inForehead, String description) {
        this(inForehead, true, false, true, description);
    }

    public abstract int getForeheadLeft();

    public abstract int getForeheadCenter();

    public abstract int getForeheadRight();

    public abstract int getHeadTopLeft();

    public abstract int getHeadTop();

    public abstract int getHeadTopRight();

    public boolean hairInForhead() {
        return inForehead;
    }

    public boolean hairOnTop() {
        return onTop;
    }

    public void apply(AdvancedAppearance appearance) {

    }

    public void addHairInBack(AdvancedAppearance advancedAppearance) {
        int yMax = 5;
        if (longHair){
            yMax = 6;
        }
        if (hairInBack) {
            advancedAppearance.addSpriteOnBelow(2, 4, new FaceSpriteWithHair(0xA6, advancedAppearance.getHairColor()));
            advancedAppearance.addSpriteOnBelow(4, 4, new FaceSpriteWithHair(0xB6, advancedAppearance.getHairColor()));
            for (int y = 5; y < yMax; ++y) {
                for (int x = 2; x < 5; ++x) {
                    if (x != 3) {
                        advancedAppearance.addSpriteOnBelow(x, y, new FilledBlockSprite(advancedAppearance.getHairColor()));
                    }
                }
            }
        }
    }

    public abstract int getNormalHair();

    public abstract int getBackHairOnly();

    public static MyColors randomHairColor() {
        return HairStyle.npcHairColors[MyRandom.randInt(HairStyle.npcHairColors.length)];
    }

    public static HairStyle randomHairStyle() {
        return HairStyle.allHairStyles[MyRandom.randInt(HairStyle.allHairStyles.length)];
    }

    public static HairStyle randomHairStyle(boolean gender) {
        if (gender) {
            return femaleHairStyles[MyRandom.randInt(femaleHairStyles.length)];
        }
        return maleHairStyles[MyRandom.randInt(maleHairStyles.length)];
    }

    public int[] getOuterFrame() {
        return null;
    }

    public String getDescription() {
        return description;
    }

    protected void setDescription(String description) {
        this.description = description;
    }
}
