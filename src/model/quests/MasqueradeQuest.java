package model.quests;

import model.Model;
import model.characters.GameCharacter;
import model.characters.appearance.CharacterAppearance;
import model.classes.Classes;
import model.classes.Skill;
import model.enemies.*;
import model.items.spells.FireworksSpell;
import model.items.spells.Spell;
import model.quests.scenes.CollaborativeSkillCheckSubScene;
import model.quests.scenes.CollectiveSkillCheckSubScene;
import model.quests.scenes.CombatSubScene;
import model.quests.scenes.SoloSkillCheckSubScene;
import model.races.Race;
import model.states.QuestState;
import sound.BackgroundMusic;
import view.MyColors;
import view.sprites.Sprite;
import view.sprites.Sprite32x32;
import view.combat.CombatTheme;
import view.combat.GrassCombatTheme;
import view.combat.MansionTheme;
import view.subviews.PortraitSubView;
import view.widget.QuestBackground;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MasqueradeQuest extends Quest {


    private static final String INTRO =
            "Perusing a bounty, the party is searching for a person who is hiding out at a " +
            "masquerade, under the protection of the host and her hired muscle.";
    private static final String OUTRO =
            "You deliver the wanted person to the local law enforcement and collect the bounty from the bounty office.";
    public static final MyColors FLOOR_COLOR = MyColors.LIGHT_GRAY;
    public static final Sprite DOOR = new Sprite32x32("door", "world_foreground.png", 0x34,
            MyColors.DARK_GRAY, MyColors.LIGHT_YELLOW, MyColors.TAN, MyColors.DARK_RED);
    public static final Sprite WALL = new Sprite32x32("tavernfarwall", "world_foreground.png", 0x44,
            MyColors.DARK_GRAY, MyColors.BROWN, MyColors.TAN);
    public static final Sprite FLOOR = new Sprite32x32("townhallfloor", "world_foreground.png", 0x56,
            MyColors.GRAY, FLOOR_COLOR, MyColors.TAN);
    public static final Sprite LOWER_WALL = new Sprite32x32("lowerwall", "world_foreground.png", 0x24,
            MyColors.DARK_GRAY, MyColors.LIGHT_YELLOW, MyColors.TAN);
    public static final Sprite RUG = new Sprite32x32("townhallrug", "world_foreground.png", 0x72,
            MyColors.DARK_RED, FLOOR_COLOR, MyColors.TAN);
    private static final Sprite PLANT = new Sprite32x32("plant", "world_foreground.png", 0x45,
            MyColors.DARK_GRAY, MyColors.BLACK, MyColors.DARK_GREEN, MyColors.CYAN);
    private static final Sprite WINDOW = new Sprite32x32("window", "world_foreground.png", 0x35,
            MyColors.BLACK, MyColors.BLACK, MyColors.GREEN, MyColors.CYAN);
    private static final CharacterAppearance PORTRAIT = PortraitSubView.makeRandomPortrait(Classes.OFFICIAL);

    private static final List<QuestBackground> BG_SPRITES = makeBackground();
    public static final Sprite[] SPECTATORS = makeSpectators();

    private static final List<QuestBackground> DECORATIONS = makeDecorations();

    public MasqueradeQuest() {
        super("Masquerade", "Bounty Office", QuestDifficulty.MEDIUM,
                new Reward(1, 175), 0, INTRO, OUTRO);
    }

    @Override
    public CharacterAppearance getPortrait() {
        return PORTRAIT;
    }

    @Override
    protected List<QuestScene> buildScenes() {
        return List.of(
                new QuestScene("Mingle",
                List.of(new CollectiveSkillCheckSubScene(4, 1, Skill.Entertain, 3,
                        "Okay gang, let's mingle and see if we can spot him."))),
                new QuestScene("Spot Target Inside",
                List.of(new SoloSkillCheckSubScene(5, 2, Skill.Perception, 10, "Anybody see our target?"))),
                new QuestScene("Investigate",
                List.of(new CollaborativeSkillCheckSubScene(2, 3, Skill.SeekInfo, 8,
                        "Somebody around here must know where he is. Spread out and gather clues."))),
                new QuestScene("Spot Target Outside",
                List.of(new SoloSkillCheckSubScene(2, 6, Skill.Perception, 9,
                        "Maybe he's out in the garden?"))),
                new QuestScene("Diversion",
                List.of(new SoloSkillCheckSubScene(4, 3, Skill.Entertain, 10,
                        "Quick, somebody cause a scene."),
                        new SoloSkillCheckSubScene(4, 4, Skill.Labor, 9,
                                "Quick, let's knock over this statue."))),
                new QuestScene("Detain Target",
                List.of(new TargetCombatSubScene(4, 7),
                        new BodyGuardCombatSubScene(3, 4))));
    }

    @Override
    protected List<QuestJunction> buildJunctions(List<QuestScene> scenes) {
        QuestJunction start = new QuestStartPointWithoutDecision(new QuestEdge(scenes.get(0).get(0)),
                "Our target is hiding somewhere at this party. Let's find him.");
        QuestDecisionPoint qd = new QuestDecisionPoint(5, 3, List.of(
                new QuestEdge(scenes.get(4).get(0), QuestEdge.VERTICAL),
                new QuestEdge(scenes.get(4).get(1), QuestEdge.VERTICAL)),
                "There he is! Let's make a diversion!");
        qd.addSpellCallback(new FireworksSpell().getName(), new SpellCallback() {
            @Override
            public QuestEdge run(Model model, QuestState state, Spell spell, GameCharacter caster) {
                state.println("All eyes are drawn to the stunning fireworks display, " +
                        "just the kind of diversion you need to get to the target.");
                return new QuestEdge(scenes.get(5).get(0), QuestEdge.VERTICAL);
            }
        });
        SimpleJunction extra = new SimpleJunction(6, 6, new QuestEdge(qd, QuestEdge.VERTICAL));
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

    @Override
    public List<QuestBackground> getBackgroundSprites() {
        return BG_SPRITES;
    }

    private static List<QuestBackground> makeBackground() {
        Random random = new Random(9847);
        List<QuestBackground> list = new ArrayList<>();
        for (int row = 0; row < 9; ++row) {
            for (int col = 0; col < 8; ++col) {
                Point p = new Point(col, row);
                if (0 < row && row < 6 && 1 < col && col < 6) {
                    list.add(new QuestBackground(p, RUG));
                } else if (0 < row && row < 6) {
                    list.add(new QuestBackground(p, FLOOR));
                } else if (row == 0) {
                    list.add(new QuestBackground(p, WALL));
                } else if (row == 6) {
                    list.add(new QuestBackground(p, LOWER_WALL));
                } else {
                    list.add(new QuestBackground(p,
                            GrassCombatTheme.grassSprites[random.nextInt(GrassCombatTheme.grassSprites.length)]));
                }
            }
        }
        return list;
    }

    @Override
    public List<QuestBackground> getDecorations() {
        return DECORATIONS;
    }

    @Override
    public BackgroundMusic getMusic() {
        return BackgroundMusic.lightQuestSong;
    }

    private static Sprite[] makeSpectators() {
        return new Sprite[]{
            new Sprite32x32("masq1", "world_foreground.png", 0x67,
                MyColors.BLACK, MyColors.DARK_BLUE, Race.NORTHERN_HUMAN.getColor(), MyColors.PURPLE),
                new Sprite32x32("masq1", "world_foreground.png", 0x67,
                        MyColors.BLACK, MyColors.DARK_RED, Race.SOUTHERN_HUMAN.getColor(), MyColors.GOLD),
                new Sprite32x32("masq1", "world_foreground.png", 0x67,
                        MyColors.BLACK, MyColors.LIGHT_GRAY, Race.HIGH_ELF.getColor(), MyColors.LIGHT_BLUE),
                new Sprite32x32("masq1", "world_foreground.png", 0x67,
                        MyColors.BLACK, MyColors.DARK_GREEN, Race.DARK_ELF.getColor(), MyColors.GREEN),
        };
    }

    private static List<QuestBackground> makeDecorations() {
        Random random = new Random(9847);
        List<QuestBackground> list = new ArrayList<>();
        list.add(new QuestBackground(new Point( 1, 0), WINDOW));
        list.add(new QuestBackground(new Point( 3, 0), WINDOW));
        list.add(new QuestBackground(new Point( 4, 0), WINDOW));
        list.add(new QuestBackground(new Point( 6, 0), WINDOW));
        list.add(new QuestBackground(new Point( 7, 1), PLANT));
        list.add(new QuestBackground(new Point( 0, 5), PLANT));
        list.add(new QuestBackground(new Point( 7, 5), PLANT));
        list.add(new QuestBackground(new Point( 3, 6), DOOR));
        list.add(new QuestBackground(new Point( 4, 6), DOOR));
        String[] placement = new String[]{
                "        ",
                " xxx xx ",
                "x xx  xx",
                " x x  x ",
                "xx    xx",
                " x   xx ",
                "        ",
                "x  x xxx",
                "  x  x  "};
        for (int y = 0; y < placement.length; ++y) {
            for (int x = 0; x < placement[0].length(); ++x) {
                if (placement[y].charAt(x) == 'x') {
                    list.add(new QuestBackground(new Point(x, y), SPECTATORS[random.nextInt(SPECTATORS.length)]));
                }
            }
        }
        return list;
    }

    @Override
    public CombatTheme getCombatTheme() {
        return new MansionTheme();
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
