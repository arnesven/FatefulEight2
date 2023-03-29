package model.ruins;

import model.Model;
import model.states.ExploreRuinsState;

import java.awt.*;
import java.util.List;

public class BossRoom extends DungeonRoom {
    private Point relPos;

    public BossRoom() {
        super(5, 5);
        addObject(new BossMonsterObject());
        this.relPos = new Point(0, 0);
    }

    @Override
    public void entryTrigger(Model model, ExploreRuinsState exploreRuinsState) {
        this.relPos = new Point(2, 1);
        exploreRuinsState.moveCharacterToCenterAnimation(model, relPos);
        model.getParty().partyMemberSay(model, model.getParty().getLeader(),
                List.of("Okay people. Get ready for a boss fight.",
                "This has to be the last enemy...", "Everybody ready?"));
        exploreRuinsState.waitForReturn();
        super.entryTrigger(model, exploreRuinsState);
        if (!exploreRuinsState.isDungeonExited()) {
            exploreRuinsState.println("Congratulations! You have vanquished the boss of this ruin. The party gains 1 reputation.");
            model.getParty().addToReputation(1);
            exploreRuinsState.setDungeonExited(true);
        }
    }

    @Override
    public Point getRelativeAvatarPosition() {
        return relPos;
    }
}
