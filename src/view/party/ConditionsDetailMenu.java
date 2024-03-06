package view.party;

import model.Model;
import model.characters.GameCharacter;
import model.combat.conditions.Condition;
import view.MyColors;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

public class ConditionsDetailMenu extends FixedPositionSelectableListMenu {
    private final GameCharacter character;

    public ConditionsDetailMenu(PartyView partyView, GameCharacter gc, int x, int y) {
        super(partyView, 24, gc.getConditions().size() + 4, x, y);
        this.character = gc;
    }

    @Override
    protected List<DrawableObject> buildDecorations(Model model, int xStart, int yStart) {
        List<DrawableObject> contents = new ArrayList<>();
        contents.add(new TextDecoration("Conditions:", xStart+1, yStart+1, MyColors.WHITE, MyColors.BLUE, false));
        int count = 0;
        for (Condition cond : character.getConditions()) {
            contents.add(new DrawableObject(xStart+1, yStart+2+count) {
                @Override
                public void drawYourself(Model model, int x, int y) {
                    model.getScreenHandler().register(cond.getSymbol().getName(), new Point(x, y), cond.getSymbol());
                }
            });
            count++;
        }
        return contents;
    }

    @Override
    protected List<ListContent> buildContent(Model model, int xStart, int yStart) {
        List<ListContent> contents = new ArrayList<>();
        int count = 0;
        for (Condition cond : character.getConditions()) {
            contents.add(new SelectableListContent(xStart + 2, yStart + 2 + count, cond.getName() + " (" + cond.getShortName() + ")") {
                @Override
                public void performAction(Model model, int x, int y) {
                    setInnerMenu(cond.getHelpView(model.getView()), model);
                }

                @Override
                public boolean isEnabled(Model model) {
                    return true;
                }
            });
            count++;
        }
        return contents;
    }

    @Override
    protected void specificHandleEvent(KeyEvent keyEvent, Model model) {

    }
}
