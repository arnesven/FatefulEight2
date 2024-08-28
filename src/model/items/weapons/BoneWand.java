package model.items.weapons;

import model.classes.Skill;
import model.items.Item;
import util.MyRandom;
import view.MyColors;
import view.sprites.ItemSprite;
import view.sprites.Sprite;

import java.util.List;

public class BoneWand extends WandWeapon implements PairableWeapon {

    private final ItemSprite sprite;
    private final MyColors color;

    public BoneWand() {
        super("Bone Wand", 20, Skill.MagicBlack, new int[]{9,11,14});
        color = MyRandom.sample(List.of(MyColors.LIGHT_BLUE, MyColors.PINK, MyColors.LIGHT_YELLOW));
        sprite = new ItemSprite(5, 6, MyColors.BEIGE, color);
    }

    @Override
    protected Sprite getSprite() {
        return sprite;
    }

    @Override
    public Item copy() {
        return new BoneWand();
    }

    @Override
    public Sprite makePairSprite() {
        return new ItemSprite(13, 15, MyColors.BEIGE, color);
    }
}
