package model.states.events;

import model.Model;
import model.states.DailyEventState;
import view.MyColors;
import view.sprites.Sprite;
import view.sprites.Sprite32x32;

import java.awt.*;

public class EaglesEvent extends AlternativeTravelEvent {
    private static final Sprite SPRITE = new Sprite32x32("eagles", "enemies.png", 0x63,
            MyColors.BLACK, MyColors.BEIGE, MyColors.GRAY, MyColors.BROWN);

    public EaglesEvent(Model model) {
        super(model);
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    protected boolean eventIntro(Model model) {
        println("The party encounter some giant eagles. You are amazed to find that the eagles can talk! They are\n" +
                "however not too pleased that the party is trespassing here. " +
                "They offer to fly you down from the mountain.");
        if (model.getParty().hasHorses()) {
            print("You will have to leave your horses behind if you fly with the eagles. Do you abandon your horses? (Y/N) ");
            model.getParty().getHorseHandler().abandonHorses(model);
            return yesNoInput();
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
