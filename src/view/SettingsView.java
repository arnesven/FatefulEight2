package view;

import control.FatefulEight;
import model.Model;
import model.SettingsManager;
import view.party.DrawableObject;
import view.party.SelectableListMenu;
import view.widget.TopText;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

public class SettingsView extends SelectableListMenu {
    private static final int WIDTH = 24;
    private static final int HEIGHT = 33;

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
        List<ListContent> result = new ArrayList<>();
        int y = yStart + 3;
        result.add(new ListContent(xStart+2, y, "Autosave " + (model.getSettings().autosaveEnabled()?"ON":"OFF")) {
            @Override
            public void performAction(Model model, int x, int y) {
                model.getSettings().toggleAutosave();
            }
        });

        y += 2;
        result.add(new ListContent(xStart+2, y, "Log Speed " + SettingsManager.logSpeedAsText(model.getSettings().getLogSpeed())) {
            @Override
            public void performAction(Model model, int x, int y) {
                model.getSettings().toggleLogSpeed();
            }
        });

        y += 1;
        result.add(new ListContent(xStart+2, y, "Combat Speed " + SettingsManager.logSpeedAsText(model.getSettings().getCombatLogSpeed())) {
            @Override
            public void performAction(Model model, int x, int y) {
                model.getSettings().toggleCombatLogSpeed();
            }
        });
        y += 1;
        result.add(new ListContent(xStart+2, y, "Movement Speed " + SettingsManager.logSpeedAsText(model.getSettings().getMovementSpeed())) {
            @Override
            public void performAction(Model model, int x, int y) {
                model.getSettings().toggleMovementSpeed();
            }
        });

        y += 2;
        result.add(new ListContent(xStart+2, y, "Tutorial " + (SettingsManager.tutorialEnabled(model)?"ON":"OFF")) {
            @Override
            public void performAction(Model model, int x, int y) {
                SettingsManager.toggleTutorial(model);
            }
        });

        y += 1;
        result.add(new ListContent(xStart+2, y, "Level Up Summary " + (model.getSettings().levelUpSummaryEnabled()?"ON":"OFF")) {
            @Override
            public void performAction(Model model, int x, int y) {
                model.getSettings().toggleLevelUpSummary();
            }
        });

        y += 1;
        result.add(new ListContent(xStart+2, y, "Always Ride " + (model.getSettings().alwaysRide()?"ON":"OFF")) {
            @Override
            public void performAction(Model model, int x, int y) {
                model.getSettings().toggleAlwaysRide();
            }
        });
        y += 1;
        result.add(new ListContent(xStart+2, y, "Game Difficulty " + (model.getSettings().getGameDifficultyString())) {
            @Override
            public void performAction(Model model, int x, int y) {
                model.getSettings().cycleGameDifficulty();
            }
        });
        y += 1;
        result.add(new ListContent(xStart+2, y, "Die Animations " + (model.getSettings().animateDieRollsEnabled()?"ON":"OFF")) {
            @Override
            public void performAction(Model model, int x, int y) {
                model.getSettings().toggleAnimateDieRolls();
            }
        });
//        y += 1;
//        result.add(new ListContent(xStart+2, y, "Hide Spells in Sell " + (model.getSettings().hideSpellsInSell()?"ON":"OFF")) {
//            @Override
//            public void performAction(Model model, int x, int y) {
//                model.getSettings().toggleHideSpellsInSell();
//            }
//        });

        y += 2;
        result.add(new ListContent(xStart+2, y++, "Top Bar Contents:"));
        int x = xStart+3;
        y = addTopBarSettings(model, result, x, y, "Time of Day   ", TopText.TIME_OF_DAY_SETTINGS_FLAG);
        y = addTopBarSettings(model, result, x, y, "Gold          ", TopText.GOLD_SETTINGS_FLAG);
        y = addTopBarSettings(model, result, x, y, "Obols         ", TopText.OBOLS_SETTINGS_FLAG);
        y = addTopBarSettings(model, result, x, y, "Food          ", TopText.FOOD_SETTINGS_FLAG);
        y = addTopBarSettings(model, result, x, y, "Weight        ", TopText.WEIGHT_SETTINGS_FLAG);
        y = addTopBarSettings(model, result, x, y, "Carrying Cap. ", TopText.CARRYING_CAPACITY_SETTINGS_FLAG);
        y = addTopBarSettings(model, result, x, y, "Horses        ", TopText.HORSE_SETTINGS_FLAG);
        y = addTopBarSettings(model, result, x, y, "Alignment     ", TopText.ALIGNMENT_SETTINGS_FLAG);
        y = addTopBarSettings(model, result, x, y, "Notoriety     ", TopText.NOTORIETY_SETTINGS_FLAG);
        y = addTopBarSettings(model, result, x, y, "Reputation    ", TopText.REPUTATION_SETTINGS_FLAG);
        y = addTopBarSettings(model, result, x, y, "Ingredients   ", TopText.INGREDIENTS_SETTINGS_FLAG);
        y = addTopBarSettings(model, result, x, y, "Materials     ", TopText.MATERIALS_SETTINGS_FLAG);
        y = addTopBarSettings(model, result, x, y, "Lockpicks     ", TopText.LOCKPICKS_SETTINGS_FLAG);
        y = addTopBarSettings(model, result, x, y, "Key Reminders ", TopText.KEY_REMINDERS_SETTINGS_FLAG);

        y += 1;
        result.add(new ListContent(xStart+2, y, "Fullscreen Mode " + (FatefulEight.inFullScreenMode?"ON":"OFF")) {
            @Override
            public void performAction(Model model, int x, int y) {
                model.toggleFullScreen();
            }
        });

        return result;
    }

    private int addTopBarSettings(Model model, List<ListContent> result, int xStart, int y, String label, String key) {
        result.add(new ListContent(xStart, y++, label +
                (model.getSettings().getMiscFlags().get(key)?"ON":"OFF")) {
            @Override
            public void performAction(Model model, int x, int y) {
                boolean val = model.getSettings().getMiscFlags().get(key);
                model.getSettings().getMiscFlags().put(key, !val);
            }
        });
        return y;
    }

    @Override
    protected void specificHandleEvent(KeyEvent keyEvent, Model model) {

    }

    @Override
    public void transitionedFrom(Model model) {

    }
}
