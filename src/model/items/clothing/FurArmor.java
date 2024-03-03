package model.items.clothing;

import model.classes.Skill;
import model.items.Item;
import util.MyPair;
import util.MyRandom;
import view.MyColors;
import view.sprites.ItemSprite;
import view.sprites.Sprite;

import java.util.List;

public class FurArmor extends Clothing {
    private static final Sprite SPRITE = new ItemSprite(13, 3,
            MyColors.GRAY_RED, MyColors.LIGHT_GRAY, MyColors.BEIGE);
    private static final Sprite ALT_SPRITE = new ItemSprite(12, 3,
            MyColors.ORANGE, MyColors.BROWN, MyColors.BEIGE);
    private final Sprite sprite;

    public FurArmor() {
        super("Fur Armor", 28, 3, false);
        if (MyRandom.flipCoin()) {
            this.sprite = SPRITE;
        } else {
            this.sprite = ALT_SPRITE;
        }
    }

    @Override
    public List<MyPair<Skill, Integer>> getSkillBonuses() {
        return List.of(new MyPair<>(Skill.Endurance, +1),
                new MyPair<>(Skill.SeekInfo, -1));
    }

    @Override
    protected Sprite getSprite() {
        return sprite;
    }

    @Override
    public int getWeight() {
        return 2500;
    }

    @Override
    public Item copy() {
        return new FurArmor();
    }
}
