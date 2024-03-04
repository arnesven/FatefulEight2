package model.items.accessories;

import model.classes.Skill;
import model.items.Item;
import util.MyPair;
import view.MyColors;
import view.sprites.ItemSprite;
import view.sprites.Sprite;

import java.util.List;

public class PlateGauntlets extends GlovesItem {
    private static final Sprite SPRITE =  new ItemSprite(6, 0xA,MyColors.LIGHT_GRAY, MyColors.DARK_GRAY);

    public PlateGauntlets() {
        super("Plate Gauntlets", 14);
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public List<MyPair<Skill, Integer>> getSkillBonuses() {
        return List.of(new MyPair<>(Skill.Security, -1));
    }

    @Override
    public Item copy() {
        return new PlateGauntlets();
    }

    @Override
    public int getAP() {
        return 2;
    }

    @Override
    public int[] getDamageTable() {
        return new int[]{6, 7, 9};
    }

    @Override
    public String getExtraText() {
        return "Unarmed Attack [6/7/9]";
    }

    @Override
    public boolean isHeavy() {
        return true;
    }
}
