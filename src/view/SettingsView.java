package view;

import model.Model;
import model.SettingsManager;
import view.party.SelectableListMenu;

import java.awt.event.KeyEvent;
import java.util.List;

public class SettingsView extends SelectableListMenu {
    private static final int WIDTH = 24;
    private static final int HEIGHT = 16;

    public SettingsView(GameView previous) {
        super(previous, WIDTH, HEIGHT);
    }

    @Override
    public void transitionedTo(Model model) {
        super.transitionedTo(model);
        internalUpdate(model);
    }

    @Override
    protected List<DrawableObject> buildDecorations(Model model, int xStart, int yStart) {
        return List.of(new DrawableObject(xStart, yStart+1) {
            @Override
            public void drawYourself(Model model, int x, int y) {
                print(model.getScreenHandler(), x+6, y, "- Settings -");
            }
        });
    }

    @Override
    protected List<ListContent> buildContent(Model model, int xStart, int yStart) {
        return List.of(
            new ListContent(xStart+2, yStart+3, "Autosave " + (model.getSettings().autosaveEnabled()?"ON":"OFF")) {
                @Override
                public void performAction(Model model, int x, int y) {
                    model.getSettings().toggleAutosave();
                }
            },
            new ListContent(xStart+2, yStart+5, "Log Speed " + SettingsManager.logSpeedAsText(model.getSettings().getLogSpeed())) {
                @Override
                public void performAction(Model model, int x, int y) {
                    model.getSettings().toggleLogSpeed();
                }
            },
            new ListContent(xStart+2, yStart+6, "Combat Speed " + SettingsManager.logSpeedAsText(model.getSettings().getCombatLogSpeed())) {
                @Override
                public void performAction(Model model, int x, int y) {
                    model.getSettings().toggleCombatLogSpeed();
                }
            },
            new ListContent(xStart+2, yStart+8, "Tutorial " + (SettingsManager.tutorialEnabled(model)?"ON":"OFF")) {
                @Override
                public void performAction(Model model, int x, int y) {
                    SettingsManager.toggleTutorial(model);
                }
            },
            new ListContent(xStart+2, yStart+10, "Level Up Summary " + (model.getSettings().levelUpSummaryEnabled()?"ON":"OFF")) {
                @Override
                public void performAction(Model model, int x, int y) {
                    model.getSettings().toggleLevelUpSummary();
                }
            }
            );
    }

    @Override
    protected void specificHandleEvent(KeyEvent keyEvent, Model model) {

    }

    @Override
    public void transitionedFrom(Model model) {

    }
}