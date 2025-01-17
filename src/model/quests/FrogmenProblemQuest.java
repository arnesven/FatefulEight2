package model.quests;

import model.Model;
import model.characters.GameCharacter;
import model.classes.Skill;
import model.enemies.FrogmanChiefEnemy;
import model.enemies.FrogmanLeaderEnemy;
import model.enemies.FrogmanScoutEnemy;
import model.enemies.FrogmanShamanEnemy;
import model.items.spells.HarmonizeSpell;
import model.items.spells.Spell;
import model.quests.scenes.CollaborativeSkillCheckSubScene;
import model.quests.scenes.CombatSubScene;
import model.quests.scenes.SoloSkillCheckSubScene;
import model.races.FrogmanAppearance;
import model.states.DailyEventState;
import model.states.QuestState;
import sound.BackgroundMusic;
import view.MyColors;
import view.sprites.Sprite;
import view.sprites.Sprite32x32;
import view.combat.CombatTheme;
import view.combat.GrassCombatTheme;
import view.widget.QuestBackground;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class FrogmenProblemQuest extends MainQuest {
    public static final String QUEST_NAME = "Frogmen Problem";
    private static final String TEXT = "You've been contracted by a town to take care of a rampart population of " +
            "frogmen. The frogmen have their settlement nearby. You could just wipe them all out, but is there " +
            "something more to this job than meets the eye?";
    private static final String END_TEXT = "You return to town to report your success. You are rewarded for your service.";
    private static final Sprite SWAMP = new Sprite32x32("frogmenswamp", "quest.png", 0x0F,
            MyColors.DARK_BROWN, MyColors.DARK_GREEN, MyColors.TAN, MyColors.GREEN);
    private static final Sprite HUTS = new Sprite32x32("frogmenhuts", "quest.png", 0x1F,
            MyColors.DARK_BROWN, MyColors.DARK_GREEN, MyColors.TAN, MyColors.GREEN);
    private static List<QuestBackground> bgSprites = makeBgSprites();

    public FrogmenProblemQuest() {
        super(QUEST_NAME, "Uncle", QuestDifficulty.EASY,
                new Reward(1, 0, 25), 2, TEXT, END_TEXT);
    }

    @Override
    public CombatTheme getCombatTheme() {
        return new GrassCombatTheme();
    }

    @Override
    public boolean drawTownOrCastleInBackground() {
        return true;
    }

    @Override
    public BackgroundMusic getMusic() {
        return BackgroundMusic.mysticSong;
    }

    @Override
    protected List<QuestScene> buildScenes() {
        return List.of(new QuestScene("Gather Clues",
                List.of(new CollaborativeSkillCheckSubScene(1, 1, Skill.SeekInfo, 8,
                        "Maybe we can ask around town. Somebody must know something."))),
                new QuestScene("Find the Camp",
                        List.of(new CollaborativeSkillCheckSubScene(2, 2, Skill.Survival, 8,
                                "Now we just have to find this settlement."))),
                new QuestScene("Approach the Camp",
                        List.of(new FrogmanGuardsCombat(2, 5),
                                new PerceptFrogmenLanguageSubScene(5, 4),
                                new UnderstandFrogmenLanguageSubScene(5, 5),
                                new PersuadeFrogmenSubScene(5, 6))),
                new QuestScene("Alarm Raised",
                        List.of(new FrogmanShamanCombat(2, 6),
                                new FrogmanChieftainCombat(2, 7))));
    }

    @Override
    protected List<QuestJunction> buildJunctions(List<QuestScene> scenes) {
        QuestStartPointWithoutDecision qsp = new QuestStartPointWithoutDecision(new QuestEdge(scenes.get(0).get(0)),
                "First we need to find out where these frogmen are located.");
        QuestDecisionPoint qdp = new QuestDecisionPoint(3, 3,
                List.of(new QuestEdge(scenes.get(2).get(0)), new QuestEdge(scenes.get(2).get(1))),
                "We can either approach peacefully, or rush straight in. Any communication though will require " +
                        "no trivial degree of finesse.");
        SimpleJunction simp = new SimpleJunction(3, 5, new QuestEdge(scenes.get(2).get(0)));
        StoryJunction combatEnding = new StoryJunction(5, 8, new QuestEdge(getSuccessEndingNode())) {
            @Override
            protected void doAction(Model model, QuestState state) {
                state.println("The frogmen lay slain all around you.");
                state.leaderSay("Well, not exactly peaceful...");
                state.println("You are about to leave the carnage, but cast one last glance at the crazed frogman chieftain. " +
                        "A huge gash in his belly has spilled his slimy innards onto the ground. Among them you spot a shiny red orb.");
                state.leaderSay("What's this? A pearl? Why would a frogman have it in his belly. Did he eat it by mistake?");
                state.println("You clean the pearl a bit and put it into your pocket.");
                state.leaderSay("Perhaps it is valuable?");
                getCrimsonPearl(model, state);
            }
        };
        StoryJunction peacefulEnding = new StoryJunction(6, 7, new QuestEdge(getSuccessEndingNode())) {
            @Override
            protected void doAction(Model model, QuestState state) {
                state.println("The frogmen seem to have calmed down. It appears the reason they have been so aggressive lately " +
                        "is due to their new leadership. A very aggressive chieftain from a nearby settlement. But the chieftain has " +
                        "become ill. He is lying on a cot, feverish and dry.");
                state.leaderSay("Perhaps if we gave him some of our medicine?");
                state.println("You try to treat the frogman as best you can. After a while he retches violently.");
                if (model.getParty().size() > 1) {
                    GameCharacter gc = model.getParty().getRandomPartyMember(model.getParty().getLeader());
                    state.partyMemberSay(gc, "Disgusting!");
                }
                state.leaderSay("Wait a moment. What's that on the floor?");
                state.leaderSay("What's this? A red pearl? Why would a frogman have it in his belly. Did he eat it by mistake?");
                state.println("You clean the pearl a bit and put it into your pocket.");
                state.leaderSay("Perhaps it is valuable?");
                getCrimsonPearl(model, state);
            }
        };
        return List.of(qsp, qdp, simp, combatEnding, peacefulEnding);
    }

    @Override
    protected void connectScenesToJunctions(List<QuestScene> scenes, List<QuestJunction> junctions) {
        scenes.get(0).get(0).connectFail(getFailEndingNode());
        scenes.get(0).get(0).connectSuccess(scenes.get(1).get(0));

        scenes.get(1).get(0).connectFail(getFailEndingNode());
        scenes.get(1).get(0).connectSuccess(junctions.get(1));

        scenes.get(2).get(0).connectFail(getFailEndingNode());
        scenes.get(2).get(0).connectSuccess(scenes.get(3).get(0));
        scenes.get(2).get(0).addSpellCallback(new HarmonizeSpell().getName(), (SpellCallback) (model, state, spell, caster) -> {
            state.println("The frogmen seem to have calmed down and let you go about your business.");
            return new QuestEdge(scenes.get(3).get(0));
        });

        scenes.get(2).get(1).connectFail(junctions.get(2));
        scenes.get(2).get(1).connectSuccess(scenes.get(2).get(2));

        scenes.get(2).get(2).connectFail(junctions.get(2));
        scenes.get(2).get(2).connectSuccess(scenes.get(2).get(3));

        scenes.get(2).get(3).connectFail(junctions.get(2));
        scenes.get(2).get(3).connectSuccess(junctions.get(4));

        scenes.get(3).get(0).connectFail(getFailEndingNode());
        scenes.get(3).get(0).connectSuccess(scenes.get(3).get(1));
        scenes.get(3).get(0).addSpellCallback(new HarmonizeSpell().getName(), new SpellCallback() {
            @Override
            public QuestEdge run(Model model, QuestState state, Spell spell, GameCharacter caster) {
                state.println("The frogmen seem to have calmed down and let you go about your business.");
                return new QuestEdge(scenes.get(3).get(1));
            }
        });

        scenes.get(3).get(1).connectFail(getFailEndingNode());
        scenes.get(3).get(1).connectSuccess(junctions.get(3), QuestEdge.VERTICAL);
        scenes.get(3).get(1).addSpellCallback(new HarmonizeSpell().getName(), new SpellCallback() {
            @Override
            public QuestEdge run(Model model, QuestState state, Spell spell, GameCharacter caster) {
                return new QuestEdge(junctions.get(4));
            }
        });
    }

    @Override
    public MyColors getBackgroundColor() {
        return MyColors.GREEN;
    }

    @Override
    public List<QuestBackground> getBackgroundSprites() {
        return bgSprites;
    }


    private static List<QuestBackground> makeBgSprites() {
        List<QuestBackground> background = new ArrayList<>();
        for (int y = 0; y < 4; ++y) {
            for (int x = 1+y; x < 8; ++x) {
                background.add(new QuestBackground(new Point(x, y), SWAMP));
            }
        }
        for (int y = 2; y < 9; ++y) {
            background.add(new QuestBackground(new Point(1, y), SWAMP));
            background.add(new QuestBackground(new Point(0, y), SWAMP));
        }
        background.add(new QuestBackground(new Point(4, 4), HUTS));

        for (int y = 4; y < 7; ++y) {
            background.add(new QuestBackground(new Point(6, y), HUTS));
            background.add(new QuestBackground(new Point(7, y), HUTS));
        }
        background.add(new QuestBackground(new Point(7, 7), HUTS));

        for (int x = 3; x < 6; ++x) {
            background.add(new QuestBackground(new Point(x, 7), HUTS));
        }
        for (int x = 2; x < 6; ++x) {
            background.add(new QuestBackground(new Point(x, 8), SWAMP));
        }
        return background;
    }

    @Override
    public MainQuest copy() {
        return new FrogmenProblemQuest();
    }

    private static class FrogmanGuardsCombat extends CombatSubScene {
        public FrogmanGuardsCombat(int col, int row) {
            super(col, row, List.of(new FrogmanScoutEnemy('A'), new FrogmanLeaderEnemy('B'),
                    new FrogmanScoutEnemy('A')), true);
        }

        @Override
        protected String getCombatDetails() {
            return "Frogman Guards";
        }
    }

    private static class FrogmanShamanCombat extends CombatSubScene {
        public FrogmanShamanCombat(int col, int row) {
            super(col, row, List.of(new FrogmanShamanEnemy('B'), new FrogmanScoutEnemy('A'),
                    new FrogmanScoutEnemy('A')), true);

        }

        @Override
        protected String getCombatDetails() {
            return "Frogman Shaman";
        }
    }

    private static class FrogmanChieftainCombat extends CombatSubScene {
        public FrogmanChieftainCombat(int col, int row) {
            super(col, row, List.of(new FrogmanLeaderEnemy('A'), new FrogmanScoutEnemy('B'),
                    new FrogmanChiefEnemy('C'), new FrogmanScoutEnemy('B'), new FrogmanLeaderEnemy('A')),
                    true);
        }

        @Override
        protected String getCombatDetails() {
            return "Frogman Chieftain";
        }
    }

    private static class PerceptFrogmenLanguageSubScene extends CollaborativeSkillCheckSubScene {
        private FrogmanPortraitEvent event;

        public PerceptFrogmenLanguageSubScene(int col, int row) {
            super(col, row, Skill.Perception, 8,
                    "First, let's try to get a sense of how the frogmen communicate.");
        }

        @Override
        protected void subSceneIntro(Model model, QuestState state) {
            super.subSceneIntro(model, state);
            this.event = new FrogmanPortraitEvent(model);
            event.doEvent(model);
        }

        @Override
        protected void subSceneOutro(Model model, QuestState state, boolean skillSuccess) {
            event.doEventPartTwo(model, skillSuccess);
        }

        private static class FrogmanPortraitEvent extends DailyEventState {
            public FrogmanPortraitEvent(Model model) {
                super(model);
            }

            @Override
            protected void doEvent(Model model) {
                showExplicitPortrait(model, new FrogmanAppearance(), "Frogman Villager");
                portraitSay("Shlrrp-chiirp chi chi shsh llrrrp, chi chi chirp shllrrip?");
                leaderSay("This is going to be harder than I thought...");
            }

            public void doEventPartTwo(Model model, boolean success) {
                if (success) {
                    leaderSay("I think there's a pattern to their sounds...");
                    portraitSay("Chirri-Shhhl?");
                } else {
                    leaderSay("Who can understand such an abominable sounds? SPEAK CLEARLY!!!");
                    portraitSay("Urrr, shlupp shlupp! Glrlrluuuhu!");
                    if (model.getParty().size() > 1) {
                        GameCharacter other = model.getParty().getRandomPartyMember(model.getParty().getLeader());
                        partyMemberSay(other, "I think you made him angry. Or is it a her?");
                    }
                    println("The frogmen suddenly attack you!");
                }
                model.getLog().waitForAnimationToFinish();
                removePortraitSubView(model);
            }
        }
    }

    private static class UnderstandFrogmenLanguageSubScene extends SoloSkillCheckSubScene {
        private FrogmanPortrait2Event event;

        public UnderstandFrogmenLanguageSubScene(int col, int row) {
            super(col, row, Skill.Logic, 7,
                    "Can anybody make any sense out of their garbled words?");
        }

        @Override
        protected void subSceneIntro(Model model, QuestState state) {
            super.subSceneIntro(model, state);
            this.event = new FrogmanPortrait2Event(model);
            event.doEvent(model);
        }

        @Override
        protected void subSceneOutro(Model model, QuestState state, boolean skillSuccess) {
            event.doPartTwo(model, skillSuccess, getPerformer());
        }

        private static class FrogmanPortrait2Event extends DailyEventState {
            public FrogmanPortrait2Event(Model model) {
                super(model);
            }

            @Override
            protected void doEvent(Model model) {
                showExplicitPortrait(model, new FrogmanAppearance(), "Frogman Villager");
                portraitSay("Gluhurrip plrrpi shhshhlurrilurp!");
            }

            public void doPartTwo(Model model, boolean skillSuccess, GameCharacter performer) {
                if (skillSuccess) {
                    partyMemberSay(performer, "I think he said: 'Please leave our village, we fear outsiders'.");
                    portraitSay("Chi chi glurg!");
                } else {
                    partyMemberSay(performer, "I think he said: 'Grab my spear.");
                    println(performer.getFirstName() + " approaches the frogman and attempts to grab its spear.");
                    portraitSay("Shlurri! Shlurri! Shlurriiii!");
                    leaderSay("Oh oh... he's pissed.");
                    println("The frogmen suddenly attack you!");
                }
                model.getLog().waitForAnimationToFinish();
                removePortraitSubView(model);
            }
        }
    }

    private static class PersuadeFrogmenSubScene extends CollaborativeSkillCheckSubScene {
        private FrogmanPortrait3Event event;

        public PersuadeFrogmenSubScene(int col, int row) {
            super(col, row, Skill.Persuade, 8,
                    "Maybe we can persuade them to negotiate with the townspeople?");
        }

        @Override
        protected void subSceneIntro(Model model, QuestState state) {
            super.subSceneIntro(model, state);
            this.event = new FrogmanPortrait3Event(model);
            this.event.doEvent(model);
        }

        @Override
        protected void subSceneOutro(Model model, QuestState state, boolean skillSuccess) {
            this.event.doEventPartTwo(model, skillSuccess);
        }

        private static class FrogmanPortrait3Event extends DailyEventState {
            public FrogmanPortrait3Event(Model model) {
                super(model);
            }

            @Override
            protected void doEvent(Model model) {
                showExplicitPortrait(model, new FrogmanAppearance(), "Frogman Villager");
                portraitSay("Glurri chirp?");
            }

            public void doEventPartTwo(Model model, boolean skillSuccess) {
                if (skillSuccess) {
                    leaderSay("We... come... in... peace.");
                    println(model.getParty().getLeader().getFirstName() + " gestures carefully while speaking to the frogman.");
                    portraitSay("Shlurrpirurrrp.");
                    leaderSay("Stop... attacking... village?");
                    portraitSay("Churri churr shlurrp.");
                    leaderSay("I think... maybe he understands?");
                } else {
                    leaderSay("Okay... here it goes. SHLURP, SHLURP, RIBBIT-SHLURP, get it?");
                    portraitSay("Shlurri! Shlurri! Shlurriiii!");
                    if (model.getParty().size() > 1) {
                        GameCharacter other = model.getParty().getRandomPartyMember(model.getParty().getLeader());
                        partyMemberSay(other, "I don't think you said that right.");
                        leaderSay("How can you tell?");
                        partyMemberSay(other, "Oh you know, from the way they're coming at us with their weapons drawn!");
                    }
                    println("The frogmen attack you!");
                }
                model.getLog().waitForAnimationToFinish();
                removePortraitSubView(model);
            }
        }
    }
}
