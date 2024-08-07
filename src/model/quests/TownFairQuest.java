package model.quests;

import model.Model;
import model.characters.GameCharacter;
import model.characters.appearance.CharacterAppearance;
import model.classes.Classes;
import model.classes.Skill;
import model.items.Equipment;
import model.items.spells.FireworksSpell;
import model.items.spells.Spell;
import model.items.weapons.BowWeapon;
import model.items.weapons.CompetitionBow;
import model.items.weapons.YewBow;
import model.map.UrbanLocation;
import model.quests.scenes.CollaborativeSkillCheckSubScene;
import model.quests.scenes.SoloSkillCheckSubScene;
import model.races.Race;
import model.states.ArcheryState;
import model.states.QuestState;
import sound.BackgroundMusic;
import util.MyLists;
import view.BorderFrame;
import view.MyColors;
import view.sprites.Sprite;
import view.sprites.Sprite32x32;
import view.subviews.CollapsingTransition;
import view.subviews.PortraitSubView;
import view.widget.QuestBackground;
import view.widget.TopText;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TownFairQuest extends Quest {
    private static final String INTRO =
            "The town is having a fair on the coming market day, and there are many chores " +
            "to be done. This is a good opportunity to earn some extra coin, make some " +
            "connections and to join in the festivities and games!";
    private static final String ENDING =
            "Quite please with your contributions, you join in the rest of the " +
            "evening's festivities. What joy!";
    private static final CharacterAppearance APPEARANCE = PortraitSubView.makeRandomPortrait(Classes.NOB, Race.NORTHERN_HUMAN);

    public TownFairQuest() {
        super("Town Fair", "Townsfolk", QuestDifficulty.EASY, 0, 0, 0, INTRO, ENDING);
        getScenes().get(2).get(0).addSpellCallback(new FireworksSpell().getName(), new SpellCallback() {
            @Override
            public QuestEdge run(Model model, QuestState state, Spell spell, GameCharacter caster) {
                state.println("The people are overjoyed by the stunning display of pyrotechnics! " +
                        "The entertainment for this year has been a stunning success.");
                payOutFromSoloSkillCheckSubScene(model, state);
                return new QuestEdge(getScenes().get(3).get(0));
            }
        });
    }

    @Override
    public boolean drawTownOrCastleInBackground() {
        return true;
    }

    @Override
    public void drawSpecialReward(Model model, int x, int y) {
        BorderFrame.drawString(model.getScreenHandler(), "  *", x, y, MyColors.WHITE, MyColors.BLACK);
        model.getScreenHandler().put(x, y, TopText.GOLD_ICON_SPRITE);
    }

    @Override
    public BackgroundMusic getMusic() {
        return BackgroundMusic.happyMandolin;
    }

    @Override
    protected List<QuestScene> buildScenes() {
        return List.of(new QuestScene("Construction",
                List.of(new PayOutCollaborativeSkillCheckSubScene(1, 1, Skill.Labor, 10,
                        "Let's help put up these stalls and decorations."))),
                new QuestScene("Make Connections",
                List.of(new SoloSkillCheckSubScene(5, 2, Skill.SeekInfo, 10,
                        "Perhaps we can get some face time with the top brass around here."))),
                new QuestScene("Put on a Show",
                        List.of(new PayOutSoloSkillCheckSubScene(2, 3, Skill.Entertain, 12,
                                "Now for some fine entertainment for the townspeople."))),
                new QuestScene("Sprint Race",
                        List.of(new PayOutSoloSkillCheckSubScene(5, 5, Skill.Endurance, 12,
                                "A sprint race? I'm sure one of us is up for the challenge."))),
                new QuestScene("Eating Contest",
                        List.of(new PayOutSoloSkillCheckSubScene(1, 6, Skill.Endurance, 10,
                                "Next, an eating contest. Any volunteers?"))),
                new QuestScene("Shooting Contest",
                        List.of(new ArcherySubScene(5, 7,
                                "What town fair would be complete without a bow and arrow contest? Let's enter."))));
    }

    @Override
    protected List<QuestJunction> buildJunctions(List<QuestScene> scenes) {
        SpriteDecorativeJunction sj = new MeetLordJunction(6, 3);
        sj.connectTo(new QuestEdge(scenes.get(2).get(0)));
        SimpleJunction sj2 = new SimpleJunction(3, 4, new QuestEdge(scenes.get(3).get(0)));
        return List.of(new QuestStartPointWithoutDecision(new QuestEdge(scenes.get(0).get(0)),
                        "There's much to be done before and during the fair."),
                sj, sj2);
    }

    @Override
    protected void connectScenesToJunctions(List<QuestScene> scenes, List<QuestJunction> junctions) {
        scenes.get(0).get(0).connectSuccess(scenes.get(1).get(0));
        scenes.get(1).get(0).connectFail(scenes.get(2).get(0));
        scenes.get(1).get(0).connectSuccess(junctions.get(1));
        scenes.get(2).get(0).connectSuccess(junctions.get(2), QuestEdge.VERTICAL);
        scenes.get(3).get(0).connectSuccess(scenes.get(4).get(0));
        scenes.get(4).get(0).connectSuccess(scenes.get(5).get(0));
        scenes.get(5).get(0).connectSuccess(getSuccessEndingNode());
    }

    @Override
    public MyColors getBackgroundColor() {
        return MyColors.GREEN;
    }

    private static void payOutFromSoloSkillCheckSubScene(Model model, QuestState state) {
        int gold = 50;
        state.println("You received " + gold + " gold!");
        model.getParty().addToGold(gold);
    }

    private static class PayOutSoloSkillCheckSubScene extends SoloSkillCheckSubScene {
        public PayOutSoloSkillCheckSubScene(int col, int row, Skill skill, int diff, String talk) {
            super(col, row, skill, diff, talk);
        }

        @Override
        protected MyColors getSuccessEdgeColor() {
            return MyColors.WHITE;
        }

        @Override
        public boolean performSkillCheck(Model model, QuestState state, Skill skill, int difficulty) {
            boolean toReturn = super.performSkillCheck(model, state, skill, difficulty);
            if (toReturn) {
                payOutFromSoloSkillCheckSubScene(model, state);
            }
            return toReturn;
        }

        @Override
        protected QuestEdge getEdgeToReturn(boolean skillCheckWasSuccessful) {
            return getSuccessEdge();
        }
    }

    private static class PayOutCollaborativeSkillCheckSubScene extends CollaborativeSkillCheckSubScene {
        public PayOutCollaborativeSkillCheckSubScene(int col, int row, Skill skill, int diff, String talk) {
            super(col, row, skill, diff, talk);
        }

        @Override
        protected MyColors getSuccessEdgeColor() {
            return MyColors.WHITE;
        }

        @Override
        public boolean performSkillCheck(Model model, QuestState state, Skill skill, int difficulty) {
            boolean toReturn = super.performSkillCheck(model, state, skill, difficulty);
            if (toReturn) {
                int gold = model.getParty().size() * 10;
                state.println("You received " + gold + " gold!");
                model.getParty().addToGold(gold);
            }
            return toReturn;
        }

        @Override
        protected QuestEdge getEdgeToReturn(boolean skillCheckWasSuccessful) {
            return getSuccessEdge();
        }
    }

    private static class MeetLordJunction extends SpriteDecorativeJunction {
        public MeetLordJunction(int col, int row) {
            super(col, row, Classes.NOB.getAvatar(Race.NORTHERN_HUMAN, APPEARANCE), "Meet Lord");
        }

        @Override
        public QuestEdge run(Model model, QuestState state) {
            if (model.getCurrentHex().getLocation() instanceof UrbanLocation) {
                UrbanLocation loc = (UrbanLocation) model.getCurrentHex().getLocation();
                model.getParty().addSummon(loc);
                state.println("You have been introduced to " + loc.getLordName() + ". You should visit later!");
            }
            return getConnection(0);
        }
    }

    @Override
    public List<QuestBackground> getBackgroundSprites() {
        return BG_SPRITES;
    }
    private static final Sprite32x32 grass = new Sprite32x32("grass", "combat.png", 0x13,
            MyColors.GREEN, MyColors.LIGHT_GREEN, MyColors.CYAN, MyColors.CYAN);
    private static final Sprite32x32 lstall = new Sprite32x32("lstall", "quest.png", 0x68,
            MyColors.GREEN, MyColors.DARK_BROWN, MyColors.BEIGE, MyColors.BLUE);
    private static final Sprite32x32 rstall = new Sprite32x32("rstall", "quest.png", 0x69,
            MyColors.GREEN, MyColors.DARK_BROWN, MyColors.BEIGE, MyColors.RED);
    private static final Sprite32x32 welLeft = new Sprite32x32("welleft", "quest.png", 0x6A,
            MyColors.GREEN, MyColors.DARK_BROWN, MyColors.PINK, MyColors.BLACK);
    private static final Sprite32x32 welRight = new Sprite32x32("wellright", "quest.png", 0x6B,
            MyColors.GREEN, MyColors.DARK_BROWN, MyColors.PINK, MyColors.BLACK);
    private static final Sprite32x32 stageleft = new Sprite32x32("stageleft", "quest.png", 0x58,
            MyColors.GREEN, MyColors.BROWN, MyColors.RED, MyColors.DARK_GRAY);
    private static final Sprite32x32 stageright = new Sprite32x32("stageright", "quest.png", 0x59,
            MyColors.GREEN, MyColors.BROWN, MyColors.RED, MyColors.DARK_GRAY);
    private static final Sprite32x32 achery = new Sprite32x32("archery", "quest.png", 0x5A,
            MyColors.GREEN, MyColors.WHITE, MyColors.RED, MyColors.BROWN);

    private static final List<QuestBackground> BG_SPRITES = makeBackgroundSprites();

    private static List<QuestBackground> makeBackgroundSprites() {
        Sprite[][] sprites = new Sprite[][]{
                {grass, grass, grass, welLeft, welRight, grass, grass, grass},
                {grass, grass, grass, grass, grass, grass, grass, grass},
                {lstall, grass, grass, grass, grass, grass, rstall, grass},
                {grass, grass, grass, grass, grass, grass, grass, rstall},
                {stageleft, stageright, grass, grass, grass, grass, grass, grass},
                {grass, grass, grass, grass, grass, grass, grass, grass},
                {lstall, grass, grass, grass, grass, grass, grass, grass},
                {grass, grass, grass, grass, grass, grass, grass, grass},
                {grass, grass, grass, grass, achery, grass, grass, grass}
        };
        List<QuestBackground> result = new ArrayList<>();
        for (int y = 0; y < sprites.length; y++) {
            for (int x = 0; x < sprites[0].length; ++x) {
                result.add(new QuestBackground(new Point(x, y), sprites[y][x]));
            }
        }
        return result;
    }

    private static class ArcherySubScene extends QuestSubScene {
        private final String leaderTalk;

        public ArcherySubScene(int col, int row, String leaderTalk) {
            super(col, row);
            this.leaderTalk = leaderTalk;
        }

        @Override
        public void drawYourself(Model model, int xPos, int yPos) {
            Sprite sprite = SoloSkillCheckSubScene.SPRITE;
            model.getScreenHandler().register(sprite.getName(), new Point(xPos, yPos), sprite, 1);
        }

        @Override
        public String getDescription() {
            return "Compete in archery contest.";
        }

        @Override
        public QuestEdge run(Model model, QuestState state) {
            if (model.getParty().size() > 1) {
                model.getParty().partyMemberSay(model, model.getParty().getLeader(), leaderTalk);
            }
            state.print("Who shall compete in the archery contest? ");
            GameCharacter shooter = model.getParty().partyMemberInput(model, state, model.getParty().getPartyMember(0));
            BowWeapon bowToUse = ArcheryState.getCharactersBowOrDefault(shooter);
            ArcheryState archeryState = new ArcheryState(model, shooter, bowToUse, ArcheryState.SHORT_DISTANCE);
            List<GameCharacter> opponents = List.of(
                    new GameCharacter("Tamara", "Didriksson", Race.NORTHERN_HUMAN, Classes.MAR,
                        PortraitSubView.makeRandomPortrait(Classes.MAR, Race.NORTHERN_HUMAN, true),
                        Classes.NO_OTHER_CLASSES,
                            new Equipment(new CompetitionBow())),
                    new GameCharacter("Golbert", "Volantis", Race.DARK_ELF, Classes.FOR,
                        PortraitSubView.makeRandomPortrait(Classes.FOR, Race.DARK_ELF, false),
                        Classes.NO_OTHER_CLASSES,
                            new Equipment(new YewBow())));
            MyLists.forEach(opponents, (GameCharacter gc) -> gc.setLevel(4));
            archeryState.addNPCShooters(opponents);
            archeryState.run(model);
            state.print("Press enter to continue.");
            state.waitForReturn();
            CollapsingTransition.transition(model, state.getSubView());
            Map<GameCharacter, Integer> points = archeryState.getPoints();
            if (points.get(shooter) >= points.get(opponents.get(0)) && points.get(shooter) >= points.get(opponents.get(1))) {
                state.println("You are the winner of the archery contest!");
                model.getParty().partyMemberSay(model, shooter, "Yes! I rock!");
                payOutFromSoloSkillCheckSubScene(model, state);
                return getSuccessEdge();
            } else {
                state.println("Unfortunately, you did not win the archery contest.");
            }
            model.getParty().partyMemberSay(model, shooter, "Darn! I really thought I could do it.");
            return getSuccessEdge();
        }

        @Override
        protected MyColors getSuccessEdgeColor() {
            return MyColors.WHITE;
        }

        @Override
        public String getDetailedDescription() {
            return "Bows H";
        }
    }
}
