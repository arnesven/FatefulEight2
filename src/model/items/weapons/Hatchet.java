package model.items.weapons;

import model.classes.Skill;
import model.items.Item;
import view.sprites.ItemSprite;
import view.sprites.Sprite;

public class Hatchet extends AxeWeapon {
    private static final Sprite SPRITE = new ItemSprite(0, 5);

    public Hatchet() {
        super("Hatchet", 5, new int[]{6, 11}, false);
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public Item copy() {
        return new Hatchet();
    }
}
