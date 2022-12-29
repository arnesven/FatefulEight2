package model.quests;

import model.Model;
import model.classes.Skill;
import model.enemies.BanditEnemy;
import model.enemies.Enemy;
import model.enemies.MansionGuardEnemy;
import model.enemies.SoldierEnemy;
import model.items.spells.LevitateSpell;
import model.quests.scenes.CollaborativeSkillCheckSubScene;
import model.quests.scenes.CollectiveSkillCheckSubScene;
import model.quests.scenes.CombatSubScene;
import model.quests.scenes.SoloSkillCheckSubScene;
import model.states.QuestState;
import model.states.SpellCastException;
import view.MyColors;

import java.util.ArrayList;
import java.util.List;

public class MansionHeistQuest extends Quest {
    private static final String text = "A shady contact has a job of the " +
            "unreputable type. Break into Lady " +
            "Golbrads mansion and crack her safe. " +
            "Watch out for those guards though. If " +
            "you are caught, incarceration or fines " +
            "will follow.";

    private static final String endText = "You return to your contact and deliver the contents of Lady Golbrads safe.";

    public MansionHeistQuest() {
        super("Mansion Heist", "a shady contact", QuestDifficulty.MEDIUM, -1, 35, text, endText);
    }

    @Override
    protected List<QuestScene> buildScenes() {
        return List.of(new QuestScene("Ground Floor Guards",
                            List.of(new GuardsCombatScene(2, 1, 4, "downstairs"),
                                new SoloSkillCheckSubScene(2, 0, Skill.Persuade, 8,
                                        "Perhaps we can come up with an convincing reason for why we are here."))),
                        new QuestScene("Side Entrance",
                            List.of(new CollectiveSkillCheckSubScene(1, 2, Skill.SeekInfo, 10,
                                "There could be a side entrance to the mansion. If we ask around a bit we may have an easier time doing this job."))),
                        new QuestScene("First Floor Guards",
                                List.of(new GuardsCombatScene(3, 4, 3, "upstairs"),
                                        new SoloSkillCheckSubScene(4, 4, Skill.Persuade, 9,
                                                "This is going to require some eloquent speech."),
                                        new CollectiveSkillCheckSubScene(5,4, Skill.Sneak, 5,
                                                "Let's just quietly pass this lot."))),
                        new QuestScene("Crack Safe",
                                List.of(new SoloSkillCheckSubScene(2, 7, Skill.Security, 10,
                                        "That lock needs to be picked."),
                                        new CollaborativeSkillCheckSubScene(3, 7, Skill.Labor, 10,
                                                "If we work together, we can probably pry the safe open."),
                                        new CollaborativeSkillCheckSubScene(4, 7, Skill.Search, 12,
                                                "Wait a minute, Lady Golbrad probably keeps the key to the safe somewhere around here. Let's look for it."))));
    }

    @Override
    protected List<QuestJunction> buildJunctions(List<QuestScene> scenes) {
        QuestJunction qd0 = new MansionHeistStartingPoint(List.of(
                new QuestEdge(scenes.get(0).get(0), QuestEdge.VERTICAL),
                new QuestEdge(scenes.get(0).get(1)),
                new QuestEdge(scenes.get(1).get(0), QuestEdge.VERTICAL)),
                "Hmm, how to deal with the mansion's ground floor?");

        QuestJunction qd1 = new QuestDecisionPoint(4, 3,
                List.of(new QuestEdge(scenes.get(2).get(0)),
                        new QuestEdge(scenes.get(2).get(1)),
                        new QuestEdge(scenes.get(2).get(2))),
                "Right. Now how to deal with the first floor?");
        QuestJunction qd2 = new QuestDecisionPoint(3, 6,
                List.of(new QuestEdge(scenes.get(3).get(0)),
                        new QuestEdge(scenes.get(3).get(1)),
                        new QuestEdge(scenes.get(3).get(2))),
                "There's the safe! Now how to open it...");

        return List.of(qd0, qd1, qd2,
                new SimpleJunction(3, 2, new QuestEdge(qd1)),
                new SimpleJunction(3, 5, new QuestEdge(qd2)));
    }

    @Override
    protected void connectScenesToJunctions(List<QuestScene> scenes, List<QuestJunction> junctions) {
        scenes.get(0).get(0).connectSuccess(scenes.get(2).get(0), QuestEdge.VERTICAL);

        scenes.get(0).get(1).connectFail(scenes.get(0).get(0));
        scenes.get(0).get(1).connectSuccess(junctions.get(3));

        scenes.get(1).get(0).connectFail(scenes.get(0).get(0), QuestEdge.VERTICAL);
        scenes.get(1).get(0).connectSuccess(junctions.get(3));

        scenes.get(2).get(0).connectSuccess(junctions.get(4));

        scenes.get(2).get(1).connectFail(scenes.get(2).get(0));
        scenes.get(2).get(1).connectSuccess(junctions.get(4), QuestEdge.VERTICAL);

        scenes.get(2).get(2).connectFail(scenes.get(2).get(0));
        scenes.get(2).get(2).connectSuccess(junctions.get(4), QuestEdge.VERTICAL);

        for (int i = 0; i < 3; ++i) {
            scenes.get(3).get(i).connectFail(getFailEndingNode());
            scenes.get(3).get(i).connectSuccess(getSuccessEndingNode(), QuestEdge.VERTICAL);
        }
    }

    @Override
    public MyColors getBackgroundColor() {
        return MyColors.DARK_BROWN;
    }

    private class GuardsCombatScene extends CombatSubScene {
        private final int amount;
        private final String location;

        public GuardsCombatScene(int col, int row, int i, String text) {
            super(col, row, makeListOfEnemies(i));
            amount = i;
            location = text;
        }

        @Override
        protected String getCombatDetails() {
            return amount + " Guards " + location;
        }
    }

    private static List<Enemy> makeListOfEnemies(int i) {
        List<Enemy> result = new ArrayList<>();
        for ( ; i > 0; --i) {
            result.add(new MansionGuardEnemy('A'));
        }
        return result;
    }

    private class MansionHeistStartingPoint extends QuestStartPoint {
        public MansionHeistStartingPoint(List<QuestEdge> questEdges, String text) {
            super(questEdges, text);
        }

        @Override
        public QuestEdge run(Model model, QuestState state) {
            model.getSpellHandler().acceptSpell(new LevitateSpell().getName());
            do {
                try {
                    return super.run(model, state);
                } catch (SpellCastException sce) {
                    if (sce.getSpell() instanceof LevitateSpell) {
                        state.println("");
                        boolean success = sce.getSpell().castYourself(model, state, sce.getCaster());
                        if (success) {
                            state.println(sce.getCaster().getFirstName() + " levitates the party to the second floor!");
                            return new QuestEdge(getJunctions().get(1));
                        }
                    }
                }
            } while (true);
        }
    }
}
