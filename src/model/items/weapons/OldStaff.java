package model.items.weapons;

import model.classes.Skill;
import model.items.Item;
import util.MyPair;
import view.sprites.TwoHandedItemSprite;
import view.sprites.Sprite;

import java.util.List;

public class OldStaff extends BluntWeapon {
    private static final Sprite SPRITE = new TwoHandedItemSprite(2, 1);

    public OldStaff() {
        super("Old Staff", 16, new int[]{6, 11}, true, 0);
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public Item copy() {
        return new OldStaff();
    }

    @Override
    public List<MyPair<Skill, Integer>> getSkillBonuses() {
        return List.of(new MyPair<>(Skill.SpellCasting, 1));
    }
}
