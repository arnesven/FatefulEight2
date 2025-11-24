package model.items.analysis;

import model.Model;
import model.items.designs.CraftingDesign;
import view.AnalyzeDialog;
import view.CraftingDesignAnalysisDialog;

public class CraftingDesignAnalysis extends ItemAnalysis {
    private final CraftingDesign design;

    public CraftingDesignAnalysis(CraftingDesign craftingDesign) {
        super("Crafting Chance");
        this.design = craftingDesign;
    }

    @Override
    public AnalyzeDialog getDialog(Model model) {
        return new CraftingDesignAnalysisDialog(model, design);
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
