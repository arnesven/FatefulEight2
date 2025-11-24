package model.items.analysis;

import model.Model;
import view.AnalyzeDialog;

public abstract class ItemAnalysis {

    private final String type;

    public ItemAnalysis(String analysisType) {
        this.type = analysisType;
    }

    public String getAnalysisType() {
        return type;
    }

    public abstract AnalyzeDialog getDialog(Model model);

    public abstract boolean showInInventory();

    public abstract boolean showInUpgradeView();
}
