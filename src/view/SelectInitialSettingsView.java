package view;

import model.Model;
import model.map.WorldBuilder;
import util.Arithmetics;
import util.MyStrings;
import view.party.DrawableObject;
import view.party.SelectableListMenu;
import view.subviews.NoAvatarMapSubView;
import view.subviews.SubView;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

public class SelectInitialSettingsView extends SelectableListMenu {
    private static final int WIDTH = 31;
    private static final int HEIGHT = 23;
    private static final String DIFFICULTY_INTRO_TEXT = "Game Difficulty\n(can be changed at any time in the settings menu)";
    private static final String LOCATION_INTRO_TEXT = "Starting Location";
    private static final String[] DIFFICULTIES = new String[]{"EASY", "NORMAL", "HARD", "IMPOSSIBLE"};
    private static final String[] LOCATIONS = new String[]{"Crossroads Inn", "Waterfront Inn", "Hunter's Inn"};
    private static final int DIFFICULTY_START_Y = 2;
    private static final int LOCATION_START_Y = 13;
    private final SubView[] subViews;
    private int selectedDifficulty = 1;
    private int selectedLocation = 0;

    public SelectInitialSettingsView(Model model) {
        super(model.getView(), WIDTH, HEIGHT);
        this.subViews = new SubView[]{new NoAvatarMapSubView(getPositionForSelected(0), 10),
                                      new NoAvatarMapSubView(getPositionForSelected(1), 10),
                                      new NoAvatarMapSubView(getPositionForSelected(2), 5)};
    }

    @Override
    public void transitionedFrom(Model model) {
        model.getSettings().setGameDifficulty(selectedDifficulty);
        model.setStartingPosition(getPositionForSelected(selectedLocation));
    }

    @Override
    protected int getYStart() {
        return super.getYStart() + 6;
    }

    private Point getPositionForSelected(int selectedLocation) {
        if (selectedLocation == 0) {
            return WorldBuilder.CROSSROADS_INN_POSITION;
        }
        if (selectedLocation == 1) {
            return WorldBuilder.WATERFRONT_INN_POISITION;
        }
        return WorldBuilder.HUNTERS_INN_POSITION;
    }

    @Override
    public void transitionedTo(Model model) {
        super.transitionedTo(model);
        setSelectedRow(0);
        model.setSubView(subViews[selectedLocation]);
    }

    @Override
    protected List<DrawableObject> buildDecorations(Model model, int xStart, int yStart) {
        List<DrawableObject> decorations = new ArrayList<>();
        addCenteredText(decorations, DIFFICULTY_INTRO_TEXT, yStart+DIFFICULTY_START_Y);
        addCenteredText(decorations, getDifficultyDescription(selectedDifficulty), yStart+DIFFICULTY_START_Y + 6);
        addCenteredText(decorations, LOCATION_INTRO_TEXT, yStart + LOCATION_START_Y);
        addCenteredText(decorations, getLocationDescription(selectedLocation), yStart+LOCATION_START_Y+4);
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
            return "Skill checks and combat events are slightly harder.";
        case 3:
            return "Skill checks and combat events are much harder. Autosaving disabled.";
        }
        throw new IllegalStateException("Illegal difficulty: " + selectedRow);
    }

    private String getLocationDescription(int selectedLoc) {
        switch (selectedLoc) {
            case 0:
                return "You start at the center of the realm, where four kingdoms meet (recommended).";
            case 1:
                return "You start in the north, in the Kingdom of Arkvale.";
            case 2:
                return "You start in the south, in the Kingdom of Sunblaze.";
        }
        throw new IllegalStateException("Illegal starting location: " + selectedLoc);
    }

    @Override
    protected List<ListContent> buildContent(Model model, int xStart, int yStart) {
        List<ListContent> content = new ArrayList<>();
        {
            String label = DIFFICULTIES[selectedDifficulty];
            content.add(new CarouselListContent(xStart + WIDTH / 2 - label.length() / 2, yStart + DIFFICULTY_START_Y + 4, label) {
                @Override
                public void turnLeft(Model model) {
                    selectedDifficulty = Arithmetics.decrementWithWrap(selectedDifficulty, DIFFICULTIES.length);
                }

                @Override
                public void turnRight(Model model) {
                    selectedDifficulty = Arithmetics.incrementWithWrap(selectedDifficulty, DIFFICULTIES.length);
                }
            });
        }
        {
            String label = LOCATIONS[selectedLocation];
            content.add(new CarouselListContent(xStart + WIDTH / 2 - label.length() / 2, yStart + LOCATION_START_Y + 2, label) {
                @Override
                public void turnLeft(Model model) {
                    selectedLocation = Arithmetics.decrementWithWrap(selectedLocation, LOCATIONS.length);
                    model.setSubView(subViews[selectedLocation]);
                }

                @Override
                public void turnRight(Model model) {
                    selectedLocation = Arithmetics.incrementWithWrap(selectedLocation, LOCATIONS.length);
                    model.setSubView(subViews[selectedLocation]);
                }
            });
        }

        content.add(makeOkButton(model, xStart + WIDTH/2 - 1, yStart + HEIGHT - 2, this));
        return content;
    }

    @Override
    protected void specificHandleEvent(KeyEvent keyEvent, Model model) {
        super.handleCarousels(keyEvent, model);
    }
}
