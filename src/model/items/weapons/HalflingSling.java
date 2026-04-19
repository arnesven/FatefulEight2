package model.items.weapons;

import model.items.Item;
import model.items.Prevalence;
import model.races.Race;
import util.MyPair;
import view.MyColors;
import view.sprites.ItemSprite;
import view.sprites.Sprite;

public class HalflingSling extends Sling {

    private static final Sprite SPRITE = new ItemSprite(5, 16, MyColors.DARK_RED, MyColors.GRAY,
            MyColors.DARK_BROWN);

    @Override
    public String getName() {
        return "Halfling Sling";
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public int[] getDamageTable() {
        return new int[]{8, 9, 9};
    }

    @Override
    protected MyPair<Integer, String> getAttackBonusForRace() {
        return new MyPair<>(1, Race.HALFLING.getPlural());
    }

    @Override
    protected int getAttackBonusForRace(Race race) {
        return race.id() == Race.HALFLING.id() ? getAttackBonusForRace().first : 0;
    }

    @Override
    public Prevalence getPrevalence() {
        return Prevalence.rare;
    }

    @Override
    public Item copy() {
        return new HalflingSling();
    }
}
