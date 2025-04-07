package model.quests;

import model.Model;
import model.characters.GameCharacter;
import model.characters.PersonalityTrait;
import model.characters.appearance.*;
import model.classes.Classes;
import model.classes.Skill;
import model.enemies.CrowEnemy;
import model.enemies.Enemy;
import model.enemies.MonkeyEnemy;
import model.items.Item;
import model.items.weapons.Cutlass;
import model.items.weapons.Pistol;
import model.journal.MainStorySpawnWest;
import model.mainstory.pirates.PotentialMutineer;
import model.quests.scenes.CollaborativeSkillCheckSubScene;
import model.quests.scenes.CollectiveSkillCheckSubScene;
import model.quests.scenes.CombatSubScene;
import model.quests.scenes.StoryJunctionWithEvent;
import model.races.ElvenRace;
import model.races.HumanRace;
import model.races.Race;
import model.states.DailyEventState;
import model.states.GameState;
import model.states.QuestState;
import sound.BackgroundMusic;
import util.MyLists;
import view.MyColors;
import view.combat.CombatTheme;
import view.combat.IslandCombatTheme;
import view.sprites.Sprite;
import view.sprites.Sprite32x32;
import view.sprites.WaterSprayFrontSprite;
import view.sprites.WaterSpraySprite;
import view.subviews.ArrowMenuSubView;
import view.subviews.PortraitSubView;
import view.widget.QuestBackground;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class AvertTheMutinyQuest extends Quest {
    private static final String INTRO_TEXT =
            "The renowned pirate captain Blackbone has contracted the party to investigate a rumor that " +
            "there is a mutineer among his crew. He has narrowed it down to eight crew members. The party must pose as " +
            "fresh recruits while trying to figure out which one of the eight is the mutineer.";

    private static final String END_TEXT = "The mutineer is shackled and hauled off the ship. " +
            "Captain Blackbone rewards the party and promises to be a friend and ally for the future.";

    private static final int COLLABORATIVE_CHECK_DIFFICULTY = 14;
    private static final int COLLECTIVE_CHECK_DIFFICULTY = 7;
    private final List<PotentialMutineer> potentialMutineers;
    private final PotentialMutineer realMutineer;
    private final AdvancedAppearance firstMatePortrait;
    private final CharacterAppearance blackbonePortrait;
    private final Sprite blackboneAvatar;
    private static List<QuestBackground> backgroundSprites = makeBackgroundSprites();
    private final Sprite waterSprayFront = new WaterSprayFrontSprite();
    private final Sprite[] waterSpray = new Sprite[]{new WaterSpraySprite(0), new WaterSpraySprite(1), new WaterSpraySprite(2)};

    public AvertTheMutinyQuest() {
        super("Avert the Mutiny", "Captain Blackbone", QuestDifficulty.VERY_HARD,
                new Reward(1, 200, 0), 0, INTRO_TEXT, END_TEXT);
        MainStorySpawnWest storySpawn = new MainStorySpawnWest(); // Get this from the main story
        this.potentialMutineers = storySpawn.getPotentialMutineers();
        this.realMutineer = storySpawn.getRealMutineer();
        blackbonePortrait = PortraitSubView.makeRandomPortrait(Classes.PIRATE_CAPTAIN);
        blackboneAvatar = Classes.PIRATE_CAPTAIN.getAvatar(blackbonePortrait.getRace(), blackbonePortrait);
        firstMatePortrait = PortraitSubView.makeRandomPortrait(Classes.PIRATE);
        firstMatePortrait.setFaceDetail(new EyePatchDetail());
        firstMatePortrait.setDetailColor(MyColors.BLACK);
        firstMatePortrait.setClass(Classes.PIRATE);
        System.out.println("Real mutineer is " + realMutineer.getName() + " the " +
                (realMutineer.getGender() ? "female":"male") + " " + realMutineer.getRace().getName() +
                ". IsTrans=" + realMutineer.isTrans() + ", likesRum=" + realMutineer.likesRum() +
                ",usesPistol=" + realMutineer.usesPistol() + ", weaponFlippsed=" + realMutineer.isFlippedWeapon() + ".");
    }

    @Override
    public CharacterAppearance getPortrait() {
        return blackbonePortrait;
    }

    @Override
    public CombatTheme getCombatTheme() {
        return new IslandCombatTheme();
    }

    @Override
    protected List<QuestScene> buildScenes() {
        return List.of(new QuestScene("Come up with a plan", List.of(
                            new CollaborativeSkillCheckSubScene(2, 1, Skill.Logic, COLLABORATIVE_CHECK_DIFFICULTY,
                                    "We need to come up with an idea."))),
                       new QuestScene("Bury bounty", List.of(
                            new CombatMonkeysSubScene(0, 3),
                            new CollectiveSkillCheckSubScene(0, 4, Skill.Endurance, COLLECTIVE_CHECK_DIFFICULTY,
                                       "Now we have to dig a hole. With the sun blazing down at us, this will get sweaty."),
                            new CollaborativeSkillCheckSubScene(4, 5, Skill.Entertain, COLLABORATIVE_CHECK_DIFFICULTY,
                                    "The crew have become restless, we must divert their attention with some entertainment."),
                            new SearchForHiddenCacheSubScene(5, 5))),
                       new QuestScene("Set up secret meeting", List.of(
                            new CollaborativeSkillCheckSubScene(3, 3, Skill.SeekInfo, COLLABORATIVE_CHECK_DIFFICULTY,
                                    "No way we'll get to know anything around here until we start rubbing some elbows with these pirates."))),
                       new QuestScene("Accuse mutineer", List.of(
                            new AccuseMutineerSubScene(3, 8))),
                       new QuestScene("Sailing", List.of(
                            new CollectiveSkillCheckSubScene(1, 1, Skill.Labor, COLLECTIVE_CHECK_DIFFICULTY,
                                "Since we're posing as normal crew members, we have to partake in the " +
                                        "hard work that is sailing captain Blackbones ship."))));
    }

    @Override
    protected List<QuestJunction> buildJunctions(List<QuestScene> scenes) {
        QuestStartPointWithoutDecision start = new QuestStartPointWithoutDecision(new QuestEdge(scenes.get(4).get(0)), "Part of the ship, part of the crew!");
        SimpleJunction beforeAccuse = new SimpleJunction(3, 7, new QuestEdge(scenes.get(3).get(0)), "Make accusation.");
        SimpleJunction beforeSail = new SimpleJunction(1, 0, new QuestEdge(scenes.get(4).get(0)), "Continue investigation.");
        QuestDecisionPoint gather = new QuestDecisionPoint(7, 6,
                List.of(new QuestEdge(beforeSail, QuestEdge.VERTICAL),
                        new QuestEdge(beforeAccuse, QuestEdge.VERTICAL)), "What's our next move?");
        TalkToCrewNode talk = new TalkToCrewNode(4, 4, new QuestEdge(scenes.get(1).get(2), QuestEdge.VERTICAL));
        StoryJunctionWithEvent interrogate = new InterrogateFirstMateNode(6, 4, new QuestEdge(gather, QuestEdge.VERTICAL));
        QuestDecisionPoint decision = new QuestDecisionPoint(4, 2, List.of(
                new QuestEdge(scenes.get(1).get(0)),
                new QuestEdge(talk, QuestEdge.VERTICAL),
                new QuestEdge(scenes.get(2).get(0)),
                new QuestEdge(scenes.get(1).get(3)),
                new QuestEdge(interrogate)
        ),"We need some kind of activity which could lead us " +
                "to a clue about the identity of the mutineer.");
        StoryJunctionWithEvent nightlyMeet = new NightlyMeetNode(3, 4, new QuestEdge(gather, QuestEdge.VERTICAL));
        StoryJunction forceGuess = new StoryJunction(2, 7, new QuestEdge(beforeAccuse)) {
            @Override
            protected void doAction(Model model, QuestState state) {
                boolean gender = blackbonePortrait.getGender();
                state.println("Captain Blackbone approaches you. You have performed poorly as pirates and " +
                        GameState.heOrShe(gender) + " wants you off the ship as soon as possible. " +
                        GameState.heOrSheCap(gender) + " offers you chance to redeem yourself if you can tell " +
                        GameState.himOrHer(gender) + " who the mutineer is now.");
            }
        };
        return List.of(start, decision, talk, gather, nightlyMeet, interrogate, beforeAccuse, beforeSail, forceGuess);
    }

    @Override
    public List<QuestBackground> getDecorations() {
        return List.of(new QuestBackground(new Point(4, 8), blackboneAvatar),
                new QuestBackground(new Point(4, 5), waterSprayFront, false),
                new QuestBackground(new Point(5, 5), waterSpray[0], false),
                new QuestBackground(new Point(6, 5), waterSpray[1], false),
                new QuestBackground(new Point(7, 5), waterSpray[2], false));
    }

    @Override
    public List<QuestBackground> getBackgroundSprites() {
        return backgroundSprites;
    }

    @Override
    protected void connectScenesToJunctions(List<QuestScene> scenes, List<QuestJunction> junctions) {
        scenes.get(0).get(0).connectSuccess(junctions.get(1));
        scenes.get(0).get(0).connectFail(junctions.get(8), QuestEdge.VERTICAL);

        scenes.get(1).get(0).connectSuccess(scenes.get(1).get(1), QuestEdge.VERTICAL);

        scenes.get(1).get(1).connectSuccess(junctions.get(3), QuestEdge.VERTICAL);
        scenes.get(1).get(1).connectFail(junctions.get(8));

        scenes.get(1).get(2).connectSuccess(junctions.get(3), QuestEdge.VERTICAL);
        scenes.get(1).get(2).connectFail(junctions.get(8));

        scenes.get(1).get(3).connectSuccess(junctions.get(3), QuestEdge.VERTICAL);
        scenes.get(1).get(3).connectFail(scenes.get(1).get(2));

        scenes.get(2).get(0).connectFail(junctions.get(8));
        scenes.get(2).get(0).connectSuccess(junctions.get(4));

        scenes.get(3).get(0).connectFail(getFailEndingNode(), QuestEdge.VERTICAL);
        scenes.get(3).get(0).connectSuccess(getSuccessEndingNode());

        scenes.get(4).get(0).connectFail(junctions.get(8), QuestEdge.VERTICAL);
        scenes.get(4).get(0).connectSuccess(scenes.get(0).get(0));
    }

    @Override
    public MyColors getBackgroundColor() {
        return MyColors.BLACK;
    }

    @Override
    public BackgroundMusic getMusic() {
        return BackgroundMusic.festiveSong;
    }

    private static List<Enemy> makeMonkeys() {
        return List.of(new MonkeyEnemy('A'), new MonkeyEnemy('A'), new MonkeyEnemy('A'));
    }

    private class CombatMonkeysSubScene extends CombatSubScene {

        public CombatMonkeysSubScene(int col, int row) {
            super(col, row, makeMonkeys());
        }
        @Override
        public QuestEdge run(Model model, QuestState state) {
            state.println("As the crew drag several extremely heavy chests of swag ashore, " +
                    "they are at once assailed by a bunch of pesky monkeys!");
            state.leaderSay("Oh, come on, just let us do our job!");
            model.getLog().waitForAnimationToFinish();
            QuestEdge toReturn = super.run(model, state);
            setDefeated(false);
            setEnemies(makeMonkeys());
            return toReturn;
        }

        @Override
        protected List<GameCharacter> getAllies() {
            List<GameCharacter> chars = MyLists.transform(potentialMutineers, PotentialMutineer::getCharacter);
            return chars;
        }

        @Override
        protected String getCombatDetails() {
            return "Monkeys";
        }

    }

    private class TalkToCrewNode extends StoryJunctionWithEvent {

        public TalkToCrewNode(int col, int row, QuestEdge edge) {
            super(col, row, edge);
        }
        @Override
        public DailyEventState makeEvent(Model model, QuestState state) {
            return new TalkToCrewEvent(model, state, potentialMutineers);
        }

        @Override
        public String getDescription() {
            return "Talk to crew";
        }

    }
    private class AccuseMutineerSubScene extends ConditionSubScene {

        public AccuseMutineerSubScene(int col, int row) {
            super(col, row);
        }
        @Override
        public String getDescription() {
            return "Make a decision.";
        }

        @Override
        public QuestEdge run(Model model, QuestState state) {
            state.println("You approach captain Blackbone. Who do you accuse of being the mutineer?");
            List<String> crew = MyLists.transform(potentialMutineers, PotentialMutineer::getName);
            int[] selectedAction = new int[1];
            model.setSubView(new ArrowMenuSubView(model.getSubView(),
                    crew, 24, 24, ArrowMenuSubView.NORTH_WEST) {
                @Override
                protected void enterPressed(Model model, int cursorPos) {
                    selectedAction[0] = cursorPos;
                    model.setSubView(getPrevious());
                }
            });
            state.waitForReturnSilently(true);

            PotentialMutineer accused = potentialMutineers.get(selectedAction[0]);
            if (accused == realMutineer) {
                state.println("You present the evidence you've collected and Blackbone agrees with your conclusion.");
                return getSuccessEdge();
            }
            state.println("You present your findings to Blackbone, but as you describe it, you realize yourself " +
                    "that there are gaps in your case against " + crew.get(selectedAction[0]) +
                    ". Blackbone is unconvinced and he dismisses you in disgust.");
            return getFailEdge();
        }

    }
    private class InterrogateFirstMateNode extends StoryJunctionWithEvent {

        public InterrogateFirstMateNode(int col, int row, QuestEdge edge) {
            super(col, row, edge);
        }
        @Override
        public DailyEventState makeEvent(Model model, QuestState state) {
            return new DailyEventState(model) {
                @Override
                protected void doEvent(Model model) {
                    showExplicitPortrait(model, firstMatePortrait, "First Mate");
                    println("You approach the first mate of captain Blackbone's ship.");
                    portraitSay("What do you want?");
                    leaderSay("Captain Blackbone mentioned you overheard a conversation at the Sunken Worlds about a " +
                            "mutineer on " + hisOrHer(blackbonePortrait.getGender()) + ". Can you give us the details?");
                    portraitSay("Well, it was late one night, and aye, I was at the Sunken Worlds enjoying me brew. " +
                            "I was just about to leave, when I heard a few people talking about Captain Blackbone. " +
                            "One of them, I heard, was set on staging a mutiny.");
                    leaderSay("Who was it? What did they look like?");
                    portraitSay("Arrr... the lighting was dim, and the three of them were off in a dark corner.");
                    leaderSay("So what did you do?");
                    portraitSay("I started moving closer, but then the tavern wench distracted me with her...");
                    leaderSay("Yeah, we get it. So, then what?");
                    String bottleOf = AvertTheMutinyQuest.this.realMutineer.likesRum() ? "Sunblazean rum" : "Arkvalean wine";
                    portraitSay("Once I made me way over to their booth, the shady trio " +
                            "had vanished, leaving only some cups and half empty bottle of " + bottleOf + ".");
                    leaderSay("So you didn't really get a look?");
                    portraitSay("Naw... too dim.");
                    leaderSay("What about their voices. Did the mutineer sound like a man, or a woman?");
                    portraitSay("It's hard to say... they were whispering, and I had had one or two pints of ale.");
                    leaderSay("Come on " + (firstMatePortrait.getGender() ? "man" : "woman") + " there must be some information you can give me.");
                    portraitSay("Aye, now that I think about it. I did hear them talk about a secret cache. The mutineer has " +
                            "hid a weapon in a secret compartment somewhere on the ship.");
                    leaderSay("Interesting. Well, if you don't have any more information, I think we're done.");
                    randomSayIfPersonality(PersonalityTrait.jovial, List.of(), "No wait, tell us more about the tavern wench!");
                    model.getLog().waitForAnimationToFinish();
                    removePortraitSubView(model);
                }
            };
        }

        @Override
        public String getDescription() {
            return "Question first mate";
        }

    }
    private class NightlyMeetNode extends StoryJunctionWithEvent {

        public NightlyMeetNode(int col, int row, QuestEdge edge) {
            super(col, row, edge);
        }
        @Override
        public DailyEventState makeEvent(Model model, QuestState state) {
            return new DailyEventState(model) {
                @Override
                protected void doEvent(Model model) {
                    GameCharacter chosen;
                    if (model.getParty().size() > 1) {
                        leaderSay("One of us could pose as a disgruntled crew member, willing to join in the mutiny. " +
                            "That way we may get a meeting with the mutineer.");
                        println("Who will pose as a fellow mutineer?");
                        chosen = model.getParty().partyMemberInput(model, this, model.getParty().getLeader());
                        println("You carefully let know that " + chosen.getFirstName() + " is interested in the concept of mutiny, " +
                                "and that " + heOrShe(chosen.getGender()) + " are willing to meet, at midnight, in the aft cargo hold.");
                        GameCharacter sayer;

                        if (chosen != model.getParty().getLeader()) {
                            sayer = model.getParty().getLeader();
                        } else {
                            sayer = model.getParty().getRandomPartyMember(model.getParty().getLeader());
                        }
                        partyMemberSay(sayer, "Okay " + chosen.getFirstName() +
                                ", the rest of us will hang back and let you talk to him or her. " +
                                "Once you know the face of this mutinous scum, call out for us and we'll spring into action.");
                        partyMemberSay(chosen, "Understood. Wish me luck");
                        partyMemberSay(sayer, "Good luck!");
                        model.getLog().waitForAnimationToFinish();
                        model.getParty().benchPartyMembers(MyLists.filter(model.getParty().getPartyMembers(), pm -> pm != chosen));
                    } else {
                        chosen = model.getParty().getLeader();
                        println("You carefully let know that you are interested in the concept of mutiny, " +
                                "and that you are willing to meet, at midnight, in the aft cargo hold.");
                    }
                    println(chosen.getFirstName() + " waits in the cargo hold. Suddenly " + heOrShe(chosen.getGender()) +
                            " hears somebody approaching.");
                    partyMemberSay(chosen, "Who's there?");
                    showSilhouettePortrait(model, "Mutineer");
                    portraitSay("Somebody who's sick of Blackbone's orders.");
                    println("It's dark and " + chosen.getFirstName() + " can't tell who the mutineer is.");
                    println("What do you do?");
                    int choice = multipleOptionArrowMenu(model, 24, 24, List.of("Talk with mutineer", "Jump on mutineer"));
                    if (choice == 0) {
                        println("You talk for a short while, mostly about the terrible conditions on captain Blackbone's ship.");
                        boolean feminine = realMutineer.getGender() ^ realMutineer.isTrans();
                        println(chosen.getFirstName() + " still can't see the mutineers face clearly, but " +
                                (feminine ? "she speaks in a soft, feminine voice." : "he speaks in a deep, masculine voice."));
                        println("Then, just as the mutineer was about to reveal themself, there's a startling noise.");
                        partyMemberSay(chosen, "What was that?");
                        model.getLog().waitForAnimationToFinish();
                        removePortraitSubView(model);
                        println("The mutineer seems to have slipped away.");
                        model.getLog().waitForAnimationToFinish();
                        if (model.getParty().size() > 1) {
                            model.getParty().unbenchAll();
                            if (chosen != model.getParty().getLeader()) {
                                leaderSay(chosen.getFirstName() + ", you never called for us. What happened, did you meet with the mutineer?");
                            } else {
                                GameCharacter other = model.getParty().getRandomPartyMember(model.getParty().getLeader());
                                partyMemberSay(other, chosen.getFirstName() + ", you never called for us. What happened, did you meet with the mutineer?");
                            }
                            partyMemberSay(chosen, "I never got a good look, but I'm pretty sure by the " +
                                    "sound of the voice that our mutineer is a " + (feminine ? "woman" : "man") + ".");
                        } else {
                            println("You're disappointed that you didn't get to know the mutineers identity, " +
                                    "but at least you seem to have found a clue to their gender.");
                        }
                    } else {
                        println(chosen.getFirstName() + " throws " + himOrHer(chosen.getGender()) + " at the mutineer in the darkness.");
                        portraitSay("Oooof! Get off!");
                        partyMemberSay(chosen, "Hold still you!");
                        println("They wrestle violently in the darkness, but the mysterious person manages to " +
                                "get away and scurries off into the shadows, leaving " + chosen.getFirstName() + " with a bleeding nose.");
                        model.getLog().waitForAnimationToFinish();
                        removePortraitSubView(model);
                        if (model.getParty().size() > 1) {
                            model.getParty().unbenchAll();
                            if (chosen != model.getParty().getLeader()) {
                                leaderSay(chosen.getFirstName() + ", you never called for us. What happened, did you meet with the mutineer?");
                            } else {
                                GameCharacter other = model.getParty().getRandomPartyMember(model.getParty().getLeader());
                                partyMemberSay(other, chosen.getFirstName() + ", you never called for us. What happened, did you meet with the mutineer?");
                            }
                            partyMemberSay(chosen, "I never got a good look, but I did get a clue. " +
                                    "We wrestled for a bit, and I know own kind. He or she " + isSameRace(chosen.getRace(), realMutineer.getRace()));
                        } else {
                            String sameRace = isSameRace(chosen.getRace(), realMutineer.getRace());
                            println("Although you didn't get a good look at the mutineer, " +
                                    "you did get a clue. You know your own kind and he or she " + sameRace + ".");
                        }
                    }
                }

                private String isSameRace(Race race1, Race race2) {
                    if (race1 instanceof ElvenRace && race2 instanceof ElvenRace) {
                        return "is an elf";
                    }
                    if (race1 instanceof HumanRace && race2 instanceof HumanRace) {
                        return "is human";
                    }
                    if (race1.id() == race2.id()) {
                        return "is a " + race1.getName().toLowerCase();
                    }
                    if (race1 instanceof ElvenRace) {
                        return "is not an elf";
                    }
                    if (race1 instanceof HumanRace) {
                        return "is not human";
                    }
                    return "is not a " + race1.getName().toLowerCase();
                }
            };
        }

        @Override
        public String getDescription() {
            return "Nightly secret meeting with mutineer";
        }

    }
    private class SearchForHiddenCacheSubScene extends CollaborativeSkillCheckSubScene {

        public SearchForHiddenCacheSubScene(int col, int row) {
            super(col, row, Skill.Search, COLLABORATIVE_CHECK_DIFFICULTY,
                    "Let's search the ship for hidden caches.");
        }
        @Override
        protected void subSceneOutro(Model model, QuestState state, boolean skillSuccess) {
            if (skillSuccess) {
                Item weapon = realMutineer.usesPistol() ^ realMutineer.isFlippedWeapon()
                        ? new Pistol()
                        : new Cutlass();
                state.println("In a secret compartment under the stairs between the decks, you find a " + weapon.getName().toLowerCase() + ".");
                state.leaderSay("This must be the mutineers weapon they've stashed away.");
                if (model.getParty().size() > 1) {
                    GameCharacter other = model.getParty().getRandomPartyMember(model.getParty().getLeader());
                    state.partyMemberSay(other, "We'll just take care of that for now.");
                }
                weapon.addYourself(model.getParty().getInventory());
                state.println("You got a " + weapon.getName() + ".");
            } else {
                state.leaderSay("All that work, and we didn't find more than a few rats and some empty bottles.");
            }
        }

    }
    private static List<QuestBackground> makeBackgroundSprites() {
        MyColors waterColor = MyColors.LIGHT_BLUE;
        MyColors skyColor = MyColors.CYAN;
        List<QuestBackground> bg = new ArrayList<>();
        Sprite32x32[][] island = new Sprite32x32[2][5];
        for (int row = 0; row < island[0].length; ++row) {
            for (int col = 0; col < island.length; ++col) {
                island[col][row] = new Sprite32x32("mutinybg1", "quest.png", 0xA6 + 0x10*row + col,
                        MyColors.DARK_GREEN, MyColors.LIGHT_YELLOW, waterColor, skyColor);
                bg.add(new QuestBackground(new Point(col, row), island[col][row], false));
            }
        }

        Sprite32x32[][] ship = new Sprite32x32[5][5];
        for (int row = 0; row < ship[0].length; ++row) {
            for (int col = 0; col < ship.length; ++col) {
                MyColors[] colors = new MyColors[]{waterColor, MyColors.WHITE, MyColors.BEIGE, MyColors.WHITE};
                if (row < 1) {
                    colors[0] = skyColor;
                    if (col == 0) {
                        colors[1] = MyColors.CYAN;
                    }
                } else if (row > 3) {
                    colors[1] = MyColors.GOLD;
                    colors[2] = MyColors.DARK_BROWN;
                    colors[3] = MyColors.BROWN;
                } else if (col < 1) {
                    colors[1] = MyColors.BROWN;
                }

                ship[col][row] = new Sprite32x32("mutinybg2", "quest.png", 0xA8 + 0x10*row + col,
                        colors[0], colors[1], colors[2], colors[3]);
                bg.add(new QuestBackground(new Point(col+3, row+1), ship[col][row], false));
            }
        }

        for (int row = 0; row < 2; ++row) {
            bg.add(new QuestBackground(new Point(2, row), ship[1][0], false));
        }
        for (int col = 3; col < 8; ++col) {
            bg.add(new QuestBackground(new Point(col, 0), ship[1][0], false));
        }

        for (int row = 2; row < 5; ++row) {
            bg.add(new QuestBackground(new Point(2, row), ship[0][1], false));
        }

        for (int col = 0; col < 8; ++col) {
            if (col < 3) {
                bg.add(new QuestBackground(new Point(col, 5), ship[0][1], false));
            }
            bg.add(new QuestBackground(new Point(col, 6), ship[0][1], false));
        }

        return bg;
    }
}
