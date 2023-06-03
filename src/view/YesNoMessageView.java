package view;

import model.Model;

import java.util.List;

public abstract class YesNoMessageView extends SimpleMessageView {
    public YesNoMessageView(GameView previous, String text) {
        super(previous, text, "NO");
    }

    @Override
    protected List<ListContent> buildContent(Model model, int xStart, int yStart) {
        List<ListContent> list = super.buildContent(model, xStart, yStart);
        list.add(new SelectableListContent(xStart+getWidth()/2-1, yStart+getHeight()-1, "YES") {
            @Override
            public void performAction(Model model, int x, int y) {
                setTimeToTransition(true);
                doAction(model);
            }

            @Override
            public boolean isEnabled(Model model) {
                return true;
            }
        });
        return list;
    }

    protected abstract void doAction(Model model);
}
