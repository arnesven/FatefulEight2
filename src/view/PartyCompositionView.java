package view;

import model.Model;
import model.characters.GameCharacter;
import model.classes.CharacterClass;
import model.classes.Classes;
import model.races.AllRaces;
import model.races.ElvenRace;
import model.races.Race;
import util.Arithmetics;
import util.MyLists;
import util.MyTriplet;
import view.party.DrawableObject;
import view.party.SelectableListMenu;
import view.sprites.PieChartSprite;
import view.widget.*;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.*;
import java.util.List;

public class PartyCompositionView extends SelectableListMenu {
    private static final String OTHER_RACE = "Other";
    private static final int PIE_CHART_SIZE = 16;

    private PieChartWidget<Race> racePieChart;
    private PieChartWidget<CharacterClass> classPieChart;

    private final RaceStrategy pieChartStrategyShow = new RaceStrategy(makeRaceColors(), Race::getQualifiedName);
    private final RaceStrategy pieChartStrategyHide = new RaceStrategy(makeBasicRaceColors(), PartyCompositionView::getBasicName);
    private RaceStrategy currentPieChartStrategy = pieChartStrategyHide;

    private final ClassStrategy[] classStrategies = new ClassStrategy[]{new ClassicClassNameStrategy(),
                                                                        new DetailedClassNameStrategy(),
                                                                        new AlignmentClassStrategy()};

    private int currentClassStrategyIndex = 0;

    public PartyCompositionView(Model model) {
        super(model.getView(), DrawingArea.WINDOW_COLUMNS-10, StatisticsView.HEIGHT);
        racePieChart = new PieChartWidget<>(model, currentPieChartStrategy, PIE_CHART_SIZE);
        classPieChart = new PieChartWidget<>(model, getCurrentClassStrategy(), PIE_CHART_SIZE);
    }

    private ClassStrategy getCurrentClassStrategy() {
        return classStrategies[currentClassStrategyIndex];
    }

    @Override
    public void transitionedFrom(Model model) { }

    private static String getBasicName(Race race) {
        if (race instanceof ElvenRace) {
            return "Elf";
        }
        return race.getName();
    }

    @Override
    protected List<DrawableObject> buildDecorations(Model model, int xStart, int yStart) {
        return List.of(
                new TextDecoration("Class types    ", xStart + 2, yStart + getHeight() - 6, MyColors.WHITE, MyColors.BLUE, false),
                new TextDecoration("Elf/Human types", xStart + 2, yStart + getHeight() - 4, MyColors.WHITE, MyColors.BLUE, false),
                new DrawableObject(xStart + 1, yStart + 1) {
                    @Override
                    public void drawYourself(Model model, int x, int y) {
                        BorderFrame.drawCentered(model.getScreenHandler(), "Party Race/Class Breakdown", y, MyColors.WHITE, MyColors.BLUE);
                    }
                },
                new DrawableObject(xStart + 10, yStart + 6) {
                    @Override
                    public void drawYourself(Model model, int x, int y) {
                        racePieChart.drawYourself(model, x, y);
                    }
                },
                new DrawableObject(xStart + getWidth() - 12 - PIE_CHART_SIZE, yStart + 6) {
                    @Override
                    public void drawYourself(Model model, int x, int y) {
                        classPieChart.drawYourself(model, x, y);
                    }
                },
                new DrawableObject(xStart + 27,
                        yStart + getHeight() - getCurrentClassStrategy().getDescription().size() - 3) {
                    @Override
                    public void drawYourself(Model model, int x, int y) {
                        int row = y;
                        for (String s : getCurrentClassStrategy().getDescription()) {
                            BorderFrame.drawString(model.getScreenHandler(), s, x, row++, MyColors.WHITE, MyColors.BLUE);
                        }
                    }
                });
    }

    @Override
    protected List<ListContent> buildContent(Model model, int xStart, int yStart) {
        return List.of(new SelectableListContent(xStart + 18, yStart + getHeight() - 6,
                getCurrentClassStrategy().getName().toUpperCase()) {
            @Override
            public void performAction(Model model, int x, int y) {
                currentClassStrategyIndex = Arithmetics.incrementWithWrap(currentClassStrategyIndex, classStrategies.length);
                classPieChart = new PieChartWidget<>(model, getCurrentClassStrategy(), PIE_CHART_SIZE);
                madeChanges();
            }
        },
            new SelectableListContent(xStart + 18, yStart + getHeight() - 4,
                (currentPieChartStrategy == pieChartStrategyShow ? "SHOW" : "HIDE")) {
            @Override
            public void performAction(Model model, int x, int y) {
                if (currentPieChartStrategy == pieChartStrategyShow) {
                    currentPieChartStrategy = pieChartStrategyHide;
                } else {
                    currentPieChartStrategy = pieChartStrategyShow;
                }
                racePieChart = new PieChartWidget<>(model, currentPieChartStrategy, PIE_CHART_SIZE);
                madeChanges();
            }
        });
    }

    @Override
    protected void specificHandleEvent(KeyEvent keyEvent, Model model) { }

    private static Map<String, MyColors> makeRaceColors() {
        return Map.of(
                AllRaces.WOOD_ELF.getQualifiedName(),       MyColors.LIGHT_BLUE,
                AllRaces.HIGH_ELF.getQualifiedName(),       MyColors.CYAN,
                AllRaces.DARK_ELF.getQualifiedName(),       MyColors.PURPLE,
                AllRaces.HALF_ORC.getQualifiedName(),       MyColors.GREEN,
                AllRaces.DWARF.getQualifiedName(),          MyColors.BEIGE,
                AllRaces.HALFLING.getQualifiedName(),       MyColors.ORANGE,
                AllRaces.NORTHERN_HUMAN.getQualifiedName(), MyColors.LIGHT_RED,
                AllRaces.SOUTHERN_HUMAN.getQualifiedName(), MyColors.RED,
                OTHER_RACE,                                MyColors.BLACK);
    }

    private static Map<String, MyColors> makeBasicRaceColors() {
        return Map.of(
                "Elf",                                  MyColors.LIGHT_BLUE,
                AllRaces.HALF_ORC.getQualifiedName(),       MyColors.GREEN,
                AllRaces.DWARF.getQualifiedName(),          MyColors.BEIGE,
                AllRaces.HALFLING.getQualifiedName(),       MyColors.ORANGE,
                "Human",                                MyColors.RED,
                OTHER_RACE,                                MyColors.BLACK);
    }


}
