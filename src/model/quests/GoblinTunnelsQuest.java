package model.quests;

import model.Model;
import model.characters.GameCharacter;
import model.characters.appearance.CharacterAppearance;
import model.classes.Classes;
import model.classes.Skill;
import model.classes.SkillCheckResult;
import model.combat.CaveTheme;
import model.combat.StandardCombatLoot;
import model.enemies.*;
import model.quests.scenes.CombatSubScene;
import model.races.AllRaces;
import model.states.GameState;
import model.states.QuestState;
import view.BorderFrame;
import view.MyColors;
import view.sprites.Sprite;
import view.sprites.Sprite32x32;
import view.subviews.CombatTheme;
import view.subviews.PortraitSubView;
import view.subviews.QuestSubView;
import view.widget.QuestBackground;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class GoblinTunnelsQuest extends Quest {
    private static final String INTRO =
            "The party is contracted by a map maker to explore the depths of " +
            "Khaz-Ak-Ahrak, an ancient dwarven city now overrun by goblins!";

    private static final String ENDING = "The door to the overworld is right in front of the party. But so is the tunnel leading" +
            " to the Goblin King's throne room. Do you escape the goblin tunnels or do you enter into the throne room to clear out" +
            " this pestilence, once and for all?";
    private static final CharacterAppearance PORTRAIT = PortraitSubView.makeRandomPortrait(Classes.ART, AllRaces.ALL);
    private static final Point RIDDLE_JUNCTION_POINT = new Point(0, 2);
    private static final Sprite SLIMY_CREATURE_SPRITE = new Sprite32x32("slimycreature", "quest.png", 0x5B,
            MyColors.BLACK, MyColors.GRAY, MyColors.BEIGE, MyColors.DARK_BLUE);
    private static final List<QuestBackground> BACKGROUND = makeBackground();
    private static final int LOOT_REWARDS = 4;
    private GameCharacter riddleGuy = null;

    public GoblinTunnelsQuest() {
        super("Goblin Tunnels", "Map Maker", QuestDifficulty.MEDIUM, 0, 0, 0, INTRO, ENDING);
    }

    @Override
    public void drawSpecialReward(Model model, int x, int y) {
        BorderFrame.drawString(model.getScreenHandler(), "Loot", x, y++, MyColors.WHITE, MyColors.BLACK);
        y++;
        BorderFrame.drawString(model.getScreenHandler(), "Goblin", x, y++, MyColors.WHITE, MyColors.BLACK);
        BorderFrame.drawString(model.getScreenHandler(), "King", x, y++, MyColors.WHITE, MyColors.BLACK);
        BorderFrame.drawString(model.getScreenHandler(), "Quest", x, y++, MyColors.WHITE, MyColors.BLACK);
    }

    @Override
    public CharacterAppearance getPortrait() {
        return PORTRAIT;
    }

    @Override
    protected List<QuestScene> buildScenes() {
        return List.of(
                new QuestScene("Goblin Spearmen", List.of(
                        new GoblinCombatSubScene(6, 1, 7, new GoblinSpearman('A')))),
                new QuestScene("Riddles", List.of(
                        new RiddlesSubScene(2, 2, 6, true),
                        new RiddlesSubScene(2, 4, 7, false),
                        new RiddlesSubScene(2, 6, 8, false),
                        new RiddleDecorativeSubScene(RIDDLE_JUNCTION_POINT.x, RIDDLE_JUNCTION_POINT.y))),
                new QuestScene("Goblin Club Wielders", List.of(
                        new GoblinCombatSubScene(7, 3, 8, new GoblinClubWielder('A')))),
                new QuestScene("Goblin Bowmen", List.of(
                        new GoblinCombatSubScene(5, 5, 9, new GoblinBowman('A'))))
        );
    }

    @Override
    protected List<QuestJunction> buildJunctions(List<QuestScene> scenes) {
        SeparatePartyMemberStartingPoint start = new SeparatePartyMemberStartingPoint(new QuestEdge(scenes.get(0).get(0)),
                "It's easy to get lost down here. Everybody stick together.");
        start.getConnections().add(new QuestEdge(scenes.get(1).get(3)));
        QuestJunction merge1 = new MergePointJunction(6, 7, new QuestEdge(getSuccessEndingNode()));
        QuestJunction merge2 = new MergePointJunction(1, 6, new QuestEdge(getFailEndingNode()));
        return List.of(start, merge1, merge2);
    }

    @Override
    protected void connectScenesToJunctions(List<QuestScene> scenes, List<QuestJunction> junctions) {
        scenes.get(0).get(0).connectSuccess(scenes.get(1).get(0));

        scenes.get(1).get(0).connectFail(junctions.get(2));
        scenes.get(1).get(0).connectSuccess(scenes.get(2).get(0));

        scenes.get(1).get(1).connectFail(junctions.get(2));
        scenes.get(1).get(1).connectSuccess(scenes.get(3).get(0));

        scenes.get(1).get(2).connectFail(junctions.get(2));
        scenes.get(1).get(2).connectSuccess(junctions.get(1));

        scenes.get(1).get(3).connectSuccess(junctions.get(1), QuestEdge.VERTICAL);
        scenes.get(1).get(3).connectFail(junctions.get(2), QuestEdge.VERTICAL);

        scenes.get(2).get(0).connectSuccess(scenes.get(1).get(1));
        scenes.get(3).get(0).connectSuccess(scenes.get(1).get(2));
    }

    @Override
    public GameState endOfQuest(Model model, QuestState state, boolean questWasSuccess) {
        for (int i = 0; i < LOOT_REWARDS; ++i) {
            StandardCombatLoot loot = new StandardCombatLoot(model);
            if (loot.getText().equals("")) {
                state.println("You have found something valuable, " + loot.getText() + ".");
            } else {
                state.println("You have found " + loot.getGold() + " gold.");
            }
            loot.giveYourself(model.getParty());
        }
        
        if (questWasSuccess) {
            state.print("Do you wish to immediately continue to the Goblin King Quest " +
                    "(you will not be able to come back and do this later)? (Y/N) ");
            if (state.yesNoInput()) {
                return new QuestState(model, new GoblinKingQuest());
            }
        }
        return super.endOfQuest(model, state, questWasSuccess);
    }

    @Override
    public MyColors getBackgroundColor() {
        return MyColors.BLACK;
    }

    @Override
    public CombatTheme getCombatTheme() {
        return new CaveTheme();
    }

    @Override
    public List<QuestBackground> getBackgroundSprites() {
        return BACKGROUND;
    }

    private static List<Enemy> makeGoblins(int number, GoblinEnemy type) {
        List<Enemy> result = new ArrayList<>();
        for (int i = 0; i < number; ++i) {
            result.add(type.copy());
        }
        return result;
    }

    private class GoblinCombatSubScene extends CombatSubScene {
        private final String text;

        public GoblinCombatSubScene(int col, int row, int number, GoblinEnemy type) {
            super(col, row, makeGoblins(number, type));
            if (type.getName().contains("man")) {
                this.text = type.getName().replace("man", "men");
            } else {
                text = type.getName() + "s";
            }
        }

        @Override
        public QuestEdge run(Model model, QuestState state) {
            QuestEdge toReturn = super.run(model, state);

            if (model.getParty().size() == 1 && model.getParty().getPartyMember(0) == riddleGuy) {
                riddleGuy = null;
                state.setCurrentPosition(getScenes().get(1).get(3));
                model.getParty().unbenchAll();
                return new QuestEdge(getFailEndingNode());
            }

            return toReturn;
        }

        @Override
        protected String getCombatDetails() {
            return text;
        }
    }

    private class SeparatePartyMemberStartingPoint extends QuestStartPointWithoutDecision {
        public SeparatePartyMemberStartingPoint(QuestEdge questEdge, String s) {
            super(questEdge, s);
        }

        @Override
        public QuestEdge run(Model model, QuestState state) {
            QuestEdge toReturn = super.run(model, state);
            if (model.getParty().size() > 1) {
                state.print("Choose a party member to be separated from the party.");
                GameCharacter gc;
                do {
                    state.print(" You may not select the party leader.");
                    gc = model.getParty().partyMemberInput(model, state, model.getParty().getPartyMember(model.getParty().size() - 1));
                } while (model.getParty().getLeader() == gc);
                model.getParty().partyMemberSay(model, model.getParty().getRandomPartyMember(gc), List.of("Wait! Where is " + gc.getFirstName() + "?"));
                model.getParty().partyMemberSay(model, model.getParty().getLeader(), "Oh no...");

                state.getSubView().addMovementAnimation(gc.getAvatarSprite(), QuestSubView.convertToScreen(getPosition()),
                        QuestSubView.convertToScreen(RIDDLE_JUNCTION_POINT));
                state.getSubView().waitForAnimation();
                state.getSubView().removeMovementAnimation();
                state.println("! " + gc.getName() + " has been separated from the party!");

                riddleGuy = gc;
                model.getParty().benchPartyMembers(List.of(riddleGuy));
            }
            return toReturn;
        }
    }

    private static final Sprite32x32 SPRITE = new Sprite32x32("riddlessubscene", "quest.png", 0x03,
            MyColors.BLACK, MyColors.WHITE, MyColors.RED, MyColors.BLACK);

    private class RiddlesSubScene extends QuestSubScene {
        private final int difficulty;
        private final boolean withIntro;

        public RiddlesSubScene(int col, int row, int difficulty, boolean withIntro) {
            super(col, row);
            this.difficulty = difficulty;
            this.withIntro = withIntro;
        }

        @Override
        public void drawYourself(Model model, int xPos, int yPos) {
            model.getScreenHandler().register(SPRITE.getName(), new Point(xPos, yPos), SPRITE, 1);
        }

        @Override
        public String getDescription() {
            return "Wait for Riddle";
        }

        @Override
        public QuestEdge run(Model model, QuestState state) {
            if (riddleGuy == null) {
                return getSuccessEdge();
            }

            if (withIntro) {
                model.getParty().partyMemberSay(model, riddleGuy, List.of("Where the devil am I? Is that an underground lake?"));
                state.println("Slimy Creature: \"Does my preshious want to play a game of riddles?\"");
                model.getParty().partyMemberSay(model, riddleGuy, List.of("Sure, why not? If you'll show me the way out of here afterwards"));
            }
            state.println("The slimy creature asks " + riddleGuy.getName() + " for the answer to a tricky riddle.");
            SkillCheckResult result = model.getParty().doSkillCheckWithReRoll(model, state, riddleGuy, Skill.Logic, difficulty, 20, 0);
            if (result.isSuccessful()) {
                state.println(riddleGuy.getName() + " answered the riddle correctly.");
                return getSuccessEdge();
            }
            state.println(riddleGuy.getName() + " answered the riddle incorrectly.");
            return getFailEdge();
        }

        @Override
        protected MyColors getSuccessEdgeColor() {
            return MyColors.LIGHT_GREEN;
        }

        @Override
        public String getDetailedDescription() {
            String diffStr = "E";
            if (difficulty > 9) {
                diffStr = "H";
            } else if (difficulty > 6) {
                diffStr = "M";
            }
            return "Logic " + diffStr;
        }
    }

    private static final Sprite SOLO_SPRITE = new Sprite32x32("soloskillscene", "quest.png", 0x10,
                             MyColors.BLACK, MyColors.WHITE, MyColors.RED, MyColors.BLACK);

    private class RiddleDecorativeSubScene extends QuestSubScene {
        public RiddleDecorativeSubScene(int col, int row) {
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
            model.getScreenHandler().register(SOLO_SPRITE.getName(), new Point(xPos, yPos), SOLO_SPRITE, 1);
            if (riddleGuy != null) {
                model.getScreenHandler().register(riddleGuy.getAvatarSprite().getName(), new Point(xPos, yPos),
                        riddleGuy.getAvatarSprite(), 3);
            }
        }

        @Override
        public String getDescription() {
            return "Logic 6, Logic 7, Logic 8";
        }

        @Override
        public QuestEdge run(Model model, QuestState state) {
            throw new IllegalStateException("Should never run this subscene");
        }
    }

    private class MergePointJunction extends SimpleJunction {
        public MergePointJunction(int col, int row, QuestEdge edge) {
            super(col, row, edge);
        }

        @Override
        public QuestEdge run(Model model, QuestState state) {
            QuestEdge toReturn = super.run(model, state);
            if (riddleGuy == null) {
                return toReturn;
            }

            GameCharacter rejoiner = riddleGuy;
            riddleGuy = null;

            Point a = QuestSubView.convertToScreen(RIDDLE_JUNCTION_POINT);
            Point b = QuestSubView.convertToScreen(new Point(RIDDLE_JUNCTION_POINT.x, getRow()));
            Point c = QuestSubView.convertToScreen(new Point(getColumn(), getRow()));

            state.getSubView().addMovementAnimation(rejoiner.getAvatarSprite(), a, b);
            state.getSubView().waitForAnimation();
            state.getSubView().removeMovementAnimation();
            state.getSubView().addMovementAnimation(rejoiner.getAvatarSprite(), b, c);
            state.getSubView().waitForAnimation();
            state.getSubView().removeMovementAnimation();

            model.getParty().unbenchAll();
            state.println(rejoiner.getName() + " has rejoined the party.");

            model.getParty().partyMemberSay(model, model.getParty().getLeader(), rejoiner.getFirstName() + "! There you are. We've been looking all over for you!3");
            model.getParty().partyMemberSay(model, rejoiner,
                    List.of("You won't believe what happened. This weird slimy creature wouldn't show me the way out unless I played this strange riddle game with him."));
            model.getParty().partyMemberSay(model, model.getParty().getLeader(), "How bizarre.");
            model.getParty().partyMemberSay(model, rejoiner,
                    List.of("Yeah. Can we get out of here now? This place gives me the creeps."));
            model.getParty().partyMemberSay(model, model.getParty().getLeader(), "Sure thing. Good to have you back " + rejoiner.getFirstName() + "!");


            return toReturn;
        }

        @Override
        public String getDescription() {
            return "";
        }
    }

    private static List<QuestBackground> makeBackground() {
        List<QuestBackground> list = new ArrayList<>();
        list.add(new QuestBackground(new Point(1, 1), SLIMY_CREATURE_SPRITE, false));
        list.add(new QuestBackground(new Point(0, 0), AbandonedMineQuest.ENTRANCE, false));
        for (int i = 1; i < 6; ++i) {
            list.add(new QuestBackground(new Point(i, 0), AbandonedMineQuest.HORIZONTAL, false));
        }
        list.add(new QuestBackground(new Point(6, 0), AbandonedMineQuest.UR_CORNER, false));
        list.add(new QuestBackground(new Point(6, 1), AbandonedMineQuest.LR_CORNER, false));
        for (int i = 5; i > 2; --i) {
            list.add(new QuestBackground(new Point(i, 1), AbandonedMineQuest.HORIZONTAL, false));
        }
        list.add(new QuestBackground(new Point(2, 1), AbandonedMineQuest.UL_CORNER, false));
        list.add(new QuestBackground(new Point(2, 2), AbandonedMineQuest.LL_CORNER, false));
        for (int i = 3; i < 8; ++i) {
            list.add(new QuestBackground(new Point(i, 2), AbandonedMineQuest.HORIZONTAL, false));
        }
        list.add(new QuestBackground(new Point(7, 2), AbandonedMineQuest.UR_CORNER, false));
        list.add(new QuestBackground(new Point(7, 3), AbandonedMineQuest.LR_CORNER, false));
        for (int i = 6; i > 2; --i) {
            list.add(new QuestBackground(new Point(i, 3), AbandonedMineQuest.HORIZONTAL, false));
        }
        list.add(new QuestBackground(new Point(2, 3), AbandonedMineQuest.UL_CORNER, false));
        list.add(new QuestBackground(new Point(2, 4), AbandonedMineQuest.LL_CORNER, false));
        for (int i = 3; i < 5; ++i) {
            list.add(new QuestBackground(new Point(i, 4), AbandonedMineQuest.HORIZONTAL, false));
        }
        list.add(new QuestBackground(new Point(5, 4), AbandonedMineQuest.UR_CORNER, false));
        list.add(new QuestBackground(new Point(5, 5), AbandonedMineQuest.LR_CORNER, false));
        for (int i = 4; i > 2; --i) {
            list.add(new QuestBackground(new Point(i, 5), AbandonedMineQuest.HORIZONTAL, false));
        }
        list.add(new QuestBackground(new Point(2, 5), AbandonedMineQuest.UL_CORNER, false));
        list.add(new QuestBackground(new Point(2, 6), AbandonedMineQuest.LL_CORNER, false));
        for (int i = 3; i < 6; ++i) {
            list.add(new QuestBackground(new Point(i, 6), AbandonedMineQuest.HORIZONTAL, false));
        }
        list.add(new QuestBackground(new Point(6, 6), AbandonedMineQuest.UR_CORNER, false));
        list.add(new QuestBackground(new Point(6, 7), AbandonedMineQuest.VERTICAL, false));


        for (int i = 1; i < 7; ++i) {
            list.add(new QuestBackground(new Point(0, i), AbandonedMineQuest.VERTICAL, false));
        }
        list.add(new QuestBackground(new Point(0, 7), AbandonedMineQuest.LL_CORNER, false));
        list.add(new QuestBackground(new Point(1, 6), AbandonedMineQuest.UR_CORNER, false));
        for (int i = 1; i < 6; ++i) {
            list.add(new QuestBackground(new Point(i, 7), AbandonedMineQuest.HORIZONTAL, false));
        }
        return list;
    }
}
