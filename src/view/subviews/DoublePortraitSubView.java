package view.subviews;

import model.Model;
import model.PortraitAnimations;
import model.characters.appearance.CharacterAppearance;
import model.states.GameState;
import util.MyPair;
import view.BorderFrame;
import view.MyColors;
import view.sprites.CalloutSprite;

import java.awt.*;

public class DoublePortraitSubView extends SubView {

    public static final int PORTRAIT_FRAME_HEIGHT = 13;
    public static final int PORTRAIT_FRAME_WIDTH = 16;
    private final SubView previous;
    private final PortraitAnimations portraitAnimations = new PortraitAnimations();
    private final CharacterAppearance leftApp;
    private final CharacterAppearance rightApp;
    private static final Point CHAR_LOCATION_LEFT = new Point(29, 10);
    private static final Point CHAR_LOCATION_RIGHT = new Point(44, 10);

    public DoublePortraitSubView(SubView previous, CharacterAppearance leftApp, CharacterAppearance rightApp) {
        this.previous = previous;
        this.leftApp = leftApp;
        this.rightApp = rightApp;
    }

    @Override
    protected void drawArea(Model model) {
        previous.drawArea(model);
        model.getScreenHandler().clearForeground(X_OFFSET, X_MAX,
                Y_OFFSET+5, Y_OFFSET+7+PORTRAIT_FRAME_HEIGHT);
        BorderFrame.drawFrame(model.getScreenHandler(), X_OFFSET, Y_OFFSET+7,
                X_MAX-X_OFFSET-1, PORTRAIT_FRAME_HEIGHT-2, MyColors.BLACK, MyColors.GRAY, MyColors.BLACK, true);
        leftApp.drawYourself(model.getScreenHandler(), X_OFFSET+5, Y_OFFSET+9);
        portraitAnimations.drawBlink(model.getScreenHandler(), leftApp, CHAR_LOCATION_LEFT);
        rightApp.drawYourself(model.getScreenHandler(), X_MAX-12, Y_OFFSET+9);
        portraitAnimations.drawBlink(model.getScreenHandler(), rightApp, CHAR_LOCATION_RIGHT);
        portraitAnimations.drawSpeakAnimations(model.getScreenHandler());
    }

    public void portraitSay(Model model, GameState state, CharacterAppearance app, String name, String line) {
        model.getLog().waitForAnimationToFinish();
        MyPair<Integer, String> pair = CalloutSprite.getSpriteNumForText(line);
        state.printQuote(name, pair.second);
        portraitAnimations.addSpeakAnimation(app == leftApp ? CHAR_LOCATION_LEFT : CHAR_LOCATION_RIGHT,
                line, app, false);
    }

    @Override
    protected String getUnderText(Model model) {
        return previous.getUnderText(model);
    }

    @Override
    protected String getTitleText(Model model) {
        return previous.getTitleText(model);
    }
}
