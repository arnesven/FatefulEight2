package model.classes.prestige;

import model.characters.GameCharacter;
import model.classes.CharacterClass;
import model.classes.WeightedSkill;
import util.MyLists;
import view.MyColors;

import java.util.List;

public abstract class PrestigeClass extends CharacterClass {
    public static final int MINIMUM_LEVEL = 5;

    protected PrestigeClass(String name, String shortname, int hp, int speed,
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

    protected abstract boolean canBecomeFrom(CharacterClass charClass);

    @Override
    public String getHowToLearn() {
        return "";
    }

    public static List<GameCharacter> getMembersEligibleFor(List<GameCharacter> partyMembers, PrestigeClass targetClass) {
        return MyLists.filter(partyMembers, gc -> gc.getLevel() >= MINIMUM_LEVEL &&
                targetClass.canBecomeFrom(gc.getCharClass()));
    }
}
