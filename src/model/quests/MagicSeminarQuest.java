package model.quests;

import model.Model;
import model.characters.appearance.AdvancedAppearance;
import model.characters.appearance.CharacterAppearance;
import model.characters.appearance.FacialExpression;
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
                new Reward(100), 0, INTRO, OUTRO);
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
    public QuestIntroEventState getIntroEvent(Model model) {
        return new IntroEvent(model);
    }

    @Override
    public GameState endOfQuest(Model model, QuestState state, boolean questWasSuccess) {
        if (count > 6) {
            int extraGold = (count - 6) * 25;
            state.println("Inspired by your phenomenal performance, the students give you some extra tips after the seminar.");
            state.println("The party gains " + extraGold + " gold!");
            model.getParty().earnGold(extraGold);
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

    private class IntroEvent extends QuestIntroEventState {
        public IntroEvent(Model model) {
            super(model);
        }

        @Override
        protected void runQuestIntro(Model model) {
            println("That evening, at the tavern, you can't but help but overhear a conversation at the next table.");
            boolean profsGender = getPortrait().getGender();
            printQuote("Student #1", "It's pathetic! " + heOrSheCap(profsGender) + " talks about magic, but does " + heOrShe(profsGender) +
                    " even know a single real spell?");
            printQuote("Student #2", "We should demand a refund on our tuition fees.");
            printQuote("Student #3", "Come on, guys, it's not that bad.");
            printQuote("Student #2", "Tell me one thing about magic you actually learned today.");
            printQuote("Student #3", "That's not fair. Today was all about flora and fauna. It's just a lead-up to the lecture " +
                    "on green magic later in the course.");
            printQuote("Student #1", "The professor is just padding out the course with inconsequential nonsense. " +
                    heOrSheCap(profsGender) + "'s a hack, " + heOrShe(profsGender) + "'s a...");
            println("The conversation suddenly falls silent as a middle-aged " + (profsGender ? "lady" : "gentleman") + " enters the tavern.");
            model.getLog().waitForAnimationToFinish();
            showExplicitPortrait(model, getPortrait(), getProvider());
            printQuote("Student #3", "Uh... maybe we should keep our voices down.");
            println("The professor clearly recognizes " + hisOrHer(profsGender) + " students, and their less-than-enthusiastic expressions. " +
                    "Dejected, " + heOrShe(profsGender) + " retreats to a corner with " + hisOrHer(profsGender) + " ale, takes a slow sip and stares blankly at the table.");
            println("Curious about the situation, you wander over.");
            leaderSay("May I join you?", FacialExpression.questioning);
            portraitSay("Be my guest.");
            leaderSay("Thank you. I couldn't help but notice. But are those young people over there...?", FacialExpression.questioning);
            portraitSay("My students yes. I'm a teacher at the local college here.");
            leaderSay("How exciting. And you are a magic teacher?");
            portraitSay("No, not at all, I'm a natural science teacher! But ever since professor Umpin left to become an adventurer full-time, I've had to cover for him.");
            leaderSay("I understand, that's why...");
            portraitSay("My students hate me. They all think I'm a fake, and they're right. I'm only teaching what " +
                    "I've read about in manuals and I try to fill the lectures which as much natural science stuff as I can.", FacialExpression.sad);
            portraitSay("It's making me miserable too. I'm thinking about quitting, and there will be nobody else to lead the course. " +
                    "Then the collage will have to pay them their tuition back.", FacialExpression.sad);
            leaderSay("Maybe " + iOrWe() + " can help? " + iOrWeCap() + " know a couple of things about magic actually.");
            portraitSay("You? I uh... well. Oh, why not? It's not like you can make it any worse.", FacialExpression.surprised);
            leaderSay("Perhaps " + iOrWe() + "'ll make an appearance.");
            portraitSay("Please do. I'm desperate!", FacialExpression.sad);
        }
    }
}
