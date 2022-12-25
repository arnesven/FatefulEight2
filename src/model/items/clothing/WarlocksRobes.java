package model.items.clothing;

import model.classes.Skill;
import model.items.Item;
import view.MyColors;

public class WarlocksRobes extends RobesClothing {
    public WarlocksRobes() {
        super("Warlock's Robes", Skill.MagicRed, 14, MyColors.DARK_GRAY, MyColors.GOLD, MyColors.RED);
    }

    @Override
    public Item copy() {
        return new WarlocksRobes();
    }
}
