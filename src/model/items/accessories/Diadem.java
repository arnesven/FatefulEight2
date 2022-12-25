package model.items.accessories;

import model.classes.Skill;
import model.items.Item;
import util.MyPair;
import util.MyRandom;
import view.MyColors;
import view.sprites.ItemSprite;
import view.sprites.Sprite;

import java.util.List;

public class Diadem extends HeadGearItem {
    private Sprite sprite;

    public Diadem() {
        super("Diadem", 26);
        MyColors[] lightSet = new MyColors[]{MyColors.YELLOW, MyColors.ORANGE, MyColors.CYAN, MyColors.ORC_GREEN, MyColors.PINK};
        MyColors[] darkSet =  new MyColors[]{MyColors.ORC_GREEN, MyColors.RED, MyColors.LIGHT_BLUE, MyColors.GREEN, MyColors.LIGHT_RED};
        int index = MyRandom.randInt(lightSet.length);
        sprite = new ItemSprite(14, 9, MyColors.LIGHT_GRAY, darkSet[index], lightSet[index]);
    }

    @Override
    public List<MyPair<Skill, Integer>> getSkillBonuses() {
        return List.of(new MyPair<>(Skill.SpellCasting, 1));
    }

    @Override
    protected Sprite getSprite() {
        return sprite;
    }

    @Override
    public Item copy() {
        return new Diadem();
    }

    @Override
    public int getAP() {
        return 0;
    }
}
