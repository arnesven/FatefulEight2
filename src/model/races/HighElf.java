package model.races;

import model.classes.Skill;
import view.MyColors;
import view.sprites.FaceSprite;
import view.sprites.PortraitSprite;

public class HighElf extends ElvenRace {
    protected HighElf() {
        super("High Elf", MyColors.LIGHT_PINK,
                new Skill[]{Skill.MagicWhite, Skill.Perception, Skill.Polearms});
    }

}
