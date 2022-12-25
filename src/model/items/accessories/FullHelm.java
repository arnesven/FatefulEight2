package model.items.accessories;

import model.classes.Skill;
import model.items.Item;
import util.MyPair;
import view.MyColors;
import view.sprites.ItemSprite;
import view.sprites.Sprite;

import java.util.List;

public class FullHelm extends HeadGearItem {
    private static final Sprite SPRITE = new ItemSprite(11, 9, MyColors.GOLD, MyColors.DARK_GRAY);

    public FullHelm() {
        super("Full Helm", 22);
    }

    @Override
    public List<MyPair<Skill, Integer>> getSkillBonuses() {
        return List.of(new MyPair<>(Skill.Perception, -1));
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public Item copy() {
        return new FullHelm();
    }

    @Override
    public int getAP() {
        return 3;
    }

    @Override
    public boolean isHeavy() {
        return true;
    }
}
