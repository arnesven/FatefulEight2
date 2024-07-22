package view.subviews;

import model.Model;
import util.MyPair;
import util.MyRandom;
import view.BorderFrame;
import view.DrawingArea;
import view.MyColors;
import view.sprites.*;
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
                AnimationManager.unregister(p.second);
            }
        }
        toRemove.forEach(ongoingEffects::remove);
    }

    protected synchronized void addOngoingEffect(MyPair<Point, RunOnceAnimationSprite> pair) {
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

    protected synchronized void addFloatyDamage(Point point, int damge, MyColors color) {
        point.x += MyRandom.randInt(-1, 1);
        point.y += MyRandom.randInt(-1, 1);
        if (damge > 15) {
            Point left = new Point(point);
            left.x -= 1;
            addOngoingEffect(new MyPair<>(left, new DamageValueEffect(damge/10, color)));
            addOngoingEffect(new MyPair<>(point, new DamageValueEffect(damge, color)));
        } else {
            addOngoingEffect(new MyPair<>(point, new DamageValueEffect(damge, color)));
        }
    }

    protected synchronized void addFloatyText(Point point, int strikeTextEffect) {
        int mapOffset = 0xF0 + strikeTextEffect*3;
        for (int x = 0; x < 3; ++x) {
            addOngoingEffect(new MyPair<>(new Point(point.x + x - 1, point.y),
                    new DamageValueEffect(mapOffset + x)));
        }
    }
}
