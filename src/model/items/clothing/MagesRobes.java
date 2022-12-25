package model.items.clothing;

import model.classes.Skill;
import model.items.Item;
import view.MyColors;

public class MagesRobes extends RobesClothing {
    public MagesRobes() {
        super("Mage's Robes", Skill.MagicWhite, 13, MyColors.LIGHT_GRAY, MyColors.DARK_RED);
    }

    @Override
    public Item copy() {
        return new MagesRobes();
    }
}
