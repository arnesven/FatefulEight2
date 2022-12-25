package model.items.weapons;

import model.classes.Skill;
import model.items.Item;
import util.MyRandom;
import view.MyColors;
import view.sprites.ItemSprite;
import view.sprites.Sprite;

import java.util.List;

public class BoneWand extends WandWeapon {

    private final ItemSprite sprite;

    public BoneWand() {
        super("Bone Wand", 20, Skill.MagicBlack, new int[]{9,11,14});
        sprite = new ItemSprite(5, 6, MyColors.BEIGE,
                MyRandom.sample(List.of(MyColors.LIGHT_BLUE, MyColors.PINK, MyColors.LIGHT_YELLOW)));
    }

    @Override
    protected Sprite getSprite() {
        return sprite;
    }

    @Override
    public Item copy() {
        return new BoneWand();
    }
}
