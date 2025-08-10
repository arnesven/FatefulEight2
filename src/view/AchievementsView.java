package view;

import model.Model;
import model.achievements.Achievement;
import util.MyStrings;
import view.party.DrawableObject;
import view.party.SelectableListMenu;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

public class AchievementsView extends SelectableListMenu {
    private static final int COLUMN_RIGHT = 30;

    public AchievementsView(Model model) {
        super(model.getView(), StatisticsView.WIDTH, StatisticsView.HEIGHT);
    }

    @Override
    protected List<DrawableObject> buildDecorations(Model model, int xStart, int yStart) {
        return List.of(new DrawableObject(xStart + 1, yStart + 1) {
            @Override
            public void drawYourself(Model model, int x, int y) {
                BorderFrame.drawCentered(model.getScreenHandler(), "Achievements", y, MyColors.WHITE, MyColors.BLUE);
            }
        });
    }

    @Override
    protected List<ListContent> buildContent(Model model, int xStart, int yStart) {
        List<ListContent> contents = new ArrayList<>();
        int row = 0;
        int count = 0;
        for (Achievement a : model.getAchievements().getAsList()) {
            int xExtra = (count % 2) * COLUMN_RIGHT;
            int finalX = xStart + 2 + xExtra;
            int finalY = 3 + yStart + row * 2;
            if (a.isCompleted()) {
                contents.add(new SelectableListContent(finalX, finalY, (char) 0xB6 + a.getName()) {
                    @Override
                    public void performAction(Model model, int x, int y) {
                        setInnerMenu(new AchievementDescriptionView(AchievementsView.this, a.getDescription()),
                                 model);
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
            if (keyEvent.getKeyCode() == KeyEvent.VK_ESCAPE) {
                super.handleKeyEvent(keyEvent, model);
            }
        }

        @Override
        public void transitionedFrom(Model model) {

        }
    }
}
