package model.classes.npcs;

import model.characters.appearance.AdvancedAppearance;
import model.characters.appearance.CharacterAppearance;
import model.characters.appearance.EvilStaffDetail;
import model.classes.Looks;
import view.MyColors;
import view.sprites.Sprite8x8;

public class WarlockNPCClass extends NPCClass {
    private static final MyColors ROBE_COLOR = MyColors.DARK_PURPLE;

    private static final Sprite8x8 HOOD_LEFT = new Sprite8x8("warlockhoodleft", "face.png", 0xD0,
            ROBE_COLOR, ROBE_COLOR, MyColors.GRAY_RED, MyColors.DARK_GRAY);
    private static final Sprite8x8 HOOD_TOP = new Sprite8x8("warlockhoodtop", "face.png", 0xD1,
            ROBE_COLOR, ROBE_COLOR, MyColors.GRAY_RED, MyColors.DARK_GRAY);
    private static final Sprite8x8 HOOD_RIGHT = new Sprite8x8("warlockhoodright", "face.png", 0xD2,
            ROBE_COLOR, ROBE_COLOR, MyColors.GRAY_RED, MyColors.DARK_GRAY);
    private static final Sprite8x8 HOOD_LL = new Sprite8x8("warlockhoodlowerleft", "face.png", 0x2B0,
            MyColors.TRANSPARENT, ROBE_COLOR, MyColors.GRAY_RED, MyColors.BLACK);
    private static final Sprite8x8 HOOD_MIDDLE = new Sprite8x8("warlockhoodmiddle", "face.png", 0x2B1,
            MyColors.TRANSPARENT, ROBE_COLOR, MyColors.GRAY_RED, MyColors.BLACK);
    private static final Sprite8x8 HOOD_LR = new Sprite8x8("warlockhoodlowerright", "face.png", 0x2B2,
            MyColors.TRANSPARENT, ROBE_COLOR, MyColors.GRAY_RED, MyColors.BLACK);
    private static final Sprite8x8 HOOD_MID_L = new Sprite8x8("warlockhoodlowerleft", "face.png", 0x2B3,
            MyColors.TRANSPARENT, ROBE_COLOR, MyColors.GRAY_RED, MyColors.BLACK);
    private static final Sprite8x8 HOOD_MID_MID = new Sprite8x8("warlockhoodmiddle", "face.png", 0x2B4,
            MyColors.TRANSPARENT, ROBE_COLOR, MyColors.GRAY_RED, MyColors.BLACK);
    private static final Sprite8x8 HOOD_MID_R = new Sprite8x8("warlockhoodlowerright", "face.png", 0x2B5,
            MyColors.TRANSPARENT, ROBE_COLOR, MyColors.GRAY_RED, MyColors.BLACK);
    private final EvilStaffDetail staff;

    public WarlockNPCClass() {
        super("Warlock");
        this.staff = new EvilStaffDetail(MyColors.DARK_GRAY, MyColors.PURPLE);
    }

    @Override
    public void putClothesOn(CharacterAppearance characterAppearance) {
        Looks.putOnRobe(characterAppearance, ROBE_COLOR, MyColors.DARK_GREEN);
        Looks.putOnHood(characterAppearance, ROBE_COLOR);
        if (characterAppearance instanceof AdvancedAppearance) {
            staff.applyYourself((AdvancedAppearance) characterAppearance,
                    characterAppearance.getRace());
        }
    }

    @Override
    public boolean coversEars() {
        return true;
    }

    @Override
    public boolean coversEyebrows() {
        return true;
    }

    @Override
    public boolean coversEyes() {
        return true;
    }

    @Override
    public boolean showDetail() {
        return false;
    }

    @Override
    public void finalizeLook(CharacterAppearance appearance) {
        appearance.addSpriteOnTop(2, 1, HOOD_LEFT);
        appearance.addSpriteOnTop(3, 1, HOOD_TOP);
        appearance.addSpriteOnTop(4, 1, HOOD_RIGHT);

        appearance.addSpriteOnTop(2, 2, HOOD_MID_L);
        appearance.addSpriteOnTop(3, 2, HOOD_MID_MID);
        appearance.addSpriteOnTop(4, 2, HOOD_MID_R);

        appearance.addSpriteOnTop(2, 3, HOOD_LL);
        appearance.addSpriteOnTop(3, 3, HOOD_MIDDLE);
        appearance.addSpriteOnTop(4, 3, HOOD_LR);
    }
}
