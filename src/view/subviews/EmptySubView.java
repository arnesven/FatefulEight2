package view.subviews;

import model.Model;

public class EmptySubView extends SubView {

    @Override
    protected void drawArea(Model model) {

    }

    @Override
    protected String getUnderText(Model model) {
        return "";
    }

    @Override
    protected String getTitleText(Model model) {
        return "";
    }
}
