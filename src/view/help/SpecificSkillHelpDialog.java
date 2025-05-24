package view.help;

import model.actions.PassiveCombatAction;
import model.combat.abilities.AbilityCombatAction;
import model.classes.Skill;
import model.combat.abilities.SkillAbilityCombatAction;
import model.combat.abilities.SpecialAbilityCombatAction;
import util.MyLists;
import util.MyPair;
import util.MyStrings;
import view.GameView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class SpecificSkillHelpDialog extends SubChapterHelpDialog {
    public SpecificSkillHelpDialog(GameView view, Skill s) {
        super(view, 20, s.getName(), makeText(s));
    }

    private static String makeText(Skill s) {

        List<SkillAbilityCombatAction> skillAbilities = new ArrayList<>();

        addAllForSkill(skillAbilities, s, AbilityCombatAction.getAllCombatAbilities(null));
        addAllForSkill(skillAbilities, s, AbilityCombatAction.getAllPassiveCombatActions());
        skillAbilities.sort(Comparator.comparingInt(SkillAbilityCombatAction::getRequiredRanks));

        String abiExtra = "";
        if (!skillAbilities.isEmpty()) {
            abiExtra = "\n\nCombat Abilities (Required Ranks)\n" + MyStrings.makeString(skillAbilities,
                    skiAb -> " " + skiAb.getName() + " (" + skiAb.getRequiredRanks() + ")\n");
        }

        return "Short Name: " + s.getShortName() + "\n" +
                getGoverningAttribute(s) + s.getDescription() + abiExtra;
    }

    private static String getGoverningAttribute(Skill s) {
        StringBuilder bldr = new StringBuilder("Governing attribute: ");
        for (String key : Skill.getAttributeSets().keySet()) {
            for (Skill attrSkill : Skill.getAttributeSets().get(key)) {
                if (attrSkill == s) {
                    bldr.append(key);
                    bldr.append("\n\n");
                    return bldr.toString();
                }
            }
        }
        bldr.append("None\n\n");
        return bldr.toString();
    }

    private static <T> void addAllForSkill(List<SkillAbilityCombatAction> skillAbilities,
                                           Skill s, List<T> objects) {
        for (Object sac : objects) {
            if (sac instanceof SkillAbilityCombatAction) {
                SkillAbilityCombatAction skiAb = (SkillAbilityCombatAction) sac;
                if (skiAb.getLinkedSkills().contains(s)) {
                    skillAbilities.add(skiAb);
                }
            }
        }
    }
}
