package model.items.accessories;

import model.classes.Skill;
import model.items.Item;
import model.items.Prevalence;
import util.MyPair;
import util.MyRandom;
import view.MyColors;
import view.sprites.ItemSprite;
import view.sprites.Sprite;

import java.util.List;

public class GreatHelm extends HeadGearItem {
    private static final Sprite SPRITE = new ItemSprite(10, 9, MyColors.LIGHT_GRAY, MyColors.BEIGE);
    private static final Sprite ALT_SPRITE = new ItemSprite(10, 11, MyColors.LIGHT_GRAY, MyColors.BEIGE);
    private Sprite sprite;

    public GreatHelm() {
        super("Great Helm", 28);
        if (MyRandom.flipCoin()) {
            sprite = SPRITE;
        } else {
            sprite = ALT_SPRITE;
        }
    }

    @Override
    public List<MyPair<Skill, Integer>> getSkillBonuses() {
        return List.of(new MyPair<>(Skill.Persuade, +1), new MyPair<>(Skill.Perception, -1));
    }

    @Override
    protected Sprite getSprite() {
        return sprite;
    }

    @Override
    public Item copy() {
        return new GreatHelm();
    }

    @Override
    public int getAP() {
        return 3;
    }

    @Override
    public boolean isHeavy() {
        return true;
    }

    @Override
    public Prevalence getPrevalence() {
        return Prevalence.rare;
    }
}
