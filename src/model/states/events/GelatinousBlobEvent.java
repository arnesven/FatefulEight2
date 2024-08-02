package model.states.events;

import model.Model;
import model.characters.GameCharacter;
import model.enemies.*;
import model.states.DailyEventState;
import util.MyRandom;

import java.util.ArrayList;
import java.util.List;

public class GelatinousBlobEvent extends DailyEventState {
    public GelatinousBlobEvent(Model model) {
        super(model);
    }

    @Override
    protected void doEvent(Model model) {
        leaderSay("Hmm... how odd.");
        if (model.getParty().size() > 1) {
            GameCharacter other = model.getParty().getRandomPartyMember(model.getParty().getLeader());
            partyMemberSay(other, "What is it " + model.getParty().getLeader().getFirstName() + "?");
        }
        leaderSay("The ground feels strange here. It's not dry... and it's not really wet. " +
                "It's spongy, like we're walking on a mushroom.");
        leaderSay("Wait a minute... That's some kind of creature!");
        println("All of a sudden, blobs of goop rise out of the ground and form clumps. They attack you!");
        GelatinousBlobEnemy blobTemplate = makeRandomBlob();
        List<Enemy> enemies = new ArrayList<>();
        for (int i = MyRandom.randInt(4, 8); i > 0; --i) {
            enemies.add(blobTemplate.copy());
        }
        runCombat(enemies);
    }

    public static GelatinousBlobEnemy makeRandomBlob() {
        return MyRandom.sample(List.of(
                new GreenGelatinousBlobEnemy('A'),
                new YellowGelatinousBlobEnemy('A'),
                new BlueGelatinousBlobEnemy('A'),
                new BrownGelatinousBlobEnemy('A'),
                new PurpleGelatinousBlobEnemy('A'),
                new PinkGelatinousBlobEnemy('A'),
                new RedGelatinousBlobEnemy('A'),
                new OrangeGelatinousBlobEnemy('A'),
                new WhiteGelatinousBlobEnemy('A'),
                new BlackGelatinousBlobEnemy('A')));
    }
}
