package model.quests;

import model.Model;
import model.achievements.Achievement;
import model.characters.GameCharacter;
import model.classes.Skill;
import model.combat.Combatant;
import model.enemies.Enemy;
import model.enemies.OrcishBombThrowerEnemy;
import model.enemies.OrcishNinjaEnemy;
import model.enemies.OrcishNinjaStarThrowerEnemy;
import model.mainstory.honorable.GainSupportOfHonorableWarriorsTask;
import model.mainstory.honorable.HonorableWarriorAlly;
import model.quests.scenes.CombatSubScene;
import model.quests.scenes.SoloSkillCheckSubScene;
import model.quests.scenes.StoryJunctionWithEvent;
import model.states.DailyEventState;
import model.states.QuestState;
import model.states.dailyaction.VisitEasternPalaceNode;
import sound.BackgroundMusic;
import util.MyLists;
import util.MyRandom;
import view.MyColors;
import view.combat.CombatTheme;
import view.combat.NightGrassCombatTheme;
import view.sprites.Sprite;
import view.sprites.Sprite32x32;
import view.subviews.CollapsingTransition;
import view.subviews.SubView;
import view.widget.QuestBackground;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class NightAtTheTheaterQuest extends RemotePeopleQuest {
    public static final String QUEST_NAME = "Night at the Theater";
    private static final String INTRO_TEXT = "Lord Shinigen has invited you to a special event at the theater of the Eastern Palace. " +
            "Lots of people are attending the event.";
    private static final String END_TEXT = "To thank you again, Lord Shingen hands you a purse of coins so that you may " +
            "equip yourself adequately for the quests to come.";
    private static final int TOTAL_NUMBER_OF_NINJAS = 18;
    private static final List<QuestBackground> BACKGROUND_SPRITES = makeBackground();
    private static final List<QuestBackground> DECORATIONS = makeDecorations();

    public NightAtTheTheaterQuest() {
        super(QUEST_NAME, "Lord Shingen", QuestDifficulty.VERY_HARD, new Reward( 200, 0),
                0, INTRO_TEXT, END_TEXT);
    }

    @Override
    public Achievement.Data getAchievementData() {
        return makeAchievement(this, "You attended the theatre in the Eastern Palace, but the show was " +
                "interrupted by orcish ninjas.");
    }

    @Override
    public MainQuest copy() {
        return new NightAtTheTheaterQuest();
    }

    @Override
    protected List<QuestScene> buildScenes() {
        return List.of(new QuestScene("Detect Ninjas", List.of(
                new SoloSkillCheckSubScene(2, 2, Skill.Perception, 16,
                        "Was that something there, up on the roof top?"))),
                new QuestScene("Distributed Fight", List.of(
                        new DistributedCombatSubScene(2, 4))),
                new QuestScene("Gathered Fight", List.of(
                        new GatheredFightCombatSubScene(5, 5))));
    }

    @Override
    protected List<QuestJunction> buildJunctions(List<QuestScene> scenes) {

        StoryJunctionWithEvent endingEvent = new StoryJunctionWithEvent(6, 7,
                new QuestEdge(getSuccessEndingNode())) {
            @Override
            public DailyEventState makeEvent(Model model, QuestState state) {
                return new DiscoverNinjaOriginsEvent(model);
            }
        };

        return List.of(new TheaterStartPoint(
                new QuestEdge(scenes.get(0).get(0), QuestEdge.VERTICAL),
                "This will be a nice change to all the drama we've had lately."), endingEvent);
    }

    @Override
    protected void connectScenesToJunctions(List<QuestScene> scenes, List<QuestJunction> junctions) {
        scenes.get(0).get(0).connectSuccess(scenes.get(2).get(0));
        scenes.get(0).get(0).connectFail(scenes.get(1).get(0));

        scenes.get(1).get(0).connectFail(getFailEndingNode(), QuestEdge.VERTICAL);
        scenes.get(1).get(0).connectSuccess(junctions.get(1));

        scenes.get(2).get(0).connectFail(getFailEndingNode(), QuestEdge.VERTICAL);
        scenes.get(2).get(0).connectSuccess(junctions.get(1));
    }

    @Override
    public MyColors getBackgroundColor() {
        return MyColors.DARK_GREEN;
    }

    @Override
    public BackgroundMusic getMusic() {
        return BackgroundMusic.darkQuestSong;
    }

    private static List<Enemy> makeOrcishNinjas(int count) {
        List<Enemy> enemies = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            int roll = MyRandom.rollD6();
            if (roll < 3) {
                enemies.add(new OrcishNinjaEnemy('A'));
            } else if (roll < 5) {
                enemies.add(new OrcishBombThrowerEnemy('B'));
            } else {
                enemies.add(new OrcishNinjaStarThrowerEnemy('C'));
            }
        }
        return enemies;
    }

    private List<GameCharacter> makeHonorableWarriorAllies(int count) {
        GainSupportOfHonorableWarriorsTask task = (GainSupportOfHonorableWarriorsTask) getTask();
        List<GameCharacter> result = new ArrayList<>();
        for (int i = 0; i < count; ++i) {
            result.add(new HonorableWarriorAlly(task.makeShingenClass()));
        }
        return result;
    }

    private class GatheredFightCombatSubScene extends CombatSubScene {
        public GatheredFightCombatSubScene(int col, int row) {
            super(col, row, makeOrcishNinjas(TOTAL_NUMBER_OF_NINJAS), true);
        }

        @Override
        protected List<GameCharacter> getAllies() {
            return makeHonorableWarriorAllies(6);
        }

        @Override
        public QuestEdge run(Model model, QuestState state) {
            state.leaderSay("Lord Shingen! An enemy force has infiltrated the town!");
            state.printQuote("Lord Shingen", "To arms Honorable Warriors. We must defend ourselves!");
            state.println("The Honorable Warriors quickly gather and corner the ambushing enemy.");
            return super.run(model, state);
        }

        @Override
        protected String getCombatDetails() {
            return "Mysterious Ninjas";
        }
    }

    @Override
    public CombatTheme getCombatTheme() {
        return new NightGrassCombatTheme();
    }

    private class DistributedCombatSubScene extends QuestSubScene {

        private static final Sprite32x32 SPRITE = new Sprite32x32("storyjunc", "quest.png", 0x0C,
                MyColors.BLACK, MyColors.WHITE, MyColors.RED, MyColors.BROWN);

        public DistributedCombatSubScene(int col, int row) {
            super(col, row);
        }

        @Override
        protected MyColors getSuccessEdgeColor() {
            return MyColors.LIGHT_GREEN;
        }

        @Override
        public String getDetailedDescription() {
            return "???";
        }

        @Override
        public void drawYourself(Model model, int xPos, int yPos) {
            model.getScreenHandler().register(SPRITE.getName(), new Point(xPos, yPos), SPRITE, 1);
        }

        @Override
        public String getDescription() {
            return "???";
        }

        @Override
        public QuestEdge run(Model model, QuestState state) {
            state.println("In the middle of the play, a swarm of masked enemies rush into the theater. " +
                    "The Honorable Warriors are caught off guard by the attack.");
            List<List<GameCharacter>> groups = divideIntoGroups(model);
            if (groups.size() > 1) {
                state.println("In the confusion, the party is split up.");
            }

            SimpleJunction dummySucces = new SimpleJunction(0, 0, new QuestEdge(this));
            SimpleJunction dummyFail = new SimpleJunction(0, 0, new QuestEdge(this));

            for (List<GameCharacter> group : groups) {
                List<GameCharacter> nonGroup = new ArrayList<>(model.getParty().getPartyMembers());
                nonGroup.removeAll(group);
                model.getParty().benchPartyMembers(nonGroup);
                state.partyMemberSay(group.get(0), "Get ready for a fight! Wait, where are the others?");
                int enemiesPerGroup = (int)Math.round((double)TOTAL_NUMBER_OF_NINJAS / groups.size());
                CombatSubScene combat = makeSmallCombatSubscene(enemiesPerGroup);
                combat.connectFail(dummyFail);
                combat.connectSuccess(dummySucces);
                QuestEdge result = combat.run(model, state);
                model.getParty().unbenchAll();
                if (result == combat.getFailEdge() || MyLists.all(group, Combatant::isDead)) {
                    state.println("The group has failed its combat!");
                    return getFailEdge();
                }
            }
            state.println("All groups completed their combats.");
            return getSuccessEdge();
        }

        private CombatSubScene makeSmallCombatSubscene(int enemyCount) {
            return new CombatSubScene(0, 0, makeOrcishNinjas(enemyCount), true) {
                @Override
                protected String getCombatDetails() {
                    return "Mysterious Ninjas";
                }

                @Override
                protected List<GameCharacter> getAllies() {
                    return makeHonorableWarriorAllies(2);
                }
            };
        }

        private List<List<GameCharacter>> divideIntoGroups(Model model) {
            int twoGroups = model.getParty().size() / 2;
            List<GameCharacter> partyMembers = new ArrayList<>(model.getParty().getPartyMembers());
            Collections.shuffle(partyMembers);
            List<List<GameCharacter>> groups = new ArrayList<>();
            for (int i = 0; i < twoGroups; ++i) {
                List<GameCharacter> group = new ArrayList<>(partyMembers.subList(0, 2));
                partyMembers.removeAll(partyMembers.subList(0, 2));
                groups.add(group);
            }
            if (!partyMembers.isEmpty()) {
                if (groups.isEmpty()) {
                    List<GameCharacter> finalGroup = new ArrayList<>(partyMembers);
                    groups.add(finalGroup);
                } else {
                    groups.getLast().addAll(partyMembers);
                }
            }
            return groups;
        }
    }

    @Override
    public List<QuestBackground> getBackgroundSprites() {
        return BACKGROUND_SPRITES;
    }

    @Override
    public List<QuestBackground> getDecorations() {
        return DECORATIONS;
    }

    private static List<QuestBackground> makeBackground() {
        Sprite32x32 stageleft = new Sprite32x32("stageleft", "quest.png", 0x58,
                MyColors.DARK_GREEN, MyColors.BROWN, MyColors.RED, MyColors.DARK_GRAY);
        Sprite32x32 stageright = new Sprite32x32("stageright", "quest.png", 0x59,
                MyColors.DARK_GREEN, MyColors.BROWN, MyColors.RED, MyColors.DARK_GRAY);

        List<QuestBackground> bg = new ArrayList<>();
        bg.add(new QuestBackground(new Point(3, 3), stageleft, false));
        bg.add(new QuestBackground(new Point(4, 3), stageright, false));

        Sprite skyNoCloud = new Sprite32x32("skynocloud", "quest.png", 8,
                MyColors.YELLOW, MyColors.BEIGE, MyColors.PURPLE, MyColors.DARK_BLUE);

        Sprite skyGradient = new Sprite32x32("skyGradient", "quest.png", 0xAD,
                MyColors.DARK_PURPLE, MyColors.PURPLE, MyColors.PINK, MyColors.ORANGE);
        Sprite sunset = new Sprite32x32("skyGradient", "quest.png", 0xAE,
                MyColors.DARK_PURPLE, MyColors.PURPLE, MyColors.PINK, MyColors.YELLOW);
        for (int i = 0; i < 8; ++i) {
            bg.add(new QuestBackground(new Point(i, 0), skyNoCloud, true));
            if (i == 1) {
                bg.add(new QuestBackground(new Point(i, 1), sunset, true));
            } else {
                bg.add(new QuestBackground(new Point(i, 1), skyGradient, true));
            }
        }

        Sprite blackBlock = new Sprite32x32("blackBlock", "quest.png", 8,
                MyColors.YELLOW, MyColors.BEIGE, MyColors.PURPLE, MyColors.BLACK);

        Sprite street = new Sprite32x32("gravel", "world_foreground.png", 0xB2,
                MyColors.DARK_GREEN, MyColors.DARK_GRAY, MyColors.GREEN);

        for (int x = 1; x < 8; ++x) {
            if (x > 4) {
                bg.add(new QuestBackground(new Point(x, 3), street, false));
            }
            bg.add(new QuestBackground(new Point(x, 4), street, false));
            bg.add(new QuestBackground(new Point(x, 5), street, false));
        }

        bg.add(new QuestBackground(new Point(6, 2), blackBlock, true));
        return bg;
    }

    private static List<QuestBackground> makeDecorations() {
        List<QuestBackground> bg = new ArrayList<>();
        Sprite[][] palaceSprites = VisitEasternPalaceNode.makePalaceSprites();

        for (int y = 0; y < palaceSprites[0].length; ++y ) {
            for (int x = 0; x < palaceSprites.length; ++x) {
                bg.add(new QuestBackground(new Point(5+x, y), palaceSprites[x][y], true));
            }
        }

        Sprite house = new Sprite32x32("easternhouse", "world_foreground.png", 0x2F,
                MyColors.YELLOW, MyColors.DARK_GRAY, MyColors.RED, MyColors.WHITE);

        for (int y = 1; y < 8; ++y) {
            bg.add(new QuestBackground(new Point(0, y), house, false));
        }

        for (int x = 3; x < 5; ++x) {
            bg.add(new QuestBackground(new Point(x, 6), house, false));
            bg.add(new QuestBackground(new Point(x, 1), house, false));
        }

        return bg;
    }

    private static class TheaterStartPoint extends QuestStartPointWithoutDecision {
        public TheaterStartPoint(QuestEdge questEdge, String talk) {
            super(questEdge, talk);
            setRow(1);
        }
    }

    private class DiscoverNinjaOriginsEvent extends DailyEventState {
        public DiscoverNinjaOriginsEvent(Model model) {
            super(model);
        }

        @Override
        protected void doEvent(Model model) {
            SubView oldSubview = model.getSubView();
            setCurrentTerrainSubview(model);
            println("In the aftermath of the fight, Lord Shingen seeks you out.");
            showExplicitPortrait(model, getPortrait(), "Lord Shingen");
            portraitSay("You fought well my friend. What a cowardly attack!");
            println("You look down at the masked enemies who lay slain around you.");
            leaderSay("Yes, but who are they?");
            portraitSay("They're dressed as shadow assassins - ninjas. But they don't quite look human.");
            println("You crouch down and remove one of the masks.");
            leaderSay("An orc. Most likely a lackey of the Quad.");
            portraitSay("Orcs have been attacking us more frequently lately, but never in this insidious fashion. " +
                    "What's gotten into them.");
            leaderSay("It's a long story. Suffice to say, we need to get to the bottom of the mystery of the quad, and the " +
                    "answer lies within Arkvale Castle.");
            portraitSay("So it seems. I will ready my men. We will assault Arkvale from the east. " +
                    "This should draw the attention away from your endevour.");
            leaderSay("Many thanks Lord Shingen.");
            portraitSay("Until we meed again " + model.getParty().getLeader().getName() + ".");
            model.getLog().waitForAnimationToFinish();
            CollapsingTransition.transition(model, oldSubview);
        }
    }
}
