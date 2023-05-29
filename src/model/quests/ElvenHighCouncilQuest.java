package model.quests;

import model.Model;
import model.characters.GameCharacter;
import model.characters.appearance.CharacterAppearance;
import model.classes.Classes;
import model.classes.Skill;
import model.classes.SkillCheckResult;
import model.enemies.ElvenGuardEnemy;
import model.quests.scenes.*;
import model.races.Race;
import model.states.GameState;
import model.states.QuestState;
import util.MyRandom;
import view.MyColors;
import view.sprites.Sprite;
import view.sprites.Sprite32x32;
import view.subviews.GrassCombatTheme;
import view.subviews.KeepSubView;
import view.subviews.PortraitSubView;
import view.widget.QuestBackground;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ElvenHighCouncilQuest extends Quest {
    private static final String INTRO = "War! An invasion of orcs is threatening the heartland. " +
            "A delegation has been sent to the High Elves to convince them to join the alliance " +
            "and enter the war. The elves however, seem determined to stay out of the conflict. " +
            "A human lord has asked you to sway the minds of the elves.";

    private static final String ENDING = "The elven nobles have consented and agreed to send troops " +
            "to stifle the orc threat. The human lords thank you for your help and reward you.";
    private static final int PERSUADE_DIFFICULTY = 16;
    private static final CharacterAppearance PORTRAIT = PortraitSubView.makeRandomPortrait(Classes.NOB, Race.NORTHERN_HUMAN);
    private static final Sprite WALL = new Sprite32x32("hallwall", "world_foreground.png", 0x57,
            MyColors.GRAY, MyColors.BEIGE, MyColors.GREEN);
    private static final Sprite FLOOR = new Sprite32x32("hallfloor", "world_foreground.png", 0x56,
            MyColors.GRAY, MyColors.WHITE, MyColors.CYAN);
    private static final Sprite LOWER_WALL = new Sprite32x32("outerwall", "quest.png", 0x27,
            MyColors.BLACK, MyColors.GRAY_RED, MyColors.PINK, MyColors.GRAY);
    private static final Sprite LOWER_WALL_CORNER = new Sprite32x32("lowerwallcorner", "quest.png", 0x37,
            MyColors.BLACK, MyColors.GRAY_RED, MyColors.GREEN, MyColors.GRAY);
    private static final Sprite LEFT_WALL = new Sprite32x32("leftwall", "quest.png", 0x3C,
            MyColors.BLACK, MyColors.GREEN, MyColors.WHITE, MyColors.GRAY);
    private static final Sprite LEFT_WALL_DOOR = new Sprite32x32("leftwall", "quest.png", 0x3D,
            MyColors.BLACK, MyColors.GRAY_RED, MyColors.WHITE, MyColors.GREEN);
    private static final Sprite LEFT_WALL_RUG = new Sprite32x32("leftwallrug", "quest.png", 0x3C,
            MyColors.BLACK, MyColors.GREEN, MyColors.DARK_RED, MyColors.GRAY);
    private static final Sprite WALL_CORNER = new Sprite32x32("leftwall", "quest.png", 0x3B,
            MyColors.BLACK, MyColors.BEIGE, MyColors.GREEN, MyColors.GRAY);
    private static final Sprite[] ELF_NOBLES = makeSpectators();
    private static final List<QuestBackground> BACKGROUND = makeBackground();
    private static final List<QuestBackground> FOREGROUND = makeForeground();

    private boolean foughtGuards = false;

    public ElvenHighCouncilQuest() {
        super("Elven High Council", "Human Lords", QuestDifficulty.HARD, 1, 35, 35, INTRO, ENDING);
    }

    @Override
    public CharacterAppearance getPortrait() {
        return PORTRAIT;
    }

    @Override
    public boolean drawTownOrCastleInBackground() {
        return true;
    }

    @Override
    protected List<QuestScene> buildScenes() {
        return List.of(
                new QuestScene("Find Suitable Gift", List.of(
                        new CountingCollaborativeSkillCheckSubScene(1, 4, Skill.SeekInfo, 13,
                        "We should find out more about these elven nobles, to avoid an awkward faux pas."),
                        new CountingSoloSkillCheckSubScene(1, 5, Skill.Blades, 9,
                        "Let's find the finest sword available. Elves are fascinated by metallurgy."))),
                new QuestScene("Elven Guards", List.of(
                        new CollectiveSkillCheckSubScene(4, 1, Skill.Sneak, 6,
                                "Maybe we'll just sneak in while they aren't looking."),
                        new ElvenGuardsCombatSubScene(4, 2),
                        new SoloSkillCheckSubScene(4, 3, Skill.Persuade, 10,
                                "Maybe one of us can talk some sense into them."))),
                new QuestScene("Elven High King", List.of(
                        new PersuadeHighKingSubScene(6, 6)
                )));
    }

    @Override
    protected List<QuestJunction> buildJunctions(List<QuestScene> scenes) {
        QuestStartPoint qsp = new QuestStartPoint(List.of(
                new QuestEdge(scenes.get(0).get(0), QuestEdge.VERTICAL),
                new QuestEdge(scenes.get(0).get(1), QuestEdge.VERTICAL)),
                "What if we get the elves a bribe... uh... I mean a gift. " +
                "To break the ice, who doesn't like presents?");
        QuestDecisionPoint qdp = new QuestDecisionPoint(3, 2,
                List.of(new QuestEdge(scenes.get(1).get(0), QuestEdge.VERTICAL),
                        new QuestEdge(scenes.get(1).get(1)),
                        new QuestEdge(scenes.get(1).get(2), QuestEdge.VERTICAL)),
                "These guards aren't too keen on letting us into the council meeting. How to deal with them?");
        SimpleJunction sj = new SimpleJunction(5, 2, new QuestEdge(scenes.get(2).get(0)));
        return List.of(qsp, qdp, sj);
    }

    @Override
    protected void connectScenesToJunctions(List<QuestScene> scenes, List<QuestJunction> junctions) {
        scenes.get(0).get(0).connectSuccess(junctions.get(1), QuestEdge.VERTICAL);
        scenes.get(0).get(1).connectSuccess(junctions.get(1), QuestEdge.VERTICAL);

        scenes.get(1).get(0).connectFail(scenes.get(1).get(1));
        scenes.get(1).get(0).connectSuccess(junctions.get(2));
        scenes.get(1).get(1).connectSuccess(junctions.get(2));
        scenes.get(1).get(2).connectFail(scenes.get(1).get(1));
        scenes.get(1).get(2).connectSuccess(junctions.get(2));

        scenes.get(2).get(0).connectFail(getFailEndingNode());
        scenes.get(2).get(0).connectSuccess(getSuccessEndingNode());
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
    public List<QuestBackground> getDecorations() {
        return FOREGROUND;
    }

    private class ElvenGuardsCombatSubScene extends CombatSubScene {
        public ElvenGuardsCombatSubScene(int col, int row) {
            super(col, row, List.of(new ElvenGuardEnemy('A'), new ElvenGuardEnemy('A'), new ElvenGuardEnemy('A')));
        }

        @Override
        protected String getCombatDetails() {
            return "Elven Guards";
        }

        @Override
        public QuestEdge run(Model model, QuestState state) {
            foughtGuards = true;
            return super.run(model, state);
        }
    }

    private class PersuadeHighKingSubScene extends SkillQuestSubScene {
        public PersuadeHighKingSubScene(int col, int row) {
            super(col, row, "We did it, we're in. The elven council...", Skill.Persuade, PERSUADE_DIFFICULTY);
        }

        @Override
        public void drawYourself(Model model, int xPos, int yPos) {
            model.getScreenHandler().register(SoloSkillCheckSubScene.SPRITE.getName(), new Point(xPos, yPos),
                    SoloSkillCheckSubScene.SPRITE, 1);
        }

        @Override
        protected String getDescriptionType() {
            return "Solo";
        }

        @Override
        public boolean performSkillCheck(Model model, QuestState state, Skill skill, int difficulty) {
            int diff = PERSUADE_DIFFICULTY;
            if (foughtGuards) {
                model.getParty().randomPartyMemberSay(model, List.of("They don't seem too happy."));
                model.getParty().randomPartyMemberSay(model, List.of("Maybe it's because we beat up those guards back there..."));
                diff += 4;
            }

            if (state.getCounter() > 0) {
                state.leaderSay("Uhm, please accept this humble gift!");
                state.println("The elven nobles raise their eyebrows and seem pleased, but remain silent, waiting for you to speak.");
                diff -= 4;
            }
            state.leaderSay("We're only going to get one shot at this. Let's choose our words well.");
            model.getParty().randomPartyMemberSay(model, List.of("The elves may be more willing to listen if somebody of their kin handled the talking."));
            state.print("Which party member should perform the Solo Persuade " + diff + " check?");
            model.getSpellHandler().acceptSkillBoostingSpells(Skill.Persuade);
            GameCharacter talker = model.getParty().partyMemberInput(model, state, model.getParty().getLeader());
            int bonus = 0;
            if (talker.getRace().id() == Race.HIGH_ELF.id()) {
                state.println("The elven nobles smile and seem to relax as " + talker.getFirstName() + ", a high elf, steps forward to speak.");
                bonus = 4;
            } else if (talker.getRace().id() == Race.DARK_ELF.id() || talker.getRace().id() == Race.WOOD_ELF.id()) {
                state.println("The elven nobles seem to relax a little as " + talker.getFirstName() + ", an elf, steps forward to speak.");
                bonus = 2;
            } else {
                state.println("The elven nobles seem tense and suspicious " + talker.getFirstName() + ", a non-elf, steps forward to speak.");
            }
            SkillCheckResult result = model.getParty().doSkillCheckWithReRoll(model, state, talker, Skill.Persuade, diff, 20, bonus);
            model.getSpellHandler().unacceptSkillBoostingSpells(Skill.Persuade);
            return result.isSuccessful();
        }

        @Override
        public String getDetailedDescription() {
            return "Persuade H*";
        }
    }

    private static class CountingCollaborativeSkillCheckSubScene extends CollaborativeSkillCheckSubScene {
        public CountingCollaborativeSkillCheckSubScene(int col, int row, Skill skill, int diff, String talk) {
            super(col, row, skill, diff, talk);
        }

        @Override
        public boolean performSkillCheck(Model model, QuestState state, Skill skill, int difficulty) {
            boolean inner = super.performSkillCheck(model, state, skill, difficulty);
            if (inner) {
                state.increaseQuestCounter();
            }
            return inner;
        }

        @Override
        protected QuestEdge getEdgeToReturn(boolean skillCheckWasSuccessful) {
            return getSuccessEdge();
        }

        @Override
        protected MyColors getSuccessEdgeColor() {
            return MyColors.WHITE;
        }
    }

    private static class CountingSoloSkillCheckSubScene extends SoloSkillCheckSubScene {
        public CountingSoloSkillCheckSubScene(int col, int row, Skill skill, int diff, String talk) {
            super(col, row, skill, diff, talk);
        }

        @Override
        public boolean performSkillCheck(Model model, QuestState state, Skill skill, int difficulty) {
            boolean inner = super.performSkillCheck(model, state, skill, difficulty);
            if (inner) {
                state.increaseQuestCounter();
            }
            return inner;
        }

        @Override
        protected QuestEdge getEdgeToReturn(boolean skillCheckWasSuccessful) {
            return getSuccessEdge();
        }

        @Override
        protected MyColors getSuccessEdgeColor() {
            return MyColors.WHITE;
        }
    }

    private static List<QuestBackground> makeBackground() {
        Random random = new Random(9847);
        List<QuestBackground> list = new ArrayList<>();
        list.add(new QuestBackground(new Point(2, 0), WALL_CORNER));
        for (int i = 3; i < 8; ++i) {
            list.add(new QuestBackground(new Point(i, 0), WALL));
        }
        for (int y = 1; y < 4; ++y) {
            if (y == 2) {
                list.add(new QuestBackground(new Point(2, y), LEFT_WALL_DOOR));
            } else {
                list.add(new QuestBackground(new Point(2, y), LEFT_WALL));
            }
            for (int i = 3; i < 8; ++i) {
                list.add(new QuestBackground(new Point(i, y), FLOOR));
            }
        }
        list.add(new QuestBackground(new Point(2, 4), WALL_CORNER));
        for (int i = 3; i < 8; ++i) {
            if (i == 6) {
                list.add(new QuestBackground(new Point(i, 4), FLOOR));
            } else {
                list.add(new QuestBackground(new Point(i, 4), WALL));
            }
        }
        for (int y = 5; y < 8; ++y) {
            list.add(new QuestBackground(new Point(2, y), LEFT_WALL_RUG));
            for (int i = 3; i < 8; ++i) {
                list.add(new QuestBackground(new Point(i, y), MasqueradeQuest.RUG));
            }
        }
        list.add(new QuestBackground(new Point(2, 8), LOWER_WALL_CORNER));
        for (int i = 3; i < 8; ++i) {
            list.add(new QuestBackground(new Point(i, 8), LOWER_WALL));
        }

        for (int y = 0; y < 9; ++y) {
            for (int x = 0; x < 2; ++x) {
                Point p = new Point(x, y);
                list.add(new QuestBackground(p,
                        GrassCombatTheme.grassSprites[random.nextInt(GrassCombatTheme.grassSprites.length)]));
            }
        }
        return list;
    }

    private static Sprite[] makeSpectators() {
        return new Sprite[]{
                new Sprite32x32("elfnob1", "quest.png", 0x87,
                        MyColors.BLACK, MyColors.BEIGE, Race.HIGH_ELF.getColor(), MyColors.BLUE),
                new Sprite32x32("elfnob2", "quest.png", 0x87,
                        MyColors.BLACK, MyColors.LIGHT_RED, Race.HIGH_ELF.getColor(), MyColors.GRAY),
                new Sprite32x32("elfnob3", "quest.png", 0x87,
                        MyColors.BLACK, MyColors.CYAN, Race.HIGH_ELF.getColor(), MyColors.GOLD),
                new Sprite32x32("elfnob4", "quest.png", 0x87,
                        MyColors.BLACK, MyColors.BROWN, Race.HIGH_ELF.getColor(), MyColors.ORANGE),

        };
    }


    private static List<QuestBackground> makeForeground() {
        List<QuestBackground> list = new ArrayList<>();
        list.add(new QuestBackground(new Point(3, 5), ELF_NOBLES[0], true));
        list.add(new QuestBackground(new Point(4, 4), ELF_NOBLES[1], false));
        list.add(new QuestBackground(new Point(5, 4), ELF_NOBLES[2], false));
        list.add(new QuestBackground(new Point(3, 6), ELF_NOBLES[3], true));
        list.add(new QuestBackground(new Point(7, 5), ELF_NOBLES[0], true));
        list.add(new QuestBackground(new Point(7, 6), ELF_NOBLES[1], true));
        list.add(new QuestBackground(new Point(3, 0), KeepSubView.COLUMN, false));
        list.add(new QuestBackground(new Point(5, 1), KeepSubView.PLANT, true));
        list.add(new QuestBackground(new Point(7, 0), KeepSubView.COLUMN, false));

        return list;
    }
}
