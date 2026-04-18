package view.combat;

import model.Model;

import util.MyRandom;
import view.MyColors;
import view.sprites.Sprite;
import view.sprites.Sprite32x32;

import java.util.Map;
import java.util.Random;

public class FieldsCombatTheme extends CombatTheme {

    private static final int BARN_INDEX = 7;
    private static final Sprite[] GRASS_SPRITES_DAY = makeGroundSprites(MyColors.LIGHT_YELLOW, MyColors.TAN, VERY_GRASSY_ROW);
    private static final Sprite[] GRASS_SPRITES_NIGHT = makeGroundSprites(MyColors.TAN, MyColors.DARK_GRAY, VERY_GRASSY_ROW);

    private static final Map<String, Sprite> DAY_SPRITES = Map.of(
            "CORN_FIELD", new Sprite32x32("cornfield", "combat.png", 0x122,
                    MyColors.LIGHT_YELLOW, MyColors.DARK_GREEN, MyColors.YELLOW, MyColors.CYAN),
            "CORN_FIELD_LOWER", new Sprite32x32("cornfieldlower", "combat.png", 0x128,
                    MyColors.LIGHT_YELLOW, MyColors.DARK_GREEN, MyColors.YELLOW, MyColors.CYAN),
            "SKY", new Sprite32x32("sky", "combat.png", 0x129,
                    MyColors.CYAN, MyColors.CYAN, MyColors.YELLOW, MyColors.CYAN),
            "MOON", new Sprite32x32("notamoon", "combat.png", 0x129,
                    MyColors.CYAN, MyColors.CYAN, MyColors.YELLOW, MyColors.CYAN),
            "CLOUDS", new Sprite32x32("clouds", "quest.png", 0x09,
                    MyColors.CYAN, MyColors.WHITE, MyColors.YELLOW, MyColors.CYAN),
            "BARN", new Sprite32x32("barn", "combat.png", 0x12A,
                    MyColors.LIGHT_YELLOW, MyColors.WHITE, MyColors.RED, MyColors.CYAN),
            "HUT", new Sprite32x32("hut", "combat.png", 0x12B,
                    MyColors.LIGHT_YELLOW, MyColors.WHITE, MyColors.RED, MyColors.CYAN));

    private static final Map<String, Sprite> NIGHT_SPRITES = Map.of(
            "CORN_FIELD", new Sprite32x32("cornfield", "combat.png", 0x122,
                    MyColors.TAN, MyColors.BLACK, MyColors.GOLD, MyColors.DARK_BLUE),
            "CORN_FIELD_LOWER", new Sprite32x32("cornfieldlower", "combat.png", 0x128,
                    MyColors.TAN, MyColors.BLACK, MyColors.GOLD, MyColors.DARK_BLUE),
            "SKY", new Sprite32x32("sky", "combat.png", 0x129,
                    MyColors.DARK_BLUE, MyColors.DARK_BLUE, MyColors.YELLOW, MyColors.DARK_BLUE),
            "MOON", new Sprite32x32("moon", "combat.png", 0x129,
                    MyColors.DARK_BLUE, MyColors.YELLOW, MyColors.YELLOW, MyColors.DARK_BLUE),
            "CLOUDS", new Sprite32x32("clouds", "quest.png", 0x09,
                    MyColors.DARK_BLUE, MyColors.BLUE, MyColors.YELLOW, MyColors.DARK_BLUE),
            "BARN", new Sprite32x32("barn", "combat.png", 0x12A,
                    MyColors.TAN, MyColors.WHITE, MyColors.RED, MyColors.DARK_BLUE),
            "HUT", new Sprite32x32("hut", "combat.png", 0x12B,
                    MyColors.TAN, MyColors.WHITE, MyColors.RED, MyColors.DARK_BLUE));

    private final boolean withBarn;
    private final boolean withHut;
    private final Map<String, Sprite> sprites;
    private final Sprite[] grass;
    private final boolean[] cloudIndices = new boolean[]{false, false, false, false, false, false, false};
    private final int hutIndex;

    private Random random = new Random(321);

    public FieldsCombatTheme(boolean useDaySprites) {
        this.withBarn = MyRandom.flipCoin();
        this.withHut = MyRandom.flipCoin();
        this.hutIndex = MyRandom.randInt(7);
        if (MyRandom.flipCoin()) {
            for (int i = 0; i < cloudIndices.length; ++i) {
                cloudIndices[i] = MyRandom.flipCoin();
            }
        }
        if (useDaySprites) {
            this.sprites = DAY_SPRITES;
            this.grass = GRASS_SPRITES_DAY;
        } else {
            this.sprites = NIGHT_SPRITES;
            this.grass = GRASS_SPRITES_NIGHT;
        }
    }

    public FieldsCombatTheme() {
        this(true);
    }

    @Override
    public void drawBackground(Model model, int xOffset, int yOffset) {
        random.setSeed(321);
        for (int i = 0; i < 8; ++i) {
            for (int y = 0; y < 9; y++) {
                int yPos = yOffset + y*4;
                int xPos = xOffset + i*4;
                Sprite spr;
                if (y == 0) {
                    if (i == 7) {
                        spr = sprites.get("MOON");
                    } else if (cloudIndices[i]) {
                        spr = sprites.get("CLOUDS");
                    } else {
                        spr = sprites.get("SKY");
                    }
                } else if (y == 1) {
                    if (withBarn && i == BARN_INDEX) {
                        spr = sprites.get("BARN");
                    } else if (withHut && i == hutIndex) {
                        spr = sprites.get("HUT");
                    } else {
                        spr = sprites.get("CORN_FIELD");
                    }
                } else if (y == 2) {
                    if (withBarn && i == BARN_INDEX || withHut && i == hutIndex) {
                        spr = grass[grass.length - 1];
                    } else {
                        spr = sprites.get("CORN_FIELD_LOWER");
                    }
                } else {
                    spr = grass[random.nextInt(grass.length)];
                }
                model.getScreenHandler().put(xPos, yPos, spr);
            }
        }
    }
}
