package model.races;

import model.classes.Skill;
import view.MyColors;

public class WoodElf extends ElvenRace {
    public WoodElf() {
        super("Wood Elf", MyColors.BEIGE,
                new Skill[]{Skill.Bows, Skill.MagicGreen, Skill.Perception},
                "Wood elves are prevalent in the woodlands of the world. Wood elves are tall, have olive or almond skin, " +
                        " and often come across as friendly, frivolous ore care-free. Like other elves their bodies are somewhat more " +
                        "fragile than other races but what they lack in fortitude they make up for in agility. " +
                        "High elves take up all kinds of professions but it is not uncommon for them to be " +
                        "Amazons, Witches, Marksmen, Druids and Foresters.");
    }
}
