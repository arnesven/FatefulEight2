package model.quests;

import model.Model;
import model.classes.Skill;
import model.combat.CombatLoot;
import model.enemies.*;
import model.quests.scenes.CollaborativeSkillCheckSubScene;
import model.quests.scenes.CollectiveSkillCheckSubScene;
import model.quests.scenes.CombatSubScene;
import model.quests.scenes.SoloSkillCheckSubScene;
import view.MyColors;
import view.sprites.Sprite;

import java.util.List;

public class MasqueradeQuest extends Quest {


    private static final String INTRO =
            "Perusing a bounty, the party is searching for a person who is hiding out at a " +
            "masquerade, under the protection of the host and her hired muscle.";
    private static final String OUTRO =
            "You deliver the wanted person to the local law enforcement and collect the bounty from the bounty office.";

    public MasqueradeQuest() {
        super("Masquerade", "Bounty Office", QuestDifficulty.MEDIUM, 1, 35, 0, INTRO, OUTRO);
    }

    @Override
    protected List<QuestScene> buildScenes() {
        return List.of(
                new QuestScene("Mingle",
                List.of(new CollectiveSkillCheckSubScene(4, 2, Skill.Entertain, 3,
                        "Okay gang, let's mingle and see if we can spot him."))),
                new QuestScene("Spot Target Inside",
                List.of(new SoloSkillCheckSubScene(5, 3, Skill.Perception, 10, "Anybody see our target?"))),
                new QuestScene("Investigate",
                List.of(new CollaborativeSkillCheckSubScene(2, 4, Skill.SeekInfo, 8,
                        "Somebody around here must know where he is. Spread out and gather clues."))),
                new QuestScene("Spot Target Oustide",
                List.of(new SoloSkillCheckSubScene(2, 6, Skill.Perception, 9,
                        "Maybe he's out in the garden?"))),
                new QuestScene("Diversion",
                List.of(new SoloSkillCheckSubScene(4, 5, Skill.Entertain, 10,
                        "Quick, somebody cause a scene as a diversion!"),
                        new SoloSkillCheckSubScene(4, 6, Skill.Labor, 9,
                                "Quick, let's knock over this statue as a diversion!"))),
                new QuestScene("Detain Target",
                List.of(new TargetCombatSubScene(4, 8),
                        new BodyGuardCombatSubScene(3, 7))));
    }

    @Override
    protected List<QuestJunction> buildJunctions(List<QuestScene> scenes) {
        QuestJunction start = new QuestStartPointWithoutDecision(new QuestEdge(scenes.get(0).get(0)),
                "Our target is hiding somewhere at this party. Let's find him.");
        QuestDecisionPoint qd = new QuestDecisionPoint(5, 4, List.of(
                new QuestEdge(scenes.get(4).get(0), QuestEdge.VERTICAL),
                new QuestEdge(scenes.get(4).get(1), QuestEdge.VERTICAL)),
                "There he is!");
        SimpleJunction extra = new SimpleJunction(6, 7, new QuestEdge(qd, QuestEdge.VERTICAL));
        return List.of(start, qd, extra);
    }

    @Override
    protected void connectScenesToJunctions(List<QuestScene> scenes, List<QuestJunction> junctions) {
        scenes.get(0).get(0).connectFail(getFailEndingNode());
        scenes.get(0).get(0).connectSuccess(scenes.get(1).get(0));

        scenes.get(1).get(0).connectFail(scenes.get(2).get(0));
        scenes.get(1).get(0).connectSuccess(junctions.get(1));

        scenes.get(2).get(0).connectFail(getFailEndingNode());
        scenes.get(2).get(0).connectSuccess(scenes.get(3).get(0));

        scenes.get(3).get(0).connectFail(getFailEndingNode());
        scenes.get(3).get(0).connectSuccess(junctions.get(2), QuestEdge.VERTICAL);

        scenes.get(4).get(0).connectFail(scenes.get(5).get(1));
        scenes.get(4).get(0).connectSuccess(scenes.get(5).get(0));
        scenes.get(4).get(1).connectFail(scenes.get(5).get(1));
        scenes.get(4).get(1).connectSuccess(scenes.get(5).get(0));

        scenes.get(5).get(0).connectSuccess(getSuccessEndingNode());
        scenes.get(5).get(1).connectSuccess(scenes.get(5).get(0), QuestEdge.VERTICAL);
    }

    @Override
    public MyColors getBackgroundColor() {
        return MyColors.BLACK;
    }

    private static class TargetCombatSubScene extends CombatSubScene {
        public TargetCombatSubScene(int col, int row) {
            super(col, row, List.of(new WantedPersonEnemy('A')));
        }

        @Override
        protected String getCombatDetails() {
            return "Wanted Person";
        }

    }

    private static class BodyGuardCombatSubScene extends CombatSubScene {
        public BodyGuardCombatSubScene(int col, int row) {
            super(col, row, List.of(new BodyGuardEnemy('A'), new BodyGuardEnemy('A'), new BodyGuardEnemy('A'), new BodyGuardEnemy('A')));
        }

        @Override
        protected String getCombatDetails() {
            return "Bodyguards";
        }
    }

    private static class WantedPersonEnemy extends InterloperEnemy {
        public WantedPersonEnemy(char a) {
            super(a);
            setName("Wanted Person");
        }

        @Override
        public int getMaxHP() {
            return 4;
        }

        @Override
        public int getSpeed() {
            return 3;
        }

        @Override
        public int getDamage() {
            return 1;
        }
    }
}
