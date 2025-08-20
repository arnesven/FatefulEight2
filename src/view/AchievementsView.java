package view;

import model.Model;
import model.achievements.Achievement;
import model.characters.GameCharacter;
import util.Arithmetics;
import util.MyStrings;
import view.party.DrawableObject;
import view.party.SelectableListMenu;

import java.awt.event.KeyEvent;
import java.util.*;

public class AchievementsView extends SelectableListMenu {
    private static final int COLUMN_RIGHT = 30;
    private List<Achievement> achievements;
    private int sortType = 0;
    private static final int NO_OF_SORT_TYPES = 3;

    public AchievementsView(GameView previous) {
        super(previous, StatisticsView.WIDTH, StatisticsView.HEIGHT-2);
        setScrollArrowsEnabled(true);
    }

    @Override
    public void transitionedTo(Model model) {
        super.transitionedTo(model);
        this.achievements = model.getAchievements().getAsList();
        sortByAlpha();
    }

    @Override
    public void drawYourself(Model model) {
        super.drawYourself(model);
        int xStart = getXStart();
        int yStart = getYStart()-2;
        clearPreviousForeground(model, xStart, yStart);
        BorderFrame.drawFrame(model.getScreenHandler(),
                xStart, yStart, getWidth(), 2,
                MyColors.BLACK, MyColors.WHITE, MyColors.BLUE, true);
        BorderFrame.drawString(model.getScreenHandler(), "ACHIEVEMENTS (" +
                        model.getAchievements().numberOfCompleted(model) + "/" +
                        model.getAchievements().getTotal() + ")", xStart+1, yStart+1,
                MyColors.WHITE, MyColors.BLUE);

        BorderFrame.drawString(model.getScreenHandler(), "Sort(F3)=" + getSortTypeString(sortType), xStart+42, yStart+1,
                MyColors.WHITE, MyColors.BLUE);
    }

    private String getSortTypeString(int sortType) {
        if (sortType == 0) {
            return "All";
        }
        if (sortType == 1) {
            return "Type";
        }
        return "Completed";
    }

    @Override
    protected List<DrawableObject> buildDecorations(Model model, int xStart, int yStart) {
        return List.of();
    }

    @Override
    protected List<ListContent> buildContent(Model model, int xStart, int yStart) {
        List<ListContent> contents = new ArrayList<>();
        int row = 0;
        int count = 0;
        for (Achievement a : achievements) {
            int xExtra = (count % 2) * COLUMN_RIGHT;
            int finalX = xStart + 2 + xExtra;
            int finalY = 1 + yStart + row * 2;
            if (a.isCompleted(model)) {
                contents.add(new SelectableListContent(finalX, finalY, (char) 0xB6 + a.getName()) {
                    @Override
                    public void performAction(Model model, int x, int y) {
                        setInnerMenu(new AchievementDescriptionView(AchievementsView.this, a.getDescription()),
                                 model);
                    }

                    @Override
                    public boolean isEnabled(Model model) {
                        return true;
                    }
                });
            } else {
                contents.add(new ListContent(finalX, finalY, (char)0xBC + a.getName()));
            }
            count++;
            if (count % 2 == 0) {
                row++;
            }
        }
        return contents;
    }

    @Override
    protected void specificHandleEvent(KeyEvent keyEvent, Model model) {
        if (keyEvent.getKeyCode() == KeyEvent.VK_F3) {
            sortType = Arithmetics.incrementWithWrap(sortType, NO_OF_SORT_TYPES);
            switch (sortType) {
                case 0:
                    sortByAlpha();
                    break;
                case 1:
                    sortByType();
                    break;
                default:
                    sortByCompleted(model);
                    break;
            }
            setSelectedRow(0);
            checkForSelectedRowReset(model);
            madeChanges();
        }
    }

    @Override
    public void transitionedFrom(Model model) {

    }

    private class AchievementDescriptionView extends SelectableListMenu {
        private static final int WIDTH = 35;
        private final String[] text;

        public AchievementDescriptionView(GameView previous, String description) {
            super(previous, WIDTH, 3 + MyStrings.partition(description, WIDTH).length);
            this.text = MyStrings.partition(description, WIDTH);
        }

        @Override
        protected List<DrawableObject> buildDecorations(Model model, int xStart, int yStart) {
            return List.of(new DrawableObject(xStart, yStart+1) {
                @Override
                public void drawYourself(Model model, int x, int y) {
                    for (String s : text) {
                        BorderFrame.drawCentered(model.getScreenHandler(), s, ++y,
                                MyColors.WHITE, MyColors.BLUE);
                    }
                }
            });
        }

        @Override
        protected List<ListContent> buildContent(Model model, int xStart, int yStart) {
            return List.of();
        }

        @Override
        protected void specificHandleEvent(KeyEvent keyEvent, Model model) {}

        @Override
        public void handleKeyEvent(KeyEvent keyEvent, Model model) {
            setTimeToTransition(true);
        }

        @Override
        public void transitionedFrom(Model model) {

        }
    }

    private void sortByAlpha() {
        this.achievements.sort(Comparator.comparing(Achievement::getName));
    }

    private void sortByType() {
        this.achievements.sort((o1, o2) -> {
            if (o1.getType().equals(o2.getType())) {
                return o1.getName().compareTo(o2.getName());
            }
            return o1.getType().compareTo(o2.getType());
        });
    }

    private void sortByCompleted(Model model) {
       Map<Achievement, Boolean> isCompleted = new HashMap<>();
       for (Achievement a : achievements) {
           isCompleted.put(a, a.isCompleted(model));
       }

       this.achievements.sort((o1, o2) -> {
           if (isCompleted.get(o1) == isCompleted.get(o2)) {
               return o1.getName().compareTo(o2.getName());
           }
           return (isCompleted.get(o2) ? 1 : 0) - (isCompleted.get(o1) ? 1 : 0);
       });
    }
}
