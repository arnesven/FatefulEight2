package test;

import model.classes.Skill;
import model.items.ItemDeck;
import model.items.spells.MasterySpell;
import model.items.spells.Spell;

import java.util.List;
import java.util.Locale;

public class SpellSummary {
    public static void main(String[] args) {
        Locale.setDefault(Locale.US);
        List<Spell> spellList = ItemDeck.allSpells();
        System.out.println("NAME                COLOR  MASTERY? COST DIFF   HP");
        for (Spell sp : spellList) {
            System.out.format("%-20s %-10s %3s %4d %4d %4d\n", sp.getName(), getColor(sp),
                    sp instanceof MasterySpell && ((MasterySpell) sp).masteriesEnabled() ? "Yes" : "No",
                    sp.getCost(), sp.getDifficulty(), sp.getHPCost());
        }
    }

    private static String getColor(Spell sp) {
        if (sp.getSkill() == Skill.MagicAny) {
            return "COLORLESS";
        }
        return Spell.getColorForSkill(sp.getSkill()).name();
    }
}
