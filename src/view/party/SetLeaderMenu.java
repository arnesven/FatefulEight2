package view.party;

import model.Model;
import model.characters.GameCharacter;
import view.MyColors;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

class SetLeaderMenu extends FixedPositionSelectableListMenu {
    private final GameCharacter potentialLeader;

    public SetLeaderMenu(PartyView partyView, GameCharacter gc, int x, int y) {
        super(partyView, 13, 4, x, y);
        this.potentialLeader = gc;
    }

    @Override
    protected List<DrawableObject> buildDecorations(Model model, int xStart, int yStart) {
        return List.of(new TextDecoration("Make Leader?", xStart + 1, yStart + 1, MyColors.WHITE, MyColors.BLUE, false));
    }

    @Override
    protected List<ListContent> buildContent(Model model, int xStart, int yStart) {
        List<ListContent> content = new ArrayList<>();
        content.add(new SelectableListContent(xStart + 1, yStart + 2, "Yes") {
            @Override
            public void performAction(Model model, int x, int y) {
                model.getParty().setLeader(potentialLeader);
                setTimeToTransition(true);
            }
        });
        content.add(new SelectableListContent(xStart + 1, yStart + 3, "No") {
            @Override
            public void performAction(Model model, int x, int y) {
                setTimeToTransition(true);
            }
        });
        return content;
    }

    @Override
    protected void specificHandleEvent(KeyEvent keyEvent, Model model) {

    }
}
