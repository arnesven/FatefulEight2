package view;

import model.Model;
import model.characters.GameCharacter;
import model.classes.Skill;
import util.BeforeAndAfterLine;
import view.party.DrawableObject;
import view.party.SelectableListMenu;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

public class LevelUpSummaryView extends SelectableListMenu {
    private static final int DIALOG_WIDTH = 27;
    private static final int DIALOG_HEIGHT = 30;
    private final GameCharacter levler;
    private final GameCharacter willBe;
    private final ArrayList<BeforeAndAfterLine<Integer>> content;

    public LevelUpSummaryView(Model model, GameCharacter gc) {
        super(model.getView(), DIALOG_WIDTH, DIALOG_HEIGHT);
        this.levler = gc;
        this.willBe = gc.copy();
        this.willBe.setLevel(levler.getLevel()+1);
        this.content = new ArrayList<>();
        this.content.add(new BeforeAndAfterLine<>("Health Points", levler.getMaxHP(), willBe.getMaxHP()));
        this.content.add(new BeforeAndAfterLine<>("", 0, 0));
        for (Skill s : Skill.values()) {
            if (willBe.getRankForSkill(s) != levler.getRankForSkill(s)) {
                this.content.add(new BeforeAndAfterLine<>(s.getName(), levler.getRankForSkill(s), willBe.getRankForSkill(s)));
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
        objs.add(new DrawableObject(xStart, yStart+2) {
            @Override
            public void drawYourself(Model model, int x, int y) {
                levler.getAppearance().drawYourself(model.getScreenHandler(), 36, y);
            }
        });
        yStart += 9;
        objs.add(new TextDecoration("Leveled up to level " + (willBe.getLevel()) + "!", xStart+7, ++yStart,
                MyColors.WHITE, MyColors.BLUE, true));
        yStart += 2;
        for (BeforeAndAfterLine<Integer> line : content) {
            if (!line.isEmpty()) {
                String text = String.format("%-14s %2d  " + ((char) 0xB0) + " %2d", line.getLabel(), line.getBefore(), line.getAfter());
                objs.add(new TextDecoration(text,
                        xStart+2, yStart++, MyColors.WHITE, MyColors.BLUE, false));
            } else {
                yStart++;
            }
        }
        return objs;
    }

    @Override
    protected List<ListContent> buildContent(Model model, int xStart, int yStart) {
        List<ListContent> list = new ArrayList<>();
        list.add(makeOkButton(model, xStart+getWidth()/2-1, yStart+getHeight()-2, this));
        return list;
    }

    @Override
    protected void specificHandleEvent(KeyEvent keyEvent, Model model) {
        if (keyEvent.getKeyCode() == KeyEvent.VK_ENTER) {
            setTimeToTransition(true);
        }
    }

}
