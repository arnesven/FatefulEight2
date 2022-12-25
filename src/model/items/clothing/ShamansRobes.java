package model.items.clothing;

import model.classes.Skill;
import model.items.Item;
import view.MyColors;

public class ShamansRobes extends RobesClothing {
    public ShamansRobes() {
        super("Shaman's Robes", Skill.MagicGreen, 13, MyColors.LIGHT_GREEN, MyColors.BROWN);
    }

    @Override
    public Item copy() {
        return new ShamansRobes();
    }
}
