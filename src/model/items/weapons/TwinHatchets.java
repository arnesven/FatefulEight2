package model.items.weapons;

import model.classes.Skill;
import model.items.Item;
import util.MyPair;
import view.sprites.Sprite;
import view.sprites.TwoHandedItemSprite;

import java.util.List;

public class TwinHatchets extends AxeWeapon {

    private static final Sprite SPRITE = new TwoHandedItemSprite(7, 11);

    public TwinHatchets() {
        super("Twin Hatchets", 16, new int[]{6, 11}, true);
    }

    @Override
    public int getNumberOfAttacks() {
        return 2;
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public Item copy() {
        return new TwinHatchets();
    }

    @Override
    public List<MyPair<Skill, Integer>> getSkillBonuses() {
        return List.of(new MyPair<>(Skill.Axes, -2));
    }
}
