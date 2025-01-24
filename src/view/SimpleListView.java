package view;

import model.Model;
import util.MyLists;
import view.party.DrawableObject;
import view.party.SelectableListMenu;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

public class SimpleListView extends SelectableListMenu {
    private final List<String> items;
    private final String title;

    public SimpleListView(GameView view, List<String> items, String title) {
        super(view, 25, 20);
        this.items = items;
        this.title = title;
    }

    @Override
    public void transitionedFrom(Model model) { }

    @Override
    protected List<DrawableObject> buildDecorations(Model model, int xStart, int yStart) {
        return List.of(new TextDecoration(title, xStart, yStart+1, MyColors.LIGHT_GRAY, MyColors.BLUE, true));
    }

    @Override
    protected List<ListContent> buildContent(Model model, int xStart, int yStart) {
        List<ListContent> result = new ArrayList<>();
        int row = yStart + 1;
        for (String st : items) {
            result.add(new ListContent(xStart + 1, ++row, st));
        }
        return result;
    }

    @Override
    protected void specificHandleEvent(KeyEvent keyEvent, Model model) {

    }
}
