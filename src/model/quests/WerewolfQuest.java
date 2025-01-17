package model.quests;

import model.Model;
import model.characters.appearance.AdvancedAppearance;
import model.characters.appearance.CharacterAppearance;
import model.classes.Classes;
import model.classes.Skill;
import model.enemies.Enemy;
import model.enemies.WerewolfEnemy;
import model.items.Item;
import model.items.potions.Potion;
import model.items.potions.SleepingPotion;
import model.items.spells.WerewolfFormSpell;
import model.quests.scenes.CollaborativeSkillCheckSubScene;
import model.quests.scenes.SoloSkillCheckSubScene;
import model.quests.scenes.TimedCombatSubScene;
import model.races.Race;
import model.states.GameState;
import model.states.QuestState;
import sound.BackgroundMusic;
import util.MyLists;
import view.BorderFrame;
import view.MyColors;
import view.combat.NightGrassCombatTheme;
import view.sprites.Sprite;
import view.sprites.Sprite32x32;
import view.combat.CombatTheme;
import view.combat.GrassCombatTheme;
import view.subviews.PortraitSubView;
import view.subviews.TownSubView;
import view.widget.QuestBackground;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class WerewolfQuest extends Quest {
    private static final int TURNS_TO_SURVIVE = 4;
    private static final String INTRO = "Benny is worried his girlfriend Sandy has been abducted by some kind of monster. " +
            "She was last seen in her home in town. Where could she have been taken, and what took her?";
    private static final String ENDING = "Relieved that Sandy was not abducted by a monster, Benny gladly pays " +
            "you your reward.";
    private static final List<QuestBackground> BACKGROUND_SPRITES = makeBackground();

    private AdvancedAppearance PORTRAIT = PortraitSubView.makeRandomPortrait(Classes.None, Race.ALL, false);
    private boolean getSpell = false;

    public WerewolfQuest() {
        super("Werewolf", "Boyfriend Benny", QuestDifficulty.EASY,
                new Reward(0, 80), 1, INTRO, ENDING);
    }

    @Override
    protected void resetQuest() {
        super.resetQuest();
        getSpell = false;
    }

    @Override
    protected List<String> getSpecialRewards() {
        return List.of("Spell");
    }

    @Override
    public List<QuestBackground> getBackgroundSprites() {
        return BACKGROUND_SPRITES;
    }

    @Override
    public GameState endOfQuest(Model model, QuestState state, boolean questWasSuccess) {
        if (getSpell) {
            WerewolfFormSpell spell = new WerewolfFormSpell();
            state.println("Since you successfully completed the magical examination of the werewolf, " +
                    "you've gained a spell: " + spell.getName() + ".");
            spell.addYourself(model.getParty().getInventory());
        }
        return super.endOfQuest(model, state, questWasSuccess);
    }

    @Override
    public BackgroundMusic getMusic() {
        return BackgroundMusic.mysticSong;
    }

    @Override
    public CharacterAppearance getPortrait() {
        return PORTRAIT;
    }

    @Override
    protected List<QuestScene> buildScenes() {
        return List.of(
                new QuestScene("Sandra's House",
                List.of(new CollaborativeSkillCheckSubScene(1, 0, Skill.Search, 8,
                                "Let's see what we can find in here."),
                        new SoloSkillCheckSubScene(1, 1, Skill.Perception, 7,
                                "Perhaps one of us can see something the others won't?"))),
                new QuestScene("Find the Cave",
                List.of(new SoloSkillCheckSubScene(5, 2, Skill.Survival, 5,
                                "Who can find the cave?"),
                        new SoloSkillCheckSubScene(4, 2, Skill.Search, 5,
                                "Can anybody find the tracks of this monster?"))),
                new QuestScene("Encounter with Sandra",
                List.of(new WerewolfTimedCombatSubScene(3, 5, List.of(new WerewolfEnemy('A'))),
                        new SleepingPotionSubScene(4, 6))),
                new QuestScene("Convince Benny",
                List.of(new CollaborativeSkillCheckSubScene(3, 8, Skill.Persuade, 8,
                        "Now we just have to convince Benny to stick with Sandra."))),
                new QuestScene("Examine Sandra",
                        List.of(new SpecialSoloSkillCheckSubScene(2, 6))));
    }

    @Override
    public CombatTheme getCombatTheme() {
        return new NightGrassCombatTheme();
    }

    @Override
    protected List<QuestJunction> buildJunctions(List<QuestScene> scenes) {
        QuestStartPoint qsp = new WerewolfQuestStartPoint(List.of(
                new QuestEdge(scenes.get(0).get(0), QuestEdge.VERTICAL),
                new QuestEdge(scenes.get(0).get(1), QuestEdge.VERTICAL)),
                "Let's search Sandra's home first, maybe we'll find a clue there.");

        QuestDecisionPoint qdp1 = new QuestDecisionPoint(4, 1, List.of(
                new QuestEdge(scenes.get(1).get(0)),
                new QuestEdge(scenes.get(1).get(1))),
                "Hmm... torn up clothing... but there's no blood. What's this, " +
                        "soil from a cave? Can we assume this monster lives in a cave then?");

        QuestDecisionPoint qdp2 = new QuestDecisionPoint(4, 4, List.of(
                new QuestEdge(scenes.get(2).get(0)),
                new QuestEdge(scenes.get(2).get(1))),
                "Whaaa? A werewolf - wait, that must be Sandy! We can't just kill her, what do we do?");


        QuestDecisionPoint qdp3 = new QuestDecisionPoint(3, 7, List.of(
                new QuestEdge(scenes.get(3).get(0)),
                new QuestEdge(scenes.get(4).get(0))),
                "We should take this opportunity to study this " +
                        "specimen of werewolf, maybe we can learn something.");

        StoryJunction sj = new StoryJunction(3, 6, new QuestEdge(qdp3)) {
            @Override
            protected void doAction(Model model, QuestState state) {
                state.println("You carry the sleeping Sandra back to her home. You need to report your discovery " +
                        "about Sandra to Benny. But will he be relieved or not? Perhaps you can convince him to " +
                        "help Sandra find a cure for her condition.");
            }
        };

        return List.of(qsp, qdp1, qdp2, sj, qdp3);
    }

    @Override
    protected void connectScenesToJunctions(List<QuestScene> scenes, List<QuestJunction> junctions) {
        scenes.get(0).get(0).connectFail(getFailEndingNode());
        scenes.get(0).get(0).connectSuccess(junctions.get(1));

        scenes.get(0).get(1).connectFail(getFailEndingNode());
        scenes.get(0).get(1).connectSuccess(junctions.get(1));

        scenes.get(1).get(0).connectFail(getFailEndingNode());
        scenes.get(1).get(0).connectSuccess(junctions.get(2), QuestEdge.VERTICAL);

        scenes.get(1).get(1).connectFail(getFailEndingNode());
        scenes.get(1).get(1).connectSuccess(junctions.get(2), QuestEdge.VERTICAL);

        scenes.get(2).get(0).connectFail(getFailEndingNode());
        scenes.get(2).get(0).connectSuccess(junctions.get(3));

        scenes.get(2).get(1).connectSuccess(junctions.get(3), QuestEdge.VERTICAL);

        scenes.get(3).get(0).connectFail(getFailEndingNode());
        scenes.get(3).get(0).connectSuccess(getSuccessEndingNode());

        scenes.get(4).get(0).connectSuccess(junctions.get(4));
        scenes.get(4).get(0).connectFail(getFailEndingNode());
    }


    @Override
    public MyColors getBackgroundColor() {
        return MyColors.DARK_GREEN;
    }

    private static class SleepingPotionSubScene extends QuestSubScene {
        private static final Sprite32x32 SPRITE = new Sprite32x32("lootsubscene", "quest.png", 0x26,
                MyColors.BLACK, MyColors.WHITE, MyColors.RED, MyColors.BLACK);

        public SleepingPotionSubScene(int col, int row) {
            super(col, row);
        }

        @Override
        protected MyColors getSuccessEdgeColor() {
            return MyColors.WHITE;
        }

        @Override
        public String getDetailedDescription() {
            return "Sleeping Potion";
        }

        @Override
        public void drawYourself(Model model, int xPos, int yPos) {
            model.getScreenHandler().register(SPRITE.getName(), new Point(xPos, yPos), SPRITE, 1);
        }

        @Override
        public String getDescription() {
            return "Use a sleeping potion";
        }

        @Override
        protected boolean isEligibleForSelection(Model model, QuestState state) {
            if (MyLists.find(model.getParty().getInventory().getPotions(),
                    (Potion p) -> p instanceof SleepingPotion) != null) {
                return true;
            }
            state.println("You do not have a sleeping potion to use.");
            return false;
        }

        @Override
        public QuestEdge run(Model model, QuestState state) {
            state.println("You use a sleep potion on the werewolf.");
            model.getParty().getInventory().remove(MyLists.find(model.getParty().getInventory().getAllItems(),
                    (Item it) -> it instanceof SleepingPotion));
            return getSuccessEdge();
        }
    }

    private static class WerewolfTimedCombatSubScene extends TimedCombatSubScene {

        public WerewolfTimedCombatSubScene(int col, int row, List<Enemy> enemies) {
            super(col, row, enemies, true, WerewolfQuest.TURNS_TO_SURVIVE);
        }

        @Override
        public QuestEdge run(Model model, QuestState state) {
            QuestEdge toReturn = super.run(model, state);
            if (getEnemies().get(0).isDead()) {
                return getFailEdge();
            }
            if (toReturn == getSuccessEdge()) {
                state.println("The moon is going down. As the moonlight fades " +
                        "Sandra turns back into human form and falls asleep?");
            }
            return toReturn;
        }
    }

    private class SpecialSoloSkillCheckSubScene extends SoloSkillCheckSubScene{
        public SpecialSoloSkillCheckSubScene(int col, int row) {
            super(col, row, Skill.MagicGreen, 8,
                    "One of us needs to examine the magical properties of this werewolf.");
        }

        @Override
        protected QuestEdge getEdgeToReturn(boolean skillCheckWasSuccessful) {
            if (skillCheckWasSuccessful) {
                WerewolfQuest.this.getSpell = true;
            }
            return super.getEdgeToReturn(skillCheckWasSuccessful);
        }
    }

    private static class WerewolfQuestStartPoint extends QuestStartPoint {
        public WerewolfQuestStartPoint(List<QuestEdge> edges, String talk) {
            super(edges, talk);
            setRow(2);
        }
    }

    private static List<QuestBackground> makeBackground() {
        Sprite townHouse = new Sprite32x32("townhousequest", "quest.png", 0x50,
                TownSubView.GROUND_COLOR_NIGHT, TownSubView.PATH_COLOR, MyColors.BROWN, MyColors.LIGHT_YELLOW);

        final Sprite32x32 sky1 = new Sprite32x32("sky1", "quest.png", 0x08,
                MyColors.BLACK, MyColors.LIGHT_GRAY, MyColors.DARK_GRAY, MyColors.DARK_BLUE);
        final Sprite32x32 sky2 = new Sprite32x32("sky2", "quest.png", 0x09,
                MyColors.BLACK, MyColors.LIGHT_GRAY, MyColors.DARK_GRAY, MyColors.DARK_BLUE);
        final Sprite32x32 sky4 = new Sprite32x32("sky4", "quest.png", 0x0B,
                MyColors.BLACK, MyColors.LIGHT_GRAY, MyColors.YELLOW, MyColors.DARK_BLUE);

        final Sprite32x32 horizon = new Sprite32x32("horizon", "combat.png", 0x12,
                TownSubView.GROUND_COLOR_NIGHT, MyColors.DARK_BLUE, MyColors.BROWN, MyColors.DARK_BROWN);

        final Sprite32x32 cave = new Sprite32x32("cave", "quest.png", 0x46,
                TownSubView.GROUND_COLOR_NIGHT, MyColors.GRAY_RED, MyColors.GRAY_RED, MyColors.BLACK);
        final Sprite woods = new Sprite32x32("woodsqmb", "quest.png", 0x88,
                MyColors.BLACK, MyColors.BROWN, MyColors.DARK_GREEN, TownSubView.GROUND_COLOR_NIGHT);

        List<QuestBackground> result = new ArrayList<>();

        Sprite[] skies = new Sprite[]{sky1, sky1, sky2, sky2, sky4, sky2, sky1, sky1};
        for (int x = 0; x < skies.length; ++x) {
            result.add(new QuestBackground(new Point(x, 0), skies[x]));
            result.add(new QuestBackground(new Point(x, 1), horizon));
        }
        result.add(new QuestBackground(new Point(2, 1), townHouse));

        for (int x = 0; x < 8; ++x) {
            for (int y = 3; y < 9; ++y) {
                if (!(x == 0 && y == 3)) {
                    result.add(new QuestBackground(new Point(x, y), woods));
                }
            }
        }
        result.add(new QuestBackground(new Point(7, 2), woods));
        result.add(new QuestBackground(new Point(6, 2), woods));
        result.add(new QuestBackground(new Point(2, 5), cave));

        return result;
    }

}
