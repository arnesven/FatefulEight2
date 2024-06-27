package model.states.battle;

import model.Model;
import view.sprites.QuestCursorSprite;
import view.sprites.Sprite;

import java.awt.*;
import java.awt.event.KeyEvent;

public abstract class BattleAction {

    public static final Sprite CURRENT_MARKER = new QuestCursorSprite();
    private final BattleUnit performer;
    private final String name;
    private boolean noPrompt = false;

    public BattleAction(String name, BattleUnit unit) {
        this.name = name;
        this.performer = unit;
    }

    public String getName() {
        return name;
    }

    public BattleUnit getPerformer() {
        return performer;
    }

    public abstract void execute(Model model, BattleState battleState, BattleUnit performer);

    public abstract boolean handleKeyEvent(KeyEvent keyEvent, Model model, BattleState state);

    public void drawUnit(Model model, BattleState state, boolean withMp, Point p) {
        performer.drawYourself(model.getScreenHandler(), p, withMp, 3);
        drawMarker(model, p);
    }

    protected void drawMarker(Model model, Point p) {
        model.getScreenHandler().register(CURRENT_MARKER.getName(), new Point(p.x, p.y), CURRENT_MARKER);
    }

    public boolean isNoPrompt() {
        return noPrompt;
    }

    public void setNoPrompt(boolean b) {
        noPrompt = b;
    }
}
