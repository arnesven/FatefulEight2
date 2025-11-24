package model.items.analysis;

import model.Model;
import model.items.spells.Spell;
import view.AnalyzeDialog;
import view.AnalyzeSpellDialog;

public class SpellAnalysis extends ItemAnalysis {
    private final Spell spell;

    public SpellAnalysis(Spell spell) {
        super("Cast Chance");
        this.spell = spell;
    }

    @Override
    public AnalyzeDialog getDialog(Model model) {
        return new AnalyzeSpellDialog(model, spell);
    }

    @Override
    public boolean showInInventory() {
        return true;
    }

    @Override
    public boolean showInUpgradeView() {
        return false;
    }
}
