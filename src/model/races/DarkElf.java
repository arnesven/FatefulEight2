package model.races;

import model.classes.Skill;
import view.MyColors;

public class DarkElf extends ElvenRace {

    public DarkElf() {
        super("Dark Elf", MyColors.LIGHT_GRAY, new Skill[]{Skill.MagicRed, Skill.Perception, Skill.Blades},
                "Dark elves are prevalent in the west of the world. Dark elves are tall, have dusky complexions, " +
                        " and can have a menacing presences. They often come across as reserved or even hostile but are often " +
                        "very dedicated and loyal once they have formed a connection with someone. Like other elves their bodies are somewhat more " +
                        "fragile than other races but what they lack in fortitude they make up for in agility. " +
                        "Dark elves take up all kinds of professions but it is not uncommon for them to be " +
                        "Sorcerers, Wizards, Captains, Marksmen, Black Knights and Nobles.");
    }

    @Override
    public MyColors getMouthDefaultColor() {
        return MyColors.DARK_GRAY;
    }

    @Override
    public String getShortDescription() {
        return "tall, and have dusky complexions, determined but reserved.";
    }
}
