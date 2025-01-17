package model.quests;

import model.Model;
import model.characters.GameCharacter;
import model.characters.appearance.CharacterAppearance;
import model.classes.Classes;
import model.classes.Skill;
import model.enemies.AutomatonEnemy;
import model.enemies.RedMageEnemy;
import model.items.spells.DispelSpell;
import model.items.spells.LevitateSpell;
import model.items.spells.Spell;
import model.quests.scenes.*;
import model.races.Race;
import model.states.QuestState;
import sound.BackgroundMusic;
import view.MyColors;
import view.sprites.Sprite;
import view.sprites.Sprite32x32;
import view.subviews.PortraitSubView;
import view.widget.QuestBackground;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class WizardsTowerQuest extends Quest {

    private static final String INTRO_TEXT =
            "Rastomel the Red Mage owes money to a " +
            "local bookie, but has locked himself in his " +
            "tower and refuses to come out.";

    private static final String END_TEXT = "You return to the bookie with the owed money. " +
            "The bookie tells you that it's not really about the money, but the principle. If " +
            "he would let Rastomel off, lots of other rascals would ignore their debts. He rewards " +
            "you handsomely for your effort.";
    private static final CharacterAppearance PORTRAIT = PortraitSubView.makeRandomPortrait(Classes.BANDIT, Race.ALL);
    private static List<QuestBackground> bgSprites = makeBackgroundSprites();

    public WizardsTowerQuest() {
        super("Wizard's Tower", "Local Bookie", QuestDifficulty.HARD,
                new Reward(1, 250), 0, INTRO_TEXT, END_TEXT);
    }

    @Override
    public CharacterAppearance getPortrait() {
        return PORTRAIT;
    }

    @Override
    public BackgroundMusic getMusic() {
        return BackgroundMusic.lightQuestSong;
    }

    public boolean drawTownOrCastleInBackground() {
        return true;
    }

    @Override
    public List<QuestBackground> getBackgroundSprites() {
        return bgSprites;
    }

    @Override
    protected List<QuestScene> buildScenes() {
        return List.of(
                new QuestScene("Front Door", List.of(
                        new SoloLockpickingSubScene(2, 7, 10,
                                "Can somebody pick this lock?"),
                        new SoloSkillCheckSubScene(2, 6, Skill.Acrobatics, 11,
                                "Maybe one of us can climb in through that window?"))),
                new QuestScene("Automaton", List.of(
                        new AutomatonCombatSubScene(5, 6),
                        new SoloSkillCheckSubScene(6, 6, Skill.Perception, 9,
                                "Hang on a moment. Let's have a look at this thing before we smash it to bits."),
                        new CollaborativeLockpickingSubScene(6, 5, 10,
                                "There is some kind of engine on it's back. Can we disable it?"))),
                new QuestScene("Illusion", List.of(
                        new CollectiveSkillCheckSubScene(3, 3, Skill.Logic, 4,
                        "This doesn't make any sense. Can we figure this out?"),
                        new CollectiveSkillCheckSubScene(4, 3, Skill.Perception, 4,
                        "Can anybody see anything?"),
                        new CollaborativeSkillCheckSubScene(3, 2, Skill.Search, 11,
                        "Now let's find a way to the next floor."))),
                new QuestScene("Confrontation", List.of(
                        new SoloSkillCheckSubScene(5, 0, Skill.Persuade, 12,
                                "Who can talk some sense into Rostomel?"),
                        new PayGoldSubScene(5, 2, 10, "Oh hell with it. We can pay Rostomel's debt."),
                        new RostomelCombatSubScene(5, 1))));
    }

    @Override
    protected List<QuestJunction> buildJunctions(List<QuestScene> scenes) {
        QuestStartPoint start = new QuestStartPoint(List.of(
                new QuestEdge(scenes.get(0).get(0), QuestEdge.VERTICAL),
                new QuestEdge(scenes.get(0).get(1), QuestEdge.VERTICAL)),
                "How to get into the tower?");
        QuestDecisionPoint qd1 = new QuestDecisionPoint(4, 7, List.of(
                new QuestEdge(scenes.get(1).get(0)),
                new QuestEdge(scenes.get(1).get(1))),
                "What's that? Some kind of metal beast?");
        start.addSpellCallback(new LevitateSpell().getName(), new SpellCallback() {
            @Override
            public QuestEdge run(Model model, QuestState state, Spell spell, GameCharacter caster) {
                state.println(caster.getFirstName() + " levitates the party through the tower window!");
                return new QuestEdge(qd1, QuestEdge.VERTICAL);
            }
        });
        QuestDecisionPoint qd2 = new QuestDecisionPoint(4, 4, List.of(
                new QuestEdge(scenes.get(2).get(0)),
                new QuestEdge(scenes.get(2).get(1))),
                "Wait... I don't see the tower anymore. Are we in... space?");
        QuestDecisionPoint qd3 = new QuestDecisionPoint(4, 1, List.of(
                new QuestEdge(scenes.get(3).get(0), QuestEdge.VERTICAL),
                new QuestEdge(scenes.get(3).get(1), QuestEdge.VERTICAL),
                new QuestEdge(scenes.get(3).get(2))),
                "Okay Rostomel. Time to pay up.");
        qd2.addSpellCallback(new DispelSpell().getName(), new SpellCallback() {
            @Override
            public QuestEdge run(Model model, QuestState state, Spell spell, GameCharacter caster) {
                state.println(caster.getFirstName() + " dispells the illusion!");
                return new QuestEdge(qd3);
            }
        });
        SimpleJunction sj = new SimpleJunction(7, 2, new QuestEdge(getSuccessEndingNode(), QuestEdge.VERTICAL));
        return List.of(start, qd1, qd2, qd3, sj);
    }

    @Override
    protected void connectScenesToJunctions(List<QuestScene> scenes, List<QuestJunction> junctions) {
        scenes.get(0).get(0).connectFail(getFailEndingNode(), QuestEdge.VERTICAL);
        scenes.get(0).get(0).connectSuccess(junctions.get(1));
        scenes.get(0).get(1).connectFail(getFailEndingNode(), QuestEdge.VERTICAL);
        scenes.get(0).get(1).connectSuccess(junctions.get(1));

        scenes.get(1).get(0).connectSuccess(junctions.get(2));
        scenes.get(1).get(1).connectFail(scenes.get(1).get(0));
        scenes.get(1).get(1).connectSuccess(scenes.get(1).get(2));
        scenes.get(1).get(2).connectFail(scenes.get(1).get(0));
        scenes.get(1).get(2).connectSuccess(junctions.get(2), QuestEdge.VERTICAL);

        scenes.get(2).get(0).connectFail(getFailEndingNode());
        scenes.get(2).get(0).connectSuccess(scenes.get(2).get(2), QuestEdge.VERTICAL);
        scenes.get(2).get(1).connectFail(getFailEndingNode());
        scenes.get(2).get(1).connectSuccess(scenes.get(2).get(2), QuestEdge.VERTICAL);
        scenes.get(2).get(2).connectFail(getFailEndingNode());
        scenes.get(2).get(2).connectSuccess(junctions.get(3), QuestEdge.VERTICAL);

        scenes.get(3).get(0).connectFail(scenes.get(3).get(2));
        scenes.get(3).get(0).connectSuccess(junctions.get(4));
        scenes.get(3).get(1).connectSuccess(junctions.get(4));
        scenes.get(3).get(2).connectSuccess(junctions.get(4));
    }

    @Override
    public MyColors getBackgroundColor() {
        return MyColors.GREEN;
    }

    private static class AutomatonCombatSubScene extends CombatSubScene {
        public AutomatonCombatSubScene(int col, int row) {
            super(col, row, List.of(new AutomatonEnemy('A')), false);
        }

        @Override
        protected String getCombatDetails() {
            return "Automaton";
        }
    }

    private static class RostomelCombatSubScene extends CombatSubScene {
        public RostomelCombatSubScene(int col, int row) {
            super(col, row, List.of(new RedMageEnemy('A')), false);
        }

        @Override
        protected String getCombatDetails() {
            return "Rostomel the Wizard";
        }
    }

    private static List<QuestBackground> makeBackgroundSprites() {
        List<QuestBackground> list = new ArrayList<>();
        Sprite TOWER_LL = makeTowerSprite(0x70);
        Sprite TOWER_DOOR = makeTowerSprite(0x71);
        Sprite TOWER_BOTTOM = makeTowerSprite(0x72);
        Sprite TOWER_LR = makeTowerSprite(0x73);
        list.add(new QuestBackground(new Point(2, 8), TOWER_LL));
        list.add(new QuestBackground(new Point(3, 8), TOWER_DOOR));
        for (int x = 4; x < 6; ++x) {
            list.add(new QuestBackground(new Point(x, 8), TOWER_BOTTOM));
        }
        list.add(new QuestBackground(new Point(6, 8), TOWER_LR));

        Sprite TOWER_LEFT = makeTowerSprite(0x74);
        Sprite TOWER_RIGHT = makeTowerSprite(0x76);
        Sprite TOWER_FULL = makeTowerSprite(0x77);
        list.add(new QuestBackground(new Point(2, 7), TOWER_LEFT));
        for (int x = 3; x < 7; ++x) {
            list.add(new QuestBackground(new Point(x, 7), TOWER_FULL));
        }
        list.add(new QuestBackground(new Point(6, 7), TOWER_RIGHT));

        Sprite TOWER_WINDOW = makeTowerSprite(0x75);
        list.add(new QuestBackground(new Point(2, 6), TOWER_WINDOW));
        for (int x = 3; x < 7; ++x) {
            list.add(new QuestBackground(new Point(x, 6), TOWER_FULL));
        }
        list.add(new QuestBackground(new Point(6, 6), TOWER_RIGHT));

        for (int y = 5; y > 0; --y) {
            if (y % 2 == 0) {
                list.add(new QuestBackground(new Point(2, y), TOWER_WINDOW));
            } else {
                list.add(new QuestBackground(new Point(2, y), TOWER_LEFT));
            }
            for (int x = 3; x < 7; ++x) {
                list.add(new QuestBackground(new Point(x, y), TOWER_FULL));
            }
            list.add(new QuestBackground(new Point(6, y), TOWER_RIGHT));
        }

        Sprite ROOF_LEFT = makeTowerSprite(0x80);
        Sprite ROOF = makeTowerSprite(0x81);
        Sprite ROOF_RIGHT = makeTowerSprite(0x82);

        list.add(new QuestBackground(new Point(1, 0), ROOF_LEFT));
        for (int x = 2; x < 7; ++x) {
            list.add(new QuestBackground(new Point(x, 0), ROOF));
        }
        list.add(new QuestBackground(new Point(7, 0), ROOF_RIGHT));

        return list;
    }

    private static Sprite makeTowerSprite(int num) {
        return new Sprite32x32("wizardtower"+num, "quest.png", num, MyColors.BLACK, MyColors.DARK_GRAY, MyColors.LIGHT_GRAY, MyColors.GREEN);
    }

}
