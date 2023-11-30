package model.classes;

import view.MyColors;

public abstract class SpecialCharacterClass extends CharacterClass {

    protected SpecialCharacterClass(String name, String shortname, int hp, int speed,
                                    boolean canUseHeavyArmor, int startGold, WeightedSkill[] skillBonuses) {
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
        return "* Special Class *";
    }

    @Override
    public String getHowToLearn() {
        return "";
    }

    @Override
    public boolean isSpecialCharacter() {
        return true;
    }
}
