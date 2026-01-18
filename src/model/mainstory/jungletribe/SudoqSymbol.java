package model.mainstory.jungletribe;

import view.MyColors;
import view.ScreenHandler;
import view.sprites.Sprite;
import view.sprites.Sprite16x16;

import java.awt.*;

public class SudoqSymbol {

    private static final MyColors[] COLORS = new MyColors[]{
            MyColors.RED, MyColors.BLUE, MyColors.GREEN,
            MyColors.YELLOW, MyColors.PURPLE, MyColors.ORANGE};

    private static final Sprite[] SPRITES = new Sprite[]{
            new Sprite16x16("sudoq1", "quest.png", 0x1DA, MyColors.BLACK, MyColors.RED, MyColors.BLACK, MyColors.BLACK),
            new Sprite16x16("sudoq2", "quest.png", 0x1DB, MyColors.BLACK, MyColors.BLUE, MyColors.BLACK, MyColors.BLACK),
            new Sprite16x16("sudoq3", "quest.png", 0x1EA, MyColors.BLACK, MyColors.GREEN, MyColors.BLACK, MyColors.BLACK),
            new Sprite16x16("sudoq4", "quest.png", 0x1EB, MyColors.BLACK, MyColors.YELLOW, MyColors.BLACK, MyColors.BLACK),
            new Sprite16x16("sudoq5", "quest.png", 0x1FA, MyColors.BLACK, MyColors.PURPLE, MyColors.BLACK, MyColors.BLACK),
            new Sprite16x16("sudoq6", "quest.png", 0x1FB, MyColors.BLACK, MyColors.ORANGE, MyColors.BLACK, MyColors.BLACK)
    };

    private static final Sprite[] PRESET_SPRITES = new Sprite[]{
            new Sprite16x16("preset1", "quest.png", 0x1DA, MyColors.GRAY, MyColors.RED, MyColors.RED, MyColors.DARK_GRAY),
            new Sprite16x16("preset2", "quest.png", 0x1DB, MyColors.GRAY, MyColors.BLUE, MyColors.BLUE, MyColors.DARK_GRAY),
            new Sprite16x16("preset3", "quest.png", 0x1EA, MyColors.GRAY, MyColors.GREEN, MyColors.GREEN, MyColors.DARK_GRAY),
            new Sprite16x16("preset4", "quest.png", 0x1EB, MyColors.GRAY, MyColors.YELLOW, MyColors.YELLOW, MyColors.DARK_GRAY),
            new Sprite16x16("preset5", "quest.png", 0x1FA, MyColors.GRAY, MyColors.PURPLE, MyColors.PURPLE, MyColors.DARK_GRAY),
            new Sprite16x16("preset6", "quest.png", 0x1FB, MyColors.GRAY, MyColors.ORANGE, MyColors.ORANGE, MyColors.DARK_GRAY)
    };

    private final Sprite sprite;
    private final boolean preset;
    private int value;

    public SudoqSymbol(int value, boolean preset) {
        this.value = value;
        this.sprite = value == 0 ? null : (preset ? PRESET_SPRITES[value - 1] : SPRITES[value - 1]);
        this.preset = preset;
    }

    public int getValue() {
        return value;
    }

    public boolean isPreset() {
        return preset;
    }

    public void drawYourself(ScreenHandler screenHandler, Point p) {
        if (value > 0) {
            screenHandler.register(sprite.getName(), p, sprite);
        }
    }

    public SudoqSymbol copy() {
        return new SudoqSymbol(value, preset);
    }

    public static SudoqSymbol makeBlank() {
        return new SudoqSymbol(0, false);
    }

    public String getColor() {
        if (value == 0) {
            return "None";
        }
        return COLORS[value-1].name().toLowerCase();
    }
}
