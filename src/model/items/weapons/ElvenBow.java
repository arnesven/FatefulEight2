package model.items.weapons;

import model.items.Item;
import model.items.Prevalence;
import model.races.ElvenRace;
import model.races.Race;
import util.MyPair;
import view.MyColors;
import view.sprites.Sprite;
import view.sprites.TwoHandedItemSprite;

public class ElvenBow extends LongBow {

    private static final Sprite SPRITE = new TwoHandedItemSprite(7, 6, MyColors.BROWN, MyColors.LIGHT_GRAY, MyColors.DARK_GREEN);

    @Override
    public String getName() {
        return "Elven Bow";
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    protected int getAttackBonusForRace(Race race) {
        return ElvenRace.isElf(race) ? getAttackBonusForRace().first : 0;
    }

    @Override
    protected MyPair<Integer, String> getAttackBonusForRace() {
        return new MyPair<>(1, "Elves");
    }

    @Override
    public Prevalence getPrevalence() {
        return Prevalence.rare;
    }

    @Override
    public Item copy() {
        return new ElvenBow();
    }
}
