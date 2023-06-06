package view;

import model.Model;
import model.characters.GameCharacter;
import model.classes.Skill;
import view.party.SelectableListMenu;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

public class LevelUpSummaryView extends SelectableListMenu {
    private static final int DIALOG_WIDTH = 29;
    private static final int DIALOG_HEIGHT = 26;
    private final GameCharacter levler;
    private final GameCharacter willBe;
    private final ArrayList<LevelUpLine> content;

    public LevelUpSummaryView(Model model, GameCharacter gc) {
        super(model.getView(), DIALOG_WIDTH, DIALOG_HEIGHT);
        this.levler = gc;
        this.willBe = gc.copy();
        this.willBe.setLevel(levler.getLevel()+1);
        this.content = new ArrayList<LevelUpLine>();
        this.content.add(new LevelUpLine("Health Points", levler.getMaxHP(), willBe.getMaxHP()));
        this.content.add(new LevelUpLine("", 0, 0));
        for (Skill s : Skill.values()) {
            if (willBe.getRankForSkill(s) != levler.getRankForSkill(s)) {
                this.content.add(new LevelUpLine(s.getName(), levler.getRankForSkill(s), willBe.getRankForSkill(s)));
            }
        }
    }

    @Override
    public void transitionedFrom(Model model) {

    }

    @Override
    protected List<DrawableObject> buildDecorations(Model model, int xStart, int yStart) {
        List<DrawableObject> objs = new ArrayList<>();
        objs.add(new TextDecoration(levler.getName(), xStart+2, ++yStart,
                MyColors.WHITE, MyColors.BLUE, true));
        objs.add(new DrawableObject(xStart+1, yStart+2) {
            @Override
            public void drawYourself(Model model, int x, int y) {
                levler.getAppearance().drawYourself(model.getScreenHandler(), 36, y);
            }
        });
        yStart += 9;
        objs.add(new TextDecoration("Leveled up to level " + (willBe.getLevel()) + "!", xStart+7, ++yStart,
                MyColors.WHITE, MyColors.BLUE, true));
        yStart += 2;
        for (LevelUpLine line : content) {
            if (!line.label.equals("")) {
                objs.add(new TextDecoration(String.format("%-14s %2d  " + ((char) 0xB0) + " %2d", line.label, line.before, line.after),
                        xStart+3, yStart++, MyColors.WHITE, MyColors.BLUE, false));
            } else {
                yStart++;
            }
        }
        return objs;
    }

    @Override
    protected List<ListContent> buildContent(Model model, int xStart, int yStart) {
        List<ListContent> list = new ArrayList<>();
        list.add(new SelectableListContent(xStart+getWidth()/2-1, yStart+getHeight()-2, "OK") {
            @Override
            public void performAction(Model model, int x, int y) {
                setTimeToTransition(true);
            }
        });
        return list;
    }

    @Override
    protected void specificHandleEvent(KeyEvent keyEvent, Model model) {
        if (keyEvent.getKeyCode() == KeyEvent.VK_ENTER) {
            setTimeToTransition(true);
        }
    }

    private static class LevelUpLine {
        public final String label;
        public final int before;
        public final int after;

        public LevelUpLine(String label, int before, int after) {
            this.label = label;
            this.before = before;
            this.after = after;
        }
    }
}
