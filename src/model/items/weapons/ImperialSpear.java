package model.items.weapons;

import model.items.Item;
import model.items.Prevalence;
import model.races.HumanRace;
import model.races.Race;
import util.MyPair;
import util.MyRandom;
import view.MyColors;
import view.sprites.Sprite;
import view.sprites.TwoHandedItemSprite;

import java.util.List;

public class ImperialSpear extends Spear {

    private final TwoHandedItemSprite sprite;

    public ImperialSpear() {
        MyColors flagColor = MyRandom.sample(List.of(MyColors.PURPLE, MyColors.RED, MyColors.PEACH,
                MyColors.ORC_GREEN, MyColors.LIGHT_BLUE));
        this.sprite = new TwoHandedItemSprite(1, 4,
                MyColors.BROWN, MyColors.LIGHT_GRAY, flagColor);
    }

    @Override
    public String getName() {
        return "Imperial Spear";
    }

    @Override
    protected Sprite getSprite() {
        return sprite;
    }

    @Override
    protected int getAttackBonusForRace(Race race) {
        return HumanRace.isHuman(race) ? getAttackBonusForRace().first : 0;
    }

    @Override
    protected MyPair<Integer, String> getAttackBonusForRace() {
        return new MyPair<>(1, "Humans");
    }

    @Override
    public Prevalence getPrevalence() {
        return Prevalence.rare;
    }

    @Override
    public Item copy() {
        return new ImperialSpear();
    }
}
