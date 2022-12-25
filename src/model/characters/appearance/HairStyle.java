package model.characters.appearance;

import model.characters.FemaleLongHairStyle;
import model.characters.MordKroftHairStyle;
import model.characters.TorhildHairstyle;
import model.characters.VzaniHairStyle;
import view.MyColors;
import view.sprites.FaceSpriteWithHair;
import view.sprites.FilledBlockSprite;
import view.sprites.Sprite8x8;

import java.io.Serializable;

public abstract class HairStyle implements Serializable {
    public static MyColors[] allHairColors = new MyColors[]{
            MyColors.DARK_GRAY, MyColors.GRAY,
            MyColors.DARK_BROWN, MyColors. BROWN, MyColors.TAN,
            MyColors.YELLOW, MyColors.LIGHT_YELLOW, MyColors.BEIGE,
            MyColors.LIGHT_GRAY, MyColors.WHITE,
            MyColors.DARK_RED, MyColors.GRAY_RED, MyColors.ORANGE,
            MyColors.DARK_PURPLE, MyColors.PURPLE, MyColors.PINK,
            MyColors.LIGHT_BLUE, MyColors.CYAN
    };
    public static HairStyle[] allHairStyles = new HairStyle[]{
            new BaldHairStyle(), new FemaleLongHairStyle(), new FemaleLongHairStyle(0x9),
            new FemaleLongHairStyle(0x5D), new FemaleLongHairStyle(0xA7), new ShortFemaleHair(),
            new ShortFemaleHair(0x5D), new ShortFemaleHair(0xA7),
            new HairStyle3x2(0x9, true),
            new HairStyle3x2(0x9, true, true, true, true),
            new HairStyle3x2(0x3D, true, true, false, false),
            new HairStyle3x2(0x5D, true, true, true, true),
            new HairStyle3x2(0x5D, true, true, true, false),
            new HairStyle3x2(0x9D, false),
            new HairStyle3x2(0xC, false),
            new HairStyle3x2(0xC, false, true, true, true),
            new ExplicitHairStyle(true, 0x04, 0xEF, 0x14, 0x05, 0xFC, 0x15),
            new ExplicitHairStyle(false, 0x02, 0xFF, 0x12, 0x03, 0xFD, 0x13),
            new TopKnotHairStyle(MyColors.BEIGE, true),
            new MordKroftHairStyle(),
            new MordKroftHairStyle(0xC, true, true, true, true),
            new HairStyle3x2(6, false),
            new HairStyle3x2(0xA7, false, true, false, false),
            new HairStyle3x2(0xA7, false, true, true, true),
            new TorhildHairstyle(),
            new PigTailHairStyle(0x9, true),
            new PigTailHairStyle(0xC, true),
            new VzaniHairStyle()
    };
    private final boolean onTop;
    private final boolean inForehead;
    private final boolean hairInBack;
    private final boolean longHair;

    public HairStyle(boolean inForehead, boolean hairOnTop, boolean hairInBack, boolean longHair) {
        this.inForehead = inForehead;
        this.onTop = hairOnTop;
        this.hairInBack = hairInBack;
        this.longHair = longHair;
    }

    public HairStyle(boolean inForehead) {
        this(inForehead, true, false, true);
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
}
