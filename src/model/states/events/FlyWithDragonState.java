package model.states.events;

import model.Model;
import model.states.GameState;
import view.sprites.AvatarSprite;
import view.sprites.Sprite;

import java.awt.*;

public class FlyWithDragonState extends AlternativeTravelEvent {
    private final Sprite sprite;

    public FlyWithDragonState(Model model) {
        super(model, false);
        this.sprite = model.getParty().getTamedDragons().values().iterator().next().getFlyingSprite();
    }

    @Override
    protected Sprite getSprite() {
        return sprite;
    }

    @Override
    protected boolean eventIntro(Model model) {
        println("The party climbs up onto the back of the dragon.");
        return true;
    }

    @Override
    protected void eventOutro(Model model) {
    }

    @Override
    protected String getTitleText() {
        return "FLY ON DRAGON";
    }

    @Override
    protected String getUnderText() {
        return "You are flying on a dragon!";
    }

    @Override
    protected String getTravelPrompt() {
        return "Please select a land hex to fly to: ";
    }

    @Override
    protected boolean isSelectableDestination(Point startPoint, Point cursorPos, int dx, int dy) {
        return startPoint.distance(cursorPos.x+dx, cursorPos.y+dy) < 4;
    }

}
