package model.ruins;

import model.Model;
import model.states.ExploreRuinsState;
import util.MyRandom;

import java.awt.*;
import java.util.List;

public class BossRoom extends DungeonRoom {
    private final BossMonsterObject boss;
    private Point relPos;

    public BossRoom() {
        super(5, 5);
        this.boss = new BossMonsterObject();
        addObject(boss);
        this.relPos = new Point(0, 0);
    }

    @Override
    public void entryTrigger(Model model, ExploreRuinsState exploreRuinsState) {
        if (exploreRuinsState.getDungeon().isCompleted()) {
            model.getParty().randomPartyMemberSay(model, List.of("Why did we come back here?"));
            return;
        }
        this.relPos = new Point(2, 1);
        exploreRuinsState.moveCharacterToCenterAnimation(model, relPos);
        model.getParty().partyMemberSay(model, model.getParty().getLeader(),
                List.of("Okay people. Get ready for a boss fight.",
                "This has to be the last enemy...", "Everybody ready?"));
        String line = MyRandom.sample(List.of("Wahahaha! Come meet your doom!", "Who dares disturb me?",
                "Now you shall all meet your end.", "Fools! Die for my happiness!"));
        exploreRuinsState.println(boss.getName() + ": \"" + line + "\"");
        exploreRuinsState.waitForReturn();
        super.entryTrigger(model, exploreRuinsState);
        if (!exploreRuinsState.isDungeonExited()) {
            exploreRuinsState.println("Congratulations! You have vanquished the boss of this ruin. The party gains 1 reputation.");
            model.getParty().addToReputation(1);
            exploreRuinsState.setDungeonExited(true);
            exploreRuinsState.getDungeon().setCompleted(true);
        }
    }

    @Override
    public Point getRelativeAvatarPosition() {
        return relPos;
    }
}
