package model.quests;

import model.classes.Skill;
import model.enemies.*;
import model.quests.scenes.CollaborativeSkillCheckSubScene;
import model.quests.scenes.CombatSubScene;
import sound.BackgroundMusic;
import util.MyRandom;
import view.MyColors;
import view.combat.CombatTheme;
import view.combat.TundraCombatTheme;
import view.sprites.Sprite;
import view.sprites.Sprite32x32;
import view.subviews.TownSubView;
import view.widget.QuestBackground;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class SavageVikingsQuest extends RemotePeopleQuest {
    public static final String QUEST_NAME = "Savage Vikings";
    private static final String INTRO_TEXT = "The inhabitants of the Viking Village seem very aggravated by your presence. " +
            "You will have to convince them quickly you are an ally.";
    private static final String END_TEXT = "The vikings have accepted the reason for your presence. Now you should " +
            "be able to conduct diplomatic relations with the tribe.";
    private static final VikingBerserkerEnemy VIKING_COMBAT_AVATAR = new VikingBerserkerEnemy('A');
    private static List<QuestBackground> backgroundSprites = makeBackgroundSprites();
    private static List<QuestBackground> foregroundSprites = makeForegroundSprites();


    public SavageVikingsQuest() {
        super("Savage Vikings", "Yourself", QuestDifficulty.VERY_HARD,
                new Reward(1, 0, 250), 0,
                INTRO_TEXT, END_TEXT);
    }

    @Override
    public MainQuest copy() {
        return new SavageVikingsQuest();
    }

    @Override
    protected List<QuestScene> buildScenes() {
        return List.of(
                new QuestScene("Viking Combat", List.of(
                    new VikingCombatScene(3, 2))),
                new QuestScene("Convince Vikings", List.of(
                    new CollaborativeSkillCheckSubScene(2, 5, Skill.Mercantile, 13,
                            "We are wealthy traders. You'll " +
                                    "be better off doing business with us than killing us."),
                    new CollaborativeSkillCheckSubScene(3, 5, Skill.MagicWhite, 14,
                                "We are a group of holy pilgrims. " +
                                        "Killing us would surely draw the wrath of the gods upon you!"),
                    new CollaborativeSkillCheckSubScene(4, 5, Skill.Entertain, 15,
                            "We are a famous group of minstrels. " +
                                    "How about putting off killing us until we've put on our amazing show?"),
                    new CollaborativeSkillCheckSubScene(5, 5, Skill.Persuade, 16,
                        "Actually, we here because of what's happening in the Kingdom of Bogdown.")
                )));
    }

    @Override
    protected List<QuestJunction> buildJunctions(List<QuestScene> scenes) {
        QuestStartPointWithoutDecision start = new QuestStartPointWithoutDecision(
                new QuestEdge(scenes.get(0).get(0), QuestEdge.VERTICAL),
                "Savage Vikings, coming straight for us! Everybody, get ready for a fight!");
        QuestDecisionPoint dp = new QuestDecisionPoint(3, 4, List.of(
                new QuestEdge(getFailEndingNode()),
                new QuestEdge(scenes.get(1).get(0)),
                new QuestEdge(scenes.get(1).get(1)),
                new QuestEdge(scenes.get(1).get(2)),
                new QuestEdge(scenes.get(1).get(3))
        ), "Stop! Stop the fighting! We're not here to fight you! Don't kill us, we're actually...");
        SimpleJunction loopBack = new SimpleJunction(7, 2, new QuestEdge(scenes.get(0).get(0)));
        return List.of(start, dp, loopBack);
    }

    @Override
    protected void connectScenesToJunctions(List<QuestScene> scenes, List<QuestJunction> junctions) {
        scenes.get(0).get(0).connectSuccess(junctions.get(1));

        for (int i = 0; i < 4; ++i) {
            scenes.get(1).get(i).connectFail(junctions.get(2));
            scenes.get(1).get(i).connectSuccess(getSuccessEndingNode(), QuestEdge.VERTICAL);
        }
    }

    @Override
    public CombatTheme getCombatTheme() {
        return new TundraCombatTheme();
    }

    @Override
    public MyColors getBackgroundColor() {
        return MyColors.WHITE;
    }

    @Override
    public BackgroundMusic getMusic() {
        return BackgroundMusic.darkQuestSong;
    }

    @Override
    public List<QuestBackground> getBackgroundSprites() {
        return backgroundSprites;
    }

    @Override
    public List<QuestBackground> getDecorations() {
        return foregroundSprites;
    }

    @Override
    public boolean hasBlackCursor() {
        return true;
    }

    private class VikingCombatScene extends CombatSubScene {
        public VikingCombatScene(int col, int row) {
            super(col, row, List.of(VIKING_COMBAT_AVATAR, VIKING_COMBAT_AVATAR,
                    VIKING_COMBAT_AVATAR, VIKING_COMBAT_AVATAR)); // To make difficulty hard
            setTimeLimit(3);
        }

        @Override
        public List<Enemy> getEnemies() {
            List<Enemy> list = new ArrayList<>();
            for (int i = MyRandom.randInt(12, 16); i > 0; --i) {
                int dieRoll = MyRandom.rollD10();
                if (dieRoll < 3) {
                    list.add(new VikingSwordsmanEnemy('B'));
                } else if (dieRoll < 5) {
                    list.add(new VikingAxeWielderEnemy('B'));
                } else if (dieRoll < 8) {
                    list.add(new VikingArcherEnemy('A'));
                } else {
                    list.add(new VikingBerserkerEnemy('C'));
                }
            }
            return list;
        }

        @Override
        protected String getCombatDetails() {
            return "Combat Vikings";
        }
    }

    private static List<QuestBackground> makeBackgroundSprites() {
        List<QuestBackground> bg = new ArrayList<>();
        Sprite32x32 ground = new Sprite32x32("snowyground", "quest.png", 0xBD,
                MyColors.WHITE, MyColors.GRAY, MyColors.BLACK);
        Sprite skyGradient = new Sprite32x32("skyGradient", "quest.png", 0xAD,
                MyColors.BLUE, MyColors.LIGHT_BLUE, MyColors.CYAN, MyColors.WHITE);
        for (int row = 0; row < 9; ++row) {
            for (int col = 0; col < 8; ++col) {
                if (row == 0) {
                    bg.add(new QuestBackground(new Point(col, row), skyGradient, true));
                } else {
                    bg.add(new QuestBackground(new Point(col, row), ground, false));
                }
            }
        }
        return bg;
    }

    private static List<QuestBackground> makeForegroundSprites() {
        List<QuestBackground> fg = new ArrayList<>();
        final Sprite SPRITE1 = new Sprite32x32("longhouseleft", "world_foreground.png", 0xDC,
                MyColors.YELLOW, TownSubView.PATH_COLOR, MyColors.WHITE, MyColors.GOLD);
        final Sprite SPRITE2 = new Sprite32x32("longhouseright", "world_foreground.png", 0xDD,
                MyColors.YELLOW, TownSubView.PATH_COLOR, MyColors.WHITE, MyColors.GOLD);
        fg.add(new QuestBackground(new Point(3, 1), SPRITE1, true));
        fg.add(new QuestBackground(new Point(4, 1), SPRITE2, true));


        final Sprite[] HOUSE_SPRITES = new Sprite[]{
                new Sprite32x32("townhouse", "world_foreground.png", 0x43,
                        MyColors.YELLOW, MyColors.DARK_GRAY, MyColors.WHITE, MyColors.GOLD),
                new Sprite32x32("townhouse2", "world_foreground.png", 0x53,
                        MyColors.YELLOW, MyColors.DARK_GRAY, MyColors.WHITE, MyColors.GOLD),
                new Sprite32x32("townhouse3", "world_foreground.png", 0x73,
                        MyColors.YELLOW, MyColors.DARK_GRAY, MyColors.WHITE, MyColors.GOLD)};

        fg.add(new QuestBackground(new Point(0, 3), HOUSE_SPRITES[0], true));
        fg.add(new QuestBackground(new Point(6, 3), HOUSE_SPRITES[1], false));
        fg.add(new QuestBackground(new Point(6, 4), HOUSE_SPRITES[2], false));
        fg.add(new QuestBackground(new Point(0, 5), HOUSE_SPRITES[2], false));
        fg.add(new QuestBackground(new Point(7, 7), HOUSE_SPRITES[0], true));

        return fg;
    }


}
