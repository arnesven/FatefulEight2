package view.help;

import model.Model;
import model.characters.GameCharacter;
import view.party.DrawableObject;
import view.party.SelectableListMenu;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

public class RaceAndClassHelpDialog extends SelectableListMenu {
    private final SpecificRaceHelpDialog raceHelpDialog;
    private final SpecificClassHelpDialog classHelpDialog;

    public RaceAndClassHelpDialog(Model model, GameCharacter gc) {
        super(model.getView(), 70, HelpView.HELP_VIEW_HEIGHT);
        raceHelpDialog = new SpecificRaceHelpDialog(model.getView(), gc.getRace(), gc.getAppearance());
        classHelpDialog = new SpecificClassHelpDialog(model.getView(), gc.getCharClass());
    }

    @Override
    public void transitionedFrom(Model model) { }

    @Override
    protected List<DrawableObject> buildDecorations(Model model, int xStart, int yStart) {
        List<DrawableObject> result = new ArrayList<>();
        result.addAll(raceHelpDialog.buildDecorations(model, xStart, yStart + 1));
        result.addAll(classHelpDialog.buildDecorations(model, xStart + 35, yStart + 1));
        return result;
    }

    @Override
    protected List<ListContent> buildContent(Model model, int xStart, int yStart) {
        return List.of(SelectableListMenu.makeOkButton(model, 40 - 2, yStart + getHeight() - 2, this));
    }

    @Override
    protected void specificHandleEvent(KeyEvent keyEvent, Model model) { }
}
