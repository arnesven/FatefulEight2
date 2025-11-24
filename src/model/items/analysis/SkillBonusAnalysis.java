package model.items.analysis;

import model.Model;
import model.items.accessories.Accessory;
import model.items.clothing.Clothing;
import view.AnalyzeDialog;
import view.AnalyzeSkillFromAccessoryDialog;
import view.AnalyzeSkillFromClothingDialog;

public class SkillBonusAnalysis extends ItemAnalysis {
    private static final String ANALYSIS_TYPE = "Skill Bonuses";
    private final Clothing clothing;
    private final Accessory accessory;

    public SkillBonusAnalysis(Clothing clothing) {
        super(ANALYSIS_TYPE);
        this.clothing = clothing;
        this.accessory = null;
    }

    public SkillBonusAnalysis(Accessory accessory) {
        super(ANALYSIS_TYPE);
        this.clothing = null;
        this.accessory = accessory;
    }

    @Override
    public AnalyzeDialog getDialog(Model model) {
        if (accessory == null) {
            return new AnalyzeSkillFromClothingDialog(model, clothing);
        }
        return new AnalyzeSkillFromAccessoryDialog(model, accessory);
    }

    @Override
    public boolean showInInventory() {
        return false;
    }

    @Override
    public boolean showInUpgradeView() {
        return false;
    }
}
