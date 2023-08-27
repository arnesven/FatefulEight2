package model.items.weapons;

import model.classes.Skill;
import model.items.Item;
import util.MyPair;
import view.MyColors;
import view.sprites.Sprite;
import view.sprites.TwoHandedItemSprite;

import java.util.List;

public class GoldenBow extends BowWeapon {
    private static final Sprite SPRITE = new TwoHandedItemSprite(9, 6, MyColors.GOLD, MyColors.ORANGE, MyColors.LIGHT_GRAY);

    public GoldenBow() {
        super("Golden Bow", 84, new int[]{7,11,11,11,15});
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public int getReloadSpeed() {
        return 2;
    }

    @Override
    public int getSpeedModifier() {
        return 1;
    }

    @Override
    public List<MyPair<Skill, Integer>> getSkillBonuses() {
        return List.of(new MyPair<>(Skill.Bows, 1));
    }

    @Override
    public Item copy() {
        return new GoldenBow();
    }
}
