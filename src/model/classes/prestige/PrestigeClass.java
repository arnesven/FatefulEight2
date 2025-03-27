package model.classes.prestige;

import model.classes.CharacterClass;
import view.MyColors;

public abstract class PrestigeClass extends CharacterClass {
    protected PrestigeClass(String name, String shortname, int hp, int speed, boolean canUseHeavyArmor, int startGold, WeightedSkill[] skillBonuses) {
        super(name, shortname, hp, speed, canUseHeavyArmor, startGold, skillBonuses);
    }

    @Override
    protected MyColors getIconColor() {
        return MyColors.WHITE;
    }

    @Override
    protected int getIconNumber() {
        return 0x14;
    }

    @Override
    public String getDescription() {
        return "* Prestige Class *";
    }

    @Override
    public String getHowToLearn() {
        return "";
    }
}
