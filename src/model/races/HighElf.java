package model.races;

import model.classes.Skill;
import view.MyColors;
import view.sprites.FaceSprite;
import view.sprites.PortraitSprite;

public class HighElf extends ElvenRace {
    protected HighElf() {
        super("High Elf", MyColors.LIGHT_PINK,
                new Skill[]{Skill.MagicWhite, Skill.Perception, Skill.Polearms},
                "High elves are not prevalent in a specific part of the world, they " +
                        "are a scattered race. High elves are tall, fair, well-spoken and gentle, but can " +
                        "come across as distant, cold or aloof. Like other elves their bodies are somewhat more " +
                        "fragile than other races but what they lack in fortitude they make up for in agility. " +
                        "High elves take up all kinds of professions but it is not uncommon for them to be " +
                        "Priests, Paladins, Nobles, Bards and Artisans.");
    }

    @Override
    public String getShortDescription() {
        return "tall, fair, well-spoken and gentle but perhaps sometimes a little aloof.";
    }
}
