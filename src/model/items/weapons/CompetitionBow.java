package model.items.weapons;

import model.classes.Skill;
import model.items.Item;
import util.MyPair;
import view.MyColors;
import view.sprites.Sprite;
import view.sprites.TwoHandedItemSprite;

import java.util.List;

public class CompetitionBow extends BowWeapon {
    private static final Sprite SPRITE = new TwoHandedItemSprite(9, 6, MyColors.BROWN, MyColors.LIGHT_GRAY, MyColors.BEIGE);

    public CompetitionBow() {
        super("Competition Bow", 10, new int[]{13,13,15,15});
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public Item copy() {
        return new CompetitionBow();
    }

    @Override
    public List<MyPair<Skill, Integer>> getSkillBonuses() {
        return List.of(new MyPair<>(Skill.Bows, 1));
    }
}
