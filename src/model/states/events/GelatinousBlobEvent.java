package model.states.events;

import model.Model;
import model.characters.GameCharacter;
import model.enemies.*;
import model.states.DailyEventState;
import util.MyRandom;
import view.MyColors;

import java.util.ArrayList;
import java.util.List;

public class GelatinousBlobEvent extends DailyEventState {
    private static final String HAVE_MET_BLOBS = "firstTimeMeetingGelatinousBlobs";

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
        leaderSay("The ground feels strange here. It's not dry... and it's not quite wet. " +
                "It's spongy, like we're walking on a mushroom.");
        if (model.getSettings().getMiscFlags().containsKey(HAVE_MET_BLOBS)) {
            leaderSay("I think we're running into some blobs again.");
            println("No sooner has " + model.getParty().getLeader().getFirstName() + " said so, many gelatinous blobs rise out of the ground and attack you!");
        } else {
            leaderSay("Wait a minute... That's some kind of creature!");
            println("All of a sudden, blobs of goop rise out of the ground and form clumps. They attack you!");
        }
        List<Enemy> enemies = makeRandomBlobEnemies(4, 8);
        if (model.getSettings().getMiscFlags().containsKey(HAVE_MET_BLOBS)) {
            runCombat(enemies);
        } else {
            model.getSettings().getMiscFlags().put(HAVE_MET_BLOBS, true);
            runAmbushCombat(enemies, model.getCurrentHex().getCombatTheme(), true);
        }
    }

    public static List<Enemy> makeRandomBlobEnemies(int minAmount, int maxAmount) {
        GelatinousBlobEnemy blobTemplate = makeRandomBlob();
        List<Enemy> enemies = new ArrayList<>();
        for (int i = MyRandom.randInt(minAmount, maxAmount); i > 0; --i) {
            enemies.add(blobTemplate.copy());
        }
        return enemies;
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

    public static List<MyColors> getBlobColors() {
        return List.of(MyColors.GREEN, MyColors.YELLOW, MyColors.BLUE, MyColors.BROWN,
                MyColors.PURPLE, MyColors.PINK, MyColors.RED, MyColors.ORANGE, MyColors.WHITE, MyColors.BLACK);
    }
}
