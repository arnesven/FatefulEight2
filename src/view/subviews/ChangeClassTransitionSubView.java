package view.subviews;

import model.Model;
import model.characters.GameCharacter;
import sound.SoundEffects;
import view.MyColors;
import view.sprites.*;

import java.awt.*;

public class ChangeClassTransitionSubView extends SubView implements Animation {
    private static final int DELAY = 2;
    private static final int STAGE_MAX = 6;
    private static final Sprite MID_SPRITE = new ChangeClassTransitionSprite("transitionmid", 0xEE);
    private static final Sprite LEFT_SPRITE = new ChangeClassTransitionSprite("transitionleft", 0xED);
    private static final Sprite RIGHT_SPRITE = new ChangeClassTransitionSprite("transitionright", 0xEF);
    private final SubView inner;
    private final GameCharacter futureCharacter;
    private final GameCharacter pastCharacter;
    private int steps = 0;
    private int stage = -1;

    public ChangeClassTransitionSubView(SubView inner, GameCharacter current, GameCharacter wouldBe) {
        this.inner = inner;
        this.futureCharacter = wouldBe;
        this.pastCharacter = current;
        AnimationManager.register(this);
    }


    @Override
    protected void drawArea(Model model) {
        inner.drawArea(model);
        Point p = model.getParty().getLocationForPartyMember(model.getParty().getPartyMembers().indexOf(pastCharacter));
        p.y = p.y + 3;
        futureCharacter.getAppearance().drawYourself(model.getScreenHandler(), p.x, p.y, STAGE_MAX-stage, STAGE_MAX);
        pastCharacter.getAppearance().drawYourself(model.getScreenHandler(), p.x, p.y, 0, STAGE_MAX-stage-1);

        model.getScreenHandler().put(p.x, p.y+STAGE_MAX-stage, LEFT_SPRITE);
        for (int x = 1; x < 6; ++x) {
            model.getScreenHandler().put(p.x + x, p.y+STAGE_MAX-stage, MID_SPRITE);
        }
        model.getScreenHandler().put(p.x + 6, p.y+STAGE_MAX-stage, RIGHT_SPRITE);

    }

    @Override
    protected String getUnderText(Model model) {
        return inner.getUnderText(model);
    }

    @Override
    protected String getTitleText(Model model) {
        return inner.getTitleText(model);
    }

    @Override
    public void stepAnimation(long elapsedTimeMs, Model model) {
        if (!animationDone()) {
            this.steps++;
            if (steps % DELAY == 0) {
                stage++;
            }
        }
    }

    @Override
    public void synch() {

    }

    public boolean animationDone() {
        return stage == STAGE_MAX;
    }

    public static void transition(Model model, SubView subView, GameCharacter gc, GameCharacter wouldBe) {
        ChangeClassTransitionSubView transition = new ChangeClassTransitionSubView(subView, gc, wouldBe);
        model.setSubView(transition);
        SoundEffects.playSound("classchange");
        while (!transition.animationDone()) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        model.setSubView(subView);
        AnimationManager.unregister(transition);
    }

    private static class ChangeClassTransitionSprite extends Sprite8x8 {
        public ChangeClassTransitionSprite(String name, int i) {
            super(name, "clothes.png", i);
            setColor1(MyColors.WHITE);
            setColor2(MyColors.GRAY);
        }
    }
}
