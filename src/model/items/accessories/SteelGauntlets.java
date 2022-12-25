package model.items.accessories;

import model.classes.Skill;
import model.items.Item;
import util.MyPair;
import view.MyColors;
import view.sprites.ItemSprite;
import view.sprites.Sprite;

import java.util.List;

public class SteelGauntlets extends GlovesItem {
    private static final Sprite SPRITE = new ItemSprite(5, 0xA, MyColors.LIGHT_GRAY, MyColors.DARK_GRAY);

    public SteelGauntlets() {
        super("Steel Gauntlets", 20);
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public Item copy() {
        return new SteelGauntlets();
    }

    @Override
    public List<MyPair<Skill, Integer>> getSkillBonuses() {
        return List.of(new MyPair<>(Skill.Security, -1));
    }

    @Override
    public boolean isHeavy() {
        return true;
    }

    @Override
    public int getAP() {
        return 2;
    }
}
