package model.quests;

import model.Model;
import model.characters.DeniseBoyd;
import model.characters.GameCharacter;
import model.characters.appearance.DefaultAppearance;
import model.classes.Classes;
import model.classes.Skill;
import model.combat.TownCombatTheme;
import model.enemies.InterloperEnemy;
import model.items.spells.FireworksSpell;
import model.items.spells.Spell;
import model.quests.scenes.CollaborativeSkillCheckSubScene;
import model.quests.scenes.CombatSubScene;
import model.quests.scenes.SoloSkillCheckSubScene;
import model.races.Race;
import model.states.QuestState;
import view.MyColors;
import view.sprites.Sprite;
import view.sprites.Sprite32x32;
import view.subviews.CombatTheme;
import view.subviews.TownSubView;
import view.widget.QuestBackground;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class UnsuspectingLoversQuest extends Quest {

    private static final String text = "Jason and Tamara are in love with " +
            "each other, but neither knows about the " +
            "other's feelings. Their parents want a " +
            "good matchmaker to set the mood and " +
            "remove sleazy interlopers. Finally, a " +
            "perfect date will seal their love forever!";

    private static final String endText = "Jason and Tamara both thank you for bringing them together. What a happy ending!";
    private static List<QuestBackground> bgSprites = makeBgSprites();

    public UnsuspectingLoversQuest() {
        super("Unsuspecting Lovers", "Jason and Tamara's parents", QuestDifficulty.EASY, 0, 25, 50, text, endText);
    }

    @Override
    public CombatTheme getCombatTheme() {
        return new TownCombatTheme();
    }

    @Override
    protected List<QuestScene> buildScenes() {
        return List.of(new QuestScene("Confessions",
                List.of(new CollaborativeSkillCheckSubScene(0, 1, Skill.Persuade, 8,
                        "Okay, first step, get these two talking..."))),
                new QuestScene("Remove Interloper",
                        List.of(new SoloSkillCheckSubScene(5, 2, Skill.Persuade, 10,
                                "Perhaps we can persuade him to direct his ardor elsewhere."),
                                new ReducePartyRepCombatSubScene(4, 2))),
                new QuestScene("Date Cuisine",
                        List.of(new CollaborativeSkillCheckSubScene(3, 4, Skill.Survival, 10,
                                "I'm sure we can whip something up ourselves."),
                                new PayGoldSubScene(4, 4, 5,
                                        "We should probably cater."))),
                new QuestScene("Date Entertainment", // TODO: add a spell callback on the actual subscene as well
                        List.of(new CollaborativeSkillCheckSubScene(2, 7, Skill.Entertain, 10,
                                "How about a love song?"),
                                new PayGoldSubScene(3, 7, 5, "Can we hire a troubadour?"))));
    }

    @Override
    protected List<QuestJunction> buildJunctions(List<QuestScene> scenes) {
        QuestDecisionPoint qd1 = new QuestDecisionPoint(2, 0,
                List.of(new QuestEdge(scenes.get(1).get(0)),
                        new QuestEdge(scenes.get(1).get(1))),
                "Now, how to deal with this meddlesome interloper.");
        QuestDecisionPoint qd2 = new QuestDecisionPoint(4, 3,
                List.of(new QuestEdge(scenes.get(2).get(0)),
                        new QuestEdge(scenes.get(2).get(1))),
                "For a romantic dinner, the entrees must be supreme!");
        QuestDecisionPoint qd3 = new QuestDecisionPoint(3, 6,
                List.of(new QuestEdge(scenes.get(3).get(0)),
                        new QuestEdge(scenes.get(3).get(1))),
                        "To set the mood, some proper entertainment is required.");
        qd3.addSpellCallback(new FireworksSpell().getName(), new SpellCallback() {
            @Override
            public QuestEdge run(Model model, QuestState state, Spell spell, GameCharacter caster) {
                state.println(caster.getFirstName() + " conjures up a magical fireworks display!");
                return new QuestEdge(getSuccessEndingNode());
            }
        });

        DecorativeJunction jason = new SpriteDecorativeJunction(7, 0,
                Classes.None.getAvatar(Race.SOUTHERN_HUMAN, new DefaultAppearance()), "Jason");

        DecorativeJunction tamara = new SpriteDecorativeJunction(6, 6,
                Classes.PRI.getAvatar(Race.WOOD_ELF, new DeniseBoyd()), "Tamara");

        return List.of(new PauseQuestJunction(0, 0, new QuestEdge(scenes.get(0).get(0)),
                "We'll get these two lovebirds together in no time."),
                qd1, qd2, qd3, jason, tamara);
    }

    @Override
    protected void connectScenesToJunctions(List<QuestScene> scenes, List<QuestJunction> junctions) {
        scenes.get(0).get(0).connectFail(getFailEndingNode(), QuestEdge.VERTICAL);
        scenes.get(0).get(0).connectSuccess(junctions.get(1));

        scenes.get(1).get(0).connectFail(scenes.get(1).get(1));
        scenes.get(1).get(0).connectSuccess(junctions.get(2), QuestEdge.VERTICAL);
        scenes.get(1).get(1).connectFail(getFailEndingNode());
        scenes.get(1).get(1).connectSuccess(junctions.get(2));

        scenes.get(2).get(0).connectFail(getFailEndingNode());
        scenes.get(2).get(0).connectSuccess(junctions.get(3));
        scenes.get(2).get(1).connectSuccess(junctions.get(3), QuestEdge.VERTICAL);

        scenes.get(3).get(0).connectFail(getFailEndingNode());
        scenes.get(3).get(0).connectSuccess(getSuccessEndingNode(), QuestEdge.VERTICAL);
        scenes.get(3).get(1).connectSuccess(getSuccessEndingNode());

    }

    @Override
    public MyColors getBackgroundColor() {
        return MyColors.BLACK;
    }

    @Override
    public List<QuestBackground> getBackgroundSprites() {
        return bgSprites;
    }

    private static List<QuestBackground> makeBgSprites() {
        List<QuestBackground> list = new ArrayList<>();
        for (int col = 0; col < 8; col++) {
            list.add(new QuestBackground(new Point(col, 0), TownSubView.STREET, true));
        }
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 8; col++) {
                list.add(new QuestBackground(new Point(col, row), TownSubView.STREET, false));
            }
        }
        Sprite townHouse = new Sprite32x32("townhousequest", "quest.png", 0x50,
                TownSubView.GROUND_COLOR, TownSubView.PATH_COLOR, MyColors.BROWN, MyColors.CYAN);
        list.add(new QuestBackground(new Point(1, 0), townHouse, false));
        list.add(new QuestBackground(new Point(3, 1), townHouse, false));
        list.add(new QuestBackground(new Point(2, 3), townHouse, false));
        list.add(new QuestBackground(new Point(5, 4), townHouse, false));
        list.add(new QuestBackground(new Point(6, 4), townHouse, false));
        list.add(new QuestBackground(new Point(5, 5), townHouse, false));
        list.add(new QuestBackground(new Point(6, 5), townHouse, false));
        list.add(new QuestBackground(new Point(2, 5), townHouse, false));
        return list;
    }

    private static class ReducePartyRepCombatSubScene extends CombatSubScene {
        public ReducePartyRepCombatSubScene(int col, int row) {
            super(col, row, List.of(new InterloperEnemy('A')), true);
        }

        @Override
        protected String getCombatDetails() {
            return "an interloper infatuated with Tamara (-1 Rep)";
        }

        @Override
        protected MyColors getSuccessEdgeColor() {
            return MyColors.LIGHT_GREEN;
        }

        @Override
        public QuestEdge run(Model model, QuestState state) {
            QuestEdge qe = super.run(model, state);
            if (qe == getSuccessEdge()) {
                model.getParty().addToReputation(-1);
                state.println("The party loses 1 reputation!");
            }
            return qe;
        }
    }

}
