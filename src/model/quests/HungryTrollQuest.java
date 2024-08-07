package model.quests;

import model.Model;
import model.characters.appearance.CharacterAppearance;
import model.classes.Classes;
import model.classes.Skill;
import model.enemies.AbnormallyLargeTrollEnemy;
import model.horses.Horse;
import model.quests.scenes.CollaborativeSkillCheckSubScene;
import model.quests.scenes.CombatSubScene;
import model.quests.scenes.SoloSkillCheckSubScene;
import model.races.AllRaces;
import model.states.QuestState;
import sound.BackgroundMusic;
import util.MyRandom;
import view.MyColors;
import view.combat.CombatTheme;
import view.combat.GrassCombatTheme;
import view.subviews.PortraitSubView;
import view.widget.QuestBackground;

import java.util.List;

public class HungryTrollQuest extends Quest {
    private static final String INTRO = "The elder of a nearby village have come to you with their problem. " +
            "An abnormally large troll has settled nearby and keeps steeling their livestock. They've hired you to deal with the behemoth.";
    private static final String OUTRO = "Relieved that the issue with the troll has been resolved, the elder rewards " +
            "you with what little gold the village can spare.";
    private static final CharacterAppearance PROTRAIT = PortraitSubView.makeOldPortrait(Classes.PRI, AllRaces.ALL, MyRandom.flipCoin());
    private static final List<QuestBackground> BACKGROUND = DefendTheVillageQuest.makeBackgroundSprites();

    public HungryTrollQuest() {
        super("Hungry Troll", "Elder", QuestDifficulty.EASY, 1, 75, 0, INTRO, OUTRO);
    }

    @Override
    public CharacterAppearance getPortrait() {
        return PROTRAIT;
    }

    @Override
    public BackgroundMusic getMusic() {
        return BackgroundMusic.lightQuestSong;
    }

    @Override
    public boolean drawTownOrCastleInBackground() {
        return true;
    }

    @Override
    protected List<QuestScene> buildScenes() {
        return List.of(
            new QuestScene("Combat with Troll", List.of(
                new CombatSubScene(4, 5, List.of(new AbnormallyLargeTrollEnemy('A')), true) {
                    @Override
                    protected String getCombatDetails() {
                        return "Abnormally large troll";
                    }
            })),
            new QuestScene("Reason with Troll", List.of(
                new SoloSkillCheckSubScene(6, 2, Skill.Persuade, 16,
                        "Can somebody talk some sense into him?"),
                new SoloSkillCheckSubScene(6, 3, Skill.Persuade, 16,
                        "There must be a way to get through to this guy...")

            )),
            new QuestScene("Instruct Troll", List.of(
                    new CollaborativeSkillCheckSubScene(6, 5, Skill.Labor, 9,
                            "Perhaps we can instruct the troll, how to work for the villagers. That way he could " +
                                    "earn his food."),
                    new CollaborativeSkillCheckSubScene(7, 5, Skill.Search, 9,
                            "Let's search this area for more natural sources of food. That way the troll won't have " +
                                    "to steal from the villagers.")
            )));
    }

    @Override
    protected List<QuestJunction> buildJunctions(List<QuestScene> scenes) {
        StoryJunction sj1 = new StoryJunction(6, 1, new QuestEdge(scenes.get(1).get(0), QuestEdge.VERTICAL)) {
            @Override
            protected void doAction(Model model, QuestState state) {
                state.leaderSay("Listen mate, you've got to stop stealing the villagers' livestock.");
                state.printQuote("Troll", "I'M HUNGRY!");
                state.leaderSay("Uh... yeah... we get it. But these peasants... they're poor.");
                state.printQuote("Troll", "ME ALSO POOR! AND HUNGRY!");
            }
        };

        StoryJunction sj2 = new StoryJunction(7, 2, new QuestEdge(scenes.get(1).get(1), QuestEdge.VERTICAL)) {
            @Override
            protected void doAction(Model model, QuestState state) {
                state.print("The troll has eaten one of your backpacks. ");
                if (model.getParty().getFood() > 10) {
                    int lost = model.getParty().getFood() / 3;
                    state.println("You have lost " + lost + " rations.");
                    state.leaderSay("Hey! That's our food!");
                    model.getParty().addToFood(-lost);
                } else {
                    state.println("Fortunately, it did not contain anything too important.");
                    state.partyMemberSay(model.getParty().getRandomPartyMember(), "Hey, those were my spare clothes!");
                }
                state.printQuote("Troll", "YUM YUM.... GOOD FOOD.");
                state.leaderSay("Okay troll, you've had a snack. Ready to talk now?");
                state.printQuote("Troll", "NO TALK! I'M STILL HUNGRY!");
            }
        };

        QuestDecisionPoint qdp = new QuestDecisionPoint(6, 4, List.of(new QuestEdge(scenes.get(2).get(0)),
                new QuestEdge(scenes.get(2).get(1))), "We're getting nowhere with this guy. What else can we try?");

        StoryJunction sj3 = new StoryJunction(5, 3, new QuestEdge(qdp, QuestEdge.VERTICAL)) {
            @Override
            protected void doAction(Model model, QuestState state) {
                if (model.getParty().hasHorses()) {
                    Horse horse = model.getParty().getHorseHandler().get(0);
                    state.println("The troll has eaten one of your horses.");
                    model.getParty().getHorseHandler().removeHorse(horse);
                    state.leaderSay("Hey! That poor " + horse.getType() + "!");
                } else {
                    state.print("The troll has eaten another one of your backpacks. ");
                    if (model.getParty().getFood() > 10) {
                        int lost = model.getParty().getFood() / 2;
                        state.println("You have lost " + lost + " rations.");
                        model.getParty().addToFood(-lost);
                    } else {
                        state.println("Fortunately, it did not contain anything too important.");
                        state.partyMemberSay(model.getParty().getRandomPartyMember(), "Hey, the cooking stuff was in there!");
                    }
                }
                state.printQuote("Troll", "AAAAH, SO GOOD...");
                state.leaderSay("Troll, I'm losing my patience with you! You can't just eat the next thing you see.");
                state.printQuote("Troll", "BUT I'M SO HUNGRY, ALL THE TIME! MUST EEEEEAAAAAT.");
            }
        };

        QuestStartPoint qsp = new QuestStartPoint(List.of(new QuestEdge(scenes.get(0).get(0)), new QuestEdge(sj1)),
                "There's the troll. He doesn't look very aggressive, perhaps there's a peaceful solution to this dilemma.");


        return List.of(qsp, sj1, sj2, sj3, qdp);
    }

    @Override
    protected void connectScenesToJunctions(List<QuestScene> scenes, List<QuestJunction> junctions) {
        scenes.get(0).get(0).connectSuccess(getSuccessEndingNode(), QuestEdge.VERTICAL);
        scenes.get(0).get(0).connectFail(getFailEndingNode());

        scenes.get(1).get(0).connectSuccess(scenes.get(1).get(1));
        scenes.get(1).get(0).connectFail(junctions.get(2));

        scenes.get(1).get(1).connectSuccess(junctions.get(4));
        scenes.get(1).get(1).connectFail(junctions.get(3));

        scenes.get(2).get(0).connectFail(scenes.get(0).get(0));
        scenes.get(2).get(0).connectSuccess(getSuccessEndingNode(), QuestEdge.VERTICAL);

        scenes.get(2).get(1).connectFail(scenes.get(0).get(0));
        scenes.get(2).get(1).connectSuccess(getSuccessEndingNode(), QuestEdge.VERTICAL);
    }

    @Override
    public MyColors getBackgroundColor() {
        return MyColors.GREEN;
    }

    @Override
    public List<QuestBackground> getBackgroundSprites() {
        return BACKGROUND;
    }

    @Override
    public CombatTheme getCombatTheme() {
        return new GrassCombatTheme();
    }
}
