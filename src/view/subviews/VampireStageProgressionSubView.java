package view.subviews;

import model.Model;
import model.characters.GameCharacter;
import model.combat.conditions.VampireAbility;
import model.combat.conditions.VampirismCondition;
import view.VampireStageProgressionDialog;

import java.awt.event.KeyEvent;

public class VampireStageProgressionSubView extends SubView {

    private final SubView previous;
    private final VampireStageProgressionDialog dialog;

    public VampireStageProgressionSubView(Model model, GameCharacter owner, VampirismCondition vampirismCondition) {
        this.previous = model.getSubView();
        this.dialog = new VampireStageProgressionDialog(model, owner, vampirismCondition);
    }

    @Override
    protected void drawArea(Model model) {
        previous.drawArea(model);
        dialog.drawYourself(model);
    }

    @Override
    protected String getUnderText(Model model) {
        return previous.getUnderText(model);
    }

    @Override
    protected String getTitleText(Model model) {
        return previous.getTitleText(model);
    }

    public boolean isDone() {
        return dialog.timeToTransition();
    }

    @Override
    public boolean handleKeyEvent(KeyEvent keyEvent, Model model) {
        dialog.handleKeyEvent(keyEvent, model);
        return true;
    }

    public VampireAbility getChosenVampireAbility() {
        return dialog.getChosenVampireAbility();
    }
}
