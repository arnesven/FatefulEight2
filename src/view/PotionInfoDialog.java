package view;

import model.Model;
import model.items.potions.Potion;
import view.party.DrawableObject;
import view.party.SelectableListMenu;

import java.awt.event.KeyEvent;
import java.util.List;

public class PotionInfoDialog extends SelectableListMenu {
    private final Potion potion;

    public PotionInfoDialog(PotionsView potionsView, Potion potion) {
        super(potionsView, 24, 13);
        this.potion = potion;
    }

    @Override
    protected List<DrawableObject> buildDecorations(Model model, int xStart, int yStart) {
        return List.of(new DrawableObject(xStart+1, yStart+1) {
            @Override
            public void drawYourself(Model model, int x, int y) {
                InventoryView.innerPrintItemText(model, potion.getName(), potion, x, y);
            }
        });
    }

    @Override
    protected List<ListContent> buildContent(Model model, int xStart, int yStart) {
        return List.of(makeOkButton(model, xStart+getWidth()/2 - 1, yStart+getHeight()-2, this));
    }

    @Override
    protected void specificHandleEvent(KeyEvent keyEvent, Model model) {

    }

    @Override
    public void transitionedFrom(Model model) {

    }
}
