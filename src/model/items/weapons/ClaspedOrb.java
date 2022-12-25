package model.items.weapons;

import model.classes.Skill;
import model.items.Item;
import util.MyRandom;
import view.MyColors;
import view.sprites.ItemSprite;
import view.sprites.Sprite;

import java.util.List;

public class ClaspedOrb extends WandWeapon {

    private final ItemSprite sprite;

    public ClaspedOrb() {
        super("Clasped Orb", 20, Skill.MagicBlue, new int[]{9,11,14});
        MyColors color = MyRandom.sample(List.of(MyColors.BLUE, MyColors.CYAN, MyColors.LIGHT_BLUE, MyColors.PURPLE));
        sprite = new ItemSprite(2, 6, MyColors.BROWN, color);
    }

    @Override
    protected Sprite getSprite() {
        return sprite;
    }

    @Override
    public Item copy() {
        return new ClaspedOrb();
    }
}
