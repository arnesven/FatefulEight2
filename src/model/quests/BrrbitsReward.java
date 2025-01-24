package model.quests;

import model.Model;
import model.characters.BrrbitCharacter;
import model.characters.GameCharacter;
import model.characters.PersonalityTrait;
import model.characters.appearance.CharacterAppearance;
import model.classes.Skill;
import model.classes.SkillCheckResult;
import model.enemies.Enemy;
import model.enemies.LizardmanEnemy;
import model.enemies.MuggerEnemy;
import model.horses.Horse;
import model.horses.HorseHandler;
import model.items.weapons.AxeWeapon;
import model.items.weapons.Pickaxe;
import model.items.weapons.RustyPickaxe;
import model.quests.scenes.CombatSubScene;
import model.quests.scenes.DecisionJunctionWithEvent;
import model.quests.scenes.SoloSkillCheckSubScene;
import model.quests.scenes.StoryJunctionWithEvent;
import model.races.FrogmanAppearance;
import model.races.FrogmanRace;
import model.states.DailyEventState;
import model.states.QuestState;
import model.states.events.GelatinousBlobEvent;
import sound.BackgroundMusic;
import view.BorderFrame;
import view.MyColors;
import view.combat.CombatTheme;
import view.combat.GrassCombatTheme;
import view.combat.MountainCombatTheme;
import view.combat.TownCombatTheme;
import view.sprites.Sprite;
import view.sprites.Sprite32x32;
import view.widget.QuestBackground;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BrrbitsReward extends Quest {
    private static final String FROGMAN_NAME = "Brrbit";
    private static final String INTRO = "The party is casually strolling down the street when they happen to see " +
            "a gang of thugs ruffing up a lone frogman. Intrigued, the party joins the fight to drive off the bandits.";
    private static final String ENDING = "You return to town.";
    private static final CharacterAppearance PORTRAIT = new FrogmanAppearance();
    private BrrbitCharacter frogman;
    private boolean hasSeenHorses;
    private boolean foundOreVein;
    private CombatTheme theme;
    private static final List<QuestBackground> bgSprites = makeBackgroundSprites();

    public BrrbitsReward() {
        super(FROGMAN_NAME + "'s Reward", "Brrbit", QuestDifficulty.EASY,
                new Reward(0, 0, 50), 3, INTRO, ENDING);
    }

    @Override
    protected void resetQuest() {
        super.resetQuest();
        hasSeenHorses = false;
        foundOreVein = false;
        theme = new TownCombatTheme();
    }

    @Override
    protected List<String> getSpecialRewards() {
        return List.of("???");
    }

    @Override
    public boolean drawTownOrCastleInBackground() {
        return true;
    }

    @Override
    public CharacterAppearance getPortrait() {
        return PORTRAIT;
    }

    @Override
    protected List<QuestScene> buildScenes() {
        frogman = new BrrbitCharacter(FROGMAN_NAME);
        return List.of(new QuestScene("Rescue " + FROGMAN_NAME, List.of(new ThugsCombatScene(0, 1, frogman))),
                new QuestScene("The Swamp", List.of(new BlobsCombatScene(5, 1, frogman))),
                new QuestScene("The Mountains", List.of(new LizardmenCombatScene(5, 3, frogman))),
                new QuestScene(FROGMAN_NAME + "'s hut", List.of(new PlayGameWithFrogman(6, 6))));
    }

    @Override
    protected List<QuestJunction> buildJunctions(List<QuestScene> scenes) {
        QuestStartPointWithoutDecision start = new QuestStartPointWithoutDecision(new QuestEdge(scenes.get(0).get(0)),
                "Let's help the poor bugger. Who knows, maybe the frogman will reward us for helping him?");

        StoryJunction lootCaravan = new StoryJunction(3, 1, new QuestEdge(getFailEndingNode())) {

            @Override
            public String getDescription() {
                return "Loot caravan";
            }

            @Override
            protected void doAction(Model model, QuestState state) {
                state.leaderSay("No " + FROGMAN_NAME + ", we're staying here and gathering as much stuff as we can.");
                state.println("Your party starts searching the abandoned caravan.");
                state.println(FROGMAN_NAME + " is clearly upset by your refusal to follow him. He stomps his feet " +
                        "and babbles for a few minutes but then quickly shuffles off.");
                state.println("The work takes more than an hour, but in the end it was a pretty good haul.");
                state.println("You gain 20 materials.");
                model.getParty().getInventory().addToMaterials(20);
                state.println("You gain 20 ingredients.");
                model.getParty().getInventory().addToIngredients(20);
                state.println("You gain 20 rations.");
                model.getParty().addToFood(20);
                state.println("You gain 20 gold.");
                model.getParty().addToGold(20);
                state.println("You gain 60 obols.");
                model.getParty().addToObols(60);
                model.getLog().waitForAnimationToFinish();
            }
        };

        StoryJunction followFrogman1 = new StoryJunction(5, 0, new QuestEdge(scenes.get(1).get(0))) {
            @Override
            public String getDescription() {
                return "Follow " + FROGMAN_NAME;
            }

            @Override
            protected void doAction(Model model, QuestState state) {
                state.leaderSay("Alright " + FROGMAN_NAME + ", I'm guessing whatever you have got in store for " +
                        "us is better than these rotting old wagons. Lead on friend!");
                model.getLog().waitForAnimationToFinish();
            }
        };

        DecisionJunctionWithEvent dj1 = new DecisionJunctionWithEvent(3, 0,
                List.of(new QuestEdge(lootCaravan), new QuestEdge(followFrogman1))) {
            @Override
            public DailyEventState makeEvent(Model model, QuestState state) {
                return new AbandonedCaravanRewardEvent(model);
            }
        };

        StoryJunctionWithEvent sj1 = new StoryJunctionWithEvent(1, 0, new QuestEdge(dj1, QuestEdge.VERTICAL)) {
            @Override
            public DailyEventState makeEvent(Model model, QuestState state) {
                return new MeetFrogmanEvent(model);
            }
        };

        StoryJunction tameHorses = new StoryJunction(3, 2, new QuestEdge(getFailEndingNode())) {

            @Override
            public String getDescription() {
                return hasSeenHorses ? "Tame horses" : "???";
            }

            @Override
            protected void doAction(Model model, QuestState state) {
                state.leaderSay("No " + FROGMAN_NAME + ", we're staying here taming these horses.");
                state.println("Your party starts trying to round up the horses."); // TODO: Could be a chance of Brrbit waiting for you?
                state.println(FROGMAN_NAME + " is clearly upset by your refusal to follow him. He stomps his feet " +
                        "and babbles for a few minutes but then quickly shuffles off.");
                for (GameCharacter gc : model.getParty().getPartyMembers()) {
                    if (model.getParty().canBuyMoreHorses()) {
                        Horse horse = HorseHandler.generateHorse();
                        state.println(gc.getName() + " tames a " + horse.getName() + ".");
                        model.getParty().getHorseHandler().addHorse(horse);
                    }
                }
                model.getLog().waitForAnimationToFinish();
            }
        };

        StoryJunction followFrogman2 = new StoryJunction(6, 2, new QuestEdge(scenes.get(2).get(0), QuestEdge.VERTICAL)) {
            @Override
            public String getDescription() {
                return "Follow " + FROGMAN_NAME;
            }

            @Override
            protected void doAction(Model model, QuestState state) {
                state.leaderSay("Alright " + FROGMAN_NAME + ", the horses was not what you wanted to show us. " +
                        "Perhaps my curiosity is getting the better of me, but we're following you. Show the way.");
            }
        };

        DecisionJunctionWithEvent dj2 = new DecisionJunctionWithEvent(4, 2,
                List.of(new QuestEdge(tameHorses), new QuestEdge(followFrogman2, QuestEdge.VERTICAL))) {
            @Override
            public DailyEventState makeEvent(Model model, QuestState state) {
                return new WildHorsesRewardEvent(model);
            }
        };

        StoryJunction mineSilver = new StoryJunction(3, 4, new QuestEdge(getFailEndingNode())) {
            @Override
            public String getDescription() {
                if (foundOreVein) {
                    return "Mine silver ore vein";
                }
                return "???";
            }

            @Override
            protected void doAction(Model model, QuestState state) {
                state.leaderSay(FROGMAN_NAME + ", sorry, but this is too good to pass on.");
                state.println("Your party starts trying to mine the silver ore vein.");
                state.println(FROGMAN_NAME + " desperately tries to get your attention, but he tires quickly and then walks off " +
                        "toward the end of the valley.");
                for (GameCharacter gc : model.getParty().getPartyMembers()) {
                    int multiplier = 1;
                    if (gc.getEquipment().getWeapon().isOfType(AxeWeapon.class)) {
                        multiplier = 2;
                    } else if (gc.getEquipment().getWeapon().isOfType(Pickaxe.class) ||
                                gc.getEquipment().getWeapon().isOfType(RustyPickaxe.class)) {
                        multiplier = 4;
                    }
                    SkillCheckResult result = gc.testSkill(model, Skill.Labor);
                    int silverAmount = multiplier * result.getModifiedRoll();
                    state.println(gc.getName() + " mines silver worth " + silverAmount + " gold.");
                    model.getParty().addToGold(silverAmount);
                }
                model.getLog().waitForAnimationToFinish();

            }
        };

        StoryJunction followFrogman3 = new StoryJunction(4, 5, new QuestEdge(scenes.get(3).get(0))) {

            @Override
            public String getDescription() {
                return "Follow " + FROGMAN_NAME;
            }

            @Override
            protected void doAction(Model model, QuestState state) {
                state.leaderSay("But but... the silver... ah... whatever. If we stop now we're never " +
                        "gonna get to know what he wants to give us. Maybe we can find this place again some time.");
            }
        };

        DecisionJunctionWithEvent dj3 = new DecisionJunctionWithEvent(4, 4,
                List.of(new QuestEdge(mineSilver), new QuestEdge(followFrogman3, QuestEdge.VERTICAL))) {
            @Override
            public DailyEventState makeEvent(Model model, QuestState state) {
                return new FindSilverOreVeinEvent(model);
            }
        };

        return List.of(start, sj1,
                dj1, lootCaravan, followFrogman1,
                dj2, tameHorses, followFrogman2,
                dj3, mineSilver, followFrogman3);
    }

    @Override
    protected void connectScenesToJunctions(List<QuestScene> scenes, List<QuestJunction> junctions) {
        scenes.get(0).get(0).connectFail(getFailEndingNode(), QuestEdge.VERTICAL);
        scenes.get(0).get(0).connectSuccess(junctions.get(1));

        scenes.get(1).get(0).connectFail(getFailEndingNode(), QuestEdge.VERTICAL);
        scenes.get(1).get(0).connectSuccess(junctions.get(5));

        scenes.get(2).get(0).connectFail(getFailEndingNode(), QuestEdge.VERTICAL);
        scenes.get(2).get(0).connectSuccess(junctions.get(8));

        scenes.get(3).get(0).connectFail(getFailEndingNode());
        scenes.get(3).get(0).connectSuccess(getSuccessEndingNode());
    }

    @Override
    public MyColors getBackgroundColor() {
        return MyColors.BLACK;
    }

    @Override
    public BackgroundMusic getMusic() {
        return BackgroundMusic.lightQuestSong;
    }

    @Override
    public CombatTheme getCombatTheme() {
        return theme;
    }


    private static abstract class ProtectFrogmanCombatScene extends CombatSubScene {
        private final BrrbitCharacter frogman;

        public ProtectFrogmanCombatScene(BrrbitCharacter frogman, int col, int row, List<Enemy> enemies, boolean fleeingEnabled) {
            super(col, row, enemies, fleeingEnabled);
            this.frogman = frogman;
        }


        @Override
        protected List<GameCharacter> getAllies() {
            return List.of(frogman);
        }

        @Override
        public QuestEdge run(Model model, QuestState state) {
            QuestEdge toReturn = super.run(model, state);
            if (frogman.isDead()) {
                state.println(frogman.getName() + " has died. Dispirited, the party returns to town.");
                return getFailEdge();
            }
            return toReturn;
        }
    }

    private static class ThugsCombatScene extends ProtectFrogmanCombatScene {
        public ThugsCombatScene(int col, int row, BrrbitCharacter frogman) {
            super(frogman, col, row, List.of(new MuggerEnemy('A'), new MuggerEnemy('A'),
                    new MuggerEnemy('A')), true);
        }

        @Override
        protected String getCombatDetails() {
            return "Thugs";
        }
    }

    private static class MeetFrogmanEvent extends DailyEventState {
        public MeetFrogmanEvent(Model model) {
            super(model);
        }

        protected void doEvent(Model model) {
            showExplicitPortrait(model, PORTRAIT, FROGMAN_NAME);
            leaderSay("Hey there fellow, are you all right?");
            portraitSay(FrogmanRace.makeFrogmanMumboJumbo(6) + "!");
            leaderSay("Uhm... okay....");
            println("The frogman seems very happy and agitated. He is pointing rapidly at " +
                    model.getParty().getLeader().getFirstName() + ".");
            leaderSay("Me? Uhm, I'm " + model.getParty().getLeader().getName() + ", and this is may company of " +
                    "adventurers. What's your name?");
            GameCharacter leader = model.getParty().getLeader();
            portraitSay(FrogmanRace.makeFrogmanMumboJumbo(3) + " Ibbr Bbrribbrit.");
            leaderSay(FROGMAN_NAME + " huh? Well " + FROGMAN_NAME + " I guess this was your lucky day.");
            if (leader.hasPersonality(PersonalityTrait.greedy) ||
                    leader.hasPersonality(PersonalityTrait.rude)) {
                leaderSay("How about you cough up some kind of reward for our bravery?");
            } else if (leader.hasPersonality(PersonalityTrait.benevolent) ||
                    leader.hasPersonality(PersonalityTrait.friendly)) {
                leaderSay("Be careful in the future when you are in town. Lot's of unfriendly characters about.");
            } else {
                leaderSay("We'll be on our way now.");
            }
            println(FROGMAN_NAME + " is tugging at " + leader.getFirstName() + "'s sleeve, like he wants " +
                    "you to follow him.");
            leaderSay("So you do want to give us some kind of reward? Okay, we'll follow you.");
            if (model.getParty().size() > 1) {
                boolean said = randomSayIfPersonality(PersonalityTrait.gluttonous, List.of(leader), "I hope it's something yummy!");
                said |= randomSayIfPersonality(PersonalityTrait.greedy, List.of(leader), "I hope it's a mountain of gold!");
                said |= randomSayIfPersonality(PersonalityTrait.critical, List.of(leader), "I'm sure we will be disappointed.");
            }
            println("You follow " + FROGMAN_NAME + " out of town.");
            removePortraitSubView(model);
        }
    }

    private static class AbandonedCaravanRewardEvent extends DailyEventState {
        public AbandonedCaravanRewardEvent(Model model) {
            super(model);
        }

        @Override
        protected void doEvent(Model model) {
            println(FROGMAN_NAME + " leads the party into a dense forest. After a while you emerge on to a road which " +
                    "cuts through the woods.");
            leaderSay("Uhm... " + FROGMAN_NAME + ". Where are we?");
            showExplicitPortrait(model, PORTRAIT, FROGMAN_NAME);
            portraitSay(FrogmanRace.makeFrogmanMumboJumbo(4) + ". " + FrogmanRace.makeFrogmanMumboJumbo(5) + ".");
            if (model.getParty().size() > 1) {
                leaderSay("Anybody get any of that?");
            }
            println(FROGMAN_NAME + " continues down the road and the party reluctantly follow. A few minutes later you spot " +
                    "several broken down wagons. There are barrels, crates and sacks scattered all over the place. There " +
                    "is blood on the ground, but no dead bodies.");
            if (model.getParty().size() > 1) {
                GameCharacter other = model.getParty().getRandomPartyMember(model.getParty().getLeader());
                partyMemberSay(other, "An ambush by raiders?");
                leaderSay("No, all the goods are still here...");
            }
            leaderSay("Perhaps they were attacked by wolves, or something... But who cares? " +
                    "There's tons of free loot scattered about. It will take a little time, but there's bound to be " +
                    "some good stuff to be salvaged here.");
            if (model.getParty().size() > 1) {
                GameCharacter other = model.getParty().getRandomPartyMember(model.getParty().getLeader());
                partyMemberSay(other, FROGMAN_NAME + " must have come this way earlier, " +
                        "but couldn't salvage the stuff himself. Now he's letting us have it as a reward?");
            }
            leaderSay("This is great " + FROGMAN_NAME + ", thank you!");
            portraitSay(FrogmanRace.makeFrogmanMumboJumbo(1) + "?");
            println(FROGMAN_NAME + " however, seems disinterested in the abandoned caravan. " +
                    "He gently pulls at your sleeve as if he wants you to keep following him.");
            leaderSay("Wait, this isn't the reward you had in mind for us?");
            println("You could stay here and loot the abandoned caravan, but " + FROGMAN_NAME +
                    " will probably leave you (quest will be failed). Or you can follow " + FROGMAN_NAME +
                    " to find out what he wants to share with you.");
            model.getLog().waitForAnimationToFinish();
            removePortraitSubView(model);
        }
    }

    private class BlobsCombatScene extends ProtectFrogmanCombatScene {
        public BlobsCombatScene(int col, int row, BrrbitCharacter frogman) {
            super(frogman, col, row, GelatinousBlobEvent.makeRandomBlobEnemies(2, 2), true);
        }

        @Override
        public QuestEdge run(Model model, QuestState state) {
            state.println("The thick forest you've been traveling through has now become more of a swamp.");
            state.println("Suddenly, strange gelatinous blobs appear all around the party.");
            state.leaderSay("Uh-oh, those blobs look vicious. We've better protect " + FROGMAN_NAME +
                    " if we ever want to find out where he's taking us.");
            theme = new GrassCombatTheme();
            return super.run(model, state);
        }

        @Override
        protected String getCombatDetails() {
            return "Blobs";
        }
    }

    private class WildHorsesRewardEvent extends DailyEventState {
        public WildHorsesRewardEvent(Model model) {
            super(model);
        }

        @Override
        protected void doEvent(Model model) {
            println("You finally get out of the swamp and come to a large meadow.");
            leaderSay("Ah, this is much nicer.");
            println("You can hear whinnying here.");
            GameCharacter other = null;
            if (model.getParty().size() > 1) {
                other = model.getParty().getRandomPartyMember(model.getParty().getLeader());
                partyMemberSay(other, "Wild horses?");
            }
            println("A group of wild horses come trotting toward you.");
            leaderSay("Wow, this is a rare breed of horses, Acuramix. They're supposedly very strong, " +
                    "clever and loyal. If we could tame them they would probably be very good mounts.");
            if (other != null) {
                partyMemberSay(other, "Now I get it! " + FROGMAN_NAME + " knew about this place and wanted us " +
                        "to come here to get the horses.");
            }
            leaderSay(FROGMAN_NAME + " is this our reward? These lovely mares and steeds?");
            showExplicitPortrait(model, PORTRAIT, FROGMAN_NAME);
            portraitSay(FrogmanRace.makeFrogmanMumboJumbo(5) + ".");
            leaderSay("What a great reward. Thank you " + FROGMAN_NAME + "! Now how to tame these horses?");
            println(FROGMAN_NAME + " seems surprised when " + model.getParty().getLeader().getFirstName() + " turns away from him.");
            portraitSay(FrogmanRace.makeFrogmanMumboJumbo(4) + "?");
            leaderSay("What is it " + FROGMAN_NAME + "?");
            portraitSay(FrogmanRace.makeFrogmanMumboJumbo(6) + "!");
            leaderSay("I'm sorry " + FROGMAN_NAME + ", I don't...");
            println("Once again, " + FROGMAN_NAME + " starts tugging at your sleeve.");
            leaderSay("I don't understand. You want us to keep following you? I thought you wanted us to see these horses.");
            portraitSay(FrogmanRace.makeFrogmanMumboJumbo(3) + "!");
            println("You could stay here and attempt to tame the wild horses, but " + FROGMAN_NAME +
                    " will probably leave you (quest will be failed). Or you can keep following " + FROGMAN_NAME + ".");
            model.getLog().waitForAnimationToFinish();
            hasSeenHorses = true;
            removePortraitSubView(model);
        }
    }

    private class LizardmenCombatScene extends ProtectFrogmanCombatScene {
        public LizardmenCombatScene(int col, int row, BrrbitCharacter frogman) {
            super(frogman, col, row,
                    List.of(new LizardmanEnemy('A'), new LizardmanEnemy('A'),
                            new LizardmanEnemy('A'), new LizardmanEnemy('A')), true);
        }

        @Override
        public QuestEdge run(Model model, QuestState state) {
            state.println("The road leads up into the mountain. Suddenly a group of " +
                    "lizardmen approach you with weapons drawn.");
            state.leaderSay("They look hostile. Let's keep it together and protect " + FROGMAN_NAME + "!");
            theme = new MountainCombatTheme();
            return super.run(model, state);
        }

        @Override
        protected String getCombatDetails() {
            return "Lizardmen";
        }
    }

    private class FindSilverOreVeinEvent extends DailyEventState {
        public FindSilverOreVeinEvent(Model model) {
            super(model);
        }

        @Override
        protected void doEvent(Model model) {
            println(FROGMAN_NAME + " leads you into a mountain pass. There, in a narrow gorge, " +
                    "something glimmering suddenly catches your eye.");
            leaderSay("What is this... An ore vein, of silver!");
            showExplicitPortrait(model, PORTRAIT, FROGMAN_NAME);
            portraitSay(FrogmanRace.makeFrogmanMumboJumbo(4) + "!");
            leaderSay("This is fantastic " + FROGMAN_NAME + "! Now I understand why you kept pulling us along. " +
                    "We could get very rich if we just put in some elbow grease here.");
            portraitSay(FrogmanRace.makeFrogmanMumboJumbo(3) + "?");
            if (model.getParty().size() > 1) {
                leaderSay("Bring out the pick-axes gang, we're setting up camp here.");
            } else {
                leaderSay("Now where did I put my pick-axe?");
            }
            portraitSay(FrogmanRace.makeFrogmanMumboJumbo(2) + "!");
            leaderSay("What now " + FROGMAN_NAME + "?");
            println(FROGMAN_NAME + " points furiously towards the end of the valley.");
            leaderSay("Seriously? The ore vein isn't the reward? What could possibly be better than this?");
            portraitSay(FrogmanRace.makeFrogmanMumboJumbo(5) + "!");
            leaderSay("This is unbelievable.");
            println("You could stay here mine the silver ore vein, but " + FROGMAN_NAME +
                    " will not wait for you and is anxiously wanting for you to come with him. " +
                    "Do you stay (quest will be failed). Or you can keep following " + FROGMAN_NAME + ".");
            model.getLog().waitForAnimationToFinish();
            foundOreVein = true;
            removePortraitSubView(model);
        }
    }

    private static class PlayGameWithFrogman extends SoloSkillCheckSubScene {
        public PlayGameWithFrogman(int col, int row) {
            super(col, row, Skill.Logic, 5, "");
        }

        @Override
        public String getDescription() {
            return "";
        }

        @Override
        public String getDetailedDescription() {
            return "???";
        }

        @Override
        protected void subSceneIntro(Model model, QuestState state) {
            new DailyEventState(model) {
                @Override
                protected void doEvent(Model model) {
                    println(FROGMAN_NAME + " leads you into a forest again. Soon, you spot a small hut. " +
                            FROGMAN_NAME + " eagerly beckons you inside.");
                    leaderSay("Our reward is in there?");
                    showExplicitPortrait(model, PORTRAIT, FROGMAN_NAME);
                    portraitSay(FrogmanRace.makeFrogmanMumboJumbo(3) + ".");
                    println(FROGMAN_NAME + " nods eagerly, and leads you inside.");
                    println("The hut seems to be made for a single inhabitant. There's a small cot. But there in the " +
                            "center of the room is a small table with two low stools beside it. On the table is a flat piece of " +
                            "wood which has been painted in various colors. Upon it stands some other pieces of wood.");
                    portraitSay(FrogmanRace.makeFrogmanMumboJumbo(6) + ".");
                    println("Sitting down at the table, " + FROGMAN_NAME + " gestures for you to do the same.");
                    leaderSay("Is this some kind of game?");
                    if (model.getParty().size() > 1) {
                        GameCharacter other = model.getParty().getRandomPartyMember(model.getParty().getLeader());
                        partyMemberSay(other, "What? We came all this way just to play some kind of game with this miscreant? " +
                                "What a waste of time. Let's just get out of here.");
                    }
                    leaderSay("Let's just do it.");
                    removePortraitSubView(model);
                }
            }.doTheEvent(model);
        }

        @Override
        protected void subSceneOutro(Model model, QuestState state, boolean skillSuccess) {
            if (skillSuccess) {
                state.println("You quickly grasp the gist of the game and sit for a while with " + FROGMAN_NAME);
                state.println(FROGMAN_NAME + " seems elated that you played his game.");
            } else {
                state.println("You try to make sense of " + FROGMAN_NAME + " game, but no matter how hard you try " +
                        "you don't understand the concepts of it. " + FROGMAN_NAME + " seems bored by your failing attempts.");
            }
            if (model.getParty().size() > 1) {
                GameCharacter other = model.getParty().getRandomPartyMember(model.getParty().getLeader());
                state.partyMemberSay(other, "So we came all this way for nothing?");
                state.leaderSay("Well, at least we got some exercise...");
            }
            state.println("You take your leave of " + FROGMAN_NAME + ".");
        }
    }
    @Override
    public List<QuestBackground> getBackgroundSprites() {
        return bgSprites;
    }

    private static List<QuestBackground> makeBackgroundSprites() {
        List<QuestBackground> result = new ArrayList<>();
        Random rand = new Random(1234);
        for (int row = 0; row < 9 ; ++row) {
            for (int col = 0; col < 8; ++col) {
                result.add(new QuestBackground(new Point(col, row),
                        GrassCombatTheme.grassSprites[rand.nextInt(GrassCombatTheme.grassSprites.length)],
                        true));
                if (row == 8) {
                    result.add(new QuestBackground(new Point(col, row),
                            GrassCombatTheme.grassSprites[rand.nextInt(GrassCombatTheme.grassSprites.length)],
                            false));
                }
            }
        }
        final Sprite woods = new Sprite32x32("woodsqmb", "quest.png", 0x53,
                MyColors.BLACK, MyColors.BROWN, MyColors.DARK_GREEN, MyColors.GREEN);
        Sprite32x32 bogSprite = new Sprite32x32("bogsprite", "quest.png", 0x65,
                MyColors.DARK_BROWN, MyColors.DARK_GREEN, MyColors.TAN, MyColors.GREEN);
        Sprite32x32 mountains = new Sprite32x32("mountains", "quest.png", 0x66,
                MyColors.BLACK, MyColors.WHITE, MyColors.GRAY, MyColors.GREEN);
        for (int row = 0; row < 2; row++) {
            for (int col = row+2; col < 8; col++) {
                result.add(new QuestBackground(new Point(col, row), woods, true));
            }
        }
        for (int y = 1; y < 3; ++y) {
            for (int x = 5; x < 8; ++x) {
                if (y != 1 || x > 5) {
                    result.add(new QuestBackground(new Point(x, y), bogSprite, true));
                }
            }
        }
        for (int y = 3; y < 6; ++y) {
            for (int x = 4; x < 8; ++x) {
                if (y != 3 || x == 7) {
                    result.add(new QuestBackground(new Point(x, y), mountains, true));
                }
            }
        }
        for (int y = 6; y < 8; ++y) {
            for (int x = 6; x < 8; ++x) {
                result.add(new QuestBackground(new Point(x, y), woods, true));
            }
        }
        final Sprite huts = new Sprite32x32("frogmenhuts", "quest.png", 0x1F,
                MyColors.DARK_BROWN, MyColors.DARK_GREEN, MyColors.TAN, MyColors.GREEN);
        result.add(new QuestBackground(new Point(7, 7), huts, true));

        return result;
    }
}
