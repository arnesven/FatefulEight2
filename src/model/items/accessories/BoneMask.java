package model.items.accessories;

import model.classes.Skill;
import model.items.Item;
import util.MyPair;
import view.MyColors;
import view.sprites.ItemSprite;
import view.sprites.Sprite;

import java.util.List;

public class BoneMask extends HeadGearItem {
    private static final Sprite SPRITE = new ItemSprite(13, 10, MyColors.LIGHT_GRAY, MyColors.BEIGE);

    public BoneMask() {
        super("Bone Mask", 20);
    }

    @Override
    public List<MyPair<Skill, Integer>> getSkillBonuses() {
        return List.of(new MyPair<>(Skill.MagicBlack, 1));
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public Item copy() {
        return new BoneMask();
    }

    @Override
    public int getAP() {
        return 1;
    }
}
