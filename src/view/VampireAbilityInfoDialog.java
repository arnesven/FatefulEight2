package view;

import model.Model;
import model.combat.conditions.VampireAbility;
import view.party.DrawableObject;
import view.party.SelectableListMenu;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

public class VampireAbilityInfoDialog extends SelectableListMenu {
    private final int width;
    private final String[] parts;
    private final boolean withSelect;

    public VampireAbilityInfoDialog(GameView previous, int width, String[] parts, boolean withSelect) {
        super(previous, width, parts.length + 6);
        this.width = width;
        this.parts = parts;
        this.withSelect = withSelect;
    }
    
    @Override
    protected List<DrawableObject> buildDecorations(Model model, int xStart, int yStart) {
        return List.of(new DrawableObject(xStart, yStart+1) {
            @Override
            public void drawYourself(Model model, int x, int y) {
                y++;
                for (String part : parts) {
                    BorderFrame.drawCentered(model.getScreenHandler(), part, y++, MyColors.WHITE, MyColors.BLUE);
                }
            }
        });
    }

    @Override
    protected List<ListContent> buildContent(Model model, int xStart, int yStart) {
        List<ListContent> result = new ArrayList<>();
        if (withSelect) {
            result.add(new SelectableListContent(xStart + width / 2 - 3, yStart + getHeight() - 3, "SELECT") {
                @Override
                public void performAction(Model model, int x, int y) {
                    VampireAbilityInfoDialog.this.setTimeToTransition(true);
                    getPrevious().setTimeToTransition(true);
                }
            });
        }

        result.add(new SelectableListContent(xStart + width / 2 - 2, yStart + getHeight() - 2, "BACK") {
            @Override
            public void performAction(Model model, int x, int y) {
                VampireAbilityInfoDialog.this.setTimeToTransition(true);
            }
        });
        return result;
    }

    @Override
    protected void specificHandleEvent(KeyEvent keyEvent, Model model) { }

    @Override
    public void transitionedFrom(Model model) { }

}
