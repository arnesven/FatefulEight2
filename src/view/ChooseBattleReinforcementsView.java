package view;

import model.Model;
import model.states.battle.*;
import util.Arithmetics;
import util.MyLists;
import view.party.DrawableObject;
import view.party.SelectableListMenu;
import view.sprites.ArrowSprites;
import view.sprites.FilledBlockSprite;
import view.sprites.Sprite;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChooseBattleReinforcementsView extends SelectableListMenu {

    private static final int WIDTH = 29;
    private static final int HEIGHT = 40;
    private static final Sprite GREEN_BLOCK = new FilledBlockSprite(MyColors.GREEN);
    private static final int NUMBER_OF_PRIORITIES = 3;
    private final List<BattleUnit> units;
    private final Map<BattleUnit, Integer> unitCounts;
    private final Map<String, Integer> prioMap;
    private boolean priosOk = false;

    private Map<BattleUnit, Integer> makeEmptyMap(MyColors color) {
        Map<BattleUnit, Integer> result = new HashMap<>();
        result.put(new MilitiaUnit(0, "", color), 0);
        result.put(new ArchersUnit(0, "", color), 0);
        result.put(new PikemenUnit(0, "", color), 0);
        result.put(new SwordsmanUnit(0, "", color), 0);
        result.put(new KnightsUnit(0, "", color), 0);
        return result;
    }

    public ChooseBattleReinforcementsView(Model model, List<BattleUnit> battleUnits, MyColors unitColor) {
        super(model.getView(), WIDTH, HEIGHT);
        unitCounts = makeEmptyMap(unitColor);
        this.prioMap = new HashMap<>();
        for (BattleUnit bu : unitCounts.keySet()) {
            prioMap.put(bu.getName(), 0);
        }
        unitCounts.replaceAll((unit, v) -> MyLists.intAccumulate(battleUnits,
                (BattleUnit other) -> other.getName().equals(unit.getName()) ? other.getCount() : 0));
        this.units = battleUnits;
    }

    @Override
    public void transitionedFrom(Model model) { }

    @Override
    protected List<DrawableObject> buildDecorations(Model model, int xStart, int yStart) {
        return List.of(new DrawableObject(xStart+1, yStart+1) {
            @Override
            public void drawYourself(Model model, int x, int y) {
                x += 2;
                BorderFrame.drawCentered(model.getScreenHandler(), "REINFORCEMENTS", y, MyColors.WHITE, MyColors.BLUE);
                y += 2;

                BorderFrame.drawCentered(model.getScreenHandler(), "Select one unit type to", y++, MyColors.WHITE, MyColors.BLUE);
                BorderFrame.drawCentered(model.getScreenHandler(), "reinforce with high priority", y++, MyColors.WHITE, MyColors.BLUE);
                BorderFrame.drawCentered(model.getScreenHandler(), "and one with medium priority", y++, MyColors.WHITE, MyColors.BLUE);

                y += 2;
                for (BattleUnit bu : unitCounts.keySet()) {
                    BorderFrame.drawString(model.getScreenHandler(),
                            String.format("%-9s Now  Later", bu.getName()), x, y, MyColors.WHITE, MyColors.BLUE);

                    y++;
                    model.getScreenHandler().fillSpace(x, x+4, y, y+4, GREEN_BLOCK);
                    bu.drawYourself(model.getScreenHandler(), new Point(x, y), false, 1, 0);

                    int count = unitCounts.get(bu);
                    BorderFrame.drawString(model.getScreenHandler(), String.format("%2d", count),
                            x + 10, y, MyColors.WHITE, MyColors.BLUE);

                    model.getScreenHandler().put(x + 13, y, ArrowSprites.RIGHT);

                    int min = bu.getMinimumReinforcement()*(1 + prioMap.get(bu.getName()));
                    int max = bu.getMaximumReinforcement()*(1 + prioMap.get(bu.getName()));
                    BorderFrame.drawString(model.getScreenHandler(), String.format("%d-%d",
                            count + min, count + max),
                            x + 15, y, MyColors.WHITE, MyColors.BLUE);

                    y += 2;
                    BorderFrame.drawString(model.getScreenHandler(), "Priority:", x+5, y, MyColors.WHITE, MyColors.BLUE);
                    y += 3;
                }
            }
        });
    }

    @Override
    protected List<ListContent> buildContent(Model model, int xStart, int yStart) {
        List<ListContent> content = new ArrayList<>();

        int row = 0;
        for (BattleUnit bu : unitCounts.keySet()) {
            content.add(new CarouselListContent(xStart + 19, yStart + row * 6 + 11,
                    prioToText(prioMap.get(bu.getName()))) {
                @Override
                public void turnLeft(Model model) {
                    prioMap.replace(bu.getName(),
                            Arithmetics.decrementWithWrap(prioMap.get(bu.getName()), NUMBER_OF_PRIORITIES));
                }

                @Override
                public void turnRight(Model model) {
                    prioMap.replace(bu.getName(),
                            Arithmetics.incrementWithWrap(prioMap.get(bu.getName()), NUMBER_OF_PRIORITIES));
                }
            });
            row++;
        }

        content.add(new SelectableListContent(xStart + getWidth()/2 - 1, yStart + getHeight()-2, "DONE") {
            @Override
            public void performAction(Model model, int x, int y) {
                ChooseBattleReinforcementsView.this.setTimeToTransition(true);
            }

            @Override
            public boolean isEnabled(Model model) {
                return priosOk;
            }
        });
        return content;
    }

    private String prioToText(int prio) {
        if (prio == 0) {
            return "LOW";
        }
        if (prio == 1) {
            return "MEDIUM";
        }
        return "HIGH";
    }

    @Override
    protected void specificHandleEvent(KeyEvent keyEvent, Model model) {
        List<ListContent> content = buildContent(model, 0, 0);
        if (content.get(getSelectedRow()) instanceof CarouselListContent) {
            CarouselListContent carousel = (CarouselListContent) content.get(getSelectedRow());
            if (keyEvent.getKeyCode() == KeyEvent.VK_LEFT) {
                carousel.turnLeft(model);
                madeChanges();
                priosOk = checkPrios();
            } else if (keyEvent.getKeyCode() == KeyEvent.VK_RIGHT) {
                carousel.turnRight(model);
                madeChanges();
                priosOk = checkPrios();
            }
        }
    }

    private boolean checkPrios() {
        List<Integer> list = new ArrayList<>(prioMap.values());
        return MyLists.any(list, (Integer i) -> i == 2) &&
                MyLists.intAccumulate(list, (Integer i) -> i) == 3;
    }

    public Map<String, Integer> getPriorityMap() {
        return prioMap;
    }
}
