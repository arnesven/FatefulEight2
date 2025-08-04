package model.combat.abilities;

import model.characters.GameCharacter;
import model.classes.Skill;
import util.MyLists;

import java.util.List;

public interface SkillAbilityCombatAction {
    String getName();
    List<Skill> getLinkedSkills();
    int getRequiredRanks();

    default boolean hasRequiredRanks(GameCharacter gc) {
        return MyLists.any(getLinkedSkills(),
                s -> gc.getUnmodifiedRankForSkill(s) >= getRequiredRanks());
    }

    default boolean hasRequiredLevel() {
        return getRequiredLevel() > 0;
    }

    default int getRequiredLevel() {
        return 0;
    }
}
