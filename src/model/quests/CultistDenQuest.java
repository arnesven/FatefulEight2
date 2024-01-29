package model.quests;

import model.Model;
import model.characters.PersonalityTrait;
import model.characters.appearance.CharacterAppearance;
import model.classes.Classes;
import model.classes.Skill;
import model.combat.StandardCombatLoot;
import model.enemies.CultistEnemy;
import model.enemies.CultistLeaderEnemy;
import model.enemies.ElderDaemonEnemy;
import model.enemies.Enemy;
import model.quests.scenes.CollaborativeSkillCheckSubScene;
import model.quests.scenes.CombatSubScene;
import model.quests.scenes.SoloSkillCheckSubScene;
import model.races.AllRaces;
import model.states.DailyEventState;
import model.states.QuestState;
import view.LogView;
import view.sprites.DungeonWallSprite;
import util.MyRandom;
import view.MyColors;
import view.sprites.Sprite32x32;
import view.subviews.PortraitSubView;
import view.widget.QuestBackground;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class CultistDenQuest extends Quest {
    private static final int TIME_MINUTES = 5;
    private static final String INTRO = "A group of cultists are reportedly performing some dark ritual to resurrect " +
            "an other-wordly demigod. Stop them.\n" +
            LogView.RED_COLOR + "This is a timed quest. You have " + TIME_MINUTES + " minutes until the ritual is complete." + LogView.DEFAULT_COLOR;
    private static final String ENDING = "You have cleared out the cultist den. The cleric thanks you for dealing with the cultist threat.";
    private static final CharacterAppearance PORTRAIT = PortraitSubView.makeRandomPortrait(Classes.TEMPLE_GUARD, AllRaces.ALL);


    public CultistDenQuest() {
        super("Cultist Den", "Cleric", QuestDifficulty.MEDIUM, 1, 75, 0, INTRO, ENDING);
    }

    @Override
    public int getTimeLimitSeconds() {
        return TIME_MINUTES * 60;
    }

    @Override
    public boolean clockEnabled() {
        return true;
    }

    @Override
    public boolean clockTimeOutFailsQuest() {
        return false;
    }

    @Override
    public CharacterAppearance getPortrait() {
        return PORTRAIT;
    }

    @Override
    protected List<QuestScene> buildScenes() {
        return List.of(
                new QuestScene("Trapped Locked Door", List.of(
                    new SoloSkillCheckSubScene(2, 1, Skill.Security, 11, "Can somebody disable the trap mechanism?"),
                    new CollaborativeSkillCheckSubScene(3, 1, Skill.Security, 11, "Can we jimmie the lock?"))),
                new QuestScene("Cultists", List.of(
                        new CultistCombatSubScene(2, 3, 4)
                )),
                new QuestScene("More Cultists", List.of(
                        new CultistCombatSubScene(2, 5, 5))),
                new QuestScene("Room With Loot", List.of(
                        new LootRomSubScene(4, 5))),
                new QuestScene("Ritual Chamber", List.of(
                        new CheckTimeSubScene(4, 7),
                        new CultistLeaderCombatSubScene(3, 7),
                        new ElderDaemonSubScene(5, 7)
                )));
    }

    @Override
    protected List<QuestJunction> buildJunctions(List<QuestScene> scenes) {
        QuestStartPointWithoutDecision qsp = new QuestStartPointWithoutDecision(
                new QuestEdge(scenes.get(0).get(0)), "Maybe we don't have to rush into this");
        return List.of(qsp);
    }

    @Override
    protected void connectScenesToJunctions(List<QuestScene> scenes, List<QuestJunction> junctions) {
        scenes.get(0).get(0).connectFail(scenes.get(1).get(0));
        scenes.get(0).get(0).connectSuccess(scenes.get(0).get(1));
        scenes.get(0).get(1).connectFail(scenes.get(1).get(0), QuestEdge.VERTICAL);
        scenes.get(0).get(1).connectSuccess(scenes.get(3).get(0));

        scenes.get(1).get(0).connectSuccess(scenes.get(2).get(0));
        scenes.get(1).get(0).connectFail(getFailEndingNode());

        scenes.get(2).get(0).connectSuccess(scenes.get(3).get(0));
        scenes.get(2).get(0).connectFail(getFailEndingNode());

        scenes.get(3).get(0).connectSuccess(scenes.get(4).get(0));

        scenes.get(4).get(0).connectSuccess(scenes.get(4).get(1));
        scenes.get(4).get(0).connectFail(scenes.get(4).get(2));

        scenes.get(4).get(1).connectSuccess(getSuccessEndingNode(), QuestEdge.VERTICAL);
        scenes.get(4).get(2).connectSuccess(getSuccessEndingNode());
    }

    @Override
    public MyColors getBackgroundColor() {
        return MyColors.GRAY_RED;
    }

    @Override
    public List<QuestBackground> getBackgroundSprites() {
        return bgSprites;
    }



    private static List<QuestBackground> bgSprites = makeBackground();

    private static List<QuestBackground> makeBackground() {
        List<QuestBackground> result = new ArrayList<>();
        result.add(new QuestBackground(new Point(0, 0), DungeonWallSprite.DOOR));
        result.add(new QuestBackground(new Point(1, 0), DungeonWallSprite.UPPER_WALL));
        for (int i = 6; i < 8; ++i) {
            result.add(new QuestBackground(new Point(i, 0), DungeonWallSprite.FULL_WALL));
        }

        for (int row = 1; row < 3; ++row) {
            for (int col = 6; col < 8; ++col) {
                result.add(new QuestBackground(new Point(col, row), DungeonWallSprite.FULL_WALL));
            }
        }

        for (int row = 2; row < 9; ++row) {
            result.add(new QuestBackground(new Point(0, row), DungeonWallSprite.FULL_WALL, false));
            result.add(new QuestBackground(new Point(7, row), DungeonWallSprite.FULL_WALL, false));
            if (row < 6) {
                result.add(new QuestBackground(new Point(6, row), DungeonWallSprite.FULL_WALL, false));
            }
        }

        result.add(new QuestBackground(new Point(1, 2), DungeonWallSprite.DOOR, false));


        result.add(new QuestBackground(new Point(2, 2), DungeonWallSprite.UPPER_WALL, false));
        result.add(new QuestBackground(new Point(3, 2), DungeonWallSprite.UPPER_T, false));
        result.add(new QuestBackground(new Point(4, 2), DungeonWallSprite.DOOR, false));
        result.add(new QuestBackground(new Point(5, 2), DungeonWallSprite.UPPER_WALL, false));

        result.add(new QuestBackground(new Point(3, 3), DungeonWallSprite.VERTI_WALL, false));

        result.add(new QuestBackground(new Point(1, 4), DungeonWallSprite.DOOR, false));
        result.add(new QuestBackground(new Point(2, 4), DungeonWallSprite.UPPER_WALL, false));
        result.add(new QuestBackground(new Point(3, 4), DungeonWallSprite.UPPER_T, false));
        result.add(new QuestBackground(new Point(4, 4), DungeonWallSprite.DOOR, false));
        result.add(new QuestBackground(new Point(5, 4), DungeonWallSprite.UPPER_WALL, false));

        result.add(new QuestBackground(new Point(3, 5), DungeonWallSprite.VERTI_WALL, false));

        result.add(new QuestBackground(new Point(1, 6), DungeonWallSprite.DOOR, false));
        result.add(new QuestBackground(new Point(2, 6), DungeonWallSprite.FULL_WALL, false));
        result.add(new QuestBackground(new Point(3, 6), DungeonWallSprite.UPPER_WALL, false));
        result.add(new QuestBackground(new Point(4, 6), DungeonWallSprite.DOOR, false));
        result.add(new QuestBackground(new Point(5, 6), DungeonWallSprite.UPPER_WALL, false));
        result.add(new QuestBackground(new Point(6, 6), DungeonWallSprite.UPPER_WALL, false));

        result.add(new QuestBackground(new Point(2, 7), DungeonWallSprite.FULL_WALL, false));

        result.add(new QuestBackground(new Point(2, 8), DungeonWallSprite.FULL_WALL, false));
        return result;
    }


    private static List<Enemy> makeCultistList(int noOfCultists) {
        List<Enemy> result = new ArrayList<>();
        for (int i = 0; i < noOfCultists; ++i) {
            result.add(new CultistEnemy('A'));
        }
        return result;
    }

    private static class CultistCombatSubScene extends CombatSubScene {
        private final int noOfCultists;

        public CultistCombatSubScene(int col, int row, int noOfCultists) {
            super(col, row, makeCultistList(noOfCultists), true);
            this.noOfCultists = noOfCultists;
        }


        @Override
        protected String getCombatDetails() {
            return (noOfCultists>4?"More ":"") + "Cultists";
        }
    }
    private static final Sprite32x32 SPRITE = new Sprite32x32("lootsubscene", "quest.png", 0x26,
            MyColors.BLACK, MyColors.WHITE, MyColors.RED, MyColors.BLACK);

    private static class LootRomSubScene extends QuestSubScene {
        public LootRomSubScene(int col, int row) {
            super(col, row);
        }

        @Override
        protected MyColors getSuccessEdgeColor() {
            return MyColors.WHITE;
        }

        @Override
        public String getDetailedDescription() {
            return "Loot";
        }

        @Override
        public void drawYourself(Model model, int xPos, int yPos) {
            model.getScreenHandler().register(SPRITE.getName(), new Point(xPos, yPos), SPRITE, 1);
        }

        @Override
        public String getDescription() {
            return "Room with loot";
        }

        @Override
        public QuestEdge run(Model model, QuestState state) {
            state.leaderSay("Wow, these cultists sure have collected some loot!");
            state.randomSayIfPersonality(PersonalityTrait.naive, new ArrayList<>(),
                    "This is our chance to get stinking rich!");
            model.getParty().randomPartyMemberSay(model, List.of("But I see a lot of worthless junk too..."));
            state.print("There may be valuables in this room. Do you wish to attempt to search for loot? (Y/N) ");
            if (state.yesNoInput()) {
                int maxTimes = model.getParty().size() * 4;
                for (int i = 0; i < maxTimes; ++i) {
                    boolean result = model.getParty().doCollaborativeSkillCheck(model, state, Skill.Search, 12);
                    if (result) {
                        StandardCombatLoot loot = new StandardCombatLoot(model);
                        if (loot.getText().equals("")) {
                            state.println("You have found something valuable, " + loot.getText() + ".");
                        } else {
                            state.println("You have found " + loot.getGold() + " gold.");
                        }
                        loot.giveYourself(model.getParty());
                    }
                    state.print("There may still be something of value here. Do you wish to search? (Y/N) ");
                    if (!state.yesNoInput()) {
                        state.leaderSay("Come on team, we can't spend more time here.");
                        break;
                    }
                }
            } else {
                state.leaderSay("Come on team, we need to press on.");
            }
            return getSuccessEdge();
        }
    }

    private static class CheckTimeSubScene extends ConditionSubScene {
        public CheckTimeSubScene(int col, int row) {
            super(col, row);
        }

        @Override
        public String getDescription() {
            return "Less than " + TIME_MINUTES + " minutes passed?";
        }

        @Override
        public QuestEdge run(Model model, QuestState state) {
            long time = state.getClockTime();
            if (time == 0) {
                state.leaderSay("Oh no, the ritual is already complete! They've summoned a daemon!");
                return getFailEdge();
            }
            state.leaderSay("Hey there! Stop performing that ritual!");
            state.printQuote("Cultist Leader", "Just try and stop me!");
            boolean gender = MyRandom.flipCoin();
            state.leaderSay("Get " + DailyEventState.himOrHer(gender) + "!");

            return getSuccessEdge();
        }
    }

    private static class CultistLeaderCombatSubScene extends CombatSubScene {
        public CultistLeaderCombatSubScene(int col, int row) {
            super(col, row, List.of(new CultistLeaderEnemy('A')));
        }

        @Override
        protected String getCombatDetails() {
            return "Cult Leader";
        }
    }

    private static class ElderDaemonSubScene extends CombatSubScene{
        public ElderDaemonSubScene(int col, int row) {
            super(col, row, List.of(new ElderDaemonEnemy('A')), false);
        }

        @Override
        protected String getCombatDetails() {
            return "Elder Daemon";
        }
    }
}
