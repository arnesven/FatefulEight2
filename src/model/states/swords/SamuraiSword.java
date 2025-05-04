package model.states.swords;

import model.Model;
import model.items.SpecialEasternWeapon;
import model.items.weapons.Weapon;
import model.mainstory.honorable.ShingenWeapon;
import view.MyColors;
import view.sprites.Sprite;
import view.sprites.Sprite32x32;

import java.io.Serializable;

public abstract class SamuraiSword implements Serializable {
    private static final MyColors BLADE_DARK_COLOR = MyColors.GRAY;
    private static final MyColors BLADE_LIGHT_COLOR = MyColors.LIGHT_GRAY;
    private static final MyColors BLADE_HILT_COLOR = MyColors.GOLD;
    private final Weapon item;
    private final MyColors color;
    private final boolean inscription;
    private final Sprite[] sprites;

    public SamuraiSword(MyColors color, boolean inscription, Weapon innerItem, int spriteMapColumn) {
        this.color = color;
        this.inscription = inscription;
        this.item = innerItem;
        this.sprites = makeSprites(color, spriteMapColumn);
    }

    protected Sprite[] makeSprites(MyColors color, int column) {
        Sprite[] sprs = new Sprite32x32[6];
        for (int y = 0; y < sprs.length; ++y) {
            sprs[y] = new Sprite32x32("katana" + column + "-" + y,"katanas.png", 0x10 * y + column,
                    BLADE_DARK_COLOR, BLADE_LIGHT_COLOR, BLADE_HILT_COLOR, color);
        }
        return sprs;
    }

    public void drawYourself(Model model, int x, int y) {
        for (int i = 0; i < sprites.length - 1; ++i) {
            model.getScreenHandler().put(x, y + i * 4, sprites[i]);
        }
        if (inscription) {
            drawInscription(model, x, y, sprites[5]);
        }
    }

    protected void drawInscription(Model model, int x, int y, Sprite sprite) {
        model.getScreenHandler().put(x, y + 3 * 4, sprite);
    }

    public abstract int getCursorOffset();

    public abstract boolean matchesWeaponType(ShingenWeapon shingenWeapon);

    public String getTypeName() {
        return item.getName();
    }

    public boolean hasInscription() {
        return inscription;
    }

    public String getColorName() {
        return colorToString(color);
    }

    public int getCost() {
        return item.getCost() + 66;
    }

    public static String colorToString(MyColors color) {
        return color.name().replace("_", " ").toLowerCase();
    }

    public Weapon makeItem() {
        return new SpecialEasternWeapon(this);
    }

    public Weapon getInnerItem() {
        return item;
    }
}
