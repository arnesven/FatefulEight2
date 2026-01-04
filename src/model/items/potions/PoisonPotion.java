package model.items.potions;

import model.characters.GameCharacter;
import model.classes.Skill;
import util.MyRandom;

public abstract class PoisonPotion extends ThrowablePotion {
    public PoisonPotion(String name, int cost) {
        super(name, cost);
    }


    public static boolean didResistWeakPotion(GameCharacter gc) {
        return gc.getRankForSkill(Skill.Endurance) >= MyRandom.rollD6();
    }

    public static boolean didResistStrongPotion(GameCharacter gc) {
        return gc.getRankForSkill(Skill.Endurance) >= MyRandom.rollD10();
    }

}
