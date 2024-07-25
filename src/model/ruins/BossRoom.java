package model.ruins;

import model.Model;
import model.ruins.objects.BossMonsterObject;
import model.states.ExploreRuinsState;
import util.MyRandom;

import java.awt.*;
import java.util.List;

public class BossRoom extends LargeDungeonRoom {
    private final BossMonsterObject boss;

    public BossRoom() {
        this.boss = new BossMonsterObject();
        addObject(boss);
    }

    @Override
    public void entryTrigger(Model model, ExploreRuinsState exploreRuinsState) {
        if (exploreRuinsState.getDungeon().isCompleted()) {
            model.getParty().randomPartyMemberSay(model, List.of("Why did we come back here?"));
            return;
        }
        setRelativeAvatarPosition(new Point(2, 1));
        exploreRuinsState.moveCharacterToCenterAnimation(model, getRelativeAvatarPosition());
        model.getParty().partyMemberSay(model, model.getParty().getLeader(),
                List.of("Okay people. Get ready for a boss fight.",
                "This has to be the last enemy...", "Everybody ready?"));
        String line = MyRandom.sample(List.of("Wahahaha! Come meet your doom!", "Who dares disturb me?",
                "Now you shall all meet your end.", "Fools! Die for my happiness!"));
        exploreRuinsState.printQuote(boss.getName(), line);
        exploreRuinsState.waitForReturn();
        super.entryTrigger(model, exploreRuinsState);
        if (!exploreRuinsState.isDungeonExited()) {
            exploreRuinsState.println("Congratulations! You have vanquished the boss of this ruin. The party gains 1 reputation.");
            model.getParty().addToReputation(1);
            exploreRuinsState.setDungeonExited(true);
            exploreRuinsState.getDungeon().setCompleted(true);
        }
    }

}
