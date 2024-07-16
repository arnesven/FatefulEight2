package view;

import model.Model;
import model.characters.GameCharacter;
import model.combat.conditions.VampireAbility;
import model.combat.conditions.VampirismCondition;
import util.MyStrings;
import view.party.DrawableObject;
import view.party.SelectableListMenu;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

public class VampireStageProgressionDialog extends SelectableListMenu {
    private static final int DIALOG_WIDTH = 29;
    private static final int DIALOG_HEIGHT = 24;
    private final GameCharacter vampire;
    private final VampirismCondition condition;
    private final List<VampireAbility> abilitiesToChooseFrom;
    private VampireAbility chosenAbility = null;

    public VampireStageProgressionDialog(Model model, GameCharacter vampire, VampirismCondition cond) {
        super(model.getView(), DIALOG_WIDTH, DIALOG_HEIGHT);
        this.vampire = vampire;
        this.condition = cond;
        abilitiesToChooseFrom = cond.getRandomAbilities();
    }

    @Override
    protected boolean escapeDisposesMenu() {
        return false;
    }

    @Override
    public void transitionedFrom(Model model) {
        VampireAbility chosen = getChosenVampireAbility();
        model.getLog().addAnimated(vampire.getName() + " vampirism progressed to stage " +
                condition.getStage() + ", learned ability " + chosen.getName() + ".\n");
        condition.learnAbility(chosen);
    }

    @Override
    protected List<DrawableObject> buildDecorations(Model model, int xStart, int yStart) {
        return List.of(new DrawableObject(xStart+1, yStart+1) {
            @Override
            public void drawYourself(Model model, int x, int y) {

                BorderFrame.drawCentered(model.getScreenHandler(), "VAMPIRISM", y, MyColors.WHITE, MyColors.BLUE);
                y += 2;

                String text = vampire.getName() + "'s vampiric powers have grown, they are now at stage " +
                        condition.getStage() + ".\n\nSelect one vampiric ability to learn from the list below.";
                String[] parts = MyStrings.partitionWithLineBreaks(text, DIALOG_WIDTH-1);

                for (String part : parts) {
                    BorderFrame.drawCentered(model.getScreenHandler(), part, y++, MyColors.WHITE, MyColors.BLUE);
                }
                y += 1;
                x += 6;
                for (VampireAbility abi : abilitiesToChooseFrom) {
                    model.getScreenHandler().register(abi.getSprite().getName(), new Point(x, y), abi.getSprite());
                    y += 4;
                }
            }
        });
    }

    @Override
    protected List<ListContent> buildContent(Model model, int xStart, int yStart) {
        List<ListContent> content = new ArrayList<>();

        int row = yStart + 12;
        int finalX = xStart + 12;
        for (VampireAbility abi : abilitiesToChooseFrom) {
            content.add(new SelectableListContent(finalX, row, abi.getName()) {
                @Override
                public void performAction(Model model, int x, int y) {
                    setInnerMenu(abi.makeInfoDialog(VampireStageProgressionDialog.this, true), model);
                    chosenAbility = abi;
                }
            });
            row += 4;
        }
        return content;
    }

    @Override
    protected void specificHandleEvent(KeyEvent keyEvent, Model model) {

    }

    public VampireAbility getChosenVampireAbility() {
        return chosenAbility;
    }
}
