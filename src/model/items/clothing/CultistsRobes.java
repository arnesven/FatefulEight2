package model.items.clothing;

import model.classes.Skill;
import model.items.Item;
import view.MyColors;

public class CultistsRobes extends RobesClothing {
    public CultistsRobes() {
        super("Cultist's Robes", Skill.MagicBlack, 13, MyColors.DARK_GRAY, MyColors.BLUE);
    }

    @Override
    public Item copy() {
        return new CultistsRobes();
    }
}
