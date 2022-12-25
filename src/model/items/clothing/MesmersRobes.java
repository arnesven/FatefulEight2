package model.items.clothing;

import model.classes.Skill;
import model.items.Item;
import view.MyColors;

public class MesmersRobes extends RobesClothing {
    public MesmersRobes() {
        super("Mesmer's Robes", Skill.MagicBlue, 14, MyColors.DARK_GRAY, MyColors.DARK_PURPLE, MyColors.BLUE);
    }

    @Override
    public Item copy() {
        return new MesmersRobes();
    }
}
