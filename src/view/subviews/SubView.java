package view.subviews;

import model.Model;
import util.MyPair;
import view.BorderFrame;
import view.DrawingArea;
import view.MyColors;
import view.sprites.FilledBlockSprite;
import view.sprites.RunOnceAnimationSprite;
import view.sprites.Sprite;
import view.widget.CenterText;
import view.widget.TitleText;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public abstract class SubView {
    public static final int Y_OFFSET = 4;
    public static final int Y_MAX = Y_OFFSET+38;
    public static final int X_OFFSET = BorderFrame.CHARACTER_WINDOW_COLUMNS + 1;
    public static final int X_MAX = DrawingArea.WINDOW_COLUMNS - BorderFrame.CHARACTER_WINDOW_COLUMNS - 1;
    protected static final Sprite blueBlock = new FilledBlockSprite(MyColors.BLUE);
    protected static final Sprite blackBlock = new FilledBlockSprite(MyColors.BLACK);
    private final Set<MyPair<Point, RunOnceAnimationSprite>> ongoingEffects = new HashSet<>();

    private TitleText topCenterText = new TitleText();
    private CenterText lowCenterText;

    public SubView(int centerTextRows) {
        lowCenterText = new CenterText(centerTextRows);
    }

    public SubView() {
        this(2);
    }

    public void drawYourself(Model model) {
        drawOverText(model);
        drawArea(model);
        drawEffects(model);
    }

    private synchronized void drawEffects(Model model) {
        List<MyPair<Point, RunOnceAnimationSprite>> toRemove = new ArrayList<>();
        for (MyPair<Point, RunOnceAnimationSprite> p : ongoingEffects) {
            if (!p.second.isDone()) {
                model.getScreenHandler().register(p.second.getName(), p.first, p.second, 2,
                        p.second.getXShift(), -p.second.getYShift());
            } else {
                toRemove.add(p);
            }
        }
        ongoingEffects.remove(toRemove);
    }

    protected void addOngoingEffect(MyPair<Point, RunOnceAnimationSprite> pair) {
        ongoingEffects.add(pair);
    }

    protected abstract void drawArea(Model model);

    private void drawOverText(Model model) {
        model.getScreenHandler().clearSpace(X_OFFSET, X_MAX, Y_OFFSET-2, Y_OFFSET-1);
        topCenterText.setText(getTitleText(model));
        topCenterText.drawYourself(model);
        lowCenterText.setContents(getUnderText(model));
        lowCenterText.drawYourself(model);
    }

    protected abstract String getUnderText(Model model);

    protected abstract String getTitleText(Model model);

    public boolean handleKeyEvent(KeyEvent keyEvent, Model model) { return false; }

    public int getCenterTextHeight() {
        return lowCenterText.getRows();
    }
}
