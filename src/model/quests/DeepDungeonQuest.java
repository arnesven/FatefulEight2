package model.quests;

import model.Model;
import model.classes.Skill;
import model.items.spells.LevitateSpell;
import model.quests.scenes.*;
import model.states.QuestState;
import model.states.SpellCastException;
import util.MyPair;
import view.MyColors;
import view.sprites.Sprite;
import view.sprites.Sprite32x32;
import view.subviews.QuestSubView;
import view.widget.QuestBackground;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class DeepDungeonQuest extends Quest {
    private static final String text = "Long regarded as forbidden by locals, this " +
            "dungeon is believed to be haunted by the " +
            "ghost of the mad Count of Vizmeria. " +
            "Recently, an antiques dealer has been " +
            "looking for a crew to clear it and bring " +
            "back an ancient magical artifact.";
    private static final String endText = "With the vampire dealt with, you are free to collect the" +
            "artifact. The party returns to the antiques dealer and collects the reward.";
    private static List<QuestBackground> bgSprites = makeBackground();

    public DeepDungeonQuest() {
        super("Deep Dungeon", "an antiques dealer", QuestDifficulty.HARD, 1, 50, text, endText);
    }

    @Override
    protected List<QuestScene> buildScenes() {
        return List.of(new QuestScene("Skeleton Sentries",
                        List.of(new SkeletonCombatSubScene(2, 1),
                                new CollectiveSkillCheckSubScene(2, 0, Skill.Sneak, 5,
                                        "Maybe we can sneak past them."))),
                new QuestScene("Mechanical Trap",
                        List.of(new TrapSubScene(4, 3),
                                new SoloSkillCheckSubScene(3, 3, Skill.Security, 9,
                                        "Do you think you can disable that?"),
                                new CollectiveSkillCheckSubScene(5, 3, Skill.Acrobatics, 4,
                                        "We're going to have to be very careful now."))),
                new QuestScene("Puzzle",
                        List.of(new CollaborativeSkillCheckSubScene(1, 6, Skill.Logic, 11,
                                "There some kind of mechanism here. Wait it's a puzzle! But what's the solution?"))),
                new QuestScene("Vampire Guardian",
                        List.of(new VampireCombatSubScene(4, 7),
                        new SoloSkillCheckSubScene(5, 7, Skill.Persuade, 10,
                                "do you think you can talk any sense into him?"))));
    }

    @Override
    protected List<QuestJunction> buildJunctions(List<QuestScene> scenes) {
        QuestJunction junc0 = new QuestStartPoint(
                List.of(new QuestEdge(scenes.get(0).get(0), QuestEdge.VERTICAL), new QuestEdge(scenes.get(0).get(1))),
                "Looks like we have some skeleton sentries up ahead.");
        QuestJunction junc1 = new BoobyTrapJunction(scenes);
        QuestJunction junc2 = new QuestDecisionPoint(4, 6,
                List.of(new QuestEdge(scenes.get(3).get(0)),
                        new QuestEdge(scenes.get(3).get(1))),
                "Is that a vampire lord?");
        return List.of(junc0, junc1, junc2,
                new SimpleJunction(4, 1, new QuestEdge(junc1)),
                new SimpleJunction(4, 4, new QuestEdge(junc2)));
    }

    @Override
    protected void connectScenesToJunctions(List<QuestScene> scenes, List<QuestJunction> junctions) {
        scenes.get(0).get(0).connectSuccess(junctions.get(3));
        scenes.get(0).get(1).connectFail(scenes.get(0).get(0));
        scenes.get(0).get(1).connectSuccess(junctions.get(3));

        scenes.get(1).get(0).connectSuccess(junctions.get(4), QuestEdge.VERTICAL);
        scenes.get(1).get(1).connectFail(scenes.get(1).get(0));
        scenes.get(1).get(1).connectSuccess(junctions.get(4), QuestEdge.VERTICAL);
        scenes.get(1).get(2).connectFail(scenes.get(1).get(0));
        scenes.get(1).get(2).connectSuccess(junctions.get(4), QuestEdge.VERTICAL);

        scenes.get(2).get(0).connectFail(getFailEndingNode());
        scenes.get(2).get(0).connectSuccess(junctions.get(2), QuestEdge.VERTICAL);

        scenes.get(3).get(0).connectSuccess(getSuccessEndingNode(), QuestEdge.VERTICAL);
        scenes.get(3).get(1).connectFail(scenes.get(3).get(0));
        scenes.get(3).get(1).connectSuccess(getSuccessEndingNode());
    }

    @Override
    public List<QuestBackground> getBackgroundSprites() {
        return bgSprites;
    }


    private static List<QuestBackground> makeBackground() {
        List<QuestBackground> result = new ArrayList<>();
        result.add(new QuestBackground(new Point(0, 0), new WallSprite(0x31)));
        for (int col = 1; col < 8; ++col) {
            if (col < 5) {
                result.add(new QuestBackground(new Point(col, 0), new WallSprite(0x30)));
            } else {
                result.add(new QuestBackground(new Point(col, 0), new WallSprite(0x32)));
            }
        }

        for (int row = 1; row < 3; ++row) {
            for (int col = 5; col < 8; ++col) {
                result.add(new QuestBackground(new Point(col, row), new WallSprite(0x32)));
            }
        }

        result.add(new QuestBackground(new Point(1, 2), new WallSprite(0x30), false));
        result.add(new QuestBackground(new Point(2, 3), new WallSprite(0x34), false));
        result.add(new QuestBackground(new Point(2, 4), new WallSprite(0x32), false));
        result.add(new QuestBackground(new Point(3, 5), new WallSprite(0x30), false));
        for (int row = 2; row < 9; ++row) {
            result.add(new QuestBackground(new Point(0, row), new WallSprite(0x32), false));
            result.add(new QuestBackground(new Point(7, row), new WallSprite(0x32), false));
            if (row < 6) {
                result.add(new QuestBackground(new Point(6, row), new WallSprite(0x32), false));
            }
        }

        result.add(new QuestBackground(new Point(2, 2), new WallSprite(0x33), false));
        result.add(new QuestBackground(new Point(3, 2), new WallSprite(0x30), false));
        result.add(new QuestBackground(new Point(5, 2), new WallSprite(0x30), false));

        result.add(new QuestBackground(new Point(2, 5), new WallSprite(0x33), false));
        result.add(new QuestBackground(new Point(4, 5), new WallSprite(0x31), false));
        result.add(new QuestBackground(new Point(5, 5), new WallSprite(0x30), false));
        result.add(new QuestBackground(new Point(6, 6), new WallSprite(0x30), false));
        result.add(new QuestBackground(new Point(2, 6), new WallSprite(0x34), false));

        result.add(new QuestBackground(new Point(2, 7), new WallSprite(0x32), false));
        result.add(new QuestBackground(new Point(3, 7), new WallSprite(0x32), false));
        result.add(new QuestBackground(new Point(2, 8), new WallSprite(0x32), false));
        result.add(new QuestBackground(new Point(3, 8), new WallSprite(0x32), false));
        return result;
    }


    private static class WallSprite extends Sprite32x32 {
        public WallSprite(int num) {
            super("wall", "quest.png", num, MyColors.DARK_GRAY, MyColors.DARK_RED, MyColors.TAN, MyColors.YELLOW);
        }
    }

    private class BoobyTrapJunction extends QuestDecisionPoint {
        public BoobyTrapJunction(List<QuestScene> scenes) {
            super(4, 2,
                    List.of(new QuestEdge(scenes.get(1).get(0)),
                            new QuestEdge(scenes.get(1).get(1)),
                            new QuestEdge(scenes.get(1).get(2)),
                            new QuestEdge(scenes.get(2).get(0))),
                    "Watch out, that looks like a booby trap right there.");
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
                            state.println(sce.getCaster().getFirstName() + " levitates the party over the booby trap!");
                            return new QuestEdge(getJunctions().get(2));
                        }
                    }
                }
            } while (true);
        }
    }
}
