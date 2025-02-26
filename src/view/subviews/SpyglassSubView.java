package view.subviews;

import model.Model;

public class SpyglassSubView extends MapSubView {
    public SpyglassSubView(Model model) {
        super(model);
    }

    @Override
    protected String getTitleText(Model model) {
        return "USING SPYGLASS";
    }
}
