package model.races;

import model.classes.Skill;
import view.MyColors;

public class WoodElf extends ElvenRace {
    public WoodElf() {
        super("Wood Elf", MyColors.BEIGE,
                new Skill[]{Skill.Bows, Skill.MagicGreen, Skill.Perception});
    }
}
