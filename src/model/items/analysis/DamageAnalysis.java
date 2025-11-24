package model.items.analysis;

import model.Model;
import model.items.weapons.Weapon;
import view.AnalyzeDialog;
import view.AnalyzeWeaponDialog;

public class DamageAnalysis extends ItemAnalysis {
    private final Weapon weapon;

    public DamageAnalysis(Weapon weapon) {
        super("Damage Analysis");
        this.weapon = weapon;
    }

    @Override
    public AnalyzeDialog getDialog(Model model) {
        return new AnalyzeWeaponDialog(model, weapon);
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
