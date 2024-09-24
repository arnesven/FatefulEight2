package model.quests;

import model.Model;
import model.characters.appearance.CharacterAppearance;
import model.classes.Classes;
import model.classes.Skill;
import model.enemies.Enemy;
import model.enemies.OrcBaker;
import model.enemies.OrcWarrior;
import model.quests.scenes.*;
import model.races.OrcAppearance;
import model.races.Race;
import model.states.DailyEventState;
import model.states.QuestState;
import sound.BackgroundMusic;
import view.BorderFrame;
import view.MyColors;
import view.combat.CombatTheme;
import view.combat.MansionTheme;
import view.sprites.Sprite;
import view.sprites.Sprite32x32;
import view.subviews.TownSubView;
import view.widget.QuestBackground;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class OrcishDelightQuest extends Quest implements CountingQuest {
    private static final String INTRO = "The party has been contacted through a middle man to distribute and promote " +
            "a new pastry by an unknown confectionist. Payment will depend on the success of the party. They have " +
            "received whole box of sweet smelling goodies, but why does this person want to remain anonymous?";
    private static final String ENDING = "";
    private int patronsPersuaded = 0;
    private final CharacterAppearance orcPortrait;
    private static List<QuestBackground> bgSprites = makeBgSprites();

    public OrcishDelightQuest() {
        super("Orcish Delight", "Mysterious Baker", QuestDifficulty.EASY, 0, 50, 50, INTRO, ENDING);
        orcPortrait = new OrcAppearance();
        orcPortrait.setClass(Classes.BAKER);
    }

    @Override
    public void drawSpecialReward(Model model, int x, int y) {
        y += 2;
        BorderFrame.drawString(model.getScreenHandler(), "25 Rations", x, y++, MyColors.WHITE, MyColors.BLACK);
    }

    @Override
    public Reward getReward() {
        return super.getReward();
    }

    @Override
    public boolean drawTownOrCastleInBackground() {
        return true;
    }

    @Override
    public List<QuestBackground> getBackgroundSprites() {
        return bgSprites;
    }

    @Override
    public CombatTheme getCombatTheme() {
        return new MansionTheme();
    }

    @Override
    protected List<QuestScene> buildScenes() {
        return List.of(new QuestScene("Distribute and promote", List.of(
                new CollaborativeSkillCheckSubScene(0, 1, Skill.SeekInfo, 8, "First we need to find out where " +
                        "pastries and goodies are being sold in this town."),
                new CountingSubScene(this, new SoloSkillCheckSubScene(5, 1, Skill.Persuade, 8, "Here's a cafe, maybe they would like to try it.")),
                new CountingSubScene(this, new SoloSkillCheckSubScene(5, 2, Skill.Persuade, 8, "There may be some merchants at the market who would be interested.")),
                new CountingSubScene(this, new SoloSkillCheckSubScene(5, 3, Skill.Persuade, 8, "How about the tavern? They may want to sell sweets too."))
                )),
                new QuestScene("Find Hideout", List.of(
                        new CollaborativeSkillCheckSubScene(1, 4, Skill.Perception, 10, "We should be able to find out where " +
                                "the confectionist is baking his goodies if we just use our noses."),
                        new CollaborativeSkillCheckSubScene(4, 5, Skill.SeekInfo, 6,
                                "Let's ask around in the market if they've seen anybody buying a lot of lemons and cinnamon sticks.")
                )),
                new QuestScene("Confront Confectionist", List.of(
                        new CountCheckSubScene(this, 5, 4, 1, 3) {
                            @Override
                            protected String getFailText() {
                                return "You have not been able to promoted the pastry well enough. (" + patronsPersuaded + "/3 *).";
                            }

                            @Override
                            protected String getSuccessText() {
                                return "You have successfully promoted the pastry. (" + patronsPersuaded + "/3 *).";
                            }
                        },
                        new ConfectionistCombatSubScene(3, 8)
                )));
    }

    @Override
    protected List<QuestJunction> buildJunctions(List<QuestScene> scenes) {
        QuestStartPoint qsp = new QuestStartPoint(List.of(new QuestEdge(scenes.get(0).get(0)), new QuestEdge(scenes.get(1).get(0))),
                "Hmm... We could ask around town if there are any shops or some such interested in trying this thing. " +
                        "On the other hand, maybe there's some funny business here. Perhaps we should try to track down the supplier and make sure there's no " +
                        "malicious intent.");
        StoryJunction happyMerchant = new StoryJunctionWithEvent(5, 5, new QuestEdge(scenes.get(1).get(1))) {
            @Override
            public DailyEventState makeEvent(Model model, QuestState state) {
                return new DailyEventState(model) {
                    @Override
                    protected void doEvent(Model model) {
                        showRandomPortrait(model, Classes.MERCHANT, "Merchant");
                        portraitSay("Good heavens! These lemon bars are absolutely scrumptious! " +
                                "So tart and creamy, and what is that... cinnamon? Who ever made this?");
                        leaderSay("Actually, " + iOrWe() + " have no idea.");
                        portraitSay("Oh but you must find out and deliver my compliments!");
                        leaderSay("Well, we will need to find out if we want to get paid. ");
                        portraitSay("These are freshly baked! Whoever made this be fairly close, and must have " +
                                "quite recently purchased lemons and cinnamon sticks.");
                        leaderSay("Thanks for the tip.");
                        model.getLog().waitForAnimationToFinish();
                        removePortraitSubView(model);
                    }
                };
            }
        };

        StoryJunction findOrcsBad = new StoryJunctionWithEvent(3, 7, new QuestEdge(scenes.get(2).get(1))) {
            @Override
            public DailyEventState makeEvent(Model model, QuestState state) {
                return new EncounterOrcBakerEvent(model, true);
            }
        };

        StoryJunction findOrcsGood = new StoryJunctionWithEvent(4, 7, new QuestEdge(getSuccessEndingNode(), QuestEdge.VERTICAL)) {
            @Override
            public DailyEventState makeEvent(Model model, QuestState state) {
                return new EncounterOrcBakerEvent(model, false);
            }
        };

        SimpleJunction extraFail = new SimpleJunction(0, 5, new QuestEdge(getFailEndingNode(), QuestEdge.VERTICAL));
        return List.of(qsp, happyMerchant, findOrcsBad, findOrcsGood, extraFail);
    }

    @Override
    protected void connectScenesToJunctions(List<QuestScene> scenes, List<QuestJunction> junctions) {
        scenes.get(0).get(0).connectFail(getFailEndingNode(), QuestEdge.VERTICAL);
        scenes.get(0).get(0).connectSuccess(scenes.get(0).get(1));

        scenes.get(0).get(1).connectSuccess(scenes.get(0).get(2));
        scenes.get(0).get(2).connectSuccess(scenes.get(0).get(3));
        scenes.get(0).get(3).connectSuccess(scenes.get(2).get(0));

        scenes.get(1).get(0).connectFail(junctions.get(4));
        scenes.get(1).get(0).connectSuccess(junctions.get(2), QuestEdge.VERTICAL);

        scenes.get(1).get(1).connectFail(junctions.get(4));
        scenes.get(1).get(1).connectSuccess(junctions.get(3));

        scenes.get(2).get(0).connectSuccess(junctions.get(1));
        scenes.get(2).get(0).connectFail(scenes.get(1).get(0));

        scenes.get(2).get(1).connectFail(getFailEndingNode(), QuestEdge.VERTICAL);
        scenes.get(2).get(1).connectSuccess(getSuccessEndingNode(), QuestEdge.VERTICAL);
    }

    @Override
    public MyColors getBackgroundColor() {
        return MyColors.GREEN;
    }

    @Override
    public BackgroundMusic getMusic() {
        return BackgroundMusic.lightQuestSong;
    }

    @Override
    public void addToCount(int x) {
        patronsPersuaded += x;
    }

    @Override
    public int getCount() {
        return patronsPersuaded;
    }

    private static class ConfectionistCombatSubScene extends CombatSubScene {
        public ConfectionistCombatSubScene(int col, int row) {
            super(col, row, makeEnemies(), true);
        }

        private static List<Enemy> makeEnemies() {
            return List.of(new OrcBaker('A'), new OrcBaker('A'), new OrcBaker('A'),
                    new OrcBaker('A'), new OrcBaker('A'));
        }

        @Override
        protected String getCombatDetails() {
            return "Orcs";
        }
    }

    private class EncounterOrcBakerEvent extends DailyEventState {
        private final boolean badEnding;

        public EncounterOrcBakerEvent(Model model, boolean badEnding) {
            super(model);
            this.badEnding = badEnding;
        }

        @Override
        protected void doEvent(Model model) {
            println("You enter a warehouse with many tables and baking equipment. There are bags of flour, " +
                    "sugar and lemons scattered everywhere. There are half a dozen people in here busy baking. " +
                    "Then you realize...");
            leaderSay("My goodness... their orcs!");
            showExplicitPortrait(model, orcPortrait, "Orc Confectionist");
            portraitSay("Huh, what are you doing in here?");
            if (badEnding) {
                leaderSay("We're here to shut this thing down. Did you think your little scheme would work, " +
                        "poisoning our town with your dirty pastries?");
                portraitSay("What are you talking about? What poison?");
                leaderSay("Enough talking. Clear out, or else!");
                portraitSay("Boyz! These rascals want to fight. Let's teach'em a lesson!");
            } else {
                leaderSay("We're your delivery service. Your treats have become quite popular.");
                portraitSay("That's great!");
                leaderSay("Uhm, if you don't mind me asking... Don't orcs usually live in the hills and ...");
                portraitSay("And what?");
                leaderSay("Well, you know... Raid, pillage, and that sort of stuff?");
                portraitSay("Yes, I suppose that's normally the case. Awfully dreadful stuff in my opinion.");
                println("You are surprised of how well spoken this orc is.");
                leaderSay("But you seem slightly... different.");
                portraitSay("Yes. I never really fit in with my gruff brethren. They were always harassing me about " +
                        "joining raid parties and that sort of thing. I just wanted to bake.");
                leaderSay("You're quite talented.");
                portraitSay("Thank you for noticing. In our camp, nobody ever did. So one day I decided to leave, taking a few of my friends with me. " +
                        "I figured if I managed to get my baked goods popular in town, maybe people wouldn't care that an Orc had baked them.");
                leaderSay("Well, now you have.");
                portraitSay("Thanks to you. Sorry for all the covertness, let me compensate you for your troubles. Oh, and grab " +
                        "as many lemon bars as you want on your way out.");
                leaderSay("Thanks!");
                println("You stuff your bags with lemon bars. The party receives 25 rations.");
                model.getParty().addToFood(25);
            }
        }
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
        list.add(new QuestBackground(new Point(2, 0), townHouse, false));
        list.add(new QuestBackground(new Point(3, 0), townHouse, false));
        list.add(new QuestBackground(new Point(4, 0), townHouse, false));
        list.add(new QuestBackground(new Point(7, 0), townHouse, false));

        Sprite wareHouseLeft = new Sprite32x32("warehouseleft", "world_foreground.png", 0x86,
                TownSubView.GROUND_COLOR, TownSubView.PATH_COLOR, MyColors.BROWN, MyColors.LIGHT_YELLOW);
        Sprite wareHouseRight = new Sprite32x32("warehouseRight", "world_foreground.png", 0x87,
                TownSubView.GROUND_COLOR, TownSubView.PATH_COLOR, MyColors.BROWN, MyColors.LIGHT_YELLOW);
        Sprite cafe = new Sprite32x32("cafe", "quest.png", 0x95,
                TownSubView.GROUND_COLOR, TownSubView.PATH_COLOR, MyColors.BROWN, MyColors.LIGHT_YELLOW);
        Sprite32x32 lstall = new Sprite32x32("lstall", "quest.png", 0x68,
                MyColors.GREEN, MyColors.DARK_BROWN, MyColors.BEIGE, MyColors.BLUE);
        Sprite32x32 rstall = new Sprite32x32("rstall", "quest.png", 0x69,
                MyColors.GREEN, MyColors.DARK_BROWN, MyColors.BEIGE, MyColors.RED);
        Sprite tavernUpper = new Sprite32x32("tavernupper", "quest.png", 0xA5,
                TownSubView.GROUND_COLOR, TownSubView.PATH_COLOR, MyColors.BROWN, MyColors.LIGHT_YELLOW);
        Sprite tavernLower = new Sprite32x32("tavernlower", "quest.png", 0xB5,
                TownSubView.GROUND_COLOR, TownSubView.PATH_COLOR, MyColors.BROWN, MyColors.LIGHT_YELLOW);

        list.add(new QuestBackground(new Point(4, 2), lstall, false));
        list.add(new QuestBackground(new Point(6, 2), rstall, false));
        list.add(new QuestBackground(new Point(2, 3), townHouse, false));
//        list.add(new QuestBackground(new Point(5, 5), townHouse, false));

        list.add(new QuestBackground(new Point(5, 0), cafe, false));
        list.add(new QuestBackground(new Point(2, 6), wareHouseLeft, false));
        list.add(new QuestBackground(new Point(3, 6), wareHouseRight, false));
        list.add(new QuestBackground(new Point(6, 4), tavernUpper, false));
        list.add(new QuestBackground(new Point(6, 5), tavernLower, false));
        return list;
    }
}
