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

public class PirateCaptainsHat extends HeadGearItem {
    private static final List<MyColors> BAND_COLORS = List.of(MyColors.RED, MyColors.ORANGE,
            MyColors.BEIGE, MyColors.DARK_RED, MyColors.DARK_PURPLE, MyColors.BLUE);
    private final ItemSprite sprite;

    public PirateCaptainsHat() {
        super("Captain's Hat", 34);
        MyColors bandColor = MyRandom.sample(BAND_COLORS);
        this.sprite = new ItemSprite(1, 17, MyColors.DARK_GRAY, bandColor, MyColors.DARK_BLUE);
    }

    @Override
    protected Sprite getSprite() {
        return sprite;
    }

    @Override
    public Item copy() {
        return new PirateCaptainsHat();
    }

    @Override
    public List<MyPair<Skill, Integer>> getSkillBonuses() {
        return List.of(new MyPair<>(Skill.Endurance, 1),
                       new MyPair<>(Skill.Leadership, 1),
                       new MyPair<>(Skill.Persuade, 1));
    }

    @Override
    public Prevalence getPrevalence() {
        return Prevalence.veryRare;
    }

    @Override
    public int getAP() {
        return 1;
    }
}
