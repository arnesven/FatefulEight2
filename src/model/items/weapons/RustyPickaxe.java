package model.items.weapons;

import model.items.Item;
import view.MyColors;
import view.sprites.ItemSprite;
import view.sprites.Sprite;

public class RustyPickaxe extends AxeWeapon {

    private static final Sprite SPRITE =  new ItemSprite(2, 5, MyColors.DARK_BROWN, MyColors.DARK_RED);

    public RustyPickaxe() {
        super("Rusty Pickaxe", 5, new int[]{6, 11}, false);
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public Item copy() {
        return new RustyPickaxe();
    }
}
