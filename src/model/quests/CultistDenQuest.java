package model.quests;

import model.characters.appearance.CharacterAppearance;
import model.classes.Classes;
import model.classes.Skill;
import model.enemies.CultistEnemy;
import model.enemies.Enemy;
import model.quests.scenes.CollaborativeSkillCheckSubScene;
import model.quests.scenes.CombatSubScene;
import model.quests.scenes.SoloSkillCheckSubScene;
import model.races.AllRaces;
import view.MyColors;
import view.subviews.PortraitSubView;

import java.util.ArrayList;
import java.util.List;

public class CultistDenQuest extends Quest {
    private static final int TIME_MINUTES = 15;
    private static final String INTRO = "A group of cultists are reportedly performing some dark ritual to resurrect " +
            "an other-wordly demigod. Stop them.\n" +
            "!! This is a timed quest. You have " + TIME_MINUTES + " minutes until the ritual is complete.";;
    private static final String ENDING = "The local ruler thanks you for dealing with the cultist threat, and rewards you well.";
    private static final CharacterAppearance PORTRAIT = PortraitSubView.makeRandomPortrait(Classes.NOB, AllRaces.ALL);

    public CultistDenQuest() {
        super("Cultist Den", "Local Lord", QuestDifficulty.MEDIUM, 1, 35, 0, INTRO, ENDING);
    }

    @Override
    public int getTimeLimitSeconds() {
        return TIME_MINUTES * 60;
    }

    @Override
    public boolean clockEnabled() {
        return true;
    }

    @Override
    public boolean clockTimeOutFailsQuest() {
        return false;
    }

    @Override
    public CharacterAppearance getPortrait() {
        return PORTRAIT;
    }

    @Override
    protected List<QuestScene> buildScenes() {
        return List.of(
                new QuestScene("Trapped Locked Door", List.of(
                    new SoloSkillCheckSubScene(1, 1, Skill.Security, 11, "Can somebody disable the trap mechanism?"),
                    new CollaborativeSkillCheckSubScene(3, 1, Skill.Security, 11, "Can we jimmie the lock?"))),
                new QuestScene("Cultists", List.of(
                        new CultistCombatSubScene(3, 2, 4)
                )),
                new QuestScene("More Cultists", List.of(
                        new CultistCombatSubScene(3, 3, 5)
                )));
    }

    @Override
    protected List<QuestJunction> buildJunctions(List<QuestScene> scenes) {
        QuestStartPoint qsp = new QuestStartPoint(List.of(
                new QuestEdge(scenes.get(0).get(0)),
                new QuestEdge(scenes.get(1).get(0))),
                "Hmm. Maybe we don't have to rush into this?");
        return List.of(qsp);
    }

    @Override
    protected void connectScenesToJunctions(List<QuestScene> scenes, List<QuestJunction> junctions) {
        scenes.get(0).get(0).connectFail(scenes.get(1).get(0));
        scenes.get(0).get(0).connectSuccess(scenes.get(0).get(1));
        scenes.get(0).get(1).connectFail(scenes.get(1).get(0));
        scenes.get(0).get(1).connectSuccess(getSuccessEndingNode());

        scenes.get(1).get(0).connectSuccess(scenes.get(2).get(0));
        scenes.get(1).get(0).connectFail(getFailEndingNode());

        scenes.get(2).get(0).connectSuccess(getSuccessEndingNode());
        scenes.get(2).get(0).connectFail(getFailEndingNode());
    }

    @Override
    public MyColors getBackgroundColor() {
        return MyColors.BLACK;
    }


    private static List<Enemy> makeCultistList(int noOfCultists) {
        List<Enemy> result = new ArrayList<>();
        for (int i = 0; i < noOfCultists; ++i) {
            result.add(new CultistEnemy('A'));
        }
        return result;
    }

    private static class CultistCombatSubScene extends CombatSubScene {
        private final int noOfCultists;

        public CultistCombatSubScene(int col, int row, int noOfCultists) {
            super(col, row, makeCultistList(noOfCultists), true);
            this.noOfCultists = noOfCultists;
        }


        @Override
        protected String getCombatDetails() {
            return (noOfCultists>4?"More ":"") + "Cultists";
        }
    }
}
