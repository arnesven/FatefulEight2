package model.quests;

import model.Model;
import model.achievements.Achievement;
import model.characters.GameCharacter;
import model.characters.appearance.AdvancedAppearance;
import model.characters.appearance.CharacterAppearance;
import model.classes.Classes;
import model.classes.Skill;
import model.classes.npcs.RegentClass;
import model.combat.conditions.PossessedCondition;
import model.enemies.*;
import model.items.special.CommunicatorDevice;
import model.journal.PartFourStoryPart;
import model.journal.StoryPart;
import model.journal.ZeppelinStoryPart;
import model.map.CastleLocation;
import model.quests.scenes.CollectiveSkillCheckSubScene;
import model.quests.scenes.CombatSubScene;
import model.quests.scenes.SoloSkillCheckSubScene;
import model.quests.scenes.StoryJunctionWithEvent;
import model.states.DailyEventState;
import model.states.GameState;
import model.states.QuestState;
import sound.BackgroundMusic;
import util.MyLists;
import view.MyColors;
import view.sprites.Sprite;
import view.sprites.Sprite32x32;
import view.subviews.*;
import view.widget.QuestBackground;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class MindMachineQuest extends MainQuest {
    public static final String QUEST_NAME = "Mind Machine";
    private static final String INTRO_TEXT = "You find yourself back at the sewage pipe from you once escaped. " +
            "Once aside, a labyrinth of watery passageways lay between you and the castle dungeons. " +
            "If you can find the way through you may be able to get into the castle, but what will you find inside?";

    private static final String ENDING_TEXT = "TODO Ending";
    private List<QuestBackground> backgroundSprites = makeBackgroundSprites();

    public MindMachineQuest() {
        super(QUEST_NAME, "Yourself", QuestDifficulty.VERY_HARD,
                new Reward( 0, 0), 0, INTRO_TEXT, ENDING_TEXT);
    }

    @Override
    public Achievement.Data getAchievementData() {
        return makeAchievement(this, "TODO");
    }

    @Override
    protected void resetQuest() {
        super.resetQuest();
        getSuccessEndingNode().move(2, 7);
    }


    @Override
    public BackgroundMusic getMusic() {
        return BackgroundMusic.strongholdSong;
    }

    @Override
    public MainQuest copy() {
        return new MindMachineQuest();
    }

    @Override
    protected List<QuestScene> buildScenes() {
        return List.of(
                new QuestScene("The sewers", List.of(
                    new KokodrillionCombatSubScene(6, 7),        // TODO: Difficulty 7
                    new CollectiveSkillCheckSubScene(5, 8, Skill.Endurance, 1, "Yuck what a stench! I guess " +
                            "that's what the sewage from the whole castle smells like. Or is it these beasts?"),
                    new SoloSkillCheckSubScene(5, 7, Skill.Logic, 1, "These sewers are " + // TODO: 14
                            "like a maze! I know we came this way, but now I can't tell which way to go at all. Can somebody figure " +
                            "out this maze?"))),
                new QuestScene("The Castle", List.of(
                        new EvidenceChectSubScene(7, 4),
                        new CombatLordSubScene(6, 0)
                )));
    }

    @Override
    protected List<QuestJunction> buildJunctions(List<QuestScene> scenes) {
        StoryJunction sj1 = new StoryJunctionWithEvent(7, 3, new QuestEdge(scenes.get(1).get(1), QuestEdge.VERTICAL)) {
            @Override
            public DailyEventState makeEvent(Model model, QuestState state) {
                return new FreeAdvisorsEvent(model);
            }
        };

        StoryJunction sj3 = new StoryJunctionWithEvent(3, 3, new QuestEdge(getSuccessEndingNode())) {
            @Override
            public DailyEventState makeEvent(Model model, QuestState state) {
                return new ArabellasWorkshopEvent(model);
            }
        };

        StoryJunction sj2 = new StoryJunctionWithEvent(6, 1, new QuestEdge(sj3)) {
            @Override
            public DailyEventState makeEvent(Model model, QuestState state) {
                return new LordRecoveredEvent(model);
            }
        };


        QuestJunction start = new MindMachineStartingPoint(new QuestEdge(sj2, QuestEdge.VERTICAL)); // TODO: scenes.get(0).get(0)
        return List.of(start, sj1, sj2, sj3);
    }

    @Override
    protected void connectScenesToJunctions(List<QuestScene> scenes, List<QuestJunction> junctions) {
        scenes.get(0).get(0).connectSuccess(scenes.get(0).get(1), QuestEdge.VERTICAL);

        scenes.get(0).get(1).connectFail(getFailEndingNode());
        scenes.get(0).get(1).connectSuccess(scenes.get(0).get(2));

        scenes.get(0).get(2).connectFail(scenes.get(0).get(0));
        scenes.get(0).get(2).connectSuccess(scenes.get(1).get(0), QuestEdge.VERTICAL);

        scenes.get(1).get(0).connectSuccess(junctions.get(1));

        scenes.get(1).get(1).connectSuccess(junctions.get(2));
        scenes.get(1).get(1).connectFail(getFailEndingNode());
    }

    @Override
    public GameState endOfQuest(Model model, QuestState state, boolean questWasSuccess) {
        GameState toIgnore = super.endOfQuest(model, state, questWasSuccess);
        if (questWasSuccess) {
            teleportToOtherWorld(model, state, new Point(5, 5));
        }
        return toIgnore; // TODO Fix
    }

    public static void teleportToOtherWorld(Model model, GameState state, Point destinationPosition) {
        MapSubView mapSubView = new MapSubView(model);
        CollapsingTransition.transition(model, mapSubView);
        state.println("Preparing to teleport, press enter to continue.");
        state.waitForReturn();
        TeleportBetweenWorldsTransition.transition(model, mapSubView, destinationPosition);
        state.println("Press enter to continue.");
        state.waitForReturn();
        CollapsingTransition.transition(model, model.getCurrentHex().getImageSubView(model));
    }

    @Override
    public MyColors getBackgroundColor() {
        return MyColors.BLACK;
    }

    private static List<QuestBackground> makeBackgroundSprites() {
        List<QuestBackground> result = new ArrayList<>();
        for (int row = 2; row < 9; ++row) {
            for (int col = 0; col < 8; ++col) {
                Sprite spr =
                        new FlippedBgSprite("mindmachinebg"+row+":"+col, "castle.png",
                                row * 0x10 + col);

                MyColors.transformImage(spr);
                result.add(new QuestBackground(new Point(7 - col, row - 2), spr));
            }
        }
        return result;
    }

    @Override
    public List<QuestBackground> getBackgroundSprites() {
        return backgroundSprites;
    }

    private class MindMachineStartingPoint extends QuestStartPointWithoutDecision{
        public MindMachineStartingPoint(QuestEdge questEdge) {
            super(questEdge, "");
            setColumn(7);
            setRow(8);
        }
    }

    private static class FlippedBgSprite extends Sprite32x32 {
        public FlippedBgSprite(String name, String map, int num) {
            super(name, map, num, MyColors.BLACK, MyColors.WHITE, MyColors.BLUE);
            setFlipHorizontal(true);
        }
    }

    private class KokodrillionCombatSubScene extends CombatSubScene  {
        public KokodrillionCombatSubScene(int col, int row) {
            super(col, row, List.of(new KokodrillionEnemy('A')));
        }

        @Override
        protected String getCombatDetails() {
            return "Monsters";
        }

        @Override
        public List<Enemy> getEnemies() {
            return List.of(new CrowEnemy('A'));
        }
    }

    private class EvidenceChectSubScene extends QuestSubScene {
        private static final Sprite32x32 SPRITE = new Sprite32x32("lootsubscene", "quest.png", 0x26,
                MyColors.BLACK, MyColors.WHITE, MyColors.RED, MyColors.BLACK);

        public EvidenceChectSubScene(int col, int row) {
            super(col, row);
        }

        @Override
        protected MyColors getSuccessEdgeColor() {
            return MyColors.WHITE;
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
            return "Evidence Chest";
        }

        @Override
        public QuestEdge run(Model model, QuestState state) {
            state.println("You find the entrance to the castle dungeons. You spot neither prisoners nor guards.");
            state.leaderSay("This is where we came out when we fled from here.");
            if (model.getParty().size() > 1) {
                state.partyMemberSay(model.getParty().getRandomPartyMember(model.getParty().getLeader()),
                        "Hey, there's a chest here!");
                state.leaderSay("It's marked 'Evidence'. This is where we found all our gear that " +
                        "was taken from us when we were imprisoned.");
            } else {
                state.leaderSay("Here's the chest marked 'Evidence', where we found all our gear that " +
                        "was taken from us when we were imprisoned.");
            }
            state.println("Just out of curiosity, you open the chest.");
            StoryPart storyPart = MyLists.find(model.getMainStory().getStoryParts(), sp -> sp instanceof ZeppelinStoryPart);
            if (storyPart != null && !((ZeppelinStoryPart)storyPart).isXelbiMet()) {
                state.println("Inside is a small circular device, made from brass.");
                state.leaderSay("What on earth is this?");
                state.println("You're examining the strange device when you here troubled voices down the hall. " +
                        "You put the device in your pocket.");
                new CommunicatorDevice(false).addYourself(model.getParty().getInventory());
            } else {
                state.println("Inside you find a mechanical arm with a shield attached to it.");
                state.leaderSay("This is Xelbi's extra-shield-arm thing. He must have been a prisoner here.");
                state.println("You're examining the strange apparatus when you here troubled voices down the hall.");
            }
            state.leaderSay("There's somebody here. Let's go!");
            return getSuccessEdge();
        }
    }

    private class FreeAdvisorsEvent extends DailyEventState {
        public FreeAdvisorsEvent(Model model) {
            super(model);
        }

        @Override
        protected void doEvent(Model model) {
            CastleLocation castle = model.getWorld().getCastleByName(model.getMainStory().getCastleName());
            println("You enter a large cell. There, chained to the wall are " +
                    "three of " + castle.getLordName() + "'s advisors. You recognize them from the court.");
            AdvancedAppearance general = PartFourStoryPart.getGeneralAppearance(model);
            AdvancedAppearance marshal = PartFourStoryPart.getMarshalAppearance(model);
            AdvancedAppearance captain = PartFourStoryPart.getCaptainAppearance(model);
            showExplicitPortrait(model, captain, "Captain");
            portraitSay("Free us!");
            showExplicitPortrait(model, general, "General");
            portraitSay("We've been stuck here for days!");
            leaderSay("We didn't see any action from you when we were unjustly seized at this very castle!");
            showExplicitPortrait(model, marshal, "Marshal");
            portraitSay("But, but... The " + castle.getLordTitle() + "'s gone mad! " + heOrSheCap(castle.getLordGender()) +
                    " isn't " + himOrHer(castle.getLordGender()) + "self.");
            leaderSay("Well obviously. We're looking for a suspicious outsider, she's supposed to be tall, dark and fair. " +
                    "Have you seen her?");
            showExplicitPortrait(model, general, "General");
            portraitSay("We know who you're after. Free us and we'll tell you what we know.");
            println("You unshackle the three advisors.");
            leaderSay("There. Now, what's going on here?");
            portraitSay("Her name is Arabella. She was exiled many years ago for practicing necromancy and illicit sorcery.");
            GameCharacter caid = model.getMainStory().getCaidCharacter();
            if (model.getParty().getPartyMembers().contains(caid)) {
                partyMemberSay(caid, "Arabella? I remember hunting her down and bringing her to justice. " +
                        "How was she able to sneak her way back into the kigndom?");
            } else {
                showExplicitPortrait(model, captain, "Captain");
                portraitSay("I think Caid was the one who finally exposed her.");
            }
            showExplicitPortrait(model, general, "General");
            portraitSay("She must have changed her appearance, nobody recognized her when she returned to the castle some time ago. " +
                    "She claimed to be an envoy sent from a powerful lord in a distant kingdom.");
            showExplicitPortrait(model, marshal, "Marshal");
            portraitSay("It was only a short time before you yourself came to our castle and helped our lord " +
                    "by rescuing Caid.");
            if (model.getParty().getPartyMembers().contains(caid)) {
                partyMemberSay(caid, "Thanks again for that by the way.");
            }
            portraitSay("By the time the orcish raids started getting out of hand, she had inserted herself as " +
                    castle.getLordName() + "'s favorite. The " + castle.getLordTitle() + " sought her wisdom often.");
            showExplicitPortrait(model, general, "General");
            portraitSay("Needless to say, I was already suspicious!");
            leaderSay("And yet you did nothing!");
            portraitSay("I did! I went to confront her, but could not find her. She was nowhere in the castle.");
            showExplicitPortrait(model, captain, "Captain");
            portraitSay("None of us could. We thought perhaps she had returned to the remote kingdom whence she came.");
            showExplicitPortrait(model, marshal, "Marshal");
            portraitSay("But then, a few days later, I found her tinkering on some strange machine in one of our workshops. " +
                    "That's when I recognized her as Arabella!");
            showExplicitPortrait(model, general, "General");
            portraitSay("When we confronted the " + castle.getLordTitle() + " about it, " + heOrShe(castle.getLordGender()) +
                    " threw us in the dungeon!");
            leaderSay("Hmm... I wonder what she's doing.");
            portraitSay("We can give you directions to the workshop and you can deal with Arabella, but what about the " +
                    castle.getLordTitle() + "? Maybe Arabella is controlling " + hisOrHer(castle.getLordGender()) + " or something?");
            leaderSay("Don't worry, " + iOrWe() + " will sort " + hisOrHer(castle.getLordGender()) + " out. " +
                    "You have another problem to deal with.");
            portraitSay("What do you mean?");
            leaderSay("This castle is about to be stormed by the combined forces of the neighboring kingdoms.");
            showExplicitPortrait(model, captain, "Captain");
            portraitSay("What!?");
            leaderSay("Yes. You must immediately go and try to parley with them, or the castle will be sacked.");
            showExplicitPortrait(model, marshal, "Marshal");
            portraitSay("It seems we have no choice.");
            leaderSay("You don't. Now go. Leave Arabella and the " + castle.getLordTitle() + " to us.");
            showExplicitPortrait(model, general, "General");
            portraitSay("Before you go, a warning. The " + castle.getLordTitle() + " is surrounded by " + hisOrHer(castle.getLordGender()) + " elite guard.");
            leaderSay("Elite?");
            portraitSay("Yes. They are highly skilled battle mages. Whatever you plan to do, be very careful.");
            showExplicitPortrait(model, marshal, "Marshal");
            portraitSay("Please spare the " + castle.getLordTitle() + "'s life. I'm convinced " + heOrShe(castle.getLordGender()) +
                    " is simply being manipulated by this villain.");
            leaderSay(iOrWeCap() + " will do what " + iOrWe() + " can.");
            model.getLog().waitForAnimationToFinish();
            removePortraitSubView(model);
        }
    }

    private class CombatLordSubScene extends CombatSubScene {
        private final ArrayList<Enemy> enemies;

        public CombatLordSubScene(int col, int row) {
            super(col, row, List.of(new BattleMageEnemy('B')));
            this.enemies = new ArrayList<>();
        }

        @Override
        public ArrayList<Enemy> getEnemies() {
            return enemies;
        }

        @Override
        public QuestEdge run(Model model, QuestState state) {
            CastleLocation castle = model.getWorld().getCastleByName(model.getMainStory().getCastleName());
            state.println("You enter into " + castle.getLordTitle() + "'s throne room. As you expected, his elite guards " +
                    "are by his side. He slowly gets up. His appearance is pale, and he seems tired.");
            CharacterAppearance app = model.getLordPortrait(castle);
            SubView oldSubView = model.getSubView();
            PortraitSubView psv = new PortraitSubView(model.getSubView(), app, castle.getLordName());
            model.setSubView(psv);
            state.leaderSay(castle.getLordName() + ", surrender now!");
            psv.portraitSay(model, state, "You again? Guards, eliminate them!");
            state.leaderSay("Oh come on... But we can't just kill him. What do we do?");
            GameCharacter lordCharacter = new GameCharacter(castle.getLordName(), "", app.getRace(),
                    new RegentClass(castle.getCastleColor()), app, Classes.NO_OTHER_CLASSES);
            Enemy lordEnemy = new FormerPartyMemberEnemy(lordCharacter);
            lordEnemy.addCondition(new PossessedCondition());
            enemies.add(new BattleMageEnemy('B'));
            enemies.add(new BattleMageEnemy('B'));
            enemies.add(new BattleMageEnemy('B'));
            enemies.add(lordEnemy);
            enemies.add(new BattleMageEnemy('B'));
            enemies.add(new BattleMageEnemy('B'));
            enemies.add(new BattleMageEnemy('B'));
            super.run(model, state);
            model.setSubView(oldSubView);
            if (lordEnemy.isDead()) {
                state.leaderSay("Oh no. We've killed " + castle.getLordName() + "...");
                return getFailEdge();
            }
            return getSuccessEdge();
        }

        @Override
        protected String getCombatDetails() {
            return "Battle Mages";
        }
    }

    private class LordRecoveredEvent extends DailyEventState {
        public LordRecoveredEvent(Model model) {
            super(model);
        }

        @Override
        protected void doEvent(Model model) {
           CastleLocation castle = model.getWorld().getCastleByName(model.getMainStory().getCastleName());
           showExplicitPortrait(model, model.getLordPortrait(castle), castle.getLordName());
           portraitSay("I feel terrible! Like I've been caught in a nightmare.");
           leaderSay("Good to see you've come to your senses. Your kingdom is in pieces.");
           portraitSay("Yes. Mari, my advisor has been controlling me! My actions were not my own. " +
                   "I could hear my voice talking and feel my body moving, but it was not me doing it, it was her!");
           leaderSay("How did she manage to get you to ingest that pearl?");
           portraitSay("We shared a particularly sweet pie in her chambers. Could she be one of the Quad?");
           leaderSay("We've freed some of your other advisors. They've identified her as Arabella, a dark " +
                   "wizard at large.");
           portraitSay("Yes of course, how could I have been so foolish?");
           leaderSay("She's been using powerful illusions to fool us all. The spirit of the Quad we defeated " +
                   "at the Ancient Stronghold now seems more like an elaborate hoax than an actual specter.");
           portraitSay("But we must hurry to stop her. Controlling me was not enough for her. She has been building " +
                   " a machine in her workshop. I think it's some kind of improved version of the Crimson pearl magic. " +
                   "She believes she will be able to control everybody in the kingdom once it is finished.");
           leaderSay("Just tell us where the workshop is.");
           println(castle.getLordName() + " gives you directions to the workshop.");
           portraitSay("I'll have to thank you later. You've done this kingdom a service not easily repaid.");
           leaderSay("Don't worry about that now. Go meet up with your generals and take back control of your " +
                   "kingdom.");
           portraitSay("I will. Be careful with Arabella. Do not underestimate her.");
           removePortraitSubView(model);
        }
    }

    private class ArabellasWorkshopEvent extends DailyEventState {
        public ArabellasWorkshopEvent(Model model) {
            super(model);
        }

        @Override
        protected void doEvent(Model model) {
            println("Arabella TODO");
            waitForReturn();
        }
    }
}
