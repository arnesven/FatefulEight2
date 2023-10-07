package model.states.events;

import model.Model;
import model.states.DailyEventState;
import view.MyColors;
import view.sprites.MiniPictureSprite;
import view.sprites.Sprite;
import view.sprites.Sprite32x32;
import view.subviews.MiniPictureSubView;

import java.awt.*;

public class EaglesEvent extends AlternativeTravelEvent {
    private static final Sprite SPRITE = new Sprite32x32("eagles", "enemies.png", 0x63,
            MyColors.BLACK, MyColors.BEIGE, MyColors.GRAY, MyColors.BROWN);
    private static final MiniPictureSprite MINI_PIC_SPRITE = new MiniPictureSprite(0x02);

    public EaglesEvent(Model model) {
        super(model);
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    protected boolean eventIntro(Model model) {
        model.setSubView(new MiniPictureSubView(model.getSubView(), MINI_PIC_SPRITE, "Eagles"));
        println("The party encounter some giant eagles. You are amazed to find that the eagles can talk! They are\n" +
                "however not too pleased that the party is trespassing here. " +
                "They offer to fly you down from the mountain.");
        if (model.getParty().hasHorses()) {
            print("You will have to leave your horses behind if you fly with the eagles. Do you abandon your horses? (Y/N) ");
            if (yesNoInput()) {
                model.getParty().getHorseHandler().abandonHorses(model);
                return true;
            }
            return false;
        }
        return true;
    }

    @Override
    protected void eventOutro(Model model) {

    }

    @Override
    protected String getTitleText() {
        return "FLY ON EAGLES";
    }

    @Override
    protected String getUnderText() {
        return "Eagles are flying you down from the mountain.";
    }

    @Override
    protected String getTravelPrompt() {
        return "Please select a hex to travel to: ";
    }

    @Override
    protected boolean isSelectableDestination(Point startPoint, Point cursorPos, int dx, int dy) {
        return startPoint.distance(cursorPos.x+dx, cursorPos.y+dy) < 2;
    }
}
