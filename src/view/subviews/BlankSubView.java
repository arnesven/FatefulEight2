package view.subviews;

import model.Model;

public class BlankSubView extends SubView {
    private final SubView inner;

    public BlankSubView(SubView inner) {
        this.inner = inner;
    }

    @Override
    protected void drawArea(Model model) {
        model.getScreenHandler().clearSpace(X_OFFSET, X_MAX, Y_OFFSET, Y_MAX);
        model.getScreenHandler().clearForeground(X_OFFSET, X_MAX, Y_OFFSET, Y_MAX);
    }

    @Override
    protected String getUnderText(Model model) {
        return inner.getUnderText(model);
    }

    @Override
    protected String getTitleText(Model model) {
        return inner.getTitleText(model);
    }
}
