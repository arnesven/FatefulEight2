package model.items.analysis;

import model.Model;
import model.items.ArmorItem;
import view.AnalyzeArmorDialog;
import view.AnalyzeDialog;

public class ArmorAnalysis extends ItemAnalysis {
    private final ArmorItem armor;

    public ArmorAnalysis(ArmorItem armorItem) {
        super("Armor Analysis");
        this.armor = armorItem;
    }

    @Override
    public AnalyzeDialog getDialog(Model model) {
        return new AnalyzeArmorDialog(model, armor);
    }

    @Override
    public boolean showInInventory() {
        return true;
    }

    @Override
    public boolean showInUpgradeView() {
        return true;
    }
}
