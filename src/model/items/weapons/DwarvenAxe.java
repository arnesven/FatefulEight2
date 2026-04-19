package model.items.weapons;

import model.items.Item;
import model.items.Prevalence;
import model.races.Race;
import util.MyPair;
import util.MyRandom;
import view.MyColors;
import view.sprites.Sprite;
import view.sprites.TwoHandedItemSprite;

import java.util.List;

public class DwarvenAxe extends GreatAxe {

    private final TwoHandedItemSprite sprite;

    public DwarvenAxe() {
        MyColors patternColor = MyRandom.sample(List.of(MyColors.BLACK, MyColors.ORANGE, MyColors.DARK_RED, MyColors.DARK_BLUE));
        this.sprite = new TwoHandedItemSprite(5, 5,
                MyColors.BROWN, MyColors.LIGHT_GRAY, patternColor);
    }

    @Override
    public String getName() {
        return "Dwarven Axe";
    }

    @Override
    protected MyPair<Integer, String> getAttackBonusForRace() {
        return new MyPair<>(1, Race.DWARF.getPlural());
    }

    @Override
    protected int getAttackBonusForRace(Race race) {
        return race.id() == Race.DWARF.id() ? getAttackBonusForRace().first : 0;
    }

    @Override
    protected Sprite getSprite() {
        return sprite;
    }

    @Override
    public Prevalence getPrevalence() {
        return Prevalence.rare;
    }

    @Override
    public Item copy() {
        return new DwarvenAxe();
    }
}
