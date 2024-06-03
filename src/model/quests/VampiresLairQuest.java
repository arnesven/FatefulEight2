package model.quests;

import model.Model;
import model.characters.GameCharacter;
import model.classes.Skill;
import model.enemies.Enemy;
import model.enemies.ThrallEnemy;
import model.enemies.VampirePuppeteer;
import model.quests.scenes.CollectiveSkillCheckSubScene;
import model.quests.scenes.CombatSubScene;
import model.quests.scenes.SoloSkillCheckSubScene;
import model.states.DailyEventState;
import model.states.GameState;
import model.states.QuestState;
import model.states.RecruitState;
import view.BorderFrame;
import view.MyColors;
import view.sprites.Sprite;
import view.sprites.Sprite32x32;
import view.combat.GrassCombatTheme;
import view.subviews.SplitPartySubView;
import view.subviews.SubView;
import view.widget.QuestBackground;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class VampiresLairQuest extends MainQuest {
    public static final String QUEST_NAME = "Vampire's Lair";
    private static final String TEXT = "As you travel through the mountains you encounter Caid outside a door to an old crypt. " +
            "He asks you to accompany him inside to find the sister of his lord.";
    private static final String END_TEXT = "You've solved the mystery of the lord's missing sister. Caid compensates you.";
    private static final List<QuestBackground> BACKGROUND_SPRITES = makeBackground();


    public VampiresLairQuest() {
        super(QUEST_NAME, "", QuestDifficulty.HARD, 1, 120, 0, TEXT, END_TEXT);
    }

    @Override
    public MainQuest copy() {
        return new VampiresLairQuest();
    }

    @Override
    public void drawSpecialReward(Model model, int x, int y) {
        y++;
        BorderFrame.drawString(model.getScreenHandler(), "Recruit", x, y++, MyColors.WHITE, MyColors.BLACK);
        BorderFrame.drawString(model.getScreenHandler(), "Caid", x, y++, MyColors.WHITE, MyColors.BLACK);
    }

    @Override
    public GameState endOfQuest(Model model, QuestState state, boolean questWasSuccess) {
        model.getParty().unbenchAll();
        model.getMainStory().setCaidQuestDone(true);
        if (questWasSuccess) {
           possiblyRecruitCaid(model, state);
        }
        return Quest.endOfQuestProcedure(model, state, questWasSuccess);
    }

    @Override
    protected List<QuestScene> buildScenes() {
        return List.of(
                new QuestScene("Thralls #1", List.of(
                    new CollectiveSkillCheckSubScene(1, 1, Skill.Sneak, 5, "Let's try to be quiet now."), // 5
                    new VampirePuppeteerCombatSubScene(2, 1, 11, 1))),
                new QuestScene("Thralls #2", List.of(
                    new CollectiveSkillCheckSubScene(2, 3, Skill.Sneak, 6, "Maybe we can still remain undetected in here..."), // 6
                    new VampirePuppeteerCombatSubScene(3, 3, 16, 2))),
                new QuestScene("Thralls #3", List.of(
                    new CollectiveSkillCheckSubScene(3, 5, Skill.Sneak, 7, "Amazing, nobody has noticed us yet! Let's keep going."), // 7
                    new VampirePuppeteerCombatSubScene(4, 5, 21, 3))),
                new QuestScene("Assassination", List.of(
                        new SoloSkillCheckSubScene(3, 6, Skill.Perception, 8, "Hmm... two of the vampires are talking. If we get a little closer we may overhear " +
                                "the conversation and learn where the missing sister is being held."), // 8
                        new SoloSkillCheckSubScene(1, 7, Skill.Sneak, 14, "Maybe we can just sneak up and take her out?"), // 12
                        new SoloSkillCheckSubScene(2, 7, Skill.Bows, 12, "With one well placed arrow, she'll be one vanquished vampire.") // 12
                ))
                );
    }

    @Override
    protected List<QuestJunction> buildJunctions(List<QuestScene> scenes) {
        QuestDecisionPoint qdpFirst = new QuestDecisionPoint(0, 0, List.of(new QuestEdge(scenes.get(0).get(0)),
                new QuestEdge(scenes.get(0).get(1))), "Do we attempt a stealthy approach, or not?");
        qdpFirst.setDefaultConnectionIndex(1);
        QuestDecisionPoint qdp1 = new QuestDecisionPoint(2, 2,
                List.of(new QuestEdge(scenes.get(1).get(0)),
                        new QuestEdge(scenes.get(1).get(1))), "Do we keep on sneaking?");
        qdp1.setDefaultConnectionIndex(1);
        QuestDecisionPoint qdp2 = new QuestDecisionPoint(3, 4,
                List.of(new QuestEdge(scenes.get(2).get(0)),
                        new QuestEdge(scenes.get(2).get(1))), "Do we attack this group, or sneak past them?");
        qdp2.setDefaultConnectionIndex(1);
        SimpleJunction extraFail = new SimpleJunction(5, 8, new QuestEdge(getFailEndingNode()));
        StoryJunction story = new StoryJunction(0, 1, new QuestEdge(qdpFirst)) {
            @Override
            protected void doAction(Model model, QuestState state) {
                new CaidIntroEvent(model).doEvent(model);
            }
        };
        StoryJunction vampDead = new StoryJunction(7, 5, new QuestEdge(getSuccessEndingNode(), QuestEdge.VERTICAL)) {
            @Override
            protected void doAction(Model model, QuestState state) {
                new VampiresSlainEvent(model).doEvent(model);
            }
        };
        QuestStartPointWithoutDecision qsp = new QuestStartPointWithoutDecision(new QuestEdge(story), "");
        qsp.setRow(2);
        QuestDecisionPoint qdp3 = new QuestDecisionPoint(2, 6, List.of(
                new QuestEdge(scenes.get(3).get(1)),
                new QuestEdge(scenes.get(3).get(2))),
                "Did you hear that? The lord's sister IS a vampire? Let's just assassinate her!");
        StoryJunction vampAssassinated = new StoryJunction(6, 7, new QuestEdge(getSuccessEndingNode())) {
            @Override
            protected void doAction(Model model, QuestState state) {
                new VampiresAssassinatedEvent(model).doEvent(model);
            }
        };
        return List.of(qsp, qdp1, extraFail, qdp2, story, qdpFirst, vampDead, qdp3, vampAssassinated);
    }

    @Override
    protected void connectScenesToJunctions(List<QuestScene> scenes, List<QuestJunction> junctions) {
        scenes.get(0).get(0).connectFail(scenes.get(0).get(1));
        scenes.get(0).get(0).connectSuccess(junctions.get(1), QuestEdge.VERTICAL);

        scenes.get(0).get(1).connectFail(junctions.get(2));
        scenes.get(0).get(1).connectSuccess(scenes.get(1).get(1));

        scenes.get(1).get(0).connectSuccess(junctions.get(3), QuestEdge.VERTICAL);
        scenes.get(1).get(0).connectFail(scenes.get(1).get(1));

        scenes.get(1).get(1).connectFail(junctions.get(2));
        scenes.get(1).get(1).connectSuccess(scenes.get(2).get(1));

        scenes.get(2).get(0).connectFail(scenes.get(2).get(1));
        scenes.get(2).get(0).connectSuccess(scenes.get(3).get(0));

        scenes.get(2).get(1).connectFail(getFailEndingNode(), QuestEdge.VERTICAL);
        scenes.get(2).get(1).connectSuccess(junctions.get(6));

        scenes.get(3).get(0).connectSuccess(junctions.get(7));
        scenes.get(3).get(0).connectFail(scenes.get(2).get(1));

        scenes.get(3).get(1).connectFail(getFailEndingNode());
        scenes.get(3).get(1).connectSuccess(junctions.get(8));

        scenes.get(3).get(2).connectFail(getFailEndingNode(), QuestEdge.VERTICAL);
        scenes.get(3).get(2).connectSuccess(junctions.get(8));
    }

    @Override
    public MyColors getBackgroundColor() {
        return MyColors.BLACK;
    }

    private static class VampirePuppeteerCombatSubScene extends CombatSubScene {
        private GameCharacter caidCharacter;

        public VampirePuppeteerCombatSubScene(int col, int row, int thralls, int vampires) {
            super(col, row, makeEnemies(thralls, vampires), true);
        }

        @Override
        protected List<GameCharacter> getAllies() {
            return List.of(this.caidCharacter);
        }

        @Override
        public QuestEdge run(Model model, QuestState state) {
            this.caidCharacter = model.getMainStory().getCaidCharacter();
            caidCharacter.setLevel((int)Math.ceil(GameState.calculateAverageLevel(model)));
            state.printQuote("Caid", "Wow, that's a lot of thralls, and they're coming straight for us. " +
                    "I really don't want to hurt them but it seems like we won't even be able to take a swing at " +
                    "the vampire before we cut some of them down.");
            return super.run(model, state);
        }

        @Override
        protected String getCombatDetails() {
            return "Lots of thralls";
        }
    }

    private static List<Enemy> makeEnemies(int thralls, int vampires) {
        List<Enemy> enemies = new ArrayList<>();
        for (int i = 0; i < 11; ++i) {
            enemies.add(new ThrallEnemy('B'));
        }
        for (int i = 0; i < vampires; ++i) {
            enemies.add(new VampirePuppeteer('A'));
        }
        for (int i = 0; i < thralls-11; ++i) {
            enemies.add(new ThrallEnemy('B'));
        }
        return enemies;
    }

    private static class CaidIntroEvent extends DailyEventState {
        public CaidIntroEvent(Model model) {
            super(model);
        }

        @Override
        protected void doEvent(Model model) {
            GameCharacter caid = model.getMainStory().getCaidCharacter();
            showExplicitPortrait(model, caid.getAppearance(), "Caid");
            portraitSay("Thank you for joining me here. I've tracked my lord's sister to this vampire lair.");
            leaderSay("A vampire lair!?");
            portraitSay("Yes, and it's a pretty nasty cult. They apparently enslave and mind control innocent people " +
                    "from a village nearby. These poor souls are completely enthralled by the power of the vampire.");
            leaderSay("How did a lord's sister get mixed up in that?");
            portraitSay("I'm not quite sure. After she joined the Vermin gang it seems like she must have been captured " +
                    "by these vampires. I suspect they may have turned her into a thrall as well.");
            leaderSay("What's the plan then?");
            portraitSay("Ideally, we sneak inside and learn the location of our target, then try to free her. I would " +
                    "rather not storm this crypt and face hordes of deranged villagers, as well as the vampires themselves.");
            model.getLog().waitForAnimationToFinish();
            removePortraitSubView(model);
            leaderSay("Some of us may not be the stealthiest types. Perhaps they should remain outside (A), " +
                    "while the rest of us (B) go inside?");
            do {
                List<GameCharacter> groupA = new ArrayList<>(model.getParty().getPartyMembers());
                List<GameCharacter> groupB = new ArrayList<>();
                SubView oldSubView = model.getSubView();
                model.setSubView(new SplitPartySubView(model.getSubView(), groupA, groupB));
                waitForReturnSilently();
                model.setSubView(oldSubView);
                if (groupB.isEmpty()) {
                    println("At least one party member must go inside with Caid!");
                } else {
                    model.getParty().benchPartyMembers(groupA);
                    if (groupA.contains(model.getParty().getLeader())) {
                        model.getParty().setLeader(groupB.get(0));
                    }
                    break;
                }
            } while (true);
        }
    }

    private static class VampiresSlainEvent extends CaidQuestEndingEvent {
        public VampiresSlainEvent(Model model) {
            super(model);
        }

        @Override
        protected void doEvent(Model model) {
            GameCharacter caid = model.getMainStory().getCaidCharacter();
            showExplicitPortrait(model, caid.getAppearance(), "Caid");
            leaderSay("Well Caid, we've slaughtered every single vampire in this place and we haven't " +
                    "encountered your missing person. I'm sorry to say but your intel may have been wrong on this one.");
            portraitSay("Hmm... I don't understand. I was sure she was here...");
            println("Caid looks around the crypt a bit. His eyes fall on one of the slain vampires.");
            portraitSay("Wait just a minute now... This vampire here, this IS the lord's sister!");
            leaderSay("Are you sure?");
            portraitSay("Of course, I'd know her anywhere, we grew up in the same castle.");
            leaderSay("She's a vampire? What gives?");
            super.doEvent(model);
        }

    }

    private static class VampiresAssassinatedEvent extends CaidQuestEndingEvent {
        public VampiresAssassinatedEvent(Model model) {
            super(model);
        }

        @Override
        protected void doEvent(Model model) {
            GameCharacter caid = model.getMainStory().getCaidCharacter();
            showExplicitPortrait(model, caid.getAppearance(), "Caid");
            portraitSay("I can barely believe it! The lord's sister turned into a vampire. " +
                    "But this really is her, or was.");
            println("Caid gestures to the dead body on the ground.");
            super.doEvent(model);
        }
    }

    private static class CaidQuestEndingEvent extends DailyEventState {
        public CaidQuestEndingEvent(Model model) {
            super(model);
        }

        @Override
        protected void doEvent(Model model) {
            portraitSay("She must have fallen in with an even worse crowd than the bandit gang. " +
                    "She was always a bit of a wild one... but a vampire?");
            leaderSay("This world will surprise you.");
            portraitSay("It usually doesn't... Oh well, I guess that concludes my investigation at least. " +
                    "Thanks for all the help.");
            leaderSay("Our pleasure Caid.");
            portraitSay("Say, maybe I do need to come out and see the real world a bit. I think my lord has " +
                    "enough capable hands to manage without me for a while.");
            leaderSay("Thinking about joining up with us?");
            portraitSay("Perhaps, if you'd be willing to take me in.");
            removePortraitSubView(model);
        }
    }

    private void possiblyRecruitCaid(Model model, QuestState state) {
        GameCharacter caid = model.getMainStory().getCaidCharacter();
        caid.setLevel((int) Math.ceil(GameState.calculateAverageLevel(model)));
        RecruitState recruit = new RecruitState(model, List.of(caid));
        recruit.run(model);
        if (model.getParty().getPartyMembers().contains(caid)) {
            state.leaderSay("Good to have you with us Caid.");
            state.partyMemberSay(caid, "It's my pleasure.");
        } else {
            state.leaderSay("On the other hand. Maybe we should go our separate ways.");
            state.printQuote("Caid", "Perhaps it's for the best. Goodbye friends, until our next meeting.");
            state.leaderSay("Bye Caid.");
        }
    }

    @Override
    public List<QuestBackground> getBackgroundSprites() {
        return BACKGROUND_SPRITES;
    }

    private static List<QuestBackground> makeBackground() {
        List<QuestBackground> list = new ArrayList<>();
        list.add(new QuestBackground(new Point(0, 0), AbandonedMineQuest.ENTRANCE, true));
        list.add(new QuestBackground(new Point(0, 1), GrassCombatTheme.grassSprites[0]));
        list.add(new QuestBackground(new Point(0, 2), GrassCombatTheme.grassSprites[1]));
        list.add(new QuestBackground(new Point(0, 3), GrassCombatTheme.grassSprites[2]));
        Sprite CAVE_T = new Sprite32x32("minet", "quest.png", 0x5C, MyColors.BLACK, MyColors.GRAY, MyColors.BROWN);
        Sprite CAVE_FLIPPED_T = new Sprite32x32("mineflippedt", "quest.png", 0x6D, MyColors.BLACK, MyColors.GRAY, MyColors.BROWN);
        Sprite CAVE_LEFT_T = new Sprite32x32("mineleftt", "quest.png", 0x6C, MyColors.BLACK, MyColors.GRAY, MyColors.BROWN);
        Sprite CAVE_RIGHT_T = new Sprite32x32("minerightt", "quest.png", 0x7C, MyColors.BLACK, MyColors.GRAY, MyColors.BROWN);
        list.add(new QuestBackground(new Point(1, 0), CAVE_T, false));
        list.add(new QuestBackground(new Point(2, 0), AbandonedMineQuest.UR_CORNER, false));

        list.add(new QuestBackground(new Point(1, 1), CAVE_LEFT_T, false));
        list.add(new QuestBackground(new Point(2, 1), CAVE_FLIPPED_T, false));
        list.add(new QuestBackground(new Point(3, 1), AbandonedMineQuest.UR_CORNER, false));

        list.add(new QuestBackground(new Point(1, 2), AbandonedMineQuest.LL_CORNER, false));
        list.add(new QuestBackground(new Point(2, 2), CAVE_T, false));
        list.add(new QuestBackground(new Point(3, 2), CAVE_RIGHT_T, false));

        list.add(new QuestBackground(new Point(2, 3), CAVE_LEFT_T, false));
        list.add(new QuestBackground(new Point(3, 3), CAVE_FLIPPED_T, false));
        list.add(new QuestBackground(new Point(4, 3), AbandonedMineQuest.UR_CORNER, false));

        list.add(new QuestBackground(new Point(2, 4), AbandonedMineQuest.LL_CORNER, false));
        list.add(new QuestBackground(new Point(3, 4), CAVE_T, false));
        list.add(new QuestBackground(new Point(4, 4), CAVE_RIGHT_T, false));

        list.add(new QuestBackground(new Point(3, 5), CAVE_LEFT_T, false));
        list.add(new QuestBackground(new Point(4, 5), AbandonedMineQuest.LR_CORNER, false));

        list.add(new QuestBackground(new Point(1, 6), AbandonedMineQuest.UL_CORNER, false));
        list.add(new QuestBackground(new Point(2, 6), CAVE_T, false));
        list.add(new QuestBackground(new Point(3, 6), AbandonedMineQuest.LR_CORNER, false));

        list.add(new QuestBackground(new Point(1, 7), AbandonedMineQuest.LL_CORNER, false));
        list.add(new QuestBackground(new Point(2, 7), AbandonedMineQuest.LR_CORNER, false));
        return list;
    }
}
