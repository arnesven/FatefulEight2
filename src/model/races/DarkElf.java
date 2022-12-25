package model.races;

import model.classes.Skill;
import view.MyColors;

public class DarkElf extends ElvenRace {

    public DarkElf() {
        super("Dark Elf", MyColors.LIGHT_GRAY, new Skill[]{Skill.MagicRed, Skill.Perception, Skill.Blades});
    }
}
