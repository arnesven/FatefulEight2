package model.quests;

import model.Model;
import model.characters.appearance.AdvancedAppearance;
import model.characters.appearance.CharacterAppearance;
import model.characters.appearance.HairStyle;
import model.classes.CharacterClass;
import model.classes.Classes;
import model.classes.Skill;
import model.quests.scenes.*;
import model.races.Race;
import model.states.GameState;
import model.states.QuestState;
import sound.BackgroundMusic;
import util.MyRandom;
import view.MyColors;
import view.sprites.Sprite;
import view.sprites.Sprite32x32;
import view.subviews.PortraitSubView;
import view.widget.QuestBackground;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MagicSeminarQuest extends Quest implements CountingQuest {
    private static final String INTRO = "A magic professor needs some mages to make practical demonstrations as " +
            "part of a course. Is the party up to the challenge?";
    private static final String OUTRO = "Pleased with your performance, the professor rewards you with the promised sum.";
    private static final List<QuestBackground> BG_SPRITES = makeBackground();
    private static final List<QuestBackground> DECORATIONS = makeDecorations();
    private int count = 0;
    private CharacterAppearance portrait = PortraitSubView.makeOldPortrait(Classes.PROFESSOR, Race.NORTHERN_HUMAN, MyRandom.flipCoin());

    public MagicSeminarQuest() {
        super("Magic Seminar", "Professor", QuestDifficulty.EASY,
                new Reward(0, 100), 0, INTRO, OUTRO);
    }

    @Override
    public CharacterAppearance getPortrait() {
        return portrait;
    }

    @Override
    protected void resetQuest() {
        super.resetQuest();
        count = 0;
    }

    @Override
    public GameState endOfQuest(Model model, QuestState state, boolean questWasSuccess) {
        if (count > 6) {
            int extraGold = (count - 6) * 25;
            state.println("Inspired by your phenomenal performance, the students give you some extra tips after the seminar.");
            state.println("The party gains " + extraGold + " gold!");
            model.getParty().addToGold(extraGold);
        }
        return super.endOfQuest(model, state, questWasSuccess);
    }

    @Override
    public BackgroundMusic getMusic() {
        return BackgroundMusic.lightQuestSong;
    }

    @Override
    protected List<QuestScene> buildScenes() {
        return List.of(new QuestScene("Spell Color Demonstrations", List.of(
                new CountingSubScene(this, new SoloSkillCheckSubScene(1, 6, Skill.MagicWhite, 6, "Let's start with white magic.")),
                new CountingSubScene(this, new SoloSkillCheckSubScene(2,6, Skill.MagicGreen, 6, "Next, green magic.")),
                new CountingSubScene(this, new SoloSkillCheckSubScene(3, 6, Skill.MagicBlue, 6, "Now... how about some blue magic?")),
                new CountingSubScene(this, new SoloSkillCheckSubScene(4, 6, Skill.MagicRed, 6, "Time for some red magic!")),
                new CountingSubScene(this, new SoloSkillCheckSubScene(5, 6, Skill.MagicBlack, 6, "And let's not forget black magic.")),
                new CountingSubScene(this, new SoloSkillCheckSubScene(6, 6, Skill.MagicAny, 7, "Nor should we miss to talk about colorless magic.")),
                new CountingSubScene(this, new CollectiveSkillCheckSubScene(4, 7, Skill.MagicAny, 3, "There was also a request for demonstrating a ritual.")),
                new CountingSubScene(this, new CollaborativeSkillCheckSubScene(3, 7, Skill.SpellCasting, 7, "Let's round off the seminar with a short discussion about spell casting in general.")),
                new CountCheckSubScene(this, 3, 8, 6, 8) {
                    @Override
                    protected String getFailText() {
                        return "Your demonstrations have neither impressed the professor or his students. (" + count + "/8 *).";
                    }

                    @Override
                    protected String getSuccessText() {
                        return "Your demonstrations have impressed the professor and his students. (" + count + "/8 *).";
                    }
                }
        )));
    }

    @Override
    protected List<QuestJunction> buildJunctions(List<QuestScene> scenes) {
        QuestStartPointWithoutDecision qsp = new MagicSeminarStartPoint(0, 8,
                new QuestEdge(scenes.get(0).get(0), QuestEdge.VERTICAL));
        return List.of(qsp);
    }

    @Override
    protected void connectScenesToJunctions(List<QuestScene> scenes, List<QuestJunction> junctions) {
        for (int i = 1; i < scenes.get(0).size(); ++i) {
            scenes.get(0).get(i-1).connectSuccess(scenes.get(0).get(i), QuestEdge.VERTICAL);
        }
        scenes.get(0).get(scenes.get(0).size()-1).connectSuccess(getSuccessEndingNode());
        scenes.get(0).get(scenes.get(0).size()-1).connectFail(getFailEndingNode());
    }

    @Override
    public MyColors getBackgroundColor() {
        return MyColors.BLACK;
    }


    @Override
    public List<QuestBackground> getBackgroundSprites() {
        return BG_SPRITES;
    }

    @Override
    public List<QuestBackground> getDecorations() {
        return DECORATIONS;
    }

    public static List<QuestBackground> makeBackground() {
        final Sprite WALL = new Sprite32x32("tavernfarwall", "world_foreground.png", 0x44,
                MyColors.DARK_GRAY, MyColors.BROWN, MyColors.TAN);
        final Sprite FLOOR = new Sprite32x32("townhallfloor", "world_foreground.png", 0x56,
                MyColors.DARK_GRAY, MyColors.GRAY_RED, MyColors.TAN);
        final Sprite LOWER_WALL = new Sprite32x32("lowerwall", "world_foreground.png", 0x24,
                MyColors.DARK_GRAY, MyColors.LIGHT_YELLOW, MyColors.TAN);
        final Sprite DOOR = new Sprite32x32("door", "world_foreground.png", 0x34,
                MyColors.DARK_GRAY, MyColors.LIGHT_YELLOW, MyColors.TAN, MyColors.DARK_RED);
        List<QuestBackground> list = new ArrayList<>();
        for (int row = 0; row < 9; ++row) {
            for (int col = 0; col < 8; ++col) {
                Point p = new Point(col, row);
                if (row == 0) {
                    list.add(new QuestBackground(p, WALL));
                } else if (row == 8) {
                    if (col == 0 || col == 3) {
                        list.add(new QuestBackground(p, DOOR));
                    } else {
                        list.add(new QuestBackground(p, LOWER_WALL));
                    }
                } else {
                    list.add(new QuestBackground(p, FLOOR));
                }
            }
        }
        return list;
    }

    private static List<QuestBackground> makeDecorations() {
        MyColors[] clothesColor = new MyColors[]{MyColors.PURPLE, MyColors.DARK_GREEN, MyColors.LIGHT_YELLOW,
                                                 MyColors.LIGHT_BLUE, MyColors.GRAY, MyColors.PINK};
        Random random = new Random(431);
        List<QuestBackground> result = new ArrayList<>();
        for (int y = 1; y < 6; ++y) {
            for (int x = 0; x < 8; ++x) {
                if (x != 4) {
                    Race race = Race.getAllRaces().get(random.nextInt(Race.getAllRaces().size()));
                    HairStyle hair = HairStyle.allHairStyles[random.nextInt(HairStyle.allHairStyles.length)];
                    MyColors hairColor = HairStyle.npcHairColors[random.nextInt(HairStyle.npcHairColors.length)];
                    int num = 0x89 + (race.isShort() ? 1 : 0);
                    Sprite32x32 hairSprite = new Sprite32x32("avatarNormalHair"+hair.toString(), "hair.png", hair.getNormalHair(),
                            hairColor, MyColors.GOLD, MyColors.ORANGE);
                    if (race.isShort()) {
                        hairSprite.shiftUpPx(1);
                    } else {
                        hairSprite.shiftUpPx(3);
                    }
                    Sprite32x32 person = new Sprite32x32("spectator" + num, "quest.png", num,
                            MyColors.BLACK, List.of(hairSprite));
                    person.setColor2(clothesColor[random.nextInt(clothesColor.length)]);
                    person.setColor3(race.getColor());
                    person.setColor4(MyColors.BROWN);
                    result.add(new QuestBackground(new Point(x, y), person));
                }
            }
        }
        return result;
    }

    @Override
    public void addToCount(int x) {
        count += x;
    }

    @Override
    public int getCount() {
        return count;
    }

    private static class MagicSeminarStartPoint extends QuestStartPointWithoutDecision {
        public MagicSeminarStartPoint(int col, int row, QuestEdge questEdge) {
            super(questEdge, "");
            setColumn(col);
            setRow(row);
        }
    }
}
