package model.quests;

import model.characters.GameCharacter;
import model.characters.JordynStrong;
import model.characters.MiklosAutumntoft;
import model.characters.appearance.CharacterAppearance;
import model.classes.CharacterClass;
import model.classes.Classes;
import model.classes.Skill;
import model.enemies.DaemonEnemy;
import model.items.Equipment;
import model.items.accessories.FullHelm;
import model.items.accessories.SkullCap;
import model.items.clothing.LeatherArmor;
import model.items.clothing.ScaleArmor;
import model.items.weapons.Pickaxe;
import model.items.weapons.Warhammer;
import model.quests.scenes.CollaborativeSkillCheckSubScene;
import model.quests.scenes.CollectiveSkillCheckSubScene;
import model.quests.scenes.CombatSubScene;
import model.quests.scenes.SoloSkillCheckSubScene;
import model.races.Race;
import view.MyColors;
import view.sprites.Sprite;
import view.sprites.Sprite32x32;
import view.subviews.PortraitSubView;
import view.widget.QuestBackground;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class AbandonedMineQuest extends Quest {
    private static final String INTRO =
            "Murak the dwarven miner has contracted an escort to return to a nearby mine " +
            "to retrieve some equipment which was hastily left behind. He is curiously " +
            "tight lipped about why the Mine was abandoned.";
    private static final String OUTRO =
            "Murak has retrieved his missing equipment and is a little embarrassed about the " +
                    "fire daemon. Fortunately he compensates you well for your trouble.";
    public static final Sprite ENTRANCE = new Sprite32x32("mineentrance", "quest.png", 0x46, MyColors.GREEN, MyColors.DARK_GREEN, MyColors.BROWN, MyColors.BLACK);
    public static final Sprite UR_CORNER = new Sprite32x32("mineURcorner", "quest.png", 0x4B, MyColors.BLACK, MyColors.GRAY, MyColors.BROWN);
    public static final Sprite LR_CORNER = new Sprite32x32("mineLRcorner", "quest.png", 0x4C, MyColors.BLACK, MyColors.GRAY, MyColors.BROWN);
    public static final Sprite UL_CORNER = new Sprite32x32("mineLRcorner", "quest.png", 0x4A, MyColors.BLACK, MyColors.GRAY, MyColors.BROWN);
    public static final Sprite LL_CORNER = new Sprite32x32("mineLLcorner", "quest.png", 0x49, MyColors.BLACK, MyColors.GRAY, MyColors.BROWN);
    public static final Sprite VERTICAL = new Sprite32x32("minevertical", "quest.png", 0x47, MyColors.BLACK, MyColors.GRAY, MyColors.BROWN);
    public static final Sprite HORIZONTAL = new Sprite32x32("minehorizontal", "quest.png", 0x48, MyColors.BLACK, MyColors.GRAY, MyColors.BROWN);
    public static final Sprite WATER = new Sprite32x32("watertunnel", "quest.png", 0x48, MyColors.BLACK, MyColors.BLUE, MyColors.BROWN);

    private static final List<QuestBackground> BG_SPRITES = makeBackground();
    private static final CharacterAppearance PORTRAIT = PortraitSubView.makeRandomPortrait(Classes.MIN, Race.DWARF);

    public AbandonedMineQuest() {
        super("Abandoned Mine", "Murak", QuestDifficulty.HARD, 1, 50, 0, INTRO, OUTRO);
    }

    @Override
    public CharacterAppearance getPortrait() {
        return PORTRAIT;
    }

    @Override
    protected List<QuestScene> buildScenes() {
        return List.of(
                new QuestScene("Blocked Entrance",
                List.of(new CollaborativeSkillCheckSubScene(1, 1, Skill.Labor, 10,
                        "The entrance needs to be cleared if we're going down there."))),
                new QuestScene("Chasm",
                List.of(new SoloSkillCheckSubScene(1, 2, Skill.Acrobatics, 10,
                        "Somehow we need to get across. Can somebody leap to the other side with a rope?"),
                        new CollectiveSkillCheckSubScene(1, 3, Skill.Acrobatics, 3,
                                "Now the rest of us just need to climb the rope"))),
                new QuestScene("Long Passageways",
                List.of(new CollectiveSkillCheckSubScene(1, 4, Skill.Endurance, 3,
                        "These tunnels are never-ending. Murak, are you sure you know your way here?"))),
                new QuestScene("Flooded Tunnel",
                List.of(new CollectiveSkillCheckSubScene(1, 5, Skill.Endurance, 4,
                        "We'll need to swim under water to get past this."))),
                new QuestScene("Fire Daemon",
                List.of(new FireDaemonCombatScene(6, 7),
                        new CollaborativeSkillCheckSubScene(5, 7, Skill.MagicBlack, 12,
                                "Perhaps we can bind him with black magic!"),
                        new SoloSkillCheckSubScene(7, 7, Skill.Leadership, 10,
                                "Come on people, we don't need to tussle with this menace. Let's just get out of here!"))));
    }

    @Override
    protected List<QuestJunction> buildJunctions(List<QuestScene> scenes) {
        QuestStartPointWithoutDecision qsp = new QuestStartPointWithoutDecision(new QuestEdge(scenes.get(0).get(0)),
                "The entranced is blocked by rubble.");
        QuestDecisionPoint qd = new QuestDecisionPoint(6, 6, List.of(
                new QuestEdge(scenes.get(4).get(0)),
                new QuestEdge(scenes.get(4).get(1)),
                new QuestEdge(scenes.get(4).get(2))),
                "Is that a... fire daemon? Murak, you haven't been completely honest with us!");
        SimpleJunction extraFail = new SimpleJunction(0, 8, new QuestEdge(getFailEndingNode()));

        SimpleJunction long4 = new SimpleJunction(3, 7, new QuestEdge(scenes.get(3).get(0)));
        SimpleJunction long3 = new SimpleJunction(7, 4, new QuestEdge(long4));
        SimpleJunction long2 = new SimpleJunction(2, 1, new QuestEdge(long3));
        SimpleJunction long1 = new SimpleJunction(2, 4, new QuestEdge(long2));

        return List.of(qsp, qd, extraFail, long1, long2, long3, long4);
    }

    @Override
    protected void connectScenesToJunctions(List<QuestScene> scenes, List<QuestJunction> junctions) {
        scenes.get(0).get(0).connectFail(junctions.get(2));
        scenes.get(0).get(0).connectSuccess(scenes.get(1).get(0));

        scenes.get(1).get(0).connectFail(junctions.get(2));
        scenes.get(1).get(0).connectSuccess(scenes.get(1).get(1));
        scenes.get(1).get(1).connectFail(junctions.get(2));
        scenes.get(1).get(1).connectSuccess(scenes.get(2).get(0));

        scenes.get(2).get(0).connectFail(junctions.get(2));
        scenes.get(2).get(0).connectSuccess(junctions.get(3));

        scenes.get(3).get(0).connectFail(junctions.get(2));
        scenes.get(3).get(0).connectSuccess(junctions.get(1));

        scenes.get(4).get(0).connectSuccess(getSuccessEndingNode(), QuestEdge.VERTICAL);
        scenes.get(4).get(1).connectFail(scenes.get(4).get(0));
        scenes.get(4).get(1).connectSuccess(getSuccessEndingNode(), QuestEdge.VERTICAL);
        scenes.get(4).get(2).connectFail(scenes.get(4).get(0));
        scenes.get(4).get(2).connectSuccess(getSuccessEndingNode(), QuestEdge.VERTICAL);
    }

    @Override
    public MyColors getBackgroundColor() {
        return MyColors.BLACK;
    }

    @Override
    public boolean drawTownOrCastleInBackground() {
        return true;
    }

    @Override
    public List<QuestBackground> getBackgroundSprites() {
        return BG_SPRITES;
    }

    private static List<QuestBackground> makeBackground() {

        List<QuestBackground> list = new ArrayList<>();
        list.add(new QuestBackground(new Point(1, 0), ENTRANCE));
        list.add(new QuestBackground(new Point(2, 0), UR_CORNER));
        list.add(new QuestBackground(new Point(2, 1), LR_CORNER));
        list.add(new QuestBackground(new Point(1, 1), UL_CORNER));
        for (int i = 2; i < 5; ++i) {
            list.add(new QuestBackground(new Point(1, i), VERTICAL));
        }
        list.add(new QuestBackground(new Point(1, 5), LL_CORNER));
        list.add(new QuestBackground(new Point(2, 5), LR_CORNER));
        list.add(new QuestBackground(new Point(2, 4), VERTICAL));
        list.add(new QuestBackground(new Point(2, 3), VERTICAL));
        list.add(new QuestBackground(new Point(2, 2), UL_CORNER));
        list.add(new QuestBackground(new Point(3, 2), LR_CORNER));
        list.add(new QuestBackground(new Point(3, 1), VERTICAL));
        list.add(new QuestBackground(new Point(3, 0), UL_CORNER));
        list.add(new QuestBackground(new Point(4, 0), HORIZONTAL));
        list.add(new QuestBackground(new Point(5, 0), UR_CORNER));
        list.add(new QuestBackground(new Point(5, 1), VERTICAL));
        list.add(new QuestBackground(new Point(5, 2), VERTICAL));
        list.add(new QuestBackground(new Point(5, 3), LL_CORNER));
        list.add(new QuestBackground(new Point(6, 3), LR_CORNER));
        list.add(new QuestBackground(new Point(6, 2), VERTICAL));
        list.add(new QuestBackground(new Point(6, 1), UL_CORNER));
        list.add(new QuestBackground(new Point(7, 1), UR_CORNER));
        list.add(new QuestBackground(new Point(7, 2), VERTICAL));
        list.add(new QuestBackground(new Point(7, 3), VERTICAL));
        list.add(new QuestBackground(new Point(7, 4), LR_CORNER));
        list.add(new QuestBackground(new Point(6, 4), HORIZONTAL));
        list.add(new QuestBackground(new Point(5, 4), HORIZONTAL));
        list.add(new QuestBackground(new Point(4, 4), LL_CORNER));
        list.add(new QuestBackground(new Point(4, 3), UR_CORNER));
        list.add(new QuestBackground(new Point(3, 3), UL_CORNER));
        list.add(new QuestBackground(new Point(3, 4), VERTICAL));
        list.add(new QuestBackground(new Point(3, 5), VERTICAL));
        list.add(new QuestBackground(new Point(3, 6), VERTICAL));
        list.add(new QuestBackground(new Point(3, 7), LR_CORNER));
        list.add(new QuestBackground(new Point(2, 7), HORIZONTAL));
        list.add(new QuestBackground(new Point(1, 7), LL_CORNER));
        list.add(new QuestBackground(new Point(1, 6), UL_CORNER));
        list.add(new QuestBackground(new Point(2, 6), WATER));
        list.add(new QuestBackground(new Point(4, 6), WATER));
        list.add(new QuestBackground(new Point(5, 6), HORIZONTAL));
        list.add(new QuestBackground(new Point(6, 6), UR_CORNER));
        list.add(new QuestBackground(new Point(6, 7), VERTICAL));
        list.add(new QuestBackground(new Point(6, 8), VERTICAL));
        return list;
    }

    private static class FireDaemonCombatScene extends CombatSubScene {
        public FireDaemonCombatScene(int col, int row) {
            super(col, row, List.of(new DaemonEnemy('A')));
        }

        @Override
        protected String getCombatDetails() {
            return "Fire Daemon";
        }

        @Override
        protected List<GameCharacter> getAllies() {
            GameCharacter murak = new GameCharacter("Murak", "", Race.DWARF, Classes.MIN, new JordynStrong(),
                    new CharacterClass[]{Classes.None, Classes.None, Classes.None, Classes.None},
                    new Equipment(new Pickaxe(), new LeatherArmor(), new FullHelm()));
            return List.of(murak);
        }
    }
}
