package view;

import model.Model;
import util.MyStrings;
import view.party.DrawableObject;
import view.party.SelectableListMenu;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

public class SelectGameDifficultyView extends SelectableListMenu {
    private static final int WIDTH = 31;
    private static final int HEIGHT = 16;
    private static final String INTRO_TEXT = "The difficulty of the game can be changed at any time in the settings menu.";

    public SelectGameDifficultyView(Model model) {
        super(model.getView(), WIDTH, HEIGHT);
    }

    @Override
    public void transitionedFrom(Model model) { }

    @Override
    public void transitionedTo(Model model) {
        super.transitionedTo(model);
        setSelectedRow(1);
    }

    @Override
    protected List<DrawableObject> buildDecorations(Model model, int xStart, int yStart) {
        List<DrawableObject> decorations = new ArrayList<>();
        decorations.add(new TextDecoration("Game Difficulty", xStart, yStart + 1, MyColors.WHITE, MyColors.BLUE, true));
        addCenteredText(decorations, INTRO_TEXT, yStart+3);
        addCenteredText(decorations, getDifficultyDescription(getSelectedRow()), yStart+12);
        return decorations;
    }

    private void addCenteredText(List<DrawableObject> decorations, String introText, int row) {
        String[] parts = MyStrings.partitionWithLineBreaks(introText, WIDTH-1);
        for (int i = 0 ; i < parts.length; ++i) {
            decorations.add(new TextDecoration(parts[i], 0, row+i, MyColors.WHITE, MyColors.BLUE, true));
        }
    }

    private String getDifficultyDescription(int selectedRow) {
        switch (selectedRow) {
        case 0:
            return "Skill checks and combat events are slightly easier.";
        case 1:
            return "No adjustment.";
        case 2:
            return "Tasks and combat events are slightly harder.";
        }
        throw new IllegalStateException("Illegal difficulty: " + selectedRow);
    }

    @Override
    protected List<ListContent> buildContent(Model model, int xStart, int yStart) {
        return List.of(new SelectableListContent(xStart + WIDTH/2-2, yStart + 7, "EASY") {
                    @Override
                    public void performAction(Model model, int x, int y) {
                        setTimeToTransition(true);
                        model.getSettings().setGameDifficulty(getSelectedRow());
                    }
                },
                new SelectableListContent(xStart + WIDTH/2-3, yStart + 8, "NORMAL") {
                    @Override
                    public void performAction(Model model, int x, int y) {
                        setTimeToTransition(true);
                        model.getSettings().setGameDifficulty(getSelectedRow());
                    }
                },
                new SelectableListContent(xStart + WIDTH/2-2, yStart + 9, "HARD") {
                    @Override
                    public void performAction(Model model, int x, int y) {
                        setTimeToTransition(true);
                        model.getSettings().setGameDifficulty(getSelectedRow());
                    }
                });
    }

    @Override
    protected void specificHandleEvent(KeyEvent keyEvent, Model model) {

    }
}
