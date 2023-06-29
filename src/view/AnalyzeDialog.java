package view;

import model.Model;
import model.items.Item;
import util.BeforeAndAfterLine;
import view.party.SelectableListMenu;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;


public abstract class AnalyzeDialog extends SelectableListMenu {
    private static final int DIALOG_WIDTH = 25;

    public AnalyzeDialog(Model model, int baseHeight) {
        super(model.getView(), DIALOG_WIDTH, baseHeight + model.getParty().size());
    }

    public abstract List<DrawableObject> getAnalysisDrawableObjects(Model model, Item it, int xStart, int yStart);

    @Override
    public void transitionedFrom(Model model) { }

    @Override
    protected List<ListContent> buildContent(Model model, int xStart, int yStart) {
        List<ListContent> list = new ArrayList<>();
        list.add(new SelectableListContent(xStart+getWidth()/2-1, yStart+getHeight()-2, "OK") {
            @Override
            public void performAction(Model model, int x, int y) {
                setTimeToTransition(true);
            }
        });
        return list;
    }

    @Override
    protected void specificHandleEvent(KeyEvent keyEvent, Model model) {

    }
}
