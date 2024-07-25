package model.ruins;

import model.Model;
import model.states.ExploreRuinsState;

import java.awt.*;

public class LargeDungeonRoom extends DungeonRoom {

    private Point relPos;

    public LargeDungeonRoom() {
        super(5, 5);
        this.relPos = new Point(0, 0);
    }

    @Override
    public void entryTrigger(Model model, ExploreRuinsState exploreRuinsState) {
        super.entryTrigger(model, exploreRuinsState);
        setRelativeAvatarPosition(new Point(2, 1));
        exploreRuinsState.moveCharacterToCenterAnimation(model, getRelativeAvatarPosition());
    }

    @Override
    public Point getRelativeAvatarPosition() {
        return relPos;
    }

    public void setRelativeAvatarPosition(Point p) { relPos = p; }

}
