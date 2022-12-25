package model.items.weapons;

import model.classes.Skill;
import model.items.Item;
import view.MyColors;
import view.sprites.ItemSprite;
import view.sprites.Sprite;

public class YewWand extends WandWeapon {
    private static final Sprite SPRITE = new ItemSprite(3, 6, MyColors.TAN, MyColors.BEIGE);

    public YewWand() {
        super("Yew Wand", 20, Skill.MagicGreen, new int[]{9, 11, 14});
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public Item copy() {
        return new YewWand();
    }
}
