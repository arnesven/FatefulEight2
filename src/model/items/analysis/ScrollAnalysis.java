package model.items.analysis;

import model.Model;
import model.items.Scroll;
import view.AnalyzeDialog;
import view.AnalyzeScrollDialog;

public class ScrollAnalysis extends ItemAnalysis {
    private final Scroll scroll;

    public ScrollAnalysis(Scroll scroll) {
        super("Cast Chance for");
        this.scroll = scroll;
    }

    @Override
    public AnalyzeDialog getDialog(Model model) {
        return new AnalyzeScrollDialog(model, scroll);
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
