package view;

import model.Model;
import model.items.Item;
import view.party.DrawableObject;
import view.party.SelectableListMenu;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;


public abstract class AnalyzeDialog extends SelectableListMenu {
    private static final int DIALOG_WIDTH = 25;
    private final String analysisType;

    public AnalyzeDialog(Model model, int width, int baseHeight, String analysisType) {
        super(model.getView(), width, baseHeight + model.getParty().size());
        this.analysisType = analysisType;
    }

    public AnalyzeDialog(Model model, int baseHeight, String analysisType) {
        this(model, DIALOG_WIDTH, baseHeight, analysisType);
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

    protected List<DrawableObject> makeHeader(Item item, int xStart, int yStart) {
        List<DrawableObject> objs = new ArrayList<>();
        objs.add(new TextDecoration(analysisType + ":", xStart, ++yStart,  MyColors.WHITE, MyColors.BLUE, true));
        yStart+=2;
        objs.add(new DrawableObject(xStart, yStart++) {
            @Override
            public void drawYourself(Model model, int x, int y) {
                item.drawYourself(model.getScreenHandler(), 38, y);
            }
        });
        yStart+=2;
        objs.add(new TextDecoration(item.getName(), xStart, ++yStart,  MyColors.WHITE, MyColors.BLUE, true));
        return objs;
    }
}
