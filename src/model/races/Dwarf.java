package model.races;

import model.classes.Skill;
import view.MyColors;
import view.sprites.PortraitSprite;

public class Dwarf extends Race {
    public Dwarf() {
        super("Dwarf", MyColors.PINK, +2, -2,
                new Skill[]{Skill.Axes, Skill.Endurance, Skill.Labor},
                "Dwarves are short, stocky individuals who are skilled in all kinds of crafting " +
                        "and manual labor. They are prevalent in hills, mountains and in caves. Dwarves have all " +
                        "kinds of professions but is not uncommon for them to be Miners, Foresters, Artisans, " +
                        "Paladins, Captains and Sorcerers.");
    }

    @Override
    public PortraitSprite getLeftEar(MyColors hairColor) {
        return Race.normalLeftEar(hairColor);
    }

    @Override
    public PortraitSprite getRightEar(MyColors hairColor) {
        return Race.normalRightEar(hairColor);
    }

    @Override
    public boolean isShort() {
        return true;
    }

    @Override
    public Shoulders getShoulders() {
        return Shoulders.BROAD;
    }
}
